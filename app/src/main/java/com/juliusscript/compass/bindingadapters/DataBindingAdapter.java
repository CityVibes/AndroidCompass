package com.juliusscript.compass.bindingadapters;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.animation.Animation;

/**
 * Created by Julius.
 */
public class DataBindingAdapter {

    @BindingAdapter("animation")
    public static void addAnimation(View view, Animation animation) {
        view.startAnimation(animation);
    }
}
