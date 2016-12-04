package com.juliusscript.compass.di.modules;

import com.juliusscript.compass.di.components.ViewModelComponent;
import com.juliusscript.compass.di.scopes.PerActivity;
import com.juliusscript.compass.viewmodels.CompassViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Julius.
 */
@Module
public class ViewModelModule {

    @PerActivity
    @Provides
    CompassViewModel providesCompassViewModel(ViewModelComponent viewModelComponent) {
        return new CompassViewModel(viewModelComponent);
    }
}
