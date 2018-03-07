package as.leap.maxwon.docs.common;

import as.leap.maxwon.docs.common.entity.*;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class MarkdownUtils {


    public static String paramTable(List<DocsParam> params) { List<DocsParam> arrayList = new ArrayList<>();
        List<String> stringArrayList = new ArrayList<>();

        StringBuffer table = new StringBuffer();
        table.append("| 名称   | 必填  | 类型  | 说明 |\n");
        table.append("| ----- | ----- | ----- | ----- |\n");

        for (DocsParam param : params) {
            if (!arrayList.contains(param)) {
                table.append("|" + param.getName().trim() + "|" + param.getRequired().trim() + "|" + param.getType().trim() + "| " + param.getDescribe().trim() + " |\n");
                arrayList.add(param);
                List child = findChild(param, params);
                if (CollectionUtils.isNotEmpty(child)) {
                    String childString = paramTable(param, child);
                    stringArrayList.add(childString);
                    arrayList.addAll(child);
                }
            }
        }

        for (String s : stringArrayList) {
            table.append("\n"+s + "\n");
        }

        return table.toString();
    }

    public static String paramTable(DocsParam parentParam, List<DocsParam> params) {
        StringBuffer table = new StringBuffer();
        table.append(parentParam.getName() + "说明:\n\n");
        table.append("| 名称   | 必填  | 类型  | 说明 |\n");
        table.append("| ----- | ----- | ----- | ----- |\n");

        for (DocsParam param : params) {
            table.append("|" + param.getName().trim() + "|" + param.getRequired().trim() + "|" + param.getType().trim() + "| " + param.getDescribe().trim() + " |\n");
        }

        return table.toString();
    }

    public static List<DocsParam> findChild(DocsParam param, List<DocsParam> params) {
        List<DocsParam> result = new ArrayList<>();
        for (DocsParam docsParam : params) {
            if (docsParam.getParentId()!=null && docsParam.getParentId().equals(param.getId())) {
                result.add(docsParam);
            }
        }
        return result;
    }

    public static int getMaxLevel(List<DocsParam> params) {
        int maxLevel = 1;
        for (DocsParam param : params) {
            if (param.getLevel() > maxLevel) {
                maxLevel = param.getLevel();
            }
        }

        return maxLevel;
    }

    public static String headerTable(List<DocsHeader> headers) {

        StringBuffer table = new StringBuffer();
        table.append("| key   | value  | 描述  | \n");
        table.append("| ----- | ----- | ----- | \n");
        for (DocsHeader header : headers) {
            table.append("|" + header.getKey().trim() + "|" + header.getValue().trim() + "|" + header.getDescribe().trim() + " |\n");
        }
        return table.toString();
    }


    public static String pathParamTable(List<DocsPathParam> params) {

        StringBuffer table = new StringBuffer();
        table.append("| 名称   | 描述  | \n");
        table.append("| ----- | ----- | \n");
        for (DocsPathParam param : params) {
            table.append("|" + param.getName().trim() + "|" + param.getDescribe().trim() + "|\n");
        }
        return table.toString();
    }

    public static String getApiListTable(List<DocsApi> apis) {
        StringBuffer table = new StringBuffer();
        table.append("| URL   | HTTP  | 描述  | 权限级别|\n");
        table.append("| ----- | ----- | ----- | ----- |\n");
        for (DocsApi api : apis) {
            table.append("|" + api.getUrl().trim() + "|" + api.getHttp().trim() + "|[" + api.getName().trim() + "](#" + api.getName().trim().toLowerCase() + ")|" + api.getPermission().trim() + "|\n");
        }
        return table.toString();
    }


    public static void main(String[] args) {
        DocsParam param1 = new DocsParam();
        param1.setId("1");
        param1.setLevel(1);
        param1.setName("1");


        DocsParam param2 = new DocsParam();
        param2.setId("2");
        param2.setLevel(2);
        param2.setName("2");
        param2.setParentId("1");

        List<DocsParam> params = Lists.newArrayList();
        params.add(param1);
        params.add(param2);

        String table = paramTable(params);
        System.out.println(table);
    }
}
