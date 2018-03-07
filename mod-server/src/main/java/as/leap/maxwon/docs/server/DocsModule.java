package as.leap.maxwon.docs.server;

import as.leap.maxwon.docs.common.ModelUtils;
import as.leap.maxwon.docs.common.mongo.MongoFactory;
import as.leap.maxwon.docs.service.*;
import com.google.inject.AbstractModule;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import okhttp3.OkHttpClient;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

public class DocsModule extends AbstractModule{
    private Vertx vertx;
    public DocsModule(Vertx vertx){
        this.vertx = vertx;
    }
    @Override
    protected void configure() {

       final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .build();

        JsonObject config = this.vertx.getOrCreateContext().config();
        JsonObject redisConfig = config.getJsonObject("redis");
        Jedis jedis = new Jedis(redisConfig.getString("host"),redisConfig.getInteger("port"));
        bind(Vertx.class).toInstance(this.vertx);
        bind(ApiServiceOld.class);
        bind(ProjectService.class);
        bind(ApiFolderService.class);
        bind(ModelService.class);
        bind(ApiService.class);
        bind(ModelUtils.class);
        bind(Jedis.class).toInstance(jedis);

        bind(MongoFactory.class);

        bind(OkHttpClient.class).toInstance(okHttpClient);
    }
}
