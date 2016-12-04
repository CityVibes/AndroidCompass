package com.juliusscript.compass.di.components;

import com.juliusscript.compass.activities.CompassActivity;
import com.juliusscript.compass.di.SubcomponentBuilder;
import com.juliusscript.compass.di.binders.ActivityBinders;
import com.juliusscript.compass.di.modules.ViewModelModule;
import com.juliusscript.compass.di.scopes.PerActivity;

import java.util.Map;

import javax.inject.Provider;

import dagger.Subcomponent;

/**
 * Created by Julius.
 */
@PerActivity
@Subcomponent(modules = {ViewModelModule.class, ActivityBinders.class})
public interface ViewModelComponent {

    void inject(CompassActivity compassActivity);

    Map<Class<?>, Provider<SubcomponentBuilder>> subcomponentBuidlers();

    @Subcomponent.Builder
    interface Builder extends SubcomponentBuilder<ViewModelComponent> {
        Builder viewModelModule(ViewModelModule module);
    }
}