package com.xinkao.electronic_scale.page.main;

import com.dtcnet.skmvp.mvp.model.IModel;
import com.dtcnet.skmvp.mvp.presenter.IPresenter;
import com.dtcnet.skmvp.mvp.view.IView;

public interface MainContract {
    interface V extends IView{}
    interface P extends IPresenter{}
    interface M extends IModel{}
    interface Net {}
}