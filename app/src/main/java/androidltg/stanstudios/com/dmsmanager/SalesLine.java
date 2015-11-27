package androidltg.stanstudios.com.dmsmanager;

/**
 * Created by DMSv4 on 9/9/2015.
 */
public class SalesLine {
    private String Barcode, ItemNo_, UnitOfMeasure, RefNo_, ItemName,Description;


    private float ValuedQuantity, UnitPrice;
    private int Type,ItemType;
    private float StatusPrint,DiscountPT,Discount;

    public SalesLine() {

    }

    public SalesLine(int ItemType,String ItemNo_,String Description, String UnitOfMeasure,float DiscountPT,float Discount, float ValuedQuantity, String Barcode, String ItemName, String RefNo_, float UnitPrice, int Type,float StatusPrint) {
        super();
        this.ItemType = ItemType;
        this.Barcode = Barcode;
        this.ValuedQuantity = ValuedQuantity;
        this.ItemName = ItemName;
        this.RefNo_ = RefNo_;
        this.UnitPrice = UnitPrice;
        this.ItemNo_ = ItemNo_;
        this.UnitOfMeasure = UnitOfMeasure;
        this.Type = Type;
        this.Description =Description;
        this.StatusPrint = StatusPrint;
        this.DiscountPT =DiscountPT;
        this.Discount =Discount;
    }

    public String getItemNo_() {
        return ItemNo_;
    }

    public void setItemNo_(String ItemNo_) {
        this.ItemNo_ = ItemNo_;
    }
    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getUnitOfMeasure() {
        return UnitOfMeasure;
    }

    public void setUnitOfMeasure(String UnitOfMeasure) {
        this.UnitOfMeasure = UnitOfMeasure;
    }


    public String getRefNo_() {
        return RefNo_;
    }

    public void setRefNo_(String RefNo_) {
        this.RefNo_ = RefNo_;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setBarcode(String Barcode) {
        this.Barcode = Barcode;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public int getItemType() {
        return ItemType;
    }

    public void setItemType(int ItemType) {
        this.ItemType = ItemType;
    }
    public float getStatusPrint() {
        return StatusPrint;
    }

    public void setStatusPrint(float StatusPrint) {
        this.StatusPrint = StatusPrint;
    }


    public float getValuedQuantity() {
        return ValuedQuantity;
    }

    public void setValuedQuantity(float ValuedQuantity) {
        this.ValuedQuantity = ValuedQuantity;
    }

    public float getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(float UnitPrice) {
        this.UnitPrice = UnitPrice;
    }
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


}