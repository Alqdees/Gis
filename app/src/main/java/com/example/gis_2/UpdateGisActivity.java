package com.example.gis_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gis_2.db.DBHelper;
import com.example.gis_2.modle.Gis;

public class UpdateGisActivity extends AppCompatActivity {

    EditText feedered, subed, transided, gpsed, serialed, remarked;
    Spinner TransKva, TransClass, TransCondition, TransManufacture;
    String arr1[]={"Trans. Capacity", "Null", "100 KVA", "250 KVA", "400 KVA", "630 KVA", "1000 KVA", "1600 KVA"};
    String arr2[]={"Trans. Condition", "Good", "Bad", "Damage"};
    String arr3[]={"Trans. Class", "Public", "Private"};
    String arr4[]={"Trans. Manufacture", "ديالى", "وزيرية", "تركي", "سعودي", "لبناني", "يوغسلافي", "اماراتي", "ايراني"};
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_gis);

       Gis g = (Gis) getIntent().getExtras().getSerializable("GIS");

        id = g.getId();
        feedered = findViewById(R.id.feedered);
        subed =  findViewById(R.id.subed);
        transided =  findViewById(R.id.transided);
        gpsed =  findViewById(R.id.gpsed);
        serialed =  findViewById(R.id.serialed);
        remarked =  findViewById(R.id.remarked);

        TransKva = (Spinner) findViewById(R.id.TransKva);
        TransClass = (Spinner) findViewById(R.id.TransClass);
        TransCondition = (Spinner) findViewById(R.id.TransCondition);
        TransManufacture = (Spinner) findViewById(R.id.TransManufacture);

        //making the spinners
        TransKva = (Spinner) findViewById(R.id.TransKva);
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arr1);
        TransKva.setAdapter(adapter1);

        TransCondition = (Spinner) findViewById(R.id.TransCondition);
        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arr2);
        TransCondition.setAdapter(adapter2);

        TransClass = (Spinner) findViewById(R.id.TransClass);
        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arr3);
        TransClass.setAdapter(adapter3);

        TransManufacture = (Spinner) findViewById(R.id.TransManufacture);
        ArrayAdapter adapter4 = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arr4);
        TransManufacture.setAdapter(adapter4);
        //up codes for adding the elements in the spinners


        feedered.setText(g.getFeedername());
        subed.setText(g.getSubstationname());
        transided.setText(g.getTransID());
        gpsed.setText(g.getGps());
        serialed.setText(g.getSerialnumber());
        remarked.setText(g.getRemark());



    }

    public void update(View view) {
        String feedername = feedered.getText().toString().trim();
        String substationname = subed.getText().toString().trim();
        String transID = transided.getText().toString().trim();
        String gps = gpsed.getText().toString().trim();
        String transcapacity = TransKva.getSelectedItem().toString();

        String transclass = TransClass.getSelectedItem().toString();
        String transcondition = TransCondition.getSelectedItem().toString();
        String transmanufacture = TransManufacture.getSelectedItem().toString();
//        String transcapacity = TransKva.getAutofillValue().toString().trim();
//         String transclass = TransClass.getAutofillValue().toString().trim();
//         String transcondition = TransCondition.getAutofillValue().toString().trim();
//        String transmanufacture = TransManufacture.getAutofillValue().toString().trim();
        String serialnumber = serialed.getText().toString().trim();
        String Remark = remarked.getText().toString().trim();

        Gis g = new Gis(id, feedername, substationname,
                transID, gps,transcapacity,transclass,transcondition,transmanufacture, serialnumber, Remark);

        DBHelper dbHelper = new DBHelper(this);
        int result = dbHelper.UpdateGis(g);

        if (result > 0){
            Toast.makeText(this, " Updated " + result , Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(this, " Failed "  + result, Toast.LENGTH_SHORT).show();
        }
        feedered.setText("");
        subed.setText("");
        transided.setText("");
        gpsed.setText("");
        serialed.setText("");
        remarked.setText("");
        TransKva.setAdapter(TransKva.getAdapter());
        TransClass.setAdapter(TransClass.getAdapter());
        TransCondition.setAdapter(TransCondition.getAdapter());
        TransManufacture.setAdapter(TransManufacture.getAdapter());

    }
}