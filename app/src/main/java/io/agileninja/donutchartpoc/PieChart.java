package io.agileninja.donutchartpoc;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class PieChart extends com.github.mikephil.charting.charts.PieChart implements OnChartValueSelectedListener {

    private Context mContext;
    private OnSelectionChangedListener mOnSelectionChangedListener;

    public PieChart(Context context) {
        super(context);
        init(context, null);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mContext = context;
        setRotationEnabled(false);
        setOnChartValueSelectedListener(this);
        setDrawSliceText(false);
        setDescription("");
        getLegend().setEnabled(false);

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        int selectionIndex = e.getXIndex();
        float fromAngle = getRotationAngle();
        float offset = getDrawAngles()[selectionIndex] / 2;
        float toAngle = 360f - (getAbsoluteAngles()[selectionIndex] - offset);
        Toast.makeText(mContext, getRotationAngle() + " - " + getDrawAngles()[selectionIndex], Toast.LENGTH_SHORT).show();
        spin(1800, fromAngle, (toAngle), Easing.EasingOption.EaseOutBack);
        if (mOnSelectionChangedListener != null) {
            mOnSelectionChangedListener.onSelected(getXValue(selectionIndex), e.getVal());
        }
    }

    @Override
    public void onNothingSelected() {

    }

    public void setOnSelectionChangedListener(OnSelectionChangedListener onSelectionChangedListener) {
        mOnSelectionChangedListener = onSelectionChangedListener;
    }

    public interface OnSelectionChangedListener {
        void onSelected(String label, float value);
    }

    public void setViewModel(PieChartViewModel viewModel) {
        int size = viewModel.getPieChartDataSet().size();
        List<Entry> entries = new ArrayList<>(size);
        List<String> labels = new ArrayList<>(size);
        int[] colors = new int[size];
        PieChartData data;
        for (int i = 0; i < size; i++) {
            data = viewModel.getPieChartDataSet().get(i);
            labels.add(data.getLabel());
            entries.add(new Entry(data.getValue(), i));
            colors[i] = Color.parseColor(data.getColor());
        }
        PieDataSet dataSet = new PieDataSet(entries, viewModel.getTitle());
        dataSet.setDrawValues(false);
        dataSet.setColors(colors);
        PieData pieData = new PieData(labels, dataSet);
        setData(pieData);
    }
}
