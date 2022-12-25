package com.example.gis_2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.gis_2.modle.Gis;
import java.util.ArrayList;



public class DBHelper extends SQLiteOpenHelper {
    public DBHelper( Context context) {
        super(context, "gis.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE tbl_gis (id INTEGER PRIMARY KEY AUTOINCREMENT, feedername TEXT, substationname TEXT, " +
                "transID TEXT, gps TEXT, capacity TEXT, condition TEXT, class TEXT , manufacture TEXT , serialnumber TEXT, remark TEXT, date TEXT )");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tbl_gis");

        onCreate(sqLiteDatabase);
    }


    public long addgis( Gis g) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("feedername", g.getFeedername());
        cv.put("substationname", g.getSubstationname());
        cv.put("transID", g.getTransID());
        cv.put("gps", g.getGps());
        cv.put("serialnumber", g.getSerialnumber());
        cv.put("remark", g.getRemark());
        cv.put("date", g.getDate());
        cv.put("capacity", g.getCapacity());
        cv.put("condition", g.getCondition());
        cv.put("manufacture", g.getManufacture());
        cv.put("date", g.getDate());
//        capacity TEXT, condition TEXT, class TEXT , manufacture
        return db.insert("tbl_gis", null, cv);

    }

    public ArrayList<Gis> getAllgiss(){
        ArrayList <Gis> Giss = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(" SELECT * FROM tbl_gis", null);

        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(0);
                String feedername = cursor.getString(1);
                String substationname = cursor.getString(2);
                String transID = cursor.getString(3);
                String gps = cursor.getString(4);
                String capacity = cursor.getString(5);
                String condition = cursor.getString(6);
                String classes = cursor.getString(7);
                String manufacture = cursor.getString(8);
                String serialnumber = cursor.getString(9);
                String remark = cursor.getString(10);
                String date = cursor.getString(11);

//        capacity TEXT, condition TEXT, class TEXT , manufacture
                Giss.add(
                        new Gis(id,
                                feedername, substationname, transID, gps,capacity,condition,classes,manufacture,
                                serialnumber, remark,date));
            }while (cursor.moveToNext());
        }
        return Giss;

    }

    public int UpdateGis(Gis g) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("feedername", g.getFeedername());
        cv.put("substationname", g.getSubstationname());
        cv.put("transID", g.getTransID());
        cv.put("gps", g.getGps());
        cv.put("serialnumber", g.getSerialnumber());
        cv.put("remark", g.getRemark());


        return db.update("tbl_gis", cv, "id=?", new String[]{String.valueOf(g.getId())});


    }

    public int deletegis(int id) {

        SQLiteDatabase db = getWritableDatabase();
        return db.delete("tbl_gis", "id=?", new String[]{String.valueOf(id)});
    }

    public void deletedAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM tbl_gis");
        db.close();
    }


}
