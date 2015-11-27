package androidltg.stanstudios.com.dmsmanager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class TableOpening extends AppCompatActivity {
    private GridView gridopening;
    private GridViewTableOpenAdapter gridAdapter;
    private Button btndong;
    private JSONArray Array = new JSONArray();
    private String  SourceCode = "01", ResponsibilityCenter = "0101",datatableopen="";
    private int Status=2;
    //TABLEOPEN



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_opening);
        getid();
        getfromserver();
    }

    private void getfromserver() {

            MyGlobal.cgdatabaseopen();
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

                parsejsontableopen();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            xuli();
            gridAdapter.notifyDataSetChanged();

        }


        @Override

        protected void onPreExecute() {//second

            super.onPreExecute();

            //dang xu li


        }


        @Override

        protected Void doInBackground(Void... params) {//first


            getTableOpen(Status,SourceCode, ResponsibilityCenter);
            return null;

        }

    }
    private void getTableOpen(int Status,String sourcecode, String responcenter) {
        SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_TABLEOPEN);
        PropertyInfo pi = new PropertyInfo();
        pi.setName("Status");
        pi.setValue(Status);
        pi.setType(Integer.class);
        request.addProperty(pi);
        pi = new PropertyInfo();
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
            transportSE.call(MyGlobal.SOAP_ACTION_TABLEOPEN, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            datatableopen = response.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
    private void parsejsontableopen() throws JSONException {
        JSONObject jsonObject = new JSONObject(datatableopen);
        Array = jsonObject.getJSONArray("Table");
        int ncustomer = Array.length();
        MyGlobal.database.delete(MyGlobal.TABLE_OPEN, null, null);//delete old table
        for (int i = 0; i < ncustomer; i++) {
            ContentValues cv = new ContentValues();
            cv.put(MyGlobal.COLUMNTABLEOPEN_No, Array.getJSONObject(i).getString("No_"));
            cv.put(MyGlobal.COLUMNTABLEOPEN_NAME, Array.getJSONObject(i).getString("Name"));
            cv.put(MyGlobal.COLUMNTABLEOPEN_Ref_Area, Array.getJSONObject(i).getString("Ref_Area"));
            cv.put(MyGlobal.COLUMNTABLEOPEN_SourceCode, Array.getJSONObject(i).getString("SourceCode"));
            cv.put(MyGlobal.COLUMNTABLEOPEN_ResponsibilityCenter, Array.getJSONObject(i).getString("ResponsibilityCenter"));
            cv.put(MyGlobal.COLUMNTABLEOPEN_StartDate, Array.getJSONObject(i).getString("StartDate"));
            cv.put(MyGlobal.COLUMNTABLEOPEN_OrderNo, Array.getJSONObject(i).getString("OrderNo"));//Bill



            MyGlobal.database.insert(MyGlobal.TABLE_OPEN, null, cv);

        }

    }

    private void xuli() {
        gridAdapter = new GridViewTableOpenAdapter(this, R.layout.grip_item_opening, loadtableopen());

        gridopening.setAdapter(gridAdapter);
        gridopening.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                TableOpen item = (TableOpen) parent.getItemAtPosition(position);
                //Create intent
                Intent order = new Intent(TableOpening.this, MainOrder.class);
                order.putExtra("BANCHON", item.getTitle());
                order.putExtra("MABANCHON", item.getNo_());
                order.putExtra("KHUVUC", item.getArea());
                order.putExtra("StartDate", item.getstartdate());
                order.putExtra("Status", item.getStatus());
                order.putExtra("TENKHUVUC", item.gettitlearea());
                startActivity(order);
                //Start details activity
            }

        });
        btndong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private ArrayList<TableOpen> loadtableopen() {

        ArrayList<TableOpen> result = new ArrayList<TableOpen>();
        if (MyGlobal.database != null) {
            Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_OPEN, null, null, null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                TableOpen ci = new TableOpen();
                ci.setstt(Integer.parseInt(cursor.getString(7)));
                ci.setTitle(cursor.getString(2));
                ci.setstartdate(cursor.getString(6));
                ci.setArea(cursor.getString(3));
                ci.setNo_(cursor.getString(1));
                ci.setStatus(2);
                ci.setprices("100000");
                    result.add(ci);
                    cursor.moveToNext();




            }
            cursor.close();

        }

        return result;
    }

    private void getid() {
        gridopening = (GridView) findViewById(R.id.gridViewDangmo);
        btndong = (Button)findViewById(R.id.btndongopening);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_table_opening, menu);
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
}
