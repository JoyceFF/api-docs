package as.leap.maxwon.docs.server;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class DocsServer extends AbstractVerticle {

    private final static Logger logger = LoggerFactory.getLogger(DocsServer.class);
    private int port;

    private DocsHandler docsHandler;

    @Override
    public void init(Vertx vertx, Context context) {
        super.init(vertx, context);


        JsonObject config = this.vertx.getOrCreateContext().config();


        Injector  injector = Guice.createInjector(new DocsModule(vertx));
        docsHandler = injector.getInstance(DocsHandler.class);

        String port0 = config.getString("port");
        this.port = StringUtils.isBlank(port0) ? 5555 : Integer.valueOf(port0);
    }


    @Override
    public void start() throws Exception {
        final Router router = Router.router(this.vertx);

        router.route().handler(BodyHandler.create());

        router.route("/*").handler((ctx)->{
           if (ctx.request().method() == HttpMethod.OPTIONS){
               HttpUtils.send(ctx,Maps.newHashMap());
           }else{
               ctx.next();
           }
        });

        registerApi(router);

        vertx.createHttpServer().requestHandler(router::accept).listen(this.port, event -> {
            if (event.succeeded()) {
                logger.info("\n Http server start success listening for {}" , this.port);
            } else {
                logger.error("Failed to start http server listening for {}" ,this.port, event.cause());
            }
        });
    }


    public void registerApi(Router router){
        router.route().handler(BodyHandler.create());
        router.route("/static/*").handler(StaticHandler.create().setWebRoot("./data/markdown"));
        router.get("/maxwon_docs/getAllResource").handler(this.docsHandler::getResource);
        router.get("/maxwon_docs/getAllMethod").handler(this.docsHandler::getMethods);
        router.post("/maxwon_docs/getMethodDetail").handler(this.docsHandler::getMethodDetail);
        router.post("/maxwon_docs/getAllMethodDetail").handler(this.docsHandler::getAllMethodDetail);
        router.get("/maxwon_docs/getDefaultHeader").handler(this.docsHandler::getDefaultHeader);
        router.post("/maxwon_docs/getResult").handler(this.docsHandler::getResult);
        router.get("/maxwon_docs/getAllMethodDetailProgress").handler(this.docsHandler::getAllMethodDetailProgress);


        //project
        router.post("/maxwon_docs/addProject").handler(this.docsHandler::addProject);
        router.put("/maxwon_docs/updateProject/:projectId").handler(this.docsHandler::updateProject);
        router.delete("/maxwon_docs/delProject/:projectId").handler(this.docsHandler::delProject);
        router.get("/maxwon_docs/findProject").handler(this.docsHandler::findProject);
        //apiFolder
        router.post("/maxwon_docs/addApiFolder").handler(this.docsHandler::addApiFolder);
        router.put("/maxwon_docs/updateApiFolder/:apiFolderId").handler(this.docsHandler::updateApiFolder);
        router.delete("/maxwon_docs/delApiFolder/:apiFolderId").handler(this.docsHandler::delApiFolder);
        router.get("/maxwon_docs/findApiFolder/:projectId").handler(this.docsHandler::findApiFolder);
        //api
        router.post("/maxwon_docs/addApi").handler(this.docsHandler::addApi);
        router.put("/maxwon_docs/updateOrCreateApi").handler(this.docsHandler::updateOrCreateApi);
        router.delete("/maxwon_docs/delApi/:apiId").handler(this.docsHandler::delApi);
        router.get("/maxwon_docs/findApi/:folderId").handler(this.docsHandler::findApi);
        router.get("/maxwon_docs/getSaveApiDetails/:folderId").handler(this.docsHandler::getSaveApiDetails);
        router.post("/maxwon_docs/getCurlResult").handler(this.docsHandler::getCurlResult);
        router.post("/maxwon_docs/getJavaSampleByCurl").handler(this.docsHandler::getJavaSampleByCurl);
        router.post("/maxwon_docs/getJavascriptSampleByCurl").handler(this.docsHandler::getJavascriptSampleByCurl);
        router.post("/maxwon_docs/checkCurl").handler(this.docsHandler::checkCurl);
        router.post("/maxwon_docs/getMarkdownString").handler(this.docsHandler::getMarkdownString);
        router.post("/maxwon_docs/generateMarkdown").handler(this.docsHandler::generateMarkdown);
        router.get("/maxwon_docs/findAllApiPermission").handler(this.docsHandler::findAllApiPermission);
        router.post("/maxwon_docs/createOrUpdatePermission").handler(this.docsHandler::createOrUpdatePermission);
        router.delete("/maxwon_docs/deleteApiPermission/:apiPermissionId").handler(this.docsHandler::delApiPermission);
        router.get("/maxwon_docs/findMarkdownTemplate").handler(this.docsHandler::findMarkdownTemplate);
        router.post("/maxwon_docs/markdownTemplate").handler(this.docsHandler::markdownTemplate);
        //model
        router.get("/maxwon_docs/getAllModel").handler(this.docsHandler::getAllModel);
        router.post("/maxwon_docs/addModel").handler(this.docsHandler::addModel);
        router.put("/maxwon_docs/updateModel/:modelId").handler(this.docsHandler::updateModel);
        router.delete("/maxwon_docs/delModel/:modelId").handler(this.docsHandler::delModel);
        router.get("/maxwon_docs/getModelDetailsByModelId/:modelId").handler(this.docsHandler::getModelDetailsByModelId);
        router.put("/maxwon_docs/createOrUpdateModelDetails/:modelId").handler(this.docsHandler::createOrUpdateModelDetails);
    }


}
