package as.leap.maxwon.docs.service;

import as.leap.maxwon.docs.common.entity.DocsApiFolder;
import as.leap.maxwon.docs.common.entity.DocsProject;
import as.leap.maxwon.docs.common.mongo.MongoFactory;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Singleton
public class ApiFolderService {
    private MongoFactory mongoFactory;
    private MongoCollection apiFolderCollection;

    @Inject
    public ApiFolderService(MongoFactory mongoFactory){
        this.mongoFactory = mongoFactory;
        this.apiFolderCollection = mongoFactory.getCollection("docs_api_folder");
    }

    public Document createProject(DocsApiFolder apiFolder){
        Document document = MongoFactory.getInsertDocument(apiFolder);
        this.apiFolderCollection.insertOne(document);
        return new Document("id",document.getObjectId("_id").toString());
    }

    public void deleteProject(String id){
        this.apiFolderCollection.deleteOne(MongoFactory.getQuery_Id(id));
    }

    public void updateProject(String id, Map map){
        this.apiFolderCollection.updateOne(MongoFactory.getQuery_Id(id),MongoFactory.MapToUpdateSet(map),MongoFactory.getDefaultUpdateOptions());
    }

    public List<Document> findApiFolder(String where,String order,int skip,int limit,String projectId){
        List<Document> result = new ArrayList<>();
        this.apiFolderCollection.find(Document.parse(where).append(DocsApiFolder.PROJECT_ID,projectId)).skip(skip).limit(limit).sort(Document.parse(order)).into(result);
        return result;
    }
}
