//package iqra.shabeer.fragment;
//
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//import android.view.View;
//
///**
// * Created by Devprovider on 02/05/2017.
// */
//
//public class TempCode {
//
//    public static Bitmap getBitmapFromView(View view) {
//        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(returnedBitmap);
//        Drawable bgDrawable = view.getBackground();
//        if (bgDrawable != null) {
//            bgDrawable.draw(canvas);
//        } else {
//            canvas.drawColor(Color.WHITE);
//        }
//        view.draw(canvas);
//        return returnedBitmap;
//    }
//
//    public Bitmap getBitmapFromViewNew(View pView) {
//
//        pView.setDrawingCacheEnabled(true);
//        pView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//
//        pView.layout(0, 0, pView.getWidth(), pView.getHeight());
//        pView.buildDrawingCache(true);
//
//        Bitmap bitmap = Bitmap.createScaledBitmap(pView.getDrawingCache(), pView.getWidth(), pView.getHeight(), false);
//        pView.setDrawingCacheEnabled(false);
//
//        return bitmap;
//
//    }
//}
