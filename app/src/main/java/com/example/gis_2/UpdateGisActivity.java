package com.example.gis_2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gis_2.db.DBHelper;
import com.example.gis_2.modle.Gis;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class UpdateGisActivity extends AppCompatActivity {

    EditText feedered, subed, transided, gpsed, serialed, remarked;
    Spinner TransKva, TransClass, TransCondition, TransManufacture;
    String arr1[]={"Trans. Capacity", "Null", "100 KVA", "250 KVA", "400 KVA", "630 KVA", "1000 KVA", "1600 KVA"};
    String arr2[]={"Trans. Condition", "Good", "Bad", "Damage"};
    String arr3[]={"Trans. Class", "Public", "Private"};
    String arr4[]={"Trans. Manufacture", "ديالى", "وزيرية", "تركي", "سعودي", "لبناني", "يوغسلافي", "اماراتي","ايطالي","هندي","ايراني"};
    int id;
    private AutoCompleteTextView autoCompleteTextView;
    private MaterialCardView selectcard;
    TextView tvcource;
    boolean[] selectedcources;
    ArrayList<Integer> courselist = new ArrayList<>();
    String[] courceArray = {"جميع المفردات" , "قاطع دورة مفرد", "قاطع دورة مزدوج", "قاطع دورة ثلاثي", "قاطع دورة رباعي"
            ,"طقم لنك فيوز"," معدات ربط 0.4كف","معدات ربط 11كف","ارضي","مانع صواعق"};

    @SuppressLint("MissingInflatedId")
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

        // initialize all cardselection
        selectcard = findViewById(R.id.selectcard);
        tvcource = findViewById(R.id.tvcource);
        selectedcources = new boolean[courceArray.length];


        selectcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateGisActivity.this);
                builder.setTitle("Select the required information");
                builder.setCancelable(false);

                builder.setMultiChoiceItems(courceArray, selectedcources, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            courselist.add(which);
                        } else {
                            courselist.remove(which);
                        }
                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //create string builder

                        StringBuilder stringbuilder = new StringBuilder();
                        for (int i = 0; i < courselist.size(); i++) {
                            stringbuilder.append(courceArray[courselist.get(i)]);

                            //check condition
                            if (i != courselist.size() - 1) {
                                // if i value not equal to course list value then add comma
                                stringbuilder.append("-");
                            }
                            //setting the selected the information to textview
                            tvcource.setText(stringbuilder.toString());
                        }

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                }).setNeutralButton("clear all", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //clearing all the selected information
                        for (int i = 0; i < selectedcources.length; i++) {
                            selectedcources[i] = false;
                            courselist.clear();
                            tvcource.setText("Select the required information");
                        }


                    }
                });
                builder.show();
            }

        });


        feedered.setText(g.getFeedername());
        subed.setText(g.getSubstationname());
        transided.setText(g.getTransID());
        gpsed.setText(g.getGps());
        serialed.setText(g.getSerialnumber());
        remarked.setText(g.getRemark());
        TransKva.setSelection(getpostion1(g.getCapacity()));
        TransClass.setSelection(getpostion2(g.getClasses()));
        TransCondition.setSelection(getpostion3(g.getCondition()));
        TransManufacture.setSelection(getpostion4(g.getManufacture()));
//        tvcource.setText(g.getInformation());




    }

    private int getpostion4(String manufacture) {
        for (int i=0; i<TransManufacture.getCount(); i++){
            if (manufacture.equals(TransManufacture.getItemAtPosition(i).toString())){
                return i;
            }
        }
        return 0;
    }

    private int getpostion3(String condition) {
        for (int i=0; i<TransCondition.getCount(); i++){
            if (condition.equals(TransCondition.getItemAtPosition(i).toString())){
                return i;
            }
        }
        return 0;
    }

    private int getpostion2(String classes) {
        for (int i=0; i<TransClass.getCount(); i++){
            if (classes.equals(TransClass.getItemAtPosition(i).toString())){
                return i;
            }
        }
        return 0;
    }

    private int getpostion1(String capacity) {
        for (int i=0; i<TransKva.getCount(); i++){
            if (capacity.equals(TransKva.getItemAtPosition(i).toString())){
                return i;
            }
        }
        return 0;
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

        String serialnumber = serialed.getText().toString().trim();
        String Remark = remarked.getText().toString().trim();
        String information = tvcource.getText().toString().trim();

        Gis g = new Gis(id, feedername, substationname,
                transID, gps,transcapacity,transclass,transcondition,transmanufacture, serialnumber, Remark, information);

        DBHelper dbHelper = new DBHelper(this);
        int result = dbHelper.UpdateGis(g);

        if (result > 0){
            Toast.makeText(this, " Updated " + result , Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(this, " Failed "  + result, Toast.LENGTH_SHORT).show();
        }


    }
}