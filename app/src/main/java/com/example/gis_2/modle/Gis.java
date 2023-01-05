package com.example.gis_2.modle;


import java.io.Serializable;

public class Gis implements Serializable {

//    capacity TEXT, condition TEXT, class TEXT , manufacture
    int id;
    String feedername;
    String substationname;
    String transID;
    String gps;
    String capacity,condition,classes,manufacture;
    String serialnumber;
    String Remark;
    String date;
    String information;

    public Gis(int id, String feedername, String substationname, String transID, String gps,
               String capacity, String condition, String classes, String manufacture, String serialnumber, String remark, String information) {
        this.id = id;
        this.feedername = feedername;
        this.substationname = substationname;
        this.transID = transID;
        this.gps = gps;
        this.capacity = capacity;
        this.condition = condition;
        this.classes = classes;
        this.manufacture = manufacture;
        this.serialnumber = serialnumber;
        this.information = information;
        Remark = remark;

    }

    public Gis(int id, String feedername, String substationname, String transID, String gps,
               String capacity, String condition, String classes, String manufacture,
               String serialnumber, String remark, String date, String information) {
        this.id = id;
        this.feedername = feedername;
        this.substationname = substationname;
        this.transID = transID;
        this.gps = gps;
        this.capacity = capacity;
        this.condition = condition;
        this.classes = classes;
        this.manufacture = manufacture;
        this.serialnumber = serialnumber;
        Remark = remark;
        this.date = date;
        this.information= information;
    }

    public Gis(String feedername, String substationname, String transID, String gps, String capacity, String condition,
               String classes, String manufacture, String serialnumber, String remark, String date, String information) {
        this.feedername = feedername;
        this.substationname = substationname;
        this.transID = transID;
        this.gps = gps;
        this.capacity = capacity;
        this.condition = condition;
        this.classes = classes;
        this.manufacture = manufacture;
        this.serialnumber = serialnumber;
        Remark = remark;
        this.date = date;
        this.information= information;
    }


    public String getInformation() {return information;}
    public void setInformation(String information) {this.information = information;}

    public String getCapacity() {return capacity;}
    public void setCapacity(String capacity) {this.capacity = capacity;}

    public String getCondition() {return condition;}
    public void setCondition(String condition) {this.condition = condition;   }

    public String getClasses() {return classes;}
    public void setClasses(String classes) {this.classes = classes;}

    public String getManufacture() {return manufacture;}
    public void setManufacture(String manufacture) {this.manufacture = manufacture;}

    public String getDate() {return date;}
    public void setDate(String date) {this.date = date;}

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getFeedername() {return feedername;}
    public void setFeedername(String feedername) {this.feedername = feedername;}

    public String getSubstationname() {return substationname;}
    public void setSubstationname(String substationname) {this.substationname = substationname;}

    public String getTransID() {return transID;}
    public void setTransID(String transID) {this.transID = transID;}

    public String getGps() {return gps;}
    public void setGps(String gps) {this.gps = gps;}

    public String getSerialnumber() {return serialnumber;}
    public void setSerialnumber(String serialnumber) {this.serialnumber = serialnumber;}

    public String getRemark() {return Remark;}
    public void setRemark(String remark) {Remark = remark;}
}
