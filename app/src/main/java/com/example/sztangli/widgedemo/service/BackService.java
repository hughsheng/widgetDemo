package com.example.sztangli.widgedemo.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import com.example.sztangli.widgedemo.base.app.AppApplication;

/**
 * .
 * 开启后台下载服务
 */
public class BackService extends IntentService {

  public static final String ACTION_CACHE = "service.action.cache";

  private AppApplication mApplication;

  public BackService() {
    super("BackService");
  }

  @Override
  public void onCreate() {
    super.onCreate();
    mApplication = AppApplication.getInstance();
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      final String action = intent.getAction();
      final Bundle bundle = intent.getExtras();
      if (ACTION_CACHE.equals(action)) {

      }

    }

  }



}
