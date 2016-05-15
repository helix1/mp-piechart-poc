package io.agileninja.donutchartpoc;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Pie chart extension of MPAndroidChart's pie chart.
 */
public class PieChart extends com.github.mikephil.charting.charts.PieChart
        implements OnChartValueSelectedListener {

    /**
     * Angle where pie chart will spin to (360 for EAST, 90 for SOUTH,
     * 180 for WEST, and 270 for NORTH).
     */
    private static final float PIE_CHART_FACING_ANGLE = 360f;
    /**
     * Duration of pie chart spin animation in milliseconds.
     */
    public static final int PIE_CHART_SPIN_DURATION_MILLISECONDS = 1800;
    /**
     * Call back for when pie slice selection is changed.
     */
    private OnSelectionChangedListener mOnSelectionChangedListener;

    /**
     * Constructor requiring Context.
     *
     * @param context Context where the pie chart will be located.
     */
    public PieChart(final Context context) {
        super(context);
        init(context, null);
    }

    /**
     * Constructor requiring Context and AttributeSet.
     *
     * @param context Context where the pie chart will be located.
     * @param attrs   AttributeSet.
     */
    public PieChart(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * Constructor requiring Context, AttributeSet, and style definition.
     *
     * @param context  Context where the pie chart will be located.
     * @param attrs    AttributeSet.
     * @param defStyle Style definition.
     */
    public PieChart(final Context context, final AttributeSet attrs,
                    final int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /**
     * View initiation.
     *
     * @param context Context where the pie chart will be located.
     * @param attrs   AttributeSet.
     */
    private void init(final Context context, final AttributeSet attrs) {
        setRotationEnabled(false);
        setOnChartValueSelectedListener(this);
        setDrawSliceText(false);
        setDescription("");
        getLegend().setEnabled(false);
    }

    @Override
    public final void onValueSelected(final Entry e, final int dataSetIndex,
                                      final Highlight h) {
        int selectionIndex = e.getXIndex();
        float fromAngle = getRotationAngle();
        float offset = getDrawAngles()[selectionIndex] / 2;
        float toAngle = PIE_CHART_FACING_ANGLE
                - (getAbsoluteAngles()[selectionIndex] - offset);
        spin(PIE_CHART_SPIN_DURATION_MILLISECONDS, fromAngle, (toAngle),
                Easing.EasingOption.EaseOutBack);
        if (mOnSelectionChangedListener != null) {
            mOnSelectionChangedListener.onSelected(getXValue(selectionIndex),
                    e.getVal());
        }
        highlightValues(null);
    }

    @Override
    public final void onNothingSelected() {

    }

    /**
     * Set listener to listen to when a selection is made.
     *
     * @param onSelectionChangedListener Interface implementation that acts on
     *                                   value selected.
     */
    public final void setOnSelectionChangedListener(
            final OnSelectionChangedListener onSelectionChangedListener) {
        mOnSelectionChangedListener = onSelectionChangedListener;
    }

    /**
     * Listener interface for selected values.
     */
    public interface OnSelectionChangedListener {
        /**
         * Triggered when pie chart slice is selected.
         *
         * @param label Name of the pie chart slice selected.
         * @param value Value of the pie chart slice selected.
         */
        void onSelected(String label, float value);
    }

    /**
     * Set pie chart view model.
     *
     * @param viewModel View model pertaining to pie chart.
     */
    public final void setViewModel(final PieChartViewModel viewModel) {
        int size = viewModel.getPieChartSlices().size();
        List<Entry> entries = new ArrayList<>(size);
        List<String> labels = new ArrayList<>(size);
        int[] colors = new int[size];
        PieChartSlice data;
        for (int i = 0; i < size; i++) {
            data = viewModel.getPieChartSlices().get(i);
            labels.add(data.getLabel());
            entries.add(new Entry(data.getValue(), i));
            colors[i] = Color.parseColor(data.getColor());
        }
        PieDataSet dataSet = new PieDataSet(entries, viewModel.getTitle());
        dataSet.setDrawValues(false);
        dataSet.setColors(colors);
        PieData pieData = new PieData(labels, dataSet);
        setData(pieData);
        onValueSelected(getEntriesAtIndex(0).get(0), 0, null);
        invalidate();
    }
}
