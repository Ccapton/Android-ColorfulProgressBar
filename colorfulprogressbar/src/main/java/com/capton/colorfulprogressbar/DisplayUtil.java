package com.capton.colorfulprogressbar;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by CAPTON on 2017/1/11.
 */


public class DisplayUtil {
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /*
       * 获取设备的屏幕高度（px）
       * */
    public  static int getScreenHeightPx(Context context){
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display=windowManager.getDefaultDisplay();
        DisplayMetrics metrics=new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }
    /*
   * 获取设备的屏幕高度（dp）
   * */
    public  static int getScreenHeightDp(Context context){
        return px2dip(context,getScreenHeightPx(context));
    }

    /*
   * 获取设备的屏幕宽度（px）
   * */
    public static int getScreenWidthPx(Context context){
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display=windowManager.getDefaultDisplay();
        DisplayMetrics metrics=new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.widthPixels;
    }
    /*
   * 获取设备的屏幕宽度（dp）
   * */
    public  static int getScreenWidthDp(Context context){
        return px2dip(context,getScreenWidthPx(context));
    }


}