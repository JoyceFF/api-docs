package as.leap.maxwon.docs.common;

import as.leap.maxwon.docs.common.entity.ParamDescribe;
import as.leap.maxwon.docs.common.maxwon.PermissionEntity;
import as.leap.maxwon.docs.common.maxwon.PermissionType;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.*;
import com.google.common.collect.Lists;
import okhttp3.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class MethodUtils {



//    public static String methodMd(MethodDeclaration method) {
//        StringBuffer result = new StringBuffer();
//        Optional<Permission> permission = method.getAncestorOfType(Permission.class);
//        Optional<Path> path = method.getAncestorOfType(Path.class);
//
//        String methodName = "";
//        String methodPath = "";
//        if (permission.isPresent()) {
//            methodName = permission.get().name();
//        }
//
//        if (path.isPresent()) {
//            methodPath = path.get().value();
//        }
//
//        String http = getHttp(method);
//
//        return result.toString();
//    }

    public static List<ParamDescribe> getParams(MethodDeclaration method){
        List<ParamDescribe> paramDescribes = new ArrayList<>();
        List<String> filter = Lists.newArrayList("response","appId");
        method.getParameters().forEach(parameter -> {

            String name = parameter.getName().getIdentifier();

            if (!filter.contains(name)){
                ParamDescribe  paramDescribe= new ParamDescribe();

                parameter.getAnnotationByClass(QueryParam.class).ifPresent(annotationExpr -> {
                    paramDescribe.setParamType("QueryParam");
                });

                parameter.getAnnotationByClass(PathParam.class).ifPresent(annotationExpr -> {
                    paramDescribe.setParamType("PathParam");
                });

                parameter.getAnnotationByClass(DefaultValue.class).ifPresent(annotationExpr -> {
                    paramDescribe.setDefaultValue(annotationExpr.asSingleMemberAnnotationExpr().getMemberValue().toString().replaceAll("\"",""));
                });

                if (StringUtils.isEmpty(paramDescribe.getParamType())){
                    String typeName = parameter.getType().asString();
                    Optional<File> file = Utils.findFileByName(typeName);

                    if (file.isPresent()){
                        try {
                            CompilationUnit cu = JavaParser.parse(file.get());
                            for (TypeDeclaration<?> t : cu.getTypes()) {
                                NodeList<BodyDeclaration<?>> members = t.getMembers();
                                for (BodyDeclaration<?> member : members) {
                                    if (member instanceof FieldDeclaration) {
                                        FieldDeclaration field = (FieldDeclaration) member;
                                        if (field.getModifiers().contains(Modifier.PRIVATE)){
                                            ParamDescribe bodyParam = new ParamDescribe();
                                            String fieldName = field.getVariables().get(0).getName().getIdentifier();
                                            String type = field.getVariables().get(0).getType().asString();
                                            field.getJavadocComment().ifPresent(javadocComment -> {
                                                System.out.println(javadocComment.toString());
                                            });
                                            field.getComment().ifPresent(comment -> {
                                                bodyParam.setDescribe(comment.getContent());
                                             });
                                            bodyParam.setParamType("Body");
                                            bodyParam.setName(fieldName);
                                            bodyParam.setType(type);
                                            paramDescribes.add(bodyParam);
                                        }
                                    }
                                }
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }


                    }else{
                        paramDescribe.setParamType("Body");
                        paramDescribe.setName(name);
                        paramDescribe.setType(parameter.getType().asString());
                        paramDescribes.add(paramDescribe);
                    }


                }else{
                    paramDescribe.setName(name);
                    paramDescribe.setType(parameter.getType().asString());
                    paramDescribes.add(paramDescribe);
                }
            }

        });



        return paramDescribes;
    }


    public static String getPermission(MethodDeclaration method) {
        PermissionEntity permission = conversionToPermission(method);
        List result = new ArrayList();
        if (Objects.nonNull(permission)) {
            List<PermissionType> permissionTypes = permission.getType();
            if (CollectionUtils.isNotEmpty(permissionTypes)){
                if (permissionTypes.contains(PermissionType.API_KEY)) {
                    result.add(PermissionType.API_KEY.getIdentity());
                } else if (permissionTypes.contains(PermissionType.APP_USER)) {
                    result.add(PermissionType.APP_USER.getIdentity());
                } else if (permissionTypes.contains(PermissionType.MASTER_KEY)) {
                    result.add(PermissionType.MASTER_KEY.getIdentity());
                } else {
                    for (int i = 0; i < permissionTypes.size(); i++) {
                        PermissionType permissionType = permission.getType().get(i);
                        result.add(permissionType.getIdentity());
                    }
                }
            }

        }
        String  permissionStr = result.toString().replace("[", "").replace("]", "").toUpperCase();

        if (permissionStr.toUpperCase().equals("APP_USER")) permissionStr = "APP_TOKEN";
        else if (permissionStr.toUpperCase().equals("MASTER_KEY") || permissionStr.toUpperCase().equals("API_KEY")){}
        else permissionStr = "ADMIN_TOKEN";
        return permissionStr;
    }

    public static String getURL(MethodDeclaration method){
        Optional<AnnotationExpr> annotationExpr = method.getAnnotationByClass(Path.class);

        String classPath = "";
        Optional<Node> clazz =method.getParentNode();
        if (clazz.isPresent()){
            Optional<AnnotationExpr> path = ((ClassOrInterfaceDeclaration)clazz.get()).getAnnotationByClass(Path.class);
            if (path.isPresent()){
                classPath = path.get().asSingleMemberAnnotationExpr().getMemberValue().toString().replaceAll("\"","").replaceFirst("/","");
            }
        }

        String URL = "https://wonapi.maxleap.cn/1.0/"+classPath;
        if (annotationExpr.isPresent()){
            URL = URL +"/"+ annotationExpr.get().asSingleMemberAnnotationExpr().getMemberValue().toString().replaceAll("\"","").replaceFirst("/","");
        }


        return URL;
    }

    public static String getName(MethodDeclaration method) {
        PermissionEntity permissionEntity = conversionToPermission(method);

        if (StringUtils.isNotEmpty(permissionEntity.getName())){
            return permissionEntity.getName().replaceAll("\"","");
        }else{
            return "";
        }
    }

    public static String getHttp(MethodDeclaration method) {
        Optional<AnnotationExpr> post = method.getAnnotationByClass(POST.class);
        Optional<AnnotationExpr> put = method.getAnnotationByClass(PUT.class);
        Optional<AnnotationExpr> delete = method.getAnnotationByClass(DELETE.class);
        Optional<AnnotationExpr> get = method.getAnnotationByClass(GET.class);

        if (post.isPresent()) return "POST";
        else if (put.isPresent()) return "PUT";
        else if (delete.isPresent()) return "DELETE";
        else if (get.isPresent()) return "GET";

        return "";
    }


    public static String curl(String url, Map<String, String> headers, String http, Map body) {
        StringBuffer curl = new StringBuffer();
        curl.append("curl -X " + http + " \\\n");
        curl.append("  '"+url+"' \\\n");
        List<String> hs = headers.keySet().stream().map(key->key+": " + headers.get(key)).collect(Collectors.toList());

        for (String h : hs) {
            curl.append("-H '"+h+"' \\\n");
        }

        if (body.keySet().size()>0){
            curl.append("-d '"+Jsons.objectToJSONStr(body)+"'\n");
        }

        return curl.toString();
    }


    public static String javaScript(String url, Map<String, String> headers, String http, Map body){
        StringBuffer javaScript = new StringBuffer();
        javaScript.append("var settings = { \n");
        javaScript.append("  \"async\": true, \n");
        javaScript.append("  \"crossDomain\": true, \n");
        javaScript.append("  \"url\":\""+url+"\" \n");
        javaScript.append("  \"method\": \""+http+"\", \n");
        javaScript.append("  \"headers\":"+Jsons.objectToJSONStr(headers)+", \n");
        javaScript.append("  \"processData\": false, \n");
        javaScript.append("  \"data\": \""+Jsons.objectToJSONStr(body)+"\" \n");
        javaScript.append("}\n\n");
        javaScript.append("$.ajax(settings).done(function (response) { \n");
        javaScript.append("  console.log(response);\n");
        javaScript.append("});");
        return javaScript.toString();
    }

    public static String java(String url, Map<String, String> headers, String http, Map<String,Object> body){
        StringBuffer java = new StringBuffer();
        java.append("OkHttpClient client = new OkHttpClient(); \n\n");
        java.append("MediaType mediaType = MediaType.parse(\"application/json\");\n");
        java.append("RequestBody body = RequestBody.create(mediaType, \""+Jsons.objectToJSONStr(body)+"\");\n");
        java.append("Request request = new Request.Builder()\n");
        java.append("  .url(\""+url+"\")");
        if (http.toLowerCase().equals("get")){
            java.append(".get()\n");
        }else if (http.toLowerCase().equals("post")){
            java.append(".post(body)\n");
        }else if (http.toLowerCase().equals("put")){
            java.append(".put(body)\n");
        }else if (http.toLowerCase().equals("delete")){
            java.append(".delete(body)\n");
        }

        for (String key : headers.keySet()) {
            java.append("  .addHeader(\""+key+"\", \""+ MapUtils.getString(headers,key)+"\") \n");
        }
        java.append("  .build(); \n\n");
        java.append("Response response = client.newCall(request).execute();");

        return java.toString();
    }
    public static String result(String url, Map<String, String> headers, String http, Map<String,Object> body){


        Request.Builder request =new Request.Builder().headers(Headers.of(headers));
        if (http.equals("POST")){
            request.post(RequestBody.create(MediaType.parse("application/json"),Jsons.objectToJSONStr(body)));
        }else if (http.equals("DELETE")){
            request.delete(RequestBody.create(MediaType.parse("application/json"),Jsons.objectToJSONStr(body)));
        }else if (http.equals("PUT")){
            request.put(RequestBody.create(MediaType.parse("application/json"),Jsons.objectToJSONStr(body)));
        }else if (http.equals("GET")){
            request.get();
        }
        request.url(url);

//        try {
//            final Response response = this.okHttpClient.newCall(request.build()).execute();
//            return Utils.formatJsonView(response.body().string());
//        } catch (IOException e) {
//            return "";
//        }
return "";
    }

    public static PermissionEntity conversionToPermission(MethodDeclaration method){
        Optional<AnnotationExpr> annotationExpr = method.getAnnotationByName("Permission");
        PermissionEntity permission = new PermissionEntity();
        if (annotationExpr.isPresent()) {
            NormalAnnotationExpr normalAnnotationExpr =  annotationExpr.get().asNormalAnnotationExpr();
            NodeList<MemberValuePair> nodeList = normalAnnotationExpr.getPairs();
            for (MemberValuePair memberValuePair : nodeList) {
                if (memberValuePair.getName().getIdentifier().equals("name")) {
                    permission.setName(memberValuePair.getValue().toString());
                }else if (memberValuePair.getName().getIdentifier().equals("type")){
                    NodeList<Expression> values = ((ArrayInitializerExpr) memberValuePair.getValue()).getValues();
                    List<PermissionType> permissionTypes = new ArrayList<>();
                    for (Node n : values) {
                       SimpleName simpleName = (SimpleName) n.getChildNodes().get(1);
                       String identity = simpleName.asString().toLowerCase();
                       PermissionType permissionType = PermissionType.fromString(identity);
                       if (permissionType != null){
                          permissionTypes.add(permissionType);
                       }
                    }
                    permission.setType(permissionTypes);
                }else if (memberValuePair.getName().getIdentifier().equals("permisson")) {
                    permission.setPermission(memberValuePair.getValue().toString());
                }else if (memberValuePair.getName().getIdentifier().equals("relationType")){
                    permission.setRelationType(Integer.parseInt(memberValuePair.getValue().toString()));
                }
            }
        }
        return permission;
    }

    public static String formatUrl(String url, Map<String, String> param, List<ParamDescribe> params) {
        Map<String, String> paramMap = params.stream().collect(Collectors.toMap(ParamDescribe::getName, ParamDescribe::getParamType));

        for (String key : param.keySet()) {
            String paramType = paramMap.get(key);
            if (paramType != null){
                String value = param.get(key);
                if (paramType.equals("PathParam")) {
                    url.replaceAll("{" + key + "}", value);
                } else if (paramType.equals("QueryParam")) {
                    if (url.indexOf("?") > -1) {
                        url = url + "&" + key + "=" + value;
                    } else {
                        url = url + "?" + key + "=" + value;
                    }
                }
            }
        }
        return url;
    }

    public static void main(String[] args) {

        String url = "https://wonapi.maxleap.cn/1.0/mall";
        Map<String,String> headers = new HashMap<>();
        headers.put("X-ML-AppId","56b17529169e7d000197d2d7");
        headers.put("X-ML-Session-Token","MrUyhfkFFxa598T4vZP9mvlnj_DKJhHlrsVWhHr-l5k");
        headers.put("Content-Type","application/json");

        String http = "POST";

        Map<String,Object> body = new HashMap<>();
        body.put("type",1);


        System.out.println(curl(url,headers,http,body));
        System.out.println(java(url,headers,http,body));
        System.out.println(javaScript(url,headers,http,body));
    }
}
