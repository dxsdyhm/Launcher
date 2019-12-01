package com.droidlogic.mboxlauncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.hcy.launcher.R;

public class MyRelativeLayouttandw extends RelativeLayout {
    private static Rect imgRect;
    private final int MODE_CHILD_SHORTCUT = 2;
    private final int MODE_HOME_RECT = 0;
    private final int MODE_HOME_SHORTCUT = 1;
    private int animDelay = 0;
    private int animDuration;
    private float framePara = 1.09f;
    private Context mContext = null;
    private float scalePara = 1.1f;
    private float shortcutScalePara = 1.1f;

    public class ScaleAnimationListener implements AnimationListener {
        public ScaleAnimationListener() {
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            if (!Launcher.animIsRun) {
                Launcher.layoutScaleShadow.setVisibility(VISIBLE);
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    public MyRelativeLayouttandw(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        if (Launcher.isRealOutputMode) {
            this.animDuration = 70;
        } else {
            this.animDuration = 90;
        }
    }

    public MyRelativeLayouttandw(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        setAddShortcutHead();
        if (gainFocus && !Launcher.isInTouchMode && !Launcher.dontDrawFocus) {
            setNumberOfScreen();
            if (Launcher.prevFocusedView == null || (!isParentSame(this, Launcher.prevFocusedView) && !Launcher.isShowHomePage)) {
                if (Launcher.isShowHomePage || Launcher.dontRunAnim || Launcher.IntoCustomActivity) {
                    Launcher.IntoCustomActivity = false;
                    setSurface();
                }
            } else if (!Launcher.dontRunAnim && !Launcher.IntoCustomActivity) {
                Launcher.layoutScaleShadow.setVisibility(INVISIBLE);
                Rect preRect = new Rect();
                Launcher.prevFocusedView.getGlobalVisibleRect(preRect);
                setShadowEffect();
                startFrameAnim(preRect);
            } else if (!Launcher.IntoCustomActivity || !Launcher.isShowHomePage || !Launcher.ifChangedShortcut) {
                Launcher.dontRunAnim = false;
                setSurface();
            }
        } else if (!Launcher.isInTouchMode) {
            Launcher.prevFocusedView = this;
            if (!Launcher.dontRunAnim) {
                ScaleAnimation scaleAnimation = new ScaleAnimation(1.07f, 1.0f, 1.07f, 1.0f, 1, 0.5f, 1, 0.5f);
                scaleAnimation.setZAdjustment(1);
                scaleAnimation.setDuration((long) this.animDuration);
                scaleAnimation.setStartTime((long) this.animDelay);
                if (!(getParent() instanceof MyGridLayout)) {
                    bringToFront();
                    ((View) getParent()).bringToFront();
                    Launcher.viewHomePage.bringToFront();
                }
                startAnimation(scaleAnimation);
            }
        }
    }

    private void setAddShortcutHead() {
        View parent = (View) getParent();
        if (parent == Launcher.videoShortcutView) {
            Launcher.current_shortcutHead = "Video_Shortcut:";
        } else if (parent != Launcher.recommendShortcutView && parent != Launcher.musicShortcutView) {
            if (parent == Launcher.localShortcutView) {
                Launcher.current_shortcutHead = "Local_Shortcut:";
            } else {
                Launcher.current_shortcutHead = "Home_Shortcut:";
            }
        }
    }

    private void setNumberOfScreen() {
        if (getParent() instanceof MyGridLayout) {
            MyGridLayout parent = (MyGridLayout) getParent();
            if (parent == Launcher.videoShortcutView) {
                Launcher.tx_video_count.setText(Integer.toString(Launcher.videoShortcutView.indexOfChild(this) + 1));
            } else if (parent == Launcher.recommendShortcutView) {
                Launcher.tx_recommend_count.setText(Integer.toString(Launcher.recommendShortcutView.indexOfChild(this) + 1));
            } else if (parent == Launcher.appShortcutView) {
                Launcher.tx_app_count.setText(Integer.toString(Launcher.appShortcutView.indexOfChild(this) + 1));
            } else if (parent == Launcher.musicShortcutView) {
                Launcher.tx_music_count.setText(Integer.toString(Launcher.musicShortcutView.indexOfChild(this) + 1));
            } else if (parent == Launcher.localShortcutView) {
                Launcher.tx_local_count.setText(Integer.toString(Launcher.localShortcutView.indexOfChild(this) + 1));
            }
        }
    }

    private void startFrameAnim(Rect preRect) {
        imgRect = new Rect();
        getGlobalVisibleRect(imgRect);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.95f, 1.0f, 0.95f, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleAnimation.setDuration((long) this.animDuration);
        scaleAnimation.setStartTime((long) this.animDelay);
        scaleAnimation.setAnimationListener(new ScaleAnimationListener());
        Launcher.layoutScaleShadow.startAnimation(scaleAnimation);
    }

    public void setSurface() {
        setShadowEffect();
        if (!Launcher.animIsRun) {
            Launcher.layoutScaleShadow.setVisibility(VISIBLE);
        }
    }

    public void setShadowEffect() {
        float bgScalePara;
        String texttime = null;
        Launcher.layoutScaleShadow.bringToFront();
        int screen_mode = getScreenMode(this);
        imgRect = new Rect();
        getGlobalVisibleRect(imgRect);
        ImageView img1 = (ImageView) getChildAt(0);
        String path = img1.getResources().getResourceName(img1.getId());
        String vName = path.substring(path.indexOf("/") + 1);
        ImageView scaleImage = (ImageView) Launcher.layoutScaleShadow.findViewById(R.id.img_focus_unit);
        TextView scaleTexttime = (TextView) Launcher.layoutScaleShadow.findViewById(R.id.tx_focus_unit);
        if (screen_mode == 1) {
            bgScalePara = this.shortcutScalePara;
        } else {
            bgScalePara = this.scalePara;
        }
        ImageView img = (ImageView) getChildAt(0);
        img.buildDrawingCache();
        Bitmap bmp = img.getDrawingCache();
        if (bmp == null) {
            Launcher.cantGetDrawingCache = true;
            return;
        }
        Launcher.cantGetDrawingCache = false;
        Bitmap scaleBitmap = zoomBitmap(bmp, (int) (((float) imgRect.width()) * bgScalePara), (int) (((float) imgRect.height()) * bgScalePara));
        img.destroyDrawingCache();
        if (getChildAt(1) instanceof TextView) {
            texttime = ((TextView) getChildAt(1)).getText().toString();
        }
        Bitmap shadowBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), getShadow(getChildAt(0), screen_mode));
        int layout_width = (shadowBitmap.getWidth() - imgRect.width()) / 2;
        ImageView imageView = img1;
        int layout_height = (shadowBitmap.getHeight() - imgRect.height()) / 2;
        String str = path;
        Bitmap bitmap = shadowBitmap;
        float f = bgScalePara;
        ImageView imageView2 = img;
        Rect layoutRect = new Rect(imgRect.left - layout_width, imgRect.top - layout_height, imgRect.right + layout_width, imgRect.bottom + layout_height);
        Launcher.layoutScaleShadow.setBackgroundResource(0);
        if (screen_mode == 0) {
            scaleImage.setImageResource(getFocusImage(getChildAt(0)));
        } else {
            scaleImage.setImageBitmap(scaleBitmap);
        }
        if (texttime != null) {
            setTextMarginAndSize(scaleTexttime, screen_mode, vName);
            scaleTexttime.setText(texttime);
        } else {
            scaleTexttime.setText(null);
        }
        if (screen_mode != 0) {
            Launcher.layoutScaleShadow.setBackgroundResource(getShadow(getChildAt(0), screen_mode));
        }
        setViewPosition(Launcher.layoutScaleShadow, layoutRect);
    }

    private void setTextMarginAndSize(TextView text, int screen_mode, String vName) {
        LayoutParams para = new LayoutParams(-2, -2);
        text.setTextColor(this.mContext.getResources().getInteger(R.color.btn_text_color));
        if (screen_mode != 0) {
            para.addRule(14);
            para.setMargins(65, 30, 65, 10);
            text.setLayoutParams(para);
            text.setTextSize(33.0f);
            Log.e("mylog", "---------------texttime---else");
        }
    }

    private void setViewPosition(View view, Rect rect) {
        AbsoluteLayout.LayoutParams lp = new AbsoluteLayout.LayoutParams(-2, -2, 0, 0);
        lp.width = rect.width();
        lp.height = rect.height();
        lp.x = rect.left;
        lp.y = rect.top;
        view.setLayoutParams(lp);
    }

    private int getScreenMode(ViewGroup view) {
        View img = view.getChildAt(0);
        View tx = view.getChildAt(1);
        String path = img.getResources().getResourceName(img.getId());
        String vName = path.substring(path.indexOf("/") + 1);
        if (vName.equals("img_local") || vName.equals("img_video") || vName.equals("img_setting") || vName.equals("img_app") || vName.equals("img_music") || vName.equals("img_browser") || vName.equals("img_recommend") || vName.equals("img_clean")) {
            return 0;
        }
        if (tx == null) {
            return 1;
        }
        this.framePara = 1.06f;
        return 2;
    }

    private boolean isParentSame(View view1, View view2) {
        if (((ViewGroup) view1.getParent()).indexOfChild(view2) == -1) {
            return false;
        }
        return true;
    }

    private int getFocusImage(View img) {
        String path = img.getResources().getResourceName(img.getId());
        String vName = path.substring(path.indexOf("/") + 1);
        if (vName.equals("img_recommend")) {
            return R.drawable.img_recommend1;
        }
        if (vName.equals("img_video")) {
            return R.drawable.img_google1;
        }
        if (vName.equals("img_setting")) {
            return R.drawable.img_setting1;
        }
        if (vName.equals("img_app")) {
            return R.drawable.img_app1;
        }
        if (vName.equals("img_music")) {
            return R.drawable.img_music1;
        }
        if (vName.equals("img_local")) {
            return R.drawable.img_video1;
        }
        if (vName.equals("img_browser")) {
            return R.drawable.img_browser1;
        }
        if (vName.equals("img_clean")) {
            return R.drawable.img_clean1;
        }
        if (vName.equals("img_weather")) {
            return R.drawable.img_weather1;
        }
        if (vName.equals("img_time")) {
            return R.drawable.img_time1;
        }
        return -1;
    }

    private int getShadow(View img, int mode) {
        String path = img.getResources().getResourceName(img.getId());
        String vName = path.substring(path.indexOf("/") + 1);
        if (vName.equals("img_recommend")) {
            return R.drawable.img_recommend1;
        }
        if (vName.equals("img_video")) {
            return R.drawable.img_google1;
        }
        if (vName.equals("img_setting")) {
            return R.drawable.img_setting1;
        }
        if (vName.equals("img_app")) {
            return R.drawable.img_app1;
        }
        if (vName.equals("img_music")) {
            return R.drawable.img_music1;
        }
        if (vName.equals("img_local")) {
            return R.drawable.img_video1;
        }
        if (vName.equals("img_browser")) {
            return R.drawable.img_browser1;
        }
        if (vName.equals("img_clean")) {
            return R.drawable.img_clean1;
        }
        if (vName.equals("img_weather")) {
            return R.drawable.img_weather1;
        }
        if (vName.equals("img_time")) {
            return R.drawable.img_time1;
        }
        if (mode == 2) {
            return R.drawable.shadow_child_shortcut;
        }
        if (mode == 1) {
            return R.drawable.shadow_shortcut;
        }
        return -1;
    }

    public Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) w) / ((float) width), ((float) h) / ((float) height));
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}
