package com.colinear.graphstuff;

import android.content.Context;

public class Const {


    //CHART OPTIONS
    public static int LOCK_ZOOM = 0;
    public static int SHOW_VALUES = 1;

    public static int COLOR_SCHEME_RED = 100;
    public static int COLOR_SCHEME_GREEN = 101;
    public static int COLOR_SCHEME_BLUE = 102;

    public static int GET_COLOR_BY_SCHEME(int SCHEME_NAME, Context context) {
        if (SCHEME_NAME == COLOR_SCHEME_RED)
            return context.getResources().getColor(R.color.red_scheme);
        else if (SCHEME_NAME == COLOR_SCHEME_GREEN)
            return context.getResources().getColor(R.color.green_scheme);
        else
            return context.getResources().getColor(R.color.blue_scheme);
    }

}
