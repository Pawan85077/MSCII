package com.codinghelper.mscii;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoardingStepper extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dots;
    private TextView[] nDots;
    private OnBoardingSliderAdapter obsa;

    private Button back;
    private Button next;

    private  int nCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_stepper);


        viewPager=(ViewPager)findViewById(R.id.viewPager);
        dots=(LinearLayout)findViewById(R.id.dots);

        back= (Button)findViewById(R.id.previous2);
        next= (Button)findViewById(R.id.next2);

        obsa= new OnBoardingSliderAdapter(this);
        viewPager.setAdapter(obsa);

        addDotsIndicator(0);
        viewPager.addOnPageChangeListener(viewListener);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(nCurrentPage+1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(nCurrentPage-1);
            }
        });
    }

    public void addDotsIndicator(int position){
        nDots= new TextView[3];
        for(int i=0; i<nDots.length; i++){
            nDots[i]= new TextView(this);
            nDots[i].setText(Html.fromHtml("&#8226;"));
            nDots[i].setTextSize(35);
            nDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite2));
            dots.addView(nDots[i]);
        }

        if(nDots.length >0 ){
            nDots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }
    ViewPager.OnPageChangeListener viewListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);

            nCurrentPage=position;
            if(position == 0){
                next.setEnabled(true);
                back.setEnabled(false);
                back.setVisibility(View.INVISIBLE);

                next.setText("Next");
                back.setText("");
            }else if (position == nDots.length-1){
                next.setEnabled(true);
                back.setEnabled(true);
                back.setVisibility(View.VISIBLE);

                next.setText("Finish");
                back.setText("Back");

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(OnBoardingStepper.this, PrivacyPolicyPage.class));
                    }
                });
            }else{
                next.setEnabled(true);
                back.setEnabled(true);
                back.setVisibility(View.VISIBLE);

                next.setText("Next");
                back.setText("Back");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}