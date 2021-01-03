package com.codinghelper.mscii;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;


public class Help_and_feedback extends Fragment {

    private View v;
    private EditText editTextMessage;
    private TextView editTextTo;
    private ExtendedFloatingActionButton send_chip;
    private String subject;
    private String[] feedbackType = {"General Feedback","Bug Report","Suggestion","Other"};


    public Help_and_feedback() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_help_and_feedback, container, false);

        editTextTo=(TextView) v.findViewById(R.id.developer_Email_ID);
        editTextMessage=(EditText) v.findViewById(R.id.editText3);

        //Session Spinner
        Spinner feedbackType_spinner = (Spinner) v.findViewById(R.id.feedbackType_spinner);
        ArrayAdapter<String> feedbackAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, feedbackType);
        feedbackAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        feedbackType_spinner.setAdapter(feedbackAdapter);
        feedbackType_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), subject, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        send_chip=(ExtendedFloatingActionButton) v.findViewById(R.id.send_chip);
        send_chip.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                String[] to = {editTextTo.getText().toString()};
                String message=editTextMessage.getText().toString();

                if (message.equals("")){
                    editTextMessage.setError("Message can't be empty");
                }else {
                    editTextMessage.setError(null);
                    Intent email = new Intent(Intent.ACTION_SENDTO);
                    email.setData(Uri.parse("mailto:"));
                    email.putExtra(Intent.EXTRA_EMAIL, to);
                    email.putExtra(Intent.EXTRA_SUBJECT, subject);
                    email.putExtra(Intent.EXTRA_TEXT, message);
                    startActivity(Intent.createChooser(email, "Choose one application"));

                }

            }

        });

        return v;
    }
}