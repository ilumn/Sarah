//written by ilumn @ https://github.com/ilumn
//visit the repo for latest version

package com.example.sarah;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;




public class MainActivity extends AppCompatActivity {

    private SpeechRecognizer speechRecognizer;
    private TextToSpeech textToSpeech;
    private TextView textView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button2);
         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                    TextView pyld1=findViewById(R.id.input);
                    String pyld=pyld1.getText().toString();


                     OkHttpClient okHttpClient = new OkHttpClient();

                     RequestBody formbody = new FormBody.Builder().add("payload", pyld).build();

                     Request request = new Request.Builder().url("http://0.0.0.0:5000/post").post(formbody).build();
                     okHttpClient.newCall(request).enqueue(new Callback() {
                         @Override
                         public void onFailure(@NotNull Call call, @NotNull IOException e) {
                             runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     Toast.makeText(MainActivity.this, "network unavailable", Toast.LENGTH_LONG).show();
                                 }
                             });
                         }

                         @Override
                         public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                             final TextView textView = findViewById(R.id.output);

                             runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     try {
                                         textView.setText(response.body().string());
                                     } catch (IOException e) {
                                         e.printStackTrace();
                                     }
                                 }
                             });
                         }
                     });
                 }

         });



        ActivityCompat.requestPermissions(this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        textView = findViewById(R.id.input);
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
            }
        });

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
            }
            @Override
            public void onBeginningOfSpeech() {
            }
            @Override
            public void onRmsChanged(float rmsdB) {
            }
            @Override
            public void onBufferReceived(byte[] buffer) {
            }
            @Override
            public void onEndOfSpeech() {
            }
            @Override
            public void onError(int error) {
            }
            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String string = "";
                textView.setText("");
                if (matches != null){
                    string = matches.get(0);
                    textView.setText(string);
                    OkHttpClient okHttpClient = new OkHttpClient();

                    RequestBody formbody = new FormBody.Builder().add("payload", string).build();

                    Request request = new Request.Builder().url("http://0.0.0.0:5000/post").post(formbody).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, "network unavailable", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                            final TextView textView = findViewById(R.id.output);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        textView.setText(response.body().string());
                                        TextView pyld3=findViewById(R.id.output);
                                        String pyld4=pyld3.getText().toString();
                                        textToSpeech.speak(pyld4, TextToSpeech.QUEUE_FLUSH, null, null);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        }
                    });

                }
            }
            @Override
            public void onPartialResults(Bundle partialResults) {
            }
            @Override
            public void onEvent(int eventType, Bundle params) {
            }
        });
    }

    public void startButton(View view){
        textToSpeech.speak("How can I help?", TextToSpeech.QUEUE_FLUSH, null, null);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        speechRecognizer.startListening(intent);

    }

}