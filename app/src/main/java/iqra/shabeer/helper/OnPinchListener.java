package iqra.shabeer.helper;

import android.view.ScaleGestureDetector;

import iqra.shabeer.R;

/**
 * Created by Iqra on 4/11/2017.
 */

class OnPinchListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
{
    float currentSpan;
    float startFocusX;
    float startFocusY;

    public boolean onScaleBegin(ScaleGestureDetector detector)
    {
        currentSpan = detector.getCurrentSpan();
        startFocusX = detector.getFocusX();
        startFocusY = detector.getFocusY();
        return true;
    }
//
//    public boolean onScale(ScaleGestureDetector detector)
//    {
//        ZoomableRelativeLayout zoomableRelativeLayout= (ZoomableRelativeLayout) ImageFullScreenActivity.this.findViewById(R.id.imageWrapper);
//
//        zoomableRelativeLayout.relativeScale(detector.getCurrentSpan() / currentSpan, startFocusX, startFocusY);
//
//        currentSpan = detector.getCurrentSpan();
//
//        return true;
//    }
//
//    public void onScaleEnd(ScaleGestureDetector detector)
//    {
//        ZoomableRelativeLayout zoomableRelativeLayout= (ZoomableRelativeLayout) ImageFullScreenActivity.this.findViewById(R.id.imageWrapper);
//
//        zoomableRelativeLayout.release();
//    }
}
