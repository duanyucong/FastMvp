package com.xinkao.electronic_scale.page.main;

import com.dtcnet.skmvp.mvp.presenter.BasePresenter;

import javax.inject.Inject;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

public class MainPresenter extends BasePresenter<MainContract.V, MainContract.M> implements MainContract.P {

    @Inject
    public MainPresenter(MainContract.V mView, MainContract.M mModel) {
        super(mView, mModel);
    }
}