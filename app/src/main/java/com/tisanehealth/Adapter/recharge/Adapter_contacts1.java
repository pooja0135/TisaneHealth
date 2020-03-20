package com.tisanehealth.Adapter.recharge;

import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.recharge_pay_bill.datacard.DataCardActivity;
import com.tisanehealth.recharge_pay_bill.mobile_recharge.FilterType;
import com.tisanehealth.recharge_pay_bill.mobile_recharge.RechargeActivity;
import com.tisanehealth.recharge_pay_bill.mobile_recharge.RechargePostpaidActivity;
import com.tisanehealth.Model.recharge.ContactModel;
import com.tisanehealth.R;

import java.util.ArrayList;
import java.util.List;


public class Adapter_contacts1 extends RecyclerView.Adapter<Adapter_contacts1.MyViewHolder> {

    Context mContext;
    List<ContactModel> contactlist;
    List<ContactModel> filterArrayList;//duplicate list for filtering

    String [] fiilliste;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        public ImageView imageview;
        TextView tvAlphabet,tvContactName,tvContactNumber;
        public MyViewHolder(View view) {
            super(view);

            linearLayout     =view.findViewById(R.id.linearlayout);
            imageview        =view.findViewById(R.id.imageview);
            tvAlphabet       =view.findViewById(R.id.tvAlphabet);
            tvContactName    =view.findViewById(R.id.tvContactName);
            tvContactNumber  =view.findViewById(R.id.tvContactNumber);


        }
    }
    public Adapter_contacts1(Context mContext, List<ContactModel> contactlist) {
        this.mContext = mContext;
        this.contactlist = contactlist;

        this.filterArrayList = new ArrayList<>();//initiate filter list
        this.filterArrayList.addAll(contactlist);//add all items of array list to filter list

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_contact_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ContactModel contactModel = contactlist.get(position);
        String str = contactModel.getContact_name();
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        String str1 = strArray[0];
        for (String s : strArray) {
            String cap = str1.substring(0, 1).toUpperCase() ;
            holder.tvAlphabet.setText(cap);
            builder.append(cap + " ");
        }




        if (position % 7 == 0) {
            holder.imageview.setColorFilter(ContextCompat.getColor(mContext, R.color.color_list1), android.graphics.PorterDuff.Mode.MULTIPLY);

        } else if(position % 7 == 1) {
            holder.imageview.setColorFilter(ContextCompat.getColor(mContext, R.color.color_list2), android.graphics.PorterDuff.Mode.MULTIPLY);

        } else if(position % 7 == 2) {
            holder.imageview.setColorFilter(ContextCompat.getColor(mContext, R.color.color_list3), android.graphics.PorterDuff.Mode.MULTIPLY);

        } else if(position % 7 == 3) {
            holder.imageview.setColorFilter(ContextCompat.getColor(mContext,  R.color.color_list4), android.graphics.PorterDuff.Mode.MULTIPLY);

        } else if(position % 7 == 4) {
            holder.imageview.setColorFilter(ContextCompat.getColor(mContext,  R.color.color_list5), android.graphics.PorterDuff.Mode.MULTIPLY);

        } else if(position % 7 == 5) {
            holder.imageview.setColorFilter(ContextCompat.getColor(mContext,  R.color.color_list6), android.graphics.PorterDuff.Mode.MULTIPLY);

        } else if(position % 7 == 6) {
            holder.imageview.setColorFilter(ContextCompat.getColor(mContext,  R.color.color_list7), android.graphics.PorterDuff.Mode.MULTIPLY);

        }


        holder.tvContactName.setText(contactModel.getContact_name());
        holder.tvContactNumber.setText(contactModel.getContact_number());


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DashBoardActivity.mobile_type.equals("Prepaid"))
                {
                    Intent i=new Intent(mContext, RechargeActivity.class);
                    i.putExtra("name",contactModel.getContact_name());
                    i.putExtra("number",contactModel.getContact_number());
                    mContext.startActivity(i);
                }

                else if (DashBoardActivity.mobile_type.equals("Postpaid"))
                {
                    Intent i=new Intent(mContext, RechargePostpaidActivity.class);
                    i.putExtra("name",contactModel.getContact_name());
                    i.putExtra("number",contactModel.getContact_number());
                    mContext.startActivity(i);
                }

                else
                {
                    Intent i=new Intent(mContext, DataCardActivity.class);
                    i.putExtra("name",contactModel.getContact_name());
                    i.putExtra("number",contactModel.getContact_number());
                    mContext.startActivity(i);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactlist.size();
    }




    // Filter Class to filter data
    public void filter(FilterType filterType, String charText, boolean isSearchWithPrefix) {
        contactlist.clear();

        if (charText.length() == 0) {
            contactlist.clear();
        } else {
            for (ContactModel model : filterArrayList) {

                switch (filterType) {
                    case NUMBER:
                        if (isSearchWithPrefix) {
                            //if STARTS WITH radio button is selected then it will match the exact NUMBER which match with search query
                            if (model.getContact_number().contains(charText))
                                contactlist.add(model);
                        } else {
                            //if CONTAINS radio button is selected then it will match the NUMBER wherever it contains search query
                            if (model.getContact_number().contains(charText))
                            {
                                contactlist.add(model);
                            }



                        }

                        break;
                }

            }
        }
        notifyDataSetChanged();
    }

}
