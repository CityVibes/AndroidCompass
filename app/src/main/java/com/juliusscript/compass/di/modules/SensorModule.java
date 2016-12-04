package com.juliusscript.compass.di.modules;

import android.app.Application;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.support.annotation.Nullable;

import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;
import com.github.pwittchen.reactivesensors.library.ReactiveSensorFilter;
import com.github.pwittchen.reactivesensors.library.ReactiveSensors;
import com.juliusscript.compass.di.scopes.PerViewModel;
import com.patloew.rxlocation.FusedLocation;
import com.patloew.rxlocation.RxLocation;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Julius.
 */
@Module
public class SensorModule {

    @PerViewModel
    @Nullable
    @Provides
    @Named("rotation")
    rx.Observable<ReactiveSensorEvent> providesRotationSensor(Application application, ReactiveSensors reactiveSensors) {
        if (reactiveSensors.hasSensor(Sensor.TYPE_ROTATION_VECTOR)) {
            return new ReactiveSensors(application).observeSensor(Sensor.TYPE_ROTATION_VECTOR)
                    .subscribeOn(Schedulers.computation())
                    .filter(ReactiveSensorFilter.filterSensorChanged())
                    .observeOn(AndroidSchedulers.mainThread());
        }
        return null;
    }

    @PerViewModel
    @Nullable
    @Provides
    @Named("location")
    FusedLocation providesLocationSensor(Application application, RxLocation rxLocation) {
        PackageManager pm = application.getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_LOCATION)) {
            //noinspection MissingPermission
            return rxLocation.location();
        }
        return null;
    }
}