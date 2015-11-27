package androidltg.stanstudios.com.dmsmanager;

import android.content.ContentValues;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

public class Main extends AppCompatActivity implements View.OnClickListener {
    GridView gridView;
    private GridViewTableAdapter gridAdapter;
    JSONArray Array = new JSONArray();
    private Bitmap bitmap;
    Button btnthongtin, btndangmo, btnthongke, btndangxuat;
    ListView listkhuvuc;
    TextView txtuser;
    ProgressBar progressBar;
    private String datazone = "", datatable = "",datanumpage="", chonkhuvuc = "Tiền Sảnh",makhuvuc="KV1";


    private Handler mHandler = new Handler();
    private  boolean isRunning = true;
    private ListKhuVucArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getid();
        txtuser.setText(MyGlobal.SalerName);
        MyGlobal.context = getApplicationContext();//Get context to MyGlobal ( create database need it)
        if(MyGlobal.isOnline()) MyGlobal.isOnline=true;// set isonline firt run
                else MyGlobal.isOnline=false;
        getfromserver();//get data

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MyGlobal.isOnline) MyGlobal.database.delete(MyGlobal.TABLE_TABLE,null,null);// delete old table
        getfromserver();//update
    }

    private void getfromserver() {
            MyGlobal.cgdatabasezone();
            MyGlobal.cgdatabasetable();
        CAPNHATDULIEU();
    }

    private void CAPNHATDULIEU() {

        myAsyncTask myRequest = new myAsyncTask();

        myRequest.execute();

    }

    private class myAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override

        protected void onPostExecute(Void result) {//three

            super.onPostExecute(result);

            try {
                if(MyGlobal.isOnline)// if online then parse data from server
                {
                    gridAdapter=null;
                    parsejsonzone();
                    parsejsontable();
                    parsejsonnumpage();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressBar.setVisibility(View.INVISIBLE);
            xuli();
            gridAdapter.notifyDataSetChanged();

        }


        @Override

        protected void onPreExecute() {//second

            super.onPreExecute();

            //dang xu li
            progressBar.setVisibility(View.VISIBLE);

        }


        @Override

        protected Void doInBackground(Void... params) {//first

            if (MyGlobal.isOnline) {// if online then get data from server
                getZone(MyGlobal.SourceCode, MyGlobal.ResponsibilityCenter);
                getTable(MyGlobal.SourceCode, MyGlobal.ResponsibilityCenter, makhuvuc);
                getnumpageitem(MyGlobal.SourceCode,MyGlobal.ResponsibilityCenter,1);
            }
            return null;

        }

    }

    private void parsejsonzone() throws JSONException {
        JSONObject jsonObject = new JSONObject(datazone);
        Array = jsonObject.getJSONArray("Table");
        int ncustomer = Array.length();
        for (int i = 0; i < ncustomer; i++) {
            ContentValues cv = new ContentValues();
            cv.put(MyGlobal.COLUMNZONE_No, Array.getJSONObject(i).getString("No_"));
            cv.put(MyGlobal.COLUMNZONE_NAME, Array.getJSONObject(i).getString("Name"));
            cv.put(MyGlobal.COLUMNZONE_SourceCode, Array.getJSONObject(i).getString("SourceCode"));
            cv.put(MyGlobal.COLUMNZONE_ResponsibilityCenter, Array.getJSONObject(i).getString("ResponsibilityCenter"));
            cv.put(MyGlobal.COLUMNZONE_Description, Array.getJSONObject(i).getString("Description"));
            cv.put(MyGlobal.COLUMNZONE_Status, Integer.parseInt(Array.getJSONObject(i).getString("Status")));
            cv.put(MyGlobal.COLUMNZONE_LinkPicture, Array.getJSONObject(i).getString("LinkPicture"));
            if (!MyGlobal.isLTGFieldExist(MyGlobal.database, MyGlobal.TABLE_ZONE, MyGlobal.COLUMNZONE_No, Array.getJSONObject(i).getString("No_"),MyGlobal.COLUMNZONE_ResponsibilityCenter,Array.getJSONObject(i).getString("ResponsibilityCenter"))) {


                MyGlobal.database.insert(MyGlobal.TABLE_ZONE, null, cv);
            }

        }

    }

    private void parsejsonnumpage() throws JSONException {
        JSONObject jsonObject = new JSONObject(datanumpage);
        Array = jsonObject.getJSONArray("Table");
        int ncustomer = Array.length();
        MyGlobal.NumPage = Array.getJSONObject(0).getInt("Column1");
        if(MyGlobal.NumPage<100) {
            MyGlobal.indexpage =MyGlobal.NumPage;
            MyGlobal.index=0;
            MyGlobal.totalindex=1;
        }
        else {
            MyGlobal.indexpage=100;
            MyGlobal.index=0;
            if(MyGlobal.NumPage%100!=0) {
                MyGlobal.totalindex=MyGlobal.NumPage/100 +1;
                MyGlobal.lastindexpage=MyGlobal.NumPage%100;
            }
            else {
                MyGlobal.totalindex = MyGlobal.NumPage/100;
                MyGlobal.lastindexpage=0;
            }


        }

    }
    private void parsejsontable() throws JSONException {
        JSONObject jsonObject = new JSONObject(datatable);
        Array = jsonObject.getJSONArray("Table");
        int ncustomer = Array.length();
        for (int i = 0; i < ncustomer; i++) {
            ContentValues cv = new ContentValues();
            cv.put(MyGlobal.COLUMNTABLE_No, Array.getJSONObject(i).getString("No_"));
            cv.put(MyGlobal.COLUMNTABLE_NAME, Array.getJSONObject(i).getString("Name"));
            cv.put(MyGlobal.COLUMNTABLE_Ref_Area, Array.getJSONObject(i).getString("Ref_Area"));
            cv.put(MyGlobal.COLUMNTABLE_SourceCode, Array.getJSONObject(i).getString("SourceCode"));
            cv.put(MyGlobal.COLUMNTABLE_ResponsibilityCenter, Array.getJSONObject(i).getString("ResponsibilityCenter"));
            cv.put(MyGlobal.COLUMNTABLE_Status, Integer.parseInt(Array.getJSONObject(i).getString("Status")));
            cv.put(MyGlobal.COLUMNTABLE_Description, Array.getJSONObject(i).getString("Description"));
            cv.put(MyGlobal.COLUMNTABLE_LinkPicture, Array.getJSONObject(i).getString("LinkPicture"));
            cv.put(MyGlobal.COLUMNTABLE_StartDate, Array.getJSONObject(i).getString("StartDate"));
            cv.put(MyGlobal.COLUMNTABLE_EndDate, Array.getJSONObject(i).getString("EndDate"));
            cv.put(MyGlobal.COLUMNTABLE_OrderNo, Array.getJSONObject(i).getString("OrderNo"));//Bill
            cv.put(MyGlobal.COLUMNTABLE_isSync,1); //sync true
            if (!MyGlobal.isLTGFieldExist(MyGlobal.database, MyGlobal.TABLE_TABLE, MyGlobal.COLUMNTABLE_No, Array.getJSONObject(i).getString("No_"),MyGlobal.COLUMNTABLE_ResponsibilityCenter,Array.getJSONObject(i).getString("ResponsibilityCenter"))) {



                MyGlobal.database.insert(MyGlobal.TABLE_TABLE, null, cv);
            }
        }

    }


    private void getZone(String sourcecode, String responcenter) {
        SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_ZONE);
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
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
        try {
            transportSE.call(MyGlobal.SOAP_ACTION_ZONE, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            datazone = response.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    private void getTable(String sourcecode, String responcenter,String zone) {
        SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_TABLE);
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
        pi.setName("Zone");
        pi.setValue(zone);
        pi.setType(String.class);
        request.addProperty(pi);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
        try {
            transportSE.call(MyGlobal.SOAP_ACTION_TABLE, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            datatable = response.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
    private void getnumpageitem(String sourcecode, String responcenter,int page) {
        SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_GETNUMITEM);
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
        pi.setName("page");
        pi.setValue(page);
        pi.setType(Integer.class);
        request.addProperty(pi);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);
        try {
            transportSE.call(MyGlobal.SOAP_ACTION_GETNUMITEM, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            datanumpage = response.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
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

        }
    }

    private ArrayList<ItemKhuVuc> loadzone() {
        {
            ArrayList<ItemKhuVuc> result = new ArrayList<ItemKhuVuc>();
            if (MyGlobal.database != null) {
                Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_ZONE, null,MyGlobal.COLUMNZONE_SourceCode+" =? AND "+MyGlobal.COLUMNZONE_ResponsibilityCenter +" =?", new String[]{MyGlobal.SourceCode,MyGlobal.ResponsibilityCenter}, null, null, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

                    ItemKhuVuc ci = new ItemKhuVuc();

                    ci.setNo_(cursor.getString(1));
                    ci.setName(cursor.getString(2));
                    ci.setsourcecode(cursor.getString(3));
                    ci.setresponsibititycenter(cursor.getString(4));
                    ci.setdescription(cursor.getString(5));
                    ci.setstatus(cursor.getInt(6));
                    ci.setlinkpicture(cursor.getString(7));

                    result.add(ci);
                    cursor.moveToNext();


                }
                cursor.close();
            }

            return result;
        }
    }

    private ArrayList<TableItem> loadtable(int size) {
        {
            ArrayList<TableItem> result = new ArrayList<TableItem>();
            if (MyGlobal.database != null) {
                Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_TABLE, null,MyGlobal.COLUMNTABLE_SourceCode+" =? AND "+MyGlobal.COLUMNTABLE_ResponsibilityCenter+" =? AND "+MyGlobal.COLUMNTABLE_Ref_Area + " = ?", new String[]{MyGlobal.SourceCode,MyGlobal.ResponsibilityCenter,makhuvuc}, null, null, null);
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


    private void xuli() {
        gridAdapter = new GridViewTableAdapter(this, R.layout.grid_item_layout, loadtable(30));

        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TableItem item = (TableItem) parent.getItemAtPosition(position);
                //Create intent
                Intent order = new Intent(Main.this, MainOrder.class);
                order.putExtra("BANCHON", item.getName());
                order.putExtra("MABANCHON", item.getNo_());
                order.putExtra("KHUVUC", item.getRef_Area());
                order.putExtra("StartDate", item.getStartDate());
                order.putExtra("Status", item.getStatus());
                order.putExtra("TENKHUVUC", chonkhuvuc);
                startActivity(order);
                //Start details activity
            }

        });
        adapter = new ListKhuVucArrayAdapter(
                this,
                R.layout.customlistviewkhuvuc,// lấy custom layout
                loadzone()/*thiết lập data source*/);
        listkhuvuc.setAdapter(adapter);//gán Adapter vào Lisview
        listkhuvuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                final ItemKhuVuc itemkv = (ItemKhuVuc) parent.getItemAtPosition(position);


                Toast.makeText(getApplicationContext(), "Đã chọn" + itemkv.getName(), Toast.LENGTH_SHORT).show();
                chonkhuvuc = itemkv.getName();
                makhuvuc = itemkv.getNo_();
                CAPNHATDULIEU();
            }
        });


    }

    private ArrayList<TableItem> loadtablebyzone(String no_) {

        ArrayList<TableItem> result = new ArrayList<TableItem>();
        if (MyGlobal.database != null) {
            Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_TABLE, null, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (cursor.getString(3).equals(no_)) {
                    TableItem ci = new TableItem();
                    new LoadImage().execute(cursor.getString(9));
                    ci.setImage(bitmap);
                    ci.setName(cursor.getString(2));
                    ci.setStatus(cursor.getInt(6));
                    ci.setStartDate(cursor.getString(10));
                    ci.setEndDate(cursor.getString(11));

                    ci.setRef_Area(cursor.getString(3));
                    ci.setNo_(cursor.getString(1));
                    result.add(ci);
                    cursor.moveToNext();

                }
                cursor.moveToNext();

            }
            cursor.close();

        }

        return result;
    }

    private void getid() {
        btndangmo = (Button) findViewById(R.id.btndangmo);
        btndangmo.setOnClickListener(this);
        btndangxuat = (Button) findViewById(R.id.btndangxuat);
        btndangxuat.setOnClickListener(this);
        listkhuvuc = (ListView) findViewById(R.id.listkhuvuc);
        btnthongke = (Button) findViewById(R.id.btnthongke);
        btnthongke.setOnClickListener(this);
        btnthongtin = (Button) findViewById(R.id.btnthongtin);
        btnthongtin.setOnClickListener(this);
        txtuser = (TextView) findViewById(R.id.txtusername);
        gridView = (GridView) findViewById(R.id.gridView);
        progressBar = (ProgressBar) findViewById(R.id.progress);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnthongtin:
                break;
            case R.id.btndangmo:
                Intent dm = new Intent(Main.this, TableOpening.class);
                startActivity(dm);
                break;
            case R.id.btnthongke:
//                Intent tk = new Intent(Main.this, Statistical.class);
//                startActivity(tk);
                break;
            case R.id.btndangxuat:
                this.finish();
                break;
        }

    }
}
