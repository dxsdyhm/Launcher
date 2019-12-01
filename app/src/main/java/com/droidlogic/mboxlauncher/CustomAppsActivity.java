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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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
        this.gv.setOnItemClickListener(new OnItemClickListener() {
            /* JADX WARNING: Code restructure failed: missing block: B:28:0x012d, code lost:
                return;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onItemClick(android.widget.AdapterView<?> r9, android.view.View r10, int r11, long r12) {
                /*
                    r8 = this;
                    java.lang.Object r0 = r9.getItemAtPosition(r11)
                    java.util.Map r0 = (java.util.Map) r0
                    java.lang.Object r1 = com.droidlogic.mboxlauncher.CustomAppsActivity.mLock
                    monitor-enter(r1)
                    r2 = 1
                    com.droidlogic.mboxlauncher.Launcher.ifChangedShortcut = r2     // Catch:{ all -> 0x012e }
                    java.lang.String r2 = "item_type"
                    java.lang.Object r2 = r0.get(r2)     // Catch:{ all -> 0x012e }
                    r3 = 2131230921(0x7f0800c9, float:1.8077908E38)
                    java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x012e }
                    boolean r2 = r2.equals(r3)     // Catch:{ all -> 0x012e }
                    if (r2 == 0) goto L_0x0026
                    com.droidlogic.mboxlauncher.CustomAppsActivity r2 = com.droidlogic.mboxlauncher.CustomAppsActivity.this     // Catch:{ all -> 0x012e }
                    r2.finish()     // Catch:{ all -> 0x012e }
                    goto L_0x012c
                L_0x0026:
                    java.lang.String r2 = "item_sel"
                    java.lang.Object r2 = r0.get(r2)     // Catch:{ all -> 0x012e }
                    r3 = 2131230923(0x7f0800cb, float:1.8077912E38)
                    java.lang.Integer r4 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x012e }
                    boolean r2 = r2.equals(r4)     // Catch:{ all -> 0x012e }
                    if (r2 == 0) goto L_0x00db
                    java.lang.String r2 = com.droidlogic.mboxlauncher.Launcher.current_shortcutHead     // Catch:{ all -> 0x012e }
                    java.lang.String r3 = "Home_Shortcut:"
                    boolean r2 = r2.equals(r3)     // Catch:{ all -> 0x012e }
                    if (r2 == 0) goto L_0x006e
                    com.droidlogic.mboxlauncher.CustomAppsActivity r2 = com.droidlogic.mboxlauncher.CustomAppsActivity.this     // Catch:{ all -> 0x012e }
                    int r2 = r2.homeShortcutCount     // Catch:{ all -> 0x012e }
                    int r3 = com.droidlogic.mboxlauncher.Launcher.HOME_SHORTCUT_COUNT     // Catch:{ all -> 0x012e }
                    if (r2 < r3) goto L_0x006e
                    com.droidlogic.mboxlauncher.CustomAppsActivity r2 = com.droidlogic.mboxlauncher.CustomAppsActivity.this     // Catch:{ all -> 0x012e }
                    android.content.Context r2 = r2.mContext     // Catch:{ all -> 0x012e }
                    com.droidlogic.mboxlauncher.CustomAppsActivity r3 = com.droidlogic.mboxlauncher.CustomAppsActivity.this     // Catch:{ all -> 0x012e }
                    android.content.Context r3 = r3.mContext     // Catch:{ all -> 0x012e }
                    android.content.res.Resources r3 = r3.getResources()     // Catch:{ all -> 0x012e }
                    r4 = 2131689958(0x7f0f01e6, float:1.9008946E38)
                    java.lang.String r3 = r3.getString(r4)     // Catch:{ all -> 0x012e }
                    r4 = 0
                    android.widget.Toast r2 = android.widget.Toast.makeText(r2, r3, r4)     // Catch:{ all -> 0x012e }
                    r2.show()     // Catch:{ all -> 0x012e }
                    monitor-exit(r1)     // Catch:{ all -> 0x012e }
                    return
                L_0x006e:
                    java.lang.String r2 = "item_symbol"
                    java.lang.Object r2 = r0.get(r2)     // Catch:{ all -> 0x012e }
                    android.content.ComponentName r2 = (android.content.ComponentName) r2     // Catch:{ all -> 0x012e }
                    java.lang.String r2 = r2.getPackageName()     // Catch:{ all -> 0x012e }
                    com.droidlogic.mboxlauncher.CustomAppsActivity r3 = com.droidlogic.mboxlauncher.CustomAppsActivity.this     // Catch:{ all -> 0x012e }
                    java.lang.String r3 = r3.str_custom_apps     // Catch:{ all -> 0x012e }
                    if (r3 != 0) goto L_0x009e
                    com.droidlogic.mboxlauncher.CustomAppsActivity r3 = com.droidlogic.mboxlauncher.CustomAppsActivity.this     // Catch:{ all -> 0x012e }
                    java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x012e }
                    r4.<init>()     // Catch:{ all -> 0x012e }
                    java.lang.String r5 = com.droidlogic.mboxlauncher.Launcher.current_shortcutHead     // Catch:{ all -> 0x012e }
                    r4.append(r5)     // Catch:{ all -> 0x012e }
                    r4.append(r2)     // Catch:{ all -> 0x012e }
                    java.lang.String r5 = ";"
                    r4.append(r5)     // Catch:{ all -> 0x012e }
                    java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x012e }
                    r3.str_custom_apps = r4     // Catch:{ all -> 0x012e }
                    goto L_0x00b4
                L_0x009e:
                    com.droidlogic.mboxlauncher.CustomAppsActivity r3 = com.droidlogic.mboxlauncher.CustomAppsActivity.this     // Catch:{ all -> 0x012e }
                    java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ all -> 0x012e }
                    r4.<init>()     // Catch:{ all -> 0x012e }
                    r4.append(r2)     // Catch:{ all -> 0x012e }
                    java.lang.String r5 = ";"
                    r4.append(r5)     // Catch:{ all -> 0x012e }
                    java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x012e }
                    com.droidlogic.mboxlauncher.CustomAppsActivity.access$284(r3, r4)     // Catch:{ all -> 0x012e }
                L_0x00b4:
                    java.lang.Object r3 = r9.getItemAtPosition(r11)     // Catch:{ all -> 0x012e }
                    java.util.Map r3 = (java.util.Map) r3     // Catch:{ all -> 0x012e }
                    java.lang.String r4 = "item_sel"
                    r5 = 2131230922(0x7f0800ca, float:1.807791E38)
                    java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ all -> 0x012e }
                    r3.put(r4, r5)     // Catch:{ all -> 0x012e }
                    com.droidlogic.mboxlauncher.CustomAppsActivity r3 = com.droidlogic.mboxlauncher.CustomAppsActivity.this     // Catch:{ all -> 0x012e }
                    r3.updateView()     // Catch:{ all -> 0x012e }
                    java.lang.String r3 = com.droidlogic.mboxlauncher.Launcher.current_shortcutHead     // Catch:{ all -> 0x012e }
                    java.lang.String r4 = "Home_Shortcut:"
                    boolean r3 = r3.equals(r4)     // Catch:{ all -> 0x012e }
                    if (r3 == 0) goto L_0x00da
                    com.droidlogic.mboxlauncher.CustomAppsActivity r3 = com.droidlogic.mboxlauncher.CustomAppsActivity.this     // Catch:{ all -> 0x012e }
                    r3.homeShortcutCount = r3.homeShortcutCount + 1     // Catch:{ all -> 0x012e }
                L_0x00da:
                    goto L_0x012c
                L_0x00db:
                    java.lang.String r2 = "item_symbol"
                    java.lang.Object r2 = r0.get(r2)     // Catch:{ all -> 0x012e }
                    android.content.ComponentName r2 = (android.content.ComponentName) r2     // Catch:{ all -> 0x012e }
                    java.lang.String r2 = r2.getPackageName()     // Catch:{ all -> 0x012e }
                    com.droidlogic.mboxlauncher.CustomAppsActivity r4 = com.droidlogic.mboxlauncher.CustomAppsActivity.this     // Catch:{ all -> 0x012e }
                    com.droidlogic.mboxlauncher.CustomAppsActivity r5 = com.droidlogic.mboxlauncher.CustomAppsActivity.this     // Catch:{ all -> 0x012e }
                    java.lang.String r5 = r5.str_custom_apps     // Catch:{ all -> 0x012e }
                    java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ all -> 0x012e }
                    r6.<init>()     // Catch:{ all -> 0x012e }
                    r6.append(r2)     // Catch:{ all -> 0x012e }
                    java.lang.String r7 = ";"
                    r6.append(r7)     // Catch:{ all -> 0x012e }
                    java.lang.String r6 = r6.toString()     // Catch:{ all -> 0x012e }
                    java.lang.String r7 = ""
                    java.lang.String r5 = r5.replaceAll(r6, r7)     // Catch:{ all -> 0x012e }
                    r4.str_custom_apps = r5     // Catch:{ all -> 0x012e }
                    java.lang.Object r4 = r9.getItemAtPosition(r11)     // Catch:{ all -> 0x012e }
                    java.util.Map r4 = (java.util.Map) r4     // Catch:{ all -> 0x012e }
                    java.lang.String r5 = "item_sel"
                    java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ all -> 0x012e }
                    r4.put(r5, r3)     // Catch:{ all -> 0x012e }
                    com.droidlogic.mboxlauncher.CustomAppsActivity r3 = com.droidlogic.mboxlauncher.CustomAppsActivity.this     // Catch:{ all -> 0x012e }
                    r3.updateView()     // Catch:{ all -> 0x012e }
                    java.lang.String r3 = com.droidlogic.mboxlauncher.Launcher.current_shortcutHead     // Catch:{ all -> 0x012e }
                    java.lang.String r4 = "Home_Shortcut:"
                    boolean r3 = r3.equals(r4)     // Catch:{ all -> 0x012e }
                    if (r3 == 0) goto L_0x012c
                    com.droidlogic.mboxlauncher.CustomAppsActivity r3 = com.droidlogic.mboxlauncher.CustomAppsActivity.this     // Catch:{ all -> 0x012e }
                    r3.homeShortcutCount = r3.homeShortcutCount - 1     // Catch:{ all -> 0x012e }
                L_0x012c:
                    monitor-exit(r1)     // Catch:{ all -> 0x012e }
                    return
                L_0x012e:
                    r2 = move-exception
                    monitor-exit(r1)     // Catch:{ all -> 0x012e }
                    throw r2
                */
                throw new UnsupportedOperationException("Method not decompiled: com.droidlogic.mboxlauncher.CustomAppsActivity.AnonymousClass1.onItemClick(android.widget.AdapterView, android.view.View, int, long):void");
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
        Log.d("CustomAppsActivity", "------onPause");
        saveShortcut("/data/data/com.droidlogic.mboxlauncher/shortcut.cfg", this.str_custom_apps);
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
        this.str_custom_apps = loadCustomApps("/data/data/com.droidlogic.mboxlauncher/shortcut.cfg", Launcher.current_shortcutHead);
        this.list_custom_apps = this.str_custom_apps.split(";");
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
                if (this.list_custom_apps != null) {
                    int j2 = 0;
                    while (true) {
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
        String str;
        this.mFile = new File(path);
        if (!this.mFile.exists()) {
            return null;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.mFile));
            do {
                String readLine = br.readLine();
                str = readLine;
                if (readLine == null) {
                    break;
                }
            } while (!str.startsWith(shortcut_head));
            this.str_custom_apps = str.replaceAll(shortcut_head, "");
            try {
                br.close();
            } catch (IOException e) {
            }
            return this.str_custom_apps;
        } catch (Exception e2) {

        } finally {
            return null;
        }
    }

    public void saveShortcut(String path, String str_apps) {
        this.mFile = new File(path);
        if (!this.mFile.exists()) {
            try {
                this.mFile.createNewFile();
            } catch (Exception e) {
                Log.e("CustomAppsActivity", e.getMessage().toString());
            }
        }
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            BufferedReader br2 = new BufferedReader(new FileReader(this.mFile));
            List list = new ArrayList();
            while (true) {
                String readLine = br2.readLine();
                String str = readLine;
                if (readLine == null) {
                    break;
                }
                list.add(str);
            }
            if (list.size() == 0) {
                list.add("Home_Shortcut:");
                list.add("Video_Shortcut:");
                list.add("Recommend_Shortcut:");
                list.add("Music_shortcut:");
                list.add("Local_Shortcut:");
            }
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(this.mFile));
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).toString().startsWith(Launcher.current_shortcutHead)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(Launcher.current_shortcutHead);
                    sb.append(str_apps);
                    str_apps = sb.toString();
                    bw2.write(str_apps);
                } else {
                    bw2.write(list.get(i).toString());
                }
                bw2.newLine();
            }
            bw2.flush();
            bw2.close();
            try {
                br2.close();
            } catch (IOException e2) {
            }
            try {
                bw2.close();
            } catch (IOException e3) {
            }
        } catch (Exception e4) {
            String str2 = "CustomAppsActivity";
            StringBuilder sb2 = new StringBuilder();
            sb2.append("   ");
            sb2.append(e4);
            Log.d(str2, sb2.toString());
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e5) {
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e6) {
                }
            }
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e7) {
                }
            }
            throw th;
        }
    }
}
