package com.codinghelper.mscii;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class dashboardFragment extends Fragment {


    public dashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final LinearLayout topMessage = (LinearLayout) rootView.findViewById(R.id.topMessage);
        final Button okbutton = (Button) rootView.findViewById(R.id.okSecure);
        okbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                topMessage.setVisibility(View.GONE);
            }
        });
        return rootView;
    }

}
