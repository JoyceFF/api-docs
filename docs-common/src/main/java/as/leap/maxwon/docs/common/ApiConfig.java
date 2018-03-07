package as.leap.maxwon.docs.common;

public class ApiConfig {
    String projectPath; // must set
    String docsPath; // default equals projectPath

    String rapHost;
    String rapLoginCookie;
    String rapProjectId;
    String rapAccount;
    String rapPassword;

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }

    public String getDocsPath() {
        return docsPath;
    }

    public void setDocsPath(String docsPath) {
        this.docsPath = docsPath;
    }

    public String getRapHost() {
        return rapHost;
    }

    public void setRapHost(String rapHost) {
        this.rapHost = rapHost;
    }

    public String getRapLoginCookie() {
        return rapLoginCookie;
    }

    public void setRapLoginCookie(String rapLoginCookie) {
        this.rapLoginCookie = rapLoginCookie;
    }

    public String getRapProjectId() {
        return rapProjectId;
    }

    public void setRapProjectId(String rapProjectId) {
        this.rapProjectId = rapProjectId;
    }

    public String getRapAccount() {
        return rapAccount;
    }

    public void setRapAccount(String rapAccount) {
        this.rapAccount = rapAccount;
    }

    public String getRapPassword() {
        return rapPassword;
    }

    public void setRapPassword(String rapPassword) {
        this.rapPassword = rapPassword;
    }
}
