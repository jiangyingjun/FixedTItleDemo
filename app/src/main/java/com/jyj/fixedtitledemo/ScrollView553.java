package com.jyj.fixedtitledemo;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;


public class ScrollView553 extends ScrollView {

    private final String TAG=getClass().getName();
    private OnScrollListener mOnScrollListener;


    // 检查ScrollView的最终状态
    private static final int CHECK_STATE = 0;
    // 是否在触摸状态
    private boolean inTouch = false;
    // 上次滑动的最后位置
    private int lastT = 0;
    /**
     * The view is not scrolling. Note navigating the list using the
     * trackball counts as being in the idle state since these transitions
     * are not animated.
     */
    public  static  int SCROLL_STATE_IDLE = 0;

    /**
     * The user is scrolling using touch, and their finger is still on the
     * screen
     */
    public  static int SCROLL_STATE_TOUCH_SCROLL = 1;

    /**
     * The user had previously been scrolling using touch and had performed
     * a fling. The animation is now coasting to a stop
     */
    public  static int SCROLL_STATE_FLING = 2;

    public ScrollView553(Context context) {
        this(context,null);
    }
    public ScrollView553(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public ScrollView553(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }


    /***
     * 处理手离开屏幕 scrollView 还在滚动的情况
     * */
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(android.os.Message  msg) {
            if (lastT == getScrollY()) {
                // 如果上次的位置和当前的位置相同，可认为是在空闲状态
//                Log.e(TAG, "SCROLL_STATE_IDLE");
                mOnScrollListener.onScrollStateChanged(ScrollView553.this,
                        SCROLL_STATE_IDLE);
                if (getScrollY() + getHeight() >= computeVerticalScrollRange()) {
                    mOnScrollListener.onBottomArrived();
                } else {
                    Log.d(TAG, "没有到最下方");
                }
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                inTouch = true;
                if (getScrollY() + getHeight() >= computeVerticalScrollRange()) {
                    mOnScrollListener.onBottomArrived();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                inTouch = false;

                lastT = getScrollY();
                handler.removeMessages(CHECK_STATE);// 确保只在最后一次做这个check
                handler.sendEmptyMessageDelayed(CHECK_STATE, 5);// 5毫秒检查一下

                break;
        }
        return super.onTouchEvent(ev);
    }

    /**published scrolllistener interface*/
    public void setOnScrollListener(OnScrollListener OnScrollListener){
        this.mOnScrollListener=OnScrollListener;
    }
    /**
     *  scroll listener callback
     * */

    public interface OnScrollListener{
         void  onScroll(int scrollY);


        /**
         * 滑动到底部回调
         */
        public void onBottomArrived();

        /**
         * 滑动状态回调
         *
         * @param view
         *            当前的scrollView
         * @param scrollState
         *            当前的状态
         */
        public void onScrollStateChanged(ScrollView553 view,
                                         int scrollState);

    }





    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollListener==null){
            return;
        }

        if (inTouch) {
            if (t != oldt) {
                // 有手指触摸，并且位置有滚动
                Log.i(TAG, "SCROLL_STATE_TOUCH_SCROLL");
                mOnScrollListener.onScrollStateChanged(this,
                        SCROLL_STATE_TOUCH_SCROLL);
            }
        } else {
            if (t != oldt) {
                // 没有手指触摸，并且位置有滚动，就可以简单的认为是在fling
                Log.w(TAG, "SCROLL_STATE_FLING");
                mOnScrollListener.onScrollStateChanged(this,
                        SCROLL_STATE_FLING);
                // 记住上次滑动的最后位置
                lastT = t;
                handler.removeMessages(CHECK_STATE);// 确保只在最后一次做这个check
                handler.sendEmptyMessageDelayed(CHECK_STATE, 5);// 5毫秒检查一下
            }
        }

        if (t>=0){
            mOnScrollListener.onScroll(t);
        }

    }


    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);

    }
}
