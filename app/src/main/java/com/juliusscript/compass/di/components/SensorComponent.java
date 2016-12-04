package com.juliusscript.compass.di.components;

import com.juliusscript.compass.di.SubcomponentBuilder;
import com.juliusscript.compass.di.modules.SensorModule;
import com.juliusscript.compass.di.scopes.PerViewModel;
import com.juliusscript.compass.viewmodels.CompassViewModel;

import dagger.Subcomponent;

/**
 * Created by Julius.
 */
@PerViewModel
@Subcomponent(modules = {SensorModule.class})
public interface SensorComponent {

    void inject(CompassViewModel compassViewModel);

    @Subcomponent.Builder
    interface Builder extends SubcomponentBuilder<SensorComponent> {
        SensorComponent.Builder sensorModule(SensorModule module);
    }
}
