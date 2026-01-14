package com.xinkao.electronic_scale.page.test;

import com.dtcnet.skmvp.mvp.presenter.BasePresenter;

import javax.inject.Inject;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;

public class DemoPresenter extends BasePresenter<DemoContract.V, DemoContract.M> implements DemoContract.P {

    @Inject
    public DemoPresenter(DemoContract.V mView, DemoContract.M mModel) {
        super(mView, mModel);
    }
}