package as.leap.maxwon.docs.server;

import as.leap.maxwon.docs.common.entity.*;
import as.leap.maxwon.docs.common.Jsons;
import as.leap.maxwon.docs.common.Utils;
import as.leap.maxwon.docs.service.*;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.collections4.MapUtils;
import rx.util.async.Async;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class DocsHandler {
    private ApiServiceOld apiServiceOld;
    private ProjectService projectService;
    private ApiFolderService apiFolderService;
    private ModelService modelService;
    private ApiService apiService;

    @Inject
    public DocsHandler(ApiServiceOld apiServiceOld,
                       ProjectService projectService,
                       ApiFolderService apiFolderService,
                       ApiService apiService,
                       ModelService modelService){
        this.apiServiceOld = apiServiceOld;
        this.projectService = projectService;
        this.apiFolderService = apiFolderService;
        this.apiService = apiService;
        this.modelService = modelService;
    }

    public void getResource(RoutingContext routingContext){
        Async.start(()->{
            String path = HttpUtils.getParam(routingContext,"path");
            Utils.apiConfig.setProjectPath(path);
            List<Map> list = apiServiceOld.getResourceFileNames();
            return list;
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void getMethods(RoutingContext routingContext){
        Async.start(()->{
            String resourceId = HttpUtils.getParam(routingContext,"resourceId");
            List<Map> list = apiServiceOld.getMethod(resourceId);
            return list;
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void getMethodDetail(RoutingContext routingContext){
        Async.start(()->{
            JsonObject body = routingContext.getBodyAsJson();
            String methodId = MapUtils.getString(body.getMap(),"methodId");
            MethodDetail methodDetail = apiServiceOld.getMethodDetail(methodId, Maps.newHashMap(),Maps.newHashMap());
            return methodDetail;
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    private Map<String,Integer> getAllMethodDetailProgressMap = new HashMap();


    public void getAllMethodDetail(RoutingContext routingContext){
        Async.start(()->{
            JsonObject body = routingContext.getBodyAsJson();
            String resourceId = MapUtils.getString(body.getMap(),"resourceId");
            String uuid = MapUtils.getString(body.getMap(),"uuid");

            List<Map> list = apiServiceOld.getMethod(resourceId);
            List<MethodDetail> result = new ArrayList<>();

            int count = list.size();
            int index = 0;

            for (Map map : list) {
                String methodId = MapUtils.getString(map,"id");
                MethodDetail methodDetail = apiServiceOld.getMethodDetail(methodId,Maps.newHashMap(),Maps.newHashMap());
                result.add(methodDetail);
                index ++;
                int getAllMethodDetailProgress = new BigDecimal(index).divide(new BigDecimal(count),2,BigDecimal.ROUND_UP).multiply(new BigDecimal(100)).intValue();
                this.getAllMethodDetailProgressMap.put(uuid,getAllMethodDetailProgress);
            }

            Map map = new HashMap();
            map.put("methods",list);
            map.put("methodDetails",result);
            return map;
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void getAllMethodDetailProgress(RoutingContext routingContext){

        Async.start(()->{
            String uuid = HttpUtils.getParam(routingContext,"uuid");
            int getAllMethodDetailProgress = MapUtils.getInteger(this.getAllMethodDetailProgressMap,uuid,0);
            if (getAllMethodDetailProgress == 100){
                this.getAllMethodDetailProgressMap.remove(uuid);
            }
            return getAllMethodDetailProgress;
        },Utils.scheduler).subscribe(HttpUtils.send(routingContext));
    }

    public void getDefaultHeader(RoutingContext routingContext){

        Async.start(()->{
            String permission = HttpUtils.getParam(routingContext,"permission");
            Map<String,String> defaultHeader = apiServiceOld.getDefaultHeader(permission);
            return defaultHeader;
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void getResult(RoutingContext routingContext){

        Async.start(()->{

            Map body = routingContext.getBodyAsJson().getMap();
            String url = MapUtils.getString(body,"url");
            String http = MapUtils.getString(body,"http");
            String permission = MapUtils.getString(body,"permission");

            Map headers = MapUtils.getMap(body,"header",new HashMap<>());
            Map param = MapUtils.getMap(body,"param",new HashMap<>());
            List params = (List) ((List) MapUtils.getObject(body, "params"))
                    .stream()
                    .map(p-> Jsons.mapToObject((Map)p,ParamDescribe.class))
                    .collect(Collectors.toList());

            MethodDetail result = apiServiceOld.getResult(url,headers,http,param,permission,params);
            return result;
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }




    //project
    public void addProject(RoutingContext routingContext){
        Async.start(()->{
            Map body = routingContext.getBodyAsJson().getMap();
            DocsProject docsProject = Jsons.mapToObject(body,DocsProject.class);
            return projectService.createProject(docsProject);
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void updateProject(RoutingContext routingContext){
        Async.start(()->{
            String id = routingContext.pathParam("projectId");
            Map body = routingContext.getBodyAsJson().getMap();
            projectService.updateProject(id,body);
            return "";
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void delProject(RoutingContext routingContext){
        Async.start(()->{
            String id = routingContext.pathParam("projectId");
            projectService.deleteProject(id);
            return "";
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void findProject(RoutingContext routingContext){
        Async.start(()->{
            String where = HttpUtils.getParam(routingContext,"where","{}").toString();
            String order = HttpUtils.getParam(routingContext,"order","{}").toString();
            String skip = HttpUtils.getParam(routingContext,"skip","0");
            String limit = HttpUtils.getParam(routingContext,"limit","20");
            return projectService.findProduct(where,order,Integer.parseInt(skip),Integer.parseInt(limit));
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    //apiFolder
    public void addApiFolder(RoutingContext routingContext){
        Async.start(()->{
            Map body = routingContext.getBodyAsJson().getMap();
            DocsApiFolder apiFolder = Jsons.mapToObject(body,DocsApiFolder.class);
            return apiFolderService.createProject(apiFolder);
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void updateApiFolder(RoutingContext routingContext){
        Async.start(()->{
            String id = routingContext.pathParam("apiFolderId");
            Map body = routingContext.getBodyAsJson().getMap();
            apiFolderService.updateProject(id,body);
            return "";
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void delApiFolder(RoutingContext routingContext){
        Async.start(()->{
            String id = routingContext.pathParam("apiFolderId");
            apiFolderService.deleteProject(id);
            return "";
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void findApiFolder(RoutingContext routingContext){
        Async.start(()->{
            String where = HttpUtils.getParam(routingContext,"where","{}").toString();
            String order = HttpUtils.getParam(routingContext,"order","{}").toString();
            String skip = HttpUtils.getParam(routingContext,"skip","0");
            String limit = HttpUtils.getParam(routingContext,"limit","20");
            String projectId = routingContext.pathParam("projectId");
            return apiFolderService.findApiFolder(where,order,Integer.parseInt(skip),Integer.parseInt(limit),projectId);
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }


    //api
    public void addApi(RoutingContext routingContext){
        Async.start(()->{
            Map body = routingContext.getBodyAsJson().getMap();
            DocsApi api = Jsons.mapToObject(body,DocsApi.class);
            return apiService.createApi(api);
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void updateOrCreateApi(RoutingContext routingContext){
        Async.start(()->{
            JsonObject body = routingContext.getBodyAsJson();


            List<Map> docsApis =body.getJsonArray("apis").stream()
                    .map(object -> ((JsonObject)object).getMap())
                   .collect(Collectors.toList());

            List<Map> delApis =body.getJsonArray("delApis").stream()
                    .map(object -> ((JsonObject)object).getMap())
                    .collect(Collectors.toList());

            apiService.updateOrCreateApi(docsApis,delApis);
            return "";
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void delApi(RoutingContext routingContext){
        Async.start(()->{
            String id = routingContext.pathParam("apiId");
            apiService.deleteApi(id);
            return "";
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void findApi(RoutingContext routingContext){
        Async.start(()->{
            String where = HttpUtils.getParam(routingContext,"where","{}").toString();
            String order = HttpUtils.getParam(routingContext,"order","{}").toString();
            String skip = HttpUtils.getParam(routingContext,"skip","0");
            String limit = HttpUtils.getParam(routingContext,"limit","20");
            String folderId = routingContext.pathParam("folderId");
            return apiService.findApiFolder(where,order,Integer.parseInt(skip),Integer.parseInt(limit),folderId);
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void createOrUpdatePermission(RoutingContext routingContext){
        Async.start(()->{
            List<DocsApiPermission> list = routingContext.getBodyAsJsonArray().stream()
                    .map(obj -> {
                        Map map = ((JsonObject)obj).getMap();
                        return Jsons.mapToObject(map,DocsApiPermission.class);
                    }).collect(Collectors.toList());

            apiService.createOrUpdatePermission(list);
            return "";
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void findAllApiPermission(RoutingContext routingContext){
        Async.start(()->{
            return apiService.findAllApiPermission();
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void delApiPermission(RoutingContext routingContext){
        Async.start(()->{
            String id = routingContext.pathParam("apiPermissionId");
            apiService.deleteApiPermission(id);
            return "";
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void getSaveApiDetails(RoutingContext routingContext){
        Async.start(()->{
            String folderId = routingContext.pathParam("folderId");
            return apiService.getSaveApiDetails(folderId);
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void getCurlResult(RoutingContext routingContext){
        Async.start(()->{
            String curl = routingContext.getBodyAsJson().getString("curl");
            return apiService.getCurlResult(curl);
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void getJavaSampleByCurl(RoutingContext routingContext){
        Async.start(()->{
            String curl = routingContext.getBodyAsJson().getString("curl");
            return apiService.getJavaSampleByCurl(curl);
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void getJavascriptSampleByCurl(RoutingContext routingContext){
        Async.start(()->{
            String curl = routingContext.getBodyAsJson().getString("curl");
            return apiService.getJavascriptSampleByCurl(curl);
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void checkCurl(RoutingContext routingContext){
        Async.start(()->{
            String curl = routingContext.getBodyAsJson().getString("curl");
            return apiService.checkCurl(curl);
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void markdownTemplate(RoutingContext routingContext){
        Async.start(()->{
            DocsMarkdownTemplate markdownTemplate = Jsons.mapToObject(routingContext.getBodyAsJson().getMap(),DocsMarkdownTemplate.class);
            apiService.markdownTemplate(markdownTemplate);
            return "";
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void findMarkdownTemplate(RoutingContext routingContext){
        Async.start(()->{
            return apiService.findMarkdownTemplate();
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void getMarkdownString(RoutingContext routingContext){
        Async.start(()->{
            List<DocsApi> apis = routingContext.getBodyAsJsonArray()
                    .stream()
                    .map(object-> {
                        Map map = ((JsonObject)object).getMap();
                        return Jsons.mapToObject(map,DocsApi.class);
                    })
                    .collect(Collectors.toList());

            String markdown = apiService.getMarkdownString(apis);
            return markdown;
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void generateMarkdown(RoutingContext routingContext){
        Async.start(()->{
          String markdown = routingContext.getBodyAsJson().getString("markdown");

          return apiService.generateMarkdown(markdown);
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    //model
    public void getAllModel(RoutingContext routingContext){
        String where = HttpUtils.getParam(routingContext,"where","{}").toString();
        String order = HttpUtils.getParam(routingContext,"order","{}").toString();
        String skip = HttpUtils.getParam(routingContext,"skip","0");
        String limit = HttpUtils.getParam(routingContext,"limit","20");
        Async.start(()-> modelService.getModel(where,order,Integer.parseInt(skip),Integer.parseInt(limit)),Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void addModel(RoutingContext routingContext){
        Async.start(()->{
            Map body = routingContext.getBodyAsJson().getMap();
            DocsModel model = Jsons.mapToObject(body, DocsModel.class);
            modelService.addModel(model);
            Map map = Maps.newHashMap();
            map.put("addModel","success");
            return map;
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void updateModel(RoutingContext routingContext){
        Async.start(()->{
            String modelId = routingContext.pathParam("modelId");
            Map model = routingContext.getBodyAsJson().getMap();
            modelService.upModel(modelId,model);
            Map map = Maps.newHashMap();
            map.put("upModel","success");
            return map;
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void delModel(RoutingContext routingContext){
        Async.start(()->{
            String modelId = routingContext.pathParam("modelId");
            modelService.delModel(modelId);
            Map map = Maps.newHashMap();
            map.put("delModel","success");
            return map;
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void getModelDetailsByModelId(RoutingContext routingContext){
        Async.start(()->{
            String modelId = HttpUtils.getParam(routingContext,"modelId");
            return modelService.getModelDetailsByModelId(modelId);
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }

    public void createOrUpdateModelDetails(RoutingContext routingContext){
        Async.start(()->{
            List addOrUpDetails = routingContext.getBodyAsJson().getJsonArray("addOrUpDetails").getList();
            List delDetails = routingContext.getBodyAsJson().getJsonArray("delDetails").getList();
            String modelId = routingContext.pathParam("modelId");
            List<DocsModelDetails> details = (List) (addOrUpDetails)
                    .stream()
                    .map((p) -> {
                        ((Map)p).remove("createdAt");
                        ((Map)p).remove("updatedAt");
                        return Jsons.mapToObject((Map)p,DocsModelDetails.class);
                    })
                    .collect(Collectors.toList());

            List<DocsModelDetails> details2 = (List) (delDetails)
                    .stream()
                    .map((p) -> {
                        ((Map)p).remove("createdAt");
                        ((Map)p).remove("updatedAt");
                        return Jsons.mapToObject((Map)p,DocsModelDetails.class);
                    })
                    .collect(Collectors.toList());

            modelService.createOrUpdateModelDetails(details,details2);
            Map map = Maps.newHashMap();
            return map;
        },Utils.scheduler).subscribe( HttpUtils.send(routingContext));
    }
}
