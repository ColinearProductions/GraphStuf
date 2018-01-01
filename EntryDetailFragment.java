package com.colinear.graphstuff;


import android.app.DatePickerDialog;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.v4.app.DialogFragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.colinear.graphstuff.DB.Entities.ChartEntity;
import com.colinear.graphstuff.DB.Entities.EntryEntity;

import java.util.Calendar;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class EntryDetailFragment extends LifecycleFragment implements View.OnClickListener {


    ChartListViewModel chartListViewModel;


    String color1 = "@*line_color";
    String color2 = "@*gradient_end";
    String theme = "GREEN";


    FloatingActionButton minusButton;
    FloatingActionButton plusButton;

    double currentValue = 0;

    ChartEntity chartEntity;

    EditText valueText;
    Button addEntryButton;
    TextView commentText;
    TextView commentLabel;

    TextView titleText;

    String comment = "";

    EntryEntity lastEntry;


    EntryEntity updateEntry;

    public EntryDetailFragment() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        chartListViewModel = ViewModelProviders.of(getActivity()).get(ChartListViewModel.class);


        valueText = view.findViewById(R.id.entry_value_edittext);
        valueText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentValue = Double.parseDouble(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addEntryButton = view.findViewById(R.id.add_entry_button);
        commentText = view.findViewById(R.id.comment_text_view);
        commentLabel = view.findViewById(R.id.comment_label);
        titleText = view.findViewById(R.id.title_text);

        minusButton = view.findViewById(R.id.minus_fab);
        plusButton = view.findViewById(R.id.plus_fab);
        minusButton.setOnClickListener(this);
        plusButton.setOnClickListener(this);

        View.OnClickListener onCommentClickedListener = v -> {

            new MaterialDialog.Builder(getActivity())
                    .title("Add a comment")
                    .content(comment)
                    .positiveText("Apply")
                    .negativeText("Cancel")
                    .inputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                    .input("Comment", comment, (dialog, input) -> {
                        if (input.length() >= 1) {
                            commentText.setText(input);
                            comment = input.toString();
                        }
                    }).show();
        };

        commentText.setOnClickListener(onCommentClickedListener);
        commentLabel.setOnClickListener(onCommentClickedListener);


        addEntryButton.setOnClickListener(v -> {

            if (chartListViewModel.getAction().equals(Const.UPDATE_ENTRY_ACTION)) {
                updateEntry.setComment(comment);
                updateEntry.setValue(currentValue);

                chartListViewModel.updateEntry(updateEntry)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(result -> {
                            getActivity().onBackPressed();
                        });
            } else {
                chartListViewModel.addEntryWithObservable(new EntryEntity(comment, (int) currentValue, chartListViewModel.getCurrentChartEntity().getTitle()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(result -> {
                            getActivity().onBackPressed();
                        });
            }
        });


        Typeface face = Typeface.createFromAsset(getActivity().getAssets(), "Bariol_Bold.otf");
        valueText.setTypeface(face);
        commentLabel.setTypeface(face);
        titleText.setTypeface(face);


        if (!chartListViewModel.getAction().equals(Const.UPDATE_ENTRY_ACTION)) {

            /*chartListViewModel
                    .getChartByTitle(chartListViewModel.getCurrentChartTitle())
                    .observe(this, chartEntity -> onChartLoaded(chartEntity));*/
            addEntryButton.setText("Add");
            onChartLoaded(chartListViewModel.getCurrentChartEntity());
        } else {
            addEntryButton.setText("Update");
            chartEntity = chartListViewModel.getCurrentChartEntity();
            updateEntry = chartListViewModel.getCurrentEntryEntity();

            currentValue = updateEntry.getValue();
            comment = updateEntry.getComment();
            commentText.setText(comment);

            titleText.setText(chartListViewModel.getCurrentChartEntity().getTitle());

            updateColors();
        }


    }

    @Override
    public void onResume() {
        ((FloatingActionButton) getActivity().findViewById(R.id.button)).hide();
        super.onResume();
    }

    @Override
    public void onPause() {
        ((FloatingActionButton) getActivity().findViewById(R.id.button)).show();
        super.onPause();
    }


    private void onChartLoaded(ChartEntity chartEntity) {

        this.chartEntity = chartEntity;
        Log.i("VALUES", chartEntity.toString());
        chartListViewModel.getLastEntry(chartEntity.getTitle()).observeOn(AndroidSchedulers.mainThread())
                .onErrorReturnItem(new EntryEntity("", 0, null)) // if there is no last entry, initiate the view with 0
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(entry -> onlastEntryLoaded(entry));
    }

    private void onlastEntryLoaded(EntryEntity lastEntry) {

        this.lastEntry = lastEntry;

        currentValue = lastEntry.getValue();

        titleText.setText(chartEntity.getTitle());

        updateColors();


    }


    private void updateColors() {
        valueText.setText("" + currentValue);


        theme = this.chartEntity.getColorScheme();
        int c1 = ChartStyle.getColorResourceByName(color1, getActivity(), theme);
        int c2 = ChartStyle.getColorResourceByName(color2, getActivity(), theme);


        minusButton.setBackgroundTintList(ColorStateList.valueOf(c1));
        plusButton.setBackgroundTintList(ColorStateList.valueOf(c1));


        minusButton.setRippleColor(c2);
        plusButton.setRippleColor(c2);


        int UIcolor = ChartStyle.getColorResourceByName("@*line_color", getActivity(), chartEntity.getColorScheme());
        commentText.setTextColor(UIcolor);
        commentLabel.setTextColor(UIcolor);
        titleText.setTextColor(UIcolor);

        valueText.getBackground().mutate().setColorFilter(UIcolor, PorterDuff.Mode.SRC_ATOP);

        addEntryButton.setBackgroundColor(UIcolor);


        valueText.setTextColor(UIcolor);
        valueText.setText(currentValue + "");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entity_detail, container, false);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == minusButton.getId()) {
            currentValue -= 1;
        } else {
            currentValue += 1;
        }
        valueText.setText(currentValue + "");

    }


}
