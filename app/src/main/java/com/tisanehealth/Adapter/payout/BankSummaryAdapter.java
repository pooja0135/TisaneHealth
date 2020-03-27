package com.tisanehealth.Adapter.payout;

import android.content.Context;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.Model.payout.BankSummaryModel;
import com.tisanehealth.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BankSummaryAdapter extends RecyclerView.Adapter<BankSummaryAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<BankSummaryModel> banksummarylist;
    int selectedPosition = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cardViewBank,cardviewIncome,cardviewDetail;

       TextView tvMemberId;
       TextView tvMembername;
       TextView tvAdminCharges;
       TextView tvTDS;
       TextView tvPayable;
       TextView tvGeneratedDate;
       TextView tvTransferDate;
       TextView tvPancard;

       TextView tvBankName,tvBeneficiaryName,tvAccountNumber,tvIfsc;
       TextView tvMatchingIncome,tvDirectIncome,tvRoiIncome,tvLeaderIncome,tvCrownIncome;
       TextView tvBankDetail,tvIncomeDetail;



        public MyViewHolder(View view) {
            super(view);

            cardViewBank=view.findViewById(R.id.cardviewBank);
            cardviewIncome=view.findViewById(R.id.cardviewIncome);
            cardviewDetail=view.findViewById(R.id.cardviewDetail);


            tvMemberId=view.findViewById(R.id.tvMemberId);
            tvMembername=view.findViewById(R.id.tvMembername);
            tvAdminCharges=view.findViewById(R.id.tvAdminCharges);
            tvTDS=view.findViewById(R.id.tvTDS);
            tvPayable=view.findViewById(R.id.tvPayable);
            tvGeneratedDate=view.findViewById(R.id.tvGenerateDate);
            tvTransferDate=view.findViewById(R.id.tvTransferDate);
            tvPancard=view.findViewById(R.id.tvPancard);


            tvBankName=view.findViewById(R.id.tvBankName);
            tvBeneficiaryName=view.findViewById(R.id.tvBeneficiaryName);
            tvAccountNumber=view.findViewById(R.id.tvAccountNumber);
            tvIfsc=view.findViewById(R.id.tvIfsc);


            tvMatchingIncome=view.findViewById(R.id.tvMatchingIncome);
            tvDirectIncome=view.findViewById(R.id.tvDirectIncome);
            tvRoiIncome=view.findViewById(R.id.tvRoiIncome);
            tvLeaderIncome=view.findViewById(R.id.tvLeaderIncome);
            tvCrownIncome=view.findViewById(R.id.tvCrownIncome);


            tvBankDetail=view.findViewById(R.id.tvBankDetail);
            tvIncomeDetail=view.findViewById(R.id.tvIncomeDetail);


        }
    }


    public BankSummaryAdapter(Context mContext, ArrayList<BankSummaryModel> banksummarylist) {
        this.mContext = mContext;
        this.banksummarylist=banksummarylist;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_bank_summary, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if(selectedPosition ==-1){
            holder.cardViewBank.setVisibility(View.GONE);
            holder.cardviewIncome.setVisibility(View.GONE);

        }


        holder.tvBankDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardViewBank.setVisibility(View.VISIBLE);
                holder.cardviewIncome.setVisibility(View.GONE);

                selectedPosition=position;
                notifyDataSetChanged();
            }
        });
        holder.tvIncomeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardViewBank.setVisibility(View.GONE);
                holder.cardviewIncome.setVisibility(View.VISIBLE);
                selectedPosition=position;
                notifyDataSetChanged();
            }
        });

        holder.cardviewDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition=-1;
                holder.cardViewBank.setVisibility(View.GONE);
                holder.cardviewIncome.setVisibility(View.GONE);

                notifyDataSetChanged();

            }
        });



        holder.tvMemberId.setText(banksummarylist.get(position).getMember_id());
        holder.tvMembername.setText(banksummarylist.get(position).getName());
        holder.tvAdminCharges.setText("\u20B9"+banksummarylist.get(position).getAdmin_charge());
        holder.tvTDS.setText("\u20B9"+banksummarylist.get(position).getTds());
        holder.tvPayable.setText("\u20B9"+banksummarylist.get(position).getPayable_amount());
        holder.tvBankName.setText(banksummarylist.get(position).getBeneficary_bank());
        holder.tvBeneficiaryName.setText(banksummarylist.get(position).getBenificary_name());
        holder.tvAccountNumber.setText(banksummarylist.get(position).getBeneficary_account());
        holder.tvIfsc.setText(banksummarylist.get(position).getBenificary_ifsc());
        holder.tvPancard.setText(banksummarylist.get(position).getPANNo());
        holder.tvMatchingIncome.setText("\u20B9"+banksummarylist.get(position).getMatching_income());
        holder.tvDirectIncome.setText("\u20B9"+banksummarylist.get(position).getDirect_income());
        holder.tvRoiIncome.setText("\u20B9"+banksummarylist.get(position).getTurnoverIncome());
        holder.tvLeaderIncome.setText("\u20B9"+banksummarylist.get(position).getLeaderShipIncome());
        holder.tvCrownIncome.setText("\u20B9"+banksummarylist.get(position).getCrawnShipIncome());


        DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
        DateFormat targetFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = null;
        try {
            date = originalFormat.parse(banksummarylist.get(position).getGenerate_date());
        } catch (Exception e) {

            //Log.v("hhhhhhhhhhhhh",e.toString());
            e.printStackTrace();
        }
        String formattedDate = targetFormat.format(date);
        holder.tvGeneratedDate.setText(formattedDate);

    }


    @Override
    public int getItemCount() {
        return banksummarylist.size();
    }





}

