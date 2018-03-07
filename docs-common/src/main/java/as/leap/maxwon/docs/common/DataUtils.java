package as.leap.maxwon.docs.common;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.google.common.collect.Maps;
import redis.clients.jedis.Jedis;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataUtils {

    private static Map<String,File> resources = new HashMap();
    private static Map<String,MethodDeclaration> methods = new HashMap();
    public final static Map<String,String> adminDefaultHeader = Maps.newHashMap();
    public final static Map<String,String> memberDefaultHeader = Maps.newHashMap();
    public final static Map<String,String> apiKeyDefaultHeader = Maps.newHashMap();
    public final static Map<String,String> masterKeyDefaultHeader = Maps.newHashMap();
    public final static Map<String,String> paramDefault = Maps.newHashMap();
    static {
        adminDefaultHeader.put("X-ML-AppId","56b17529169e7d000197d2d7");
        adminDefaultHeader.put("X-ML-Session-Token","mtFgYiWcwFVWdchfrIOtTjOeTNyKdUK-vxUHMHTxBCM");
        memberDefaultHeader.put("X-ML-AppId","56b17529169e7d000197d2d7");
        memberDefaultHeader.put("X-ML-Session-Token","MrUyhfkFFxa598T4vZP9mt0G0Ya820wXlbtut4nxLmw");
        apiKeyDefaultHeader.put("X-ML-AppId","56b17529169e7d000197d2d7");
        apiKeyDefaultHeader.put("X-ML-APIKey","eDhNSWZfaUNIV0RyYmdTcnlpY3dSdw");
        masterKeyDefaultHeader.put("X-ML-AppId","56b17529169e7d000197d2d7");
        masterKeyDefaultHeader.put("X-ML-MasterKey","bUxiYS0yNnlXRGQxaXNOQUpVVzFVQQ");
//        paramDefault.put("where","{}");
//        paramDefault.put("order","");
//        paramDefault.put("skip","0");
        paramDefault.put("limit","1");
    }


    public static Map<String,String> getDefaultHeader(String permission){
        if (permission.equals("API_KEY")) {
            return DataUtils.apiKeyDefaultHeader;
        } else if (permission.equals("MASTER_KEY")) {
           return DataUtils.masterKeyDefaultHeader;
        } else if (permission.equals("APP_TOKEN")) {
           return DataUtils.memberDefaultHeader;
        } else {
            return DataUtils.adminDefaultHeader;
        }
    }

    public static void clearResources(){
        resources.clear();
    }

    public static void addResources(String id,File file){
        resources.put(id,file);
    }

    public static File getResourceFile(String id){
        Object o = resources.get(id);

        if (Objects.nonNull(o))
            return (File) o;
        return null;
    }

    public static void addMethod(String id,MethodDeclaration methodDeclaration){
        methods.put(id,methodDeclaration);
    }

    public static MethodDeclaration getMethod(String id){
        MethodDeclaration o = methods.get(id);
        return o;
    }

    public static void clearMethods(){
        methods.clear();
    }
}
