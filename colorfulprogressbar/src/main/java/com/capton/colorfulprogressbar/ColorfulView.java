package com.capton.colorfulprogressbar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.view.ViewGroup;


/**
 *这是一个绘制了多个等腰直角三角形的View，呈现出一个由很多个斜条纹组成的双色画布
 * 可以自行通过new ColorfulView(Context context,int width,Paint paint,Paint paint2)方法，构建出来看效果
 * Created by capton on 2017/8/10.
 */

public class ColorfulView extends View  {
    private Paint paint;
    private Paint paint2;
    private int mWidth;

    public ColorfulView(Context context,int width,Paint paint,Paint paint2) {
        super(context);
        mWidth=width;
        this.paint=paint;
        this.paint2=paint2; ;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth,mWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x,y;
        float x2,y2;
         for (int i = 20; i > 0; i--) {
             Path p1=new Path();
            x=y=((float)mWidth/20)*i;
             p1.lineTo(0,y);
             p1.lineTo(x,0);
             p1.lineTo(0,0);
             p1.close();
            if(i%2==0) {
                canvas.drawPath(p1, paint);
            }else {
                canvas.drawPath(p1, paint2);
            }
        }
        for (int i = 0; i < 20; i++) {
             Path p2=new Path();
            x2=y2=((float)mWidth/20)*i;
            p2.moveTo(mWidth,mWidth);
            p2.lineTo(mWidth,y2);
            p2.lineTo(x2,mWidth);
            p2.lineTo(mWidth,mWidth);
            p2.close();
            if(i%2!=0) {
                canvas.drawPath(p2, paint);
            }else {
                canvas.drawPath(p2, paint2);
            }
        }
    }


}
