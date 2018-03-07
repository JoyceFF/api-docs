package as.leap.maxwon.docs.server;

import as.leap.maxwon.docs.common.exception.DocsException;
import as.leap.maxwon.docs.common.Jsons;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import rx.Observer;

import java.util.Map;

public class HttpUtils {

    public static <T> Observer send(RoutingContext routingContext) {
        return new Observer<T>() {
            @Override
            public void onCompleted() {
                return;
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                send(routingContext,e);
            }

            @Override
            public void onNext(T result) {
                send(routingContext,result);
            }
        };
    }

    public static void send(RoutingContext routingContext, Object result){
        send(routingContext,result,HttpResponseStatus.OK,null);
    }

    public static void send(RoutingContext routingContext,Throwable e){
        send(routingContext,null,HttpResponseStatus.INTERNAL_SERVER_ERROR,e);
    }

    public static void send(RoutingContext routingContext,Object result,HttpResponseStatus httpResponseStatus,Throwable e){
        HttpServerResponse response = routingContext.response();
        response.headers().add("Content-Type", "application/json");
        response.headers().add("Access-Control-Allow-Origin", "*");
        response.headers().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
        response.headers().add("Access-Control-Allow-Headers", "Content-Type");


        response.setStatusCode(httpResponseStatus.code());
        if (httpResponseStatus == HttpResponseStatus.OK){
            if (result instanceof String){
                response.end((String) result);
            } else if (result == null){
                response.end("null");
            }else{
                response.end(Jsons.objectToJSONStr(result));
            }
        }else{
            Map error = Maps.newHashMap();

            if (e instanceof DocsException){
                DocsException docsException = (DocsException) e;
                error.put("errorCode",docsException.getCode());
            }

            error.put("errorMsg",e.getMessage());
            response.end(Jsons.objectToJSONStr(error));
        }
    }

    public static String getParam(RoutingContext routingContext,String paramName){
        return routingContext.request().getParam(paramName);
    }

    public static String getParam(RoutingContext routingContext,String paramName,String defaultValue){
        String param = routingContext.request().getParam(paramName);
        if (Strings.isNullOrEmpty(param)){
            return defaultValue;
        }else{
            return param;
        }
    }
}
