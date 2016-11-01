package zzh.project.stocksystem.ui.stockdetail;

import zzh.project.stocksystem.bean.StockDetailBean;
import zzh.project.stocksystem.ui.base.BasePresenter;
import zzh.project.stocksystem.ui.base.BaseView;

interface StockDetailContract {
    interface View extends BaseView {
        // 显示股票信息
        void showStockDetail(StockDetailBean detail);

        // 显示无法找到
        void showNotFound();

        // 股票代号
        String getGid();

        // 显示未关注
        void showUnFavor();

        // 显示关注
        void showAlreadyFavor();
    }

    interface Presenter extends BasePresenter {
        // 加载股票信息
        void loadStockDetail();

        // 关注
        void favor();

        // 取消关注
        void cancelFavor();
    }
}