package androidltg.stanstudios.com.dmsmanager;

import android.graphics.Bitmap;

/**
 * Created by DMSv4 on 9/8/2015.
 */
public class SalesHeader {
    private String OrderNo, Ref_Area, Ref_Table, CustomerNo, CreateDate,EndDate, CustomerName, SalerNo_, SalerName;
    private String Description;
    private String STT;
    private float AmountTotal,AmountCustomer,AmountReturn,DiscountPT,Discount;
    private int Type, TypeOrder, status;

    public SalesHeader() {

    }

    public SalesHeader(String Ref_Area, String EndDate,String Ref_Table, String CustomerName, String SalerNo_, String SalerName, int TypeOrder, int status, float AmountTotal,float AmountReturn,float DiscountPT,float Discount,float AmountCustomer, String OrderNo, String CreateDate, String CustomerNo, String Description, String STT, int Type) {
        super();
        this.OrderNo = OrderNo;
        this.AmountTotal = AmountTotal;
        this.AmountCustomer =AmountCustomer;
        this.AmountReturn =AmountReturn;
        this.CreateDate = CreateDate;
        this.CustomerNo = CustomerNo;
        this.Description = Description;
        this.STT = STT;
        this.Ref_Area = Ref_Area;
        this.Ref_Table = Ref_Table;
        this.CustomerName = CustomerName;
        this.SalerName = SalerName;
        this.SalerNo_ = SalerNo_;
        this.TypeOrder = TypeOrder;
        this.status = status;
        this.Type = Type;
        this.DiscountPT =DiscountPT;
        this.Discount =Discount;
        this.EndDate =EndDate;
    }

    public String getRef_Area() {
        return Ref_Area;
    }

    public void setRef_Area(String Ref_Area) {
        this.Ref_Area = Ref_Area;
    }

    public String getRef_Table() {
        return Ref_Table;
    }

    public void setRef_Table(String Ref_Table) {
        this.Ref_Table = Ref_Table;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getSalerNo_() {
        return SalerNo_;
    }

    public void setSalerNo_(String SalerNo_) {
        this.SalerNo_ = SalerNo_;
    }

    public String getSalerName() {
        return SalerName;
    }

    public void setSalerName(String SalerName) {
        this.SalerName = SalerName;
    }

    public String getCustomerNo() {
        return CustomerNo;
    }

    public void setCustomerNo(String CustomerNo) {
        this.CustomerNo = CustomerNo;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }

    public String getCreateDate() {
        return CreateDate;
    }
    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setOrderNo(String OrderNo) {
        this.OrderNo = OrderNo;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public String getSTT() {
        return STT;
    }

    public void setSTT(String STT) {
        this.STT = STT;
    }

    public int getTypeOrder() {
        return TypeOrder;
    }

    public void setTypeOrder(int TypeOrder) {
        this.TypeOrder = TypeOrder;
    }

    public int getstatus() {
        return status;
    }

    public void setstatus(int status) {
        this.status = status;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public float getAmountTotal() {
        return AmountTotal;
    }

    public void setAmountTotal(float AmountTotal) {
        this.AmountTotal = AmountTotal;
    }
    public float getAmountCustomer() {
        return AmountCustomer;
    }

    public void setAmountCustomer(float AmountCustomer) {
        this.AmountCustomer = AmountCustomer;
    }
    public float getAmountReturn() {
        return AmountReturn;
    }

    public void setAmountReturn(float AmountReturn) {
        this.AmountReturn = AmountReturn;
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