package androidltg.stanstudios.com.dmsmanager;

/**
 * Created by DMSv4 on 8/31/2015.
 */
import android.graphics.Bitmap;
import android.os.AsyncTask;

public class ItemList {
    private Bitmap image;
    private String title,itemcategory,productgroupcode,no_,UnitOfMeasure,itemgrouptype;
    private float prices;
    public ItemList(){

    }
    public ItemList(Bitmap image, String title,String no_,String UnitOfMeasure, float prices,String itemcategory,String itemgrouptype,String productgroupcode) {
        super();
        this.image = image;
        this.title = title;
        this.no_=no_;
        this.UnitOfMeasure =UnitOfMeasure;
        this.prices =prices;
        this.itemcategory =itemcategory;
        this.productgroupcode =productgroupcode;
        this.itemgrouptype = itemgrouptype;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public float getprices() {
        return prices;
    }

    public void setprices(float prices) {
        this.prices = prices;
    }
    public String getitemcategory() {
        return itemcategory;
    }

    public void setitemcategory(String itemcategory) {
        this.itemcategory = itemcategory;
    }
    public String getproductgroupcode() {
        return productgroupcode;
    }

    public void setproductgroupcode(String productgroupcode) {
        this.productgroupcode = productgroupcode;
    }
    public String getitemgrouptype() {
        return itemgrouptype;
    }

    public void setitemgrouptype(String itemgrouptype) {
        this.itemgrouptype = itemgrouptype;
    }
    public String getno_() {
        return no_;
    }

    public void setno_(String no_) {
        this.no_ = no_;
    }
    public String getUnitOfMeasure() {
        return UnitOfMeasure;
    }

    public void setUnitOfMeasure(String UnitOfMeasure) {
        this.UnitOfMeasure = UnitOfMeasure;
    }
}