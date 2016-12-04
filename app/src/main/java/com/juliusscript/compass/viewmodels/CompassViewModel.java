package com.juliusscript.compass.viewmodels;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;

import com.github.pwittchen.reactivesensors.library.ReactiveSensorEvent;
import com.google.android.gms.location.LocationRequest;
import com.juliusscript.compass.BR;
import com.juliusscript.compass.R;
import com.juliusscript.compass.di.components.SensorComponent;
import com.juliusscript.compass.di.components.ViewModelComponent;
import com.juliusscript.compass.di.modules.SensorModule;
import com.patloew.rxlocation.FusedLocation;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.disposables.Disposable;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Julius.
 * Defines compass behavior by reading and exposing sensory data.
 */
public class CompassViewModel extends BaseObservable {
    private static final float ALPHA = 0.8f;
    protected float latitude, longitude;
    protected float orientationOld, orientationNew, bearingOld, bearingNew;
    protected Subscription orientationSubscription;
    protected Disposable locationDisposable;
    protected float[] rotationValues;
    @Nullable @Inject @Named("location") protected FusedLocation locationSensor;
    @Nullable @Inject @Named("rotation") protected Observable<ReactiveSensorEvent> rotationSensor;

    public CompassViewModel(ViewModelComponent component) {
        //setup dependency injection
        SensorComponent.Builder builder = (SensorComponent.Builder) component.subcomponentBuidlers()
                .get(SensorComponent.Builder.class).get();
        builder.sensorModule(new SensorModule()).build().inject(this);
    }

    /**
     * Enables/starts reading orientation sensory data.
     *
     * @return Whether enabling reading of sensory data was successful, true - success, false - fail.
     */
    public boolean enableReadOrientation() {
        if (rotationSensor == null) {
            return false;
        }
        orientationSubscription = rotationSensor
                .subscribe(reactiveSensorEvent -> {
                    //get data and convert to rotation matrix
                    SensorEvent sensorEvent = reactiveSensorEvent.getSensorEvent();
                    rotationValues = lowPass(sensorEvent.values, rotationValues);
                    float R[] = new float[9];
                    SensorManager.getRotationMatrixFromVector(R, rotationValues);
                    //convert rotation matrix to device orientation a.k.a getting x,y,z values
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    //get Z axis rotation in degrees
                    float azimuth = (float) Math.toDegrees(orientation[0]);
                    //correct rotation to positive values
                    azimuth = (azimuth + 360) % 360;
                    orientationNew = azimuth;
                    //notify attached view to update cursor and marker values
                    notifyPropertyChanged(BR.cursorAnimation);
                    notifyPropertyChanged(BR.markerAnimation);
                });
        return true;
    }

    /**
     * Disables reading of orientation sensory data.
     */
    public void disableReadOrientation() {
        if (orientationSubscription != null) {
            orientationSubscription.unsubscribe();
        }
    }

    /**
     * Enables/starts reading location data.
     *
     * @param context Context within this method is called (used for requesting location permissions).
     * @return Whether enabling reading of sensory data was successful, true - success, false - fail.
     */
    public boolean enableReadLocation(Context context) {
        if (locationSensor == null) {
            return false;
        }
        //request permission
        RxPermissions.getInstance(context).request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        //noinspection MissingPermission
                        locationDisposable = locationSensor
                                .updates(LocationRequest.create()
                                        .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                                        .setInterval(5000))
                                .subscribe(this::recalculateLocation);
                    }
                });
        return true;
    }

    /**
     * Recalculates current bearing by using currently setup and given user locations,
     * notifies attached view about changes.
     *
     * @param location New user location.
     */
    protected void recalculateLocation(Location location) {
        Location targetLocation = new Location("");
        targetLocation.setLatitude(latitude);
        targetLocation.setLongitude(longitude);
        bearingNew = location.bearingTo(targetLocation);
        //notify attached view to update marker values
        notifyPropertyChanged(BR.markerAnimation);
    }

    /**
     * Manually renew location & bearing data by reading last known user location.
     *
     * @param context Context within this method is called (used for requesting location permissions).
     */
    protected void renewLocationData(Context context) {
        if (locationSensor == null) {
            return;
        }
        RxPermissions.getInstance(context).request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        //noinspection MissingPermission
                        locationSensor
                                .lastLocation()
                                .subscribe(this::recalculateLocation);
                    }
                });
    }

    /**
     * Disables reading of location data.
     */
    public void disableReadLocation() {
        if (locationDisposable != null) {
            locationDisposable.dispose();
        }
    }

    /**
     * Presents latitude dialog for manual target latitude setup.
     *
     * @param view View containing context within this is called.
     */
    public void onLatitudeClick(View view) {
        Context context = view.getContext();
        //latitude input
        final EditText input = new EditText(context);
        input.setText(String.valueOf(latitude));
        //show dialog
        showGeoDialog(context, context.getString(R.string.latitude), input,
                (dialog, which) -> {
                    renewLocationData(context);
                    latitude = Float.valueOf(input.getText().toString());
                });
    }

    /**
     * Presents longitude dialog for manual target longitude setup.
     *
     * @param view View containing context within this is called.
     */
    public void onLongitudeClick(View view) {
        Context context = view.getContext();
        //longitude input
        final EditText input = new EditText(context);
        input.setText(String.valueOf(longitude));
        //show dialog
        showGeoDialog(context, context.getString(R.string.longitude), input,
                (dialog, which) -> {
                    renewLocationData(context);
                    longitude = Float.valueOf(input.getText().toString());
                });
    }

    /**
     * Creates compass marker rotation animation.
     *
     * @return Marker rotation animation.
     */
    @Bindable
    public Animation getMarkerAnimation() {
        //calculates marker rotation to accommodate compass rotation
        Animation animation = new RotateAnimation(bearingOld - orientationOld, bearingNew - orientationNew,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 6f);
        bearingOld = bearingNew;
        animation.setFillAfter(true);
        return animation;
    }

    /**
     * Creates compass cursor rotation animation.
     *
     * @return Cursor rotation animation.
     */
    @Bindable
    public Animation getCursorAnimation() {
        Animation animation = new RotateAnimation(-orientationOld, -orientationNew,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        orientationOld = orientationNew;
        animation.setFillAfter(true);
        return animation;
    }

    /**
     * Shows dialog for geolocation data entry.
     *
     * @param context Context within this is called.
     * @param title   Title shown in a dialog box.
     * @param input   Input field for reading data.
     * @param accept  OnClick defining what happens on positive button click
     */
    protected void showGeoDialog(Context context, String title,
                                 EditText input, DialogInterface.OnClickListener accept) {
        //create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        //input field allows decimal numbers only
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setSelectAllOnFocus(true);
        builder.setView(input);
        builder.setPositiveButton(R.string.ok, accept);
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

    /**
     * Low pass frequency filtering for sensors.
     */
    protected float[] lowPass(float[] input, float[] output) {
        if (output == null) return input;

        for (int i = 0; i < input.length; i++) {
            output[i] = output[i] + ALPHA * (input[i] - output[i]);
        }
        return output;
    }

}
