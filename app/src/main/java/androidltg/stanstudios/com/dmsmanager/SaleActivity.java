package androidltg.stanstudios.com.dmsmanager;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SaleActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnxacnhangiamgia,btn0pthd, btn10pthd, btn20pthd, btn30pthd, btn40pthd, btn50pthd, btn100pthd, btnsokhacpthd, btn0tienhd, btn10tienhd, btn20tienhd, btn30tienhd, btn40tienhd, btn50tienhd, btn100tienhd, btnsokhactienhd, btn0ptmon, btn10ptmon, btn20ptmon, btn30ptmon, btn40ptmon, btn50ptmon, btn100ptmon, btnsokhacptmon, btn0tienmon, btn10tienmon, btn20tienmon, btn30tienmon, btn40tienmon, btn50tienmon, btn100tienmon, btnsokhactienmon;
    private Spinner spinchonmon;
    private String giamtheohoadon="",giamtheomon="",monchon;
    private  ArrayList<ItemOrder> arrItemOrder;
    private TextView txtgiamhoadon,txtgiamtheomon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);
        getid();
        Intent sale = getIntent();
        arrItemOrder = sale.getParcelableArrayListExtra("arritemorder");

        xuli();
    }

    private void xuli() {
        txtgiamhoadon.setText(giamtheohoadon);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < arrItemOrder.size(); i++){
            if(list.toString().indexOf(arrItemOrder.get(i).getquantity()+" <-> "+arrItemOrder.get(i).getName()+" <-> "+arrItemOrder.get(i).getdescription())==-1)
            list.add(arrItemOrder.get(i).getquantity()+" <-> "+arrItemOrder.get(i).getName()+" <-> "+arrItemOrder.get(i).getdescription());

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinchonmon.setAdapter(dataAdapter);

        spinchonmon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                monchon = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getid() {
        txtgiamtheomon = (TextView)findViewById(R.id.txttheomon);
        txtgiamhoadon = (TextView)findViewById(R.id.txtgiamtheohoadon);
        btnxacnhangiamgia = (Button) findViewById(R.id.btnxacnhangiamgia);
        btnxacnhangiamgia.setOnClickListener(this);
        btn0pthd = (Button)findViewById(R.id.btn0pthd);
        btn0pthd.setOnClickListener(this);
        btn10pthd= (Button)findViewById(R.id.btn10pthd);
        btn10pthd.setOnClickListener(this);
        btn20pthd = (Button)findViewById(R.id.btn20pthd);
        btn20pthd.setOnClickListener(this);
        btn30pthd = (Button)findViewById(R.id.btn30pthd);
        btn30pthd.setOnClickListener(this);
        btn40pthd = (Button)findViewById(R.id.btn40pthd);
        btn40pthd.setOnClickListener(this);
        btn50pthd = (Button)findViewById(R.id.btn50pthd);
        btn50pthd.setOnClickListener(this);
        btn100pthd = (Button)findViewById(R.id.btn100pthd);
        btn100pthd.setOnClickListener(this);
        btnsokhacpthd = (Button)findViewById(R.id.btnsokhachd);
        btnsokhacpthd.setOnClickListener(this);

        btn0tienhd = (Button)findViewById(R.id.btn0tienhd);
        btn0tienhd.setOnClickListener(this);
        btn10tienhd= (Button)findViewById(R.id.btn10tienhd);
        btn10tienhd.setOnClickListener(this);
        btn20tienhd = (Button)findViewById(R.id.btn20tienhd);
        btn20tienhd.setOnClickListener(this);
        btn30tienhd = (Button)findViewById(R.id.btn30tienhd);
        btn30tienhd.setOnClickListener(this);
        btn40tienhd = (Button)findViewById(R.id.btn40tienhd);
        btn40tienhd.setOnClickListener(this);
        btn50tienhd = (Button)findViewById(R.id.btn50tienhd);
        btn50tienhd.setOnClickListener(this);
        btn100tienhd = (Button)findViewById(R.id.btn100tienhd);
        btn100tienhd.setOnClickListener(this);
        btnsokhactienhd = (Button)findViewById(R.id.btnsokhactienhd);
        btnsokhactienhd.setOnClickListener(this);

        btn0ptmon = (Button)findViewById(R.id.btn0ptmon);
        btn0ptmon.setOnClickListener(this);
        btn10ptmon= (Button)findViewById(R.id.btn10ptmon);
        btn10ptmon.setOnClickListener(this);
        btn20ptmon = (Button)findViewById(R.id.btn20ptmon);
        btn20ptmon.setOnClickListener(this);
        btn30ptmon = (Button)findViewById(R.id.btn30ptmon);
        btn30ptmon.setOnClickListener(this);
        btn40ptmon = (Button)findViewById(R.id.btn40ptmon);
        btn40ptmon.setOnClickListener(this);
        btn50ptmon = (Button)findViewById(R.id.btn50ptmon);
        btn50ptmon.setOnClickListener(this);
        btn100ptmon = (Button)findViewById(R.id.btn100ptmon);
        btn100ptmon.setOnClickListener(this);
        btnsokhacptmon = (Button)findViewById(R.id.btnsokhacmon);
        btnsokhacptmon.setOnClickListener(this);

        btn0tienmon = (Button)findViewById(R.id.btn0tienmon);
        btn0tienmon.setOnClickListener(this);
        btn10tienmon= (Button)findViewById(R.id.btn10tienmon);
        btn10tienmon.setOnClickListener(this);
        btn20tienmon = (Button)findViewById(R.id.btn20tienmon);
        btn20tienmon.setOnClickListener(this);
        btn30tienmon = (Button)findViewById(R.id.btn30tienmon);
        btn30tienmon.setOnClickListener(this);
        btn40tienmon = (Button)findViewById(R.id.btn40tienmon);
        btn40tienmon.setOnClickListener(this);
        btn50tienmon = (Button)findViewById(R.id.btn50tienmon);
        btn50tienmon.setOnClickListener(this);
        btn100tienmon = (Button)findViewById(R.id.btn100tienmon);
        btn100tienmon.setOnClickListener(this);
        btnsokhactienmon = (Button)findViewById(R.id.btnsokhactienmon);
        btnsokhactienmon.setOnClickListener(this);

        spinchonmon = (Spinner) findViewById(R.id.spinchonmon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sale, menu);
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
            Intent intent = new Intent();
            intent.putExtra("HOADON", "");
            intent.putExtra("THEOMON", "");
            setResult(1, intent);
            super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnxacnhangiamgia:
                Intent intent=new Intent();
                intent.putExtra("HOADON",giamtheohoadon);
                intent.putExtra("THEOMON",giamtheomon);
                setResult(1,intent);
                finish();//finishing activity
                break;
            case R.id.btn0pthd:
                giamtheohoadon="";
                txtgiamhoadon.setText("Không giảm");
                break;
            case R.id.btn10pthd:
                giamtheohoadon="10%";
                txtgiamhoadon.setText(giamtheohoadon);
                break;
            case R.id.btn20pthd:
                giamtheohoadon="20%";
                txtgiamhoadon.setText(giamtheohoadon);
                break;
            case R.id.btn30pthd:
                giamtheohoadon="30%";
                txtgiamhoadon.setText(giamtheohoadon);
                break;
            case R.id.btn40pthd:
                giamtheohoadon="40%";
                txtgiamhoadon.setText(giamtheohoadon);
                break;
            case R.id.btn50pthd:
                giamtheohoadon="50%";
                txtgiamhoadon.setText(giamtheohoadon);
                break;
            case R.id.btn100pthd:
                giamtheohoadon="100%";
                txtgiamhoadon.setText(giamtheohoadon);
                break;
            case R.id.btnsokhachd:
                final Dialog dialogsohd = new Dialog(SaleActivity.this);
                // Include dialog.xml file
                dialogsohd.setContentView(R.layout.dialog_sale);
                // Set dialog title
                dialogsohd.setTitle("Nhập số khác");
                // set values for custom dialog components - text, image and button

                final EditText editTextsohd = (EditText) dialogsohd.findViewById(R.id.editdialoginputsale);
                dialogsohd.show();

                Button declineButtonsohd = (Button) dialogsohd.findViewById(R.id.btndialogclosesale);
                // if decline button is clicked, close the custom dialog
                declineButtonsohd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        giamtheohoadon=editTextsohd.getText().toString()+"%";
                        txtgiamhoadon.setText(giamtheohoadon);
                        dialogsohd.dismiss();
                    }
                });
                break;
            case R.id.btn0tienhd:
                giamtheohoadon="";
                txtgiamhoadon.setText("không giảm");
                break;
            case R.id.btn10tienhd:
                giamtheohoadon="10.000";
                txtgiamhoadon.setText(giamtheohoadon);
                break;
            case R.id.btn20tienhd:
                giamtheohoadon="20.000";
                txtgiamhoadon.setText(giamtheohoadon);
                break;
            case R.id.btn30tienhd:
                giamtheohoadon="30.000";
                txtgiamhoadon.setText(giamtheohoadon);
                break;
            case R.id.btn40tienhd:
                giamtheohoadon="40.000";
                txtgiamhoadon.setText(giamtheohoadon);
                break;
            case R.id.btn50tienhd:
                giamtheohoadon="50.000";
                txtgiamhoadon.setText(giamtheohoadon);
                break;
            case R.id.btn100tienhd:
                giamtheohoadon="100.000";
                txtgiamhoadon.setText(giamtheohoadon);
                break;
            case R.id.btnsokhactienhd:
                final Dialog dialogtienhd = new Dialog(SaleActivity.this);
                // Include dialog.xml file
                dialogtienhd.setContentView(R.layout.dialog_sale);
                // Set dialog title
                dialogtienhd.setTitle("Nhập số tiền khác");
                // set values for custom dialog components - text, image and button

                final EditText editTexttienhd = (EditText) dialogtienhd.findViewById(R.id.editdialoginputsale);
                dialogtienhd.show();

                Button declineButtontienhd = (Button) dialogtienhd.findViewById(R.id.btndialogclosesale);
                // if decline button is clicked, close the custom dialog
                declineButtontienhd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        giamtheohoadon=editTexttienhd.getText().toString();
                        txtgiamhoadon.setText(giamtheohoadon);
                        dialogtienhd.dismiss();
                    }
                });
                break;
            case R.id.btn0ptmon:
                giamtheomon ="";
                txtgiamtheomon.setText("Không giảm");
                break;
            case R.id.btn10ptmon:
                giamtheomon =monchon+" <-> 10%";
                txtgiamtheomon.setText("Giảm "+giamtheomon);
                break;
            case R.id.btn20ptmon:
                giamtheomon =monchon+" <-> 20%";
                txtgiamtheomon.setText("Giảm "+giamtheomon);
                break;
            case R.id.btn30ptmon:
                giamtheomon =monchon+" <-> 30%";
                txtgiamtheomon.setText("Giảm "+giamtheomon);
                break;
            case R.id.btn40ptmon:
                giamtheomon =monchon+" <-> 40%";
                txtgiamtheomon.setText("Giảm "+giamtheomon);
                break;
            case R.id.btn50ptmon:
                giamtheomon =monchon+" <-> 50%";
                txtgiamtheomon.setText("Giảm "+giamtheomon);
                break;
            case R.id.btn100ptmon:
                giamtheomon =monchon+" <-> 100%";
                txtgiamtheomon.setText("Giảm "+giamtheomon);
                break;
            case R.id.btnsokhacmon:
                final Dialog dialogsokhacmon = new Dialog(SaleActivity.this);
                // Include dialog.xml file
                dialogsokhacmon.setContentView(R.layout.dialog_sale);
                // Set dialog title
                dialogsokhacmon.setTitle("Nhập số khác");
                // set values for custom dialog components - text, image and button

                final EditText editTextsokhacmon = (EditText) dialogsokhacmon.findViewById(R.id.editdialoginputsale);
                dialogsokhacmon.show();

                Button declineButtonsokhacmon = (Button) dialogsokhacmon.findViewById(R.id.btndialogclosesale);
                // if decline button is clicked, close the custom dialog
                declineButtonsokhacmon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        giamtheomon =monchon+" <-> "+editTextsokhacmon.getText().toString()+"%";
                        txtgiamtheomon.setText("Giảm " + giamtheomon);
                        dialogsokhacmon.dismiss();
                    }
                });
                break;
            case R.id.btn0tienmon:
                giamtheomon ="";
                txtgiamtheomon.setText("Không giảm");
                break;
            case R.id.btn10tienmon:
                giamtheomon =monchon+" <-> 10.000";
                txtgiamtheomon.setText("Giảm "+giamtheomon);
                break;
            case R.id.btn20tienmon:
                giamtheomon =monchon+" <-> 20.000";
                txtgiamtheomon.setText("Giảm "+giamtheomon);
                break;
            case R.id.btn30tienmon:
                giamtheomon =monchon+" <-> 30.000";
                txtgiamtheomon.setText("Giảm "+giamtheomon);
                break;
            case R.id.btn40tienmon:
                giamtheomon =monchon+" <-> 40.000";
                txtgiamtheomon.setText("Giảm "+giamtheomon);
                break;
            case R.id.btn50tienmon:
                giamtheomon =monchon+" <-> 50.000";
                txtgiamtheomon.setText("Giảm "+giamtheomon);
                break;
            case R.id.btn100tienmon:
                giamtheomon =monchon+" <-> 100.000";
                txtgiamtheomon.setText("Giảm "+giamtheomon);
                break;
            case R.id.btnsokhactienmon:
                final Dialog dialogsotienkhacmon = new Dialog(SaleActivity.this);
                // Include dialog.xml file
                dialogsotienkhacmon.setContentView(R.layout.dialog_sale);
                // Set dialog title
                dialogsotienkhacmon.setTitle("Nhập số tiền khác");
                // set values for custom dialog components - text, image and button

                final EditText editTextsotienmon = (EditText) dialogsotienkhacmon.findViewById(R.id.editdialoginputsale);
                dialogsotienkhacmon.show();

                Button declineButtonsotienmon = (Button) dialogsotienkhacmon.findViewById(R.id.btndialogclosesale);
                // if decline button is clicked, close the custom dialog
                declineButtonsotienmon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        giamtheomon =monchon+" <-> "+editTextsotienmon.getText().toString();
                        txtgiamtheomon.setText("Giảm "+giamtheomon);
                        dialogsotienkhacmon.dismiss();
                    }
                });
                break;

        }
    }
}
