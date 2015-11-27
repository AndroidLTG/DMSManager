package androidltg.stanstudios.com.dmsmanager;


import com.google.gson.Gson;
import com.sileria.android.view.HorzListView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.PopupMenu;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import eneter.messaging.diagnostic.EneterTrace;
import eneter.messaging.endpoints.typedmessages.*;
import eneter.messaging.messagingsystems.messagingsystembase.*;
import eneter.messaging.messagingsystems.tcpmessagingsystem.TcpMessagingSystemFactory;
import eneter.net.system.EventHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MainOrder extends AppCompatActivity implements View.OnClickListener {
    Button btndatban,btngopban,btnchuyenban,btnhuyhoadon, btngiamgia, btnban, btnchebien, btnthanhtoan, btndongcuaso, btntimkiem, btntaithemsp;
    private TextView txtbanhientai, txttongtien, txtgiamgiatheohdban;
    private String loaigiam = "";// Xử lí giảm từng sp
    ListView listorder, listnhomdoan;
    LinearLayout lineroder;
    JSONArray Array = new JSONArray();
    GridView gridViewdoan;
    ArrayList<ItemOrder> arrItemOrder;
    HorzListView listgroup2;
    ProgressBar progressBar;
    private GridViewItemAdapter gridAdapter;
    private int load = 0;//check loadmore
    String temp ="";
    private String banchuyen = "",bangop="", giamgiahoadon = "", giamgiamon = "", banchon = "", mangaunhien = "", sendsh = "", sendsl = "", mabanchon = "", tenkhuvuc = "", khuvuc = "", thoigianvao = "", dataitem = "", datasl = "", datash = "";
    private int status = 0, sttsh = 0, plus = 0, guidatchuyen = 0;

    private boolean clickban=false,checksendsh = false, checksendsl = false, checkchange = false, checkxoahd = false, checkchuyenban = false,checkgopban= false;

    private Bitmap bitmap;
    ListOrderArrayAdapter adapter;
    private float tongtien = 0;


    // Request message type
    // The message must have the same name as declared in the service.
    // Also, if the message is the inner class, then it must be static.

    // UI controls
    private Handler myRefresh = new Handler();


    // Sender sending MyRequest and as a response receiving MyResponse.
    private IDuplexTypedMessageSender<MessageSocket, MessageSocket> mySender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_order);
        getid();

        Intent i = getIntent();
        banchon = i.getStringExtra("BANCHON");
        mabanchon = i.getStringExtra("MABANCHON");
        khuvuc = i.getStringExtra("KHUVUC");
        tenkhuvuc = i.getStringExtra("TENKHUVUC");
        thoigianvao = i.getStringExtra("StartDate");//get time in
        status = i.getIntExtra("Status", 0);//get status table
        if (!thoigianvao.equals("1900-01-01T00:00:00")) {//if has
            String[] time = thoigianvao.split("T");
            thoigianvao = time[1].substring(0, 5);
        } else
            thoigianvao = MyGlobal.gettimecurrent();//else get current time
        getfromserver();
        Thread anOpenConnectionThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    openConnection();
                    final MessageSocket aRequestMsg = new MessageSocket();

                    aRequestMsg.MessageType = MessageType.Login;
                    //aRequestMsg.
                    aRequestMsg.User = MyGlobal.SalerNo;


                    // Send the request message.
                    try {
                        mySender.sendRequestMessage(aRequestMsg);
                    } catch (Exception err) {
                        EneterTrace.error("Gửi lệnh in lỗi.", err);
                    }

                } catch (Exception err) {
                    EneterTrace.error(" Mở kết nối lỗi!.", err);
                }
            }
        });
        anOpenConnectionThread.start();
    }

    @Override
    protected void onDestroy() {
        mySender.detachDuplexOutputChannel();
        super.onDestroy();
    }

    private void openConnection() throws Exception {
        // Create sender sending MyRequest and as a response receiving MyResponse
        IDuplexTypedMessagesFactory aSenderFactory = new DuplexTypedMessagesFactory();
        mySender = aSenderFactory.createDuplexTypedMessageSender(MessageSocket.class, MessageSocket.class);

        // Subscribe to receive response messages.
        mySender.responseReceived().subscribe(myOnResponseHandler);

        // Create TCP messaging for the communication.
        // Note: 10.0.2.2 is a special alias to the loopback (127.0.0.1)
        //       on the development machine.
        IMessagingSystemFactory aMessaging = new TcpMessagingSystemFactory();
        IDuplexOutputChannel anOutputChannel
                = aMessaging.createDuplexOutputChannel("tcp://203.162.53.101:8094/");
        //= aMessaging.createDuplexOutputChannel("tcp://192.168.1.102:8060/");

        // Attach the output channel to the sender and be able to send
        // messages and receive responses.
        mySender.attachDuplexOutputChannel(anOutputChannel);
    }

    private void onSendRequest(View v) {
        // Create the request message.
        final MessageSocket aRequestMsg = new MessageSocket();

        aRequestMsg.MessageType = MessageType.Print;
        //aRequestMsg.
        aRequestMsg.User = MyGlobal.SalerNo;
        aRequestMsg.Data = "INBILL*" + mangaunhien;
        aRequestMsg.ToUser = "*Printer";

        // Send the request message.
        try {
            mySender.sendRequestMessage(aRequestMsg);
        } catch (Exception err) {
            EneterTrace.error("Gửi lệnh in lỗi.", err);
        }

    }

    private void onResponseReceived(Object sender,
                                    final TypedResponseReceivedEventArgs<MessageSocket> e) {
        myRefresh.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "Đã gửi lệnh in chế biến", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private EventHandler<TypedResponseReceivedEventArgs<MessageSocket>> myOnResponseHandler
            = new EventHandler<TypedResponseReceivedEventArgs<MessageSocket>>() {
        @Override
        public void onEvent(Object sender,
                            TypedResponseReceivedEventArgs<MessageSocket> e) {
            onResponseReceived(sender, e);
            MessageSocket Data = e.getResponseMessage();
            if (Data != null)
                Toast.makeText(getApplicationContext(), Data.Data, Toast.LENGTH_SHORT).show();
        }
    };



    @Override
    protected void onResume() {
        super.onResume();
        //getfromserver();
    }

    private void getfromserver() {
        MyGlobal.cgdatabaseitem();

        if (status == 2) {
            MyGlobal.cgdatabasesl();
            MyGlobal.cgdatabasesh();
        }

        CAPNHATDULIEU();
    }

    private void CAPNHATDULIEU() {
        myAsyncTask myRequest = new myAsyncTask();

        myRequest.execute();

    }

    private class myAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override

        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            if (MyGlobal.isOnline) {


                try {
                    parsejsonITEM();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            if (status == 2 && MyGlobal.isOnline) {


                try {
                    parsejsonSH();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    parsejsonSL();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            progressBar.setVisibility(View.INVISIBLE);
            xuli();
        }


        @Override

        protected void onPreExecute() {

            super.onPreExecute();

            //dang xu li
            progressBar.setVisibility(View.VISIBLE);

        }


        @Override

        protected Void doInBackground(Void... params) {
            if (!MyGlobal.isFieldExist(MyGlobal.database, MyGlobal.TABLE_ITEM, MyGlobal.COLUMNITEM_ResponsibilityCenter, MyGlobal.ResponsibilityCenter))
                getITEM(MyGlobal.SourceCode, MyGlobal.ResponsibilityCenter, MyGlobal.index, MyGlobal.indexpage);
            if (status == 2) {
                if (MyGlobal.isOnline) {
                    getSLorder(mabanchon, khuvuc);
                    getSHorder(mabanchon, khuvuc);
                }

            }
            return null;

        }

    }


    private void parsejsonITEM() throws JSONException {
        JSONObject jsonObject = new JSONObject(dataitem);
        Array = jsonObject.getJSONArray("Table");
        int ncustomer = Array.length();
        for (int i = 0; i < ncustomer; i++) {
            ContentValues cv = new ContentValues();
            cv.put(MyGlobal.COLUMNITEM_No, Array.getJSONObject(i).getString("No_"));
            cv.put(MyGlobal.COLUMNITEM_NAME, Array.getJSONObject(i).getString("Name"));
            cv.put(MyGlobal.COLUMNITEM_Description, Array.getJSONObject(i).getString("Description"));
            cv.put(MyGlobal.COLUMNITEM_SalesUnitOfMeasure, Array.getJSONObject(i).getString("SalesUnitOfMeasure"));
            cv.put(MyGlobal.COLUMNITEM_SourceCode, Array.getJSONObject(i).getString("SourceCode"));
            cv.put(MyGlobal.COLUMNITEM_ResponsibilityCenter, Array.getJSONObject(i).getString("ResponsibilityCenter"));
            cv.put(MyGlobal.COLUMNITEM_ItemCategoryCode, Array.getJSONObject(i).getString("ItemCategoryCode"));
            cv.put(MyGlobal.COLUMNITEM_SalesUnitPrice, Array.getJSONObject(i).getString("SalesUnitPrice"));
            cv.put(MyGlobal.COLUMNITEM_ItemDisc_Group, Array.getJSONObject(i).getString("ItemDisc_Group"));
            cv.put(MyGlobal.COLUMNITEM_LINKPICTURE, Array.getJSONObject(i).getString("LinkPicture"));
            cv.put(MyGlobal.COLUMNITEM_NAME_SEARCH, MyGlobal.ConvertString(Array.getJSONObject(i).getString("Name")));
            cv.put(MyGlobal.COLUMNITEM_ItemType, Integer.parseInt(Array.getJSONObject(i).getString("ItemType")));
//            if(!isFieldExist(TABLE_ITEM,COLUMNITEM_No,Array.getJSONObject(i).getString("No_"))) {


            MyGlobal.database.insert(MyGlobal.TABLE_ITEM, null, cv);
//            }
        }

    }

    private void parsejsonSL() throws JSONException {
        JSONObject jsonObject = new JSONObject(datasl);
        Array = jsonObject.getJSONArray("Table");
        int ncustomer = Array.length();

        for (int i = 0; i < ncustomer; i++) {

            ContentValues cv = new ContentValues();
            cv.put(MyGlobal.COLUMNSL_Barcode, Array.getJSONObject(i).getString("BarCode"));
            cv.put(MyGlobal.COLUMNSL_ItemNo_, Array.getJSONObject(i).getString("ItemNo_"));
            cv.put(MyGlobal.COLUMNSL_ItemName, Array.getJSONObject(i).getString("ItemName"));
            cv.put(MyGlobal.COLUMNSL_ValuedQuantity, Float.parseFloat(Array.getJSONObject(i).getString("ValuedQuantity")));
            cv.put(MyGlobal.COLUMNSL_RefNo_, Array.getJSONObject(i).getString("RefNo_"));
            cv.put(MyGlobal.COLUMNSL_Type,0);// load ve =0 gui len =1
            cv.put(MyGlobal.COLUMNSL_UnitPrice, Float.parseFloat(Array.getJSONObject(i).getString("UnitPrice")));
            cv.put(MyGlobal.COLUMNSL_UnitOfMeasure, Array.getJSONObject(i).getString("UnitOfMeasure"));
            cv.put(MyGlobal.COLUMNSL_Status, Integer.parseInt(Array.getJSONObject(i).getString("StatusPrint")));
            cv.put(MyGlobal.COLUMNSL_Description, Array.getJSONObject(i).getString("Description"));
            cv.put(MyGlobal.COLUMNSL_isSync, 1);
            cv.put(MyGlobal.COLUMNSL_ItemType, Array.getJSONObject(i).getString("ItemType"));
            if (!Array.getJSONObject(i).getString("DiscountItem%").equals("null"))
                cv.put(MyGlobal.COLUMNSL_DiscountPT, Float.parseFloat(Array.getJSONObject(i).getString("DiscountItem%")));
            else cv.put(MyGlobal.COLUMNSL_DiscountPT, 0);
            if (!Array.getJSONObject(i).getString("DiscountItem").equals("null"))
                cv.put(MyGlobal.COLUMNSL_Discount, Float.parseFloat(Array.getJSONObject(i).getString("DiscountItem")));
            else cv.put(MyGlobal.COLUMNSL_Discount, 0);
            if (i == 0) {
                String strFiltersl = MyGlobal.COLUMNSL_RefNo_ + "='" + mangaunhien + "'";
                MyGlobal.database.delete(MyGlobal.TABLE_SL, strFiltersl, null);
            }
            MyGlobal.database.insert(MyGlobal.TABLE_SL, null, cv);
//            }
        }

    }

    private void parsejsonSH() throws JSONException {
        JSONObject jsonObject = new JSONObject(datash);
        Array = jsonObject.getJSONArray("Table");
        int ncustomer = Array.length();

        for (int i = 0; i < ncustomer; i++) {

            ContentValues cv = new ContentValues();
            cv.put(MyGlobal.COLUMNSH_OrderNo, Array.getJSONObject(i).getString("No_"));
            cv.put(MyGlobal.COLUMNSH_CustomerNo, Array.getJSONObject(i).getString("CustomerNo_"));
            cv.put(MyGlobal.COLUMNSH_CreateDate, Array.getJSONObject(i).getString("CreateDate"));
            cv.put(MyGlobal.COLUMNSH_Description, Array.getJSONObject(i).getString("Description"));
            mangaunhien = Array.getJSONObject(i).getString("No_");
            cv.put(MyGlobal.COLUMNSH_AmountTotal, Float.parseFloat(Array.getJSONObject(i).getString("AmountTotal")));
            cv.put(MyGlobal.COLUMNSH_Type, Integer.parseInt(Array.getJSONObject(i).getString("Type")));
            cv.put(MyGlobal.COLUMNSH_STT, Array.getJSONObject(i).getString("RowID"));
            cv.put(MyGlobal.COLUMNSH_CustomerName, Array.getJSONObject(i).getString("CustomerName"));
            cv.put(MyGlobal.COLUMNSH_SalerNo, Array.getJSONObject(i).getString("SalerNo_"));
            cv.put(MyGlobal.COLUMNSH_SalerName, Array.getJSONObject(i).getString("SalerName"));
            cv.put(MyGlobal.COLUMNSH_TypeOrder, 0);

            cv.put(MyGlobal.COLUMNSH_Ref_Area, Array.getJSONObject(i).getString("Ref_Area"));
            cv.put(MyGlobal.COLUMNSH_Ref_Table, Array.getJSONObject(i).getString("Ref_Table"));
            cv.put(MyGlobal.COLUMNSH_Status, Integer.parseInt(Array.getJSONObject(i).getString("Status")));
            cv.put(MyGlobal.COLUMNSH_AmountCustomer, 0);
            cv.put(MyGlobal.COLUMNSH_AmountReturn, 0);
            cv.put(MyGlobal.COLUMNSH_isSync, 1);
            cv.put(MyGlobal.COLUMNSH_EndDate, Array.getJSONObject(i).getString("EndDate"));
            if (!Array.getJSONObject(i).getString("Discount%").equals("null"))
                cv.put(MyGlobal.COLUMNSH_DiscountPT, Float.parseFloat(Array.getJSONObject(i).getString("Discount%")));
            else cv.put(MyGlobal.COLUMNSH_DiscountPT, 0);
            if (!Array.getJSONObject(i).getString("Discount").equals("null"))
                cv.put(MyGlobal.COLUMNSH_Discount, Float.parseFloat(Array.getJSONObject(i).getString("Discount")));
            else cv.put(MyGlobal.COLUMNSH_Discount, 0);
            if (i == 0) {
                String strFiltersh = MyGlobal.COLUMNSH_OrderNo + "='" + mangaunhien + "'";
                MyGlobal.database.delete(MyGlobal.TABLE_SH, strFiltersh, null);
            }
            MyGlobal.database.insert(MyGlobal.TABLE_SH, null, cv);
//            }
        }

    }

    private void getITEM(String sourcecode, String responcenter, int index, int indexpage) {
        SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_ITEM);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("SourceCode");
        pi.setValue(sourcecode);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("ResponsibilityCenter");
        pi.setValue(responcenter);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("index");
        pi.setValue(index);
        pi.setType(int.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("indexpage");
        pi.setValue(indexpage);
        pi.setType(int.class);
        request.addProperty(pi);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
        try {
            transportSE.call(MyGlobal.SOAP_ACTION_ITEM, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            dataitem = response.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void getSLorder(String table, String area) {
        SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_getSL);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("Table");
        pi.setValue(table);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("Area");
        pi.setValue(area);
        pi.setType(String.class);
        request.addProperty(pi);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
        try {
            transportSE.call(MyGlobal.SOAP_ACTION_getSL, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            datasl = response.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    private void getSHorder(String table, String area) {
        SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_getSH);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("Table");
        pi.setValue(table);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("Area");
        pi.setValue(area);
        pi.setType(String.class);
        request.addProperty(pi);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
        try {
            transportSE.call(MyGlobal.SOAP_ACTION_getSH, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            datash = response.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }


    private void xuli() {
        txtbanhientai.setText(banchon + "\nBắt đầu : " + thoigianvao);


        arrItemOrder = new ArrayList<ItemOrder>();
        adapter = new ListOrderArrayAdapter(
                this,
                R.layout.customlistvieworder,// lấy custom layout
                arrItemOrder/*thiết lập data source*/);
        listorder.setAdapter(adapter);//gán Adapter vào Lisview
        if (status == 2) {
            arrItemOrder.addAll(loadorder());
            tongtien=0;

            for(int i=0;i<arrItemOrder.size();i++)
            {
                if(arrItemOrder.get(i).getDiscount()>0)
                    tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))-Float.parseFloat(arrItemOrder.get(i).getquantity())*arrItemOrder.get(i).getDiscount();
                else if(arrItemOrder.get(i).getDiscountPT()>0)
                    tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))*(100-arrItemOrder.get(i).getDiscountPT())/100;
                else tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""));


            }
            if(!giamgiahoadon.isEmpty()){
                if(giamgiahoadon.indexOf("%")!=-1)
                    tongtien =tongtien * (100-Float.parseFloat(giamgiahoadon.replace("%","").replace("-","")))/100;
                else tongtien =tongtien - Float.parseFloat(giamgiahoadon.replace("-","").replace(".","").replace(",", ""));
            }
            if (!giamgiahoadon.isEmpty())
                txtgiamgiatheohdban.setText("-" + MyGlobal.formatfloat(Float.parseFloat(giamgiahoadon.replace("%", ""))) + "%");
            txttongtien.setText("Tổng tiền : " + MyGlobal.formatfloat(tongtien) + " VNĐ");
            adapter.notifyDataSetChanged();
        }
        listorder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                TextView txtlvprice = (TextView) view.findViewById(R.id.txtlvprice);
                TextView txtlvquantity = (TextView) view.findViewById(R.id.txtlvquantity);
                TextView txtlvname = (TextView) view.findViewById(R.id.txtlvnameitem);
                TextView txtlvdescription = (TextView) view.findViewById(R.id.txtlvdescription);
                lineroder.setVisibility(View.VISIBLE);
                btndatban.setVisibility(View.INVISIBLE);
                btnchuyenban.setVisibility(View.INVISIBLE);
                btngopban.setVisibility(View.INVISIBLE);
                Button btnplus = (Button) lineroder.findViewById(R.id.btnplusorder);
                Button btnminus = (Button) lineroder.findViewById(R.id.btnminusorder);
                Button btnother = (Button) lineroder.findViewById(R.id.btnotherorder);
                Button btndelete = (Button) lineroder.findViewById(R.id.btndeleteorder);

                btnplus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        plus++;

                        arrItemOrder.get(position).setquantity(String.valueOf(Float.valueOf(arrItemOrder.get(position).getquantity()) + 1).replace(".0", ""));
                        if (arrItemOrder.get(position).getStatusprint() == 0)
                            arrItemOrder.get(position).setStatusprint(plus);
                        else
                            arrItemOrder.get(position).setStatusprint(Float.parseFloat(arrItemOrder.get(position).getquantity()));
                        tongtien=0;

                        for(int i=0;i<arrItemOrder.size();i++)
                        {
                            if(arrItemOrder.get(i).getDiscount()>0)
                                tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))-Float.parseFloat(arrItemOrder.get(i).getquantity())*arrItemOrder.get(i).getDiscount();
                            else if(arrItemOrder.get(i).getDiscountPT()>0)
                                tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))*(100-arrItemOrder.get(i).getDiscountPT())/100;
                            else tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""));


                        }
                        if(!giamgiahoadon.isEmpty()){
                            if(giamgiahoadon.indexOf("%")!=-1)
                                tongtien =tongtien * (100-Float.parseFloat(giamgiahoadon.replace("%","").replace("-","")))/100;
                            else tongtien =tongtien - Float.parseFloat(giamgiahoadon.replace("-","").replace(".","").replace(",", ""));
                        }
                        adapter.notifyDataSetChanged();
                        listorder.setAdapter(adapter);//gán Adapter vào Lisview
                        txttongtien.setText("Tổng tiền : " + MyGlobal.formatfloat(tongtien) + " VNĐ");
                    }
                });
                btnminus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Float.valueOf(arrItemOrder.get(position).getquantity()) > 1) {
                            //        if(arrItemOrder.get(position).getStatusprint()!=0) { // KHONG CHO GIAM KHI DA CHE BIEN

                            arrItemOrder.get(position).setquantity(String.valueOf(Float.valueOf(arrItemOrder.get(position).getquantity()) - 1).replace(".0", ""));
                            arrItemOrder.get(position).setStatusprint(Float.parseFloat(arrItemOrder.get(position).getquantity()));
                            tongtien=0;

                            for(int i=0;i<arrItemOrder.size();i++)
                            {
                                if(arrItemOrder.get(i).getDiscount()>0)
                                    tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))-Float.parseFloat(arrItemOrder.get(i).getquantity())*arrItemOrder.get(i).getDiscount();
                                else if(arrItemOrder.get(i).getDiscountPT()>0)
                                    tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))*(100-arrItemOrder.get(i).getDiscountPT())/100;
                                else tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""));


                            }
                            if(!giamgiahoadon.isEmpty()){
                                if(giamgiahoadon.indexOf("%")!=-1)
                                    tongtien =tongtien * (100-Float.parseFloat(giamgiahoadon.replace("%","").replace("-","")))/100;
                                else tongtien =tongtien - Float.parseFloat(giamgiahoadon.replace("-","").replace(".","").replace(",", ""));
                            }
                            adapter.notifyDataSetChanged();
                            listorder.setAdapter(adapter);//gán Adapter vào Lisview
                            txttongtien.setText("Tổng tiền : " + MyGlobal.formatfloat(tongtien) + " VNĐ");
//                           }
//                            else Toast.makeText(getApplicationContext(),"Món này đã được chế biến, không thể giảm",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btndelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        ItemOrder itemdelete = (ItemOrder) parent.getItemAtPosition(position);
                        arrItemOrder.remove(itemdelete);
                        tongtien=0;

                        for(int i=0;i<arrItemOrder.size();i++)
                        {
                            if(arrItemOrder.get(i).getDiscount()>0)
                                tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))-Float.parseFloat(arrItemOrder.get(i).getquantity())*arrItemOrder.get(i).getDiscount();
                            else if(arrItemOrder.get(i).getDiscountPT()>0)
                                tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))*(100-arrItemOrder.get(i).getDiscountPT())/100;
                            else tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""));


                        }
                        if(!giamgiahoadon.isEmpty()){
                            if(giamgiahoadon.indexOf("%")!=-1)
                                tongtien =tongtien * (100-Float.parseFloat(giamgiahoadon.replace("%","").replace("-","")))/100;
                            else tongtien =tongtien - Float.parseFloat(giamgiahoadon.replace("-","").replace(".","").replace(",",""));
                        }
                        adapter.notifyDataSetChanged();
                        lineroder.setVisibility(View.INVISIBLE);

                        txttongtien.setText("Tổng tiền : " + MyGlobal.formatfloat(tongtien) + " VNĐ");
                    }
                });
                btnother.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialogsaleother = new Dialog(MainOrder.this);
                        // Include dialog.xml file
                        dialogsaleother.setContentView(R.layout.dialog_sale_other);
                        // Set dialog title
                        dialogsaleother.setTitle("Nhập giảm giá");
                        // set values for custom dialog components - text, image and button

                        final EditText editTextsaleother = (EditText) dialogsaleother.findViewById(R.id.editdialoginputsaleother);
                        final Spinner spinsaleother = (Spinner) dialogsaleother.findViewById(R.id.spinothersale);
                        final String arr[] = {"VNĐ",
                                "%"};

                        ArrayAdapter<String> adapterspiner = new ArrayAdapter<String>
                                (
                                        dialogsaleother.getContext(),
                                        android.R.layout.simple_spinner_item,
                                        arr
                                );
                        adapter.setDropDownViewResource
                                (android.R.layout.simple_list_item_single_choice);
                        //Thiết lập adapter cho Spinner
                        spinsaleother.setAdapter(adapterspiner);
                        spinsaleother.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                loaigiam = arr[position];

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        dialogsaleother.show();
                        editTextsaleother.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (!editTextsaleother.getText().toString().isEmpty()) {
                                    if (loaigiam.equals("%")) {

                                        if (Float.parseFloat(editTextsaleother.getText().toString()) > 100)
                                            editTextsaleother.setText("100");
                                    }
                                    if (loaigiam.equals("VNĐ")) {

                                        if (Float.parseFloat(editTextsaleother.getText().toString()) < 0)
                                            editTextsaleother.setText("0");
                                    }
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        Button declineButtonsaleother = (Button) dialogsaleother.findViewById(R.id.btndialogclosesaleother);
                        // if decline button is clicked, close the custom dialog
                        declineButtonsaleother.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                giamgiahoadon = txtgiamgiatheohdban.getText().toString();
                                giamgiamon ="";
                                if (loaigiam.equals("%")) {
                                    if (!editTextsaleother.getText().toString().isEmpty()) {
                                        arrItemOrder.get(position).setDiscountPT(Float.parseFloat(editTextsaleother.getText().toString()));
                                        arrItemOrder.get(position).setDiscount(0);

                                    }

                                }
                                if (loaigiam.equals("VNĐ")) {
                                    if (!editTextsaleother.getText().toString().isEmpty()) {
                                        arrItemOrder.get(position).setDiscount(Float.parseFloat(editTextsaleother.getText().toString()));
                                        arrItemOrder.get(position).setDiscountPT(0);
                                    }
                                }
                                tongtien=0;

                                for(int i=0;i<arrItemOrder.size();i++)
                                {
                                    if(arrItemOrder.get(i).getDiscount()>0)
                                    {
                                        tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))-Float.parseFloat(arrItemOrder.get(i).getquantity())*arrItemOrder.get(i).getDiscount();
                                        giamgiamon =giamgiamon+Float.parseFloat(arrItemOrder.get(i).getquantity())+" - "+arrItemOrder.get(i).getName()+" - "+arrItemOrder.get(i).getdescription()+" - "+arrItemOrder.get(i).getDiscount()+"<>";
                                    }
                                    else if(arrItemOrder.get(i).getDiscountPT()>0)
                                    {
                                        tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))*(100-arrItemOrder.get(i).getDiscountPT())/100;
                                        giamgiamon =giamgiamon+Float.parseFloat(arrItemOrder.get(i).getquantity())+" - "+arrItemOrder.get(i).getName()+" - "+arrItemOrder.get(i).getdescription()+" - "+(int)arrItemOrder.get(i).getDiscountPT()+"%"+"<>";
                                    }
                                    else tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""));


                                }
                                if(!giamgiahoadon.isEmpty()){
                                    if(giamgiahoadon.indexOf("%")!=-1)
                                        tongtien =tongtien * (100-Float.parseFloat(giamgiahoadon.replace("%","").replace("-","")))/100;
                                    else tongtien =tongtien - Float.parseFloat(giamgiahoadon.replace("-", "").replace(".","").replace(",", ""));
                                }
                                adapter.notifyDataSetChanged();
                                lineroder.setVisibility(View.INVISIBLE);
                                txttongtien.setText("Tổng tiền : " + MyGlobal.formatfloat(tongtien) + " VNĐ");
                                dialogsaleother.dismiss();
                            }
                        });
                    }
                });
                Toast.makeText(getApplicationContext(), txtlvprice.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        final ArrayAdapter<String> adapterlistnhom = new ArrayAdapter<String>(this,
                R.layout.mylistview, android.R.id.text1, loadgroup());


        // Assign adapter to ListView
        listnhomdoan.setAdapter(adapterlistnhom);
        // ListView Item Click Listener
        listnhomdoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                view.setSelected(true);
                // ListView Clicked item index
                final String groupselect = parent.getItemAtPosition(position).toString();
                gridAdapter = new GridViewItemAdapter(view.getContext(), R.layout.grid_item_layout, loaditembygroup(groupselect));

                gridViewdoan.setAdapter(gridAdapter);
                // Show Alert
                lineroder.setVisibility(View.INVISIBLE);
                btndatban.setVisibility(View.INVISIBLE);
                btnchuyenban.setVisibility(View.INVISIBLE);
                btngopban.setVisibility(View.INVISIBLE);
            }

        });

        final ArrayAdapter<String> adapterlistnhom2 = new ArrayAdapter<String>(this,
                R.layout.mylistviewhorz, android.R.id.text1, loadgroup2());
        listgroup2.setAdapter(adapterlistnhom2);
        listgroup2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                // ListView Clicked item index
                final String groupselect2 = parent.getItemAtPosition(position).toString();
                gridAdapter = new GridViewItemAdapter(view.getContext(), R.layout.grid_item_layout, loaditembygroup(groupselect2));

                gridViewdoan.setAdapter(gridAdapter);
                // Show Alert
                lineroder.setVisibility(View.INVISIBLE);
                btndatban.setVisibility(View.INVISIBLE);
                btnchuyenban.setVisibility(View.INVISIBLE);
                btngopban.setVisibility(View.INVISIBLE);
            }
        });


        //Gridview
        gridAdapter = new GridViewItemAdapter(this, R.layout.grid_item_layout, loaditem());

        gridViewdoan.setAdapter(gridAdapter);
        gridViewdoan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                lineroder.setVisibility(View.INVISIBLE);
                btndatban.setVisibility(View.INVISIBLE);
                btnchuyenban.setVisibility(View.INVISIBLE);
                btngopban.setVisibility(View.INVISIBLE);
                final ItemList item = (ItemList) parent.getItemAtPosition(position);
                //Create intent
                final Dialog dialog = new Dialog(MainOrder.this,R.style.Theme_AppCompat_Light_NoActionBar_FullScreen);
                // Include dialog.xml file
                dialog.setContentView(R.layout.dialog_order);
                // Set dialog title
                dialog.setTitle(item.getTitle());

                // set values for custom dialog components - text, image and button
                final TextView text = (TextView) dialog.findViewById(R.id.txtdialogprice);
                final TextView texttitle = (TextView) dialog.findViewById(R.id.txtdialogtitle);
                text.setText(MyGlobal.formatfloat(item.getprices()) + " VNĐ");
                texttitle.setText(item.getTitle());
                final EditText editText = (EditText) dialog.findViewById(R.id.editdialoginput);
                final EditText editdescription = (EditText) dialog.findViewById(R.id.edit_order_description);
                final Spinner spinnerdescription = (Spinner) dialog.findViewById(R.id.spinerdescription);

                editText.setInputType(InputType.TYPE_NULL);

                final String arr[] = {"Ghi chú",
                        "ít đường",
                        "nhiều đường",
                        "không đá", "nhiều đá", "ít sữa"};
                ArrayAdapter<String> adapterspiner = new ArrayAdapter<String>
                        (
                                dialog.getContext(),
                                R.layout.spinner_item,
                                arr
                        );
                adapter.setDropDownViewResource
                        (android.R.layout.simple_list_item_single_choice);
                //Thiết lập adapter cho Spinner
                spinnerdescription.setAdapter(adapterspiner);
                spinnerdescription.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (!arr[position].equals("Ghi chú"))
                            editdescription.setText(arr[position]);
                        else editdescription.setText("");
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                dialog.show();

                Button declineButton = (Button) dialog.findViewById(R.id.btndialogclose);
                Button dialogcancel = (Button) dialog.findViewById(R.id.btndialogcancel);
                Button btndialog1 = (Button) dialog.findViewById(R.id.btndialogorder1);
                Button btndialog0 = (Button) dialog.findViewById(R.id.btndialogorder0);
                Button btndialog2 = (Button) dialog.findViewById(R.id.btndialogorder2);
                Button btndialog3 = (Button) dialog.findViewById(R.id.btndialogorder3);
                Button btndialog4 = (Button) dialog.findViewById(R.id.btndialogorder4);
                Button btndialog5 = (Button) dialog.findViewById(R.id.btndialogorder5);
                Button btndialog6  = (Button) dialog.findViewById(R.id.btndialogorder6);
                Button btndialog7  = (Button) dialog.findViewById(R.id.btndialogorder7);
                Button btndialog8 = (Button) dialog.findViewById(R.id.btndialogorder9);
                Button btndialog9 = (Button) dialog.findViewById(R.id.btndialogorder9);
                Button btndialogphay = (Button) dialog.findViewById(R.id.btndialogorderphay);
                Button btndialogxoa = (Button) dialog.findViewById(R.id.btndialogorderxoa);

                btndialogxoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!temp.isEmpty())
                        {
                            temp=temp.substring(0,temp.length()-1);
                            editText.setText(temp);
                        }
                    }
                });
                btndialog0.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp=temp+"0";
                        editText.setText(temp);
                    }
                });
                btndialogphay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp=temp+".";
                        editText.setText(temp);
                    }
                });

                btndialog1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp=temp+"1";
                        editText.setText(temp);
                    }
                });

                btndialog2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp=temp+"2";
                        editText.setText(temp);
                    }
                });

                btndialog3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp=temp+"3";
                        editText.setText(temp);
                    }
                });

                btndialog4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp=temp+"4";
                        editText.setText(temp);
                    }
                });

                btndialog5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp=temp+"5";
                        editText.setText(temp);
                    }
                });

                btndialog6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp=temp+"6";
                        editText.setText(temp);
                    }
                });

                btndialog7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp=temp+"7";
                        editText.setText(temp);
                    }
                });

                btndialog8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp=temp+"8";
                        editText.setText(temp);
                    }
                });

                btndialog9.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp=temp+"9";
                        editText.setText(temp);
                    }
                });

                dialogcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        temp="";
                        dialog.dismiss();
                    }
                });
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Xu li
                        // Neu co giam gia ca hoa don thi tinh tong tien chua giam gia
                        if (!editText.getText().toString().isEmpty() && !editText.getText().toString().equals("0")) {
                            //neu co so luong va khac 0
                            if (arrItemOrder.size() < 1) {//neu chua co don hang
                                ItemOrder itemOrder = new ItemOrder();
                                itemOrder.setName(item.getTitle());
                                itemOrder.setquantity(editText.getText().toString());
                                itemOrder.setno_(item.getno_());
                                itemOrder.setStatusprint(Float.parseFloat(editText.getText().toString()));
                                itemOrder.setUnitOfMeasure(item.getUnitOfMeasure());
                                itemOrder.setdescription(editdescription.getText().toString());
                                itemOrder.setType(1);
                                itemOrder.setDiscount(0);
                                itemOrder.setDiscountPT(0);
                                itemOrder.setprices(text.getText().toString());
                                arrItemOrder.add(itemOrder);
                                adapter.notifyDataSetChanged();
                                dialog.dismiss();

                            } else {
                                boolean check = false;
                                for (int i = 0; i < arrItemOrder.size(); i++) {
                                    if (arrItemOrder.get(i).getName().equals(item.getTitle()) && arrItemOrder.get(i).getdescription().equals(editdescription.getText().toString())) {
                                        //neu ton tai san pham trong don hang va ghi chu giong

                                        if (arrItemOrder.get(i).getDiscountPT() > 0)//neu giam theo %
                                        {
                                            arrItemOrder.get(i).setquantity(String.valueOf(Float.valueOf(arrItemOrder.get(i).getquantity()) + Float.valueOf(editText.getText().toString())).replace(".0", ""));
                                            if (arrItemOrder.get(i).getStatusprint() == 0)
                                                arrItemOrder.get(i).setStatusprint(Float.parseFloat(editText.getText().toString()));
                                            else
                                                arrItemOrder.get(i).setStatusprint(Float.parseFloat(arrItemOrder.get(i).getquantity()));
                                            adapter.notifyDataSetChanged();
                                            listorder.setAdapter(adapter);//gán Adapter vào Lisview
                                        } else if (arrItemOrder.get(i).getDiscount() > 0)//neu giam theo tien
                                        {


                                            arrItemOrder.get(i).setquantity(String.valueOf(Float.valueOf(arrItemOrder.get(i).getquantity()) + Float.valueOf(editText.getText().toString())).replace(".0", ""));
                                            if (arrItemOrder.get(i).getStatusprint() == 0)
                                                arrItemOrder.get(i).setStatusprint(Float.parseFloat(editText.getText().toString()));
                                            else
                                                arrItemOrder.get(i).setStatusprint(Float.parseFloat(arrItemOrder.get(i).getquantity()));
                                            adapter.notifyDataSetChanged();
                                            listorder.setAdapter(adapter);//gán Adapter vào Lisview

                                        } else {// neu khong giam

                                            arrItemOrder.get(i).setquantity(String.valueOf(Float.valueOf(arrItemOrder.get(i).getquantity()) + Float.valueOf(editText.getText().toString())).replace(".0", ""));
                                            if (arrItemOrder.get(i).getStatusprint() == 0)
                                                arrItemOrder.get(i).setStatusprint(Float.parseFloat(editText.getText().toString()));
                                            else
                                                arrItemOrder.get(i).setStatusprint(Float.parseFloat(arrItemOrder.get(i).getquantity()));
                                            adapter.notifyDataSetChanged();
                                            listorder.setAdapter(adapter);//gán Adapter vào Lisview
                                        }
                                        check = true;
                                        dialog.dismiss();
//                                    } else if (arrItemOrder.get(i).getName().equals(item.getTitle()) && !arrItemOrder.get(i).getdescription().equals(editdescription.getText().toString())) {
//                                        ItemOrder itemOrder = new ItemOrder();
//                                        itemOrder.setName(item.getTitle());
//                                        itemOrder.setno_(item.getno_());
//                                        itemOrder.setStatusprint(Float.parseFloat(editText.getText().toString()));
//                                        itemOrder.setUnitOfMeasure(item.getUnitOfMeasure());
//                                        itemOrder.setquantity(editText.getText().toString());
//                                        if (!editdescription.getText().toString().equals("Ghi chú"))
//                                            itemOrder.setdescription(editdescription.getText().toString());
//                                        else itemOrder.setdescription("");
//                                        itemOrder.setprices(text.getText().toString());
//                                        if (arrItemOrder.get(i).getDiscountPT() > 0)//giam theo %
//                                        {
//                                            itemOrder.setDiscount(0);
//                                            itemOrder.setDiscountPT(arrItemOrder.get(i).getDiscountPT());
//                                            tongtien = tongtien + (Float.valueOf(itemOrder.getprices().replace(" VNĐ", "").replace(".", "").replace(",", "")) * Float.valueOf(itemOrder.getquantity()) * ((100 - arrItemOrder.get(i).getDiscountPT()) / 100));
//
//                                        } else if (arrItemOrder.get(i).getDiscount() > 0)//giam theo tien
//                                        {
//                                            itemOrder.setDiscountPT(0);
//                                            itemOrder.setDiscount(arrItemOrder.get(i).getDiscount());
//                                            tongtien = tongtien + (Float.valueOf(itemOrder.getprices().replace(" VNĐ", "").replace(".", "").replace(",", "")) * Float.valueOf(itemOrder.getquantity()) - Float.valueOf(itemOrder.getquantity()) * arrItemOrder.get(i).getDiscount());
//                                        } else {
//                                            itemOrder.setDiscountPT(0);
//                                            itemOrder.setDiscount(0);
//                                            tongtien = tongtien + Float.valueOf(itemOrder.getprices().replace(" VNĐ", "").replace(".", "").replace(",", "")) * Float.valueOf(itemOrder.getquantity());
//                                        }
//                                        arrItemOrder.add(itemOrder);
//                                        adapter.notifyDataSetChanged();
//                                        check = true;
//                                        dialog.dismiss();
//                                    }
//
                                    }
                                }
                                if (check == false) {//khac ghi chu

                                    ItemOrder itemOrder = new ItemOrder();
                                    itemOrder.setType(1);
                                    itemOrder.setName(item.getTitle());
                                    itemOrder.setno_(item.getno_());
                                    itemOrder.setStatusprint(Float.parseFloat(editText.getText().toString()));
                                    itemOrder.setUnitOfMeasure(item.getUnitOfMeasure());
                                    itemOrder.setquantity(editText.getText().toString());
                                    if (!editdescription.getText().toString().equals("Ghi chú"))
                                        itemOrder.setdescription(editdescription.getText().toString());
                                    else itemOrder.setdescription("");
                                    itemOrder.setprices(text.getText().toString());

                                    itemOrder.setDiscountPT(0);
                                    itemOrder.setDiscount(0);


                                    arrItemOrder.add(itemOrder);
                                    adapter.notifyDataSetChanged();

                                    dialog.dismiss();

                                }
                            }

                        } else
                            dialog.dismiss();
                        tongtien=0;

                        for(int i=0;i<arrItemOrder.size();i++)
                        {
                            if(arrItemOrder.get(i).getDiscount()>0)
                                tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))-Float.parseFloat(arrItemOrder.get(i).getquantity())*arrItemOrder.get(i).getDiscount();
                            else if(arrItemOrder.get(i).getDiscountPT()>0)
                                tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))*(100-arrItemOrder.get(i).getDiscountPT())/100;
                            else tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""));


                        }
                        if(!giamgiahoadon.isEmpty()){
                            if(giamgiahoadon.indexOf("%")!=-1)
                                tongtien =tongtien * (100-Float.parseFloat(giamgiahoadon.replace("%","").replace("-","")))/100;
                            else tongtien =tongtien - Float.parseFloat(giamgiahoadon.replace("-","").replace(".","").replace(",", ""));
                        }
                        txttongtien.setText("Tổng tiền : " + MyGlobal.formatfloat(tongtien) + " VNĐ");
                        temp="";
                    }
                });
                //Start details activity

            }

        });
        gridViewdoan.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
//                    Toast.makeText(getApplicationContext(),"ƠN GIỜI! End đây rồi",Toast.LENGTH_SHORT).show();
                    //              progressBar.setVisibility(View.VISIBLE);

//                  CAPNHATDULIEU();

//                    MyGlobal.index = (MyGlobal.getCountTable(MyGlobal.database, MyGlobal.TABLE_ITEM, MyGlobal.COLUMNITEM_SourceCode, MyGlobal.SourceCode, MyGlobal.COLUMNITEM_ResponsibilityCenter, MyGlobal.ResponsibilityCenter) / MyGlobal.indexpage) - 1;//get last index
         //           Toast.makeText(view.getContext(), " Tổng " + MyGlobal.getCountTable(MyGlobal.database, MyGlobal.TABLE_ITEM, MyGlobal.COLUMNITEM_SourceCode, MyGlobal.SourceCode, MyGlobal.COLUMNITEM_ResponsibilityCenter, MyGlobal.ResponsibilityCenter) + " Số dòng : " + MyGlobal.indexpage + " dòng cuối " + MyGlobal.lastindexpage + "số trang " + MyGlobal.totalindex + " số lượng trang " + MyGlobal.index, Toast.LENGTH_SHORT).show();
                    if (MyGlobal.totalindex > 1) btntaithemsp.setVisibility(View.VISIBLE);


                }
            }
        });

    }

    private class myAsyncTaskmore extends AsyncTask<Void, Void, Void> {

        @Override

        protected void onPostExecute(Void result) {

            super.onPostExecute(result);

            try {
                parsejsonITEM();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.INVISIBLE);
        }


        @Override

        protected void onPreExecute() {

            super.onPreExecute();

            //dang xu li
            progressBar.setVisibility(View.VISIBLE);

        }


        @Override

        protected Void doInBackground(Void... params) {
            load++;
            MyGlobal.index++;
            getITEM(MyGlobal.SourceCode, MyGlobal.ResponsibilityCenter, MyGlobal.index, MyGlobal.indexpage);
            return null;
        }

    }

    private ArrayList<String> loadgroup() {
        ArrayList<String> result = new ArrayList<String>();
        if (MyGlobal.database != null) {
            Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_ITEM, null, MyGlobal.COLUMNITEM_ResponsibilityCenter + " =? AND " + MyGlobal.COLUMNITEM_SourceCode + " =? AND " + MyGlobal.COLUMNITEM_ItemType + " !=?", new String[]{MyGlobal.ResponsibilityCenter, MyGlobal.SourceCode, String.valueOf(2)}, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                String group;
                //new LoadImage().execute("");
                // ci.setImage(bitmap);
                group = cursor.getString(7);
                if (result.indexOf(group) != -1) {
                    cursor.moveToNext();
                } else {
                    result.add(group);
                    cursor.moveToNext();
                }

            }
            cursor.close();

        }

        return result;
    }

    private ArrayList<String> loadgroup2() {
        ArrayList<String> result = new ArrayList<String>();
        if (MyGlobal.database != null) {
            Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_ITEM, null, MyGlobal.COLUMNITEM_ResponsibilityCenter + " =? AND " + MyGlobal.COLUMNITEM_SourceCode + " =? AND " + MyGlobal.COLUMNITEM_ItemType + " !=?", new String[]{MyGlobal.ResponsibilityCenter, MyGlobal.SourceCode, String.valueOf(2)}, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                String group;
                //new LoadImage().execute("");
                // ci.setImage(bitmap);
                group = cursor.getString(11);
                if (result.indexOf(group) != -1) {
                    cursor.moveToNext();
                } else {
                    result.add(group);
                    cursor.moveToNext();
                }

            }
            cursor.close();

        }

        return result;
    }

    private ArrayList<ItemList> loaditem() {
        {
            ArrayList<ItemList> result = new ArrayList<ItemList>();
            if (MyGlobal.database != null) {
                Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_ITEM, null, MyGlobal.COLUMNITEM_ResponsibilityCenter + " =? AND " + MyGlobal.COLUMNITEM_SourceCode + " =? AND " + MyGlobal.COLUMNITEM_ItemType + " !=?", new String[]{MyGlobal.ResponsibilityCenter, MyGlobal.SourceCode, String.valueOf(2)}, null, null, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    ItemList ci = new ItemList();
                    new LoadImage().execute(cursor.getString(10));
                    ci.setImage(bitmap);
                    ci.setTitle(cursor.getString(2));
                    ci.setitemcategory(cursor.getString(7));
                    ci.setprices(cursor.getFloat(9));
                    ci.setproductgroupcode(cursor.getString(8));
                    ci.setno_(cursor.getString(1));
                    ci.setUnitOfMeasure(cursor.getString(4));
                    ci.setitemgrouptype(cursor.getString(11));

                    result.add(ci);
                    cursor.moveToNext();


                }
                cursor.close();

            }

            return result;
        }
    }

    private ArrayList<ItemOrder> loadorder() {
        {
            ArrayList<ItemOrder> result = new ArrayList<ItemOrder>();
            if (MyGlobal.database != null) {

                Cursor cursorsh = MyGlobal.database.query(MyGlobal.TABLE_SH, null, MyGlobal.COLUMNSH_Ref_Table + " = ?", new String[]{mabanchon}, null, null, null);
                cursorsh.moveToFirst();
                while (!cursorsh.isAfterLast()) {
                    if (cursorsh.getInt(14) == 1) {// SO SANH TRONG NGAY
                        mangaunhien = cursorsh.getString(1);
                     // tongtien = cursorsh.getFloat(5);
                        if (cursorsh.getFloat(18) > 0)
                            giamgiahoadon = cursorsh.getFloat(18) + "%";

                        if (cursorsh.getFloat(19) > 0)
                            giamgiahoadon = cursorsh.getFloat(19) + "";
                        cursorsh.moveToNext();
                    } else cursorsh.moveToNext();
                }
                cursorsh.close();
                Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_SL, null, MyGlobal.COLUMNSL_RefNo_ + " = ?", new String[]{mangaunhien}, null, null, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    ItemOrder ci = new ItemOrder();
                    mangaunhien = cursor.getString(6);
                    ci.setno_(cursor.getString(2));
                    ci.setName(cursor.getString(7));
                    ci.setprices(MyGlobal.formatfloat(cursor.getFloat(5)) + " VNĐ");
                    ci.setDiscountPT(cursor.getFloat(12));
                    ci.setDiscount(cursor.getFloat(13));
                    ci.setquantity(String.valueOf(cursor.getFloat(4)).replace(".0", ""));
                    ci.setUnitOfMeasure(cursor.getString(3));
                    ci.setType(cursor.getInt(8));
                    ci.setStatusprint(cursor.getFloat(9));
                    if (cursor.getString(10).equals("null"))
                        ci.setdescription("");
                    else ci.setdescription(cursor.getString(10));

                    if (cursor.getFloat(12) > 0)
                        giamgiamon = ci.getquantity() + " - " + ci.getName() + " - " + ci.getdescription() + " - " + String.valueOf(cursor.getFloat(12)) + "%";
                    if (cursor.getFloat(13) > 0)
                        giamgiamon = ci.getquantity() + " - " + ci.getName() + " - " + ci.getdescription() + " - " + MyGlobal.formatfloat(cursor.getFloat(13));
                    ;
                    result.add(ci);
                    cursor.moveToNext();


                }
                cursor.close();

            }

            return result;
        }
    }

    private ArrayList<ItemList> loaditembygroup(String groupselected) {
        {
            ArrayList<ItemList> result = new ArrayList<ItemList>();
            if (MyGlobal.database != null) {
                Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_ITEM, null, null, null, null, null, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if (cursor.getString(7).equals(groupselected)) {
                        ItemList ci = new ItemList();
                        new LoadImage().execute(cursor.getString(10));
                        ci.setImage(bitmap);
                        ci.setTitle(cursor.getString(2));
                        ci.setitemcategory(cursor.getString(7));
                        ci.setprices(cursor.getFloat(9));
                        ci.setproductgroupcode(cursor.getString(8));


                        result.add(ci);
                        cursor.moveToNext();

                    }
                    if (cursor.getString(11).equals(groupselected)) {
                        ItemList ci = new ItemList();
                        new LoadImage().execute(cursor.getString(10));
                        ci.setImage(bitmap);
                        ci.setTitle(cursor.getString(2));
                        ci.setitemcategory(cursor.getString(7));
                        ci.setprices(cursor.getFloat(9));
                        ci.setproductgroupcode(cursor.getString(8));


                        result.add(ci);
                        cursor.moveToNext();
                    } else cursor.moveToNext();

                }
                cursor.close();

            }

            return result;
        }
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            bitmap = image;
        }
    }

    private void getid() {
        btndatban = (Button) findViewById(R.id.btndatban);
        btndatban.setOnClickListener(this);
        btnchuyenban = (Button)findViewById(R.id.btnchuyenban);
        btnchuyenban.setOnClickListener(this);
        btngopban = (Button)findViewById(R.id.btngopban);
        btngopban.setOnClickListener(this);
        txtgiamgiatheohdban = (TextView) findViewById(R.id.txtgiamtheohdban);
        btnhuyhoadon = (Button) findViewById(R.id.btnhuyhoadon);
        btnhuyhoadon.setOnClickListener(this);
        btngiamgia = (Button) findViewById(R.id.btngiamgia);
        btngiamgia.setOnClickListener(this);
        btnban = (Button) findViewById(R.id.btnban);
        btnban.setOnClickListener(this);
        btnchebien = (Button) findViewById(R.id.btnchebien);
        btnchebien.setOnClickListener(this);
        btntaithemsp = (Button) findViewById(R.id.btntaithemsanpham);
        btntaithemsp.setOnClickListener(this);
        btnthanhtoan = (Button) findViewById(R.id.btnthanhtoan);
        btnthanhtoan.setOnClickListener(this);
        btndongcuaso = (Button) findViewById(R.id.btndongcuaso);
        btndongcuaso.setOnClickListener(this);
        btntimkiem = (Button) findViewById(R.id.btntimkiem);
        btntimkiem.setOnClickListener(this);
        txtbanhientai = (TextView) findViewById(R.id.txtbanhientai);
        txttongtien = (TextView) findViewById(R.id.txttongtien);
        listorder = (ListView) findViewById(R.id.listorder);
        listnhomdoan = (ListView) findViewById(R.id.listmenutab);
        listgroup2 = (HorzListView) findViewById(R.id.horizontal_lv);
        gridViewdoan = (GridView) findViewById(R.id.gridViewThucAn);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        lineroder = (LinearLayout) findViewById(R.id.lineronclick);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {// Get result giamgia
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            giamgiahoadon = data.getStringExtra("HOADON");
            giamgiamon = data.getStringExtra("THEOMON");//1 - A - B(cothekoco)
            if (giamgiamon != null) if (!giamgiamon.isEmpty()) {
                String[] giamgia = giamgiamon.split(" <-> ");
                String slgiam = giamgia[0];
                String mongiam = giamgia[1];//get name sale
                String ghichugiam = giamgia[2];
                String giagiam = giamgia[3];//get discount
                tongtien = 0;
                for (int i = 0; i < arrItemOrder.size(); i++) {
                    if (arrItemOrder.get(i).getName().equals(mongiam) && arrItemOrder.get(i).getdescription().equals(ghichugiam) && giagiam.indexOf("%") != -1)//giam theo %
                    {
                        arrItemOrder.get(i).setDiscount(0);
                        arrItemOrder.get(i).setDiscountPT(Float.parseFloat(giagiam.replace("%", "")));
                    } else if (arrItemOrder.get(i).getName().equals(mongiam) && arrItemOrder.get(i).getdescription().equals(ghichugiam) && giagiam.indexOf("%") == -1)//giam theo tien
                    {
                        arrItemOrder.get(i).setDiscountPT(0);
                        arrItemOrder.get(i).setDiscount(Float.parseFloat(giagiam.replace(",", "").replace(".", "")));
                    } else {
                        arrItemOrder.get(i).setDiscount(0);
                        arrItemOrder.get(i).setDiscountPT(0);
                    }
                }
            }

            txtgiamgiatheohdban.setText(giamgiahoadon);
            tongtien=0;

            for(int i=0;i<arrItemOrder.size();i++)
            {
                if(arrItemOrder.get(i).getDiscount()>0)
                    tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))-Float.parseFloat(arrItemOrder.get(i).getquantity())*arrItemOrder.get(i).getDiscount();
                else if(arrItemOrder.get(i).getDiscountPT()>0)
                    tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))*(100-arrItemOrder.get(i).getDiscountPT())/100;
                else tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""));


            }
            if(!giamgiahoadon.isEmpty()){
                if(giamgiahoadon.indexOf("%")!=-1)
                    tongtien =tongtien * (100-Float.parseFloat(giamgiahoadon.replace("%","").replace("-","")))/100;
                else tongtien =tongtien - Float.parseFloat(giamgiahoadon.replace("-","").replace(".","").replace(",",""));
            }
            txttongtien.setText("Tổng tiền : " + MyGlobal.formatfloat(tongtien) + " VNĐ");
            adapter.notifyDataSetChanged();


        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.btntaithemsanpham:
                if (MyGlobal.isOnline && load < MyGlobal.totalindex) {
                    myAsyncTaskmore myRequest = new myAsyncTaskmore();

                    myRequest.execute();
                } else {
                    if (load < MyGlobal.totalindex)
                        Toast.makeText(getApplicationContext(), "Vui lòng bật kết nối mạng để load dữ liệu", Toast.LENGTH_SHORT).show();

                }
                btntaithemsp.setVisibility(View.INVISIBLE);
                btndatban.setVisibility(View.INVISIBLE);
                btnchuyenban.setVisibility(View.INVISIBLE);
                btngopban.setVisibility(View.INVISIBLE);
                gridAdapter.notifyDataSetChanged();
                break;
            case R.id.btnhuyhoadon:
                //if >5 minute you can
                //and please tell me why
                String timenow = MyGlobal.gettimecurrent();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                Date date1, date2;
                long difference = 0;
                try {
                    date1 = format.parse(thoigianvao);
                    date2 = format.parse(timenow);
                    difference = date2.getTime() - date1.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (difference <= 60000 | status == 1) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainOrder.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Xác nhận hủy hóa đơn...");

                    // Setting Dialog Message
                    alertDialog.setMessage("Bạn có chắc chắn hủy hóa đơn này?");

                    // Setting Icon to Dialog

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            // Write your code here to invoke YES event
                            finish();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                } else
                    Toast.makeText(getApplicationContext(), "Không thể hủy hóa đơn quá 5 phút", Toast.LENGTH_SHORT).show();


                break;
            case R.id.btngiamgia:
                // new activity or dialog if you want
                if (arrItemOrder.size() <= 0)
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn món", Toast.LENGTH_SHORT).show();
                else {
                    Intent sale = new Intent(MainOrder.this, SaleActivity.class);
                    sale.putParcelableArrayListExtra("arritemorder", arrItemOrder);

                    startActivityForResult(sale, 1);
                }
                btndatban.setVisibility(View.INVISIBLE);
                btnchuyenban.setVisibility(View.INVISIBLE);
                btngopban.setVisibility(View.INVISIBLE);
                lineroder.setVisibility(View.INVISIBLE);
                break;
            case R.id.btnban:
                //three option (dialog three button )
                if(clickban) {
                    btndatban.setVisibility(View.INVISIBLE);
                    btnchuyenban.setVisibility(View.INVISIBLE);
                    btngopban.setVisibility(View.INVISIBLE);
                    clickban=false;
                }
                else {

                    btndatban.setVisibility(View.VISIBLE);
                    btnchuyenban.setVisibility(View.VISIBLE);
                    btngopban.setVisibility(View.VISIBLE);
                }
                lineroder.setVisibility(View.INVISIBLE);
                break;
            case  R.id.btndatban:
                AlertDialog.Builder alertDialogdb = new AlertDialog.Builder(MainOrder.this);

                // Setting Dialog Title
                alertDialogdb.setTitle(" Xác nhận đặt bàn ");

                // Setting Dialog Message
                alertDialogdb.setMessage("Bạn có chắc chắn đặt bàn này?");

                // Setting Icon to Dialog

                // Setting Positive "Yes" Button
                alertDialogdb.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // Write your code here to invoke YES event
                        //xu li dat ban
                        if (status == 0) {
                            getSH(0, mabanchon, khuvuc);
                            sendsh = ConverttoJsonSH();
                            guidatchuyen = 1;
                            sendordertoserver();
                            finish();
                        } else
                            Toast.makeText(getApplicationContext(), "Bàn đã có khách, không thể đặt trước", Toast.LENGTH_SHORT).show();
                    }
                });

                // Setting Negative "NO" Button
                alertDialogdb.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to invoke NO event
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialogdb.show();

                break;
            case  R.id.btnchuyenban:
                if (status == 2 | status == 1) {
                    GridView gridViewchuyenban = new GridView(MainOrder.this);


                    gridViewchuyenban.setAdapter(new GridViewTableAdapter(MainOrder.this, R.layout.grid_item_layout, loadtable(30)));
                    gridViewchuyenban.setNumColumns(3);
                    gridViewchuyenban.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // do something here
                        }
                    });

                    // Set grid view to alertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainOrder.this);
                    builder.setView(gridViewchuyenban);
                    builder.setTitle("Chọn bàn chuyển");

                    builder.show();
                    gridViewchuyenban.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TableItem tableitemchuyenban = (TableItem) parent.getItemAtPosition(position);
          //                  Toast.makeText(view.getContext(), tableitemchuyenban.getNo_(), Toast.LENGTH_SHORT).show();
                            banchuyen = tableitemchuyenban.getNo_();
                            CHUYENBAN();
                            finish();
                        }
                    });
                } else
                    Toast.makeText(getApplicationContext(), "Bàn trống không thể chuyển", Toast.LENGTH_SHORT).show();

                break;
            case  R.id.btngopban:
                if (status == 2 | arrItemOrder.size()>0) {
                    GridView gridViewgopban = new GridView(MainOrder.this);


                    gridViewgopban.setAdapter(new GridViewTableAdapter(MainOrder.this, R.layout.grid_item_layout, loadtablegopban(30)));
                    gridViewgopban.setNumColumns(3);
                    gridViewgopban.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // do something here
                        }
                    });

                    // Set grid view to alertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainOrder.this);
                    builder.setView(gridViewgopban);
                    builder.setTitle("Chọn bàn gộp ");

                    builder.show();
                    gridViewgopban.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            TableItem tableitemgopban = (TableItem) parent.getItemAtPosition(position);
                //            Toast.makeText(view.getContext(), tableitemgopban.getNo_(), Toast.LENGTH_SHORT).show();
                            bangop = tableitemgopban.getNo_();
                            GOPBAN();
                            finish();
                        }
                    });
                } else
                    Toast.makeText(getApplicationContext(), "Bàn không có hóa đơn, không thể gộp", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btnchebien:
                //print chebien with three option (doan,douong,dokhac)

                lineroder.setVisibility(View.INVISIBLE);
                btndatban.setVisibility(View.INVISIBLE);
                btnchuyenban.setVisibility(View.INVISIBLE);
                btngopban.setVisibility(View.INVISIBLE);
                if (mangaunhien.isEmpty()) {
                    Random hd = new Random();
                    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789";


                    mangaunhien = "HD" + alphabet.charAt(hd.nextInt(alphabet.length())) + alphabet.charAt(hd.nextInt(alphabet.length())) + MyGlobal.gettimeformat();
                }


                if (listorder.getAdapter().getCount() != 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainOrder.this);

                    alertDialog.setTitle("Xác nhận gửi hóa đơn chế biến...");

                    alertDialog.setMessage("Bạn có chắc chắn gửi hóa đơn chế biến này?");


                    alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            MyGlobal.cgdatabasesh();
                            MyGlobal.cgdatabasesl();
                            String strFiltersh = MyGlobal.COLUMNSH_OrderNo + "='" + mangaunhien + "'";
                            String strFiltersl = MyGlobal.COLUMNSL_RefNo_ + "='" + mangaunhien + "'";
                            MyGlobal.database.delete(MyGlobal.TABLE_SH, strFiltersh, null);
                            MyGlobal.database.delete(MyGlobal.TABLE_SL, strFiltersl, null);
                            getSH(1, mabanchon, khuvuc);
                            getSL();
                            sendsh = ConverttoJsonSH();
                            sendsl = ConverttoJsonSL();
                            guidatchuyen = 0;
                            sendordertoserver();
                            onSendRequest(v);
                            //finish();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                } else
                    Toast.makeText(getApplicationContext(), "HÓA ĐƠN RỖNG, KHÔNG THỂ GỬI", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnthanhtoan:
                //show total money and input money( can print)
                lineroder.setVisibility(View.INVISIBLE);
                btndatban.setVisibility(View.INVISIBLE);
                btnchuyenban.setVisibility(View.INVISIBLE);
                btngopban.setVisibility(View.INVISIBLE);
                sendsl = ConverttoJsonSL();
                Intent tt = new Intent(MainOrder.this, Payment.class);
                tt.putExtra("tongtien", tongtien);
                tt.putExtra("banchon", banchon);
                tt.putExtra("maban", mabanchon);
                tt.putExtra("khuvuc", khuvuc);
                tt.putExtra("tenkhuvuc", tenkhuvuc);

                tt.putExtra("madonhang", mangaunhien);
                tt.putExtra("thoigianvao", MyGlobal.getdaycurrent() + thoigianvao);
                tt.putExtra("sendsl", sendsl);
                startActivity(tt);


                break;
            case R.id.btndongcuaso:
                this.finish();
                break;
            case R.id.btntimkiem:
                //show dialog input name of food,drinks,other and find
                lineroder.setVisibility(View.INVISIBLE);
                btndatban.setVisibility(View.INVISIBLE);
                btnchuyenban.setVisibility(View.INVISIBLE);
                btngopban.setVisibility(View.INVISIBLE);
                Intent tk = new Intent(MainOrder.this, SearchItem.class);
                startActivity(tk);
                break;


        }
    }


    private void CHUYENBAN() {
        myAsyncTaskCB asyncTaskCB = new myAsyncTaskCB();
        asyncTaskCB.execute();
    }


    private class myAsyncTaskCB extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (checkchuyenban)

            {
                Toast.makeText(getApplicationContext(), "Đã chuyển hóa đơn sang bàn " + banchuyen, Toast.LENGTH_SHORT).show();


            }
            else
                Toast.makeText(getApplicationContext(), "Lỗi chuyển hóa đơn sang bàn " + banchuyen, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            chuyenban(MyGlobal.SourceCode, MyGlobal.ResponsibilityCenter, mangaunhien, mabanchon, banchuyen);
            return null;
        }
    }
    private void GOPBAN() {
        myAsyncTaskGB asyncTaskGB = new myAsyncTaskGB();
        asyncTaskGB.execute();
    }

    private class myAsyncTaskGB extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (checkgopban)
            {
                Toast.makeText(getApplicationContext(), "Đã gộp hóa đơn sang bàn " + bangop, Toast.LENGTH_SHORT).show();


            }
            else
                Toast.makeText(getApplicationContext(), "Lỗi gộp hóa đơn sang bàn " + bangop, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            gopban(MyGlobal.SourceCode, MyGlobal.ResponsibilityCenter, mangaunhien, mabanchon, bangop);
            return null;
        }
    }

    private class myAsyncTasksend extends AsyncTask<Void, Void, Void> {

        @Override

        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            if (MyGlobal.isOnline) {
                if (checksendsh)
                    Toast.makeText(getApplicationContext().getApplicationContext(), "Đã gửi thành công SH " + mangaunhien, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext().getApplicationContext(), "Gửi lỗi SH", Toast.LENGTH_SHORT).show();
                if (checksendsl)
                    Toast.makeText(getApplicationContext().getApplicationContext(), "Đã gửi thành công SL" + mangaunhien, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext().getApplicationContext(), "Gửi lỗi SL", Toast.LENGTH_SHORT).show();

                ContentValues v = new ContentValues();
                v.put(MyGlobal.COLUMNSH_isSync, 1);

                ContentValues v1 = new ContentValues();
                v1.put(MyGlobal.COLUMNSL_isSync, 1);
                v1.put(MyGlobal.COLUMNSL_Type,0);
                String strFiltersh = MyGlobal.COLUMNSH_OrderNo + "='" + mangaunhien + "'";
                String strFiltersl = MyGlobal.COLUMNSL_RefNo_ + "='" + mangaunhien + "'";
                MyGlobal.database.update(MyGlobal.TABLE_SH, v, strFiltersh, null);
                MyGlobal.database.update(MyGlobal.TABLE_SL, v1, strFiltersl, null);
                arrItemOrder = loadorder();
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Đã lưu thành công đơn hàng " + mangaunhien, Toast.LENGTH_SHORT).show();
            }

        }


        @Override

        protected void onPreExecute() {

            super.onPreExecute();

            //dang xu li

        }


        @Override

        protected Void doInBackground(Void... params) {

            senddatash(sendsh);
            senddatasl(sendsl);
            if (status != 2) {
                if (MyGlobal.isOnline) {
                    //   changestatus(MyGlobal.SourceCode, MyGlobal.ResponsibilityCenter, mabanchon, 2, MyGlobal.getdaycurrent() + thoigianvao);

                } else {
                    if (guidatchuyen == 0) changestatusbanday();
                    if (guidatchuyen == 1) changestatusbandat();
                    if (guidatchuyen == 2) changestatusbanchuyen(banchuyen);
                }

            }


            return null;

        }

    }

    private ArrayList<TableItem> loadtable(int size) {
        {
            ArrayList<TableItem> result = new ArrayList<TableItem>();
            if (MyGlobal.database != null) {
                Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_TABLE, null, MyGlobal.COLUMNTABLE_Status + " =?", new String[]{Integer.toString(0)}, null, null, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    TableItem ci = new TableItem();
                    new LoadImage().execute(cursor.getString(9));


                    ci.setImage(bitmap);
                    ci.setName(cursor.getString(2));
                    ci.setStatus(cursor.getInt(6));
                    ci.setStartDate(cursor.getString(10));
                    ci.setEndDate(cursor.getString(11));
                    ci.setRef_Area(cursor.getString(3));
                    ci.setNo_(cursor.getString(1));

                    if (cursor.getPosition() == size - 1) {
                        result.add(ci);

                        break;
                    } else {
                        result.add(ci);
                        cursor.moveToNext();

                    }


                }
                cursor.close();

            }

            return result;
        }
    }
    private ArrayList<TableItem> loadtablegopban(int size) {
        {
            ArrayList<TableItem> result = new ArrayList<TableItem>();
            if (MyGlobal.database != null) {
                Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_TABLE, null, MyGlobal.COLUMNTABLE_Status + " =?", new String[]{Integer.toString(2)}, null, null, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    if(!cursor.getString(2).equals(banchon)) {


                        TableItem ci = new TableItem();
                        new LoadImage().execute(cursor.getString(9));


                        ci.setImage(bitmap);
                        ci.setName(cursor.getString(2));
                        ci.setStatus(cursor.getInt(6));
                        ci.setStartDate(cursor.getString(10));
                        ci.setEndDate(cursor.getString(11));
                        ci.setRef_Area(cursor.getString(3));
                        ci.setNo_(cursor.getString(1));

                        if (cursor.getPosition() == size - 1) {
                            result.add(ci);

                            break;
                        } else {
                            result.add(ci);
                            cursor.moveToNext();

                        }
                    }
                    else cursor.moveToNext();

                }
                cursor.close();

            }

            return result;
        }
    }
    private void getSL() {
            for (int i = 0; i < arrItemOrder.size(); i++) {

                if(arrItemOrder.get(i).getDiscount()>0) {
                    ContentValues cv = new ContentValues();

                    cv.put(MyGlobal.COLUMNSL_Barcode, "Chưa có");
                    cv.put(MyGlobal.COLUMNSL_ItemNo_, arrItemOrder.get(i).getno_());
                    cv.put(MyGlobal.COLUMNSL_UnitOfMeasure, arrItemOrder.get(i).getUnitOfMeasure());
                    cv.put(MyGlobal.COLUMNSL_ValuedQuantity, Float.parseFloat(arrItemOrder.get(i).getquantity()));
                    cv.put(MyGlobal.COLUMNSL_UnitPrice, Float.parseFloat(arrItemOrder.get(i).getprices().replace(" VNĐ", "").replace(".", "").replace(",", "")));
                    cv.put(MyGlobal.COLUMNSL_RefNo_, mangaunhien);
                    cv.put(MyGlobal.COLUMNSL_ItemName, arrItemOrder.get(i).getName());
                    cv.put(MyGlobal.COLUMNSL_Type, arrItemOrder.get(i).getType());
                    cv.put(MyGlobal.COLUMNSL_Status, arrItemOrder.get(i).getStatusprint());//trang thai in che bien
                    cv.put(MyGlobal.COLUMNSL_Description, arrItemOrder.get(i).getdescription());
                    cv.put(MyGlobal.COLUMNSL_isSync, 0);
                    cv.put(MyGlobal.COLUMNSL_DiscountPT, 0);
                    cv.put(MyGlobal.COLUMNSL_Discount, arrItemOrder.get(i).getDiscount());

                    MyGlobal.database.insert(MyGlobal.TABLE_SL, null, cv);
                }
                else if(arrItemOrder.get(i).getDiscountPT()>0)
                {
                    ContentValues cv = new ContentValues();

                    cv.put(MyGlobal.COLUMNSL_Barcode, "Chưa có");
                    cv.put(MyGlobal.COLUMNSL_ItemNo_, arrItemOrder.get(i).getno_());
                    cv.put(MyGlobal.COLUMNSL_UnitOfMeasure, arrItemOrder.get(i).getUnitOfMeasure());
                    cv.put(MyGlobal.COLUMNSL_ValuedQuantity, Float.parseFloat(arrItemOrder.get(i).getquantity()));
                    cv.put(MyGlobal.COLUMNSL_UnitPrice, Float.parseFloat(arrItemOrder.get(i).getprices().replace(" VNĐ", "").replace(".", "").replace(",", "")));
                    cv.put(MyGlobal.COLUMNSL_RefNo_, mangaunhien);
                    cv.put(MyGlobal.COLUMNSL_ItemName, arrItemOrder.get(i).getName());
                    cv.put(MyGlobal.COLUMNSL_Type, arrItemOrder.get(i).getType());
                    cv.put(MyGlobal.COLUMNSL_Status, arrItemOrder.get(i).getStatusprint());//trang thai in che bien
                    cv.put(MyGlobal.COLUMNSL_Description, arrItemOrder.get(i).getdescription());
                    cv.put(MyGlobal.COLUMNSL_isSync, 0);
                    cv.put(MyGlobal.COLUMNSL_DiscountPT, arrItemOrder.get(i).getDiscountPT());
                    cv.put(MyGlobal.COLUMNSL_Discount, 0);

                    MyGlobal.database.insert(MyGlobal.TABLE_SL, null, cv);
                }
                else {
                    ContentValues cv = new ContentValues();

                    cv.put(MyGlobal.COLUMNSL_Barcode, "Chưa có");
                    cv.put(MyGlobal.COLUMNSL_ItemNo_, arrItemOrder.get(i).getno_());
                    cv.put(MyGlobal.COLUMNSL_UnitOfMeasure, arrItemOrder.get(i).getUnitOfMeasure());
                    cv.put(MyGlobal.COLUMNSL_ValuedQuantity, Float.parseFloat(arrItemOrder.get(i).getquantity()));
                    cv.put(MyGlobal.COLUMNSL_UnitPrice, Float.parseFloat(arrItemOrder.get(i).getprices().replace(" VNĐ", "").replace(".", "").replace(",", "")));
                    cv.put(MyGlobal.COLUMNSL_RefNo_, mangaunhien);
                    cv.put(MyGlobal.COLUMNSL_ItemName, arrItemOrder.get(i).getName());
                    cv.put(MyGlobal.COLUMNSL_Type, arrItemOrder.get(i).getType());
                    cv.put(MyGlobal.COLUMNSL_Status, arrItemOrder.get(i).getStatusprint());//trang thai in che bien
                    cv.put(MyGlobal.COLUMNSL_Description, arrItemOrder.get(i).getdescription());
                    cv.put(MyGlobal.COLUMNSL_isSync, 0);
                    cv.put(MyGlobal.COLUMNSL_DiscountPT, 0);
                    cv.put(MyGlobal.COLUMNSL_Discount, 0);

                    MyGlobal.database.insert(MyGlobal.TABLE_SL, null, cv);
                }
            }



    }

    private void sendordertoserver() {


        myAsyncTasksend myRequest = new myAsyncTasksend();

        myRequest.execute();
    }

    private void chuyenbanserver() {
        myAsyncTaskRemove myAsyncTaskRemove = new myAsyncTaskRemove();
        myAsyncTaskRemove.execute();
    }

    private class myAsyncTaskRemove extends AsyncTask<Void, Void, Void> {

        @Override

        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
   //         if (checkxoahd)
//                Toast.makeText(getApplicationContext(), "Delete success", Toast.LENGTH_SHORT).show();
//            else Toast.makeText(getApplicationContext(), "Delete fail", Toast.LENGTH_SHORT).show();
            String strFiltersh = MyGlobal.COLUMNSH_OrderNo + "='" + mangaunhien + "'" + " AND " + MyGlobal.COLUMNSH_Ref_Table + "='" + mabanchon + "'";
            MyGlobal.database.delete(MyGlobal.TABLE_SH, strFiltersh, null);
        }


        @Override

        protected void onPreExecute() {

            super.onPreExecute();

            //dang xu li

        }


        @Override

        protected Void doInBackground(Void... params) {

            RemoveSalesHeader(mangaunhien, mabanchon);


            return null;

        }

    }

    private void getSH(int statussh, String table, String Area) {
        tongtien=0;

        for(int i=0;i<arrItemOrder.size();i++)
        {
            if(arrItemOrder.get(i).getDiscount()>0)
                tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))-Float.parseFloat(arrItemOrder.get(i).getquantity())*arrItemOrder.get(i).getDiscount();
            else if(arrItemOrder.get(i).getDiscountPT()>0)
                tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""))*(100-arrItemOrder.get(i).getDiscountPT())/100;
            else tongtien = tongtien+Float.parseFloat(arrItemOrder.get(i).getquantity())*Float.parseFloat(arrItemOrder.get(i).getprices().replace("VNĐ","").replace(",","").replace(".",""));


        }
        if(!giamgiahoadon.isEmpty()){
            if(giamgiahoadon.indexOf("%")!=-1)
                tongtien =tongtien * (100-Float.parseFloat(giamgiahoadon.replace("%","").replace("-","")))/100;
            else tongtien =tongtien - Float.parseFloat(giamgiahoadon.replace("-", "").replace(".","").replace(",",""));
        }
        sttsh++;
        ContentValues cv = new ContentValues();

        cv.put(MyGlobal.COLUMNSH_OrderNo, mangaunhien);
        cv.put(MyGlobal.COLUMNSH_CustomerNo, "Chưa có");
        cv.put(MyGlobal.COLUMNSH_CreateDate, MyGlobal.getdaycurrent().replace(" ", "T") + thoigianvao + ":00");
        cv.put(MyGlobal.COLUMNSH_Description, "Test gửi SH");
        cv.put(MyGlobal.COLUMNSH_AmountTotal, tongtien);
        cv.put(MyGlobal.COLUMNSH_Type, status);
        cv.put(MyGlobal.COLUMNSH_STT, sttsh + "");
        cv.put(MyGlobal.COLUMNSH_CustomerName, "Chưa có");
        cv.put(MyGlobal.COLUMNSH_SalerNo, MyGlobal.SalerNo);
        cv.put(MyGlobal.COLUMNSH_SalerName, MyGlobal.SalerName);
        cv.put(MyGlobal.COLUMNSH_TypeOrder, 0);
        cv.put(MyGlobal.COLUMNSH_Ref_Area, Area);
        cv.put(MyGlobal.COLUMNSH_Ref_Table, table);
        cv.put(MyGlobal.COLUMNSH_Status, statussh); //chua thanh toan
        cv.put(MyGlobal.COLUMNSH_AmountCustomer, 0);
        cv.put(MyGlobal.COLUMNSH_AmountReturn, 0);
        cv.put(MyGlobal.COLUMNSH_isSync, 0);
        cv.put(MyGlobal.COLUMNSH_EndDate, "");
        if (giamgiahoadon != null) {
            if (giamgiahoadon.isEmpty()) {
                cv.put(MyGlobal.COLUMNSH_DiscountPT, 0);
                cv.put(MyGlobal.COLUMNSH_Discount, 0);
            } else {
                if (giamgiahoadon.indexOf("%") != -1) {
                    cv.put(MyGlobal.COLUMNSH_DiscountPT, Float.parseFloat(giamgiahoadon.replace("%", "").replace("-","")));
                    cv.put(MyGlobal.COLUMNSH_Discount, 0);
                } else {
                    cv.put(MyGlobal.COLUMNSH_DiscountPT, 0);
                    cv.put(MyGlobal.COLUMNSH_Discount, Float.parseFloat(giamgiahoadon.replace(".", "").replace(",", "")));
                }
            }
        }


        MyGlobal.database.insert(MyGlobal.TABLE_SH, null, cv);
    }


    private void changestatusbanday() {
        String strFilter = MyGlobal.COLUMNTABLE_No + "='" + mabanchon + "'";
        ContentValues args = new ContentValues();
        args.put(MyGlobal.COLUMNTABLE_isSync, 0);
        args.put(MyGlobal.COLUMNTABLE_Status, 2);
        args.put(MyGlobal.COLUMNTABLE_StartDate, MyGlobal.getdaycurrent().replace(" ", "T") + thoigianvao);
        MyGlobal.database.update(MyGlobal.TABLE_TABLE, args, strFilter, null);
    }

    private void changestatusbandat() {
        String strFilter = MyGlobal.COLUMNTABLE_No + "='" + mabanchon + "'";
        ContentValues args = new ContentValues();
        args.put(MyGlobal.COLUMNTABLE_isSync, 0);
        args.put(MyGlobal.COLUMNTABLE_Status, 1);
        args.put(MyGlobal.COLUMNTABLE_StartDate, MyGlobal.getdaycurrent().replace(" ", "T") + thoigianvao);
        MyGlobal.database.update(MyGlobal.TABLE_TABLE, args, strFilter, null);
    }

    private void changestatusbanchuyen(String mabanchuyen) {
        String strFilter0 = MyGlobal.COLUMNTABLE_No + "='" + mabanchon + "'";
        ContentValues args0 = new ContentValues();
        args0.put(MyGlobal.COLUMNTABLE_isSync, 0);
        args0.put(MyGlobal.COLUMNTABLE_Status, 0);
        args0.put(MyGlobal.COLUMNTABLE_StartDate, "1900-01-01T00:00:00");
        MyGlobal.database.update(MyGlobal.TABLE_TABLE, args0, strFilter0, null);
        String strFilter = MyGlobal.COLUMNTABLE_No + "='" + mabanchuyen + "'";
        ContentValues args = new ContentValues();
        args.put(MyGlobal.COLUMNTABLE_isSync, 0);
        args.put(MyGlobal.COLUMNTABLE_Status, 2);
        args.put(MyGlobal.COLUMNTABLE_StartDate, MyGlobal.getdaycurrent().replace(" ", "T") + thoigianvao);
        MyGlobal.database.update(MyGlobal.TABLE_TABLE, args, strFilter, null);
    }


    public String ConverttoJsonSH() {
        String kq = "";
        List<SalesHeader> ListHeader = loadheader();
        if (ListHeader != null) {
            Gson gson = new Gson();
            kq = gson.toJson(ListHeader);
        }
        return kq;
    }

    public String ConverttoJsonSL() {
        String kq = "";
        List<SalesLine> ListLine = loadline();
        if (ListLine != null) {
            Gson gson = new Gson();
            kq = gson.toJson(ListLine);
        }
        return kq;
    }

    private List<SalesHeader> loadheader() {
        List<SalesHeader> result = new ArrayList<SalesHeader>();

        if (MyGlobal.database != null) {
            Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_SH, null, MyGlobal.COLUMNSH_OrderNo + " = ?", new String[]{mangaunhien}, null, null, null);
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
                ci.setEndDate(cursor.getString(20));
                result.add(ci);

                cursor.moveToNext();
            }
            cursor.close();
        }

        return result;
    }

    private List<SalesLine> loadline() {
        List<SalesLine> result = new ArrayList<SalesLine>();

        if (MyGlobal.database != null) {
            Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_SL, null, MyGlobal.COLUMNSL_RefNo_ + " = ?", new String[]{mangaunhien}, null, null, null);

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
                ci.setStatusPrint(cursor.getFloat(9));
                ci.setDescription(cursor.getString(10));
                ci.setDiscountPT(cursor.getFloat(12));
                ci.setDiscount(cursor.getFloat(13));
                ci.setItemType(cursor.getInt(14));
                result.add(ci);


                cursor.moveToNext();
            }
            cursor.close();
        }

        return result;
    }

    private void chuyenban(String SourceCode, String ResponsibilityCenter, String No, String TableFrom, String TableTo) {
        SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_CHUYENBAN);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("SourceCode");
        pi.setValue(SourceCode);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("ResponsibilityCenter");
        pi.setValue(ResponsibilityCenter);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("OrderNo");
        pi.setValue(No);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("TableFrom");
        pi.setValue(TableFrom);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("TableTo");
        pi.setValue(TableTo);
        pi.setType(String.class);
        request.addProperty(pi);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
        try {
            transportSE.call(MyGlobal.SOAP_ACTION_CHUYENBAN, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            checkchuyenban = Boolean.parseBoolean(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
    private void gopban(String SourceCode, String ResponsibilityCenter, String No, String TableFrom, String TableTo) {
        SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_GOPBAN);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("SourceCode");
        pi.setValue(SourceCode);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("ResponsibilityCenter");
        pi.setValue(ResponsibilityCenter);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("OrderNo");
        pi.setValue(No);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("TableFrom");
        pi.setValue(TableFrom);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("TableTo");
        pi.setValue(TableTo);
        pi.setType(String.class);
        request.addProperty(pi);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
        try {
            transportSE.call(MyGlobal.SOAP_ACTION_GOPBAN, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            checkgopban = Boolean.parseBoolean(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
    private void senddatash(String senddata) {
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
            checksendsh = Boolean.parseBoolean(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void senddatasl(String senddata) {
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
            checksendsl = Boolean.parseBoolean(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void RemoveSalesHeader(String madonhang, String mabanchuyen) {
        SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_REMOVESH);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("OrderNo");
        pi.setValue(madonhang);
        pi.setType(String.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
        pi.setName("TableNo");
        pi.setValue(mabanchuyen);
        pi.setType(String.class);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
        try {
            transportSE.call(MyGlobal.SOAP_ACTION_REMOVESH, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            checkxoahd = Boolean.parseBoolean(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

}
