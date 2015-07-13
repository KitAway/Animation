package com.example.d038395.animation;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.VideoView;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    static File filepath;
    VideoView videoView;
    String[] snowWiteList = new String[]{
            "snow white",
            "What's your name?",
            "Would you like tell me the story about Snow White?",
            "Excellent, thank you."
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        filepath=new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"Animation");
        MyUtil.playVideo(this);

        Uri uri =Uri.parse("android.resource://" + getPackageName() +'/'+ R.raw.animation_speaking);
        videoView = (VideoView) findViewById(R.id.video_hint);
        videoView.setVideoURI(uri);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id== R.id.action_start) {
            startAct();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startAct(){
        String topic=snowWiteList[0];
        int len=snowWiteList.length;
        for (int i=1;i<len-1;i++){
            recording(topic,snowWiteList[i]);
        }

        videoView.start();
    }

    private void recording(String topic, String question){

    }
}
