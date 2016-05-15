package io.agileninja.donutchartpoc;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * View model for pie chart.
 */
public class PieChartViewModel extends BaseObservable
        implements PieChart.OnSelectionChangedListener {
    /**
     * Data set to pupolate pie chart.
     */
    private List<PieChartSlice> mPieChartSlices;
    /**
     * Title of pie chart.
     */
    private String mTitle;
    /**
     * Selected pie chart slice value.
     */
    private String mSelectedValue;
    /**
     * Selected pie chart slice label.
     */
    private String mSelectedLabel;

    /**
     * Constructor requiring pie chart's title.
     *
     * @param title Title of pie chart.
     */
    public PieChartViewModel(final String title) {
        mTitle = title;
        mPieChartSlices = new ArrayList<>();
    }

    /**
     * Returns pie chart's data set.
     *
     * @return Pie chart's data set.
     */
    public final List<PieChartSlice> getPieChartSlices() {
        return mPieChartSlices;
    }


    /**
     * Add slice data to pie chart.
     *
     * @param pieChartSlice Slice data containing label, value,
     *                      and color of slice.
     */
    public final void addSlice(final PieChartSlice pieChartSlice) {
        mPieChartSlices.add(pieChartSlice);
    }

    /**
     * Return pie chart's title.
     *
     * @return Title of pie chart.
     */
    public final String getTitle() {
        return mTitle;
    }

    /**
     * Get selected value of pie chart.
     *
     * @return String representation of selected value.
     */
    @Bindable
    public final String getSelectedValue() {
        return mSelectedValue;
    }

    /**
     * Get selected label of pie chart.
     *
     * @return Label of selected pie chart slice.
     */
    @Bindable
    public final String getSelectedLabel() {
        return mSelectedLabel;
    }

    /**
     * Format float value to String dollar currency.
     *
     * @param value Float value to format.
     * @return String formatted currency.
     */
    private String formatCurrency(final float value) {
        return "$" + String.format(Locale.getDefault(), "%,.2f", value);
    }

    @Override
    public final void onSelected(final String label, final float value) {
        mSelectedLabel = label;
        mSelectedValue = formatCurrency(value);
        notifyPropertyChanged(io.agileninja.donutchartpoc.BR.selectedLabel);
        notifyPropertyChanged(io.agileninja.donutchartpoc.BR.selectedValue);
    }
}
