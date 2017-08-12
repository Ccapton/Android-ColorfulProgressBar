package com.capton.colorfulprogressbar;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
/**
 * Created by capton on 2017/8/10.
 */

public class ColorfulProgressbar extends ViewGroup {

    public static final String STYLE_NORMAL="normal";  //正常单色样式
    public static final String STYLE_COLORFUL="colorful"; //双色样式
    public   String style="colorful";

    private ColorfulView colofulView;     //双色View
    private TextView progressView;     // 第二进度条
    private TextView backgroundMaskView;     // 背景罩
    private TextView maskView;         // 进度条白色渐变图层
    private TextView percentView;       //文字显示进度层
    private Paint progressPaint=new Paint();  //颜色一画笔
    private Paint progressPaint2=new Paint();  //颜色二画笔
    private Paint backgroundPaint=new Paint();  //背景画笔

    private  int maxHeight;  //ColorfulProgressbar高度最大值
    private  int mHeight;     //ColorfulProgressbar高度
    private  int mWidth;      //ColorfulProgressbar宽度

    private  long progress;     //进度值
    private  long secondProgress;   //第二进度值
    private  long maxProgress=100;  //默然最大进度100
    private  int backgroundColor=getResources().getColor(R.color.progressBg);    //背景颜色
    private  int secondProgressColor=getResources().getColor(R.color.secondProgressColor);  //第二进度条颜色
    private  int progressColor=getResources().getColor(R.color.colorAccent);    //进度条颜色一
    private  int progressColor2=getResources().getColor(R.color.ltcolorAccent);  //进度条颜色二
    private  int percentColor=Color.DKGRAY;          //进度文字的颜色，默认暗灰色
    private  int percentShadeColor=Color.WHITE;   //进度文字的阴影颜色，默认白色

    private TranslateAnimation translateAnimation; //双色进度条的动画
    private boolean animationOn=true;      //动画开启的标志位
    private boolean animationCancle;         //动画取消的标志位

    private boolean showPercent=true; // 是否显示进度文字的标志位
    private boolean setBackgroudColor; // 是否改变背景颜色的标志位

    public ColorfulProgressbar(Context context) {
        this(context,null);
    }
    public ColorfulProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public ColorfulProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);  //自定义ViewGroup，默认不调用onDraw方法，而这里有很多步骤需要在ondraw中操作，所以调用setWillNotDraw（false）
        mHeight=DisplayUtil.dip2px(context,4); //默认进度条高度为4dp
        getParameter(context,attrs);
    }

    /**
     * 从xml中获取各个属性
     * @param context
     * @param attrs
     */
    private void getParameter(Context context, AttributeSet attrs){
        if(attrs!=null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ColorfulProgressbar);
            style = ta.getString(R.styleable.ColorfulProgressbar_style);
            if (!STYLE_NORMAL.equals(style) && !STYLE_COLORFUL.equals(style)) {
                style = STYLE_COLORFUL;  //如果没有在xml中显示设置style，默认使用双色进度条
            }
            progress = ta.getInteger(R.styleable.ColorfulProgressbar_progress, (int)progress);
            secondProgress = ta.getInteger(R.styleable.ColorfulProgressbar_secondProgress,(int)secondProgress);
            maxProgress = ta.getInteger(R.styleable.ColorfulProgressbar_max, (int) maxProgress);
            backgroundColor = ta.getColor(R.styleable.ColorfulProgressbar_backgroundColor, backgroundColor);
            progressColor = ta.getColor(R.styleable.ColorfulProgressbar_progressColor1, progressColor);
            progressColor2 = ta.getColor(R.styleable.ColorfulProgressbar_progressColor2, progressColor2);
            ta.recycle();
            partition2= (float)this.progress/maxProgress; //进度条百分比
            partition= (float)this.secondProgress/maxProgress; //第二进度条百分比
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);

        widthSize=widthMode==MeasureSpec.EXACTLY?widthSize:DisplayUtil.dip2px(getContext(),200);
        heightSize=heightMode==MeasureSpec.EXACTLY?heightSize:DisplayUtil.dip2px(getContext(),4);

        /*
        * 当你设置高度大于20dp时，强制高度变为20dp,太高了不美观。
        * */
        maxHeight=DisplayUtil.dip2px(getContext(),20);
        if(mHeight>maxHeight) {
            mHeight = maxHeight;
        }
        /*
        * 设置高度
        * */
        if(mHeight>0){
            heightSize=mHeight;
        }

        /*
        * 在高度小于10dp时，强制不能使用文字显示进度，因为高度实在是太小了，在这个高度下字体看不清楚，放在进度条外又不美观，只好折中设计了。
        * */
        if(mHeight<DisplayUtil.dip2px(getContext(),10)){
            showPercent=false;
        }

        /*
        * 设置宽度
        * */
        if(mWidth>0){
            widthSize=mWidth;
        }
        setMeasuredDimension(widthSize,heightSize); //确定主视图宽高

    }

    boolean once;
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

         if(!once) {
             progressPaint.setColor(progressColor);
             progressPaint2.setColor(progressColor2);

             progressPaint.setAntiAlias(true);
             progressPaint2.setAntiAlias(true);

             progressView = new TextView(getContext());
             progressView.setWidth(getMeasuredWidth());
             progressView.setHeight(getMeasuredHeight());
             progressView.setBackgroundColor(secondProgressColor);

             backgroundMaskView= new TextView(getContext());
             backgroundMaskView.setWidth(getMeasuredWidth());
             backgroundMaskView.setHeight(getMeasuredHeight());
             backgroundMaskView.setBackgroundResource(R.drawable.background);

             switch (style) {
                 case STYLE_COLORFUL:
                     colofulView = new ColorfulView(getContext(), getMeasuredWidth(), progressPaint, progressPaint2);
                     break;
                 case STYLE_NORMAL:
                     colofulView = new ColorfulView(getContext(), getMeasuredWidth(), progressPaint, progressPaint);
                     break;
             }

             percentView = new TextView(getContext());
             percentView.setText((int)((float)partition2*100)+"%");
             percentView.setTextSize(DisplayUtil.px2sp(getContext(), (float) (getMeasuredHeight()*0.8)));
             percentView.setGravity(Gravity.CENTER);
             percentView.setShadowLayer(2,1,2,percentShadeColor);
              percentView.setTextColor(percentColor);
              percentView.measure(0,0);
             int textWidth = percentView.getMeasuredHeight()*2;
             int textHeight = percentView.getMeasuredHeight();

             maskView = new TextView(getContext());
             maskView.setWidth(getMeasuredWidth());
             maskView.setHeight(getMeasuredHeight() * 2 / 3);
             maskView.setBackgroundResource(R.drawable.progress_mask);

             /*
             * 依次添加第二进度条，背景罩，双色进度条（第一进度条），白色渐变层，百分比文字显示层等四个子View
             * */
             addView(progressView);
             addView(backgroundMaskView);
             addView(colofulView);
             addView(maskView);
             addView(percentView);

             getChildAt(0).layout(0, 0, getMeasuredWidth(), getMeasuredHeight()); //布局第二进度条位置
             getChildAt(1).layout(0, 0, getMeasuredWidth(), getMeasuredHeight()); //布局背景罩

             int ChildHeight = getMeasuredWidth();
             getChildAt(2).layout(0, -ChildHeight + getMeasuredHeight(), getMeasuredWidth(), getMeasuredWidth()); //布局双色进度条
             /*
             * 根据标识位，为双色进度条设置位移动画（无限向上移动，视觉上达到斜条向右移动的效果）
             * */
             if (animationOn) {
                 translateAnimation = new TranslateAnimation(0, 0, 0, ChildHeight - getMeasuredHeight());
                 translateAnimation.setDuration((long) (8000 * (float) getMeasuredWidth() / DisplayUtil.getScreenWidthPx(getContext())));
                 translateAnimation.setRepeatCount(-1);
                 translateAnimation.setInterpolator(new LinearInterpolator());
                 getChildAt(2).setAnimation(translateAnimation);
                 translateAnimation.start();
             }

             getChildAt(3).layout(0, 0, getMeasuredWidth(), getMeasuredHeight() * 2 / 3); //布局白色渐变层

             getChildAt(4).layout(0, 0, textWidth,textHeight); //布局百分比文字显示层
             /*
             * 根据标志位，确定是否显示百分比文字显示层。
             * */
             if(showPercent){
                 getChildAt(4).setVisibility(VISIBLE);
             }else {
                 getChildAt(4).setVisibility(GONE);
             }

             /*
             *  设置默认背景图，你当然也可以使用纯色的资源。这里我用了一个黑色透明渐变的背景，呈现一个由阴影效果的凹槽
             * */
             setBackgroundResource(R.drawable.background);
             once=true;
         }
    }


    public void showPercentText(boolean showPercent){
        this.showPercent=showPercent;
    }

    public int getSecondProgressColor() {
        return secondProgressColor;
    }

    public void setSecondProgressColor(int secondProgressColor) {
        this.secondProgressColor = secondProgressColor;
    }
    public void setSecondProgressColorRes(int secondProgressColorRes) {
        this.secondProgressColor =  getResources().getColor(secondProgressColorRes);
    }

    public int getPercentColor() {
        return percentColor;
    }

    public void setPercentColorRes(int percentColorRes) {
        this.percentColor = getResources().getColor(percentColorRes);
    }

    public int getPercentShadeColor() {
        return percentShadeColor;
    }

    public void setPercentShadeColor(int percentShadeColor) {
        this.percentShadeColor = percentShadeColor;
    }
    public void setPercentShadeColorRes(int percentShadeColorRes) {
        this.percentShadeColor = getResources().getColor(percentShadeColorRes);
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }
    public void setProgressColorRes(int progressColorRes) {
        this.progressColor = getResources().getColor(progressColorRes);
    }

    public int getProgressColor2() {
        return progressColor2;
    }

    public void setProgressColor2(int progressColor2) {
        this.progressColor2 = progressColor2;
    }
    public void setProgressColor2Res(int progressColor2Res) {
        this.progressColor2 = getResources().getColor(progressColor2Res);
    }

    public void setAnimation(boolean animationOn){
       this.animationOn=animationOn;
   }

    public long getSecondProgress() {
        return secondProgress;
    }
    private float partition;
    public void setSecondProgress(long secondProgress) {
        this.secondProgress = secondProgress;
        partition= (float)this.secondProgress/maxProgress;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        setBackgroudColor=true;
    }

    public void setBackgroundColorRes(int backgroundColorRes) {
        this.backgroundColor = getResources().getColor(backgroundColorRes);
        setBackgroudColor=true;
    }

    public void setHeight(int height){
        mHeight=height;
    }
    public void setWidth(int width){
        mWidth=width;
    }

    public void setMaxProgress(long progress){
        maxProgress=progress;
    }

    public long getMaxProgress(){
       return maxProgress;
    }

    private float partition2;
    public void setProgress(long progress){
        this.progress=progress;
        partition2= (float)this.progress/maxProgress;
    }

    public long getProgress(){
        return this.progress;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
            if (getChildAt(0) != null) {
                int moveX = getMeasuredWidth() - (int) (partition * getMeasuredWidth());
                getChildAt(0).setX(-moveX);
            }
            if (getChildAt(2) != null) {
                int moveX = getMeasuredWidth() - (int) (partition2 * getMeasuredWidth());
                getChildAt(2).setX(-moveX);
            }

            if (getChildAt(3) != null) {
                int moveX = getMeasuredWidth() - (int) (partition2 * getMeasuredWidth());
                getChildAt(3).setX(-moveX);
            }
            if (getChildAt(4) != null) {

                if(getChildAt(2).getX()+getMeasuredWidth()>getChildAt(4).getMeasuredHeight()*2) {
                    getChildAt(4).setX(getChildAt(2).getX()+getMeasuredWidth()-getChildAt(4).getMeasuredHeight()*2);
                }
                percentView.setText((int) ((float) partition2 * 100) + "%");

                 /*
                 * 根据标志位，确定是否显示百分比文字显示层。
                 * */
                if(showPercent){
                    getChildAt(4).setVisibility(VISIBLE);
                }else {
                    getChildAt(4).setVisibility(GONE);
                }
            }

            if (!animationOn) {
                if (translateAnimation != null) {
                    translateAnimation.cancel();
                    animationCancle = true;
                }
            } else {
                if (animationCancle) {
                    Log.w("onDraw", "translateAnimation  animationCancle");
                    translateAnimation.reset();
                    getChildAt(1).setAnimation(translateAnimation);
                    translateAnimation.startNow();
                    animationCancle = false;
                }
            }
            if(setBackgroudColor) {
                backgroundPaint.setAntiAlias(true);
                backgroundPaint.setColor(backgroundColor);
                canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), backgroundPaint);
            }
    }
}

