package com.xinkao.electronic_scale.page.main;

import android.view.LayoutInflater;

import com.dtcnet.skmvp.dagger.component.AppComponent;
import com.dtcnet.skmvp.mvp.view.BaseActivity;
import com.xinkao.electronic_scale.databinding.ActivityMainBinding;
import com.xinkao.electronic_scale.page.main.dagger.component.DaggerMainComponent;
import com.xinkao.electronic_scale.page.main.dagger.module.MainModule;

public class MainActivity extends BaseActivity<MainContract.P, ActivityMainBinding> implements MainContract.V {

    @Override
    public void registerDagger(AppComponent appComponent) {
        DaggerMainComponent.builder()
                .mainModule(new MainModule(this))
                .build()
                .Inject(this);
    }

    @Override
    public ActivityMainBinding viewBinding(LayoutInflater layoutInflater) {
        return ActivityMainBinding.inflate(layoutInflater);
    }
}