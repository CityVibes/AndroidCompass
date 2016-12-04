package com.juliusscript.compass.di.modules;

import android.app.Application;

import com.github.pwittchen.reactivesensors.library.ReactiveSensors;
import com.patloew.rxlocation.RxLocation;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Julius.
 */
@Module
public class AppModule {
    Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application providesApp() {
        return application;
    }

    @Provides
    @Singleton
    RxLocation providesRxLocation() {
        return new RxLocation(application);
    }

    @Provides
    @Singleton
    ReactiveSensors providesReactiveSensors() {
        return new ReactiveSensors(application);
    }
}
