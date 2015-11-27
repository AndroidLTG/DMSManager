package androidltg.stanstudios.com.dmsmanager;

/**
 * Created by DMSv4 on 8/31/2015.
 */

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class TableItem {
    private Bitmap image;
    private String Name,Ref_Area,No_,SourceCode,ResponsibilityCenter,Description,LinkPicture,OrderNo;
        private String StartDate;
    private String EndDate;
    private int Status;

    public TableItem() {

    }

    public TableItem(Bitmap image, String Name,String SourceCode,String ResponsibilityCenter,String Description,String LinkPicture,String OrderNo,String No_,String Ref_Area, String StartDate, String EndDate, int Status) {
        super();
        this.image = image;
        this.Name = Name;
        this.No_ = No_;
        this.Ref_Area =Ref_Area;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
        this.SourceCode =SourceCode;
        this.ResponsibilityCenter=ResponsibilityCenter;
        this.Description =Description;
        this.LinkPicture =LinkPicture;
        this.OrderNo =OrderNo;
        this.Status = Status;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getRef_Area() {
        return Ref_Area;
    }

    public void setRef_Area(String Ref_Area) {
        this.Ref_Area = Ref_Area;
    }
    public String getName() {
        return Name;
    }

    public void setNo_(String No_) {
        this.No_ = No_;
    }
    public String getNo_() {
        return No_;
    }
    public void setSourceCode(String SourceCode) {
        this.SourceCode = SourceCode;
    }
    public String getSourceCode() {
        return SourceCode;
    }
    public void setResponsibilityCenter(String ResponsibilityCenter) {
        this.ResponsibilityCenter = ResponsibilityCenter;
    }
    public String getResponsibilityCenter() {
        return ResponsibilityCenter;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }
    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }
    public String getDescription() {
        return Description;
    }public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }
    public String getOrderNo() {
        return OrderNo;
    }public void setLinkPicture(String LinkPicture) {
        this.LinkPicture = LinkPicture;
    }
    public String getLinkPicture() {
        return LinkPicture;
    }

}