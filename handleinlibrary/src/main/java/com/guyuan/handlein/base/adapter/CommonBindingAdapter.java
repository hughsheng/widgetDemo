package com.guyuan.handlein.base.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.databinding.BindingAdapter;

import com.guyuan.handlein.base.util.glide.GlideUtils;


/**
 * @author : tl
 * @description :一些通用的XML数据设置方法
 * @since: 2020/11/2 19:28
 * @company : 固远（深圳）信息技术有限公司
 **/

public class CommonBindingAdapter {

    //glide设置图片
    @BindingAdapter(value = {"imgUrl", "imgType", "imgRadius"}, requireAll = false)
    public static void setPic(ImageView imageView, String url, int type, int radius) {
        switch (type) {
            case GlideUtils.NORMAL:
                GlideUtils.getInstance().loadUrlImage(imageView, url);
                break;

            case GlideUtils.ROUND_RADIUS:
                GlideUtils.getInstance().loadRoundUriImage(imageView, url, radius);
                break;

            case GlideUtils.CIRCLE:
                GlideUtils.getInstance().loadUserCircleImageFromGuYuanServer(imageView, url);
                break;
        }
    }


    //text设置字符串有数据显示，无数据隐藏
    @BindingAdapter("visibilityByContent")
    public static void setVisibilityByContent(TextView tv, String content) {
        if (TextUtils.isEmpty(content) ||
                content.endsWith("：null") || content.endsWith("：") || content.endsWith(":null")
                || content.endsWith(":")) {
            tv.setVisibility(View.GONE);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(content);
        }
    }

    //当前view跟目标view的显示状态一致
    @BindingAdapter("visibilityByView")
    public static void setVisibilityByView(View view, int viewID) {
        View targetView = view.getRootView().findViewById(viewID);
        if (targetView != null) {
            view.setVisibility(targetView.getVisibility());
        }
    }

    //null默认显示空字符串
    @BindingAdapter("rightContent")
    public static void setRightContent(TextView tv, String content) {
        if (TextUtils.isEmpty(content)) {
            tv.setText("");
        } else {
            tv.setText(content);
        }
    }

    @BindingAdapter("rightContentWithTitle")
    public static void setRightContentWithTitle(TextView tv, String content) {
        if (TextUtils.isEmpty(content) ||
                content.endsWith("：null") || content.endsWith("：") || content.endsWith(":null")
                || content.endsWith(":")) {
            if (content.contains(":")) {
                tv.setText(content.split(":")[0]);
            } else if (content.contains("：")) {
                tv.setText(content.split("：")[0]);
            }
        } else {
            tv.setText(content);
        }
    }
}