package com.circles.circlesapp.helpers;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.circles.circlesapp.BuildConfig;
import com.circles.circlesapp.helpers.retrofit.MyServiceInterceptor;
import com.facebook.stetho.Stetho;


public class App extends Application implements AppLifeCycleHandler.AppLifeCycleCallback {
    private static App instance;
    public static volatile Handler applicationHandler = null;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // FirebaseApp.initializeApp(this);
        instance = this;
        // FirebaseMessaging.getInstance().subscribeToTopic("test");
        //  Log.d("token", FirebaseMessaging.getInstance().subscribeToTopic("test")+"");
        context = this;
        applicationHandler = new Handler(get().getMainLooper());
        if (BuildConfig.DEBUG) {
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                    .build());
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // MultiDex.install(this);
    }

    @Override
    public void onAppBackground() {
//        Log.d("LifecycleEvent", "onAppBackground");
        SharedPrefHelper helper = new SharedPrefHelper(context);
        if (MyServiceInterceptor.authentication.equals("") && helper.getUserToken() != null && !helper.getUserToken().equals("")) {
            MyServiceInterceptor.authentication = helper.getUserToken();
        }

    }

    @Override
    public void onAppForeground() {
        SharedPrefHelper helper = new SharedPrefHelper(context);
        if (MyServiceInterceptor.authentication.equals("") && helper.getUserToken() != null && !helper.getUserToken().equals("")) {
            MyServiceInterceptor.authentication = helper.getUserToken();
        }
    }


    public static Context get() {
        return instance;
    }
}
