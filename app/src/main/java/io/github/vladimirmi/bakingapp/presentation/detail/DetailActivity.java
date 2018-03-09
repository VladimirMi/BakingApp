package io.github.vladimirmi.bakingapp.presentation.detail;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.vladimirmi.bakingapp.R;
import io.github.vladimirmi.bakingapp.data.Step;
import io.github.vladimirmi.bakingapp.di.Scopes;
import io.github.vladimirmi.bakingapp.presentation.master.MasterActivity;

/**
 * An activity representing a single Recipe entity detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link MasterActivity}.
 */
public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appbar) AppBarLayout appbar;
    @BindView(R.id.detail_container) FrameLayout detailContainer;

    private DetailViewModel viewModel;
    private Step step;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        viewModel = Scopes.getViewModel(this, DetailViewModel.class);

        step = getIntent().getParcelableExtra(StepFragment.STEP);
        setupStepPager();
    }

    private void setupStepPager() {
        if (step == null) return;

        ViewPager pager = (ViewPager) getLayoutInflater()
                .inflate(R.layout.view_detail_steps, detailContainer, false);
        detailContainer.addView(pager);

        StepPagerAdapter adapter = new StepPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        int id = getIntent().getIntExtra(MasterActivity.RECIPE_ID, 0);
        viewModel.getSteps(id)
                .observe(this, steps -> {
                    //noinspection ConstantConditions
                    int position = steps.indexOf(step);
                    adapter.setData(steps);
                    tabLayout.setupWithViewPager(pager);
                    pager.setCurrentItem(position);
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
