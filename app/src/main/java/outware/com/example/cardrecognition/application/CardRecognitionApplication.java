package outware.com.example.cardrecognition.application;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class CardRecognitionApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
