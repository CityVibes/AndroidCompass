package com.juliusscript.compass.di.binders;

import com.juliusscript.compass.di.SubcomponentBuilder;
import com.juliusscript.compass.di.SubcomponentKey;
import com.juliusscript.compass.di.components.ViewModelComponent;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * Created by Julius.
 */
@Module(subcomponents = {ViewModelComponent.class})
public abstract class ApplicationBinders {

    @Binds
    @IntoMap
    @SubcomponentKey(ViewModelComponent.Builder.class)
    public abstract SubcomponentBuilder myViewModel(ViewModelComponent.Builder impl);
}
