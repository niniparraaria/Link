package com.link.activity.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<Presenter extends BasePresenter> extends AppCompatActivity {

    protected Presenter presenter;

    @NonNull
    protected abstract Presenter createPresenter();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroyView();
    }
}
