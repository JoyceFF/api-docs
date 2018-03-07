package as.leap.maxwon.docs.common;

import as.leap.maxwon.docs.common.curl.CurlUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.toilelibre.libe.curl.Curl.$;

public class SampleUtils {
    //    public static String curl(DocsApi api) {
//        Map headers = new HashMap();
//        for (DocsHeader docsHeader : api.getHeaders()) {
//            headers.put(docsHeader.getKey(),docsHeader.getValue());
//        }
//
//        Map body = new HashMap();
//
//       return curl(api.getUrl(),headers,api.getHttp(),api.)
//    }
    public static String curl(String url, Map<String, String> headers, String http, String body) {
        StringBuffer curl = new StringBuffer();
        curl.append("curl -X " + http + " \\\n");
        curl.append("  '" + url + "' \\\n");
        List<String> hs = headers.keySet().stream().map(key -> key + ": " + headers.get(key)).collect(Collectors.toList());

        for (String h : hs) {
            curl.append("-H '" + h + "' \\\n");
        }

        if (StringUtils.isNotEmpty(body)) {
            curl.append("-d '" + body + "'\n");
        }

        return curl.toString();
    }

    public static String javaScript(String curl) {
        if (StringUtils.isEmpty(curl)) return "";
        curl = curl.replaceAll("\\\\","");
        return javaScript(CurlUtils.getUrl(curl), CurlUtils.getHeaders(curl), CurlUtils.getMethod(curl), CurlUtils.getBodyJsonString(curl));
    }

    public static String javaScript(String url, Map<String, String> headers, String http, String body) {
        StringBuffer javaScript = new StringBuffer();
        javaScript.append("var settings = { \n");
        javaScript.append("  \"async\": true, \n");
        javaScript.append("  \"crossDomain\": true, \n");
        javaScript.append("  \"url\":\"" + url + "\" \n");
        javaScript.append("  \"method\": \"" + http + "\", \n");
        javaScript.append("  \"headers\":{ \n");
        List<String> keys = Lists.newArrayList(headers.keySet());
        for (String key : keys) {
            if (key != keys.get(keys.size()-1)){
                javaScript.append("    \""+key+"\": " + "\""+headers.get(key)+"\",\n");
            }else{
                javaScript.append("    \""+key+"\": " + "\""+headers.get(key)+"\"\n");
            }
        }
        if (body != null) {
            javaScript.append("  },\n");
            javaScript.append("\"data\": " + body + " \n");
        }else{
            javaScript.append("  }\n");
        }
        javaScript.append("}\n\n");
        javaScript.append("$.ajax(settings).done(function (response) { \n");
        javaScript.append("  console.log(response);\n");
        javaScript.append("});");
        return javaScript.toString();
    }

    public static String java(String curl) {
        if (StringUtils.isEmpty(curl)) return "";
        curl = curl.replaceAll("\\\\","");
        return java(CurlUtils.getUrl(curl), CurlUtils.getHeaders(curl), CurlUtils.getMethod(curl), CurlUtils.getBodyJsonString(curl));
    }

    public static String java(String url, Map<String, String> headers, String http, String body) {
        StringBuffer java = new StringBuffer();
        java.append("OkHttpClient client = new OkHttpClient(); \n\n");
        if (body != null) {
            java.append("MediaType mediaType = MediaType.parse(\"application/json\");\n");
            java.append("RequestBody body = RequestBody.create(mediaType, " + body + ");\n");
        }
        java.append("Request request = new Request.Builder()\n");
        java.append("  .url(\"" + url + "\")\n");
        if (http.toLowerCase().equals("get")) {
            java.append("  .get()\n");
        } else if (http.toLowerCase().equals("post")) {
            java.append("  .post(body)\n");
        } else if (http.toLowerCase().equals("put")) {
            java.append("  .put(body)\n");
        } else if (http.toLowerCase().equals("delete")) {
            java.append("  .delete(body)\n");
        }

        for (String key : headers.keySet()) {
            java.append("  .addHeader(\"" + key + "\", \"" + MapUtils.getString(headers, key) + "\") \n");
        }
        java.append("  .build(); \n\n");
        java.append("Response response = client.newCall(request).execute();");

        return java.toString();
    }

    public static String result(String curl){
        if (StringUtils.isEmpty(curl)) return "";
        curl = curl.replaceAll("\\\\","");
        return $(curl);
    }
}
