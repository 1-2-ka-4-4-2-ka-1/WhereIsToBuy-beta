package com.findViewById.tiwari.myapplication;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class AllBillsRipository{

    private AllBillsDao AllBillsDao;
    private LiveData<List<AllBillsItem>> allBills;

    public AllBillsRipository(Application application){
        AllBillsDatabase database = AllBillsDatabase.getInstance(application);
        AllBillsDao = database.AllBillsItemDao();
        allBills=AllBillsDao.getAllAllBillsItems();
    }



    public void insert(AllBillsItem AllBillsItem){

        new  InsertBillAsynicTask(AllBillsDao).execute(AllBillsItem);

    }

    public void update(AllBillsItem AllBillsItem){

        new UpdateBillAsynicTask(AllBillsDao).execute(AllBillsItem);

    }

    public void deleteBill(AllBillsItem AllBillsItem){

        new DeleteBillAsynicTask(AllBillsDao).execute(AllBillsItem);
    }

    public void deleteAllBills(){

        new DeleteAllBillsAsynicTask(AllBillsDao).execute();
    }

    
    public LiveData<List<AllBillsItem>>  getAllBills(){

        return allBills;

    }

    public LiveData<List<AllBillsItem>>  getAllBillsBy(String i){

        //Log.i("=----=-==-=---=",i);
        return AllBillsDao.getBillsBy(i);


    }


    


    private static class InsertBillAsynicTask extends AsyncTask<AllBillsItem,Void,Void>{

        private AllBillsDao AllBillsDao;

        private InsertBillAsynicTask(AllBillsDao AllBillsDao){
            this.AllBillsDao = AllBillsDao;
        }

        @Override
        protected Void doInBackground(AllBillsItem... bills) {

            AllBillsDao.insert(bills[0]);
            return null;
        }
    }

    private static class UpdateBillAsynicTask extends AsyncTask<AllBillsItem,Void,Void>{

        private AllBillsDao AllBillsDao;

        private UpdateBillAsynicTask(AllBillsDao AllBillsDao){
            this.AllBillsDao = AllBillsDao;
        }

        @Override
        protected Void doInBackground(AllBillsItem... bills) {

            AllBillsDao.update(bills[0]);
            return null;
        }
    }

    private static class DeleteBillAsynicTask extends AsyncTask<AllBillsItem,Void,Void>{

        private AllBillsDao AllBillsDao;

        private DeleteBillAsynicTask(AllBillsDao AllBillsDao){
            this.AllBillsDao = AllBillsDao;
        }

        @Override
        protected Void doInBackground(AllBillsItem... bills) {

            AllBillsDao.delete(bills[0]);
            return null;
        }
    }

    private static class DeleteAllBillsAsynicTask extends AsyncTask<Void,Void,Void> {

        private AllBillsDao AllBillsDao;

        private DeleteAllBillsAsynicTask(AllBillsDao AllBillsDao){
            this.AllBillsDao = AllBillsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            AllBillsDao.deleteAllAllBillsItems();
            return null;
        }
    }




}
