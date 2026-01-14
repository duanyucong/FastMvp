package com.xinkao.electronic_scale.page.main.dagger.module;

import com.xinkao.electronic_scale.page.main.MainContract;
import com.xinkao.electronic_scale.page.main.MainModel;
import com.xinkao.electronic_scale.page.main.MainPresenter;
import com.dtcnet.skmvp.dagger.scope.ActivityScope;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private MainContract.V v;

    public MainModule(MainContract.V v) {
        this.v = v;
    }

    @ActivityScope
    @Provides
    MainContract.V provideMainView() {
        return v;
    }

    @ActivityScope
    @Provides
    MainContract.M provideMainModel(MainModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    MainContract.P provideMainPresenter(MainPresenter presenter) {
        return presenter;
    }
}