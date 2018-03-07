package as.leap.maxwon.docs.test;

import com.google.common.base.Charsets;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.io.IOException;
import java.net.URL;


/**
 * Created by ben on 3/14/16.
 * 11:51 AM
 * <p>
 * benkris1@126.com
 */
@RunWith(VertxUnitRunner.class)
public class Bootstrap {
    private Logger logger = LoggerFactory.getLogger(Bootstrap.class);
    private Vertx vertx;

    @Before
    public void before() {
        VertxOptions options = new VertxOptions();
        options.setBlockedThreadCheckInterval(100000000);
        this.vertx = Vertx.vertx(options);

    }

    @Test(timeout = 1000000000l)
    public void start(TestContext context) throws IOException {
        Async async = context.async();
        URL url=Thread.currentThread().getContextClassLoader().getResource("config.json");
        String config= IOUtils.toString(url, Charsets.UTF_8);
        JsonObject jsonObject = new JsonObject(config);
        deployVerticle("as.leap.maxwon.docs.server.DocsVerticle", jsonObject)
                .subscribe(aBoolean -> {
                    System.out.println(" MaWon Ana server start success [" + aBoolean + "]");
                });
    }

    protected Observable<Boolean> deployVerticle(final String className, final JsonObject config) {

        return Observable.create(subscriber -> {
            DeploymentOptions options = new DeploymentOptions();
            options.setConfig(config);
            vertx.deployVerticle(className, options, event -> {
                if (event.succeeded()) {
                    logger.info("Deploy [" + className + "] success.");
                    subscriber.onNext(Boolean.TRUE);
                } else {
                    subscriber.onError(event.cause());
                }
                subscriber.onCompleted();
            });
        });
    }

}