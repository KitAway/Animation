package com.example.d038395.animation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Locale;


/**
 * Created by d038395 on 2015-07-13.
 */
public class MyUtils {

    static void  playVideo(Activity activity){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View videoDialogView = activity.getLayoutInflater().inflate(R.layout.video_dialog,null);
        final VideoView videoView = (VideoView)videoDialogView.findViewById(R.id.video_view);
        String videoPath= "android.resource://"+activity.getPackageName()+"/"+R.raw.welcome_to_storyteller_project;
        videoView.setVideoURI(Uri.parse(videoPath));
        final AlertDialog dialog =builder.setView(videoDialogView).create();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                dialog.dismiss();
            }
        });
        videoView.start();
        dialog.show();
    }

    static class TTs{
        Context context;
        TextToSpeech textToSpeech;
        public TTs(final Context context) {
            this.context = context;
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        Toast.makeText(context, "TTs initialized successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        public void SpeakText(String text, String utteranceId ) {
            textToSpeech.setLanguage(Locale.US);
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null,utteranceId);
        }

        public TextToSpeech getTTs(){
            return textToSpeech;
        }
    }
}
