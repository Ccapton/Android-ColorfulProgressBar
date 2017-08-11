package com.capton.colofulprogressbardemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.capton.colorfulprogressbar.ColorfulProgressbar;
import com.capton.colorfulprogressbar.DisplayUtil;

public class MainActivity extends AppCompatActivity {

    ColorfulProgressbar progressbar;
    ColorfulProgressbar progressbar2;
    ColorfulProgressbar progressbar3;
    ColorfulProgressbar progressbar4;
    SeekBar controller;
    SeekBar controller2;
    Switch aSwitch;
    Switch aSwitch2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        progressbar= (ColorfulProgressbar) findViewById(R.id.colorful);
        progressbar2= (ColorfulProgressbar) findViewById(R.id.colorful2);
        progressbar3= (ColorfulProgressbar) findViewById(R.id.colorful3);
        progressbar4= (ColorfulProgressbar) findViewById(R.id.colorful4);

        controller= (SeekBar) findViewById(R.id.controller);
        controller2= (SeekBar) findViewById(R.id.controller2);

        aSwitch= (Switch) findViewById(R.id.switch1);
        aSwitch2= (Switch) findViewById(R.id.switch2);

        progressbar.setHeight(DisplayUtil.dip2px(this,20));
        progressbar.setPercentColorRes(R.color.yellow_green);
        progressbar.setPercentShadeColorRes(R.color.progressBg); 

        progressbar2.setHeight(DisplayUtil.dip2px(this,10));
        progressbar2.setStyle(ColorfulProgressbar.STYLE_NORMAL);
        progressbar2.setProgressColorRes(R.color.green);

        progressbar.setMaxProgress(100);
        progressbar2.setMaxProgress(100);

        progressbar.setProgress(50);
        progressbar.setSecondProgress(10);

        progressbar2.setProgress(50);
        progressbar2.setSecondProgress(10);

        controller.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressbar.setProgress(progress);
                progressbar2.setProgress(progress);
                progressbar3.setProgress(progress);
                progressbar4.setProgress(progress);
                ((TextView)findViewById(R.id.tip)).setText("Set Progress : "+progress+"%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        controller2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressbar.setSecondProgress(progress);
                progressbar2.setSecondProgress(progress);
                progressbar3.setSecondProgress(progress);
                progressbar4.setSecondProgress(progress);
                ((TextView)findViewById(R.id.tip2)).setText("Set SecondProgress : "+progress+"%");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        aSwitch.setChecked(true);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    progressbar.setAnimation(true);
                    progressbar2.setAnimation(true);
                    progressbar3.setAnimation(true);
                    progressbar4.setAnimation(true);
                }else {
                    progressbar.setAnimation(false);
                    progressbar2.setAnimation(false);
                    progressbar3.setAnimation(false);
                    progressbar4.setAnimation(false);
                }
            }
        });
        aSwitch2.setChecked(true);
        aSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    progressbar.showPercentText(true);
                    progressbar2.showPercentText(true);
                    progressbar3.showPercentText(true);
                    progressbar4.showPercentText(true);
                }else {
                    progressbar.showPercentText(false);
                    progressbar2.showPercentText(false);
                    progressbar3.showPercentText(false);
                    progressbar4.showPercentText(false);
                }
            }
        });
    }

}
