package io.agileninja.donutchartpoc;

import java.util.List;

public class PieChartViewModel {
    private List<PieChartData> mPieChartDataSet;
    private String mTitle;

    public PieChartViewModel(String title, List<PieChartData> pieChartDataSet) {
        mTitle = title;
        mPieChartDataSet = pieChartDataSet;
    }

    public List<PieChartData> getPieChartDataSet() {
        return mPieChartDataSet;
    }

    public void setPieChartDataSet(List<PieChartData> pieChartDataSet) {
        mPieChartDataSet = pieChartDataSet;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
