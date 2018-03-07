package as.leap.maxwon.docs.common.maxwon;

import java.util.List;

public class PermissionEntity {
    String permission;
    List<PermissionType> type;
    String name;
    int relationType; // 0: or, 1: and

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    public List<PermissionType> getType() {
        return type;
    }

    public void setType(List<PermissionType> type) {
        this.type = type;
    }
}
