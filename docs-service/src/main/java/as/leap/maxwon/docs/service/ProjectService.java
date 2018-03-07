package as.leap.maxwon.docs.service;

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
public class ProjectService {
    private MongoFactory mongoFactory;
    private MongoCollection projectCollection;

    @Inject
    public ProjectService(MongoFactory mongoFactory){
        this.mongoFactory = mongoFactory;
        this.projectCollection = mongoFactory.getCollection("docs_project");
    }

    public Document createProject(DocsProject docsProject){
        Document document = MongoFactory.getInsertDocument(docsProject);
        this.projectCollection.insertOne(document);
        return new Document("id",document.getObjectId("_id").toString());
    }

    public void deleteProject(String id){
        this.projectCollection.deleteOne(MongoFactory.getQuery_Id(id));
    }

    public void updateProject(String id, Map map){
        this.projectCollection.updateOne(MongoFactory.getQuery_Id(id),MongoFactory.MapToUpdateSet(map),MongoFactory.getDefaultUpdateOptions());
    }

    public List<Document> findProduct(String where,String order,int skip,int limit){
        List<Document> result = new ArrayList<>();
        this.projectCollection.find(Document.parse(where)).skip(skip).limit(limit).sort(Document.parse(order)).into(result);
        return result;
    }
}
