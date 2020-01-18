package com.findViewById.tiwari.myapplication;


import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShowItemsRecyclelerViewAdapter extends RecyclerView.Adapter<ShowItemsRecyclelerViewAdapter.itemsViewHolder> implements Filterable {

    private ArrayList<ShopItemModel> mItemsList;
    private ArrayList<ShopItemModel> mItemsListFull;

    private OnShopItemClickListener mListener;

    public interface OnShopItemClickListener {
        void onShopItemClicked(int Position);
    }


    public void setOnShopItemClickListener(OnShopItemClickListener listener){
        mListener = listener;
    }


    public static class itemsViewHolder extends RecyclerView.ViewHolder{


      public TextView mItemTextView;


        public itemsViewHolder(@NonNull final View itemView, final OnShopItemClickListener listener) {
            super(itemView);

            mItemTextView=itemView.findViewById(R.id.id_item_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener !=null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            listener.onShopItemClicked(position);
                        }
                    }
                }
            });

        }

    }


    public ShowItemsRecyclelerViewAdapter(ArrayList<ShopItemModel> itemsList){

        this.mItemsList=itemsList;
        mItemsListFull = new ArrayList<>(mItemsList);


    }

    @NonNull
    @Override
    public itemsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,  int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.shop_item_layout,viewGroup,false);
        itemsViewHolder viewHolder = new itemsViewHolder(v, mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final itemsViewHolder itemsViewHolder, int i) {

        final ShopItemModel current_item  = mItemsList.get(i);
        itemsViewHolder.mItemTextView.setText(current_item.getMitem_desc());


                if(!current_item.getmIsSelected())
                {
                    itemsViewHolder.mItemTextView.setTextColor(Color.DKGRAY);
                    //current_item.setmIsSelected(false);
                }
                else {
                    itemsViewHolder.mItemTextView.setTextColor(Color.rgb(9,151,217));
                    //current_item.setmIsSelected(true);
                }
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }

    @Override
    public  Filter getFilter() {
        return itemsFilter;
    }

    public Filter itemsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ShopItemModel> filteredItems = new ArrayList<>();
            if(constraint == null || constraint.length() == 0)
            {
                filteredItems.addAll(mItemsListFull);
            }
            else {
                String filterItemPattern = constraint.toString().toLowerCase().trim();
                for (ShopItemModel item:mItemsListFull){
                    if(item.getMitem_desc().toLowerCase().contains(filterItemPattern)){
                        filteredItems.add(item);
                    }
                }
            }

            FilterResults filterResult = new FilterResults();
            filterResult.values = filteredItems;

            return filterResult;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mItemsList.clear();
            mItemsList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

}
