package com.example.administrator.bottom.custom;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2018\2\7 0007.
 */

public class MultiSwipeRefreshLayout extends SwipeRefreshLayout {
    public MultiSwipeRefreshLayout(Context context, float startY, float startX) {
        super(context);
        this.startY = startY;
        this.startX = startX;
    }

    private float startY;
    private float startX; // 记录viewPager是否拖拽的标记
    private boolean mIsBeingDragged;
    private int mTouchSlop;
    private View mSwipeableChildren;

    public MultiSwipeRefreshLayout(Context context) {
        this(context, null);
    }

    public MultiSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float currentX = ev.getX();
        float currentY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = currentY;
                startX = currentX;
                mIsBeingDragged = false;
                break;
            case MotionEvent.ACTION_MOVE: //如果viewpager正在拖拽中，那么不拦截它的事件，直接return false；
                if (mIsBeingDragged) {
                    return false;
                }
                float dx = Math.abs(currentX - startX);
                float dy = Math.abs(currentY - startY); // 如果X轴位移大于Y轴位移，那么将事件交给viewPager处理。
                if (dx > dy && dx > mTouchSlop) {
                    mIsBeingDragged = true;
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                break;
        } // 如果是Y轴位移大于X轴，事件交给swipeRefreshLayout处理。
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 传入SwipeRefreshLayout的Target的id，如R.id.viewPager * swipeRefreshLayout.setSwipeableChildren(R.id.viewPager); * @param ids
     */
    public void setSwipeableChildren(final int ids) {
        mSwipeableChildren = findViewById(ids);
    }

    @Override
    public boolean canChildScrollUp() {
        return canViewScrollUp(mSwipeableChildren);
    }

    /**
     * 根据传入的ViewPager，查找内部的ListView，并判断其是否到顶部 * @param view * @return
     */
    private static boolean canViewScrollUp(View view) {
        if (view != null && view instanceof ViewPager) {
            for (int i = 0; i < ((ViewPager) view).getChildCount(); i++) {
                View child = ((ViewPager) view).getChildAt(i);
                if (child.isShown()) {
                    if (child instanceof RelativeLayout) {
                        View subChild = ((RelativeLayout) child).getChildAt(0);
                        if (subChild instanceof AbsListView) {
                            final AbsListView listView = (AbsListView) subChild;
                            return ViewCompat.canScrollVertically(listView, -1);
                        }
                    }
                }
            }
        }
        return false;
    }
}


