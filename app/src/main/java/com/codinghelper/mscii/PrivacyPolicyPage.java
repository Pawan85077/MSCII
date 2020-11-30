package com.codinghelper.mscii;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class PrivacyPolicyPage extends AppCompatActivity {

    private TextView text1;
    private TextView text2;
    private TextView text3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy_page);
        text1=(TextView)findViewById(R.id.termsOfUse);
        text2=(TextView)findViewById(R.id.privacyPolicy);
        text3=(TextView)findViewById(R.id.iAgree);

        text1.setMovementMethod(LinkMovementMethod.getInstance());
        text2.setMovementMethod(LinkMovementMethod.getInstance());

        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrivacyPolicyPage.this, First.class));
            }
        });
    }
}