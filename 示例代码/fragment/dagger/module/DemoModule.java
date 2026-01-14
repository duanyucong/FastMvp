package com.xinkao.electronic_scale.page.test.dagger.module;

import com.xinkao.electronic_scale.page.test.DemoContract;
import com.xinkao.electronic_scale.page.test.DemoModel;
import com.xinkao.electronic_scale.page.test.DemoPresenter;
import com.dtcnet.skmvp.dagger.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

@Module
public class DemoModule {

    private DemoContract.V v;

    public DemoModule(DemoContract.V v) {
        this.v = v;
    }

    @FragmentScope
    @Provides
    DemoContract.V provideDemoView() {
        return v;
    }

    @FragmentScope
    @Provides
    DemoContract.M provideDemoModel(DemoModel model) {
        return model;
    }

    @FragmentScope
    @Provides
    DemoContract.P provideDemoPresenter(DemoPresenter presenter) {
        return presenter;
    }
}