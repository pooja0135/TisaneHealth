package com.tisanehealth.Adapter.recharge;

import android.app.Dialog;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tisanehealth.Activity.DashBoardActivity;
import com.tisanehealth.recharge_pay_bill.datacard.DataCardActivity;
import com.tisanehealth.recharge_pay_bill.mobile_recharge.RechargePostpaidActivity;
import com.tisanehealth.recharge_pay_bill.mobile_recharge.RechargeActivity;
import com.tisanehealth.Model.recharge.RechargeListModel;
import com.tisanehealth.R;

import java.util.List;


public class Adapter_operator extends RecyclerView.Adapter<Adapter_operator.MyViewHolder> {

    Context mContext;
    List<RechargeListModel> operatorlist;
    Dialog dialog;

    int status;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        public ImageView imageview;
        TextView textview;
        public MyViewHolder(View view) {
            super(view);

            imageview=view.findViewById(R.id.ivOperator);
            textview=view.findViewById(R.id.tvOperatorName);
            linearLayout=view.findViewById(R.id.linearlayout);


        }
    }
    public Adapter_operator(Context mContext, List<RechargeListModel> operatorlist, Dialog dialog,int status) {
        this.mContext = mContext;
        this.operatorlist = operatorlist;
        this.dialog = dialog;
        this.status = status;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_operator_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

       final RechargeListModel operatorModel = operatorlist.get(position);
       holder.textview.setText(operatorModel.getService());


       holder.linearLayout.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (DashBoardActivity.mobile_type.equals("Prepaid"))
               {
                   RechargeActivity.tvOperatorName.setText(operatorModel.getService());
                   RechargeActivity.operator_id=operatorModel.getOperator_Code();
                   dialog.dismiss();
               }

               else if (DashBoardActivity.mobile_type.equals("Postpaid"))
               {
                   RechargePostpaidActivity.tvOperatorName.setText(operatorModel.getService());
                   RechargePostpaidActivity.operator_id=operatorModel.getOperator_Code();
                   dialog.dismiss();
               }

               else
               {
                   DataCardActivity.tvOperatorName.setText(operatorModel.getService());
                   DataCardActivity.operator_id=operatorModel.getOperator_Code();
                   dialog.dismiss();
               }



           }
       });
    }

    @Override
    public int getItemCount() {
        return operatorlist.size();



    }
}
