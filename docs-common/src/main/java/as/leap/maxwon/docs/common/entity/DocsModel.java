package as.leap.maxwon.docs.common.entity;

public class DocsModel {
    private String id;
    private String name;
    private String describe;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIBE = "describe";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
