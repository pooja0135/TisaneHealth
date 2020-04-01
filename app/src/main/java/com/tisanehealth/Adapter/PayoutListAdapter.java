package com.tisanehealth.Adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tisanehealth.Model.PayoutModel;
import com.tisanehealth.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PayoutListAdapter extends RecyclerView.Adapter<PayoutListAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<PayoutModel> payoutlist;
    int selectedPosition = -1;
    AppCompatActivity activity;
    FragmentManager fragmentManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
       CardView cardViewBank,cardviewCommission,cardviewIncome,cardviewBusiness,cardviewDetail,cardviewAcheiver;
       TextView tvBankDetail,tvIncomeDetail,tvBusinessDetail,tvCommissionDetail,tvAcheiverDetail;
       TextView tvTeamCommission;
       TextView tvDsdId,tvDSDname,tvBankAccount,tvBankName,tvIFSC,tvSelfBusinessFresh,tvSelfBusinessRepurchase,tvTeamBusiness,tvDirectTeamBusiness,tvPreviousBusiness;
       TextView tvTeamIncome,tvDirectTeamIncome,tvGoldLevelIncome,tvPlatinumDistributeIncome,tvPlatinumIncome,tvDiamondIncome,tvDiamondDistributeIncome,tvTotalIncome;
       TextView tvPlatinumAcheiver,tvDiamondAcheiver;
       TextView tvCompanyTurnOver,tvCashback;
       TextView tvAdmincharges,tvTDS,tvPayable,tvDate;

        public MyViewHolder(View view) {
            super(view);

          cardViewBank=view.findViewById(R.id.cardviewBank);
          cardviewCommission=view.findViewById(R.id.cardviewCommission);
          cardviewIncome=view.findViewById(R.id.cardviewIncome);
          cardviewBusiness=view.findViewById(R.id.cardviewBusiness);
          cardviewDetail=view.findViewById(R.id.cardviewDetail);
          cardviewAcheiver=view.findViewById(R.id.cardviewAcheiver);


            tvBankDetail       =view.findViewById(R.id.tvBankDetail);
            tvIncomeDetail     =view.findViewById(R.id.tvIncomeDetail);
            tvBusinessDetail   =view.findViewById(R.id.tvBusinessDetail);


            tvDSDname                     =view.findViewById(R.id.tvDsdname);
            tvDsdId                        =view.findViewById(R.id.tvDsdId);
            tvBankAccount                  =view.findViewById(R.id.tvAccountNumber);
            tvBankName                     =view.findViewById(R.id.tvBankName);
            tvIFSC                         =view.findViewById(R.id.tvIfsc);
            tvAcheiverDetail               =view.findViewById(R.id.tvAcheiverDetail);


            tvSelfBusinessFresh             =view.findViewById(R.id.tvSelfBusinessFresh);
            tvTeamBusiness                  =view.findViewById(R.id.tvTeamBusiness);
            tvDirectTeamBusiness            =view.findViewById(R.id.tvDirectTeamBusiness);
            tvSelfBusinessRepurchase        =view.findViewById(R.id.tvSelfBusinessRepurchase);
            tvPreviousBusiness              =view.findViewById(R.id.tvPreviousBusiness);



            tvTeamCommission                =view.findViewById(R.id.tvTeamCommission);



            tvTeamIncome                    =view.findViewById(R.id.tvTeamIncome);
            tvDirectTeamIncome              =view.findViewById(R.id.tvDirectTeamIncome);
            tvTotalIncome                   =view.findViewById(R.id.tvTotalIncome);
            tvGoldLevelIncome               =view.findViewById(R.id.tvGoldLevelIncome);
            tvPlatinumDistributeIncome      =view.findViewById(R.id.tvPlatinumDistributeIncome);
            tvPlatinumIncome                =view.findViewById(R.id.tvPlatinumIncome);
            tvDiamondIncome                 =view.findViewById(R.id.tvDiamondIncome);
            tvDiamondDistributeIncome       =view.findViewById(R.id.tvDiamondDistributeIncome);


            tvPlatinumAcheiver              =view.findViewById(R.id.tvPlatinumAcheiver);
            tvDiamondAcheiver               =view.findViewById(R.id.tvDiamondAcheiver);


            tvCompanyTurnOver               =view.findViewById(R.id.tvCompanyTurnover);
            tvCashback                      =view.findViewById(R.id.tvCashback);


            tvAdmincharges                  =view.findViewById(R.id.tvAdminCharges);
            tvTDS                           =view.findViewById(R.id.tvTDS);
            tvPayable                       =view.findViewById(R.id.tvPayable);
            tvDate                          =view.findViewById(R.id.tvDate);


        }
    }


    public PayoutListAdapter(Context mContext,   ArrayList<PayoutModel>payoutlist) {
        this.mContext = mContext;
        this.payoutlist=payoutlist;
        activity = (AppCompatActivity)mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_payout_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if(selectedPosition ==-1){
            holder.cardViewBank.setVisibility(View.GONE);
            holder.cardviewIncome.setVisibility(View.GONE);
            holder.cardviewBusiness.setVisibility(View.GONE);
            holder.cardviewCommission.setVisibility(View.GONE);
            holder.cardviewAcheiver.setVisibility(View.GONE);
        }


        holder.tvBankDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardViewBank.setVisibility(View.VISIBLE);
                holder.cardviewIncome.setVisibility(View.GONE);
                holder.cardviewBusiness.setVisibility(View.GONE);
                holder.cardviewCommission.setVisibility(View.GONE);
                holder.cardviewAcheiver.setVisibility(View.GONE);
                selectedPosition=position;
                notifyDataSetChanged();
            }
        });
        holder.tvIncomeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardViewBank.setVisibility(View.GONE);
                holder.cardviewIncome.setVisibility(View.VISIBLE);
                holder.cardviewBusiness.setVisibility(View.GONE);
                holder.cardviewCommission.setVisibility(View.GONE);
                holder.cardviewAcheiver.setVisibility(View.GONE);
                selectedPosition=position;
                notifyDataSetChanged();
            }
        });
        holder.tvBusinessDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardViewBank.setVisibility(View.GONE);
                holder.cardviewIncome.setVisibility(View.GONE);
                holder.cardviewBusiness.setVisibility(View.VISIBLE);
                holder.cardviewCommission.setVisibility(View.GONE);
                holder.cardviewAcheiver.setVisibility(View.GONE);
                selectedPosition=position;
                notifyDataSetChanged();
            }
        });
      /*  holder.tvCommissionDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardViewBank.setVisibility(View.GONE);
                holder.cardviewIncome.setVisibility(View.GONE);
                holder.cardviewBusiness.setVisibility(View.GONE);
                holder.cardviewCommission.setVisibility(View.VISIBLE);
                holder.cardviewAcheiver.setVisibility(View.GONE);
                selectedPosition=position;
                notifyDataSetChanged();
            }
        });*/


        holder.tvAcheiverDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.cardViewBank.setVisibility(View.GONE);
                holder.cardviewIncome.setVisibility(View.GONE);
                holder.cardviewBusiness.setVisibility(View.GONE);
                holder.cardviewCommission.setVisibility(View.GONE);
                holder.cardviewAcheiver.setVisibility(View.VISIBLE);
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
                holder.cardviewBusiness.setVisibility(View.GONE);
                holder.cardviewCommission.setVisibility(View.GONE);
                holder.cardviewAcheiver.setVisibility(View.GONE);
                notifyDataSetChanged();

            }
        });




        holder.tvDSDname.setText(payoutlist.get(position).getDsd_name());
        holder.tvDsdId.setText(payoutlist.get(position).getDsd_id());
        holder.tvTDS.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getTds()))));
        holder.tvAdmincharges.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getAdmin_charges()))));
        holder.tvPayable.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getPayable()))));
        holder.tvDate.setText(parseDateToddMMyyyy(payoutlist.get(position).getDate()));

        holder.tvBankAccount.setText(payoutlist.get(position).getBank_account());
        holder.tvBankName.setText(payoutlist.get(position).getBank_aname());
        holder.tvIFSC.setText(payoutlist.get(position).getIfsc());



        holder.tvTotalIncome.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getTotal_income()))));
        holder.tvTeamIncome.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getTeam_income()))));
        holder.tvDirectTeamIncome.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getDirect_team_income()))));
        holder.tvGoldLevelIncome.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getGold_level_income()))));
        holder.tvPlatinumDistributeIncome.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getPlatinum_distribute_income()))));
        holder.tvPlatinumIncome.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getPlatinum_income()))));
        holder.tvDiamondIncome.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getDiamond_income()))));
        holder.tvDiamondDistributeIncome.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getDiamond_distribute_income()))));



        holder.tvTeamCommission.setText((Double.parseDouble(payoutlist.get(position).getTeam_commission())*100)+"%");



        holder.tvSelfBusinessFresh.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getSelf_business()))));
        holder.tvTeamBusiness.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getTeam_business()))));
        holder.tvDirectTeamBusiness.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getDirect_team_business()))));
        holder.tvSelfBusinessRepurchase.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getSelf_business_repurchase()))));
        holder.tvPreviousBusiness.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getPrevious_business()))));



        holder.tvPlatinumAcheiver.setText(payoutlist.get(position).getPlatinum_acheiver());
        holder.tvDiamondAcheiver.setText(payoutlist.get(position).getDiamond_acheiver());


        holder.tvCompanyTurnOver.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getCompany_turnover()))));
        holder.tvCashback.setText("\u20B9 " + String.valueOf(roundTwoDecimals(Double.parseDouble(payoutlist.get(position).getCashback()))));











    }


    @Override
    public int getItemCount() {
        return payoutlist.size();
    }


    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "MM/dd/yyyy hh:mm:ss a";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern,Locale.ENGLISH);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (Exception e) {


            e.printStackTrace();
        }
        return str;
    }

    public void loadFragment(Fragment fragment) {

         FragmentTransaction transaction = ((AppCompatActivity)activity).getSupportFragmentManager().beginTransaction();
//        transaction.setCustomAnimations(
//                R.anim.card_flip_right_in, R.anim.card_flip_right_out,
//                R.anim.card_flip_left_in, R.anim.card_flip_left_out);
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }


}

