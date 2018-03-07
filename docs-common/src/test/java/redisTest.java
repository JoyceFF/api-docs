import as.leap.maxwon.docs.common.curl.CurlUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;



public class redisTest {
    public static void main(String[] args) {
//       String data = DateFormatUtils.format(1514436654000l,"yyyy-MM-dd HH:mm:ss");
//       System.out.println(data);

        String curl = "curl -X PUT " +
                "  http://0.0.0.0:9090/mallVoucher/5a27a9e6dba3ba0005eebcd1?1=1  //" +
                "  -H 'content-type: application/json'" +
                "  -H 'x-ml-apikey: SzNxdjZWNUxkbVl6c0dMeWs2dGh1dw' " +
                "  -H 'x-ml-appid: 5a100bfbd9c60d0001436ac9' " +
                "  -H 'x-ml-masterkey: VUlVTFlMaldSQlhXZzFIeUtoM05LQQ' " +
                "  -H 'x-ml-session-token: 7tlZ8ec-QkBgTM7-IYOTQYgE7KC0IhHlmLYMTemgmdY' " +
                "  -d '{" +
                "\"giveItemType\":\"你啊见哦啊啥的卡\"," +
                "\"useType\":3," +
                "\"usingScope\":[1]" +
                "}'";

        String body = CurlUtils.getBodyJsonString(curl);
        System.out.println(body);

//        final String requestCommandWithoutBasename = curl.replaceAll ("^[ ]*curl[ ]*", " ") + " ";
//        CommandLine commandLine = ReadArguments.getCommandLineFromRequest(requestCommandWithoutBasename);
//        HttpUriRequest httpUriRequest = HttpRequestProvider.prepareRequest(commandLine);
//        System.out.println(httpUriRequest.getMethod());
//        System.out.println(httpUriRequest.getURI().toString());
//        for (Header header : httpUriRequest.getAllHeaders()) {
//            System.out.println(header.getName()+":"+header.getValue());
//        }
//        String body = IOUtils.quietToString(((HttpEntityEnclosingRequest)httpUriRequest).getEntity());
//        System.out.println(body);

    }

    @Test
    public void testScan(){
        Jedis jedis = new Jedis("0.0.0.0", 6379);
        jedis.select(1);
        ScanParams scanParams = new ScanParams();
        scanParams.match("*user*");
        for (String s : jedis.scan("0",scanParams).getResult()) {
            System.out.println(s);
        }
    }
}
