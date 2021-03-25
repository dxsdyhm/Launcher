package com.droidlogic.mboxlauncher;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import com.droidlogic.Weather.Utils;

public class MyOnKeyListener implements OnKeyListener {
    private int NUM_APP = 2;
    private int NUM_LOCAL = 4;
    private int NUM_MUSIC = 3;
    private int NUM_RECOMMEND = 1;
    private int NUM_VIDEO = 0;
    private Object appPath;
    private ActivityManager mActivityManager = null;
    private Context mContext;

    private class MyAnimationListener implements AnimationListener {
        private int in_or_out;

        public MyAnimationListener(int flag) {
            this.in_or_out = flag;
        }

        public void onAnimationStart(Animation animation) {
            Launcher.animIsRun = true;
            Launcher.layoutScaleShadow.setVisibility(View.INVISIBLE);
        }

        public void onAnimationEnd(Animation animation) {
            int count = 5;
            if (this.in_or_out == 1) {
                if (((ViewGroup) Launcher.viewMenu.getCurrentView()).getChildAt(4) instanceof MyScrollView) {
                    ViewGroup findGridLayout = (ViewGroup) ((ViewGroup) ((ViewGroup) Launcher.viewMenu.getCurrentView()).getChildAt(4)).getChildAt(0);
                    if (findGridLayout.getChildCount() < 6) {
                        count = findGridLayout.getChildCount() - 1;
                    }
                    Launcher.dontRunAnim = true;
                    findGridLayout.getChildAt(count).requestFocus();
                    Launcher.dontRunAnim = false;
                }
            } else if (this.in_or_out == 3) {
                Launcher.dontRunAnim = true;
                Launcher.viewMenu.clearFocus();
                Launcher.dontRunAnim = true;
                Launcher.viewMenu.requestFocus();
                Launcher.dontRunAnim = false;
            } else if (this.in_or_out == 5) {
                Launcher.dontRunAnim = true;
                Launcher.viewMenu.clearFocus();
                Launcher.dontRunAnim = true;
                Launcher.viewMenu.requestFocus();
                Launcher.dontRunAnim = false;
            }
            Launcher.animIsRun = false;
            Launcher.layoutScaleShadow.setVisibility(View.VISIBLE);
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    public MyOnKeyListener(Context context, Object path) {
        this.mContext = context;
        this.appPath = path;
    }

    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (Launcher.animIsRun) {
            return true;
        }
        if (keyCode != 4) {
            Launcher.isInTouchMode = false;
        }
        if (event.getAction() == 1 && (keyCode == 23 || keyCode == 66)) {
            ImageView img = (ImageView) ((ViewGroup) view).getChildAt(0);
            String path = img.getResources().getResourceName(img.getId());
            String vName = path.substring(path.indexOf("/") + 1);
            Log.e("dxsTest","vName:"+vName);
            if (vName.equals("img_setting")) {
                showMenuView(this.NUM_LOCAL, view);
                return true;
            } else if (vName.equals("img_weather") || keyCode == 210) {
                showMenuView(this.NUM_APP, view);
                return true;
            } else if (vName.equals("img_time")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.esp.technology.ecoo.tv", "com.youmait.echootv.presentation.splash.SplashActivity"));
                this.mContext.startActivity(intent);
                return true;
            } else if (vName.equals("img_nclean")) {
                Intent intent2 = new Intent();
                intent2.setComponent(new ComponentName("com.hcy.launcher", "com.droidlogic.mboxlauncher.RocketActivity"));
                this.mContext.startActivity(intent2);
                return true;
            } else if (vName.equals("img_next")) {
                Launcher.tx_time.setVisibility(View.GONE);
                Launcher.tx_date.setVisibility(View.GONE);
                showMenuView(this.NUM_VIDEO, view);
                return true;
            } else if (vName.equals("img_video")) {
                Intent intent3 = new Intent();
                intent3.setComponent(new ComponentName("com.google.android.youtube.tv", "com.google.android.apps.youtube.tv.activity.ShellActivity"));
                this.mContext.startActivity(intent3);
                return true;
            } else if (vName.equals("img_recommend")) {
                Intent intent4 = new Intent();
                intent4.setComponent(new ComponentName("com.classiptv.tv", "com.classiptv.tv.activities.MainActivity"));
                this.mContext.startActivity(intent4);
                return true;
            } else if (vName.equals("img_browser")) {
                Intent intent5 = new Intent();
                intent5.setComponent(new ComponentName("com.android.music", "com.android.music.MusicBrowserActivity"));
                Utils.startActivitySafe(this.mContext, intent5);
                return true;
            } else if (vName.equals("img_music")) {
                Intent intent6 = new Intent();
                intent6.setComponent(new ComponentName("com.softwinner.TvdFileManager", "com.softwinner.TvdFileManager.MainUI"));
                this.mContext.startActivity(intent6);
                return true;
            } else if (vName.equals("img_app")) {
                Intent intent7 = new Intent();
                intent7.setComponent(new ComponentName("com.android.vending", "com.android.vending.AssetBrowserActivity"));
                Utils.startActivitySafe(this.mContext, intent7);
                return true;
            } else if (vName.equals("img_clean")) {
                Launcher.saveHomeFocusView = view;
                Intent intent8 = new Intent();
                intent8.setComponent(new ComponentName("com.android.tv.settings", "com.android.tv.settings.MainSettings"));
                this.mContext.startActivity(intent8);
                Launcher.IntoApps = true;
                return true;
            } else if (this.appPath != null) {
                if (Launcher.isShowHomePage) {
                    Launcher.saveHomeFocusView = view;
                }
                this.mContext.startActivity((Intent) this.appPath);
                Launcher.IntoApps = true;
            } else if (vName.equals("img_local")) {
                Intent intent7 = new Intent();
                intent7.setComponent(new ComponentName("com.bravotv.iptv", "com.bravotv.iptv.activities.WelcomeActivity"));
                Utils.startActivitySafe(this.mContext, intent7);
                return true;
            } else if (vName.equals("img_1")) {
                Intent intent7 = new Intent();
                intent7.setComponent(new ComponentName("ps.intro.flixiptv", "ps.intro.flixiptv.ui.activity.LoginActivityTV"));
                Utils.startActivitySafe(this.mContext, intent7);
                return true;
            } else if (vName.equals("img_2")) {
                Intent intent7 = new Intent();
                intent7.setComponent(new ComponentName("com.mbm_soft.myhdiptvultra", "com.mbm_soft.myhdiptvultra.ui.intro.IntroActivity"));
                Utils.startActivitySafe(this.mContext, intent7);
                return true;
            } else if (vName.equals("img_3")) {
                Intent intent7 = new Intent();
                intent7.setComponent(new ComponentName("com.elitetvtspi.iptv", "com.elitetvtspi.iptv.activities.WelcomeActivity"));
                Utils.startActivitySafe(this.mContext, intent7);
                return true;
            } else if (vName.equals("img_4")) {
                Intent intent7 = new Intent();
                intent7.setComponent(new ComponentName("com.introps.turboiptv", "com.introps.turboiptv.StartActivity"));
                Utils.startActivitySafe(this.mContext, intent7);
                return true;
            }
        } else if (keyCode == 210) {
            showMenuView(this.NUM_APP, view);
            Launcher.tx_time.setVisibility(View.GONE);
            Launcher.tx_date.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    private void showMenuView(int num, View view) {
        Launcher.saveHomeFocusView = view;
        Launcher.isShowHomePage = false;
        Launcher.layoutScaleShadow.setVisibility(View.INVISIBLE);
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        ScaleAnimation scaleAnimationIn = new ScaleAnimation(1.0f, 1.0f, 1.0f, 1.0f, getPiovtX(rect), getPiovtY(rect));
        scaleAnimationIn.setDuration(200);
        scaleAnimationIn.setAnimationListener(new MyAnimationListener(5));
        Launcher.viewMenu.setInAnimation(scaleAnimationIn);
        Launcher.viewMenu.setOutAnimation(null);
        Launcher.viewHomePage.setVisibility(View.GONE);
        Launcher.viewMenu.setVisibility(View.VISIBLE);
        Launcher.viewMenu.setDisplayedChild(num);
        Launcher.viewMenu.getChildAt(num).requestFocus();
    }

    private float getPiovtX(Rect rect) {
        return (float) (rect.left + (rect.width() / 2));
    }

    private float getPiovtY(Rect rect) {
        return (float) (rect.top + (rect.height() / 2));
    }
}
