package as.leap.maxwon.docs.common.entity;

import java.util.ArrayList;
import java.util.List;

public class DocsApi extends BaseEntity{
    private String id;
    private String folderId;
    private String name = "";
    private String describe = "";
    private String http = "";
    private String url = "";
    private String permission = "";
    private List<DocsPathParam> pathParams = new ArrayList<>();
    private List<DocsParam> params = new ArrayList<>();
    private List<DocsParam> body = new ArrayList<>();
    private List<DocsHeader> headers = new ArrayList<>();
    private List<DocsParam> result = new ArrayList<>();
    private String curl = "";

    public static final String FOLDER_ID = "folderId";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<DocsParam> getParams() {
        return params;
    }

    public void setParams(List<DocsParam> params) {
        this.params = params;
    }

    public List<DocsHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<DocsHeader> headers) {
        this.headers = headers;
    }

    public List<DocsParam> getBody() {
        return body;
    }

    public void setBody(List<DocsParam> body) {
        this.body = body;
    }

    public List<DocsPathParam> getPathParams() {
        return pathParams;
    }

    public void setPathParams(List<DocsPathParam> pathParams) {
        this.pathParams = pathParams;
    }

    public List<DocsParam> getResult() {
        return result;
    }

    public void setResult(List<DocsParam> result) {
        this.result = result;
    }

    public String getCurl() {
        return curl;
    }

    public void setCurl(String curl) {
        this.curl = curl;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}

