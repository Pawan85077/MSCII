package com.codinghelper.mscii;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }

//    public void onBackPressed() {
//        if (getActivity() != null) {
////            getActivity().finish();
//            AlertDialog.Builder alertDialogueBuilder = new AlertDialog.Builder(getContext());
//            alertDialogueBuilder.setTitle("Confirm Exit..!!");
//            alertDialogueBuilder.setMessage("Are you sure want to exit");
//            alertDialogueBuilder.setCancelable(false);
//            alertDialogueBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    getActivity().finish();
//                    Toast.makeText(getActivity(),"Developed by team SUPRD",Toast.LENGTH_SHORT).show();
//                }
//            });
//            alertDialogueBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    Toast.makeText(getActivity(),"You clicked NO",Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }


}
