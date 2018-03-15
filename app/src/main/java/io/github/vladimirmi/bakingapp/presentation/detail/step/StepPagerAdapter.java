package io.github.vladimirmi.bakingapp.presentation.detail.step;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import io.github.vladimirmi.bakingapp.data.entity.Step;

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
        return StepFragment.newInstance(steps.get(position));
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
        String tab;
        if (position == 0) {
            tab = "Intro";
        } else {
            tab = "Step " + position;
        }
        return tab;
    }

}
