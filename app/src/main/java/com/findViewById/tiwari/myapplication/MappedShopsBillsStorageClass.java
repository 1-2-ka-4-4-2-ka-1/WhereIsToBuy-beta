package com.findViewById.tiwari.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MappedShopsBillsStorageClass {


    private final String STORAGE = "com.findViewById.tiwari.myapplication.STORAGE";
    private SharedPreferences preferences;
    private Context context ;

    public MappedShopsBillsStorageClass(Context context) {
        this.context = context;
    }

    public void storeMappedItems(List<MapppedShopsBillsModel> arrayList) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        editor.putString("mappedBillItemsArrayList", json);
        editor.apply();
    }

    public void insertMappedItems(MapppedShopsBillsModel mapppedShopsBillsModel){
        List<MapppedShopsBillsModel> m = new ArrayList<>();

        if(loadMappedItems()!=null){
            m.addAll(loadMappedItems());

        }

        m.add(mapppedShopsBillsModel);
        m.get(0).getmBillId();
        storeMappedItems(m);

    }


    public ArrayList<MapppedShopsBillsModel> loadMappedItems() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("mappedBillItemsArrayList", null);
        Type type = new TypeToken<ArrayList<MapppedShopsBillsModel>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void deleteMappedItemByBillId(int billId){
        List<MapppedShopsBillsModel> m = new ArrayList<>();
        if(loadMappedItems()!=null){
            m.addAll(loadMappedItems());

        }


        for(int i=0;i<m.size();i++)
        {
            if(m.get(i).getmBillId() == billId){
                m.remove(i);
                break;
            }
        }
        clearCachedItems();
        storeMappedItems(m);



    }


    public void clearCachedItems() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("mappedBillItemsArrayList", "");
        editor.apply();
        editor.commit();
    }
}
