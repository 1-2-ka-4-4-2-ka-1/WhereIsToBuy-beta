package com.findViewById.tiwari.myapplication;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BillItemsAdapter extends ListAdapter<BillItem,BillItemsAdapter.BillItemHolder> {



    private cOnItemClickListener listener;

    public BillItemsAdapter(){
        super(DIFF_CALLBACK);
    }

    private static  final DiffUtil.ItemCallback<BillItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<BillItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull BillItem BillItem, @NonNull BillItem t1) {
            return BillItem.getBill_id() == t1.getBill_id();
        }

        @Override
        public boolean areContentsTheSame(@NonNull BillItem BillItem, @NonNull BillItem t1) {

            return BillItem.getMitem_desc().equals(t1.getMitem_desc())
                     &&  BillItem.getBill_id().equals(t1.getBill_id())
                    &&  BillItem.getMitem_rate()==t1.getMitem_rate()
                    &&  BillItem.getMitem_unit().equals(t1.getMitem_unit())
                    && BillItem.getMitem_qty() == t1.getMitem_qty();
        }
    };




    @NonNull
    @Override
    public BillItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bill_item_layout, viewGroup, false);
        return new BillItemHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull BillItemHolder BillItemHolder, int i) {

        BillItem current_BillItem = getItem(i);
        BillItemHolder.mitem_desc.setText(current_BillItem.getMitem_desc());
        BillItemHolder.mitem_id_label.setText(String.valueOf(current_BillItem.getMitem_id_label()));
        BillItemHolder.mitem_amount.setText(Double.toString(current_BillItem.getMitem_amount()));
        BillItemHolder.mitem_rate.setText(Double.toString(current_BillItem.getMitem_rate()));
        BillItemHolder.mitem_qty.setText(Double.toString(current_BillItem.getMitem_qty()));
        BillItemHolder.mitem_unit.setText(current_BillItem.getMitem_unit());

    }


    public BillItem getBillItemAt(int position) {
        return getItem(position);
    }


    class BillItemHolder extends RecyclerView.ViewHolder {

        public TextView mitem_id_label;
        public TextView mitem_desc;
        public TextView  mitem_unit;
        public TextView  mitem_rate;
        public TextView  mitem_qty;
        public TextView  mitem_amount;


        public BillItemHolder(@NonNull View itemView) {
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

        void cOnItemClick(BillItem BillItem);
    }

    public void setcOnItemClickListener(cOnItemClickListener listener) {
        this.listener = listener;

    }



}
