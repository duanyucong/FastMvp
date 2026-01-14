package com.xinkao.electronic_scale.page.test.dagger.component;

import com.xinkao.electronic_scale.page.test.dagger.module.DemoModule;
import com.xinkao.electronic_scale.page.test.DemoFragment;
import com.dtcnet.skmvp.dagger.scope.FragmentScope;

import dagger.Component;

@FragmentScope
@Component(modules = {DemoModule.class})
public interface DemoComponent {
    void Inject(DemoFragment arg);
}