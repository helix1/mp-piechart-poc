package io.agileninja.donutchartpoc;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.agileninja.donutchartpoc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements PieChart.OnSelectionChangedListener {
    private ActivityMainBinding mBinding;
    private PieChart mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mPieChart = (PieChart) findViewById(R.id.pieChart);

        List<PieChartData> pieChartData = new ArrayList<>();
        pieChartData.add(new PieChartData("Groceries", 45.03f, "#DE9151"));
        pieChartData.add(new PieChartData("Transportation", 40.32f, "#3E8914"));
        pieChartData.add(new PieChartData("Entertainment", 34.43f, "#91C499"));
        pieChartData.add(new PieChartData("Dining", 23.38f, "#46B1C9"));
        pieChartData.add(new PieChartData("Loans", 120f, "#32746D"));

        PieChartViewModel viewModel = new PieChartViewModel("Spending Overview", pieChartData);

        mPieChart.setViewModel(viewModel);

        mPieChart.setOnSelectionChangedListener(this);

    }

    @Override
    public void onSelected(String label, float value) {
        mBinding.setCategory(new Category(label, formatCurrency(value)));
    }

    private String formatCurrency(float value) {
        return "$" + String.format(Locale.getDefault(), "%,.2f", value);
    }
}
