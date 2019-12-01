package com.droidlogic.mboxlauncher;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Instrumentation;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcy.launcher.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Launcher extends Activity {
    public static int HOME_SHORTCUT_COUNT = 10;
    public static boolean IntoApps;
    public static boolean IntoCustomActivity;
    public static String REAL_OUTPUT_MODE;
    public static int SCREEN_HEIGHT;
    public static int SCREEN_WIDTH;
    public static int accessBoundaryCount = 0;
    public static boolean animIsRun;
    public static MyGridLayout appShortcutView = null;
    public static boolean bootisFirstRunkodi = true;
    public static boolean cantGetDrawingCache;
    public static String current_shortcutHead = "Home_Shortcut:";
    public static boolean dontDrawFocus;
    public static boolean dontRunAnim;
    public static MyGridLayout homeShortcutView = null;
    public static boolean ifChangedShortcut;
    public static boolean isAddButtonBeTouched;
    public static boolean isInTouchMode;
    public static boolean isRealOutputMode;
    public static boolean isShowHomePage;
    public static RelativeLayout layoutScaleShadow;
    public static MyGridLayout localShortcutView = null;
    public static MyGridLayout musicShortcutView = null;
    public static View pressedAddButton = null;
    public static View prevFocusedView;
    public static MyGridLayout recommendShortcutView = null;
    public static View saveHomeFocusView = null;
    private static float scale_value;
    public static Bitmap screenShot;
    public static Bitmap screenShot_keep;
    public static float startX;
    /* access modifiers changed from: private */
    public static int time_count = 0;
    public static View trans_frameView;
    public static TextView tx_app_count = null;
    public static TextView tx_date;
    public static TextView tx_local_count = null;
    public static TextView tx_music_count = null;
    public static TextView tx_recommend_count = null;
    public static TextView tx_time;
    public static TextView tx_video_count = null;
    /* access modifiers changed from: private */
    public static boolean updateAllShortcut;
    public static MyGridLayout videoShortcutView = null;
    public static View viewHomePage = null;
    public static MyViewFlipper viewMenu = null;
    private final String SDCARD_FILE_NAME = "external_sd";
    private final String STORAGE_PATH = "/mnt";
    private final String STORAGE_PATH_usb = "/mnt/usb_storage";
    private BroadcastReceiver appReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.PACKAGE_CHANGED".equals(action) || "android.intent.action.PACKAGE_REMOVED".equals(action) || "android.intent.action.PACKAGE_ADDED".equals(action)) {
                Launcher.this.updateAppList(intent);
            }
        }
    };
    private Editor booteditorkodi;
    private SharedPreferences bootsharedPrekodi;
    private int count0 = 0;
    private int count2 = 0;
    private Editor editor;
    private BroadcastReceiver instabootReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("com.droidlogic.instaboot.RELOAD_APP_COMPLETED".equals(intent.getAction())) {
                Log.e("MediaBoxLauncher", "reloadappcompleted");
                Launcher.updateAllShortcut = true;
                Launcher.ifChangedShortcut = true;
                Launcher.this.displayShortcuts();
            }
        }
    };
    private boolean is24hFormart = false;
    private String[] list_homeShortcut;
    private String[] list_localShortcut;
    private String[] list_musicShortcut;
    private String[] list_recommendShortcut;
    private String[] list_videoShortcut;
    private ActivityManager mActivityManager = null;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 2:
                    ((MyRelativeLayout) Launcher.this.getCurrentFocus()).setSurface();
                    return;
                case 3:
                    ViewGroup findGridLayout = (ViewGroup) ((ViewGroup) ((ViewGroup) Launcher.viewMenu.getCurrentView()).getChildAt(4)).getChildAt(0);
                    int count = findGridLayout.getChildCount();
                    Launcher.dontRunAnim = true;
                    findGridLayout.getChildAt(count - 1).requestFocus();
                    Launcher.dontRunAnim = false;
                    return;
                case 4:
                    if (Launcher.this.numberInGrid != -1) {
                        ViewGroup findGridLayout2 = (ViewGroup) ((ViewGroup) ((ViewGroup) Launcher.viewMenu.getCurrentView()).getChildAt(4)).getChildAt(0);
                        Launcher.dontRunAnim = true;
                        int count2 = findGridLayout2.getChildCount();
                        if (Launcher.this.numberInGrid > count2 - 1) {
                            findGridLayout2.getChildAt(count2 - 1).requestFocus();
                        } else {
                            findGridLayout2.getChildAt(Launcher.this.numberInGrid).requestFocus();
                        }
                        Launcher.dontRunAnim = false;
                        Launcher.this.numberInGrid = -1;
                        return;
                    }
                    return;
                case 5:
                    if (Launcher.this.numberInGridOfShortcut != -1) {
                        Launcher.dontRunAnim = true;
                        Launcher.saveHomeFocusView = Launcher.homeShortcutView.getChildAt(Launcher.this.numberInGridOfShortcut);
                        Launcher.saveHomeFocusView.requestFocus();
                        Launcher.dontRunAnim = false;
                        Launcher.this.numberInGridOfShortcut = -1;
                        return;
                    }
                    return;
                case 6:
                    int i = Launcher.homeShortcutView.getChildCount();
                    Launcher.dontRunAnim = true;
                    Launcher.homeShortcutView.getChildAt(i - 1).requestFocus();
                    Launcher.dontRunAnim = false;
                    if (!Launcher.isInTouchMode) {
                        Launcher.layoutScaleShadow.setVisibility(View.VISIBLE);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private Handler mHandler1 = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what != 1) {
            }
        }
    };
    private IWindowManager mWindowManager;
    private BroadcastReceiver mediaReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && !"android.intent.action.MEDIA_EJECT".equals(action) && !"android.intent.action.MEDIA_UNMOUNTED".equals(action)) {
                "android.intent.action.MEDIA_MOUNTED".equals(action);
            }
        }
    };
    private int muencount = 0;
    private BroadcastReceiver netReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals("android.amlogic.settings.CHANGE_OUTPUT_MODE")) {
                    Launcher.this.setHeight();
                }
                if (action.equals("android.intent.action.TIME_SET")) {
                    Launcher.this.displayDate();
                }
                if (action.equals("android.intent.action.TIME_TICK")) {
                    Launcher.this.displayDate();
                    Launcher.access$608();
                    if (Launcher.time_count >= 180) {
                        Launcher.time_count = 0;
                    }
                } else if (action.equals("android.amlogic.settings.WEATHER_INFO")) {
                    intent.getExtras().getString("weather_today");
                } else if ("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE".equals(action) || "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE".equals(action)) {
                    Launcher.this.updateAppList(intent);
                }
            }
        }
    };
    private final String net_change_action = "android.net.conn.CONNECTIVITY_CHANGE";
    /* access modifiers changed from: private */
    public int numberInGrid = -1;
    /* access modifiers changed from: private */
    public int numberInGridOfShortcut = -1;
    private final String outputmode_change_action = "android.amlogic.settings.CHANGE_OUTPUT_MODE";
    private int popWindow_bottom = -1;
    private int popWindow_top = -1;
    private SharedPreferences pref;
    private SharedPreferences sharedPrekodi;
    private final int time_freq = 180;
    private TextView tx_app_allcount = null;
    private TextView tx_local_allcount = null;
    private TextView tx_music_allcount = null;
    private TextView tx_recommend_allcount = null;
    private TextView tx_video_allcount = null;
    private final String weather_receive_action = "android.amlogic.settings.WEATHER_INFO";
    private final String weather_request_action = "android.amlogic.launcher.REQUEST_WEATHER";
    private final String wifi_signal_action = "android.net.wifi.RSSI_CHANGED";

    static /* synthetic */ int access$608() {
        int i = time_count;
        time_count = i + 1;
        return i;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.d("MediaBoxLauncher", "------onCreate");
        //getWindow().setBackgroundDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.bg)));
        this.mWindowManager = Stub.asInterface(ServiceManager.getService("window"));
        scale_value = getAnimationScaleValue();
        initStaticVariable();
        initChildViews();
        setRectOnKeyListener();
        sendWeatherBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MEDIA_EJECT");
        filter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        filter.addAction("android.intent.action.MEDIA_MOUNTED");
        filter.addDataScheme("file");
        registerReceiver(this.mediaReceiver, filter);
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter2.addAction("android.net.wifi.RSSI_CHANGED");
        filter2.addAction("android.intent.action.TIME_TICK");
        filter2.addAction("android.intent.action.TIME_SET");
        filter2.addAction("android.amlogic.settings.WEATHER_INFO");
        filter2.addAction("android.amlogic.settings.CHANGE_OUTPUT_MODE");
        filter2.addAction("android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE");
        filter2.addAction("android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE");
        registerReceiver(this.netReceiver, filter2);
        IntentFilter filter3 = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        filter3.addAction("android.intent.action.PACKAGE_REMOVED");
        filter3.addAction("android.intent.action.PACKAGE_CHANGED");
        filter3.addDataScheme("package");
        registerReceiver(this.appReceiver, filter3);
        IntentFilter filter4 = new IntentFilter();
        filter4.addAction("com.droidlogic.instaboot.RELOAD_APP_COMPLETED");
        registerReceiver(this.instabootReceiver, filter4);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (isInTouchMode || (IntoCustomActivity && isShowHomePage && ifChangedShortcut)) {
            dontRunAnim = true;
            layoutScaleShadow.setVisibility(View.INVISIBLE);
        }
        if (animIsRun) {
            initStaticVariable();
        }
        displayShortcuts();
        displayDate();
        setHeight();
        if (cantGetDrawingCache) {
            resetShadow();
        }
        if (IntoCustomActivity) {
            setAnimationScale(true);
        }
        IntoCustomActivity = false;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        Log.d("MediaBoxLauncher", "------onPause");
        prevFocusedView = null;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        unregisterReceiver(this.mediaReceiver);
        unregisterReceiver(this.netReceiver);
        unregisterReceiver(this.appReceiver);
        unregisterReceiver(this.instabootReceiver);
        getWindow().setBackgroundDrawable(null);
        System.gc();
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if ("android.intent.action.MAIN".equals(intent.getAction())) {
            viewMenu.setVisibility(View.GONE);
            viewHomePage.setVisibility(View.VISIBLE);
            trans_frameView.setVisibility(View.INVISIBLE);
            layoutScaleShadow.setVisibility(View.INVISIBLE);
            isShowHomePage = true;
            IntoCustomActivity = false;
            updateAllShortcut = true;
            MyRelativeLayout videoView = (MyRelativeLayout) findViewById(R.id.layout_video);
            dontRunAnim = true;
            videoView.requestFocus();
            videoView.setSurface();
            tx_time.setVisibility(View.VISIBLE);
            tx_date.setVisibility(View.VISIBLE);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        layoutScaleShadow.setVisibility(View.INVISIBLE);
        if (event.getAction() == 0) {
            startX = event.getX();
        } else if (event.getAction() == 1 && pressedAddButton != null && isAddButtonBeTouched && !IntoCustomActivity) {
            new Rect();
            pressedAddButton.requestFocus();
            sendKeyCode(23);
            pressedAddButton = null;
            isAddButtonBeTouched = false;
        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 24) {
            if (this.muencount > 5) {
                Log.d("mylog", "======================ok");
                this.muencount = 0;
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.ftest", "com.ftest.HomePageActivity"));
                startActivity(intent);
            } else {
                this.muencount = 0;
                StringBuilder sb = new StringBuilder();
                sb.append("========");
                sb.append(this.muencount);
                Log.d("esle", sb.toString());
            }
        }
        if (keyCode == 25) {
            if (this.muencount > 5) {
                Log.d("mylog", "======================ok");
                this.muencount = 0;
                Intent intent2 = new Intent();
                intent2.setComponent(new ComponentName("com.ftest", "com.ftest.functiontestapp.MyFunctionActivity"));
                startActivity(intent2);
            } else {
                this.muencount = 0;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("========");
                sb2.append(this.muencount);
                Log.d("esle", sb2.toString());
            }
        }
        if (keyCode == 82) {
            this.muencount++;
            return true;
        }
        this.muencount = 0;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("======================");
        sb3.append(this.muencount);
        Log.d("muencount", sb3.toString());
        if (keyCode == 4) {
            if (!isShowHomePage && !animIsRun) {
                viewMenu.setVisibility(View.GONE);
                viewMenu.clearFocus();
                viewHomePage.setVisibility(View.VISIBLE);
                isShowHomePage = true;
                IntoCustomActivity = false;
                tx_time.setVisibility(View.VISIBLE);
                tx_date.setVisibility(View.VISIBLE);
                if (saveHomeFocusView != null && !isInTouchMode) {
                    prevFocusedView = null;
                    dontRunAnim = true;
                    saveHomeFocusView.clearFocus();
                    dontRunAnim = true;
                    saveHomeFocusView.requestFocus();
                }
            }
            return true;
        }
        if (keyCode == 23 || keyCode == 66) {
            ViewGroup view = (ViewGroup) getCurrentFocus();
            if (view.getChildAt(0) instanceof ImageView) {
                ImageView img = (ImageView) view.getChildAt(0);
                if (!(img == null || img.getContentDescription() == null || !img.getContentDescription().equals("img_add"))) {
                    setAnimationScale(false);
                    Rect rect = new Rect();
                    view.getGlobalVisibleRect(rect);
                    setHeight();
                    this.popWindow_top = rect.top - 10;
                    this.popWindow_bottom = rect.bottom + 10;
                    setPopWindow(this.popWindow_top, this.popWindow_bottom);
                    Intent intent3 = new Intent();
                    intent3.putExtra("top", this.popWindow_top);
                    intent3.putExtra("bottom", this.popWindow_bottom);
                    intent3.putExtra("left", rect.left);
                    intent3.putExtra("right", rect.right);
                    intent3.setClass(this, CustomAppsActivity.class);
                    startActivity(intent3);
                    IntoCustomActivity = true;
                }
            }
        } else if (keyCode == 84) {
            ComponentName globalSearchActivity = ((SearchManager) getSystemService(Context.SEARCH_SERVICE)).getGlobalSearchActivity();
            if (globalSearchActivity == null) {
                return false;
            }
            Intent intent4 = new Intent("android.search.action.GLOBAL_SEARCH");
            intent4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent4.setComponent(globalSearchActivity);
            Bundle appSearchData = new Bundle();
            appSearchData.putString("source", "launcher-search");
            intent4.putExtra("app_data", appSearchData);
            startActivity(intent4);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: private */
    public void displayDate() {
        this.is24hFormart = DateFormat.is24HourFormat(this);
        TextView time = (TextView) findViewById(R.id.tx_time);
        TextView date = (TextView) findViewById(R.id.tx_date);
        time.setText(getTime());
        time.setTypeface(Typeface.DEFAULT_BOLD);
        date.setText(getDate());
    }

    private void initStaticVariable() {
        isShowHomePage = true;
        dontRunAnim = false;
        dontDrawFocus = false;
        ifChangedShortcut = true;
        IntoCustomActivity = false;
        IntoApps = true;
        isAddButtonBeTouched = false;
        isInTouchMode = false;
        animIsRun = false;
        updateAllShortcut = true;
        animIsRun = false;
        cantGetDrawingCache = false;
        setHeight();
    }

    private void initChildViews() {
        this.pref = PreferenceManager.getDefaultSharedPreferences(this);
        this.editor = this.pref.edit();
        this.sharedPrekodi = getSharedPreferences("share", 0);
        this.bootsharedPrekodi = PreferenceManager.getDefaultSharedPreferences(this);
        bootisFirstRunkodi = this.bootsharedPrekodi.getBoolean("bootisFirstRunkodi", true);
        this.booteditorkodi = this.bootsharedPrekodi.edit();
        layoutScaleShadow = (RelativeLayout) findViewById(R.id.layout_focus_unit);
        trans_frameView = findViewById(R.id.img_trans_frame);
        viewHomePage = findViewById(R.id.layout_homepage);
        viewMenu = (MyViewFlipper) findViewById(R.id.menu_flipper);
        homeShortcutView = (MyGridLayout) findViewById(R.id.gv_shortcut);
        videoShortcutView = (MyGridLayout) findViewById(R.id.gv_shortcut_video);
        recommendShortcutView = (MyGridLayout) findViewById(R.id.gv_shortcut_recommend);
        appShortcutView = (MyGridLayout) findViewById(R.id.gv_shortcut_app);
        musicShortcutView = (MyGridLayout) findViewById(R.id.gv_shortcut_music);
        localShortcutView = (MyGridLayout) findViewById(R.id.gv_shortcut_local);
        tx_video_count = (TextView) findViewById(R.id.tx_video_count);
        this.tx_video_allcount = (TextView) findViewById(R.id.tx_video_allcount);
        tx_recommend_count = (TextView) findViewById(R.id.tx_recommend_count);
        this.tx_recommend_allcount = (TextView) findViewById(R.id.tx_recommend_allcount);
        tx_app_count = (TextView) findViewById(R.id.tx_app_count);
        this.tx_app_allcount = (TextView) findViewById(R.id.tx_app_allcount);
        tx_music_count = (TextView) findViewById(R.id.tx_music_count);
        this.tx_music_allcount = (TextView) findViewById(R.id.tx_music_allcount);
        tx_local_count = (TextView) findViewById(R.id.tx_local_count);
        this.tx_local_allcount = (TextView) findViewById(R.id.tx_local_allcount);
        tx_time = (TextView) findViewById(R.id.tx_time);
        tx_date = (TextView) findViewById(R.id.tx_date);
    }

    /* access modifiers changed from: private */
    public void displayShortcuts() {
        if (ifChangedShortcut) {
            StringBuilder sb = new StringBuilder();
            sb.append("===============================");
            sb.append(bootisFirstRunkodi);
            Log.d("mylog", sb.toString());
            loadApplications();
            ifChangedShortcut = false;
            if (!isShowHomePage) {
                if (this.numberInGrid == -1) {
                    new Thread(new Runnable() {
                        public void run() {
                            for (ViewGroup findGridLayout = null; findGridLayout == null; findGridLayout = (ViewGroup) ((ViewGroup) ((ViewGroup) Launcher.viewMenu.getCurrentView()).getChildAt(4)).getChildAt(0)) {
                            }
                            Launcher.this.mHandler.sendEmptyMessage(3);
                        }
                    }).start();
                } else {
                    new Thread(new Runnable() {
                        public void run() {
                            for (ViewGroup findGridLayout = null; findGridLayout == null; findGridLayout = (ViewGroup) ((ViewGroup) ((ViewGroup) Launcher.viewMenu.getCurrentView()).getChildAt(4)).getChildAt(0)) {
                            }
                            Launcher.this.mHandler.sendEmptyMessage(4);
                        }
                    }).start();
                }
            } else if (this.numberInGridOfShortcut != -1) {
                new Thread(new Runnable() {
                    public void run() {
                        do {
                        } while (Launcher.homeShortcutView.getChildAt(Launcher.this.numberInGridOfShortcut) == null);
                        Launcher.this.mHandler.sendEmptyMessage(5);
                    }
                }).start();
            } else if (IntoCustomActivity) {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(200);
                        } catch (Exception e) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("");
                            sb.append(e);
                            Log.d("MediaBoxLauncher", sb.toString());
                        }
                        Launcher.this.mHandler.sendEmptyMessage(6);
                    }
                }).start();
            }
        }
    }

    public String getTime() {
        String time;
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        this.is24hFormart = DateFormat.is24HourFormat(this);
        if (!this.is24hFormart && hour > 12) {
            hour -= 12;
        }
        if (!this.is24hFormart && hour == 0) {
            hour = 12;
        }
        String time2 = "";
        if (hour >= 10) {
            StringBuilder sb = new StringBuilder();
            sb.append(time2);
            sb.append(Integer.toString(hour));
            time = sb.toString();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(time2);
            sb2.append("0");
            sb2.append(Integer.toString(hour));
            time = sb2.toString();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(time);
        sb3.append(":");
        String time3 = sb3.toString();
        if (minute >= 10) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(time3);
            sb4.append(Integer.toString(minute));
            return sb4.toString();
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append(time3);
        sb5.append("0");
        sb5.append(Integer.toString(minute));
        return sb5.toString();
    }

    private String getDate() {
        Calendar c = Calendar.getInstance();
        int int_Month = c.get(Calendar.MONTH);
        String mDay = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        String str_week = getResources().getStringArray(R.array.week)[c.get(Calendar.DAY_OF_WEEK) - 1];
        String mMonth = getResources().getStringArray(R.array.month)[int_Month];
        if (Locale.getDefault().getLanguage().equals("zh")) {
            StringBuilder sb = new StringBuilder();
            sb.append(str_week);
            sb.append(", ");
            sb.append(mMonth);
            sb.append(" ");
            sb.append(mDay);
            sb.append(getResources().getString(R.string.str_day));
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str_week);
        sb2.append(", ");
        sb2.append(mMonth);
        sb2.append(" ");
        sb2.append(mDay);
        return sb2.toString();
    }

    private void loadCustomApps(String path) {
        File mFile = new File(path);
        File default_file = new File("/system/etc/default_shortcut.cfg");
        if (!mFile.exists()) {
            mFile = default_file;
            getShortcutFromDefault("/system/etc/default_shortcut.cfg", "/data/data/com.droidlogic.mboxlauncher/shortcut.cfg");
        } else {
            try {
                BufferedReader b = new BufferedReader(new FileReader(mFile));
                if (b.read() == -1) {
                    getShortcutFromDefault("/system/etc/default_shortcut.cfg", "/data/data/com.droidlogic.mboxlauncher/shortcut.cfg");
                }
                b.close();
            } catch (IOException e) {
            }
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(mFile));
            while (true) {
                String readLine = br.readLine();
                String str = readLine;
                if (readLine == null) {
                    try {
                        br.close();
                        return;
                    } catch (IOException e2) {
                        return;
                    }
                } else if (str.startsWith("Home_Shortcut:")) {
                    this.list_homeShortcut = str.replaceAll("Home_Shortcut:", "").split(";");
                } else if (str.startsWith("Video_Shortcut:")) {
                    this.list_videoShortcut = str.replaceAll("Video_Shortcut:", "").split(";");
                } else if (str.startsWith("Recommend_Shortcut:")) {
                    this.list_recommendShortcut = str.replaceAll("Recommend_Shortcut:", "").split(";");
                } else if (str.startsWith("Music_shortcut:")) {
                    this.list_musicShortcut = str.replaceAll("Music_shortcut:", "").split(";");
                } else if (str.startsWith("Local_Shortcut:")) {
                    this.list_localShortcut = str.replaceAll("Local_Shortcut:", "").split(";");
                }
            }
        } catch (Exception e3) {
            String str2 = "MediaBoxLauncher";
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(e3);
            Log.d(str2, sb.toString());

        } finally {

        }
    }

    public void getShortcutFromDefault(String srcPath, String desPath) {
        File srcFile = new File(srcPath);
        File desFile = new File(desPath);
        if (srcFile.exists()) {
            if (!desFile.exists()) {
                try {
                    desFile.createNewFile();
                } catch (Exception e) {
                    Log.e("MediaBoxLauncher", e.getMessage().toString());
                }
            }
            BufferedReader br = null;
            BufferedWriter bw = null;
            try {
                BufferedReader br2 = new BufferedReader(new FileReader(srcFile));
                List list = new ArrayList();
                while (true) {
                    String readLine = br2.readLine();
                    String str = readLine;
                    if (readLine == null) {
                        break;
                    }
                    list.add(str);
                }
                BufferedWriter bw2 = new BufferedWriter(new FileWriter(desFile));
                for (int i = 0; i < list.size(); i++) {
                    bw2.write(list.get(i).toString());
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
                String str2 = "MediaBoxLauncher";
                StringBuilder sb = new StringBuilder();
                sb.append("   ");
                sb.append(e4);
                Log.d(str2, sb.toString());
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

    private List<Map<String, Object>> loadShortcutList(PackageManager manager, List<LauncherActivityInfo> apps, String[] list_custom_apps) {
        ApplicationInfo application = new ApplicationInfo();
        List<Map<String, Object>> list = new ArrayList<>();
        int iconDpi = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getLauncherLargeIconDensity();
        if (list_custom_apps != null) {
            Map map = null;
            for (int i = 0; i < list_custom_apps.length; i++) {
                if (apps != null) {
                    int count = apps.size();
                    int j = 0;
                    while (true) {
                        if (j >= count) {
                            break;
                        }
                        LauncherActivityInfo info = (LauncherActivityInfo) apps.get(j);
                        application.title = info.getLabel().toString();
                        application.setActivity(info.getComponentName(), 270532608);
                        application.icon = info.getBadgedIcon(iconDpi);
                        if (!info.getComponentName().getPackageName().equals(list_custom_apps[i])) {
                            application.icon.setCallback(null);
                        } else if (!info.getComponentName().getPackageName().equals("com.android.gallery3d") || !application.intent.toString().contains("camera")) {
                            map = new HashMap();
                            map.put("item_name", application.title.toString());
                            map.put("file_path", application.intent);
                            map.put("item_type", application.icon);
                            map.put("item_symbol", application.componentName);
                            list.add(map);
                        }
                        j++;
                    }
                    map = new HashMap();
                    map.put("item_name", application.title.toString());
                    map.put("file_path", application.intent);
                    map.put("item_type", application.icon);
                    map.put("item_symbol", application.componentName);
                    list.add(map);
                }
            }
            Map map2 = map;
        }
        return list;
    }

    private Map<String, Object> getAddMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("item_name", getResources().getString(R.string.str_add));
        map.put("file_path", null);
        map.put("item_type", Integer.valueOf(R.drawable.item_img_add));
        return map;
    }

    private static final Comparator<LauncherActivityInfo> getAppNameComparator() {
        final Collator collator = Collator.getInstance();
        return new Comparator<LauncherActivityInfo>() {
            public final int compare(LauncherActivityInfo a, LauncherActivityInfo b) {
                if (!a.getUser().equals(b.getUser())) {
                    return a.getUser().toString().compareTo(b.getUser().toString());
                }
                int result = collator.compare(a.getLabel().toString(), b.getLabel().toString());
                if (result == 0) {
                    result = a.getName().compareTo(b.getName());
                }
                return result;
            }
        };
    }

    private void loadApplications() {
        List<Map<String, Object>> HomeShortCutList = new ArrayList<>();
        List<Map<String, Object>> videoShortCutList = new ArrayList<>();
        List<Map<String, Object>> recommendShortCutList = new ArrayList<>();
        List<Map<String, Object>> appShortCutList = new ArrayList<>();
        List<Map<String, Object>> musicShortCutList = new ArrayList<>();
        List<Map<String, Object>> localShortCutList = new ArrayList<>();
        PackageManager manager = getPackageManager();
        Intent mainIntent = new Intent("android.intent.action.MAIN", null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        LauncherApps launcherApps = (LauncherApps) getSystemService(Context.LAUNCHER_APPS_SERVICE);
        List<LauncherActivityInfo> apps = launcherApps.getActivityList(null, Process.myUserHandle());
        Collections.sort(apps, getAppNameComparator());
        loadCustomApps("/data/data/com.droidlogic.mboxlauncher/shortcut.cfg");
        if (updateAllShortcut) {
            HomeShortCutList = loadShortcutList(manager, apps, this.list_homeShortcut);
            videoShortCutList = loadShortcutList(manager, apps, this.list_videoShortcut);
            recommendShortCutList = loadShortcutList(manager, apps, this.list_recommendShortcut);
            musicShortCutList = loadShortcutList(manager, apps, this.list_musicShortcut);
            localShortCutList = loadShortcutList(manager, apps, this.list_localShortcut);
            ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            int iconDpi = activityManager.getLauncherLargeIconDensity();
            if (apps != null) {
                int count = apps.size();
                int i = 0;
                while (true) {
                    int i2 = i;
                    if (i2 >= count) {
                        break;
                    }
                    ApplicationInfo application = new ApplicationInfo();
                    Intent mainIntent2 = mainIntent;
                    LauncherActivityInfo info = (LauncherActivityInfo) apps.get(i2);
                    LauncherApps launcherApps2 = launcherApps;
                    application.title = info.getLabel().toString();
                    ActivityManager activityManager2 = activityManager;
                    application.setActivity(info.getComponentName(), 270532608);
                    application.icon = info.getBadgedIcon(iconDpi);
                    Map<String, Object> map = new HashMap<>();
                    LauncherActivityInfo launcherActivityInfo = info;
                    map.put("item_name", application.title.toString());
                    map.put("file_path", application.intent);
                    map.put("item_type", application.icon);
                    map.put("item_symbol", application.componentName);
                    appShortCutList.add(map);
                    application.icon.setCallback(null);
                    i = i2 + 1;
                    mainIntent = mainIntent2;
                    launcherApps = launcherApps2;
                    activityManager = activityManager2;
                }
            }
            LauncherApps launcherApps3 = launcherApps;
            ActivityManager activityManager3 = activityManager;
            Map<String, Object> map2 = getAddMap();
            HomeShortCutList.add(map2);
            videoShortCutList.add(map2);
            musicShortCutList.add(map2);
            localShortCutList.add(map2);
            homeShortcutView.setLayoutView(HomeShortCutList, 0);
            videoShortcutView.setLayoutView(videoShortCutList, 1);
            recommendShortcutView.setLayoutView(recommendShortCutList, 1);
            appShortcutView.setLayoutView(appShortCutList, 1);
            musicShortcutView.setLayoutView(musicShortCutList, 1);
            localShortcutView.setLayoutView(localShortCutList, 1);
            TextView textView = this.tx_video_allcount;
            StringBuilder sb = new StringBuilder();
            sb.append("/");
            sb.append(Integer.toString(videoShortCutList.size()));
            textView.setText(sb.toString());
            TextView textView2 = this.tx_recommend_allcount;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("/");
            sb2.append(Integer.toString(recommendShortCutList.size()));
            textView2.setText(sb2.toString());
            TextView textView3 = this.tx_app_allcount;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("/");
            sb3.append(Integer.toString(appShortCutList.size()));
            textView3.setText(sb3.toString());
            TextView textView4 = this.tx_music_allcount;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("/");
            sb4.append(Integer.toString(musicShortCutList.size()));
            textView4.setText(sb4.toString());
            TextView textView5 = this.tx_local_allcount;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("/");
            sb5.append(Integer.toString(localShortCutList.size()));
            textView5.setText(sb5.toString());
            updateAllShortcut = false;
        } else {
            LauncherApps launcherApps4 = launcherApps;
            if (current_shortcutHead.equals("Video_Shortcut:")) {
                videoShortCutList = loadShortcutList(manager, apps, this.list_videoShortcut);
                videoShortCutList.add(getAddMap());
                videoShortcutView.setLayoutView(videoShortCutList, 1);
                TextView textView6 = this.tx_video_allcount;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("/");
                sb6.append(Integer.toString(videoShortCutList.size()));
                textView6.setText(sb6.toString());
            } else if (current_shortcutHead.equals("Recommend_Shortcut:")) {
                recommendShortCutList = loadShortcutList(manager, apps, this.list_recommendShortcut);
                recommendShortcutView.setLayoutView(recommendShortCutList, 1);
                TextView textView7 = this.tx_recommend_allcount;
                StringBuilder sb7 = new StringBuilder();
                sb7.append("/");
                sb7.append(Integer.toString(recommendShortCutList.size()));
                textView7.setText(sb7.toString());
            } else if (current_shortcutHead.equals("Music_shortcut:")) {
                musicShortCutList = loadShortcutList(manager, apps, this.list_musicShortcut);
                musicShortCutList.add(getAddMap());
                musicShortcutView.setLayoutView(musicShortCutList, 1);
                TextView textView8 = this.tx_music_allcount;
                StringBuilder sb8 = new StringBuilder();
                sb8.append("/");
                sb8.append(Integer.toString(musicShortCutList.size()));
                textView8.setText(sb8.toString());
            } else if (current_shortcutHead.equals("Local_Shortcut:")) {
                localShortCutList = loadShortcutList(manager, apps, this.list_localShortcut);
                localShortCutList.add(getAddMap());
                localShortcutView.setLayoutView(localShortCutList, 1);
                TextView textView9 = this.tx_local_allcount;
                StringBuilder sb9 = new StringBuilder();
                sb9.append("/");
                sb9.append(Integer.toString(localShortCutList.size()));
                textView9.setText(sb9.toString());
            } else {
                HomeShortCutList = loadShortcutList(manager, apps, this.list_homeShortcut);
                HomeShortCutList.add(getAddMap());
                homeShortcutView.setLayoutView(HomeShortCutList, 0);
            }
        }
        HomeShortCutList.clear();
        videoShortCutList.clear();
        recommendShortCutList.clear();
        appShortCutList.clear();
        musicShortCutList.clear();
        localShortCutList.clear();
    }

    private void setRectOnKeyListener() {
        findViewById(R.id.layout_video).setOnKeyListener(new MyOnKeyListener(this, null));
        findViewById(R.id.layout_app).setOnKeyListener(new MyOnKeyListener(this, null));
        findViewById(R.id.layout_recommend).setOnKeyListener(new MyOnKeyListener(this, null));
        findViewById(R.id.layout_local).setOnKeyListener(new MyOnKeyListener(this, null));
        findViewById(R.id.layout_clean).setOnKeyListener(new MyOnKeyListener(this, null));
        findViewById(R.id.layout_time).setOnKeyListener(new MyOnKeyListener(this, null));
        findViewById(R.id.layout_nclean).setOnKeyListener(new MyOnKeyListener(this, null));
        findViewById(R.id.layout_weather).setOnKeyListener(new MyOnKeyListener(this, null));
        findViewById(R.id.layout_video).setOnTouchListener(new MyOnTouchListener(this, null));
        findViewById(R.id.layout_app).setOnTouchListener(new MyOnTouchListener(this, null));
        findViewById(R.id.layout_recommend).setOnTouchListener(new MyOnTouchListener(this, null));
        findViewById(R.id.layout_local).setOnTouchListener(new MyOnTouchListener(this, null));
        findViewById(R.id.layout_clean).setOnTouchListener(new MyOnTouchListener(this, null));
        findViewById(R.id.layout_time).setOnTouchListener(new MyOnTouchListener(this, null));
        findViewById(R.id.layout_nclean).setOnTouchListener(new MyOnTouchListener(this, null));
        findViewById(R.id.layout_weather).setOnTouchListener(new MyOnTouchListener(this, null));
    }

    /* access modifiers changed from: private */
    public void setHeight() {
        REAL_OUTPUT_MODE = "1080p";
        CustomAppsActivity.CONTENT_HEIGHT = 450;
        Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getRealSize(p);
        SCREEN_HEIGHT = p.y;
        SCREEN_WIDTH = p.x;
    }

    public void setPopWindow(int top, int bottom) {
        View view = getWindow().getDecorView();
        view.layout(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        view.setDrawingCacheEnabled(true);
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
        view.destroyDrawingCache();
        screenShot = null;
        screenShot_keep = null;
        if (bottom <= SCREEN_HEIGHT / 2) {
            screenShot = Bitmap.createBitmap(bmp, 0, bottom, bmp.getWidth(), SCREEN_HEIGHT - bottom);
            screenShot_keep = Bitmap.createBitmap(bmp, 0, bottom, bmp.getWidth(), SCREEN_HEIGHT - (CustomAppsActivity.CONTENT_HEIGHT + bottom));
        } else if ((top + 3) - CustomAppsActivity.CONTENT_HEIGHT > 0) {
            screenShot = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), top);
            screenShot_keep = Bitmap.createBitmap(bmp, 0, CustomAppsActivity.CONTENT_HEIGHT, bmp.getWidth(), (top + 3) - CustomAppsActivity.CONTENT_HEIGHT);
        } else {
            screenShot = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), CustomAppsActivity.CONTENT_HEIGHT);
            screenShot_keep = null;
        }
    }

    private void sendWeatherBroadcast() {
        Intent intent = new Intent();
        intent.setAction("android.amlogic.launcher.REQUEST_WEATHER");
        sendBroadcast(intent);
    }

    public static int parseItemIcon(String packageName) {
        if (packageName.equals("com.droidlogic.FileBrower")) {
            return R.drawable.icon_filebrowser;
        }
        if (packageName.equals("com.android.browser")) {
            return R.drawable.icon_browser;
        }
        if (packageName.equals("com.droidlogic.appinstall")) {
            return R.drawable.icon_appinstaller;
        }
        if (packageName.equals("com.android.tv.settings")) {
            return R.drawable.icon_setting;
        }
        if (packageName.equals("com.droidlogic.mediacenter")) {
            return R.drawable.icon_mediacenter;
        }
        if (packageName.equals("com.droidlogic.otaupgrade")) {
            return R.drawable.icon_backupandupgrade;
        }
        if (packageName.equals("com.android.gallery3d")) {
            return R.drawable.icon_pictureplayer;
        }
        if (packageName.equals("com.droidlogic.miracast")) {
            return R.drawable.icon_miracast;
        }
        if (packageName.equals("com.droidlogic.PPPoE")) {
            return R.drawable.icon_pppoe;
        }
        if (packageName.equals("com.android.music")) {
            return R.drawable.icon_music;
        }
        if (packageName.equals("com.android.camera2")) {
            return R.drawable.icon_camera;
        }
        return -1;
    }

    private void sendKeyCode(final int keyCode) {
        new Thread() {
            public void run() {
                try {
                    new Instrumentation().sendKeyDownUpSync(keyCode);
                } catch (Exception e) {
                    Log.e("Exception", e.toString());
                }
            }
        }.start();
    }

    private void resetShadow() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(e);
                    Log.d("MediaBoxLauncher", sb.toString());
                }
                Launcher.this.mHandler.sendEmptyMessage(2);
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public void updateAppList(Intent intent) {
        boolean isShortcutIndex = false;
        if (intent.getData() != null) {
            String packageName = intent.getData().getSchemeSpecificPart();
            if (packageName == null || packageName.length() == 0 || packageName.equals("com.android.provision")) {
                return;
            }
        }
        if (getCurrentFocus() == null || !(getCurrentFocus().getParent() instanceof MyGridLayout)) {
            this.numberInGrid = -1;
        } else {
            int parentId = ((MyGridLayout) getCurrentFocus().getParent()).getId();
            dontRunAnim = true;
            if (parentId != -1 && getResources().getResourceEntryName(parentId).equals("gv_shortcut")) {
                this.numberInGridOfShortcut = ((MyGridLayout) getCurrentFocus().getParent()).indexOfChild(getCurrentFocus());
                isShortcutIndex = true;
            }
            if (!isShortcutIndex) {
                this.numberInGrid = ((MyGridLayout) getCurrentFocus().getParent()).indexOfChild(getCurrentFocus());
            }
        }
        updateAllShortcut = true;
        ifChangedShortcut = true;
        displayShortcuts();
    }

    private void setAnimationScale(boolean enable_animation) {
        if (enable_animation) {
            try {
                this.mWindowManager.setAnimationScale(1, scale_value);
            } catch (RemoteException e) {
            }
        } else {
            try {
                this.mWindowManager.setAnimationScale(1, 0.0f);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private float getAnimationScaleValue() {
        try {
            return this.mWindowManager.getAnimationScale(1);
        } catch (RemoteException e) {
            return 0.0f;
        }
    }
}
