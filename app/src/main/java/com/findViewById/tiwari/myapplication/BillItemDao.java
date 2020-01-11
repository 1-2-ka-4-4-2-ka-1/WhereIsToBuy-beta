package com.findViewById.tiwari.myapplication;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface BillItemDao {

    @Insert
    void insert(BillItem Bill);

    @Update
    void update(BillItem Bill);

    @Delete
    void delete(BillItem Bill);


    @Query("DELETE FROM bill_table ")
    void deleteAllBills();

    @Query("SELECT * FROM bill_table ORDER BY bill_id DESC")
    LiveData<List<BillItem>> getAllBills();


}
