package com.xinkao.electronic_scale.page.test;

import android.view.LayoutInflater;

import com.dtcnet.skmvp.mvp.view.BaseFragment;
import com.xinkao.electronic_scale.databinding.FragmentDemoBinding;

public class DemoFragment extends BaseFragment<DemoContract.P, FragmentDemoBinding> implements DemoContract.V {
    @Override
    public FragmentDemoBinding viewBinding(LayoutInflater inflater) {
        return FragmentDemoBinding.inflate(inflater);
    }
}
