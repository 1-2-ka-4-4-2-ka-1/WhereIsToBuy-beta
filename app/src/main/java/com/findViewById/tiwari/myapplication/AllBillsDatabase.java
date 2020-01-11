package com.findViewById.tiwari.myapplication;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {AllBillsItem.class},version = 1)
public abstract class AllBillsDatabase extends RoomDatabase {


    private static AllBillsDatabase instance;


    public abstract AllBillsDao AllBillsItemDao();


    public static synchronized AllBillsDatabase getInstance(Context context){

        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                                            AllBillsDatabase.class,"AllBillsItem_database")
                                            .fallbackToDestructiveMigration()
                                            .addCallback(roomCallBack).build();

        }

        return instance;
    }

   private static Callback roomCallBack = new Callback(){
       @Override
       public void onCreate(@NonNull SupportSQLiteDatabase db) {
           super.onCreate(db);


           new populateDbAsyncTask(instance).execute();
       }
   };


   private static class populateDbAsyncTask extends AsyncTask<Void , Void , Void>{

       private AllBillsDao AllBillsItemDao;

       private populateDbAsyncTask(AllBillsDatabase db){
           AllBillsItemDao = db.AllBillsItemDao();
       }

       @Override
       protected Void doInBackground(Void... voids) {

         //  AllBillsItemDao.insert(new AllBillsItem("Title 1","Descrip 781", "45",78,67,67));

          //AllBillsItemDao.getAllAllBillsItems();
           return null;
       }
   }

}


