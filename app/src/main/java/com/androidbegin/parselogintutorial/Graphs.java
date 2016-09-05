package com.androidbegin.parselogintutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Emi on 21.06.2015.
 */
public class Graphs extends Fragment  {
    LinearLayout layout;
    ParseApplication globalVariable;
    TextView breathing_instruction;
    Chronometer focus;
    Button start;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.breathing_guide, container, false);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

            super.onActivityCreated(savedInstanceState);
    }
}
