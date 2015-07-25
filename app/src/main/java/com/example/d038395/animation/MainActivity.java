package com.example.d038395.animation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    static File filepath;
    VideoView videoView;
    MyUtils.TTs tts;
    ArrayList<String> snowWhiteList = new ArrayList<>();
    String thanks="Excellent, thank you.";

    final int mMain=0;
    final int mTTs=1;
    final int mRecord=2;
    final boolean[] boolTask = new boolean[]{true,true,true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        snowWhiteList.add("snow white");
        snowWhiteList.add("What's your name?");
        snowWhiteList.add("Would you like tell me the story about Snow White?");

        filepath=new File(Environment.getExternalStorageDirectory().getPath()+File.separator+"Animation");
        if (!filepath.isDirectory())
            filepath.mkdirs();

        MyUtils.playVideo(this);

        Uri uri =Uri.parse("android.resource://" + getPackageName() +'/'+ R.raw.animation_speaking);
        videoView = (VideoView) findViewById(R.id.video_hint);
        videoView.setVideoURI(uri);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });

        tts = new MyUtils.TTs(this);

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
            startAct(snowWhiteList);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MyParas{
        String topic;
        String question;
        Iterator iterator;

        public MyParas(String topic, String question, Iterator iterator) {
            this.topic = topic;
            this.question = question;
            this.iterator = iterator;
        }
    }

    private class taskTTs extends AsyncTask<MyParas,Void,MyParas> {

        String topic;
        String question;

        @Override
        protected MyParas doInBackground(MyParas...params) {
            topic=params[0].topic;
            question=params[0].question;
            tts.SpeakText(question,question);
            boolTask[mTTs]=true;
            tts.getTTs().setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {

                }

                @Override
                public void onDone(String utteranceId) {
                    boolTask[mTTs] = false;
                    videoView.pause();
                }

                @Override
                public void onError(String utteranceId) {
                    boolTask[mTTs] = false;
                    videoView.pause();
                }
            });
            while(boolTask[mTTs]){
                try{
                    Thread.sleep(100);
                } catch (InterruptedException e ){
                    e.printStackTrace();
                }
            }
            return params[0];
        }


        @Override
        protected void onPostExecute(MyParas params) {
            new asyncRecording().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,params);
        }
    }
    public void startAct(ArrayList<?> arrayList){

        Iterator<?> iterator = arrayList.iterator();


        final String topic=iterator.next().toString();

        if (iterator.hasNext())
            actNext(topic,iterator);
    }

    public void actNext(String topic, Iterator<?> iterator){
        String question=iterator.next().toString();
        new taskTTs().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,
                    new MyParas(topic,question,iterator));
        videoView.start();
    }

    private class asyncRecording extends AsyncTask<MyParas, Void ,Void> {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        AlertDialog dialog;
        MediaRecorder mRecorder = new MediaRecorder();
        @Override
        protected Void doInBackground(MyParas... params) {
            final UUID uuid = UUID.randomUUID();
            final String topic = params[0].topic;
            final String question = params[0].question;
            final Iterator iterator=params[0].iterator;

            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            String audioPath = filepath + File.separator + uuid;
            mRecorder.setOutputFile(audioPath);

            mRecorder.setAudioChannels(1);
            mRecorder.setAudioSamplingRate(44100);
            mRecorder.setAudioEncodingBitRate(44100);

            builder.setMessage("Recording")
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mRecorder.stop();
                            mRecorder.release();
                            new DataStorage(uuid.toString(), question, topic).put();
                            boolTask[mRecord] = false;
                            boolTask[mMain]=false;
                            if (iterator.hasNext())
                                actNext(topic,iterator);
                            else
                                tts.SpeakText(thanks,null);
                        }
                    });
            try {
                mRecorder.prepare();
                mRecorder.start();
                boolTask[mRecord] = true;
            } catch ( IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            dialog=builder.create();
            dialog.show();
        }

    }


}
