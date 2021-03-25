package com.droidlogic.mboxlauncher;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hcy.launcher.R;

import java.util.List;

public class RocketActivity extends Activity {
    private AnimationDrawable fireAnimationDrawable;
    /* access modifiers changed from: private */
    public ImageView iv_cloud;
    /* access modifiers changed from: private */
    public ImageView iv_cloud_line;
    /* access modifiers changed from: private */
    public ImageView iv_rocket;
    private RelativeLayout rl_cloud;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_rocket);
        findView();
        initView();
        clearMemory();
    }

    private void findView() {
        this.iv_rocket = (ImageView) findViewById(R.id.iv_rocket);
        this.iv_cloud = (ImageView) findViewById(R.id.iv_cloud);
        this.iv_cloud_line = (ImageView) findViewById(R.id.iv_cloud_line);
        this.rl_cloud = (RelativeLayout) findViewById(R.id.rl_cloud);
    }

    private void initView() {
        this.iv_rocket.setPressed(true);
        this.iv_rocket.setFocusable(true);
        this.iv_rocket.setVisibility(View.VISIBLE);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.iv_rocket.setBackgroundResource(R.drawable.rocket_fire);
        this.fireAnimationDrawable = (AnimationDrawable) this.iv_rocket.getBackground();
        this.fireAnimationDrawable.start();
        fly();
    }

    private void fly() {
        Log.e("RocketActivity", "fly....");
        Animation loadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rocket_up);
        loadAnimation.setFillAfter(true);
        loadAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);
                alphaAnimation.setDuration(500);
                AlphaAnimation alphaAnimation2 = new AlphaAnimation(0.0f, 1.0f);
                alphaAnimation2.setDuration(500);
                alphaAnimation2.setStartOffset(250);
                RocketActivity.this.iv_cloud.startAnimation(alphaAnimation);
                RocketActivity.this.iv_cloud_line.startAnimation(alphaAnimation2);
            }

            public void onAnimationEnd(Animation animation) {
                RocketActivity.this.removeClound();
            }
        });
        this.iv_rocket.startAnimation(loadAnimation);
        createClound();
    }

    private void createClound() {
        this.iv_cloud.setVisibility(View.VISIBLE);
        this.iv_cloud_line.setVisibility(View.VISIBLE);
    }

    /* access modifiers changed from: private */
    public void removeClound() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        alphaAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                RocketActivity.this.iv_rocket.setVisibility(View.GONE);
                RocketActivity.this.iv_cloud.setVisibility(View.GONE);
                RocketActivity.this.iv_cloud_line.setVisibility(View.GONE);
                RocketActivity.this.finish();
            }
        });
        this.rl_cloud.startAnimation(alphaAnimation);
    }

    private void clearMemory() {
        long availMemory = getAvailMemory(this);
        StringBuilder sb = new StringBuilder();
        sb.append("-----------before memory info : ");
        sb.append(availMemory);
        String str = "RocketActivity";
        Log.d(str, sb.toString());
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List runningAppProcesses = activityManager.getRunningAppProcesses();
        if (runningAppProcesses != null) {
            for (int i = 0; i < runningAppProcesses.size(); i++) {
                RunningAppProcessInfo runningAppProcessInfo = (RunningAppProcessInfo) runningAppProcesses.get(i);
                String[] strArr = runningAppProcessInfo.pkgList;
                if (runningAppProcessInfo.importance > 200) {
                    for (String forceStopPackage : strArr) {
                        activityManager.forceStopPackage(forceStopPackage);
                    }
                }
            }
        }
        long availMemory2 = getAvailMemory(this);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("----------- after memory info : ");
        sb2.append(availMemory2);
        Log.d(str, sb2.toString());
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Release ");
        sb3.append(Math.abs(availMemory2 - availMemory) / 1048576);
        sb3.append("M RAM");
        Toast.makeText(this, sb3.toString(), Toast.LENGTH_LONG).show();
    }

    private long getAvailMemory(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }
}
