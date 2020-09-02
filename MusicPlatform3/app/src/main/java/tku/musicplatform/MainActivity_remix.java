package tku.musicplatform;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MainActivity_remix extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView File1;
    private Button toRecord,selFile1,reMix,btnPlay,btnStop;
    private ListView lstMix;
    private MediaPlayer mediaplayer;
    private MediaRecorder mediarecorder;
    private String selectedFilePath,chooseFileName,selectedFileName,mixFileName;
    private List<String> lstArray=new ArrayList<String>();
    private File selFile, mixFile,chooseFile;
    private int cListItem=0;

    public static final int FREQUENCY = 44100;
    public static final int CHANNEL_CONFIGURATION = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    public static final int AUDIO_ENCODING =  AudioFormat.ENCODING_PCM_16BIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_remix);

        File1=(TextView)findViewById(R.id.File1);
        toRecord = (Button) findViewById(R.id.toRecord);
        selFile1 = (Button) findViewById(R.id.selFile1);
        reMix = (Button) findViewById(R.id.reMix);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnStop = (Button) findViewById(R.id.btnStop);
        lstMix=(ListView)findViewById(R.id.lstMix);
        mediaplayer=new MediaPlayer();
        btnDisable(btnStop);
        selFile= Environment.getExternalStorageDirectory();
        reMix.setOnClickListener(listener);
        btnPlay.setOnClickListener(listener);
        btnStop.setOnClickListener(listener);
        lstMix.setOnItemClickListener(lstListener);
        requestStoragePermission();


        Button nextPageBtn = (Button)findViewById(R.id.toRecord);
        Button selFile = (Button)this.findViewById(R.id.selFile1);

        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity_remix.this , MainActivity.class);
                startActivity(intent);
            }
        });

        selFile.setOnClickListener( new View.OnClickListener(){
            public void onClick(View arg0) {
                showFileChooser();
            }
        });


    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int hasPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        }
        recList();
    }

    private void showFileChooser() {
        // 建立 "選擇檔案 Action" 的 Intent
        Intent intent = new Intent( Intent.ACTION_PICK );
        // 過濾檔案格式
        intent.setType( "*/*" );
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // 建立 "檔案選擇器" 的 Intent  (第二個參數: 選擇器的標題)
        // 切換到檔案選擇器 (它的處理結果, 會觸發 onActivityResult 事件)
        startActivityForResult( Intent.createChooser( intent, "選擇音樂" ),PICK_FILE_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // 有選擇檔案
        if ( resultCode == Activity.RESULT_OK ) {
            if(requestCode == PICK_FILE_REQUEST) {
                // 取得檔案的 Uri
                Uri selectedFileUri = data.getData();
                if( selectedFileUri != null )
                {
                    selectedFilePath = FilePath.getPath(this,selectedFileUri);

                    if(selectedFilePath != null && !selectedFilePath.equals("")){
                        selectedFileName = selectedFilePath.substring(selectedFilePath.lastIndexOf("/")+1);
                        File1.setText("曲目:" + "\n" + selectedFileName);
                    }
                }
                else {
                    Toast.makeText(this,"無效的檔案路徑 !!",Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "取消選擇檔案 !!", Toast.LENGTH_SHORT).show();
        }
    }

    private Button.OnClickListener listener=new Button.OnClickListener() {

        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.reMix:
                    try {
                        Calendar calendar = new GregorianCalendar();
                        Date nowtime = new Date();
                        calendar.setTime(nowtime);
                        mixFileName = "Mix" + add0(calendar.get(Calendar.YEAR)) + add0(calendar.get(Calendar.MONTH) + 1) + add0(calendar.get(Calendar.DATE)) + add0(calendar.get(Calendar.HOUR)) + add0(calendar.get(Calendar.MINUTE)) + add0(calendar.get(Calendar.SECOND));
                        chooseFileName = "re" +mixFileName;
                        mixFile = new File(selFile + "/" + mixFileName + ".amr");
                        chooseFile = new File(selFile + "/" + chooseFileName + ".amr");
                        mediarecorder = new MediaRecorder();
                        playSong(selectedFilePath);
                        mediarecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mediarecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
                        mediarecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                        mediarecorder.setOutputFile(mixFile.getAbsolutePath());
                        mediarecorder.prepare();
                        mediarecorder.start();
                        File1.setText("正在合音.........");
                        btnDisable(reMix);
                        btnDisable(btnPlay);
                        btnDisable(toRecord);
                        btnEnable(btnStop);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btnPlay:
                    playSong(selectedFilePath);
                    break;
                case R.id.btnStop:
                    if (mixFile != null) {
                        mediaplayer.reset();
                        mediarecorder.stop();
                        mediarecorder.release();
                        mediarecorder = null;
                        File1.setText("停止" + mixFile.getName() + "合音!");
                        try {
                            mixSound();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        recList();
                    } else {
                        mediaplayer.reset();
                    }
                    btnEnable(reMix);
                    btnEnable(btnPlay);
                    btnEnable(toRecord);
                    btnDisable(btnStop);
                    break;
            }
        }
    };

    private ListView.OnItemClickListener lstListener=new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            cListItem = position; //取得點選位置
            playSong(selFile + "/" + lstArray.get(cListItem).toString()); //播放錄音
        }
    };

    private void playSong(String path) {
        try
        {
            mediaplayer.reset();
            mediaplayer.setDataSource(path); //播放錄音路徑
            mediaplayer.prepare();
            mediaplayer.start(); //開始播放
            selectedFileName = selectedFilePath.substring(selectedFilePath.lastIndexOf("/")+1);

            File1.setText("播放：" + "\n" + selectedFileName);
            btnDisable(reMix);
            btnDisable(btnPlay);
            btnDisable(toRecord);
            btnEnable(btnStop);
            mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer arg0) {
                    File1.setText(selectedFileName + "\n" + "播完！");
                    btnEnable(reMix);
                    btnEnable(btnPlay);
                    btnEnable(toRecord);
                    btnDisable(btnStop);
                }
            });
        } catch (IOException e) {}
    }

    private void mixSound() throws IOException {
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT, 44100, AudioTrack.MODE_STREAM);

        InputStream in1 = new FileInputStream(new File(Environment.getExternalStorageDirectory(), selectedFilePath));
        InputStream in2 = new FileInputStream(mixFile);

        byte[] arrayMusic1 = null;
        arrayMusic1 = new byte[in1.available()];
        arrayMusic1 = createMusicArray(in1);
        in1.close();

        byte[] arrayMusic2 = null;
        arrayMusic2 = new byte[in2.available()];
        arrayMusic2 = createMusicArray(in2);
        in2.close();


        byte[] output = new byte[arrayMusic1.length];

        audioTrack.play();

        for (int i = 0; i < output.length; i++) {
            float samplef1 = arrayMusic1[i] / 128.0f;
            float samplef2 = arrayMusic2[i] / 128.0f;
            float mixed    = samplef1 + samplef2;

            // reduce the volume a bit:
            mixed *= 0.8;
            // hard clipping
            if (mixed > 1.0f)  mixed = 1.0f;
            if (mixed < -1.0f) mixed = -1.0f;

            byte outputSample = (byte) (mixed * 128.0f);
            output[i]         = outputSample;
        }

        audioTrack.write(output, 0, output.length);
        convertByteToFile(output);
    }

    public static byte[] createMusicArray(InputStream is) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[10240];
        int i = Integer.MAX_VALUE;
        while ((i = is.read(buff, 0, buff.length)) > 0) {
            baos.write(buff, 0, i);
        }

        return baos.toByteArray(); // be sure to close InputStream in calling function

    }

    public static void convertByteToFile(byte[] fileBytes) throws FileNotFoundException {

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory().getPath()  + "/.amr" ));
        try {
            bos.write(fileBytes);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void recList() {
        lstArray.clear();
        for(File file:selFile.listFiles()) {
            if(file.getName().toLowerCase().endsWith(".amr")) {
                lstArray.add(file.getName());
            }
        }
        if(lstArray.size()>0) {
            ArrayAdapter<String> adaRec=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, lstArray);
            lstMix.setAdapter(adaRec);
        }
    }

    private void btnDisable(Button btn) {
        btn.setEnabled(false);
        btn.setTextColor(0xd0d0d0);
    }

    private void btnEnable(Button btn) {
        btn.setEnabled(true);
        btn.setTextColor(0xff000000);
    }

    protected String add0(int n) {
        if(n<10) return("0" + n);
        else return ("" + n);
    }
}
