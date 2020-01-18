package com.findViewById.tiwari.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AllBillsItemsAdapter extends ListAdapter<AllBillsItem,AllBillsItemsAdapter.AllBillsItemHolder> {



    private cOnItemClickListener listener;

    public AllBillsItemsAdapter(){
        super(DIFF_CALLBACK);
    }

    private static  final DiffUtil.ItemCallback<AllBillsItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<AllBillsItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull AllBillsItem AllBillsItem, @NonNull AllBillsItem t1) {
            return AllBillsItem.getMitem_id_label().equals(t1.getMitem_id_label());
        }

        @Override
        public boolean areContentsTheSame(@NonNull AllBillsItem AllBillsItem, @NonNull AllBillsItem t1) {

            return AllBillsItem.getMitem_desc().equals(t1.getMitem_desc())
                     &&  AllBillsItem.getMitem_id_label().equals(t1.getMitem_id_label())
                    &&  AllBillsItem.getMitem_rate()==t1.getMitem_rate()
                    &&  AllBillsItem.getMitem_unit().equals(t1.getMitem_unit())
                    && AllBillsItem.getMitem_qty() == t1.getMitem_qty();

        }
    };

    public AllBillsItemsAdapter(@NonNull DiffUtil.ItemCallback<AllBillsItem> diffCallback) {
        super(diffCallback);
    }


    @NonNull
    @Override
    public AllBillsItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_item_layout, viewGroup, false);
        return new AllBillsItemHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull AllBillsItemHolder AllBillsItemHolder, int i) {

        AllBillsItem current_AllBillsItem = getItem(i);
        AllBillsItemHolder.mitem_desc.setText(current_AllBillsItem.getMitem_desc());
        AllBillsItemHolder.mitem_id_label.setText(String.valueOf(current_AllBillsItem.getMitem_id_label()));
        AllBillsItemHolder.mitem_amount.setText(Double.toString(current_AllBillsItem.getMitem_amount()));
        AllBillsItemHolder.mitem_rate.setText(Double.toString(current_AllBillsItem.getMitem_rate()));
        AllBillsItemHolder.mitem_qty.setText(Double.toString(current_AllBillsItem.getMitem_qty()));
        AllBillsItemHolder.mitem_unit.setText(current_AllBillsItem.getMitem_unit());

    }


    public AllBillsItem getAllBillsItemAt(int position) {
        return getItem(position);
    }


    class AllBillsItemHolder extends RecyclerView.ViewHolder {

        public TextView mitem_id_label;
        public TextView mitem_desc;
        public TextView  mitem_unit;
        public TextView  mitem_rate;
        public TextView  mitem_qty;
        public TextView  mitem_amount;


        public AllBillsItemHolder(@NonNull View itemView) {
            super(itemView);

            mitem_id_label= itemView.findViewById(R.id.tv_item_id);
            mitem_desc=itemView.findViewById(R.id.tv_item_desc);
            mitem_unit=itemView.findViewById(R.id.tv_item_unit);
            mitem_rate=itemView.findViewById(R.id.tv_item_rate);
            mitem_qty=itemView.findViewById(R.id.tv_item_qty);
            mitem_amount=itemView.findViewById(R.id.tv_item_amount);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.cOnItemClick(getItem(position));
                    }
                }
            });

        }
    }


    public interface cOnItemClickListener {

        void cOnItemClick(AllBillsItem AllBillsItem);
    }

    public void setcOnItemClickListener(cOnItemClickListener listener) {
        this.listener = listener;

    }



}
