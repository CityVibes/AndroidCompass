package com.juliusscript.compass.di.components;

import com.juliusscript.compass.di.SubcomponentBuilder;
import com.juliusscript.compass.di.binders.ApplicationBinders;
import com.juliusscript.compass.di.modules.AppModule;

import java.util.Map;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Julius.
 */
@Singleton
@Component(modules = {AppModule.class, ApplicationBinders.class})
public interface AppComponent {
    Map<Class<?>, Provider<SubcomponentBuilder>> subcomponentBuidlers();
}
