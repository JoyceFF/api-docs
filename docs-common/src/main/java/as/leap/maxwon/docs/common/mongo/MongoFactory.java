package as.leap.maxwon.docs.common.mongo;

import as.leap.maxwon.docs.common.Jsons;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.mongodb.client.model.Updates.*;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Singleton
public class MongoFactory {

    private  MongoDatabase database;

    @Inject
    public MongoFactory(Vertx vertx){
        JsonObject mongoConfig = vertx.getOrCreateContext().config().getJsonObject("mongo");
        MongoClient mongoClient = new MongoClient(mongoConfig.getString("host") , mongoConfig.getInteger("port"));
        CodecRegistry registry = CodecRegistries.fromCodecs(new DocumentCodec());
        CodecRegistry pojoCodecRegistry = fromRegistries(registry,MongoClient.getDefaultCodecRegistry());
        this.database = mongoClient.getDatabase(mongoConfig.getString("database")).withCodecRegistry(pojoCodecRegistry);
    }

   public  MongoCollection getCollection(String collectionName){
       return this.database.getCollection(collectionName);
   }

   public static Document getInsertDocument(Object object){
      Map map = Jsons.objectToMap(object);
      map.remove("id");
      map.remove("createdAt");
      return new Document(map).append("createdAt",System.currentTimeMillis());
   }

    /**
     * map  转换为 update bson $set key value
     * @param map
     * @return
     */
   public static Bson MapToUpdateSet(Map<String,Object> map){
       map.remove("updatedAt");
       List<Bson> array = new ArrayList<>();
       for (String key : map.keySet()) {
           array.add(set(key,map.get(key)));
       }
       array.add(currentDate("updatedAt"));
       return combine(array);
   }

   public static Document getQuery_Id(String id){
       return new Document("_id",new ObjectId(id));
   }

   public static UpdateOptions getDefaultUpdateOptions(){
       return new UpdateOptions().upsert(false).bypassDocumentValidation(true);
   }
}
