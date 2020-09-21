package com.androidsample;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

class ActivityStarterModule extends ReactContextBaseJavaModule {

    ActivityStarterModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ActivityStarter";
    }

    @ReactMethod
    void navigateToExample() {
//        Toast.makeText(getReactApplicationContext(), "Some custom", Toast.LENGTH_SHORT).show();

//        ReactApplicationContext context = getReactApplicationContext();
//        Intent intent = new Intent(context, ExampleActivity.class);
//        context.startActivity(intent);

        Activity activity = getCurrentActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, ExampleActivity.class);
            activity.startActivity(intent);
        }
    }
}

