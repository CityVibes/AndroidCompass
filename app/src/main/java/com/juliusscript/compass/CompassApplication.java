package com.juliusscript.compass;

import android.app.Application;

import com.juliusscript.compass.di.components.AppComponent;
import com.juliusscript.compass.di.components.DaggerAppComponent;
import com.juliusscript.compass.di.modules.AppModule;


/**
 * Created by Julius.
 */
public class CompassApplication extends Application {
    protected AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        //creates app component for dagger dependency injection
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
