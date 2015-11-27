package androidltg.stanstudios.com.dmsmanager;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.sql.Array;
import java.util.concurrent.CountDownLatch;

public class Login extends AppCompatActivity {
        Button btnlogin,btninputagain;
        EditText edituser,editpass;
    public  static  ImageView imageloading;
    private String user,pass,datalogin;
    private  boolean ok =false;
    private JSONArray Array;
    Animation animRotate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyGlobal.context = getApplicationContext();
        if(MyGlobal.isOnline()) MyGlobal.isOnline =true;
        getid();
      animRotate  = AnimationUtils.loadAnimation(this,R.anim.rotate);
        xuli();
    }

    private void xuli() {


        if(!MyGlobal.isOnline)   imageloading.setImageResource(R.drawable.offline);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CountDownTimer countDownTimer = new CountDownTimer(3000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        imageloading.setVisibility(View.VISIBLE);
                        imageloading.startAnimation(animRotate);
                    }

                    @Override
                    public void onFinish() {
                        imageloading.setVisibility(View.INVISIBLE);
                        if(MyGlobal.isOnline)
                            login();

                        else {
                            Toast.makeText(getApplicationContext(),"Đăng nhập OffLine",Toast.LENGTH_SHORT).show();
                            login();
                        }
                    }
                }.start();
                //Xuli dang nhap



            }
        });
        btninputagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edituser.setText("");
                editpass.setText("");
            }
        });
    }
    private boolean checknhap() {

        if (edituser.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập mã đăng nhập", Toast.LENGTH_SHORT).show();
            edituser.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            return false;
        }
        if (editpass.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Vui lòng nhập mật khẩu", Toast.LENGTH_SHORT).show();
            editpass.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            return false;
        }

        return true;
    }

    private void login() {
        if (checknhap()) {
            user = edituser.getText().toString();
            pass = editpass.getText().toString();
            MyGlobal.cgdatabaselogin();
            checklogin();

        }
    }


    private void checklogin() {
        myAsyncTask myRequest = new myAsyncTask();
        myRequest.execute();
    }

    private class myAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
          if(MyGlobal.isOnline) try {
              parsejsonlogin();
          } catch (JSONException e) {
              e.printStackTrace();
          }
          else checkloginoff(user, pass);

            if(ok) {
                Intent dn  =new Intent(Login.this,Main.class);
                startActivity(dn);
            }
            else Toast.makeText(getApplicationContext(),"Bạn nhập sai thông tin",Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            if(MyGlobal.isOnline)
            getlogin(user, pass);
            return null;
        }
    }

    private void checkloginoff(String user, String pass) {
        if(MyGlobal.isFieldExist(MyGlobal.database,MyGlobal.TABLE_LOGIN,MyGlobal.COLUMNLOGIN_UserID,user) &&MyGlobal.isFieldExist(MyGlobal.database,MyGlobal.TABLE_LOGIN,MyGlobal.COLUMNLOGIN_Password,pass))
        {
            ok = true;
            getinfo();

        }
        else ok =false;

    }

    private void getinfo() {
        if(MyGlobal.database!=null)
        {
            Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_LOGIN, null, MyGlobal.COLUMNLOGIN_UserID + " = ?", new String[]{user}, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if(cursor.getString(2).equals(pass)) {
                    MyGlobal.SalerNo = user;
                    MyGlobal.SalerName = cursor.getString(3);
                    MyGlobal.SourceCode = cursor.getString(4);
                    MyGlobal.ResponsibilityCenter = cursor.getString(5);
                    MyGlobal.LoginTime = cursor.getString(6);
                    MyGlobal.Role = cursor.getString(7);
                    cursor.moveToNext();
                }

                else
                cursor.moveToNext();


            }
            cursor.close();

        }
    }

    private void parsejsonlogin() throws JSONException {
        JSONObject jsonObject = new JSONObject(datalogin);
        Array = jsonObject.getJSONArray("Table");
        int ncustomer = Array.length();

        for (int i = 0; i < ncustomer; i++) {
            ContentValues cv = new ContentValues();
            cv.put(MyGlobal.COLUMNLOGIN_UserID, user);
            cv.put(MyGlobal.COLUMNLOGIN_Password, pass);
            cv.put(MyGlobal.COLUMNLOGIN_Name,Array.getJSONObject(i).getString("Name"));
            cv.put(MyGlobal.COLUMNLOGIN_SourceCode, Array.getJSONObject(i).getString("SourceCode"));
            cv.put(MyGlobal.COLUMNLOGIN_ResponsibilityCenter, Array.getJSONObject(i).getString("ResponsibilityCenter"));
            cv.put(MyGlobal.COLUMNLOGIN_LoginTime, MyGlobal.getdaycurrent()+"T"+MyGlobal.gettimecurrent()+":00");
            cv.put(MyGlobal.COLUMNLOGIN_ROLE, "");
            if(!MyGlobal.isFieldExist(MyGlobal.database,MyGlobal.TABLE_LOGIN,MyGlobal.COLUMNLOGIN_UserID,user)) {
                long check = MyGlobal.database.insert(MyGlobal.TABLE_LOGIN, null, cv);

                if (check != -1) {
                    ok = true;
                    getinfo();
                } else ok = false;
            }
            else  {
                ok=true;
                getinfo();
            }
        }

    }

    private void getlogin(String user, String pass) {
        SoapObject request = new SoapObject(MyGlobal.NAMESPACE, MyGlobal.METHOD_NAME_LOGIN);
        PropertyInfo pi = new PropertyInfo();


        pi.setName("UserID");
        pi.setValue(user);
        pi.setType(String.class);
        request.addProperty(pi);

        pi = new PropertyInfo();
        pi.setName("Password");
        pi.setValue(pass);
        pi.setType(String.class);
        request.addProperty(pi);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);

        HttpTransportSE transportSE = new HttpTransportSE(MyGlobal.URL);

        try {
            transportSE.call(MyGlobal.SOAP_ACTION_LOGIN, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            datalogin = response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


    }

    private void getid() {
        btninputagain = (Button) findViewById(R.id.buttoninputagain);
        btnlogin = (Button)findViewById(R.id.buttonlogin);
        editpass = (EditText) findViewById(R.id.editpassword);
        edituser = (EditText) findViewById(R.id.editusername);
        imageloading = (ImageView)findViewById(R.id.imageloading);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
