package com.example.android.restful.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.android.restful.model.DataItem;
import com.example.android.restful.utils.HttpHelper;
import com.example.android.restful.utils.RequestPackage;
import com.google.gson.Gson;

import java.io.IOException;

public class MyService extends IntentService {
    private static final String TAG = "MyService";
    public static final String MY_SERVICE_MESSAGE = "MyServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "MyServicePayload";
    public static final String REQUEST_PACKAGE = "requestPackage";
    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        RequestPackage requestPackage = (RequestPackage) intent.getParcelableExtra(REQUEST_PACKAGE);
     Uri uri = intent.getData();
        Log.i(TAG, "onHandleIntent: "+ uri.toString());
        String response;
        try {
            response = HttpHelper.downloadUrl(requestPackage);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Gson gson = new Gson();
        DataItem[] dataItems = gson.fromJson(response, DataItem[].class);

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        messageIntent.putExtra(MY_SERVICE_PAYLOAD, dataItems);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}
