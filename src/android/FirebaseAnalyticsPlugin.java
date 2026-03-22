package com.firebase.analytics;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsPlugin extends CordovaPlugin {

    private FirebaseAnalytics firebaseAnalytics;

    @Override
    protected void pluginInitialize() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(cordova.getActivity());
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {

        if(action.equals("logEvent")) {
            try {
                String eventName = args.getString(0);
                JSONObject params = args.getJSONObject(1);

                Bundle bundle = new Bundle();

                java.util.Iterator<String> keys = params.keys();

                while(keys.hasNext()){
                    String key = keys.next();
                    bundle.putString(key, params.getString(key));
                }

                firebaseAnalytics.logEvent(eventName, bundle);

                callbackContext.success();
            } catch(Exception e){
                callbackContext.error(e.getMessage());
            }

            return true;
        }

        return false;
    }
}