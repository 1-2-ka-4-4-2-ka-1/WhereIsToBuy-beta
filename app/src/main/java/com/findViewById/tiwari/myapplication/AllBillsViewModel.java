package com.findViewById.tiwari.myapplication;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class AllBillsViewModel extends AndroidViewModel {


    private AllBillsRipository ripository;
    private LiveData<List<AllBillsItem>> allBills;

    public AllBillsViewModel(@NonNull Application application) {
        super(application);

        ripository = new AllBillsRipository(application);
        allBills = ripository.getAllBills();
    }


    public void insert(AllBillsItem Bill){
        ripository.insert(Bill);
    }

    public void update(AllBillsItem Bill){
        ripository.update(Bill);
    }

    public void delete(AllBillsItem Bill){
        ripository.deleteBill(Bill);
    }

    public void deleteAllBills(){
        ripository.deleteAllBills();
    }

    public LiveData<List<AllBillsItem>> getAllBills() {
        return allBills;
    }
    
    public LiveData<List<AllBillsItem>> getBillsBy(String i){ return  ripository.getAllBillsBy(i);}

    public void deleteAllBillsBy(String i){
        ripository.deleteAllBillsBy(i);
    }
}
