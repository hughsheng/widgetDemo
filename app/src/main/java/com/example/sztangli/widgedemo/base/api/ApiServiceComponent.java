package com.example.sztangli.widgedemo.base.api;

import com.example.sztangli.widgedemo.weather.dagger.WeatherComponent;
import com.example.sztangli.widgedemo.weather.dagger.WeatherModule;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

/**
 * Created by tl on 2018-8-9
 * dagger2管理依赖注入类(子模块的component必须放到这里管理)
 */
@Singleton
@Component(modules = {ApiServiceModule.class})
public interface ApiServiceComponent {
  Retrofit getRetrofit();

  WeatherComponent getWeatherComponent(WeatherModule weatherModule);

}
