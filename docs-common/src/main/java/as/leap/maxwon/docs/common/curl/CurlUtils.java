package as.leap.maxwon.docs.common.curl;

import as.leap.maxwon.docs.common.Jsons;
import as.leap.maxwon.docs.common.exception.CurlException;
import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;

import java.util.HashMap;
import java.util.Map;

public class CurlUtils {
    public static String getUrl(String curl) {
        HttpUriRequest httpRequest = HttpRequestProvider.prepareRequest(curl);
        return httpRequest.getURI().toString();
    }

    public static Map<String, String> getHeaders(String curl) {
        HttpUriRequest httpRequest = HttpRequestProvider.prepareRequest(curl);
        Map<String, String> headers = new HashMap();
        for (Header header : httpRequest.getAllHeaders()) {
            headers.put(header.getName(), header.getValue());
        }
        return headers;
    }

    public static String getBodyJsonString(String curl) {
        StringEntity entity = HttpRequestProvider.getData(curl);
        if (entity != null) {
            return Jsons.objectToJSONStr(IOUtils.quietToString(entity));
        }
        return null;
    }

    public static String getMethod(String curl) {
        HttpUriRequest httpRequest = HttpRequestProvider.prepareRequest(curl);
        return httpRequest.getMethod();
    }
    public static void check(String curl){
        try{
            HttpUriRequest httpRequest = HttpRequestProvider.prepareRequest(curl);
        }catch (Exception e){
            throw new CurlException(e);
        }
    }

}
