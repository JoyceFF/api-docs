import as.leap.maxwon.docs.common.Jsons;
import as.leap.maxwon.docs.common.entity.DocsProject;
import as.leap.maxwon.docs.common.mongo.MongoFactory;
import as.leap.maxwon.docs.common.mongo.DocumentCodec;
import com.google.common.collect.Maps;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoTest {
    private MongoDatabase database;

    Block<Object> printBlock = new Block<Object>() {
        @Override
        public void apply(final Object document) {
            System.out.println(Jsons.objectToJSONStr(document));
        }
    };

    @Before
    public void before() {
        MongoClient mongoClient = new MongoClient("0.0.0.0", 27017);
        CodecRegistry defaultCodecRegistry = MongoClient.getDefaultCodecRegistry();
        CodecRegistry registry = CodecRegistries.fromCodecs(new DocumentCodec());
        CodecRegistry pojoCodecRegistry = fromRegistries(registry,defaultCodecRegistry);

        this.database = mongoClient.getDatabase("api_docs").withCodecRegistry(pojoCodecRegistry);
    }

    @After
    public void after() {


    }

    @Test
    public void insertTest() {
        Document document = new Document("name", "maxwon3").append("describe", "描述");
        this.database.getCollection("docs_project").insertOne(document);
        System.out.println(document.toJson());
    }

    @Test
    public void findTest() {
        CodecRegistry registry = CodecRegistries.fromCodecs(new DocumentCodec());

        CodecRegistry pojoCodecRegistry = fromRegistries(registry);

        Document document = Document.parse("{\"name\":\"maxwon\"}");
        List<DocsProject> result = new ArrayList<>();
        this.database.getCollection("docs_project").withCodecRegistry(pojoCodecRegistry).find(document).forEach(printBlock);
    }

    @Test
    public void findIntoTest() {
        CodecRegistry registry = CodecRegistries.fromCodecs(new DocumentCodec());

        CodecRegistry pojoCodecRegistry = fromRegistries(registry);

        Document document = Document.parse("{\"name\":\"maxwon\"}");
        List<Document> result = new ArrayList<>();
        this.database.getCollection("docs_project").find(document).into(result);
    }

    @Test
    public void updateTest() {
        Map map = Maps.newLinkedHashMap();
        map.put("a", 1);
        map.put("b", "b");
        System.out.println(MongoFactory.MapToUpdateSet(map));
        this.database.getCollection("docs_project").updateOne(MongoFactory.getQuery_Id("5a2a0cfda16f0d9453f241ae"), MongoFactory.MapToUpdateSet(map), MongoFactory.getDefaultUpdateOptions());
    }
}
