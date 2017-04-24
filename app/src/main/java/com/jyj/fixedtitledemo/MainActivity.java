package com.jyj.fixedtitledemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ScrollView553.OnScrollListener {
    ScrollView553 scrollview;
    ImageView imgs;
    private RelativeLayout activity_main;
    private  int move_range;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        activity_main = (RelativeLayout) findViewById(R.id.activity_main);
        scrollview = (ScrollView553) findViewById(R.id.scrollview);
        scrollview.setOnScrollListener(this);
        imgs = (ImageView) findViewById(R.id.imgs);
        title = (TextView) findViewById(R.id.title);
        activity_main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                onScroll(scrollview.getScrollY());

            }
        });
        activity_main.post(new Runnable() {
            @Override
            public void run() {
                move_range = imgs.getBottom();

            }
        });
    }

    @Override
    public void onScroll(int scrollY) {
        int offsetY = -scrollY + move_range;
        if (offsetY>=0){
                title.setTranslationY(offsetY);
        }else {
            title.setTranslationY(0);
        }
    }

    @Override
    public void onBottomArrived() {

    }

    @Override
    public void onScrollStateChanged(ScrollView553 view, int scrollState) {

    }
}
