package com.guyuan.handlein.base.api;

import android.text.TextUtils;
import androidx.lifecycle.MutableLiveData;
import com.example.httplibrary.bean.ErrorResultBean;
import com.example.httplibrary.bean.ResultBean;
import com.example.mvvmlibrary.base.data.BaseViewModel;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;


/**
 * @author : tl
 * @description :
 * @since: 2020/9/8 15:54
 * @company : 固远（深圳）信息技术有限公司
 **/
public class RxJavaHelper<T, VM extends BaseViewModel> {

    private VM viewModel;
    private Observable<ResultBean<T>> observable;
    private String preTip;
    private boolean loading = true;
    private Consumer<Object> successConsumer;
    private ErrorResultBean failConsumer;

    public RxJavaHelper(VM viewModel, Observable<ResultBean<T>> observable) {
        this.viewModel = viewModel;
        this.observable = observable;
    }

    public static <T, VM extends BaseViewModel> Builder<T, VM> build(VM viewModel, Observable<ResultBean<T>> observable) {
        return new Builder<T, VM>(viewModel, observable);
    }

    public static class Builder<T, VM extends BaseViewModel> {
        private RxJavaHelper<T, VM> helper;

        public Builder(VM viewModel, Observable<ResultBean<T>> observable) {
            helper = new RxJavaHelper<T, VM>(viewModel, observable);
        }

        public RxJavaHelper<T, VM> getHelper() {
            return helper;
        }

        //设置加载提示
        public Builder<T, VM> setPreTip(String preTip) {
            helper.preTip = preTip;
            return this;
        }

        //设置成功回调
        public Builder<T, VM> success(Consumer<Object> success) {
            helper.successConsumer = success;
            return this;
        }

        //设置失败回调
        public Builder<T, VM> fail(ErrorResultBean fail) {
            helper.failConsumer = fail;
            return this;
        }

        //设置是否显示加载界面
        public Builder<T, VM> showLoading(boolean loading) {
            helper.loading = loading;
            return this;
        }
    }


    //默认的接口回调处理
    public Disposable flow(MutableLiveData<T> data) {
        return observable.compose(SchedulersCompat.getInstance().applyIoSchedulers())  //以第一个订阅的线程为准
                .doOnSubscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (loading) {
                            if (TextUtils.isEmpty(preTip)) {
                                viewModel.getShowLoading().postValue("数据加载中...");
                            } else {
                                viewModel.getShowLoading().postValue(preTip);
                            }
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (loading) {
                            viewModel.getHideLoading().call();
                            viewModel.getListComplete().call();
                        }
                    }
                })
                .subscribe(getSuccessConsumer(data), getFailConsumer());
    }

    //不需要监听接口回调数据或者使用自定义的回调监听
    public Disposable flow() {
        return observable.compose(SchedulersCompat.getInstance().applyIoSchedulers())  //以第一个订阅的线程为准
                .doOnSubscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (loading) {
                            if (TextUtils.isEmpty(preTip)) {
                                viewModel.getShowLoading().postValue("数据加载中...");
                            } else {
                                viewModel.getShowLoading().postValue(preTip);
                            }
                        }

                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (loading) {
                            viewModel.getHideLoading().call();
                            viewModel.getListComplete().call();
                        }
                    }
                })
                .subscribe(getSuccessConsumer(), getFailConsumer());
    }


    private Consumer<Object> getSuccessConsumer(MutableLiveData<T> data) {
        if (successConsumer == null) {
            return new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    data.postValue((T) o);
                }
            };
        } else {
            return successConsumer;
        }
    }

    private Consumer<Object> getSuccessConsumer() {
        if (successConsumer == null) {
            return new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {

                }
            };
        } else {
            return successConsumer;
        }
    }


    private ErrorResultBean getFailConsumer() {
        if (failConsumer == null) {
            return new ErrorResultBean() {
                @Override
                protected void onError(ErrorBean errorBean) {
                    if (loading) {
                        viewModel.getTip().postValue(errorBean.getErrorResult());
                    }
                }
            };
        } else {
            return failConsumer;
        }
    }
}
