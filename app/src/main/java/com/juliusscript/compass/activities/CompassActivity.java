package com.juliusscript.compass.activities;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.hardware.Sensor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.pwittchen.reactivesensors.library.ReactiveSensors;
import com.juliusscript.compass.CompassApplication;
import com.juliusscript.compass.R;
import com.juliusscript.compass.databinding.ActivityCompassBinding;
import com.juliusscript.compass.di.components.ViewModelComponent;
import com.juliusscript.compass.di.modules.ViewModelModule;
import com.juliusscript.compass.viewmodels.CompassViewModel;

import javax.inject.Inject;

/**
 * Activity responsible for displaying compass.
 */
public class CompassActivity extends AppCompatActivity {
    @Inject protected CompassViewModel compassViewModel;
    @Inject protected ReactiveSensors reactiveSensors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set content view and retrieve data binding object
        ActivityCompassBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_compass);
        //setup dagger dependecy injection
        ViewModelComponent.Builder builder = (ViewModelComponent.Builder)
                ((CompassApplication) getApplication()).getAppComponent()
                        .subcomponentBuidlers().get(ViewModelComponent.Builder.class).get();
        builder.viewModelModule(new ViewModelModule()).build().inject(this);
        //bind view model
        binding.setCompassModel(compassViewModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (reactiveSensors.hasSensor(Sensor.TYPE_ROTATION_VECTOR)) {
            compassViewModel.enableReadOrientation();
        }
        PackageManager pm = getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_LOCATION)) {
            compassViewModel.enableReadLocation(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop sensory data reading while in background
        if (reactiveSensors.hasSensor(Sensor.TYPE_ROTATION_VECTOR)) {
            compassViewModel.disableReadOrientation();
        }
        PackageManager pm = getPackageManager();
        if (pm.hasSystemFeature(PackageManager.FEATURE_LOCATION)) {
            compassViewModel.disableReadLocation();
        }
    }

}