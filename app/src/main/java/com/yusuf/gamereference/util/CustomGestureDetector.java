package com.yusuf.gamereference.util;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class CustomGestureDetector
        implements
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    public void onSwipeDown() {}

    public void onSwipeLeft() {}

    public void onSwipeRight() {}

    public void onSwipeUp() {}

    @Override
    public boolean onDown(MotionEvent e) {return true;}

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {return true;}

    @Override
    public boolean onSingleTapUp(MotionEvent e) {return true;}

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onDoubleTap(MotionEvent e) {return true;}

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {return true;}

    @Override
    public void onLongPress(MotionEvent e) {}

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {return true;}

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result = false;
        try {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight();
                    } else {
                        onSwipeLeft();
                    }
                }
            } else {
                if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeDown();
                    } else {
                        onSwipeUp();
                    }
                }
            }
        } catch (Exception exception) {exception.printStackTrace();}
        return result;
    }
}
