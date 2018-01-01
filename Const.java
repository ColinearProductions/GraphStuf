package com.colinear.graphstuff;

import android.content.Context;

public class Const {


    //CHART OPTIONS
    public static int LOCK_ZOOM = 0;
    public static int SHOW_VALUES = 1;

    public static String COLOR_SCHEME_RED = "RED";
    public static String COLOR_SCHEME_GREEN = "GREEN";
    public static String COLOR_SCHEME_BLUE = "BLUE";

    public static String UPDATE_ENTRY_ACTION = "UPDATE_ENTRY_ACTION";

    public static int GET_COLOR_BY_SCHEME(String SCHEME_NAME, Context context) {
        if (SCHEME_NAME.equals(COLOR_SCHEME_RED))
            return context.getResources().getColor(R.color.RED_line_color);
        else if (SCHEME_NAME.equals(COLOR_SCHEME_GREEN))
            return context.getResources().getColor(R.color.GREEN_line_color);
        else
            return context.getResources().getColor(R.color.BLUE_line_color);
    }


}
