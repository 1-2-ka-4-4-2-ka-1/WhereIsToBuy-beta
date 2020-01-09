package com.findViewById.tiwari.myapplication;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class BillItemRipository {

    private BillItemDao billItemDao;
    private LiveData<List<BillItem>> allBills;

    public BillItemRipository(Application application){
        BillItemDatabase database = BillItemDatabase.getInstance(application);
        billItemDao = database.BillDao();
        allBills=billItemDao.getAllBills();
    }



    public void insert(BillItem billItem){

        new  InsertBillAsynicTask(billItemDao).execute(billItem);

    }

    public void update(BillItem billItem){

        new UpdateBillAsynicTask(billItemDao).execute(billItem);

    }

    public void deleteBill(BillItem billItem){

        new DeleteBillAsynicTask(billItemDao).execute(billItem);
    }

    public void deleteAllBills(){

        new DeleteAllBillsAsynicTask(billItemDao).execute();
    }

    
    public LiveData<List<BillItem>>  getAllBills(){

        return allBills;

    }
    


    private static class InsertBillAsynicTask extends AsyncTask<BillItem,Void,Void>{

        private BillItemDao billItemDao;

        private InsertBillAsynicTask(BillItemDao billItemDao){
            this.billItemDao = billItemDao;
        }

        @Override
        protected Void doInBackground(BillItem... bills) {

            billItemDao.insert(bills[0]);
            return null;
        }
    }

    private static class UpdateBillAsynicTask extends AsyncTask<BillItem,Void,Void>{

        private BillItemDao billItemDao;

        private UpdateBillAsynicTask(BillItemDao billItemDao){
            this.billItemDao = billItemDao;
        }

        @Override
        protected Void doInBackground(BillItem... bills) {

            billItemDao.update(bills[0]);
            return null;
        }
    }

    private static class DeleteBillAsynicTask extends AsyncTask<BillItem,Void,Void>{

        private BillItemDao billItemDao;

        private DeleteBillAsynicTask(BillItemDao billItemDao){
            this.billItemDao = billItemDao;
        }

        @Override
        protected Void doInBackground(BillItem... bills) {

            billItemDao.delete(bills[0]);
            return null;
        }
    }

    private static class DeleteAllBillsAsynicTask extends AsyncTask<Void,Void,Void> {

        private BillItemDao billItemDao;

        private DeleteAllBillsAsynicTask(BillItemDao billItemDao){
            this.billItemDao = billItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            billItemDao.deleteAllBills();
            return null;
        }
    }




}
