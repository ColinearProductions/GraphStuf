package com.colinear.graphstuff;


import android.app.Activity;
import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.colinear.graphstuff.DB.Entities.ChartEntity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateChartFragment extends LifecycleFragment implements TextWatcher {

    FloatingActionButton fab;

    ChartListViewModel chartListViewModel;

    EditText chartTitleEditText;

    ArrayList<String> unavailableNames;


    Button createChartButton;

    public CreateChartFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        fab = (FloatingActionButton) getActivity().findViewById(R.id.button);
        fab.hide();
        chartListViewModel = ViewModelProviders.of(this).get(ChartListViewModel.class);

        chartTitleEditText = (EditText) getActivity().findViewById(R.id.chart_title_edit_text);

        chartTitleEditText.addTextChangedListener(this);


        createChartButton = (Button) view.findViewById(R.id.create_chart_button);
        createChartButton.setEnabled(false);
        createChartButton.setOnClickListener(v -> {

            String chartTitle = chartTitleEditText.getText().toString();
            chartListViewModel.addChart(new ChartEntity(chartTitle, "descr", "color", 1));
            hideKeyboard(getActivity());
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

        });

        unavailableNames = new ArrayList<>();


        chartListViewModel.getCharts().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(charts -> {
                    for (ChartEntity entity : charts)
                        unavailableNames.add(entity.getTitle());
                        afterTextChanged(chartTitleEditText.getText());

                });

    }


    @Override
    public void onPause() {
        super.onPause();
        fab.show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_chart, container, false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.i("EditText", s.toString());
        createChartButton.setEnabled(!unavailableNames.contains(s.toString()));

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
