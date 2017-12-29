package com.colinear.graphstuff;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;


import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class XAxisDateFormatter implements IAxisValueFormatter {



    Long[] dateMapping;
    public XAxisDateFormatter() {

    }

    public void setDateMapping(Long[] dateMapping){
        this.dateMapping = dateMapping;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        DateFormat df = new SimpleDateFormat("MM/dd");
        Date date = new Date( dateMapping[(int)value]);
        return df.format(date);
    }
}
