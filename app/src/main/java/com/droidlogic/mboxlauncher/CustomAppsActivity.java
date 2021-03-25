package com.droidlogic.mboxlauncher;

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ResolveInfo.DisplayNameComparator;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.hcy.launcher.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomAppsActivity extends Activity {
    public static int CONTENT_HEIGHT;
    static final Object mLock = new Object[0];
    public final static String SHORTCUT_PATH = "/data/data/com.hcy.launcher/shortcut.cfg";
    public final static String DEFAULT_SHORTCUR_PATH = "/system/etc/default_shortcut_simple.cfg";
    private TranslateAnimation exitTransAnim;
    /* access modifiers changed from: private */
    public GridView gv = null;
    /* access modifiers changed from: private */
    public int homeShortcutCount;
    /* access modifiers changed from: private */
    public ImageView img_dim = null;
    /* access modifiers changed from: private */
    public ImageView img_screen_shot = null;
    /* access modifiers changed from: private */
    public ImageView img_screen_shot_keep = null;
    private String[] list_custom_apps;
    /* access modifiers changed from: private */
    public Context mContext = null;
    private File mFile;
    /* access modifiers changed from: private */
    public String str_custom_apps;

    public final static String HOME_SHORTCUT_HEAD = "Home_Shortcut:";
    public final static String VIDEO_SHORTCUT_HEAD = "Video_Shortcut:";
    public final static String RECOMMEND_SHORTCUT_HEAD = "Recommend_Shortcut:";
    public final static String MUSIC_SHORTCUT_HEAD = "Music_shortcut:";
    public final static String LOCAL_SHORTCUT_HEAD = "Local_Shortcut:";

    private class MyAnimationListener implements AnimationListener {
        private int arrow_x_center;
        private int mTop;
        private int up_or_down;

        public MyAnimationListener(int top, int x_center, int flag) {
            this.mTop = top;
            this.up_or_down = flag;
            this.arrow_x_center = x_center;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            LayoutParams lp = new LayoutParams(-2, -2, 0, 0);
            CustomAppsActivity.this.img_screen_shot_keep.setImageBitmap(Launcher.screenShot_keep);
            if (this.up_or_down == 0) {
                lp.y = 0;
            } else {
                lp.y = this.mTop + CustomAppsActivity.CONTENT_HEIGHT;
            }
            CustomAppsActivity.this.img_screen_shot_keep.setLayoutParams(lp);
            CustomAppsActivity.this.img_screen_shot.setVisibility(View.INVISIBLE);
            Animation anim = AnimationUtils.loadAnimation(CustomAppsActivity.this.mContext, R.anim.anim_alpha_show);
            anim.setAnimationListener(new dimAnimationListener());
            CustomAppsActivity.this.img_dim.startAnimation(anim);
            CustomAppsActivity.this.gv.bringToFront();
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private class dimAnimationListener implements AnimationListener {
        private dimAnimationListener() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            CustomAppsActivity.this.img_dim.setVisibility(View.VISIBLE);
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private class exitAnimationListener implements AnimationListener {
        private exitAnimationListener() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            CustomAppsActivity.this.img_screen_shot.setVisibility(View.VISIBLE);
            CustomAppsActivity.this.finish();
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    static /* synthetic */ String access$284(CustomAppsActivity x0, Object x1) {
        StringBuilder sb = new StringBuilder();
        sb.append(x0.str_custom_apps);
        sb.append(x1);
        String sb2 = sb.toString();
        x0.str_custom_apps = sb2;
        return sb2;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("CustomAppsActivity", "------onCreate");
        Bundle extras = getIntent().getExtras();
        int top = extras.getInt("top");
        int bottom = extras.getInt("bottom");
        int left = extras.getInt("left");
        int right = extras.getInt("right");
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_custom_apps);
        this.gv = (GridView) findViewById(R.id.grid_add_apps);
        gv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Map<String, Object> item = (Map<String, Object>)parent.getItemAtPosition(pos);

                synchronized(mLock){

                    Launcher.ifChangedShortcut = true;

                    if (item.get("item_type").equals(R.drawable.item_img_exit)) {
                        finish();
                    } else if (item.get("item_sel").equals(R.drawable.item_img_unsel)) {
                        if (Launcher.current_shortcutHead.equals(HOME_SHORTCUT_HEAD) && homeShortcutCount >= Launcher.HOME_SHORTCUT_COUNT) {
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.str_nospace),Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String str_package_name = ((ComponentName)item.get("item_symbol")).getPackageName();
                        if (str_custom_apps == null) {
                            str_custom_apps = Launcher.current_shortcutHead + str_package_name + ";";
                        } else {
                            str_custom_apps += str_package_name  + ";";
                        }
                        ((Map<String, Object>)parent.getItemAtPosition(pos)).put("item_sel", R.drawable.item_img_sel);
                        updateView();

                        if (Launcher.current_shortcutHead.equals(HOME_SHORTCUT_HEAD))
                            homeShortcutCount++;
                    } else {
                        String str_package_name = ((ComponentName)item.get("item_symbol")).getPackageName();
                        str_custom_apps = str_custom_apps.replaceAll(str_package_name + ";", "");
                        ((Map<String, Object>)parent.getItemAtPosition(pos)).put("item_sel", R.drawable.item_img_unsel);
                        updateView();

                        if (Launcher.current_shortcutHead.equals(HOME_SHORTCUT_HEAD))
                            homeShortcutCount--;
                    }
                }

            }
        });
        this.mContext = this;
        this.img_screen_shot = (ImageView) findViewById(R.id.img_screenshot);
        this.img_screen_shot_keep = (ImageView) findViewById(R.id.img_screenshot_keep);
        this.img_dim = (ImageView) findViewById(R.id.img_dim);
        setViewPosition(top, bottom, left, right);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Log.d("CustomAppsActivity", "------onResume");
        this.str_custom_apps = "";
        displayView();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        Log.d("CustomAppsActivity", "------onPause"+str_custom_apps);
        saveShortcut(SHORTCUT_PATH, str_custom_apps);
    }

    public void onStop() {
        super.onStop();
        Log.d("CustomAppsActivity", "------onStop");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("CustomAppsActivity", "------onDestroy");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            this.img_screen_shot.bringToFront();
            this.img_dim.setVisibility(View.INVISIBLE);
            this.img_screen_shot.startAnimation(this.exitTransAnim);
            return true;
        } else if (keyCode != 84) {
            return super.onKeyDown(keyCode, event);
        } else {
            ComponentName globalSearchActivity = ((SearchManager) getSystemService(Context.SEARCH_SERVICE)).getGlobalSearchActivity();
            if (globalSearchActivity == null) {
                return false;
            }
            Intent intent = new Intent("android.search.action.GLOBAL_SEARCH");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(globalSearchActivity);
            Bundle appSearchData = new Bundle();
            appSearchData.putString("source", "launcher-search");
            intent.putExtra("app_data", appSearchData);
            startActivity(intent);
            return true;
        }
    }

    private void displayView() {
        this.homeShortcutCount = 0;
        LocalAdapter localAdapter = new LocalAdapter(this, loadApplications(), R.layout.add_apps_grid_item, new String[]{"item_type", "item_name", "item_sel", "item_bg"}, new int[]{R.id.item_type, R.id.item_name, R.id.item_sel, R.id.relative_layout});
        this.gv.setAdapter(localAdapter);
    }

    /* access modifiers changed from: private */
    public void updateView() {
        ((BaseAdapter) this.gv.getAdapter()).notifyDataSetChanged();
    }

    private int parseItemBackground(int num) {
        switch ((num % 20) + 1) {
            case 0:
                return R.drawable.item_child_3;
            case 1:
                return R.drawable.item_child_1;
            case 2:
                return R.drawable.item_child_2;
            case 3:
                return R.drawable.item_child_3;
            case 4:
                return R.drawable.item_child_4;
            case 5:
                return R.drawable.item_child_5;
            case 6:
                return R.drawable.item_child_6;
            case 7:
                return R.drawable.item_child_3;
            case 8:
                return R.drawable.item_child_4;
            case 9:
                return R.drawable.item_child_1;
            case 10:
                return R.drawable.item_child_2;
            case 11:
                return R.drawable.item_child_6;
            case 12:
                return R.drawable.item_child_5;
            case 13:
                return R.drawable.item_child_6;
            case 14:
                return R.drawable.item_child_2;
            case 15:
                return R.drawable.item_child_5;
            case 16:
                return R.drawable.item_child_3;
            case 17:
                return R.drawable.item_child_1;
            case 18:
                return R.drawable.item_child_4;
            case 19:
                return R.drawable.item_child_2;
            default:
                return R.drawable.item_child_1;
        }
    }

    private List<Map<String, Object>> loadApplications() {
        List<Map<String, Object>> list = new ArrayList<>();
        HashMap hashMap = new HashMap();
        PackageManager manager = getPackageManager();
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        Collections.sort(apps, new DisplayNameComparator(manager));
        this.str_custom_apps = loadCustomApps(CustomAppsActivity.SHORTCUT_PATH, Launcher.current_shortcutHead);
        this.list_custom_apps = this.str_custom_apps.split(";");
        Log.e("dxsTest","this.str_custom_apps:"+this.str_custom_apps);
        if (this.list_custom_apps != null) {
            int i = 0;
            while (i < this.list_custom_apps.length) {
                int count = apps.size();
                int j = 0;
                while (j < count && !((ResolveInfo) apps.get(j)).activityInfo.applicationInfo.packageName.equals(this.list_custom_apps[i])) {
                    if (j == count - 1) {
                        String str = this.str_custom_apps;
                        StringBuilder sb = new StringBuilder();
                        sb.append(this.list_custom_apps[i]);
                        sb.append(";");
                        this.str_custom_apps = str.replaceAll(sb.toString(), "");
                    }
                    j++;
                }
                i++;
            }
        }
        this.list_custom_apps = this.str_custom_apps.split(";");
        if (Launcher.current_shortcutHead.equals("Home_Shortcut:")) {
            this.homeShortcutCount = this.list_custom_apps.length;
        }
        if (apps != null) {
            int count2 = apps.size();
            HashMap hashMap2 = hashMap;
            for (int i2 = 0; i2 < count2; i2++) {
                ApplicationInfo application = new ApplicationInfo();
                ResolveInfo info = (ResolveInfo) apps.get(i2);
                application.title = info.loadLabel(manager);
                application.setActivity(new ComponentName(info.activityInfo.applicationInfo.packageName, info.activityInfo.name), 270532608);
                application.icon = info.activityInfo.loadIcon(manager);
                hashMap2 = new HashMap();
                hashMap2.put("item_name", application.title.toString());
                hashMap2.put("file_path", application.intent);
                if (Launcher.parseItemIcon(application.componentName.getPackageName()) != -1) {
                    hashMap2.put("item_type", Integer.valueOf(Launcher.parseItemIcon(application.componentName.getPackageName())));
                } else {
                    hashMap2.put("item_type", application.icon);
                }
                hashMap2.put("item_sel", Integer.valueOf(R.drawable.item_img_unsel));
                hashMap2.put("item_bg", Integer.valueOf(parseItemBackground(i2)));
                hashMap2.put("item_symbol", application.componentName);
                Log.e("dxsTest","this.list_custom_apps[j2]:"+this.list_custom_apps);
                if (this.list_custom_apps != null) {
                    int j2 = 0;
                    while (true) {
                        Log.e("dxsTest","application.componentName:"+application.componentName.getPackageName());

                        if (j2 >= this.list_custom_apps.length) {
                            break;
                        } else if (application.componentName.getPackageName().equals(this.list_custom_apps[j2])) {
                            hashMap2.put("item_sel", Integer.valueOf(R.drawable.item_img_sel));
                            break;
                        } else {
                            j2++;
                        }
                    }
                }
                list.add(hashMap2);
            }
            HashMap hashMap3 = hashMap2;
        }
        return list;
    }

    private void setViewPosition(int top, int bottom, int left, int right) {
        TranslateAnimation translateAnimation;
        int arrow_x_center = ((right - left) / 2) + left;
        this.img_screen_shot.setImageBitmap(Launcher.screenShot);
        if (bottom > Launcher.SCREEN_HEIGHT / 2) {
            LayoutParams lp = new LayoutParams(-2, -2, 0, 0);
            lp.y = 0;
            this.img_screen_shot.setLayoutParams(lp);
            translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (0 - CONTENT_HEIGHT));
            translateAnimation.setDuration(500);
            this.exitTransAnim = new TranslateAnimation(0.0f, 0.0f, (float) (0 - CONTENT_HEIGHT), 0.0f);
            this.exitTransAnim.setDuration(300);
            LayoutParams lp1 = new LayoutParams(-2, -2, 0, 0);
            lp1.height = CONTENT_HEIGHT;
            if (top - CONTENT_HEIGHT > 0) {
                lp1.y = top - CONTENT_HEIGHT;
            } else {
                lp1.y = 0;
            }
            this.gv.setLayoutParams(lp1);
            translateAnimation.setAnimationListener(new MyAnimationListener(lp1.y, arrow_x_center, 0));
        } else {
            LayoutParams lp2 = new LayoutParams(-2, -2, 0, 0);
            lp2.y = bottom;
            this.img_screen_shot.setLayoutParams(lp2);
            translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) CONTENT_HEIGHT);
            translateAnimation.setDuration(500);
            this.exitTransAnim = new TranslateAnimation(0.0f, 0.0f, (float) CONTENT_HEIGHT, 0.0f);
            this.exitTransAnim.setDuration(300);
            LayoutParams lp12 = new LayoutParams(-2, -2, 0, 0);
            lp12.y = bottom;
            lp12.height = CONTENT_HEIGHT;
            this.gv.setLayoutParams(lp12);
            translateAnimation.setAnimationListener(new MyAnimationListener(lp12.y, arrow_x_center, 1));
        }
        this.exitTransAnim.setAnimationListener(new exitAnimationListener());
        this.img_screen_shot.startAnimation(translateAnimation);
    }

    // TODO: 19-11-30 返回自定义APPs 
    private String loadCustomApps(String path, String shortcut_head) {
        return SPUtils.getInstance().getString(Launcher.current_shortcutHead,"");
    }

    public void saveShortcut(String path, String str_apps) {
        //采用SP
        SPUtils.getInstance().put(Launcher.current_shortcutHead,str_apps);
    }
}
