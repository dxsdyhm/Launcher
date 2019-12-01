package com.hcy.launcher;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.VerticalGridSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.ListRowPresenter;
import androidx.leanback.widget.VerticalGridPresenter;

import com.blankj.utilcode.util.AppUtils;
import com.hcy.presenter.AppLaunchPresenter;

public class HomeFragment extends VerticalGridSupportFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadRows();
    }

    private void loadRows() {
        ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new AppLaunchPresenter());
        rowsAdapter.addAll(0,AppUtils.getAppsInfo());
        setAdapter(rowsAdapter);

        VerticalGridPresenter gridPresenter = new VerticalGridPresenter();
        gridPresenter.setNumberOfColumns(8);
        setGridPresenter(gridPresenter);
    }
}
