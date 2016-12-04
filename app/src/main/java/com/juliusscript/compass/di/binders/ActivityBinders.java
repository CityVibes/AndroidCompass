package com.juliusscript.compass.di.binders;

import com.juliusscript.compass.di.SubcomponentBuilder;
import com.juliusscript.compass.di.SubcomponentKey;
import com.juliusscript.compass.di.components.SensorComponent;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by Julius.
 */
@Module(subcomponents = {SensorComponent.class})
public abstract class ActivityBinders {

    @Binds
    @IntoMap
    @SubcomponentKey(SensorComponent.Builder.class)
    public abstract SubcomponentBuilder mySensor(SensorComponent.Builder impl);
}
