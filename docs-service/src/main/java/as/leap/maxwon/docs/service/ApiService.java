package as.leap.maxwon.docs.service;


import as.leap.maxwon.docs.common.*;
import as.leap.maxwon.docs.common.curl.CurlUtils;
import as.leap.maxwon.docs.common.entity.DocsApi;
import as.leap.maxwon.docs.common.entity.DocsApiPermission;
import as.leap.maxwon.docs.common.entity.DocsMarkdownTemplate;
import as.leap.maxwon.docs.common.exception.CurlException;
import as.leap.maxwon.docs.common.mongo.MongoFactory;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import redis.clients.jedis.Jedis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.toilelibre.libe.curl.Curl.$;

public class ApiService {
    private MongoFactory mongoFactory;
    private MongoCollection apiFolderCollection;
    private MongoCollection permissionFolderCollection;
    private MongoCollection markdownTemplateCollection;
    @Inject
    private Jedis redis;

    @Inject
    public ApiService(MongoFactory mongoFactory) {
        this.mongoFactory = mongoFactory;
        this.apiFolderCollection = mongoFactory.getCollection("docs_method");
        this.permissionFolderCollection = mongoFactory.getCollection("docs_api_permission");
        this.markdownTemplateCollection = mongoFactory.getCollection("docs_markdown_template");
    }

    public Document createApi(DocsApi method) {
        Document document = MongoFactory.getInsertDocument(method);
        this.apiFolderCollection.insertOne(document);
        return new Document("id", document.getObjectId("_id").toString());
    }

    public void deleteApi(String id) {
        this.apiFolderCollection.deleteOne(MongoFactory.getQuery_Id(id));
    }

    public void updateOrCreateApi(List<Map> apis, List<Map> delApis) {
        List<String> folderIds = Lists.newArrayList();
        for (Map api : apis) {
            String folderId = MapUtils.getString(api, "folderId");
            String name = MapUtils.getString(api, "name");
            String url = MapUtils.getString(api, "url");
            if (StringUtils.isNotEmpty(MapUtils.getString(api, "id"))) {
                String id = MapUtils.getString(api, "id");
                UpdateResult updateRequest = this.apiFolderCollection.updateOne(MongoFactory.getQuery_Id(id), MongoFactory.MapToUpdateSet(api), MongoFactory.getDefaultUpdateOptions());
                this.redis.lpush(RedisKeys.SAVE_API_DETAILS + ":" + folderId, String.format("修改接口: name:%s url:%s", name, url));
            } else {
                createApi(Jsons.mapToObject(api, DocsApi.class));
                this.redis.lpush(RedisKeys.SAVE_API_DETAILS + ":" + folderId, String.format("添加接口: name:%s url:%s", name, url));
            }

            if (!folderIds.contains(folderId)) {
                folderIds.add(folderId);
            }
        }

        for (Map delApi : delApis) {
            String folderId = MapUtils.getString(delApi, "folderId");
            String id = MapUtils.getString(delApi, "id");
            String name = MapUtils.getString(delApi, "name");
            String url = MapUtils.getString(delApi, "url");
            deleteApi(id);
            this.redis.lpush(RedisKeys.SAVE_API_DETAILS + ":" + folderId, String.format("删除接口: name:%s url:%s", name, url));
            if (!folderIds.contains(folderId)) {
                folderIds.add(folderId);
            }
        }

        for (String folderId : folderIds) {
            this.redis.expire(RedisKeys.SAVE_API_DETAILS + ":" + folderId, 60 * 60);
        }
    }

    public List<Document> findApiFolder(String where, String order, int skip, int limit, String folderId) {
        List<Document> result = new ArrayList<>();
        this.apiFolderCollection.find(Document.parse(where).append(DocsApi.FOLDER_ID, folderId)).skip(skip).limit(limit).sort(Document.parse(order)).into(result);
        return result;
    }

    public String getSaveApiDetails(String folderId) {
        String result = this.redis.rpop(RedisKeys.SAVE_API_DETAILS + ":" + folderId);
        System.out.println(result);
        return result;
    }

    public String checkCurl(String curl){
        curl = curl.replaceAll("\\\\","");
        CurlUtils.check(curl);
        return "";
    }

    public String getCurlResult(String curl) {
        try{
            return SampleUtils.result(curl);
        }catch (Exception e){
            throw new CurlException(e);
        }
    }

    public String getJavaSampleByCurl(String curl) {
        try{
            return SampleUtils.java(curl);
        }catch (Exception e){
            throw new CurlException(e);
        }
    }

    public String getJavascriptSampleByCurl(String curl) {
        try{
            return SampleUtils.javaScript(curl);
        }catch (Exception e){
            throw new CurlException(e);
        }
    }

    public String getMarkdownString(List<DocsApi> apis){
        StringBuffer markdown = new StringBuffer();
        DocsMarkdownTemplate markdownTemplate = findMarkdownTemplate();

        for (Integer number : markdownTemplate.getMarkdown()) {
            DocsMarkdownTemplate.Markdown md = DocsMarkdownTemplate.Markdown.valueOf(number);
            switch (md){
                case api_list:
                    markdown.append("### API列表\n\n");
                    markdown.append(MarkdownUtils.getApiListTable(apis) + "\n\n");
                    break;
                case api_details:
                    markdown.append("### API详情\n\n");
                    for (DocsApi api : apis) {
                        for (Integer integer : markdownTemplate.getMarkdownApiDetails()) {
                            DocsMarkdownTemplate.MarkdownApiDetails mad = DocsMarkdownTemplate.MarkdownApiDetails.valueOf(integer);
                            switch (mad){
                                case api_name:
                                    markdown.append("#### " + api.getName() + "\n\n");
                                    break;
                                case api_desc:
                                    markdown.append("接口描述 : " + api.getDescribe() + "\n\n");
                                    break;
                                case api_url:
                                    markdown.append("请求地址 : " + api.getUrl() + "\n\n");
                                    break;
                                case api_http:
                                    markdown.append("请求类型 : `" + api.getHttp() + "`\n\n");
                                    break;
                                case api_permission:
                                    markdown.append("权限级别 : `" + api.getPermission() + "`\n\n");
                                    break;
                                case api_param:
                                    if (api.getParams().size() > 0) {
                                        markdown.append("请求参数 : \n\n");
                                        String param = MarkdownUtils.paramTable(api.getParams());
                                        markdown.append(param + "\n");
                                    }
                                    break;
                                case path_param:
                                    if (api.getPathParams().size() > 0) {
                                        markdown.append("路径参数 : \n\n");
                                        String param = MarkdownUtils.pathParamTable(api.getPathParams());
                                        markdown.append(param + "\n");
                                    }
                                    break;
                                case api_body:
                                    if (api.getBody().size() > 0) {
                                        markdown.append("body : \n\n");
                                        String param = MarkdownUtils.paramTable(api.getBody());
                                        markdown.append(param + "\n");
                                    }
                                    break;
                                case api_result:
                                    if (api.getResult().size() > 0) {
                                        markdown.append("返回值 : \n\n");
                                        String param = MarkdownUtils.paramTable(api.getResult());
                                        markdown.append(param + "\n");
                                    }
                                    break;
                                case api_header:
                                    if (api.getHeaders().size() > 0) {
                                        markdown.append("headers : \n\n");
                                        String header = MarkdownUtils.headerTable(api.getHeaders());
                                        markdown.append(header + "\n");
                                    }
                                    break;
                                case curl_sample:
                                    markdown.append("`Shell`  示例 :\n\n");
                                    markdown.append("```shell \n" + api.getCurl() + "\n```\n");
                                    break;
                                case java_sample:
                                    markdown.append("`Java`  示例 :\n\n");
                                    markdown.append("```java \n" + SampleUtils.java(api.getCurl()) + "\n```\n");
                                    break;
                                case javascript_sample:
                                    markdown.append("`Javascript` 示例 :\n\n");
                                    markdown.append("```javascript \n" + SampleUtils.javaScript(api.getCurl()) + "\n```\n");
                                    break;
                                case result_sample:
                                    markdown.append("示例返回值 :\n\n");
                                    markdown.append("```json \n" + Utils.formatJsonView(SampleUtils.result(api.getCurl())) + "\n```\n");
                                    break;
                            }
                        }
                    }
                    break;
                default:
            }
        }
        return markdown.toString();
    }

    public String generateMarkdown(String markdown){
        if (markdown == null)markdown = "";
        long time = System.currentTimeMillis();

        File file = new File("./data/markdown/" + time + ".md");

        try {
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(markdown);
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return time + "";
    }

    public void createOrUpdatePermission(List<DocsApiPermission> list){
        for (DocsApiPermission docsApiPermission : list) {
            Map map = Jsons.beanToMap(docsApiPermission);
            if (StringUtils.isNotEmpty(docsApiPermission.getId())){
                String id = docsApiPermission.getId();
                this.permissionFolderCollection.updateOne(MongoFactory.getQuery_Id(id), MongoFactory.MapToUpdateSet(map), MongoFactory.getDefaultUpdateOptions());
            }else{
                Document document = MongoFactory.getInsertDocument(map);
                this.permissionFolderCollection.insertOne(document);
            }
        }
    }

    public List<Document> findAllApiPermission(){
        List<Document> result = new ArrayList<>();
        this.permissionFolderCollection.find(Document.parse("{}")).skip(0).limit(9999).into(result);
        return result;
    }

    public void deleteApiPermission(String id){
        this.permissionFolderCollection.deleteOne(MongoFactory.getQuery_Id(id));
    }

    public void markdownTemplate(DocsMarkdownTemplate markdownTemplate){
        this.markdownTemplateCollection.deleteMany(new Document());
        Document document = MongoFactory.getInsertDocument(markdownTemplate);
        this.markdownTemplateCollection.insertOne(document);
    }

    public DocsMarkdownTemplate findMarkdownTemplate(){
        List<Document> result = new ArrayList<>();
        this.markdownTemplateCollection.find(Document.parse("{}")).into(result);
        if (result.isEmpty()){
            return new DocsMarkdownTemplate();
        }else{
            return Jsons.mapToObject(result.get(0),DocsMarkdownTemplate.class);
        }
    }

}
