//package androidltg.stanstudios.com.dmsmanager;
//
//import android.database.Cursor;
//import android.os.AsyncTask;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ListView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import org.ksoap2.SoapEnvelope;
//import org.ksoap2.serialization.PropertyInfo;
//import org.ksoap2.serialization.SoapObject;
//import org.ksoap2.serialization.SoapPrimitive;
//import org.ksoap2.serialization.SoapSerializationEnvelope;
//import org.ksoap2.transport.HttpTransportSE;
//import org.xmlpull.v1.XmlPullParserException;
//
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class Statistical extends AppCompatActivity implements View.OnClickListener{
//    Button btninlaibill,btnthoat,btnbaocaoketca,btnbaocaomathang;
//    TextView txtthongketongtien,txtthongkebanhientai;
//    ListView listthongkeorder;
//    GridView gridViewthongke;
//    ListOrderArrayAdapter listOrderArrayAdapter;
//    ArrayList<ItemOrder> arrItemOrder;
//    GridViewTableAdapter gridViewAdapter;
//    Spinner spinnhanvien,spintungay,spindenngay;
//    String FromDate="",ToDate="",SalerName="",mahoadon="",datashad="",dataslad="";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_statistic);
//        getid();
//
//        getfromserver();
//    }
//
//    private void getfromserver() {
//        myAsyncTask myAsyncTaskstatic = new myAsyncTask();
//        myAsyncTaskstatic.execute();
//
//    }
//    private class myAsyncTask extends AsyncTask<Void,Void,Void>{
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            xuli();
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            getSHAD(FromDate, ToDate, SalerName, MyGlobal.SourceCode,MyGlobal.ResponsibilityCenter);
//            getSLAD(mahoadon);
//            return null;
//        }
//    }
//    private void getSHAD(String FromDate, String ToDate, String SalerName, String SourceCode,String Center) {
//        SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_SH_AD);
//        PropertyInfo pi = new PropertyInfo();
//        pi.setName("FromDate");
//        pi.setValue(FromDate);
//        pi.setType(String.class);
//        request.addProperty(pi);
//        pi = new PropertyInfo();
//        pi.setName("ToDate");
//        pi.setValue(ToDate);
//        pi.setType(String.class);
//        request.addProperty(pi);
//        pi =new PropertyInfo();
//        pi.setName("SalerName");
//        pi.setValue(SalerName);
//        pi.setType(String.class);
//        pi =new PropertyInfo();
//        pi.setName("SourceCode");
//        pi.setValue(SourceCode);
//        pi.setType(String.class);
//        pi =new PropertyInfo();
//        pi.setName("Center");
//        pi.setValue(Center);
//        pi.setType(String.class);
//
//
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        envelope.dotNet = true;
//        envelope.setOutputSoapObject(request);
//        HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
//        try {
//            transportSE.call(MyGlobal.SOAP_ACTION_SH_AD, envelope);
//            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
//            datashad = response.toString();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getSLAD(String No_) {
//        SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_SL_AD);
//        PropertyInfo pi = new PropertyInfo();
//        pi.setName("No_");
//        pi.setValue(No_);
//        pi.setType(String.class);
//
//
//
//        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
//        envelope.dotNet = true;
//        envelope.setOutputSoapObject(request);
//        HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
//        try {
//            transportSE.call(MyGlobal.SOAP_ACTION_SL_AD, envelope);
//            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
//            dataslad = response.toString();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (XmlPullParserException e) {
//            e.printStackTrace();
//        }
//    }
//    private void xuli() {
//        gridViewAdapter = new GridViewTableAdapter(this, R.layout.grid_item_layout, loadtablethongke());
//
//        gridViewthongke.setAdapter(gridViewAdapter);
//        arrItemOrder = new ArrayList<ItemOrder>();
//        listOrderArrayAdapter = new ListOrderArrayAdapter(
//                this,
//                R.layout.customlistvieworder,// lấy custom layout
//                arrItemOrder/*thiết lập data source*/);
//        listthongkeorder.setAdapter(listOrderArrayAdapter);//gán Adapter vào Lisview
//
//    }
//    private ArrayList<TableStatic> loadtablethongke() {
//        {
//            ArrayList<TableStatic> result = new ArrayList<TableStatic>();
//            if (MyGlobal.database != null) {
//                Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_SH_AD, null,MyGlobal.COLUMNSH_ADCreateDate+" >? AND "+MyGlobal.COLUMNSH_ADCreateDate+" <? AND ", new String[]{FromDate,ToDate}, null, null, null);
//                cursor.moveToFirst();
//                while (!cursor.isAfterLast()) {
//
//                    TableStatic ci = new TableStatic();
//
//                    ci.setName(cursor.getString(13));
//                    ci.setStatus(cursor.getInt(14));
//                    ci.setDate(cursor.getString(3));
//                    ci.setAmount(cursor.getFloat(5));
//                    ci.setOrderNo(cursor.getString(1));
//                 {
//                        result.add(ci);
//                        cursor.moveToNext();
//
//                    }
//
//
//                }
//                cursor.close();
//
//            }
//
//            return result;
//        }
//    }
//
//    private void getid() {
//        spinnhanvien = (Spinner)findViewById(R.id.spinnhanvien);
//        spintungay = (Spinner) findViewById(R.id.spintungay);
//        spindenngay = (Spinner) findViewById(R.id.spindenngay);
//        btnbaocaoketca = (Button)findViewById(R.id.btnbaocaoketca);
//        btnbaocaoketca.setOnClickListener(this);
//        btnbaocaomathang = (Button) findViewById(R.id.btnmathangban);
//        btnbaocaomathang.setOnClickListener(this);
//        btninlaibill = (Button)findViewById(R.id.btninlaibill);
//        btninlaibill.setOnClickListener(this);
//        btnthoat = (Button)findViewById(R.id.btnthoat);
//        btnthoat.setOnClickListener(this);
//        txtthongketongtien = (TextView)findViewById(R.id.txtthongketongtien);
//        txtthongkebanhientai = (TextView)findViewById(R.id.txtthongkebanhientai);
//        listthongkeorder = (ListView)findViewById(R.id.listthongkeorder);
//        gridViewthongke = (GridView)findViewById(R.id.gridViewThongKe);
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_statistic, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btnbaocaoketca:
//                //option print
//                break;
//            case R.id.btnmathangban:
//                //option print
//                break;
//            case R.id.btninlaibill:
//                //option print again bill of one table
//                break;
//            case R.id.btnthoat:
//                this.finish();
//                break;
//        }
//    }
//}
