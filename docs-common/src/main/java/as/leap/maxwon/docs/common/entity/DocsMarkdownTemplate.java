package as.leap.maxwon.docs.common.entity;

import java.util.ArrayList;
import java.util.List;

public class DocsMarkdownTemplate extends BaseEntity{
    private String id;
    private List<Integer> markdown = new ArrayList<>();
    private List<Integer> markdownApiDetails = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Integer> getMarkdown() {
        return markdown;
    }

    public void setMarkdown(List<Integer> markdown) {
        this.markdown = markdown;
    }

    public List<Integer> getMarkdownApiDetails() {
        return markdownApiDetails;
    }

    public void setMarkdownApiDetails(List<Integer> markdownApiDetails) {
        this.markdownApiDetails = markdownApiDetails;
    }

    public enum Markdown{
        api_list(1),//api列表
        api_details(2);//api详情
        int value;

        public static Markdown valueOf(int value){
            for (Markdown markdown : Markdown.values()) {
                if (markdown.value == value){
                    return markdown;
                }
            }
            return null;
        }

        Markdown(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    public enum MarkdownApiDetails{
        api_name(1),//接口名称
        api_desc(2),//接口描述
        api_url(3),//请求地址
        path_param(4),//路径参数
        api_permission(5),//请求权限
        api_header(6),//请求header
        api_param(7),//查询参数
        api_body(8),//body
        api_result(9),//返回值
        curl_sample(10),//curl示例
        java_sample(11),//java示例
        javascript_sample(12),//javascript示例
        result_sample(13),//返回值示例
        api_http(14)
        ;


        public static MarkdownApiDetails valueOf(int value){
            for (MarkdownApiDetails markdownApiDetails : MarkdownApiDetails.values()) {
                if (markdownApiDetails.value == value){
                    return markdownApiDetails;
                }
            }
            return null;
        }
        int value;

        MarkdownApiDetails(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}

