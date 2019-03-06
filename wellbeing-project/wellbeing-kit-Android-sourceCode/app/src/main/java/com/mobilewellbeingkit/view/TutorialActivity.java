package com.mobilewellbeingkit.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.mobilewellbeingkit.R;
import com.mobilewellbeingkit.adapter.TutorialImageAdapter;

public class TutorialActivity extends Activity {

    ViewPager viewPager;
    TutorialImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        adapter = new TutorialImageAdapter(this);
        viewPager.setAdapter(adapter);
    }
}
