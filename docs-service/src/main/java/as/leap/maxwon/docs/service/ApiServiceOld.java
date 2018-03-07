package as.leap.maxwon.docs.service;

import as.leap.maxwon.docs.common.*;
import as.leap.maxwon.docs.common.entity.MethodDetail;
import as.leap.maxwon.docs.common.entity.DocsModel;
import as.leap.maxwon.docs.common.entity.DocsModelDetails;
import as.leap.maxwon.docs.common.entity.ParamDescribe;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import org.apache.commons.collections4.MapUtils;

import java.io.File;
import java.util.*;

public class ApiServiceOld {

    @Inject private ModelUtils modelUtils;

    public List<Map> getResourceFileNames() {
        List<File> resources = Utils.findResources();
        List<Map> result = new ArrayList<>();
        DataUtils.clearResources();

        for (int i = 0; i < resources.size(); i++) {
            File file = resources.get(i);
            Map map = new HashMap();
            map.put("id", i);
            map.put("name", file.getName().replace(".class", "").replace(".java", ""));
            result.add(map);
            DataUtils.addResources(i + "", file);
        }

        return result;
    }

    public List<Map> getMethod(String resourceId) {
        List<Map> result = new ArrayList<>();
        try {
            File file = DataUtils.getResourceFile(resourceId);
            List<MethodDeclaration> list = Utils.getMethods(file);
            for (int i = 0; i < list.size(); i++) {
                MethodDeclaration method = list.get(i);
                Map map = new HashMap();
                map.put("id", i);
                map.put("name", method.getName().asString());
                result.add(map);
                DataUtils.addMethod(i + "", method);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public MethodDetail getMethodDetail(String methodId,Map headers, Map param) {
        MethodDeclaration method = DataUtils.getMethod(methodId);
        MethodDetail methodDetail = new MethodDetail();
        String name = MethodUtils.getName(method);
        String http = MethodUtils.getHttp(method);
        String permission = MethodUtils.getPermission(method);
        String url = MethodUtils.getURL(method);
        List<ParamDescribe> params = MethodUtils.getParams(method);

        if (param.isEmpty() && http.equals("GET")){
            param = DataUtils.paramDefault;
        }

        String httpUrl = MethodUtils.formatUrl(url, param, params);
        Map body = MapUtils.getMap(param, "body", Maps.newHashMap());
        if (headers.isEmpty()) {
           headers = DataUtils.getDefaultHeader(permission);
        }

        String curl = MethodUtils.curl(httpUrl, headers, http, body);
        String javaScript = MethodUtils.javaScript(httpUrl, headers, http, body);
        String java = MethodUtils.java(httpUrl, headers, http, body);
        String result = MethodUtils.result(httpUrl, headers, http, body);

        methodDetail.setId(methodId);
        methodDetail.setHttp(http);
        methodDetail.setName(name);
        methodDetail.setPermission(permission);
        methodDetail.setUrl(url);
        methodDetail.setParams(params);
        methodDetail.setShell(curl);
        methodDetail.setJavascript(javaScript);
        methodDetail.setJava(java);
        methodDetail.setResult(result);
        return methodDetail;
    }

    public MethodDetail getResult(String url, Map<String, String> headers, String http, Map param,String permission,List<ParamDescribe> params){
        if (param.isEmpty() && http.equals("GET")){
            param = DataUtils.paramDefault;
        }

        String httpUrl = MethodUtils.formatUrl(url, param, params);
        Map body = MapUtils.getMap(param, "body", Maps.newHashMap());
        if (headers.isEmpty()) {
            headers = DataUtils.getDefaultHeader(permission);
        }

        String curl = MethodUtils.curl(httpUrl, headers, http, body);
        String javaScript = MethodUtils.javaScript(httpUrl, headers, http, body);
        String java = MethodUtils.java(httpUrl, headers, http, body);
        String result = MethodUtils.result(httpUrl, headers, http, body);

        MethodDetail methodDetail = new MethodDetail();
        methodDetail.setJava(java);
        methodDetail.setJavascript(javaScript);
        methodDetail.setShell(curl);
        methodDetail.setResult(result);
        return methodDetail;
    }

    public Map<String,String> getDefaultHeader(String permission){
        return DataUtils.getDefaultHeader(permission);
    }


    public List<DocsModel> getAllModel(){
       return this.modelUtils.getModel();
    }

    public void addModel(DocsModel model){
        this.modelUtils.addModel(model);
    }

    public void delModel(String id){
        this.modelUtils.delModel(id);
    }

    public void upModel(DocsModel model){
        this.modelUtils.updateModel(model);
    }

    public List<DocsModelDetails> getModelDetails(String modelId){
        return this.modelUtils.getModelDetails(modelId);
    }

    public void updateModelDetails(String modelId,List<DocsModelDetails> modelDetails){
        this.modelUtils.updateModelDetails(modelId,modelDetails);
    }
}
