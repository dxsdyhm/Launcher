package com.droidlogic.mboxlauncher;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.droidlogic.Weather.Utils;

public class MyOnTouchListener implements OnTouchListener {
    private int NUM_APP = 2;
    private int NUM_LOCAL = 4;
    private int NUM_MUSIC = 3;
    private int NUM_RECOMMEND = 1;
    private int NUM_VIDEO = 0;
    private Object appPath;
    private Context mContext;

    public MyOnTouchListener(Context context, Object path) {
        this.mContext = context;
        this.appPath = path;
    }

    public boolean onTouch(View view, MotionEvent event) {
        Launcher.isInTouchMode = true;
        if (event.getAction() == 1) {
            ImageView img = (ImageView) ((ViewGroup) view).getChildAt(0);
            String path = img.getResources().getResourceName(img.getId());
            String vName = path.substring(path.indexOf("/") + 1);
            if (vName.equals("img_setting")) {
                showMenuView(this.NUM_LOCAL, view);
            } else if (vName.equals("img_weather")) {
                showMenuView(this.NUM_APP, view);
            } else if (vName.equals("img_time")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.esp.technology.ecoo.tv", "com.youmait.echootv.presentation.splash.SplashActivity"));
                this.mContext.startActivity(intent);
            } else if (vName.equals("img_nclean")) {
                Intent intent2 = new Intent();
                intent2.setComponent(new ComponentName("com.hcy.launcher", "com.droidlogic.mboxlauncher.RocketActivity"));
                this.mContext.startActivity(intent2);
            } else if (vName.equals("img_next")) {
                showMenuView(this.NUM_VIDEO, view);
                Launcher.tx_time.setVisibility(View.GONE);
                Launcher.tx_date.setVisibility(View.GONE);
            } else if (vName.equals("img_video")) {
                Intent intent3 = new Intent();
                intent3.setComponent(new ComponentName("com.google.android.youtube.tv", "com.google.android.apps.youtube.tv.activity.ShellActivity"));
                this.mContext.startActivity(intent3);
            } else if (vName.equals("img_recommend")) {
                Intent intent4 = new Intent();
                intent4.setComponent(new ComponentName("com.classiptv.tv", "com.classiptv.tv.activities.MainActivity"));
                this.mContext.startActivity(intent4);
            } else if (vName.equals("img_browser")) {
                Intent intent5 = new Intent();
                intent5.setComponent(new ComponentName("com.android.music", "com.android.music.MusicBrowserActivity"));
                Utils.startActivitySafe(this.mContext, intent5);
            } else if (vName.equals("img_music")) {
                Intent intent6 = new Intent();
                intent6.setComponent(new ComponentName("com.softwinner.TvdFileManager", "com.softwinner.TvdFileManager.MainUI"));
                this.mContext.startActivity(intent6);
            } else if (vName.equals("img_app")) {
                Intent intent7 = new Intent();
                intent7.setComponent(new ComponentName("com.android.vending", "com.android.vending.AssetBrowserActivity"));
                Utils.startActivitySafe(this.mContext, intent7);
            } else if (vName.equals("img_clean")) {
                Intent intent8 = new Intent();
                intent8.setComponent(new ComponentName("com.android.tv.settings", "com.android.tv.settings.MainSettings"));
                this.mContext.startActivity(intent8);
            } else if (vName.equals("img_local")) {
                Intent intent7 = new Intent();
                intent7.setComponent(new ComponentName("com.bravotv.iptv", "com.bravotv.iptv.activities.WelcomeActivity"));
                Utils.startActivitySafe(this.mContext, intent7);
            } else if (vName.equals("img_1")) {
                Intent intent7 = new Intent();
                intent7.setComponent(new ComponentName("ps.intro.flixiptv", "ps.intro.flixiptv.ui.activity.LoginActivityTV"));
                Utils.startActivitySafe(this.mContext, intent7);
            } else if (vName.equals("img_2")) {
                Intent intent7 = new Intent();
                intent7.setComponent(new ComponentName("com.mbm_soft.myhdiptvultra", "com.mbm_soft.myhdiptvultra.ui.intro.IntroActivity"));
                Utils.startActivitySafe(this.mContext, intent7);
            } else if (vName.equals("img_3")) {
                Intent intent7 = new Intent();
                intent7.setComponent(new ComponentName("com.elitetvtspi.iptv", "com.elitetvtspi.iptv.activities.WelcomeActivity"));
                Utils.startActivitySafe(this.mContext, intent7);
            } else if (vName.equals("img_4")) {
                Intent intent7 = new Intent();
                intent7.setComponent(new ComponentName("com.introps.turboiptv", "com.introps.turboiptv.StartActivity"));
                Utils.startActivitySafe(this.mContext, intent7);
            } else if (this.appPath != null) {
                this.mContext.startActivity((Intent) this.appPath);
            }
        } else if (event.getAction() == 0) {
            ImageView img2 = (ImageView) ((ViewGroup) view).getChildAt(0);
            String path2 = img2.getResources().getResourceName(img2.getId());
            String vName2 = path2.substring(path2.indexOf("/") + 1);
            if (vName2.equals("img_video") || vName2.equals("img_local") || vName2.equals("img_app") || vName2.equals("img_browser") || vName2.equals("img_music") || vName2.equals("img_recommend") || vName2.equals("img_clean") || vName2.equals("img_gone") || vName2.equals("img_weather")) {
                return view.onTouchEvent(event);
            }
        }
        return false;
    }

    private void showMenuView(int num, View view) {
        Launcher.saveHomeFocusView = view;
        Launcher.isShowHomePage = false;
        Launcher.layoutScaleShadow.setVisibility(View.INVISIBLE);
        view.getGlobalVisibleRect(new Rect());
        Launcher.viewMenu.setInAnimation(null);
        Launcher.viewMenu.setOutAnimation(null);
        Launcher.viewHomePage.setVisibility(View.GONE);
        Launcher.viewMenu.setVisibility(View.VISIBLE);
        Launcher.viewMenu.setDisplayedChild(num);
        Launcher.viewMenu.setFocusableInTouchMode(true);
    }
}
