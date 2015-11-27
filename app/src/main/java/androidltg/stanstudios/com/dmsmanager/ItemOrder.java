package androidltg.stanstudios.com.dmsmanager;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by DMSv4 on 9/3/2015.
 */
public class ItemOrder implements Parcelable {
    private String quantity;
    private String name,no_,OrderNo;
    private String prices;
    private String description,UnitOfMeasure;
    private float Statusprint,DiscountPT,Discount;
    private int Type;

    public ItemOrder() {

    }

    public String getquantity() {
        return quantity;
    }
    public void setquantity(String quantity) {
        this.quantity = quantity;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getOrderNo() {
        return OrderNo;
    }
    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }
    public String getprices() {
        return prices;
    }
    public void setprices(String prices) {
        this.prices = prices;
    }
    public String getdescription() {
        return description;
    }
    public void setdescription(String description) {
        this.description = description;
    }
    public String getUnitOfMeasure() {
        return UnitOfMeasure;
    }
    public void setUnitOfMeasure(String UnitOfMeasure) {
        this.UnitOfMeasure = UnitOfMeasure;
    }
    public String getno_() {
        return no_;
    }
    public void setno_(String no_) {
        this.no_ = no_;
    }
    public void  setStatusprint(float Statusprint) {this.Statusprint =Statusprint;}
    public float getStatusprint(){return  Statusprint;}
    public void  setType(int Type) {this.Type =Type;}
    public int getType(){return  Type;}
    public float getDiscountPT() {
        return DiscountPT;
    }

    public void setDiscountPT(float DiscountPT) {
        this.DiscountPT = DiscountPT;
    }
    public float getDiscount() {
        return Discount;
    }

    public void setDiscount(float Discount) {
        this.Discount = Discount;
    }
    public ItemOrder(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<ItemOrder> CREATOR = new Parcelable.Creator<ItemOrder>() {
        public ItemOrder createFromParcel(Parcel in) {
            return new ItemOrder(in);
        }

        public ItemOrder[] newArray(int size) {

            return new ItemOrder[size];
        }

    };

    public void readFromParcel(Parcel in) {
        quantity = in.readString();
        name = in.readString();
        no_ = in.readString();
        OrderNo = in.readString();
        prices = in.readString();
        description = in.readString();
        UnitOfMeasure = in.readString();
        Statusprint = in.readFloat();


    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(quantity);
        dest.writeString(name);
        dest.writeString(no_);
        dest.writeString(OrderNo);
        dest.writeString(prices);
        dest.writeString(description);
        dest.writeString(UnitOfMeasure);
        dest.writeFloat(Statusprint);
    }
}
