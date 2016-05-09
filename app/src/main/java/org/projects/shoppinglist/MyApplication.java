package org.projects.shoppinglist;

import android.app.Application;

import com.firebase.client.Firebase;
import com.flurry.android.FlurryAgent;

/**
 * Created by leon on 25-04-2016.
 */
public class MyApplication extends Application {


        @Override
        public void onCreate() {
            super.onCreate();
            Firebase.setAndroidContext(this);
            Firebase.getDefaultConfig().setPersistenceEnabled(true);

            FlurryAgent.setLogEnabled(false);
            FlurryAgent.init(this, "X45PD8FQ36NVHWBBY7ZS");

        }
}
