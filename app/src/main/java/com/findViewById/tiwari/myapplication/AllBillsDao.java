package com.findViewById.tiwari.myapplication;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface AllBillsDao {

    @Insert
    void insert(AllBillsItem AllBillsItem);

    @Update
    void update(AllBillsItem AllBillsItem);

    @Delete
    void delete(AllBillsItem AllBillsItem);


    @Query("DELETE FROM all_bills_table ")
    void deleteAllAllBillsItems();

    @Query("SELECT * FROM all_bills_table ORDER BY bill_id DESC")
    LiveData<List<AllBillsItem>> getAllAllBillsItems();

    @Query("SELECT * FROM all_bills_table WHERE mitem_id_label =:s")
    LiveData<List<AllBillsItem>> getBillsBy(String s);

    @Query("DELETE  FROM all_bills_table WHERE mitem_id_label =:s")
    void deleteAllBillsByItems(String s);


}
