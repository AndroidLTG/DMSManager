package androidltg.stanstudios.com.dmsmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by DMSv4 on 8/18/2015.
 */
public class MyGlobal {
    private static String updatesh, updatesl, updatetable;
    private static boolean checkupdatetable = false, checkupdatesh = false, checkupdatesl = false;
    public static boolean isOnline;
    public static final String DATABASENAME = "data.db";
    public static SQLiteDatabase database;
    public static String SalerNo="",SalerName="", SourceCode = "", ResponsibilityCenter = "",LoginTime="",Role="";
    public static int NumPage=0,index = 0,totalindex=1,lastindexpage=0, indexpage = 0;

    //REMOVE SALESHEADEr
    public static final String SOAP_ACTION_REMOVESH = "http://tempuri.org/LTG_RemoveSH";
    public static final String METHOD_NAME_REMOVESH = "LTG_RemoveSH";


    //CHUYEN BAN
    public static final String SOAP_ACTION_CHUYENBAN = "http://tempuri.org/LTG_ChuyenBan";
    public static final String METHOD_NAME_CHUYENBAN = "LTG_ChuyenBan";

    //GOP BAN
    public static final String SOAP_ACTION_GOPBAN = "http://tempuri.org/LTG_GopBan";
    public static final String METHOD_NAME_GOPBAN = "LTG_GopBan";
    //GET LOGIN

    public static final String TABLE_LOGIN = "Retail_UserSetup";
    public static final String COLUMNLOGIN_STT = "stt";
    public static final String COLUMNLOGIN_UserID = "UserID";
    public static final String COLUMNLOGIN_Name = "Name";
    public static final String COLUMNLOGIN_Password = "Password";
    public static final String COLUMNLOGIN_SourceCode = "SourceCode";
    public static final String COLUMNLOGIN_ResponsibilityCenter = "ResponsibilityCenter";
    public static final String COLUMNLOGIN_ROLE = "Role";
    public static final String COLUMNLOGIN_LoginTime = "LoginTime";
    public static final String SOAP_ACTION_LOGIN = "http://tempuri.org/LTG_GetLoGin";
    public static final String METHOD_NAME_LOGIN = "LTG_GetLoGin";
    //CHANGE STATUS

    //ZONE
    public static final String TABLE_ZONE = "Retail_Area";
    public static final String COLUMNZONE_STT = "stt";
    public static final String COLUMNZONE_No = "No_";
    public static final String COLUMNZONE_NAME = "Name";
    public static final String COLUMNZONE_SourceCode = "SourceCode";
    public static final String COLUMNZONE_ResponsibilityCenter = "ResponsibilityCenter";
    public static final String COLUMNZONE_Description = "Description";
    public static final String COLUMNZONE_Status = "Status";
    public static final String COLUMNZONE_LinkPicture = "LinkPicture";
    public static final String SOAP_ACTION_ZONE = "http://tempuri.org/LTG_getRetail_Area";
    public static final String METHOD_NAME_ZONE = "LTG_getRetail_Area";


    //TABLE
    public static final String TABLE_TABLE = "Retail_TABLE";
    public static final String COLUMNTABLE_STT = "stt";
    public static final String COLUMNTABLE_No = "No_";
    public static final String COLUMNTABLE_NAME = "Name";
    public static final String COLUMNTABLE_Ref_Area = "Ref_Area";
    public static final String COLUMNTABLE_SourceCode = "SourceCode";
    public static final String COLUMNTABLE_ResponsibilityCenter = "ResponsibilityCenter";
    public static final String COLUMNTABLE_Description = "Description";
    public static final String COLUMNTABLE_Status = "Status";
    public static final String COLUMNTABLE_Type = "Type";
    public static final String COLUMNTABLE_StartDate = "StartDate";
    public static final String COLUMNTABLE_EndDate = "EndDate";
    public static final String COLUMNTABLE_OrderNo = "OrderNo";
    public static final String COLUMNTABLE_LinkPicture = "LinkPicture";
    public static final String COLUMNTABLE_isSync = "isSync";
    public static final String SOAP_ACTION_TABLE = "http://tempuri.org/LTG_getRetail_Table_ByArea";
    public static final String METHOD_NAME_TABLE = "LTG_getRetail_Table_ByArea";
    public static final String SOAP_ACTION_UPDATETB = "http://tempuri.org/LTG_SendTB";
    public static final String METHOD_NAME_UPDATETB = "LTG_SendTB";
    //TABLE OPENING
    public static final String TABLE_OPEN = "Retail_TABLE_OPEN";
    public static final String COLUMNTABLEOPEN_STT = "stt";
    public static final String COLUMNTABLEOPEN_No = "No_";
    public static final String COLUMNTABLEOPEN_NAME = "Name";
    public static final String COLUMNTABLEOPEN_Ref_Area = "Ref_Area";
    public static final String COLUMNTABLEOPEN_SourceCode = "SourceCode";
    public static final String COLUMNTABLEOPEN_ResponsibilityCenter = "ResponsibilityCenter";
    public static final String COLUMNTABLEOPEN_StartDate = "StartDate";
    public static final String COLUMNTABLEOPEN_OrderNo = "OrderNo";
    public static final String SOAP_ACTION_TABLEOPEN = "http://tempuri.org/LTG_getRetail_Table_Status";
    public static final String METHOD_NAME_TABLEOPEN = "LTG_getRetail_Table_Status";
    //SALESHEADER
    public static final String TABLE_SH = "Retail_SH";
    public static final String COLUMNSH_STT = "STT";
    public static final String COLUMNSH_id = "ID";
    public static final String COLUMNSH_OrderNo = "OrderNo";
    public static final String COLUMNSH_CustomerNo = "CustomerNo";
    public static final String COLUMNSH_CreateDate = "CreateDate";
    public static final String COLUMNSH_EndDate = "EndDate";
    public static final String COLUMNSH_Description = "Description";
    public static final String COLUMNSH_AmountTotal = "AmountTotal";
    public static final String COLUMNSH_Type = "Type";
    public static final String COLUMNSH_CustomerName = "CustomerName";
    public static final String COLUMNSH_SalerNo = "SalerNo";
    public static final String COLUMNSH_SalerName = "SalerName";
    public static final String COLUMNSH_TypeOrder = "TypeOrder";
    public static final String COLUMNSH_Ref_Area = "Ref_Area";
    public static final String COLUMNSH_Ref_Table = "Ref_Table";
    public static final String COLUMNSH_Status = "Status";
    public static final String COLUMNSH_AmountCustomer = "AmountCustomer";
    public static final String COLUMNSH_AmountReturn = "AmountReturn";
    public static final String COLUMNSH_isSync = "isSync";
    public static final String SOAP_ACTION_SH = "http://tempuri.org/LTG_SendSH";
    public static final String METHOD_NAME_SH = "LTG_SendSH";
    public static final String COLUMNSH_DiscountPT= "DiscountPT";
    public static final String COLUMNSH_Discount= "Discount";

    //SALESLINE
    public static final String TABLE_SL = "Retail_SL";
    public static final String COLUMNSL_id = "ID";
    public static final String COLUMNSL_Description = "Description";
    public static final String COLUMNSL_Status = "Status";
    public static final String COLUMNSL_Barcode = "Barcode";
    public static final String COLUMNSL_ItemNo_ = "ItemNo_";
    public static final String COLUMNSL_UnitOfMeasure = "UnitOfMeasure";
    public static final String COLUMNSL_ValuedQuantity = "ValuedQuantity";
    public static final String COLUMNSL_UnitPrice = "UnitPrice";
    public static final String COLUMNSL_Type = "Type";
    public static final String COLUMNSL_RefNo_ = "RefNo_";
    public static final String COLUMNSL_ItemName = "ItemName";
    public static final String COLUMNSL_isSync = "isSync";
    public static final String COLUMNSL_DiscountPT="DiscountPT";
    public static final String COLUMNSL_Discount = "Discount";
    public static final String COLUMNSL_ItemType = "ItemType";
    public static final String SOAP_ACTION_SL = "http://tempuri.org/LTG_SendSL";
    public static final String METHOD_NAME_SL = "LTG_SendSL";
    //SALESHEADERADMIN
    public static final String TABLE_SH_AD = "Retail_SH_AD";
    public static final String COLUMNSH_ADSTT = "STT";
    public static final String COLUMNSH_ADid = "ID";
    public static final String COLUMNSH_ADOrderNo = "OrderNo";
    public static final String COLUMNSH_ADCustomerNo = "CustomerNo";
    public static final String COLUMNSH_ADCreateDate = "CreateDate";
    public static final String COLUMNSH_ADEndDate = "EndDate";
    public static final String COLUMNSH_ADDescription = "Description";
    public static final String COLUMNSH_ADAmountTotal = "AmountTotal";
    public static final String COLUMNSH_ADType = "Type";
    public static final String COLUMNSH_ADCustomerName = "CustomerName";
    public static final String COLUMNSH_ADSalerNo = "SalerNo";
    public static final String COLUMNSH_ADSalerName = "SalerName";
    public static final String COLUMNSH_ADTypeOrder = "TypeOrder";
    public static final String COLUMNSH_ADRef_Area = "Ref_Area";
    public static final String COLUMNSH_ADRef_Table = "Ref_Table";
    public static final String COLUMNSH_ADStatus = "Status";
    public static final String COLUMNSH_ADAmountCustomer = "AmountCustomer";
    public static final String COLUMNSH_ADAmountReturn = "AmountReturn";
    public static final String SOAP_ACTION_SH_AD = "http://tempuri.org/LTG_getSH_byDateandSaleName";
    public static final String METHOD_NAME_SH_AD = "LTG_getSH_byDateandSaleName";
    public static final String COLUMNSH_ADDiscountPT= "DiscountPT";
    public static final String COLUMNSH_ADDiscount= "Discount";

    //SALESLINE
    public static final String TABLE_SL_AD = "Retail_SL_AD";
    public static final String COLUMNSL_ADid = "ID";
    public static final String COLUMNSL_ADDescription = "Description";
    public static final String COLUMNSL_ADStatus = "Status";
    public static final String COLUMNSL_ADBarcode = "Barcode";
    public static final String COLUMNSL_ADItemNo_ = "ItemNo_";
    public static final String COLUMNSL_ADUnitOfMeasure = "UnitOfMeasure";
    public static final String COLUMNSL_ADValuedQuantity = "ValuedQuantity";
    public static final String COLUMNSL_ADUnitPrice = "UnitPrice";
    public static final String COLUMNSL_ADType = "Type";
    public static final String COLUMNSL_ADRefNo_ = "RefNo_";
    public static final String COLUMNSL_ADItemName = "ItemName";
    public static final String COLUMNSL_ADDiscountPT="DiscountPT";
    public static final String COLUMNSL_ADDiscount = "Discount";
    public static final String COLUMNSL_ADItemType = "ItemType";
    public static final String SOAP_ACTION_SL_AD = "http://tempuri.org/LTG_getSL_byNo_";
    public static final String METHOD_NAME_SL_AD = "LTG_getSL_byNo_";

    //ITEMs
    public static final String TABLE_ITEM = "Retail_Item";

    public static final String COLUMNITEM_STT = "stt";
    public static final String COLUMNITEM_NAME_SEARCH ="NameSearch";
    public static final String COLUMNITEM_ItemType ="ItemType";
    public static final String COLUMNITEM_No = "No_";
    public static final String COLUMNITEM_NAME = "Name";
    public static final String COLUMNITEM_Description = "Description";
    public static final String COLUMNITEM_SalesUnitOfMeasure = "SalesUnitOfMeasure";
    public static final String COLUMNITEM_ItemDisc_Group = "ItemDisc_Group";
    public static final String COLUMNITEM_SourceCode = "SourceCode";
    public static final String COLUMNITEM_ResponsibilityCenter = "ResponsibilityCenter";
    public static final String COLUMNITEM_ItemCategoryCode = "ItemCategoryCode";
    public static final String COLUMNITEM_ProductGroupCode = "ProductGroupCode";
    public static final String COLUMNITEM_SalesUnitPrice = "SalesUnitPrice";
    public static final String COLUMNITEM_LINKPICTURE = "LinkPicture";
    public static final String SOAP_ACTION_getSL = "http://tempuri.org/LTG_getSL_byTableandStatus";
    public static final String METHOD_NAME_getSL = "LTG_getSL_byTableandStatus";
    public static final String SOAP_ACTION_getSH = "http://tempuri.org/LTG_getSH_byTableandStatus";
    public static final String METHOD_NAME_getSH = "LTG_getSH_byTableandStatus";
    public static final String SOAP_ACTION_ITEM = "http://tempuri.org/LTG_getRetail_Item_Page";
    public static final String METHOD_NAME_ITEM = "LTG_getRetail_Item_Page";
    public static final String SOAP_ACTION_GETNUMITEM = "http://tempuri.org/LTG_getRetail_Num_Item";
    public static final String METHOD_NAME_GETNUMITEM = "LTG_getRetail_Num_Item";
    public static final String NAMESPACE = "http://tempuri.org/";
    public static final String URL = "http://vietdms.com:8045/mywebservice.asmx?WSDL";
    public static Context context = null;

    //Convert UTF8
    private static char[] charA = { 'à', 'á', 'ạ', 'ả', 'ã',// 0-&gt;16
            'â', 'ầ', 'ấ', 'ậ', 'ẩ', 'ẫ', 'ă', 'ằ', 'ắ', 'ặ', 'ẳ', 'ẵ' };// a,// ă,// â
    private static char[] charE = { 'ê', 'ề', 'ế', 'ệ', 'ể', 'ễ',// 17-&gt;27
            'è', 'é', 'ẹ', 'ẻ', 'ẽ' };// e
    private static char[] charI = { 'ì', 'í', 'ị', 'ỉ', 'ĩ' };// i 28-&gt;32
    private static char[] charO = { 'ò', 'ó', 'ọ', 'ỏ', 'õ',// o 33-&gt;49
            'ô', 'ồ', 'ố', 'ộ', 'ổ', 'ỗ',// ô
            'ơ', 'ờ', 'ớ', 'ợ', 'ở', 'ỡ' };// ơ
    private static char[] charU = { 'ù', 'ú', 'ụ', 'ủ', 'ũ',// u 50-&gt;60
            'ư', 'ừ', 'ứ', 'ự', 'ử', 'ữ' };// ư
    private static char[] charY = { 'ỳ', 'ý', 'ỵ', 'ỷ', 'ỹ' };// y 61-&gt;65
    private static char[] charD = { 'đ', ' ' }; // 66-67
    private static String charact = String.valueOf(charA, 0, charA.length)
            + String.valueOf(charE, 0, charE.length)
            + String.valueOf(charI, 0, charI.length)
            + String.valueOf(charO, 0, charO.length)
            + String.valueOf(charU, 0, charU.length)
            + String.valueOf(charY, 0, charY.length)
            + String.valueOf(charD, 0, charD.length);

    private static char GetAlterChar(char pC) {
        if ((int) pC == 32) {
            return ' ';
        }

        char tam = pC;// Character.toLowerCase(pC);

        int i = 0;
        while (i < charact.length() && charact.charAt(i) != tam) {
            i++;
        }
        if (i < 0 || i > 67)
            return pC;

        if (i == 66) {
            return 'd';
        }
        if (i >= 0 && i <= 16) {
            return 'a';
        }
        if (i >= 17 && i <= 27) {
            return 'e';
        }
        if (i >= 28 && i <= 32) {
            return 'i';
        }
        if (i >= 33 && i <= 49) {
            return 'o';
        }
        if (i >= 50 && i <= 60) {
            return 'u';
        }
        if (i >= 61 && i <= 65) {
            return 'y';
        }
        return pC;
    }
    //Convert String Unicode
    public static String ConvertString(String pStr) {
        String convertString = pStr.toLowerCase();
        Character[] returnString = new Character[convertString.length()];
        for (int i = 0; i < convertString.length(); i++) {
            char temp = convertString.charAt(i);
            if ((int) temp < 97 || temp > 122) {
                char tam1 = GetAlterChar(temp);
                if ((int) temp != 32)
                    convertString = convertString.replace(temp, tam1);
            }
        }
        return convertString;
    }

    //check online
    public static boolean isOnline()//Check online
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }
        catch (Exception e)
        {
            return false;
        }
    }
    //create database login
    public static SQLiteDatabase cgdatabaselogin() {
        try {
            database = context.openOrCreateDatabase(DATABASENAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if (database != null) {
                if (isTableExists(database, TABLE_LOGIN))
                    return database;
                database.setLocale(Locale.getDefault());
                database.setVersion(1);
                String sql = "create table " + TABLE_LOGIN + " ("
                        + COLUMNLOGIN_STT + " integer primary key autoincrement, "
                        + COLUMNLOGIN_UserID + " text, "
                        + COLUMNLOGIN_Password + " text, "
                        + COLUMNLOGIN_Name +" text,"
                        + COLUMNLOGIN_SourceCode + " text, "
                        + COLUMNLOGIN_ResponsibilityCenter + " text, "
                        + COLUMNLOGIN_LoginTime + " text, "
                        + COLUMNLOGIN_ROLE + " text)";


                database.execSQL(sql);

            }
        } catch (Exception e) {
        }
        return database;
    }

    //create database zone
    public static SQLiteDatabase cgdatabasezone() {
        try {
            database = context.openOrCreateDatabase(DATABASENAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if (database != null) {
                if (isTableExists(database, TABLE_ZONE))
                    return database;
                database.setLocale(Locale.getDefault());
                database.setVersion(1);
                String sql = "create table " + TABLE_ZONE + " ("
                        + COLUMNZONE_STT + " integer primary key autoincrement, "
                        + COLUMNZONE_No + " text, "
                        + COLUMNZONE_NAME + " text, "
                        + COLUMNZONE_SourceCode + " text, "
                        + COLUMNZONE_ResponsibilityCenter + " text, "
                        + COLUMNZONE_Description + " text, "
                        + COLUMNZONE_Status + " integer, "
                        + COLUMNZONE_LinkPicture + " text)";


                database.execSQL(sql);

            }
        } catch (Exception e) {
        }
        return database;
    }


    //create database table
    public static SQLiteDatabase cgdatabasetable() {
        try {
            database = context.openOrCreateDatabase(DATABASENAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if (database != null) {
                if (isTableExists(database, TABLE_TABLE))
                    return database;
                database.setLocale(Locale.getDefault());
                database.setVersion(1);
                String sql = "create table " + TABLE_TABLE + " ("
                        + COLUMNTABLE_STT + " integer primary key autoincrement,"
                        + COLUMNTABLE_No + " text, "
                        + COLUMNTABLE_NAME + " text, "
                        + COLUMNTABLE_Ref_Area + " text, "
                        + COLUMNTABLE_SourceCode + " text, "
                        + COLUMNTABLE_ResponsibilityCenter + " text, "
                        + COLUMNTABLE_Status + " integer,"
                        + COLUMNTABLE_Type + " text, "
                        + COLUMNTABLE_Description + " text, "
                        + COLUMNTABLE_LinkPicture + " text, "
                        + COLUMNTABLE_StartDate + " text, "
                        + COLUMNTABLE_EndDate + " text, "
                        + COLUMNTABLE_OrderNo + " text, "
                        + COLUMNTABLE_isSync + " integer)";


                database.execSQL(sql);

            }
        } catch (Exception e) {
        }
        return database;
    }

    //create database table openning
    public static SQLiteDatabase cgdatabaseopen() {
        try {
            database = context.openOrCreateDatabase(DATABASENAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if (database != null) {
                if (isTableExists(database, TABLE_OPEN))
                    return database;
                database.setLocale(Locale.getDefault());
                database.setVersion(1);
                String sql = "create table " + TABLE_OPEN + " ("
                        + COLUMNTABLEOPEN_STT + " integer primary key autoincrement,"
                        + COLUMNTABLEOPEN_No + " text, "
                        + COLUMNTABLEOPEN_NAME + " text, "
                        + COLUMNTABLEOPEN_Ref_Area + " text, "
                        + COLUMNTABLEOPEN_SourceCode + " text, "
                        + COLUMNTABLEOPEN_ResponsibilityCenter + " text, "
                        + COLUMNTABLEOPEN_StartDate + " text, "
                        + COLUMNTABLEOPEN_OrderNo + " text)";


                database.execSQL(sql);

            }
        } catch (Exception e) {
        }
        return database;
    }

    //create database item
    public static SQLiteDatabase cgdatabaseitem() {
        try {
            database = context.openOrCreateDatabase(DATABASENAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if (database != null) {
                if (isTableExists(database, TABLE_ITEM))
                    return database;
                database.setLocale(Locale.getDefault());
                database.setVersion(1);
                String sql = "create table " + TABLE_ITEM + " ("
                        + COLUMNITEM_STT + " integer primary key autoincrement, "
                        + COLUMNITEM_No + " text, "
                        + COLUMNITEM_NAME + " text, "
                        + COLUMNITEM_Description + " text, "
                        + COLUMNITEM_SalesUnitOfMeasure + " text, "
                        + COLUMNITEM_SourceCode + " text, "
                        + COLUMNITEM_ResponsibilityCenter + " text, "
                        + COLUMNITEM_ItemCategoryCode + " text, "
                        + COLUMNITEM_ProductGroupCode + " text, "
                        + COLUMNITEM_SalesUnitPrice + " float,"
                        + COLUMNITEM_LINKPICTURE + " text,"
                        + COLUMNITEM_ItemDisc_Group + " text,"
                        + COLUMNITEM_NAME_SEARCH + " text,"
                        + COLUMNITEM_ItemType + " integer)";


                database.execSQL(sql);

            }
        } catch (Exception e) {

        }
        return database;
    }

    //create database salesheader
    public static SQLiteDatabase cgdatabasesh() {
        try {
            database = context.openOrCreateDatabase(DATABASENAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if (database != null) {
                if (isTableExists(database, TABLE_SH))
                    return database;
                database.setLocale(Locale.getDefault());
                database.setVersion(1);
                String sql = "create table " + TABLE_SH + " ("
                        + COLUMNSH_id + " integer primary key autoincrement, "
                        + COLUMNSH_OrderNo + " text, "
                        + COLUMNSH_CustomerNo + " text, "
                        + COLUMNSH_CreateDate + " text, "
                        + COLUMNSH_Description + " text, "
                        + COLUMNSH_AmountTotal + " float, "
                        + COLUMNSH_Type + " integer, "
                        + COLUMNSH_STT + " text, "
                        + COLUMNSH_CustomerName + " text, "
                        + COLUMNSH_SalerNo + " text, "
                        + COLUMNSH_SalerName + " float,"
                        + COLUMNSH_TypeOrder + " integer,"
                        + COLUMNSH_Ref_Area + " text,"
                        + COLUMNSH_Ref_Table + " text,"
                        + COLUMNSH_Status + " integer,"
                        + COLUMNSH_AmountCustomer + " float,"
                        + COLUMNSH_AmountReturn + " float,"
                        + COLUMNSH_isSync + " integer,"
                        + COLUMNSH_DiscountPT+" float,"
                        + COLUMNSH_Discount+" float,"
                        + COLUMNSH_EndDate+" text)";


                database.execSQL(sql);
            }
        } catch (Exception e) {
        }
        return database;
    }

    //create database salesline
    public static SQLiteDatabase cgdatabasesl() {
        try {
            database = context.openOrCreateDatabase(DATABASENAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if (database != null) {
                if (isTableExists(database, TABLE_SL))
                    return database;
                database.setLocale(Locale.getDefault());
                database.setVersion(1);
                String sql = "create table " + TABLE_SL + " ("
                        + COLUMNSL_id + " integer primary key autoincrement, "
                        + COLUMNSL_Barcode + " text, "
                        + COLUMNSL_ItemNo_ + " text, "
                        + COLUMNSL_UnitOfMeasure + " text, "
                        + COLUMNSL_ValuedQuantity + " float, "
                        + COLUMNSL_UnitPrice + " float, "
                        + COLUMNSL_RefNo_ + " text,"
                        + COLUMNSL_ItemName + " text, "
                        + COLUMNSL_Type + " integer,"
                        + COLUMNSL_Status + " float,"
                        + COLUMNSL_Description + " text, "
                        + COLUMNSL_isSync + " integer,"
                        + COLUMNSL_DiscountPT+ " float,"
                        + COLUMNSL_Discount+ " float,"
                        + COLUMNSL_ItemType+" integer)";
                ;


                database.execSQL(sql);

            }
        } catch (Exception e) {

        }
        return database;
    }
    //create database salesheader admin
    public static SQLiteDatabase cgdatabaseshad() {
        try {
            database = context.openOrCreateDatabase(DATABASENAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if (database != null) {
                if (isTableExists(database, TABLE_SH_AD))
                    return database;
                database.setLocale(Locale.getDefault());
                database.setVersion(1);
                String sql = "create table " + TABLE_SH_AD + " ("
                        + COLUMNSH_ADid + " integer primary key autoincrement, "
                        + COLUMNSH_ADOrderNo + " text, "
                        + COLUMNSH_ADCustomerNo + " text, "
                        + COLUMNSH_ADCreateDate + " text, "
                        + COLUMNSH_ADDescription + " text, "
                        + COLUMNSH_ADAmountTotal + " float, "
                        + COLUMNSH_ADType + " integer, "
                        + COLUMNSH_ADSTT + " text, "
                        + COLUMNSH_ADCustomerName + " text, "
                        + COLUMNSH_ADSalerNo + " text, "
                        + COLUMNSH_ADSalerName + " float,"
                        + COLUMNSH_ADTypeOrder + " integer,"
                        + COLUMNSH_ADRef_Area + " text,"
                        + COLUMNSH_ADRef_Table + " text,"
                        + COLUMNSH_ADStatus + " integer,"
                        + COLUMNSH_ADAmountCustomer + " float,"
                        + COLUMNSH_ADAmountReturn + " float,"
                        + COLUMNSH_ADDiscountPT+" float,"
                        + COLUMNSH_ADDiscount+" float,"
                        + COLUMNSH_ADEndDate+" text)";


                database.execSQL(sql);
            }
        } catch (Exception e) {
        }
        return database;
    }

    //create database salesline admin
    public static SQLiteDatabase cgdatabaseslad() {
        try {
            database = context.openOrCreateDatabase(DATABASENAME, SQLiteDatabase.CREATE_IF_NECESSARY, null);
            if (database != null) {
                if (isTableExists(database, TABLE_SL_AD))
                    return database;
                database.setLocale(Locale.getDefault());
                database.setVersion(1);
                String sql = "create table " + TABLE_SL_AD + " ("
                        + COLUMNSL_ADid + " integer primary key autoincrement, "
                        + COLUMNSL_ADBarcode + " text, "
                        + COLUMNSL_ADItemNo_ + " text, "
                        + COLUMNSL_ADUnitOfMeasure + " text, "
                        + COLUMNSL_ADValuedQuantity + " float, "
                        + COLUMNSL_ADUnitPrice + " float, "
                        + COLUMNSL_ADRefNo_ + " text,"
                        + COLUMNSL_ADItemName + " text, "
                        + COLUMNSL_ADType + " integer,"
                        + COLUMNSL_ADStatus + " float,"
                        + COLUMNSL_ADDescription + " text, "
                        + COLUMNSL_ADDiscountPT+ " float,"
                        + COLUMNSL_ADDiscount+ " float,"
                        + COLUMNSL_ADItemType+" integer)";
                ;


                database.execSQL(sql);

            }
        } catch (Exception e) {

        }
        return database;
    }
    //create database order

    //get time current
    public static String gettimecurrent() {
        Calendar c = Calendar.getInstance();


        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        if (hour < 10 && minute < 10) return "0" + hour + ":" + "0" + minute;
        if (minute < 10) return hour + ":" + "0" + minute;
        if (hour < 10) return "0" + hour + ":" + minute;
        return hour + ":" + minute;
    }
    public static String gettimeformat() {
        Calendar c = Calendar.getInstance();

        int ye = c.get(Calendar.YEAR);
        String year = String.valueOf(ye).substring(2);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        int milisecond = c.get(Calendar.MILLISECOND);
        int dat = c.get(Calendar.AM_PM);
        String date="AM";
        if(dat==0) date="AM";
        if(dat==1) date="PM";
        if (hour < 10 && minute < 10 && second<10) return year+"0" + hour  + "0" + minute+"0"+second+milisecond+date;
        if (hour<10 && minute < 10 ) return year+ "0"+hour  + "0" + minute+second+milisecond+date;
        if (hour < 10 && second<10) return year+"0" + hour  + minute+"0"+second+milisecond+date;
        if( minute<10 && second<10) return year+hour+"0"+minute+"0"+second+milisecond+date;
        if(hour<10) return year+"0"+hour+minute+second+milisecond+date;
        if(minute<10) return year+hour+"0"+minute+second+milisecond+date;
        if(second<10) return year+hour+minute+"0"+second+milisecond+date;
        //  int seconds = c.get(Calendar.SECOND);
        return year+hour+minute+second+milisecond+date;
    }

    //get day current
    public static String getdaycurrent() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (day < 10 & (month + 1) < 10) return year + "-0" + (month + 1) + "-0" + day + " ";
        if (day < 10) return year + "-" + (month + 1) + "-0" + day + " ";
        if ((month + 1) < 10) return year + "-0" + (month + 1) + "-" + day + " ";
        return year + "-" + (month + 1) + "-" + day + " ";
    }


    public static boolean isTableEmpty(SQLiteDatabase database, String tablename) {

        boolean empty = true;

        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM " + tablename, null);
        if (cursor != null && cursor.moveToFirst()) {
            empty = (cursor.getInt(0) == 0);
        }
        cursor.close();
        return empty;
    }


    public static String formatfloat(double d) {
        DecimalFormat formatter = new DecimalFormat("#,###");

        return formatter.format(d).toString();


    }


    public static int dropTable(SQLiteDatabase database, String tablename) {
        return database.delete(tablename, null, null);
    }

    public static boolean isFieldExist(SQLiteDatabase database, String tablename, String fildName, String fildValue) {
        String Query = "SELECT * FROM " + tablename + " WHERE " + fildName + " = '" + fildValue + "'";
        Cursor cursor = database.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }



    public static boolean isLTGFieldExist(SQLiteDatabase database, String tablename, String fildName, String fildValue,String fildName2,String fildValue2) {
        String Query = "SELECT * FROM " + tablename + " WHERE " + fildName + " = '" + fildValue + "'"+" AND "+fildName2+ " = '" + fildValue2 + "'";
        Cursor cursor = database.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    public static boolean isLTGFieldSLExist(SQLiteDatabase database, String tablename, String fildName, String fildValue,String fildName2,String fildValue2,String fildName3,String fildValue3) {
        String Query = "SELECT * FROM " + tablename + " WHERE " + fildName + " = '" + fildValue + "'"+" AND "+fildName2+ " = '" + fildValue2  + "'"+" AND "+fildName3+ " = '" + fildValue3 + "'";
        Cursor cursor = database.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
    public static int getCountTable(SQLiteDatabase database, String tablename, String fildName, String fildValue,String fildName2,String fildValue2) {
        String Query = "SELECT * FROM " + tablename + " WHERE " + fildName + " = '" + fildValue + "'"+" AND "+fildName2+ " = '" + fildValue2 + "'";
        Cursor cursor = database.rawQuery(Query, null);

        return cursor.getCount();
    }
    public static boolean isTableExists(SQLiteDatabase database, String tableName) {
        Cursor cursor = database.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();

                return true;
            }
            cursor.close();
        }
        return false;
    }

    //SYNCDATA

    public static void SyncData() {

        cgdatabasesl();
        cgdatabasezone();
        cgdatabasesh();
        cgdatabasetable();
        cgdatabaseitem();
        getfromserver();

    }

    private static void getfromserver() {
        MyGlobal.cgdatabasezone();
        MyGlobal.cgdatabasetable();
        updatesh = MyGlobal.ConverttoJsonSH();
        updatesl = MyGlobal.ConverttoJsonSL();
        updatetable = MyGlobal.ConverttoJsonTB();
        CAPNHATDULIEU();
    }

    private static String ConverttoJsonSL() {
        String kq = "";
        List<SalesLine> ListLine = loadline();
        if (ListLine != null) {
            Gson gson = new Gson();
            kq = gson.toJson(ListLine);
        }
        return kq;
    }

    private static String ConverttoJsonTB() {
        String kq = "";
        List<TableItem> ListTB = loadtb();
        if (ListTB != null) {
            Gson gson = new Gson();
            kq = gson.toJson(ListTB);
        }
        return kq;
    }

    private static List<TableItem> loadtb() {
        List<TableItem> result = new ArrayList<TableItem>();

        if (MyGlobal.database != null) {
            Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_TABLE, null, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                TableItem ci = new TableItem();
                ci.setNo_(cursor.getString(1));
                ci.setName(cursor.getString(2));
                ci.setRef_Area(cursor.getString(3));
                ci.setSourceCode(cursor.getString(4));
                ci.setResponsibilityCenter(cursor.getString(5));
                ci.setStatus(cursor.getInt(6));
                ci.setDescription(cursor.getString(8));
                ci.setLinkPicture(cursor.getString(9));
                ci.setStartDate(cursor.getString(10));
                ci.setEndDate(cursor.getString(11));
                ci.setOrderNo(cursor.getString(12));
                result.add(ci);

                cursor.moveToNext();
            }
            cursor.close();
        }

        return result;
    }


    private static List<SalesLine> loadline() {
        List<SalesLine> result = new ArrayList<SalesLine>();

        if (MyGlobal.database != null) {
            Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_SL, null, MyGlobal.COLUMNSL_isSync + " = ?", new String[]{"0"}, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                SalesLine ci = new SalesLine();
                ci.setBarcode(cursor.getString(1));
                ci.setItemNo_(cursor.getString(2));
                ci.setUnitOfMeasure(cursor.getString(3));
                ci.setValuedQuantity(cursor.getFloat(4));
                ci.setUnitPrice(cursor.getFloat(5));
                ci.setRefNo_(cursor.getString(6));
                ci.setItemName(cursor.getString(7));
                ci.setType(cursor.getInt(8));
                ci.setStatusPrint(cursor.getInt(9));
                ci.setDescription(cursor.getString(10));
                ci.setDiscountPT(cursor.getFloat(12));
                ci.setDiscount(cursor.getFloat(13));
                result.add(ci);


                cursor.moveToNext();
            }
            cursor.close();
        }

        return result;
    }

    private static String ConverttoJsonSH() {

        String kq = "";
        List<SalesHeader> ListHeader = MyGlobal.loadheader();
        if (ListHeader != null) {
            Gson gson = new Gson();
            kq = gson.toJson(ListHeader);
        }
        return kq;

    }

    private static List<SalesHeader> loadheader() {

        List<SalesHeader> result = new ArrayList<SalesHeader>();

        if (MyGlobal.database != null) {
            Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_SH, null, MyGlobal.COLUMNSH_isSync + " = ?", new String[]{"0"}, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                SalesHeader ci = new SalesHeader();
                ci.setOrderNo(cursor.getString(1));
                ci.setCustomerNo(cursor.getString(2));
                ci.setCreateDate(cursor.getString(3));
                ci.setDescription(cursor.getString(4));
                ci.setAmountTotal(cursor.getFloat(5));
                ci.setType(cursor.getInt(6));
                ci.setSTT(cursor.getString(7));
                ci.setCustomerName(cursor.getString(8));
                ci.setSalerNo_(cursor.getString(9));
                ci.setSalerName(cursor.getString(10));
                ci.setTypeOrder(cursor.getInt(11));
                ci.setRef_Area(cursor.getString(12));
                ci.setRef_Table(cursor.getString(13));
                ci.setstatus(cursor.getInt(14));
                ci.setAmountCustomer(cursor.getFloat(15));
                ci.setAmountReturn(cursor.getFloat(16));
                ci.setDiscountPT(cursor.getFloat(18));
                ci.setDiscount(cursor.getFloat(19));
                result.add(ci);

                cursor.moveToNext();
            }
            cursor.close();
        }

        return result;

    }

    private static void CAPNHATDULIEU() {

        myAsyncTask myRequest = new myAsyncTask();

        myRequest.execute();

    }

    private static class myAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override

        protected void onPostExecute(Void result) {//three

            super.onPostExecute(result);
            if (checkupdatesh) {
                Toast.makeText(context, "Đã đồng bộ SH", Toast.LENGTH_SHORT).show();
                ContentValues args = new ContentValues();
                args.put(MyGlobal.COLUMNTABLE_isSync, 1);

                MyGlobal.database.update(MyGlobal.TABLE_TABLE, args, null, null);
            }
            if (checkupdatesl) {
                Toast.makeText(context, "Đã đồng bộ SL", Toast.LENGTH_SHORT).show();
                ContentValues args = new ContentValues();
                args.put(MyGlobal.COLUMNSL_isSync, 1);
                MyGlobal.database.update(MyGlobal.TABLE_SL, args, null, null);
            }
            if (checkupdatetable) {
                Toast.makeText(context, "Đã đồng bộ Table", Toast.LENGTH_SHORT).show();
                ContentValues args = new ContentValues();
                args.put(MyGlobal.COLUMNSH_isSync, 1);
                MyGlobal.database.update(MyGlobal.TABLE_SH, args, null, null);
            }


        }


        @Override

        protected void onPreExecute() {//second

            super.onPreExecute();

            //dang xu li


        }


        @Override

        protected Void doInBackground(Void... params) {//first

            updatesl = ConverttoJsonSL();
            updatesh = ConverttoJsonSH();
            updateTable(updatetable);
            updateSH(updatesh);
            updateSL(updatesl);

            return null;

        }

        private void updateTable(String senddata) {
            SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_UPDATETB);
            PropertyInfo pi = new PropertyInfo();
            pi.setName("senddata");
            pi.setValue(senddata);
            pi.setType(String.class);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
            try {
                transportSE.call(MyGlobal.SOAP_ACTION_UPDATETB, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                checkupdatetable = Boolean.parseBoolean(response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        private void updateSH(String senddata) {
            SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_SH);
            PropertyInfo pi = new PropertyInfo();
            pi.setName("senddata");
            pi.setValue(senddata);
            pi.setType(String.class);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
            try {
                transportSE.call(MyGlobal.SOAP_ACTION_SH, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                checkupdatesh = Boolean.parseBoolean(response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }

        private void updateSL(String senddata) {
            SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_SL);
            PropertyInfo pi = new PropertyInfo();
            pi.setName("senddata");
            pi.setValue(senddata);
            pi.setType(String.class);
            request.addProperty(pi);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
            try {
                transportSE.call(MyGlobal.SOAP_ACTION_SL, envelope);
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                checkupdatesl = Boolean.parseBoolean(response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }


    }
    //KSOAP

}
