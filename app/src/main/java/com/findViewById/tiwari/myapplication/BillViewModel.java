package com.findViewById.tiwari.myapplication;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class BillViewModel extends AndroidViewModel {


    private BillItemRipository ripository;
    private LiveData<List<BillItem>> allBills;

    public BillViewModel(@NonNull Application application) {
        super(application);

        ripository = new BillItemRipository(application);
        allBills = ripository.getAllBills();
    }


    public void insert(BillItem Bill){
        ripository.insert(Bill);
    }

    public void update(BillItem Bill){
        ripository.update(Bill);
    }

    public void delete(BillItem Bill){
        ripository.deleteBill(Bill);
    }

    public void deleteAllBills(){
        ripository.deleteAllBills();
    }

    public LiveData<List<BillItem>> getAllBills() {
        return allBills;
    }
}
