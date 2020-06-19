package com.bigkoo.custompickerview.contrarywind.listener;

import android.view.MotionEvent;

import com.bigkoo.custompickerview.contrarywind.view.CustomWheelView;


/**
 * 手势监听
 */
public final class LoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    private final CustomWheelView wheelView;


    public LoopViewGestureListener(CustomWheelView wheelView) {
        this.wheelView = wheelView;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        wheelView.scrollBy(velocityY);
        return true;
    }
}
