package com.colinear.graphstuff;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class ChartStyle {
    private String chartBackground = "#000000";
    private String chartLineColor = "#FFFFFF";
    private float chartLineWidth = 3f;
    private String lineMode = "CUBIC";
    private boolean drawChartBorder = true;
    private String borderColor = "#FFFFFF";


    private String circleColor = "#FFFFFF";
    private float circleRadius = 7f;
    private boolean drawCircle = true;
    private boolean drawCircleHole = true;
    private String circleHoleColor = "#000000";
    private float circleHoleRadius = 4f;


    private boolean drawTextValue = true;
    private String valueTextColor = "#FFFFFF";
    private float valueFontSize = 12f;

    private boolean drawHighlightLine = true;
    private String highlightLineColor = "#ffffff";
    private float highlightLineWidth = 1f;


    private boolean leftAxisLineEnabled = true;
    private String leftAxisLineColor = "#FFFFFF";
    private String leftAxisTextColor = "#FFFFFF";
    private float leftAxisFontSize = 12f;
    private float leftAxisLineWidth = 1f;

    private boolean rightAxisLineEnabled = true;
    private String rightAxisLineColor = "#FFFFFF";
    private String rightAxisTextColor = "#FFFFFF";
    private float rightAxisFontSize = 12f;
    private float rightAxisLineWidth = 1f;

    private boolean xAxisLineEnabled = true;
    private String xAxisLineColor = "#FFFFFF";
    private String xAxisTextColor = "#FFFFFF";
    private float xAxisFontSize = 12f;
    private float xAxisLineWidth = 1f;
    private String xAxisPosition = "both";


    private boolean drawHorizontalGridLines = true;
    private float horizontalGridLineWidth = 1f;
    private String horizontalGridLineColor = "#FFFFFF";

    private boolean drawVerticalGridLines = true;
    private float verticalGridLineWidth = 1f;
    private String verticalGridLineColor = "#FFFFFF";


    private boolean drawDescription = true;
    private String descriptionColor = "#FFFFFF";
    private String descriptionAlignment = "right";
    private float descriptionFontSize = 12f;


    private boolean drawLegend = true;
    private String legendTextColor = "#FFFFFF";
    private float legendFontSize = 12f;

    private boolean overrideOffset = false;
    private float offsetLeft = 0;
    private float offsetRight = 0;
    private float offsetTop = 0;
    private float offsetBottom = 0;


    private boolean fill = true;
    private String fillColor = "#FFFFFF";
    private boolean gradientFill = true;
    private ChartGradient[] gradients;




    //todo animation
    // todo fill

    private transient Gson gson = new Gson();

    public String toJson(){
        return gson.toJson(this);
    }

    public static ChartStyle fromJson(String pathToJsonFile, Context ctx){
        String json = null;
        try {
            InputStream is = ctx.getAssets().open(pathToJsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new Gson().fromJson(json, ChartStyle.class);

    }





    public LineDataSet applyStyle(LineDataSet lineDataSet){
        lineDataSet.setColor(Color.parseColor(chartLineColor));
        lineDataSet.setLineWidth(chartLineWidth);

        switch (lineMode){
            case "CUBIC":
                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                break;
            case "STEPPED":
                lineDataSet.setMode(LineDataSet.Mode.STEPPED);
                break;
            case "HORIZONTAL_BEZIER":
                lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                break;
            default:
                lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        }

        lineDataSet.setDrawCircles(drawCircle);
        lineDataSet.setCircleColor(Color.parseColor(circleColor));
        lineDataSet.setCircleRadius(circleRadius);
        lineDataSet.setDrawCircleHole(drawCircleHole);
        lineDataSet.setCircleColorHole(Color.parseColor(circleHoleColor));
        lineDataSet.setCircleHoleRadius(circleHoleRadius);
        lineDataSet.setDrawValues(drawTextValue);
        lineDataSet.setValueTextColor(Color.parseColor(valueTextColor));
        lineDataSet.setValueTextSize(valueFontSize);
        lineDataSet.setHighlightEnabled(drawHighlightLine);
        lineDataSet.setHighLightColor(Color.parseColor(highlightLineColor));
        lineDataSet.setHighlightLineWidth(highlightLineWidth);


        lineDataSet.setDrawFilled(fill);


        lineDataSet.setFillColor(Color.parseColor(fillColor));

        if(gradientFill){
            GradientDrawable[] layers = new GradientDrawable[gradients.length];
            Log.i("JSON","There are " + layers.length + " layers");
            for(int i=0; i <gradients.length;i++)
                layers[i] = gradients[i].generateGradient();
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            lineDataSet.setFillDrawable(layerDrawable);
        }
        return lineDataSet;


    }

    public LineChart applyStyle(LineChart lineChart){




        lineChart.setBackgroundColor(Color.parseColor(chartBackground));

        if(overrideOffset)
            lineChart.setViewPortOffsets(offsetLeft, offsetTop, offsetRight, offsetBottom);


        // Left axis labels
        lineChart.getAxisLeft().setEnabled(leftAxisLineEnabled);
        lineChart.getAxisLeft().setTextColor(Color.parseColor(leftAxisTextColor)); // left y-axis
        lineChart.getAxisLeft().setAxisLineColor(Color.parseColor(leftAxisLineColor));
        lineChart.getAxisLeft().setTextSize(leftAxisFontSize);
        lineChart.getAxisLeft().setAxisLineWidth(leftAxisLineWidth);

        // Right axis labels
        lineChart.getAxisRight().setEnabled(rightAxisLineEnabled);
        lineChart.getAxisRight().setTextColor(Color.parseColor(rightAxisTextColor)); // left y-axis
        lineChart.getAxisRight().setAxisLineColor(Color.parseColor(rightAxisLineColor));
        lineChart.getAxisRight().setTextSize(rightAxisFontSize);
        lineChart.getAxisRight().setAxisLineWidth(rightAxisLineWidth);
        switch (xAxisPosition){
            case "top":
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
                break;
            case "bottom":
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                break;
            case "both":
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
                break;
            case "top_inside":
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP_INSIDE);
                break;
            case "bottom_inside":
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
                break;
        }



        // Top axis labels
        lineChart.getXAxis().setEnabled(xAxisLineEnabled);
        lineChart.getXAxis().setTextColor(Color.parseColor(xAxisTextColor));
        lineChart.getXAxis().setAxisLineWidth(xAxisLineWidth);
        lineChart.getXAxis().setTextSize(xAxisFontSize);
        lineChart.getXAxis().setAxisLineColor(Color.parseColor(xAxisLineColor));

        // Grid
            //Horizontal
        lineChart.getAxisLeft().setGridColor(Color.parseColor(horizontalGridLineColor));
        lineChart.getAxisLeft().setGridLineWidth(horizontalGridLineWidth);
        lineChart.getAxisLeft().setDrawGridLines(drawHorizontalGridLines);
        lineChart.getAxisRight().setGridColor(Color.parseColor(horizontalGridLineColor));
        lineChart.getAxisRight().setGridLineWidth(horizontalGridLineWidth);
        lineChart.getAxisRight().setDrawGridLines(drawHorizontalGridLines);
            //Vertical
        lineChart.getXAxis().setGridColor(Color.parseColor(verticalGridLineColor));
        lineChart.getXAxis().setGridLineWidth(verticalGridLineWidth);
        lineChart.getXAxis().setDrawGridLines(drawVerticalGridLines);


        lineChart.getLegend().setEnabled(drawLegend);
        lineChart.getLegend().setTextColor(Color.parseColor(legendTextColor));
        lineChart.getLegend().setTextSize(legendFontSize);

        lineChart.getDescription().setEnabled(drawDescription);
        lineChart.getDescription().setTextColor(Color.parseColor(descriptionColor));
        lineChart.getDescription().setTextSize(descriptionFontSize);

        Paint.Align align;
        switch (descriptionAlignment){
            case "right":
                align = Paint.Align.CENTER;
                break;
            case "left":
                align = Paint.Align.CENTER;
                break;
            case "center":
                align = Paint.Align.CENTER;
                break;
            default:
                align = Paint.Align.RIGHT;
                break;
        }
        lineChart.getDescription().setTextAlign(align);


        lineChart.setDrawBorders(drawChartBorder);
        lineChart.setBorderColor(Color.parseColor(borderColor));



        return lineChart;
    }






    public class ChartGradient{
        public String[] colors;
        public String orientation;
        public String type;

        public GradientDrawable generateGradient(){
            GradientDrawable gradientDrawable = new GradientDrawable();
            int[] gColors = new int[colors.length];

            for(int i=0; i<colors.length;i++)
                gColors[i] = Color.parseColor(this.colors[i]);

            gradientDrawable.setColors(gColors);

            gradientDrawable.setOrientation(GradientDrawable.Orientation.valueOf(orientation));
            int type =GradientDrawable.LINEAR_GRADIENT;
            switch (this.type){
                case "LINEAR_GRADIENT":
                    type = GradientDrawable.LINEAR_GRADIENT;
                    break;
                case "RADIAL_GRADIENT":
                    type = GradientDrawable.RADIAL_GRADIENT;
                    break;
                case "SWEEP_GRADIENT":
                    type =GradientDrawable.SWEEP_GRADIENT;
                    break;
            }
            gradientDrawable.setGradientType(type);
            gradientDrawable.setShape(GradientDrawable.RECTANGLE);

            return gradientDrawable;
        }
    }


    public String getChartBackground() {
        return chartBackground;
    }

    public String getChartLineColor() {
        return chartLineColor;
    }

    public float getChartLineWidth() {
        return chartLineWidth;
    }

    public String getLineMode() {
        return lineMode;
    }

    public boolean isDrawChartBorder() {
        return drawChartBorder;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public String getCircleColor() {
        return circleColor;
    }

    public float getCircleRadius() {
        return circleRadius;
    }

    public boolean isDrawCircle() {
        return drawCircle;
    }

    public boolean isDrawCircleHole() {
        return drawCircleHole;
    }

    public String getCircleHoleColor() {
        return circleHoleColor;
    }

    public float getCircleHoleRadius() {
        return circleHoleRadius;
    }

    public boolean isDrawTextValue() {
        return drawTextValue;
    }

    public String getValueTextColor() {
        return valueTextColor;
    }

    public float getValueFontSize() {
        return valueFontSize;
    }

    public boolean isDrawHighlightLine() {
        return drawHighlightLine;
    }

    public String getHighlightLineColor() {
        return highlightLineColor;
    }

    public float getHighlightLineWidth() {
        return highlightLineWidth;
    }

    public boolean isLeftAxisLineEnabled() {
        return leftAxisLineEnabled;
    }

    public String getLeftAxisLineColor() {
        return leftAxisLineColor;
    }

    public String getLeftAxisTextColor() {
        return leftAxisTextColor;
    }

    public float getLeftAxisFontSize() {
        return leftAxisFontSize;
    }

    public float getLeftAxisLineWidth() {
        return leftAxisLineWidth;
    }

    public boolean isRightAxisLineEnabled() {
        return rightAxisLineEnabled;
    }

    public String getRightAxisLineColor() {
        return rightAxisLineColor;
    }

    public String getRightAxisTextColor() {
        return rightAxisTextColor;
    }

    public float getRightAxisFontSize() {
        return rightAxisFontSize;
    }

    public float getRightAxisLineWidth() {
        return rightAxisLineWidth;
    }

    public boolean isxAxisLineEnabled() {
        return xAxisLineEnabled;
    }

    public String getxAxisLineColor() {
        return xAxisLineColor;
    }

    public String getxAxisTextColor() {
        return xAxisTextColor;
    }

    public float getxAxisFontSize() {
        return xAxisFontSize;
    }

    public float getxAxisLineWidth() {
        return xAxisLineWidth;
    }

    public String getxAxisPosition() {
        return xAxisPosition;
    }

    public boolean isDrawHorizontalGridLines() {
        return drawHorizontalGridLines;
    }

    public float getHorizontalGridLineWidth() {
        return horizontalGridLineWidth;
    }

    public String getHorizontalGridLineColor() {
        return horizontalGridLineColor;
    }

    public boolean isDrawVerticalGridLines() {
        return drawVerticalGridLines;
    }

    public float getVerticalGridLineWidth() {
        return verticalGridLineWidth;
    }

    public String getVerticalGridLineColor() {
        return verticalGridLineColor;
    }

    public boolean isDrawDescription() {
        return drawDescription;
    }

    public String getDescriptionColor() {
        return descriptionColor;
    }

    public String getDescriptionAlignment() {
        return descriptionAlignment;
    }

    public float getDescriptionFontSize() {
        return descriptionFontSize;
    }

    public boolean isDrawLegend() {
        return drawLegend;
    }

    public String getLegendTextColor() {
        return legendTextColor;
    }

    public float getLegendFontSize() {
        return legendFontSize;
    }

    public float getOffsetLeft() {
        return offsetLeft;
    }

    public float getOffsetRight() {
        return offsetRight;
    }

    public float getOffsetTop() {
        return offsetTop;
    }

    public float getOffsetBottom() {
        return offsetBottom;
    }

    public boolean isFill() {
        return fill;
    }

    public String getFillColor() {
        return fillColor;
    }

    public boolean isGradientFill() {
        return gradientFill;
    }

    public ChartGradient[] getGradients() {
        return gradients;
    }
}

