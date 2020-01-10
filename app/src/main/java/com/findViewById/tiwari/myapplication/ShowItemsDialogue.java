package com.findViewById.tiwari.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;

public class ShowItemsDialogue extends AppCompatDialogFragment {

    public RecyclerView mShowItemsRecyclerView;
    public ShowItemsRecyclelerViewAdapter mShowItemsAdapter;
    public RecyclerView.LayoutManager mhowItemsLayoutManager;
    public SearchView mItemsearchView;
    public  ArrayList<ShopItemModel> shop_items=null;

    public static Boolean isVisible = true;

    public BillViewModel billViewModel;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        shop_items= new ArrayList<>();

        //shop_items.add(new ShopItemModel("0","First Item","lo",45,45,45));

        ShopItemsStorageClass storage = new ShopItemsStorageClass(getContext());
        shop_items =storage.loadItems();


        billViewModel = ViewModelProviders.of(this).get(BillViewModel.class);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.show_items_layout,null);


        mShowItemsRecyclerView = view.findViewById(R.id.id_show_items_recyclerView);
        mhowItemsLayoutManager = new LinearLayoutManager(getContext());
        mShowItemsAdapter =new ShowItemsRecyclelerViewAdapter(shop_items);
        mShowItemsRecyclerView.setLayoutManager(mhowItemsLayoutManager);
        mShowItemsRecyclerView.setAdapter(mShowItemsAdapter);
        mShowItemsAdapter.setOnShopItemClickListener(new ShowItemsRecyclelerViewAdapter.OnShopItemClickListener() {
            @Override
            public void onShopItemClicked(int Position) {

                if(!shop_items.get(Position).getmIsSelected()) {

                    shop_items.get(Position).setmIsSelected(true);
                    mShowItemsAdapter.notifyItemChanged(Position);

//                    BillItem billItem = new BillItem(shop_items.get(Position).getMitem_id_label(), shop_items.get(Position).getMitem_desc(),
//                            shop_items.get(Position).getMitem_unit(), shop_items.get(Position).getMitem_rate(), shop_items.get(Position).getMitem_qty(),
//                            shop_items.get(Position).getMitem_amount());
//
//                    billViewModel.insert(billItem);
                }else {

                    shop_items.get(Position).setmIsSelected(false);
                    mShowItemsAdapter.notifyItemChanged(Position);
                }

//                                  if(shop_items.get(Position).getmIsSelected())
//                {
//
//                    for(int i=0;i<MainActivity.bill_item_container_list.size();i++)
//                    {
//                        if(MainActivity.bill_item_container_list.get(i).getMitem_desc().equals(shop_items.get(Position).getmItemDesc()))
//                        {
//                            MainActivity.bill_item_container_list.remove(i);
//                            break;
//                        }
//                    }
//
//                                      shop_items.get(Position).setmIsSelected(false);
//                                      mShowItemsAdapter.notifyDataSetChanged();
//                    MainActivity.mItemContainerRecyclerViewAdapter.notifyDataSetChanged();
//                }
//                else {
//                                      MainActivity.bill_item_container_list.add(new bill_item_container_item("Item1:",shop_items.get(Position).getmItemDesc(),
//                                              shop_items.get(Position).getmItemUnit(),
//                                              Double.parseDouble(shop_items.get(Position).getmItemRate()),
//                                              1,Double.parseDouble(shop_items.get(Position).getmItemRate())));
//                                      shop_items.get(Position).setmIsSelected(true);
//                                      mShowItemsAdapter.notifyDataSetChanged();
//                  MainActivity.mItemContainerRecyclerViewAdapter.notifyDataSetChanged();
//
//                }
            }
        });


        mItemsearchView = view.findViewById(R.id.id_item_search_searchView);
        mItemsearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mItemsearchView.setSelected(true);
        mItemsearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {

                mShowItemsAdapter.getFilter().filter(s);
                return false;
            }
        });

        builder.setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //retrive data'


                        for(int i=0;i<shop_items.size();i++) {

                            if(shop_items.get(i).getmIsSelected()) {
                                BillItem billItem = new BillItem(shop_items.get(i).getMitem_id_label(), shop_items.get(i).getMitem_desc(),
                                        shop_items.get(i).getMitem_unit(), shop_items.get(i).getMitem_rate(), shop_items.get(i).getMitem_qty(),
                                        shop_items.get(i).getMitem_amount());

                                billViewModel.insert(billItem);
                            }

                        }


                        isVisible=false;
                    }
                });
        return  builder.create();
    }



}
