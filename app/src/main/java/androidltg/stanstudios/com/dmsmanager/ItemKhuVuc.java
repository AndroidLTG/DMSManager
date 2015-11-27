package androidltg.stanstudios.com.dmsmanager;

/**
 * Created by DMSv4 on 9/3/2015.
 */
public class ItemKhuVuc {

    private String No_;
    private String name;
    private String sourcecode;
    private String responsibititycenter;
    private String linkpicture;
    private int status;
    private String description;

    public String getNo_() {
        return No_;
    }

    public void setNo_(String No_) {
        this.No_ = No_;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getsourcecode() {
        return sourcecode;
    }

    public void setsourcecode(String sourcecode) {
        this.sourcecode = sourcecode;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public String getresponsibititycenter() {
        return responsibititycenter;
    }

    public void setresponsibititycenter(String responsibititycenter) {
        this.responsibititycenter = description;
    }

    public String getlinkpicture() {
        return linkpicture;
    }

    public void setlinkpicture(String linkpicture) {
        this.linkpicture = linkpicture;
    }

    public int getstatus() {
        return status;
    }

    public void setstatus(int status) {
        this.status = status;
    }

}
