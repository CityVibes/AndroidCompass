package com.juliusscript.compass.di.scopes;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Julius.
 */
@Scope
@Documented
@Retention(value = RetentionPolicy.RUNTIME)
public @interface PerViewModel {
}
