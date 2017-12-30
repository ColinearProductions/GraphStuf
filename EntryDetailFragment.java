package com.colinear.graphstuff;


import android.animation.ArgbEvaluator;
import android.arch.lifecycle.LifecycleFragment;
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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.colinear.graphstuff.DB.Entities.ChartEntity;
import com.colinear.graphstuff.DB.Entities.EntryEntity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntryDetailFragment extends LifecycleFragment {

    FloatingActionButton fab;

    ChartListViewModel chartListViewModel;

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

    double currentValue = 0;


    ChartEntity chartEntity;

    Button addEntryButton;
    CheckBox useSuggestionCheckbox;
    TextView commentText;

    TextView commentLabel;

    EntryEntity[] extremities;

    public EntryDetailFragment() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fab = getActivity().findViewById(R.id.button);
        fab.hide();

        chartListViewModel = ViewModelProviders.of(this.getActivity()).get(ChartListViewModel.class);

        chartListViewModel.getChartByTitle(chartListViewModel.getCurrentChartTitle()).observe(this, chartEntity -> chartListViewModel.getExtremeEntries(chartEntity.getTitle()).observeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(entries -> onChartLoaded(chartEntity, entries)));


        seekBar = view.findViewById(R.id.seekBar);
        valueText = view.findViewById(R.id.entry_value_edittext);

        addEntryButton = view.findViewById(R.id.add_entry_button);
        useSuggestionCheckbox = view.findViewById(R.id.suggested_values_checkbox);
        commentText = view.findViewById(R.id.comment_text_view);
        commentLabel = view.findViewById(R.id.comment_label);

        View.OnClickListener onCommentClickedListener = v -> {
            String prefill = commentText.getText().toString();
            if (prefill.contains("Touch to add a comment"))
                prefill = "";
            new MaterialDialog.Builder(getActivity())
                    .title("Add a comment")
                    .content(prefill)
                    .positiveText("Apply")
                    .negativeText("Cancel")
                    .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                    .input("Comment", prefill, (dialog, input) -> {
                        if (input.length() >= 1)
                            commentText.setText(input);
                    }).show();
        };

        commentText.setOnClickListener(onCommentClickedListener);
        commentLabel.setOnClickListener(onCommentClickedListener);

        addEntryButton.setOnClickListener(v -> {

            chartListViewModel.addEntryWithObservable(new EntryEntity(commentText.getText().toString(), (int) currentValue, chartListViewModel.getCurrentChartTitle(), chartListViewModel.getCurrentChartLastIndex())).observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(result -> {
                        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                    });
        });


        useSuggestionCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateValues(isChecked);


        });

        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "Bariol_Bold.otf");
        valueText.setTypeface(face);
        commentLabel.setTypeface(face);

    }

    private void onChartLoaded(ChartEntity chartEntity, EntryEntity[] extremities) {
        this.chartEntity = chartEntity;
        this.extremities = extremities;

        if (extremities[0] == null)
            currentValue = 15;
        else
            currentValue = extremities[0].getValue();


        seekBar.setMax(300);
        updateValues(true);


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
                updateCurrentValue(progress);

                valueText.setText("" + (int) currentValue);

                seekBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        int color = ChartStyle.getColorResourceByName("@*line_color", getActivity(), chartEntity.getColorScheme());
        commentText.setTextColor(color);
        commentLabel.setTextColor(color);


        addEntryButton.setBackgroundColor(color);
        addEntryButton.setTextColor(Color.WHITE);

        valueText.setTextColor(color);

        setCheckBoxColor(useSuggestionCheckbox, color, color);
        useSuggestionCheckbox.setTextColor(color);

    }


    public void updateCurrentValue(int progress) {


        currentValue =  min + max * ((double) progress / seekBar.getMax());

        Log.i("VALUES", progress + " - " + currentValue);

    }

    public void updateValues(boolean suggested) {


        if (suggested) {
            if (extremities[0] != null) {
                last = extremities[0].getValue();
                min = extremities[1].getValue();
                max = extremities[2].getValue();


            } else {
                last = 50;
                min = 0;
                max = 100;

            }


            distribution = max - min;


            min = last - distribution;

            if (distribution < 5)
                min = last - distribution * 3;

            if (distribution < 1)
                min = last * .5;

            if (min < 0)
                min = 0;

            max = last + distribution * .3;

        } else {
            if (extremities[0] != null) {
                last = extremities[0].getValue();
                min = extremities[1].getValue();
                max = extremities[2].getValue();

                max = max + last;
                min = 0;

            } else {
                last = 150;
                min = 0;
                max = 300;
            }
        }


        if (currentValue > max)
            currentValue = max;

        Log.i("VALUES", "" + currentValue / (max));
        Log.i("VALUES", "" + seekBar.getMax());
        Log.i("VALUES", "" + currentValue / (max) * (double) (seekBar.getMax()));
        Log.i("VALUES", "" + (int) (currentValue / max * (double) (seekBar.getMax())));
        seekBar.setProgress((int) (((currentValue-min) / (max)) * (double) (seekBar.getMax())));


        // (max+min) * ((double) progress / seekBar.getMax())

        Log.i("VALUES", currentValue + " : " + min + " : " + max + " : " + distribution);

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
