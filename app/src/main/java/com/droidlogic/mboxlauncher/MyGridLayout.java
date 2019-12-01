package com.droidlogic.mboxlauncher;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcy.launcher.R;

import java.util.List;
import java.util.Map;

public class MyGridLayout extends GridLayout {
    private Context mContext;

    public MyGridLayout(Context context) {
        super(context);
    }

    public MyGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public MyGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setLayoutView(List<Map<String, Object>> list, int flag) {
        ViewGroup view;
        int count = 0;
        if (getChildCount() > 0) {
            removeAllViews();
        }
        for (Map<String, Object> m : list) {
            count++;
            if (flag == 0) {
                view = (ViewGroup) View.inflate(this.mContext, R.layout.homegrid_item, null);
            } else {
                view = (ViewGroup) View.inflate(this.mContext, R.layout.childgrid_item, null);
                ((TextView) view.getChildAt(1)).setText((String) m.get("item_name"));
            }
            ImageView img_bg = (ImageView) view.getChildAt(0);
            img_bg.setBackgroundResource(parseItemBackground(count, flag));
            if (m.get("item_type") instanceof Drawable) {
                int resId = Launcher.parseItemIcon(((ComponentName) m.get("item_symbol")).getPackageName());
                if (resId != -1) {
                    img_bg.setImageResource(resId);
                } else {
                    img_bg.setImageDrawable((Drawable) m.get("item_type"));
                }
            } else {
                img_bg.setImageResource(R.drawable.item_img_add);
                img_bg.setContentDescription("img_add");
            }
            view.setOnKeyListener(new MyOnKeyListener(this.mContext, m.get("file_path")));
            view.setOnTouchListener(new MyOnTouchListener(this.mContext, m.get("file_path")));
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            addView(view);
        }
    }

    private int parseItemBackground(int num, int flag) {
        if (flag == 0) {
            switch (num % 11) {
                case 0:
                    return R.drawable.item_child_6;
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
                default:
                    return R.drawable.item_child_1;
            }
        } else {
            switch (num % 18) {
                case 0:
                    return R.drawable.item_child_4;
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
                default:
                    return R.drawable.item_child_1;
            }
        }
    }
}
