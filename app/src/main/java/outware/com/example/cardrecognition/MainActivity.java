package outware.com.example.cardrecognition;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.text.FirebaseVisionCloudText;
import com.google.firebase.ml.vision.cloud.text.FirebaseVisionCloudTextDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionText.Block;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "CardRecognition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Button btnCloudApi = findViewById(R.id.btnCloud);
        Button btnOnDeviceApi = findViewById(R.id.btnOnDevice);
        final TextView txtTimeTaken = findViewById(R.id.txtTimeTaken);
        final TextView txtCardText = findViewById(R.id.textCardText);

        btnCloudApi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "Reading Started");
                try {
                    final long start = System.currentTimeMillis();

                    FirebaseVisionCloudDetectorOptions options = new FirebaseVisionCloudDetectorOptions.Builder()
                            .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                            .setMaxResults(20)
                            .build();

                    FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(BitmapFactory.decodeStream(getAssets().open("IMAG3428.png")));
                    FirebaseVisionCloudTextDetector detector = FirebaseVision.getInstance()
                                                                             .getVisionCloudTextDetector();

                    Task<FirebaseVisionCloudText> result = detector.detectInImage(image)
                                                                   .addOnSuccessListener(new OnSuccessListener<FirebaseVisionCloudText>() {
                                                                       @Override
                                                                       public void onSuccess(FirebaseVisionCloudText firebaseVisionCloudText) {
                                                                           txtTimeTaken.setText("" + (System.currentTimeMillis() - start));
                                                                           Log.d(TAG, "Reading Completed");
                                                                           String recognizedText = firebaseVisionCloudText.getText();
                                                                           txtCardText.setText(recognizedText);
                                                                       }
                                                                   })
                                                                   .addOnFailureListener(new OnFailureListener() {
                                                                       @Override
                                                                       public void onFailure(Exception e) {
                                                                           Log.d(TAG, "Reading Failed");
                                                                       }
                                                                   });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        btnOnDeviceApi.setOnClickListener(new OnClickListener() {
                                              @Override
                                              public void onClick(View v) {

                                                  Log.d(TAG, "Reading Started");
                                                  final long start = System.currentTimeMillis();
                                                  try {

                                                      FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(BitmapFactory.decodeStream(getAssets().open("IMAG3428.png")));
                                                      FirebaseVisionTextDetector detector = FirebaseVision.getInstance()
                                                                                                          .getVisionTextDetector();
                                                      Task<FirebaseVisionText> result =
                                                              detector.detectInImage(image)
                                                                      .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                                                          @Override
                                                                          public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                                                              txtTimeTaken.setText("" + (System.currentTimeMillis() - start));
                                                                              Log.d(TAG, "Reading Completed");
                                                                              List<Block> blocks = firebaseVisionText.getBlocks();
                                                                              StringBuilder recognizedText = new StringBuilder("");
                                                                              for (int i = 0; i < blocks.size(); i++) {
                                                                                  recognizedText.append(blocks.get(i).getText() +"\n");
                                                                              }
                                                                              txtCardText.setText(recognizedText);
                                                                          }
                                                                      })
                                                                      .addOnFailureListener(
                                                                              new OnFailureListener() {
                                                                                  @Override
                                                                                  public void onFailure(@NonNull Exception e) {
                                                                                      Log.d(TAG, "Reading Failed");
                                                                                  }
                                                                              });


                                                  } catch (IOException e) {
                                                      e.printStackTrace();
                                                  }

                                              }
                                          }
        );
    }
}
