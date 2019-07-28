package com.circles.circlesapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.circles.circlesapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityCountryPickerFragment extends Fragment {


    public CityCountryPickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city_country_picker, container, false);
    }

}
