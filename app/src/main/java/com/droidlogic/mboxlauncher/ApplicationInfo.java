package com.droidlogic.mboxlauncher;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;

class ApplicationInfo {
    ComponentName componentName;
    Drawable icon;
    Intent intent;
    CharSequence title;

    ApplicationInfo() {
    }

    /* access modifiers changed from: 0000 */
    public final void setActivity(ComponentName className, int launchFlags) {
        this.intent = new Intent("android.intent.action.MAIN");
        this.intent.addCategory("android.intent.category.LAUNCHER");
        this.intent.setComponent(className);
        this.intent.setFlags(launchFlags);
        this.componentName = className;
    }
}
