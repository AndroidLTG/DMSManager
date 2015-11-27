package androidltg.stanstudios.com.dmsmanager;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Payment extends AppCompatActivity implements View.OnClickListener {
    private String banchon, madonhang, maban, khuvuc, tenkhuvuc, tempmoney = "", thoigianvao,                  sendsh, sendsl, SourceCode = "01", ResponsibilityCenter = "0101";
    private float tongtien = 0, khachdua = 0, tralai = 0;
    private Button btn10k, btn20k, btn50k, btn100k, btn200k, btn500k, btn1000k, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0, btn000, btntamtinh, btnhuybo, btninhoadon, btntienmat, btnthe, btnnhaplai;
    private TextView txtkhachnotra, txttieudethanhtoan;
    private EditText edittongtien, editkhachdua, editkhachnotra;
    private boolean checksendsh = false, checksendsl = false, checkchange = false;
    private int stthd = 0;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getid();
        Intent i = getIntent();
        banchon = i.getStringExtra("banchon");
        maban = i.getStringExtra("maban");
        khuvuc = i.getStringExtra("khuvuc");
        tenkhuvuc = i.getStringExtra("tenkhuvuc");
        tongtien = i.getFloatExtra("tongtien", 0);

        madonhang = i.getStringExtra("madonhang");
        thoigianvao = i.getStringExtra("thoigianvao");
        sendsl = i.getStringExtra("sendsl");
        xuli();
    }

    private void xuli() {
        txttieudethanhtoan.setText("THANH TOÁN HÓA ĐƠN : Khu vực " + tenkhuvuc + " - " + banchon);
        edittongtien.setText(MyGlobal.formatfloat(tongtien) + "");
        edittongtien.setEnabled(false);
        editkhachdua.setText(MyGlobal.formatfloat(tongtien) + "");
        editkhachnotra.setText("0");
        editkhachnotra.setBackgroundDrawable(getResources().getDrawable(R.drawable.customeditthanhtoan));
        txtkhachnotra.setText("TRẢ LẠI");
        editkhachnotra.setEnabled(false);
        btnthe.setBackgroundDrawable(getResources().getDrawable(R.drawable.abc_btn_borderless_material));
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
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

    private List<SalesHeader> loadheader() {
        List<SalesHeader> result = new ArrayList<SalesHeader>();

        if (MyGlobal.database != null) {
            Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_SH, null, MyGlobal.COLUMNSH_OrderNo + " = ?", new String[]{madonhang}, null, null, null);
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
                ci.setstatus(2);
                ci.setAmountCustomer(khachdua);
                ci.setAmountReturn(tralai);
                ci.setDiscountPT(cursor.getFloat(18));
                ci.setDiscount(cursor.getFloat(19));
                ci.setEndDate(MyGlobal.getdaycurrent().replace(" ","T")+MyGlobal.gettimecurrent()+":00");
                result.add(ci);

                cursor.moveToNext();
            }
            cursor.close();
        }

        return result;
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


    private void getid() {
        btn10k = (Button) findViewById(R.id.btn10k);
        btn10k.setOnClickListener(this);
        btn20k = (Button) findViewById(R.id.btn20k);
        btn20k.setOnClickListener(this);
        btn50k = (Button) findViewById(R.id.btn50k);
        btn50k.setOnClickListener(this);
        btn100k = (Button) findViewById(R.id.btn100k);
        btn100k.setOnClickListener(this);
        btn200k = (Button) findViewById(R.id.btn200k);
        btn200k.setOnClickListener(this);
        btn500k = (Button) findViewById(R.id.btn500k);
        btn500k.setOnClickListener(this);
        btn1000k = (Button) findViewById(R.id.btn1000k);
        btn1000k.setOnClickListener(this);
        btn1 = (Button) findViewById(R.id.btnthanhtoan1);
        btn1.setOnClickListener(this);
        btn2 = (Button) findViewById(R.id.btnthanhtoan2);
        btn2.setOnClickListener(this);
        btn3 = (Button) findViewById(R.id.btnthanhtoan3);
        btn3.setOnClickListener(this);
        btn4 = (Button) findViewById(R.id.btnthanhtoan4);
        btn4.setOnClickListener(this);
        btn5 = (Button) findViewById(R.id.btnthanhtoan5);
        btn5.setOnClickListener(this);
        btn6 = (Button) findViewById(R.id.btnthanhtoan6);
        btn6.setOnClickListener(this);
        btn7 = (Button) findViewById(R.id.btnthanhtoan7);
        btn7.setOnClickListener(this);
        btn8 = (Button) findViewById(R.id.btnthanhtoan8);
        btn8.setOnClickListener(this);
        btn9 = (Button) findViewById(R.id.btnthanhtoan9);
        btn9.setOnClickListener(this);
        btn0 = (Button) findViewById(R.id.btnthanhtoan0);
        btn0.setOnClickListener(this);
        btn000 = (Button) findViewById(R.id.btnthanhtoan000);
        btn000.setOnClickListener(this);
        btntamtinh = (Button) findViewById(R.id.btnthanhtoantamtinh);
        btntamtinh.setOnClickListener(this);
        btntienmat = (Button) findViewById(R.id.btnthanhtoantienmat);
        btntienmat.setOnClickListener(this);
        btnthe = (Button) findViewById(R.id.btnthanhtoanthe);
        btnthe.setOnClickListener(this);
        btninhoadon = (Button) findViewById(R.id.btnthanhtoanhoadon);
        btninhoadon.setOnClickListener(this);

        btnhuybo = (Button) findViewById(R.id.btnthanhtoanhuybo);
        btnhuybo.setOnClickListener(this);
        btnnhaplai = (Button) findViewById(R.id.btnthanhtoannhaplai);
        btnnhaplai.setOnClickListener(this);
        txttieudethanhtoan = (TextView) findViewById(R.id.txtthanhtoanhoadon);
        txtkhachnotra = (TextView) findViewById(R.id.txtkhachnotra);
        editkhachdua = (EditText) findViewById(R.id.editthanhtoankhachdua);
        editkhachnotra = (EditText) findViewById(R.id.editthanhtoankhachno);
        edittongtien = (EditText) findViewById(R.id.editthanhtoantongtien);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payment, menu);
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



    public void moneyminus(String money) {
        if ((tongtien - Float.valueOf(money)) > 0) {
            txtkhachnotra.setText("KHÁCH NỢ");
            editkhachnotra.setBackgroundDrawable(getResources().getDrawable(R.drawable.customedittext));
            editkhachnotra.setText(MyGlobal.formatfloat(tongtien - Float.valueOf(money)));
        } else {
            txtkhachnotra.setText("TRẢ LẠI");
            editkhachnotra.setBackgroundDrawable(getResources().getDrawable(R.drawable.customedittralai));
            editkhachnotra.setText(MyGlobal.formatfloat(Float.valueOf(money) - tongtien));
        }
    }

    private void sendordertoserver() {


        myAsyncTasksend myRequest = new myAsyncTasksend();

        myRequest.execute();
    }

    private class myAsyncTasksend extends AsyncTask<Void, Void, Void> {

        @Override

        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            if (MyGlobal.isOnline) {
            if (checksendsh)
                Toast.makeText(getApplicationContext().getApplicationContext(), "Đã gửi thành công SH " + madonhang, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext().getApplicationContext(), "Gửi lỗi SH", Toast.LENGTH_SHORT).show();
            if (checkchange)
                Toast.makeText(getApplicationContext().getApplicationContext(), "Đã thay đổi trạng thái " + banchon, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext().getApplicationContext(), "Lỗi thay đổi trạng thái bàn", Toast.LENGTH_SHORT).show();
                ContentValues v = new ContentValues();
                v.put(MyGlobal.COLUMNSH_isSync, 1);
                v.put(MyGlobal.COLUMNSH_Status, 0);
                v.put(MyGlobal.COLUMNSH_AmountCustomer,khachdua);
                v.put(MyGlobal.COLUMNSH_AmountReturn,tralai);
                v.put(MyGlobal.COLUMNSH_EndDate, MyGlobal.getdaycurrent().replace(" ","T")+MyGlobal.gettimecurrent()+":00");
                String strFiltersh = MyGlobal.COLUMNSH_OrderNo + "='" + madonhang + "'";

                MyGlobal.database.update(MyGlobal.TABLE_SH, v, strFiltersh, null);

            } else {
                Toast.makeText(getApplicationContext().getApplicationContext(), "Đã lưu thành công đơn hàng " + madonhang, Toast.LENGTH_SHORT).show();
                ContentValues v = new ContentValues();
                v.put(MyGlobal.COLUMNSH_Status, 0);
                String strFiltersh = MyGlobal.COLUMNSH_OrderNo + "='" + madonhang + "'";

                MyGlobal.database.update(MyGlobal.TABLE_SH, v, strFiltersh, null);
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
            if (MyGlobal.isOnline) {
                //changestatus(SourceCode, ResponsibilityCenter, maban, 0, null);
                changestatustableoffline();
            }
            else changestatustableoffline();

            return null;

        }

    }
    private void changestatustableoffline() {
        String strFilter = MyGlobal.COLUMNTABLE_No + "='" + maban + "'";
        ContentValues args = new ContentValues();
        args.put(MyGlobal.COLUMNTABLE_isSync, 0);
        args.put(MyGlobal.COLUMNTABLE_Status, 0);
        args.put(MyGlobal.COLUMNTABLE_StartDate, "1900-01-01T00:00:00");
        MyGlobal.database.update(MyGlobal.TABLE_TABLE, args, strFilter, null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn10k:
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf("10000")));
                moneyminus("10000");
                break;
            case R.id.btn20k:
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf("20000")));
                moneyminus("20000");
                break;
            case R.id.btn50k:
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf("50000")));
                moneyminus("50000");
                break;
            case R.id.btn100k:
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf("100000")));
                moneyminus("100000");
                break;
            case R.id.btn200k:
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf("200000")));
                moneyminus("200000");
                break;
            case R.id.btn500k:
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf("500000")));
                moneyminus("500000");
                break;
            case R.id.btn1000k:
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf("1000000")));
                moneyminus("1000000");
                break;
            case R.id.btnthanhtoan1:
                tempmoney = tempmoney + "1";
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf(tempmoney)));
                moneyminus(tempmoney);
                break;
            case R.id.btnthanhtoan2:
                tempmoney = tempmoney + "2";
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf(tempmoney)));
                moneyminus(tempmoney);
                break;
            case R.id.btnthanhtoan3:
                tempmoney = tempmoney + "3";
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf(tempmoney)));
                moneyminus(tempmoney);
                break;
            case R.id.btnthanhtoan4:
                tempmoney = tempmoney + "4";
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf(tempmoney)));
                moneyminus(tempmoney);
                break;
            case R.id.btnthanhtoan5:
                tempmoney = tempmoney + "5";
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf(tempmoney)));
                moneyminus(tempmoney);
                break;
            case R.id.btnthanhtoan6:
                tempmoney = tempmoney + "6";
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf(tempmoney)));
                moneyminus(tempmoney);
                break;
            case R.id.btnthanhtoan7:
                tempmoney = tempmoney + "7";
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf(tempmoney)));
                moneyminus(tempmoney);
                break;
            case R.id.btnthanhtoan8:
                tempmoney = tempmoney + "8";
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf(tempmoney)));
                moneyminus(tempmoney);
                break;
            case R.id.btnthanhtoan9:
                tempmoney = tempmoney + "9";
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf(tempmoney)));
                moneyminus(tempmoney);
                break;
            case R.id.btnthanhtoan0:
                tempmoney = tempmoney + "0";
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf(tempmoney)));
                moneyminus(tempmoney);
                break;
            case R.id.btnthanhtoan000:
                tempmoney = tempmoney + "000";
                editkhachdua.setText(MyGlobal.formatfloat(Float.valueOf(tempmoney)));
                moneyminus(tempmoney);
                break;
            case R.id.btnthanhtoantamtinh:
                break;
            case R.id.btnthanhtoantienmat:
                btntienmat.setBackgroundDrawable(getResources().getDrawable(R.drawable.custombuttoncn));
                btnthe.setBackgroundDrawable(getResources().getDrawable(R.drawable.abc_btn_borderless_material));
                break;
            case R.id.btnthanhtoanthe:
                btnthe.setBackgroundDrawable(getResources().getDrawable(R.drawable.custombuttoncn));
                btntienmat.setBackgroundDrawable(getResources().getDrawable(R.drawable.abc_btn_borderless_material));
                break;
            case R.id.btnthanhtoanhoadon://print
                khachdua = Float.parseFloat(editkhachdua.getText().toString().replace(".", "").replace(",", ""));
                if(khachdua>=tongtien)
                tralai = Float.parseFloat(editkhachnotra.getText().toString().replace(".", "").replace(",", ""));
                else tralai = -Float.parseFloat(editkhachnotra.getText().toString().replace(".", "").replace(",", ""));
                MyGlobal.cgdatabasesh();



                sendsh = ConverttoJsonSH();

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Payment.this);

                alertDialog.setTitle("Xác nhận in hóa đơn...");

                alertDialog.setMessage("Bạn có chắc chắn in hóa đơn này?");


                alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {

                        // Write your code here to invoke YES event
                        sendordertoserver();
                        btnhuybo.setEnabled(false);
                        Intent gohome = new Intent(Payment.this, Main.class);
                        startActivity(gohome);
                        finishAffinity();
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

                break;

            case R.id.btnthanhtoanhuybo:
                finish();


                break;
            case R.id.btnthanhtoannhaplai:
                editkhachdua.setText("");
                tempmoney = "";
                break;


        }
    }
}
