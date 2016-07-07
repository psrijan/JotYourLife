package com.mycompany.jotyourlife.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycompany.jotyourlife.R;

/**
 * Created by Shristi on 1/25/2016.
 */
public class InspirationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.journey_fragment , container, false);
        return view;
    }
}
