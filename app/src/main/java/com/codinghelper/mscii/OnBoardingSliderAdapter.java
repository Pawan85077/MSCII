package com.codinghelper.mscii;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

class OnBoardingSliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public OnBoardingSliderAdapter(Context context)
    {
        this.context= context;
    }

    //Array for multiple slider/steppers images
    public int[] slide_images={
            R.drawable.stay_connected,
            R.drawable.stay_updated2,
            R.drawable.have_fun
    };
    //Array for multiple slider/steppers headings

    public String[] slide_headings={
            "Stay Connected",
            "Stay Updated",
            "Have Fun!!!"
    };

    //Array for multiple slider/steppers description
    public String[] slide_description={
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
    };

    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view== (RelativeLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= layoutInflater.inflate(R.layout.onboarding_slide_layout, container, false);

        ImageView slideImageView= (ImageView) view.findViewById(R.id.imageView4);
        TextView slideHeading= (TextView) view.findViewById(R.id.textView8);
        TextView slideDescription= (TextView) view.findViewById(R.id.textView9);

        slideImageView.setImageResource(slide_images[position]);
        slideHeading.setText(slide_headings[position]);
        slideDescription.setText(slide_description[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
