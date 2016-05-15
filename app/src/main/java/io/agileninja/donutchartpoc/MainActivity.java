package io.agileninja.donutchartpoc;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.agileninja.donutchartpoc.databinding.ActivityMainBinding;

/**
 * Main activity desmonstrating MPAndroidChart's PieChart.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        PieChart pieChart = (PieChart) findViewById(R.id.pieChart);

        PieChartViewModel viewModel = new PieChartViewModel("Spending Overview");
        viewModel.addSlice(new PieChartSlice("Loans", 80f, "#32746D"));
        viewModel.addSlice(new PieChartSlice("Dining", 23.38f, "#46B1C9"));
        viewModel.addSlice(new PieChartSlice("Groceries", 45.03f, "#DE9151"));
        viewModel.addSlice(new PieChartSlice("Entertainment", 34.43f, "#91C499"));
        viewModel.addSlice(new PieChartSlice("Transportation", 40.32f,
                "#3E8914"));

        assert pieChart != null;
        pieChart.setOnSelectionChangedListener(viewModel);
        pieChart.setViewModel(viewModel);
        binding.setViewModel(viewModel);
    }
}
