package androidltg.stanstudios.com.dmsmanager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SearchItem extends AppCompatActivity implements View.OnClickListener {
    private EditText editsearchitem;
    private GridView gridviewitem;
    private GridViewItemSearchAdapter gridAdapter;
    private Bitmap bitmap;
    private boolean show =false;
    private LinearLayout linerkeyboard,linersearchmain;
    private Button btnhidekeyboard,btnshowkeyboard,btnQ,btnW,btnE,btnR,btnT,btnY,btnU,btnI,btnO,btnP,btnnhaplai,btnA,btnS,btnD,btnF,btnG,btnH,btnJ,btnK,btnL,btnXoa,btnZ,btnX,btnC,btnV,btnB,btnN,btnM,btnphay,btnspace,btnhuybo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
           getid();
            xuli();
    }

    private void xuli() {
        editsearchitem.setInputType(InputType.TYPE_NULL);
        //Gridview
        gridAdapter = new GridViewItemSearchAdapter(this, R.layout.grid_item_search, loaditem(50));

        gridviewitem.setAdapter(gridAdapter);
        gridviewitem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                final ItemList item = (ItemList) parent.getItemAtPosition(position);
                //Create intent


            }
        });
                //Start details activity
        editsearchitem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            private Timer timer = new Timer();
            private final long DELAY = 500; // in ms
            @Override
            public void afterTextChanged(Editable s) {
                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // TODO: do what you need here (refresh list)
                        // you will probably need to use runOnUiThread(Runnable action) for some specific actions

                    }

                }, DELAY);
                gridAdapter = new GridViewItemSearchAdapter(SearchItem.this, R.layout.grid_item_search, loaditembyname(s.toString()));

                gridviewitem.setAdapter(gridAdapter);
            }
        });
    }
    private ArrayList<ItemList> loaditembyname(String name) {
        {
            ArrayList<ItemList> result = new ArrayList<ItemList>();
            if (MyGlobal.database != null) {
                Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_ITEM, null, MyGlobal.COLUMNITEM_NAME_SEARCH + " LIKE ?", new String[]{"%"+name+"%"}, null, null, null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {

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
                cursor.close();

            }

            return result;
        }
    }
    private ArrayList<ItemList> loaditem(int size) {
        {
            ArrayList<ItemList> result = new ArrayList<ItemList>();
            if (MyGlobal.database != null) {
                Cursor cursor = MyGlobal.database.query(MyGlobal.TABLE_ITEM, null, null, null, null, null, null);
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
    private void getid() {
        linerkeyboard = (LinearLayout) findViewById(R.id.linerkeyboard);
        linersearchmain = (LinearLayout) findViewById(R.id.linersearchmain);
        editsearchitem = (EditText) findViewById(R.id.editsearchitem);
        gridviewitem = (GridView) findViewById(R.id.gridViewsearhitem);
        btnshowkeyboard = (Button) findViewById(R.id.btnshowkeyboard);
        btnshowkeyboard.setOnClickListener(this);
        btnhidekeyboard = (Button) findViewById(R.id.btnhidekeyboard);
        btnhidekeyboard.setOnClickListener(this);
        btnQ = (Button) findViewById(R.id.btnQ);
        btnQ.setOnClickListener(this);
        btnW = (Button) findViewById(R.id.btnW);
        btnW.setOnClickListener(this);
        btnE= (Button) findViewById(R.id.btnE);
        btnE.setOnClickListener(this);
        btnR = (Button) findViewById(R.id.btnR);
        btnR.setOnClickListener(this);
        btnT = (Button) findViewById(R.id.btnT);
        btnT.setOnClickListener(this);
        btnY  = (Button) findViewById(R.id.btnY);
        btnY.setOnClickListener(this);
        btnU = (Button) findViewById(R.id.btnU);
        btnU.setOnClickListener(this);
        btnI = (Button) findViewById(R.id.btnI);
        btnI.setOnClickListener(this);
        btnO = (Button) findViewById(R.id.btnO);
        btnO.setOnClickListener(this);
        btnP = (Button) findViewById(R.id.btnP);
        btnP.setOnClickListener(this);
        btnnhaplai = (Button) findViewById(R.id.btnnhaplai);
        btnnhaplai.setOnClickListener(this);
        btnA = (Button) findViewById(R.id.btnA);
        btnA.setOnClickListener(this);
        btnS = (Button) findViewById(R.id.btnS);
        btnS.setOnClickListener(this);
        btnD = (Button) findViewById(R.id.btnD);
        btnD.setOnClickListener(this);
        btnF = (Button) findViewById(R.id.btnF);
        btnF.setOnClickListener(this);
        btnG = (Button) findViewById(R.id.btnG);
        btnG.setOnClickListener(this);
        btnH = (Button) findViewById(R.id.btnH);
        btnH.setOnClickListener(this);
        btnJ = (Button) findViewById(R.id.btnJ);
        btnJ.setOnClickListener(this);
        btnK = (Button) findViewById(R.id.btnK);
        btnK.setOnClickListener(this);
        btnL = (Button) findViewById(R.id.btnL);
        btnL.setOnClickListener(this);
        btnXoa = (Button) findViewById(R.id.btnXoa);
        btnXoa.setOnClickListener(this);
        btnZ = (Button) findViewById(R.id.btnZ);
        btnZ.setOnClickListener(this);
        btnX = (Button) findViewById(R.id.btnX);
        btnX.setOnClickListener(this);
        btnC = (Button) findViewById(R.id.btnC);
        btnC.setOnClickListener(this);btnV = (Button) findViewById(R.id.btnV);
        btnB = (Button) findViewById(R.id.btnB);
        btnB.setOnClickListener(this);
        btnN = (Button) findViewById(R.id.btnN);
        btnN.setOnClickListener(this);
        btnM = (Button) findViewById(R.id.btnM);
        btnM.setOnClickListener(this);
        btnphay = (Button) findViewById(R.id.btnphay);
        btnphay.setOnClickListener(this);
        btnspace = (Button) findViewById(R.id.btnspace);
        btnspace.setOnClickListener(this);
        btnhuybo = (Button)findViewById(R.id.btnhuybosearch);
        btnhuybo.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_item, menu);
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
    public void onBackPressed() {

        if(show){
            linerkeyboard.setVisibility(View.INVISIBLE);
            btnshowkeyboard.setVisibility(View.VISIBLE);
            show=false;

        }
        else  super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnhidekeyboard:
                linerkeyboard.setVisibility(View.GONE);
                btnshowkeyboard.setVisibility(View.VISIBLE);
                show =false;
                break;
            case R.id.btnshowkeyboard:
                show =true;
                btnshowkeyboard.setVisibility(View.GONE);
                linerkeyboard.setVisibility(View.VISIBLE);
                break;
            case R.id.btnQ:
                editsearchitem.setText(editsearchitem.getText().toString()+"Q");
                break;
            case R.id.btnW:
                editsearchitem.setText(editsearchitem.getText().toString()+"W");
                break;
            case R.id.btnE:
                editsearchitem.setText(editsearchitem.getText().toString()+"E");
                break;
            case R.id.btnR:
                editsearchitem.setText(editsearchitem.getText().toString()+"R");
                break;
            case R.id.btnT:
                editsearchitem.setText(editsearchitem.getText().toString()+"T");
                break;
            case R.id.btnY:
                editsearchitem.setText(editsearchitem.getText().toString()+"Y");
                break;
            case R.id.btnU:
                editsearchitem.setText(editsearchitem.getText().toString()+"U");
                break;
            case R.id.btnI:
                editsearchitem.setText(editsearchitem.getText().toString()+"I");
                break;
            case R.id.btnO:
                editsearchitem.setText(editsearchitem.getText().toString()+"O");
                break;
            case R.id.btnP:
                editsearchitem.setText(editsearchitem.getText().toString()+"P");
                break;
            case R.id.btnnhaplai:
                if (!editsearchitem.getText().toString().isEmpty())
                editsearchitem.setText(editsearchitem.getText().toString().substring(0, editsearchitem.getText().toString().length() - 1));
                break;
            case R.id.btnA:
                editsearchitem.setText(editsearchitem.getText().toString()+"A");
                break;
            case R.id.btnS:
                editsearchitem.setText(editsearchitem.getText().toString()+"S");
                break;
            case R.id.btnD:
                editsearchitem.setText(editsearchitem.getText().toString()+"D");
                break;
            case R.id.btnF:
                editsearchitem.setText(editsearchitem.getText().toString()+"F");
            break;
            case R.id.btnG:
                editsearchitem.setText(editsearchitem.getText().toString()+"G");
                break;
            case R.id.btnH:
                editsearchitem.setText(editsearchitem.getText().toString()+"H");
                break;
            case R.id.btnJ:
                editsearchitem.setText(editsearchitem.getText().toString()+"J");
                break;
            case R.id.btnK:
                editsearchitem.setText(editsearchitem.getText().toString()+"K");
                break;
            case R.id.btnL:
                editsearchitem.setText(editsearchitem.getText().toString()+"L");
                break;
            case R.id.btnXoa:
                editsearchitem.setText("");
            break;
            case R.id.btnZ:
                editsearchitem.setText(editsearchitem.getText().toString()+"Z");
                break;
            case R.id.btnX:
                editsearchitem.setText(editsearchitem.getText().toString()+"X");
                break;
            case R.id.btnC:
                editsearchitem.setText(editsearchitem.getText().toString()+"C");
            break;
            case R.id.btnV:
                editsearchitem.setText(editsearchitem.getText().toString()+"V");
            break;
            case R.id.btnB:
                editsearchitem.setText(editsearchitem.getText().toString()+"B");
            break;
            case R.id.btnN:
                editsearchitem.setText(editsearchitem.getText().toString()+"N");
            break;
            case R.id.btnM:
                editsearchitem.setText(editsearchitem.getText().toString()+"M");
            break;
            case R.id.btnphay:
                editsearchitem.setText(editsearchitem.getText().toString()+",");
            break;
            case R.id.btnspace:
                editsearchitem.setText(editsearchitem.getText().toString()+" ");
            break;
            case R.id.btnhuybosearch:
                finish();
                break;


        }
    }
}
