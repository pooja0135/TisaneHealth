package com.tisanehealth.Adapter.recharge;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.tisanehealth.recharge_pay_bill.mobile_recharge.RechargePlanFragment;
import java.util.ArrayList;
import java.util.List;


public class TabsAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 5;
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TabsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @Override
    public int getCount() {
        return NUM_ITEMS;
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return RechargePlanFragment.newInstance(0, "OTR");
            case 1:
                return RechargePlanFragment.newInstance(1, "3G");
            case 2:
                return RechargePlanFragment.newInstance(2, "RMG");
            case 3:
                return RechargePlanFragment.newInstance(3, "TUP");
            case 4:
                return RechargePlanFragment.newInstance(4, "SMS");

            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0)
        {
            mFragmentTitleList.add("Recharge");
        }
      else  if (position==1)
        {
            mFragmentTitleList.add("Data Plan");
        }
        else  if (position==2)
        {
            mFragmentTitleList.add("Roaming");
        }
        else  if (position==3)
        {
            mFragmentTitleList.add("Topup");
        }
        else  if (position==4)
        {
            mFragmentTitleList.add("SMS");
        }

        return mFragmentTitleList.get(position);
    }

}
