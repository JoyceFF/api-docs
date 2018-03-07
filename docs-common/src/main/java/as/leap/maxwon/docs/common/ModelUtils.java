package as.leap.maxwon.docs.common;

import as.leap.maxwon.docs.common.entity.DocsModel;
import as.leap.maxwon.docs.common.entity.DocsModelDetails;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Singleton
public class ModelUtils {
    @Inject private Jedis redis;

    public List<DocsModel> getModel() {
        List<DocsModel> list = new ArrayList<>();
        ScanParams scanParams = new ScanParams();
        scanParams.match("model:*");
        scanParams.count(10000);

        for (String key : this.redis.scan("0", scanParams).getResult()) {
            if (key.indexOf("details") == -1){
                Map<String,String> map = this.redis.hgetAll(key);
                DocsModel model = Jsons.mapToObject(map,DocsModel.class);
                list.add(model);

            }
        }

       return list;
    }

    public void addModel(DocsModel model){
        Map map = Jsons.beanToMap(model);
        this.redis.hmset("model:"+model.getId(),map);
    }

    public void delModel(String modelId){
        this.redis.del("model:"+modelId);
    }

    public void updateModel(DocsModel model){
        Map map = Jsons.beanToMap(model);
        this.redis.hmset("model:"+model.getId(),map);
    }

    public List<DocsModelDetails> getModelDetails(String modelId){

        List<DocsModelDetails> list = new ArrayList<>();
        ScanParams scanParams = new ScanParams();
        scanParams.match("model:"+modelId+":details*");
        scanParams.count(10000);

        List<String> results = this.redis.scan("0", scanParams).getResult();

        for (String key : results) {
            Map<String,String> map = this.redis.hgetAll(key);
            DocsModelDetails modelDetails = Jsons.mapToObject(map,DocsModelDetails.class);
            list.add(modelDetails);
        }

        return list;
    }

    public void updateModelDetails(String modelId,List<DocsModelDetails> modelDetails){
        for (DocsModelDetails modelDetail : modelDetails) {
            modelDetail.setModelId(modelId);
            Map map = Jsons.beanToMap(modelDetail);
            this.redis.hmset("model:"+modelId+":details:"+ modelDetail.getId(),map);
        }
    }

}
