package io.github.vladimirmi.bakingapp.presentation.detail;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.github.vladimirmi.bakingapp.data.Step;
import timber.log.Timber;

/**
 * Created by Vladimir Mikhalev 09.03.2018.
 */

public class StepPagerAdapter extends FragmentStatePagerAdapter {

    private List<Step> steps = new ArrayList<>();

    public StepPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setData(List<Step> steps) {
        this.steps = new ArrayList<>(steps);
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        Timber.e("getItem: ");
        Fragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putParcelable(StepFragment.STEP, steps.get(position));
        args.putInt(StepFragment.STEP_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return steps.size();
    }

    @Override
    public int getItemPosition(Object object) {
        //noinspection SuspiciousMethodCalls
        if (steps.contains(object)) {
            return POSITION_UNCHANGED;
        } else {
            return POSITION_NONE;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(position);
    }


    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }
}
