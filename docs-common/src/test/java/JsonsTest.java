import as.leap.maxwon.docs.common.Jsons;
import as.leap.maxwon.docs.common.entity.DocsPathParam;

import java.util.HashMap;
import java.util.Map;

public class JsonsTest {
    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("name",1);
        DocsPathParam docsPathParam = Jsons.mapToObject(map, DocsPathParam.class);
        System.out.println(Jsons.objectToJSONStr(docsPathParam));
    }
}
