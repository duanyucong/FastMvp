package com.xinkao.electronic_scale.page.main.dagger.component;

import com.xinkao.electronic_scale.page.main.dagger.module.MainModule;
import com.xinkao.electronic_scale.page.main.MainActivity;
import com.dtcnet.skmvp.dagger.scope.ActivityScope;

import dagger.Component;

@ActivityScope
@Component(modules = {MainModule.class})
public interface MainComponent {
    void Inject(MainActivity arg);
}