package com.example.android.restful;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.restful.model.DataItem;
import com.example.android.restful.services.MyService;
import com.example.android.restful.utils.NetworkHelper;
import com.example.android.restful.utils.RequestPackage;

public class LearningActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
private TextView textview;
private Button btn_clear, btn_run;
private boolean networkok;
private  static  final String JSON_URL = "http://560057.youcanlearnit.net/services/json/itemsfeed.php";
private BroadcastReceiver mBroadCastReceiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
        //String message = intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
        DataItem[] dataItems = (DataItem[]) intent.getParcelableArrayExtra(MyService.MY_SERVICE_PAYLOAD);
        for (DataItem dataItem:dataItems) {
            textview.append(dataItem.getItemName() +"\n");
        }

    }
};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);
        textview = findViewById(R.id.textView_learner);
        btn_clear = findViewById(R.id.button_clear);
        btn_run = findViewById(R.id.button_show);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mBroadCastReceiver,
                new IntentFilter(MyService.MY_SERVICE_MESSAGE));
        networkok = NetworkHelper.hasNetworkAccess(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadCastReceiver);
    }

    void runClickHandler(View view){
        //the asynctask implementation
       // textview.append("Button Clicked \n");
      /*   myAsyncTask task = new myAsyncTask();
         task.execute("String 1", "String 2", "String 3");
         //the asyncloader implementation
         getSupportLoaderManager().initLoader(0, null, this).forceLoad(); */
         //myservice implementation
        if (networkok){
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setEndPoint(JSON_URL);
            requestPackage.setParam("category", "Desserts");
            requestPackage.setMethod("POST");
            Intent intent = new Intent();
           // intent.setData(Uri.parse(JSON_URL));
            intent.putExtra(MyService.REQUEST_PACKAGE, requestPackage);
            startService(intent);
        }else{
            Toast.makeText(this, "Network not available", Toast.LENGTH_LONG).show();
        }

    }
    void clearClickHandler(View view){
        textview.setText("");
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        textview.append("Creating loader \n");
        return new MyTaskLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        textview.append("Load finished, returned: " + data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    private class myAsyncTask extends AsyncTask<String, String, Void>{

        @Override
        protected Void doInBackground(String... strings) {

            for (String string:strings) {
              publishProgress(string);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            textview.append(values[0] + "\n");
        }
    }
    private static class MyTaskLoader extends AsyncTaskLoader<String>{

        public MyTaskLoader(@NonNull Context context) {
            super(context);
        }

        @Nullable
        @Override
        public String loadInBackground() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "from the loader";
        }

        @Override
        public void deliverResult(@Nullable String data) {
            data += ", delivered";
            super.deliverResult(data);
        }
    }
}
