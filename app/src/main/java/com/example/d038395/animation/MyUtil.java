package com.example.d038395.animation;

import android.app.Activity;
import android.app.AlertDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.VideoView;


/**
 * Created by d038395 on 2015-07-13.
 */
public class MyUtil {

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
}
