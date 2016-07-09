package io.agileninja.donutchartpoc;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class PieChartRenderer extends com.github.mikephil.charting.renderer.PieChartRenderer {

    private float arcSpace = 0;
    private float arcWidth = 27;

    public PieChartRenderer(PieChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
        chart.getLegend().setEnabled(false);
        float x = 40;
        chart.setExtraOffsets(x, x, x, x);
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {
        Path path = new Path();
        RectF rectF = new RectF();
        super.drawHighlighted(c, indices);
        drawArc(mBitmapCanvas, indices, path, rectF, mRenderPaint, true);
    }


    private void drawArc(Canvas canvas, Highlight[] indices, Path path, RectF rectF, Paint paint, boolean drawOuterArc) {

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        float angle;
        float rotationAngle = mChart.getRotationAngle();

        float[] drawAngles = mChart.getDrawAngles();
        float[] absoluteAngles = mChart.getAbsoluteAngles();
        final PointF center = mChart.getCenterCircleBox();
        final float radius = drawOuterArc ? mChart.getRadius() + arcWidth + arcSpace : mChart.getRadius();
        final boolean drawInnerArc = mChart.isDrawHoleEnabled() && !mChart.isDrawSlicesUnderHoleEnabled();
        final float userInnerRadius = drawOuterArc ? radius + arcWidth : radius * (mChart.getHoleRadius() / 100.f);

        final RectF highlightedCircleBox = new RectF();

        for (int i = 0; i < indices.length; i++) {

            // get the index to highlight
            int xIndex = indices[i].getXIndex();
            if (xIndex >= drawAngles.length)
                continue;

            IPieDataSet set = mChart.getData()
                    .getDataSetByIndex(indices[i]
                            .getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            final int entryCount = set.getEntryCount();
            int visibleAngleCount = 0;
            for (int j = 0; j < entryCount; j++) {
                // draw only if the value is greater than zero
                if ((Math.abs(set.getEntryForIndex(j).getVal()) > 0.000001)) {
                    visibleAngleCount++;
                }
            }

            if (xIndex == 0)
                angle = 0.f;
            else
                angle = absoluteAngles[xIndex - 1] * phaseX;

            final float sliceSpace = visibleAngleCount <= 1 ? 0.f : set.getSliceSpace();

            float sliceAngle = drawAngles[xIndex];
            float innerRadius = userInnerRadius;

            float shift = set.getSelectionShift();
            final float highlightedRadius = radius + shift;
            highlightedCircleBox.set(mChart.getCircleBox());
            highlightedCircleBox.inset(-(arcWidth + arcSpace), -(arcWidth + arcSpace));

            final boolean accountForSliceSpacing = sliceSpace > 0.f && sliceAngle <= 180.f;

            paint.setColor(set.getColor(xIndex));

            final float sliceSpaceAngleOuter = visibleAngleCount == 1 ?
                    0.f :
                    sliceSpace / (Utils.FDEG2RAD * radius);

            final float sliceSpaceAngleShifted = visibleAngleCount == 1 ?
                    0.f :
                    sliceSpace / (Utils.FDEG2RAD * highlightedRadius);

            final float startAngleOuter = rotationAngle + (angle + sliceSpaceAngleOuter / 2.f) * phaseY;
            float sweepAngleOuter = (sliceAngle - sliceSpaceAngleOuter) * phaseY;
            if (sweepAngleOuter < 0.f) {
                sweepAngleOuter = 0.f;
            }

            final float startAngleShifted = rotationAngle + (angle + sliceSpaceAngleShifted / 2.f) * phaseY;
            float sweepAngleShifted = (sliceAngle - sliceSpaceAngleShifted) * phaseY;
            if (sweepAngleShifted < 0.f) {
                sweepAngleShifted = 0.f;
            }

            path.reset();

            if (sweepAngleOuter % 360f == 0.f) {
                // Android is doing "mod 360"
                path.addCircle(center.x, center.y, highlightedRadius, Path.Direction.CW);
            } else {

                path.moveTo(
                        center.x + highlightedRadius * (float) Math.cos(startAngleShifted * Utils.FDEG2RAD),
                        center.y + highlightedRadius * (float) Math.sin(startAngleShifted * Utils.FDEG2RAD));

                path.arcTo(
                        highlightedCircleBox,
                        startAngleShifted,
                        sweepAngleShifted
                );
            }

            float sliceSpaceRadius = 0.f;
            if (accountForSliceSpacing) {
                sliceSpaceRadius =
                        calculateMinimumRadiusForSpacedSlice(
                                center, radius,
                                sliceAngle * phaseY,
                                center.x + radius * (float) Math.cos(startAngleOuter * Utils.FDEG2RAD),
                                center.y + radius * (float) Math.sin(startAngleOuter * Utils.FDEG2RAD),
                                startAngleOuter,
                                sweepAngleOuter);
            }

            // API < 21 does not receive floats in addArc, but a RectF
            rectF.set(
                    center.x - innerRadius,
                    center.y - innerRadius,
                    center.x + innerRadius,
                    center.y + innerRadius);

            if (drawInnerArc &&
                    (innerRadius > 0.f || accountForSliceSpacing)) {

                if (accountForSliceSpacing) {
                    float minSpacedRadius = sliceSpaceRadius;

                    if (minSpacedRadius < 0.f)
                        minSpacedRadius = -minSpacedRadius;

                    innerRadius = Math.max(innerRadius, minSpacedRadius);
                }

                final float sliceSpaceAngleInner = visibleAngleCount == 1 || innerRadius == 0.f ?
                        0.f :
                        sliceSpace / (Utils.FDEG2RAD * innerRadius);
                final float startAngleInner = rotationAngle + (angle + sliceSpaceAngleInner / 2.f) * phaseY;
                float sweepAngleInner = (sliceAngle - sliceSpaceAngleInner) * phaseY;
                if (sweepAngleInner < 0.f) {
                    sweepAngleInner = 0.f;
                }
                final float endAngleInner = startAngleInner + sweepAngleInner;

                if (sweepAngleOuter % 360f == 0.f) {
                    // Android is doing "mod 360"
                    path.addCircle(center.x, center.y, innerRadius, Path.Direction.CCW);
                } else {

                    path.lineTo(
                            center.x + innerRadius * (float) Math.cos(endAngleInner * Utils.FDEG2RAD),
                            center.y + innerRadius * (float) Math.sin(endAngleInner * Utils.FDEG2RAD));

                    path.arcTo(
                            rectF,
                            endAngleInner,
                            -sweepAngleInner
                    );
                }
            } else {

                if (sweepAngleOuter % 360f != 0.f) {

                    if (accountForSliceSpacing) {
                        final float angleMiddle = startAngleOuter + sweepAngleOuter / 2.f;

                        final float arcEndPointX = center.x +
                                sliceSpaceRadius * (float) Math.cos(angleMiddle * Utils.FDEG2RAD);
                        final float arcEndPointY = center.y +
                                sliceSpaceRadius * (float) Math.sin(angleMiddle * Utils.FDEG2RAD);

                        path.lineTo(
                                arcEndPointX,
                                arcEndPointY);

                    } else {

                        path.lineTo(
                                center.x,
                                center.y);
                    }

                }

            }

            path.close();

            canvas.drawPath(path, paint);
        }
    }
}
