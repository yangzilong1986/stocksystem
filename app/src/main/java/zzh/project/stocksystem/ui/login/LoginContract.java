package zzh.project.stocksystem.ui.login;

import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.ui.base.BaseView;

interface LoginContract {
    interface View extends BaseView {
        // 获取用户名
        String getUsername();

        // 获取密码
        String getPassword();

        // 清空用户名
        void clearUsername();

        // 清空密码
        void clearPassword();

        // 设置用户名
        void setUsername(String username);

        void toMainActivity();
    }

    interface Presenter extends BasePresenter {
        // 登陆
        void login();
    }
}
