package com.droidlogic.mboxlauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.droidlogic.Weather.Utils;
import com.hcy.launcher.R;

public class StatusBar extends RelativeLayout {
    private final String SDCARD_FILE_NAME = "sdcard";
    private final String STORAGE_PATH = "/storage";
    private final String UDISK_FILE_NAME = "udisk";
    private boolean mAttached;
    private ConnectivityManager mConnectivityManager;
    private Context mContext;
    private boolean mFocused;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    StatusBar.this.updateNetworkState();
                    return;
                case 2:
                    if (1 == msg.arg1) {
                        StatusBar.this.updateMediaSdcardStatus(true);
                        return;
                    } else {
                        StatusBar.this.updateMediaSdcardStatus(false);
                        return;
                    }
                case 3:
                    if (1 == msg.arg2) {
                        StatusBar.this.updateMediaUsbStatus(true);
                        return;
                    } else {
                        StatusBar.this.updateMediaUsbStatus(false);
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private NetworkInfo mNetworkInfo;
    private NetworkStateChangeReceiver mNetworkStateChangedReceiver = new NetworkStateChangeReceiver();
    private View mStatusBarContainer;
    private WifiManager mWifiManager;
    private BroadcastReceiver mediaReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.MEDIA_EJECT".equals(action) || "android.intent.action.MEDIA_UNMOUNTED".equals(action) || "android.intent.action.MEDIA_MOUNTED".equals(action)) {
                StatusBar.this.storageYN(context);
            }
        }
    };
    private int netWortmsg = 1;
    private int sdcardStatusmsg = 2;
    private String tag = "StatusBar";
    private int usbStatusmsg = 3;
    private ImageView vEthernet;
    private ImageView vSdcard;
    private ImageView vUsb;
    private ImageView vWifi;

    class NetworkStateChangeReceiver extends BroadcastReceiver {
        NetworkStateChangeReceiver() {
        }

        public void onReceive(Context mContext, Intent netWorkIntent) {
            if (netWorkIntent != null) {
                String str = netWorkIntent.getAction();
                if ("android.net.conn.CONNECTIVITY_CHANGE".equals(str) || "android.net.wifi.RSSI_CHANGED".equals(str)) {
                    StatusBar.this.mHandler.removeMessages(1);
                }
                StatusBar.this.mHandler.sendEmptyMessage(1);
            }
        }
    }

    public StatusBar(Context mContext2) {
        super(mContext2);
        initializeStatusBar(mContext2);
    }

    public StatusBar(Context mContext2, AttributeSet mAttributeSet) {
        super(mContext2, mAttributeSet);
        initializeStatusBar(mContext2);
    }

    private int getLevelResId(int paramInt) {
        switch (paramInt) {
            case 0:
                return R.drawable.wifi1;
            case 1:
                return R.drawable.wifi2;
            case 2:
                return R.drawable.wifi3;
            case 3:
                return R.drawable.wifi4;
            case 4:
                return R.drawable.wifi5;
            default:
                return R.drawable.wifi1;
        }
    }

    private void initializeStatusBar(Context paramContext) {
        this.mContext = paramContext;
        this.mStatusBarContainer = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.inflater_layout_status_bar, this);
        this.vEthernet = (ImageView) this.mStatusBarContainer.findViewById(R.id.status_ethernet);
        this.vWifi = (ImageView) this.mStatusBarContainer.findViewById(R.id.status_wifi);
        this.vUsb = (ImageView) this.mStatusBarContainer.findViewById(R.id.status_usb);
        this.mConnectivityManager = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.mWifiManager = (WifiManager) this.mContext.getSystemService(Context.WIFI_SERVICE);
    }

    /* access modifiers changed from: private */
    public void updateMediaSdcardStatus(boolean sdStatusBoolean) {
        if (sdStatusBoolean) {
            this.vSdcard.setImageResource(R.drawable.img_status_sdcard);
        } else {
            this.vSdcard.setImageResource(R.drawable.img_status_sdcard1);
        }
    }

    /* access modifiers changed from: private */
    public void updateMediaUsbStatus(boolean paramBoolean) {
        if (paramBoolean) {
            this.vUsb.setImageResource(R.drawable.img_status_usb);
        } else {
            this.vUsb.setImageResource(R.drawable.img_status_usb1);
        }
    }

    /* access modifiers changed from: private */
    public void updateNetworkState() {
        if (Utils.getNetworkConnState(this.mContext)) {
            this.mNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
            if (this.mNetworkInfo.getType() == 1) {
                this.vEthernet.setImageResource(R.drawable.img_status_ethernet1);
                this.vWifi.setVisibility(VISIBLE);
                this.vWifi.setImageResource(getLevelResId(WifiManager.calculateSignalLevel(this.mWifiManager.getConnectionInfo().getRssi(), 5)));
            } else if (this.mNetworkInfo.getType() == 9) {
                this.vWifi.setImageResource(R.drawable.wifi1);
                this.vEthernet.setImageResource(R.drawable.img_status_ethernet);
            }
        } else {
            this.vEthernet.setImageResource(R.drawable.img_status_ethernet1);
            this.vWifi.setImageResource(R.drawable.wifi1);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mAttached) {
            IntentFilter NetWorkIntentFilter = new IntentFilter();
            NetWorkIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            NetWorkIntentFilter.addAction("android.net.wifi.RSSI_CHANGED");
            this.mContext.registerReceiver(this.mNetworkStateChangedReceiver, NetWorkIntentFilter);
            IntentFilter MediaIntentFilter = new IntentFilter();
            MediaIntentFilter.addAction("android.intent.action.MEDIA_EJECT");
            MediaIntentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
            MediaIntentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
            MediaIntentFilter.addAction("android.intent.action.MEDIA_REMOVED");
            MediaIntentFilter.addDataScheme("file");
            this.mContext.registerReceiver(this.mediaReceiver, MediaIntentFilter);
            storageYN(this.mContext);
            this.mAttached = true;
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mAttached) {
            this.mContext.unregisterReceiver(this.mediaReceiver);
            this.mAttached = false;
        }
    }

    public void onWindowFocusChanged(boolean focusBoolean) {
        super.onWindowFocusChanged(focusBoolean);
        this.mFocused = focusBoolean;
        if (focusBoolean) {
            this.mHandler.removeMessages(65280);
            this.mHandler.removeMessages(65281);
            this.mHandler.sendEmptyMessage(65281);
        }
    }

    // TODO: 19-11-30 listen usb state
    public void storageYN(Context context) {
        StorageManager storageManagerService= (StorageManager) this.mContext.getSystemService(Context.STORAGE_SERVICE);
        for (VolumeInfo vol : storageManagerService.getVolumes()) {
            if (vol.getDisk() == null || !vol.getDisk().isUsb()) {
                this.vUsb.setImageResource(R.drawable.img_status_usb1);
            } else {
                this.vUsb.setImageResource(R.drawable.img_status_usb);
            }
            if (vol.getDisk() != null && vol.getDisk().isSd()) {
                VolumeInfo sdcardVolume = vol;
            }
        }
    }
}
