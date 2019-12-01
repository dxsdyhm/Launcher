package com.hcy.presenter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.leanback.widget.ImageCardView;
import androidx.leanback.widget.Presenter;
import com.blankj.utilcode.util.AppUtils;
import com.hcy.launcher.R;

public class AppLaunchPresenter extends Presenter {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app,null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        AppUtils.AppInfo video = (AppUtils.AppInfo) item;
        ImageView ivIcon=viewHolder.view.findViewById(R.id.iv_icon);
        TextView txName=viewHolder.view.findViewById(R.id.item_name);

        ivIcon.setImageDrawable(video.getIcon());
        txName.setText(video.getName());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }
}
