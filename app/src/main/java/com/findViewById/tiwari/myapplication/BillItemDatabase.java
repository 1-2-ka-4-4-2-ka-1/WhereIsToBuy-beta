package com.findViewById.tiwari.myapplication;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;


@Database(entities = {BillItem.class},version = 1)
public  abstract class BillItemDatabase extends RoomDatabase {

    private static BillItemDatabase instance;


    public abstract BillItemDao BillDao();


    public static synchronized BillItemDatabase getInstance(Context context){

        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    BillItemDatabase.class,"bill_item_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack).build();

        }

        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            new populateDbAsyncTask(instance).execute();
        }
    };


    private static class populateDbAsyncTask extends AsyncTask<Void , Void , Void> {

        private BillItemDao BillDao;

        private populateDbAsyncTask(BillItemDatabase db){
            BillDao = db.BillDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            BillDao.insert(new BillItem("4","Descrip 561","Kg" ,16,7,7887));
            BillDao.insert(new BillItem("4","Descrip 561","Kg" ,16,7,7887));
            BillDao.insert(new BillItem("4","Descrip 561","Kg" ,16,7,7887));
            BillDao.insert(new BillItem("4","Descrip 561","Kg" ,16,7,7887));
            BillDao.insert(new BillItem("4","Descrip 561","Kg" ,16,7,7887));

            return null;

        }
    }



}
