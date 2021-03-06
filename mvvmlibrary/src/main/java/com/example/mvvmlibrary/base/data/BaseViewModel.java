package com.example.mvvmlibrary.base.data;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.HashMap;
import java.util.Map;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import com.example.mvvmlibrary.data.SingleLiveEvent;

/**
 * created by com.tl
 * created at 2020/8/18
 */
public class BaseViewModel extends ViewModel {

    private CompositeDisposable mCompositeSubscription;

    private SingleLiveEvent<String> showLoading;//显示加载框
    private SingleLiveEvent<Void> hideLoading;//隐藏加载框
    private SingleLiveEvent<String> tip; //提示语
    private SingleLiveEvent<Map<String, Object>> startActivityEvent;//activity跳转
    private SingleLiveEvent<Void> finishEvent;  //关闭当前activity
    private SingleLiveEvent<Void> onBackPressedEvent; //点击返回键
    private SingleLiveEvent<Void> listComplete;   //列表获取数据结束
    /**
     * 是否显示loading by Leo
     */
    public MutableLiveData<Boolean> isShowLoading = new MutableLiveData<>();

    public SingleLiveEvent<String> getShowLoading() {
        showLoading = createLiveData(showLoading);
        return showLoading;
    }

    public SingleLiveEvent<Void> getHideLoading() {
        hideLoading = createLiveData(hideLoading);
        return hideLoading;
    }

    public SingleLiveEvent<String> getTip() {
        tip = createLiveData(tip);
        return tip;
    }

    public SingleLiveEvent<Map<String, Object>> getStartActivityEvent() {
        startActivityEvent = createLiveData(startActivityEvent);
        return startActivityEvent;
    }

    public SingleLiveEvent<Void> getFinishEvent() {
        finishEvent = createLiveData(finishEvent);
        return finishEvent;
    }

    public SingleLiveEvent<Void> getOnBackPressedEvent() {
        onBackPressedEvent = createLiveData(onBackPressedEvent);
        return onBackPressedEvent;
    }

    public SingleLiveEvent<Void> getListComplete() {
        listComplete = createLiveData(listComplete);
        return listComplete;
    }

    protected <T> SingleLiveEvent<T> createLiveData(SingleLiveEvent<T> liveData) {
        if (liveData == null) {
            liveData = new SingleLiveEvent<T>();
        }
        return liveData;
    }


    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Map<String, Object> params = new HashMap<>();
        params.put(ParameterField.CLASS, clz);
        if (bundle != null) {
            params.put(ParameterField.BUNDLE, bundle);
        }
        startActivityEvent.postValue(params);
    }

    /**
     * 关闭界面
     */
    public void finish() {
        finishEvent.call();
    }

    /**
     * 点击返回键
     */
    public void onBackPressed() {
        onBackPressedEvent.call();
    }


    public static final class ParameterField {
        public static String CLASS = "CLASS";
        public static String BUNDLE = "BUNDLE";
    }

    protected void addSubscription(@Nullable Disposable disposable) {
        if (disposable == null) {
            return;
        }
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeDisposable();
        }
        mCompositeSubscription.add(disposable);
    }

    /**
     * RxJava取消注册，以避免内存泄露
     */
    public void onUnSubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.size() > 0) {
            mCompositeSubscription.dispose();
        }
    }

}
