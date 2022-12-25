package com.example.gis_2;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.gis_2.db.DBHelper;
import com.example.gis_2.modle.Gis;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private String stringFile = Environment.getExternalStorageDirectory().getPath() + "/Gis/Gis_Report.csv";


    FusedLocationProviderClient fusedLocationProviderClient;
    private  final  static int REQUEST_CODE=100;


    private DBHelper db;
    private static final int STORAGE_REQUEST_CODE_EXPORT = 1;
    private String[] storagePermissions;
    private Spinner TransKva, TransClass, TransCondition, TransManufacture;
    EditText feedered, subed, transided, gpsed, serialed, remarked;
    Button addbtn, viewbtn, exportbtn, clearbtn, sharebtn;
    String arr1[]={"Trans. Capacity", "Null", "100 KVA", "250 KVA", "400 KVA", "630 KVA", "1000 KVA", "1600 KVA"};
    String arr2[]={"Trans. Condition", "Good", "Bad", "Damage"};
    String arr3[]={"Trans. Class", "Public", "Private"};
    String arr4[]={"Trans. Manufacture", "ديالى", "وزيرية", "تركي", "سعودي", "لبناني", "يوغسلافي", "اماراتي", "ايراني"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);
        storagePermissions = new String[]{WRITE_EXTERNAL_STORAGE
                , READ_EXTERNAL_STORAGE};
        feedered = (EditText) findViewById(R.id.feedered);
        subed = (EditText) findViewById(R.id.subed);
        transided = (EditText) findViewById(R.id.transided);
        gpsed = (EditText) findViewById(R.id.gpsed);
        serialed = (EditText) findViewById(R.id.serialed);
        remarked = (EditText) findViewById(R.id.remarked);

        addbtn = findViewById(R.id.addbtn);
        viewbtn = findViewById(R.id.viewbtn);
        exportbtn = findViewById(R.id.exportbtn);
        clearbtn = findViewById(R.id.clearbtn);
        sharebtn = findViewById(R.id.sharebtn);




        //making the spinners
        TransKva = (Spinner) findViewById(R.id.TransKva);
        ArrayAdapter adapter1 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, arr1);
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


        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());




        clearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Warning!!!!");
                builder.setMessage("Are You Sure Want to Delete All the Data?");
                builder.setIcon(android.R.drawable.ic_menu_delete);
                builder.setCancelable(false); // this comment is to cancel the data clearness AlertDialog

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                            db.deletedAll();
                            Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();

            }
        });

        // export to csv
        exportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkExport();
            }
        });


        // code below to get the current location by click on the gps edittext
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        gpsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });

        }



    public void buttonShareFile(View view){
        File file = new File(stringFile);
        if (!file.exists()){
            Toast.makeText(this, "File doesn't exists", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intentShare = new Intent(Intent.ACTION_SEND);
        intentShare.setType("application/csv");
        intentShare.putExtra(Intent.EXTRA_SUBJECT,"GIS Report");
        intentShare.putExtra(Intent.EXTRA_TEXT,"Here I attached the GIS report.... With Thanks");
        intentShare.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+file));
        startActivity(Intent.createChooser(intentShare, "Share the file ..."));


    }

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location !=null){
                                Geocoder geocoder=new Geocoder(MainActivity.this, Locale.getDefault());
                                List<Address> addresses= null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                    gpsed.setText("Lagitude   :" +addresses.get(0).getLatitude() + " Longitude :"+addresses.get(0).getLongitude());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    });


        }else
        {

            askPermission();

        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
    }


        public void ShowAll (View view){
            startActivity(new Intent(MainActivity.this, GisListActivity.class));
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void Save (View view){
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



            if (feedername.isEmpty() ||
                    substationname.isEmpty() || transID.isEmpty() || gps.isEmpty() ||
                    serialnumber.isEmpty() || Remark.isEmpty()) {
                Toast.makeText(this, "Please Insert the Required Data ", Toast.LENGTH_SHORT).show();
                return;

            } else {

                // get date
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                String f = formatter.format(date);
                //
                DBHelper dbHelper = new DBHelper(MainActivity.this);
//                Gis g = new Gis(feedername, substationname, transID, gps, serialnumber, Remark,f);
                dbHelper.addgis(new Gis(feedername,substationname,transID,gps,transcapacity,transclass,transcondition,transmanufacture ,serialnumber,Remark,f));
                Toast.makeText(this, f, Toast.LENGTH_SHORT).show();
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

        
    // export to csv
    private void checkExport() {

        if (checkStoragePermission()) {
//            requestStoragePermissionExport();
            exportCSV();
        } else {

            requestStoragePermissionExport();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                    false == Environment.isExternalStorageManager()) {
                Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    false == Environment.isExternalStorageManager()) {
                Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2 &&
                    !Environment.isExternalStorageManager()) {
                Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri));
            }
        }
    }
    private void requestStoragePermissionExport() {

        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE_EXPORT);
    }



    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);

        return result;
    }
    private void exportCSV() {

        File folder = new File(Environment.getExternalStorageDirectory() + "/" + "Gis");
        boolean isFolderCreate = false;
        if (!folder.exists()) {
            isFolderCreate = folder.mkdir();
        }
        String csvFileName = "Gis_Report.csv";
        String filePathAndName = folder+ "/" + csvFileName;

        ArrayList<Gis> recordArray = new ArrayList<>();
        recordArray.clear();
        recordArray = db.getAllgiss();;

        try {
            FileWriter fw = new FileWriter(filePathAndName);
            Arrays.toString(fw.getEncoding().getBytes(StandardCharsets.UTF_16));

            // to add the first row in the csv report
            fw.append("Feeder_Name");
            fw.append(",");
            fw.append("Substation_Name");
            fw.append(",");
            fw.append("Trans_ID");
            fw.append(",");
            fw.append("GPS");
            fw.append(",");
            fw.append("Serial_Number");
            fw.append(",");
            fw.append("Remarks");
            fw.append(",");
            fw.append("date");
            fw.append(",");
            fw.append("\n");


            for (int i = 0; i < recordArray.size(); i++) {

                fw.append(recordArray.get(i).getFeedername());
                fw.append(",");
                fw.append(recordArray.get(i).getSubstationname());
                fw.append(",");
                fw.append(recordArray.get(i).getTransID());
                fw.append(",");
                fw.append(recordArray.get(i).getGps());
                fw.append(",");
                fw.append(recordArray.get(i).getSerialnumber());
                fw.append(",");
                fw.append(recordArray.get(i).getRemark());
                fw.append(",");
                fw.append(recordArray.get(i).getDate());
                fw.append(",");
                fw.append(recordArray.get(i).getCapacity());
                fw.append(",");
                fw.append(recordArray.get(i).getCondition());
                fw.append(",");
                fw.append(recordArray.get(i).getClasses());
                fw.append(",");
                fw.append(recordArray.get(i).getManufacture());
                fw.append(",");
                fw.append("\n");
            }
            fw.flush();
            fw.close();
            Toast.makeText(MainActivity.this, "Successfully Exported to" + filePathAndName, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
            Log.d("TAGS",e.getMessage());
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_REQUEST_CODE_EXPORT: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        exportCSV();
                    } catch (Exception e) {
                        Log.d("GRANTED" ,  e.getMessage());
                    }
                } else {
                    Toast.makeText(this, "نحتاج صلاحيات للذاكرة", Toast.LENGTH_LONG).show();
                }
            }
            break;
//            case STORAGE_REQUEST_CODE_IMPORT: {
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    try {
////                        importCSV();
//                    } catch (Exception e) {
//                        Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(this, "نحتاج صلاحيات للذاكرة" + requestCode, Toast.LENGTH_LONG).show();
//                }
//            }
//            break;
//            case CAMERA_REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] ==
//                        PackageManager.PERMISSION_GRANTED) {
//                } else {
//                    Toast.makeText(MainActivity.this, "تحتاج الى صلاحيات الكاميرا", Toast.LENGTH_SHORT).show();
//                }
//                break;
        }
    }
    }
