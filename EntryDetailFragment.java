package com.colinear.graphstuff;


import android.animation.ArgbEvaluator;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.colinear.graphstuff.DB.Entities.ChartEntity;
import com.colinear.graphstuff.DB.Entities.EntryEntity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntryDetailFragment extends LifecycleFragment {

    FloatingActionButton fab;

    ChartListViewModel chartListViewModel;

    ArrayList<String> unavailableNames = new ArrayList<>();
    SeekBar seekBar;
    EditText valueText;


    String color1 = "@*line_color";
    String color2 = "@*gradient_end";
    String theme = "GREEN";
    int c1;
    int c2;

    double last = 0;
    double min = 0;
    double max = 0;

    double distribution = 100;


    ChartEntity chartEntity;

    Button addEntryButton;
    CheckBox useSuggestionCheckbox;
    TextView commentText;


    public EntryDetailFragment() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fab = getActivity().findViewById(R.id.button);
        fab.hide();
        chartListViewModel = ViewModelProviders.of(this.getActivity()).get(ChartListViewModel.class);

        Log.i("AMXKASDA", chartListViewModel.getCurrentChartTitle());
        chartListViewModel.getChartByTitle(chartListViewModel.getCurrentChartTitle()).observe(this, new Observer<ChartEntity>() {
            @Override
            public void onChanged(@Nullable ChartEntity chartEntity) {

                chartListViewModel.getExtremeEntries(chartEntity.getTitle()).observeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(entries -> onChartLoaded(chartEntity, entries));

            }
        });


        seekBar = view.findViewById(R.id.seekBar);
        valueText = view.findViewById(R.id.entry_value_edittext);

        addEntryButton = view.findViewById(R.id.add_entry_button);
        useSuggestionCheckbox = view.findViewById(R.id.suggested_values_checkbox);
        commentText = view.findViewById(R.id.comment_text_view);





        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "Bariol_Bold.otf");
        valueText.setTypeface(face);


    }

    private void onChartLoaded(ChartEntity chartEntity, EntryEntity[] extremities) {
        this.chartEntity = chartEntity;

        Log.i("VALUES", extremities.length + "");


        if (extremities[0] != null) {
            last = extremities[0].getValue();
            min = extremities[1].getValue();
            max = extremities[2].getValue();
        }


        distribution = (int) (max - min);

        seekBar.setProgress(50);


        Log.i("VALUES", last + " : " + min + " : " + max + " : " + distribution);


        valueText.setText("" + last);


        theme = this.chartEntity.getColorScheme();
        c1 = ChartStyle.getColorResourceByName(color1, getActivity(), theme);
        c2 = ChartStyle.getColorResourceByName(color2, getActivity(), theme);

        Bitmap gradientBitmap = Util.applyGradient(R.drawable.fingerprint_glow, c1, c2, getActivity());
        gradientBitmap = Bitmap.createScaledBitmap(gradientBitmap, 300, 300, false);
        seekBar.setThumb(new BitmapDrawable(getResources(), gradientBitmap));

        seekBar.getProgressDrawable().setColorFilter(c2, PorterDuff.Mode.MULTIPLY);


        ColorStateList colorStateList = ColorStateList.valueOf(Color.TRANSPARENT);
        ViewCompat.setBackgroundTintList(valueText, colorStateList);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int color = (int) new ArgbEvaluator().evaluate((float) progress / seekBar.getMax(), c2, c1);
                valueText.setTextColor(color);
                valueText.setText("" + getValue(progress));

                seekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        int color = ChartStyle.getColorResourceByName("@*line_color",getActivity(),chartEntity.getColorScheme());
        commentText.setTextColor(color);

        addEntryButton.setBackgroundColor(color);
        addEntryButton.setTextColor(Color.WHITE);

        valueText.setTextColor(color);

        setCheckBoxColor(useSuggestionCheckbox, color, color);
        useSuggestionCheckbox.setTextColor(color);

    }


    public double getValue(int progress) {
        Log.i("VALUES", progress+"");
        return (int) ((min-distribution)+(distribution * 4) * ((double)progress / seekBar.getMax()));
    }


    @Override
    public void onPause() {
        super.onPause();
        fab.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entity_detail, container, false);
    }

    public void setCheckBoxColor(CheckBox checkBox, int checkedColor, int uncheckedColor) {
        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {checkedColor, uncheckedColor};
        CompoundButtonCompat.setButtonTintList(checkBox, new
                ColorStateList(states, colors));
    }

}
