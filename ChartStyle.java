package com.colinear.graphstuff;

import android.graphics.Color;
import android.graphics.Paint;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

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

    private boolean topAxisLineEnabled = true;
    private String topAxisLineColor = "#FFFFFF";
    private String topAxisTextColor = "#FFFFFF";
    private float topAxisFontSize = 12f;
    private float topAxisLineWidth = 1f;


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

    //todo animation


    private transient Gson gson = new Gson();

    public String toJson(){
        return gson.toJson(this);
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

        return lineDataSet;
    }

    public LineChart applyStyle(LineChart lineChart){

        lineChart.setBackgroundColor(Color.parseColor(chartBackground));


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


        // Top axis labels
        lineChart.getXAxis().setEnabled(topAxisLineEnabled);
        lineChart.getXAxis().setTextColor(Color.parseColor(topAxisTextColor));
        lineChart.getXAxis().setAxisLineWidth(topAxisLineWidth);
        lineChart.getXAxis().setTextSize(topAxisFontSize);
        lineChart.getXAxis().setAxisLineColor(Color.parseColor(topAxisLineColor));

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
}

