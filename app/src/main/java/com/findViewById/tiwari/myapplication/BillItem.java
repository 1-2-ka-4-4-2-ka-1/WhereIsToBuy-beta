package com.findViewById.tiwari.myapplication;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "bill_table")
public class BillItem {


    @PrimaryKey(autoGenerate = true)
    private int bill_id;

    private String mitem_id_label;
    private String mitem_desc;
    private String mitem_unit;
    private double mitem_rate;
    private double mitem_qty;
    private double mitem_amount;


    public BillItem(String id, String desc, String unit, double rate, double qty, double amount) {

        mitem_id_label = id;
        mitem_desc = desc;
        mitem_unit = unit;
        mitem_rate = rate;
        mitem_qty = qty;
        mitem_amount = amount;

    }

    public void setMitem_qty(double mitem_qty) {
        this.mitem_qty = mitem_qty;
    }

    public int getBill_id() {
        return bill_id;
    }

    public String getMitem_id_label() {
        return mitem_id_label;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public String getMitem_desc() {
        return mitem_desc;
    }

    public String getMitem_unit() {
        return mitem_unit;
    }

    public double getMitem_rate() {
        return mitem_rate;
    }

    public double getMitem_qty() {
        return mitem_qty;
    }

    public double getMitem_amount() {
        return mitem_amount;
    }

    public void setMitem_desc(String mitem_desc) {
        this.mitem_desc = mitem_desc;
    }


    public void setMitem_id_label(String mitem_id_label) {
        this.mitem_id_label = mitem_id_label;
    }

    public void setMitem_rate(double mitem_rate) {
        this.mitem_rate = mitem_rate;
    }

    public void setMitem_amount(double mitem_amount) {
        this.mitem_amount = mitem_amount;
    }

    public void setMitem_unit(String mitem_unit) {
        this.mitem_unit = mitem_unit;
    }


}
