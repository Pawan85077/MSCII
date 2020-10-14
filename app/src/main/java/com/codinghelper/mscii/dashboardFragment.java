package com.codinghelper.mscii;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;




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
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Handling Expand and Collapse
        RelativeLayout UtilityDocHeader = (RelativeLayout) rootView.findViewById(R.id.utilityDocHeader);
        final HorizontalScrollView UtilityDocScroll = (HorizontalScrollView) rootView.findViewById(R.id.utilityDocCardScroll);
        final ImageView UtilityExpandCollapse = (ImageView) rootView.findViewById(R.id.utilityExpandCollapse);
        UtilityDocHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UtilityDocScroll.getVisibility() == View.GONE){
                    UtilityDocScroll.setVisibility(View.VISIBLE);
                    UtilityExpandCollapse.setBackgroundResource(R.drawable.ic_colapse);
                }else {
                    UtilityDocScroll.setVisibility(View.GONE);
                    UtilityExpandCollapse.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });

        RelativeLayout TenthDocHeader = (RelativeLayout) rootView.findViewById(R.id.tenthDocHeader);
        final HorizontalScrollView TenthDocScroll = (HorizontalScrollView) rootView.findViewById(R.id.tenthDocCardScroll);
        final ImageView TenthExpandCollapse = (ImageView) rootView.findViewById(R.id.tenthExpandCollapse);
        TenthDocHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TenthDocScroll.getVisibility() == View.GONE){
                    TenthDocScroll.setVisibility(View.VISIBLE);
                    TenthExpandCollapse.setBackgroundResource(R.drawable.ic_colapse);
                }else {
                    TenthDocScroll.setVisibility(View.GONE);
                    TenthExpandCollapse.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });

        RelativeLayout TwelfthDocHeader = (RelativeLayout) rootView.findViewById(R.id.twelfthDocHeader);
        final HorizontalScrollView TwelfthDocScroll = (HorizontalScrollView) rootView.findViewById(R.id.twelfthDocCardScroll);
        final ImageView TwelfthExpandCollapse = (ImageView) rootView.findViewById(R.id.twelfthExpandCollapse);
        TwelfthDocHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TwelfthDocScroll.getVisibility() == View.GONE){
                    TwelfthDocScroll.setVisibility(View.VISIBLE);
                    TwelfthExpandCollapse.setBackgroundResource(R.drawable.ic_colapse);
                }else {
                    TwelfthDocScroll.setVisibility(View.GONE);
                    TwelfthExpandCollapse.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });

        RelativeLayout GraduationDocHeader = (RelativeLayout) rootView.findViewById(R.id.graduationDocHeader);
        final HorizontalScrollView GraduationDocScroll = (HorizontalScrollView) rootView.findViewById(R.id.graduationDocCardScroll);
        final ImageView GraduationExpandCollapse = (ImageView) rootView.findViewById(R.id.graduationExpandCollapse);
        GraduationDocHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GraduationDocScroll.getVisibility() == View.GONE){
                    GraduationDocScroll.setVisibility(View.VISIBLE);
                    GraduationExpandCollapse.setBackgroundResource(R.drawable.ic_colapse);
                }else {
                    GraduationDocScroll.setVisibility(View.GONE);
                    GraduationExpandCollapse.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });





        //initToolbar();
        getActivity().setTitle("Dashboard");
        return rootView;
    }


//    private void initToolbar() {
//        Toolbar toolbar = (Toolbar)getView().findViewById(R.id.toolbar);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Tools.setSystemBarColor(this);
//    }

}
