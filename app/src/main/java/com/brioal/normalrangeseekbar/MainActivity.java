package com.brioal.normalrangeseekbar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.brioal.seekbar.OnRangeChangedListener;
import com.brioal.seekbar.RangeBar;


public class MainActivity extends AppCompatActivity {
    private RangeBar mRangeBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRangeBar = (RangeBar) findViewById(R.id.main_rengae);
        mRangeBar.setBeginValue(20);
        mRangeBar.setFinishValue(50);
        mRangeBar.setIndex("%");
        mRangeBar.setInitValue(23, 40);
        mRangeBar.setRangeChangeListener(new OnRangeChangedListener() {
            @Override
            public void selected(int start, int end) {
                Log.i("Range:", start + ":" + end);
            }
        });
    }
}
