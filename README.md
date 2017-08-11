# Android-ColorfulProgressBar

> 关于我，欢迎关注  
  博客：ccapton(http://blog.csdn.net/ccapton) 微信：[Ccapton]()   
  
### 简介: 

这是一个自定义的Progressbar，效果看着还行吧，滚动的双色斜条作为进度条，有点类似Bootstrap风格。原生Progress的基本操作都有，自行观摩我的源码吧，挺简单的。

### 示例:  

![](https://raw.githubusercontent.com/Ccapton/Android-ColofulProgressBar/master/ColorfulProgressDemo.gif)

### 演示Demo

demo下载：
https://github.com/Ccapton/Android-ColorfulProgressBar/blob/master/ColorfulProgressDemo.apk

### 特性 
与原生Progress相比，感觉更漂亮一点，可以显示进度值，背景凹凸感明显，进度条效果更加立体。

### 原理说明
额，挺简单的。不过感觉我的做法有点复杂了，我先自定义了一个View，专门作为进度条的显示图层,如下所示

![](https://raw.githubusercontent.com/Ccapton/Android-ColorfulProgressBar/master/ColorfulView.jpg)

然后将其布局在高度不超过20dp的ColorfulProgressBar父布局中，设置Y方向的偏移量，然后动画循环改变Y坐标，实现斜条滚动的动画效果，当你调用setProgress方法时，则改变其在父布局的X坐标实现进度显示的功能，进度文字同样原理添加到了父布局中。

### 如何配置
build.gradle(Project)
``` code
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
build.gradle(Module:app)
``` code
 dependencies {
	         compile 'com.github.Ccapton:Android-ColorfulProgressBar:1.0'
	}
```

### 主要方法

``` code
 setStyle(String style) // ColofulProgressBar.STYLE_NORMAL、 ColofulProgressBar.STYLE_COLORFUL两种风格
 setMaxProgress(long maxProgress)   // 设置进度最大值
 setProgress(long progress);           //设置当前进度
 setSecondProgress(long secondProgress);  //设置第二进度
 setAnimation(false);                  // 关闭动画
 showPercentText(false);                //隐藏进度文字
 setHeight(int height);               // 设置整个控件高度
 setWidth(int width);                // 设置整个控件宽度
 setProgressColorRes(int progressColorRes);  // 设置进度条颜色一
 setProgressColor2Res(int progressColor2Res)  // 设置进度条颜色二
 setPercentColorRes(int percentColorRes);       //设置进度文字的颜色
 setPercentShadeColorRes(int percentShadeColorRes);  //设置进度文字的阴影颜色
 setBackgroundColorRes(int backgroundColorRes)     //设置控件背景色
  
```
### 使用方法

例：在activity_main.xml中，
``` xml
这是普通效果的Progressbar
<com.capton.colorfulprogressbar.ColorfulProgressbar
        android:id="@+id/colorfulProgressbar"					    
        xmlns:app="http://schemas.android.com/apk/res-auto"
        app:style="@string/style_normal"
        app:max="100"
        app:progress="50"
        app:secondProgress="10"
        app:progressColor1="@color/green"
        android:layout_marginTop="16dp" 
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        >
这是双色的Progressbar
	<com.capton.colorfulprogressbar.ColorfulProgressbar
        android:id="@+id/colorfulProgressbar2"					    
        xmlns:app="http://schemas.android.com/apk/res-auto"
        app:style="@string/style_colorful"
        app:max="100"
        app:progress="50"
        app:secondProgress="10"
        app:progressColor1="@color/green"
	app:progressColor2="@color/orange"
        android:layout_marginTop="16dp" 
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        >
```

例：在MainActivity中
``` code
  ColorfulProgressbar progressbar;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	
	progressbar= (ColorfulProgressbar) findViewById(R.id.colorfulProgressbar);
	progressbar.setMaxProgress(100);
        progressbar.setProgress(50);
      //  progressbar.setSecondProgress(10);
      // progressbar.setAnimation(false); 关闭动画
      // progressbar.showPercentText(false); 隐藏进度文字
      
	}
```  
### 作者的话
  挺漂亮的ProgressBar，就是没啥技术含量。。。。具体需求大家自己随便研究琢磨
