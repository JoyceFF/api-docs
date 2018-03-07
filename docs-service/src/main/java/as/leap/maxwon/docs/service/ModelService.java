package as.leap.maxwon.docs.service;

import as.leap.maxwon.docs.common.Jsons;
import as.leap.maxwon.docs.common.entity.DocsModel;
import as.leap.maxwon.docs.common.entity.DocsModelDetails;
import as.leap.maxwon.docs.common.mongo.MongoFactory;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelService {
    private MongoFactory mongoFactory;
    private MongoCollection modelCollection;
    private MongoCollection modelDetailsCollection;
     @Inject
    public ModelService(MongoFactory mongoFactory){
         this.mongoFactory = mongoFactory;
         this.modelCollection = mongoFactory.getCollection("docs_model");
         this.modelDetailsCollection = mongoFactory.getCollection("docs_model_details");
    }

    public List<Document> getModel(String where, String order, int skip, int limit){
        List<Document> result = new ArrayList<>();
        this.modelCollection.find(Document.parse(where)).skip(skip).limit(limit).sort(Document.parse(order)).into(result);
        return result;
    }

    public Document addModel(DocsModel model){
        Document document = MongoFactory.getInsertDocument(model);
        this.modelCollection.insertOne(document);
        return new Document("id",document.getObjectId("_id").toString());
    }

    public void delModel(String id){
        this.modelCollection.deleteOne(MongoFactory.getQuery_Id(id));
    }

    public void upModel(String id, Map map){
        this.modelCollection.updateOne(MongoFactory.getQuery_Id(id),MongoFactory.MapToUpdateSet(map),MongoFactory.getDefaultUpdateOptions());
    }

    public void createOrUpdateModelDetails(List<DocsModelDetails> modelDetails,List<DocsModelDetails> deleteDetails){
        for (DocsModelDetails modelDetail : modelDetails) {
            if (StringUtils.isNotEmpty(modelDetail.getId())){
                this.modelDetailsCollection.updateOne(MongoFactory.getQuery_Id(modelDetail.getId()),MongoFactory.MapToUpdateSet(Jsons.beanToMap(modelDetail)),MongoFactory.getDefaultUpdateOptions());
            }else{
                Document document = MongoFactory.getInsertDocument(modelDetail);
                this.modelDetailsCollection.insertOne(document);
            }
        }
        for (DocsModelDetails deleteDetail : deleteDetails) {
            this.modelDetailsCollection.deleteOne(MongoFactory.getQuery_Id(deleteDetail.getId()));
        }

    }

    public List<Document> getModelDetailsByModelId(String modelId){
        List<Document> result = new ArrayList<>();
        this.modelDetailsCollection.find(Document.parse("{}").append("modelId",modelId)).limit(1000).into(result);
        return result;
    }
}
