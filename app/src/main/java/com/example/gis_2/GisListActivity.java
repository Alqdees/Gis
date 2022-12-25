package com.example.gis_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.gis_2.adapter.GisAdapter;
import com.example.gis_2.db.DBHelper;
import com.example.gis_2.modle.Gis;

import java.util.ArrayList;

public class GisListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Gis> Giss;
    GisAdapter gisAdapter;
    TextView txtlistview;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gis_list);

        recyclerView = findViewById(R.id.recyclerView);
        txtlistview = findViewById(R.id.txtlistview);


        dbHelper = new DBHelper(this);


    }

    @Override
    protected void onStart() {
        super.onStart();


        Giss = dbHelper.getAllgiss();
        txtlistview.setText("Total Number of Transformers: " + Giss.size() );

        gisAdapter = new GisAdapter(Giss, this);
        recyclerView.setAdapter(gisAdapter);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);

    }
}