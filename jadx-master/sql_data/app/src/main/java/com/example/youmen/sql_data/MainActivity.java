package com.example.youmen.sql_data;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //設定HTTP Get & Post要連線的Url
    private String postUrl = "http://menyou.servehttp.com/etras/TestAndroid.php";
    private String getUrl = "http://opendata.epa.gov.tw/ws/Data/AQX/?$format=xml";

    private EditText txtMessage;
    private Button postBtn;
    private Button getBtn;

    private String msg = null;  //存放要Post的訊息
    private String result;  //存放Post回傳值

    Http_Post HP;
    Http_Get HG;

    static Handler handler; //宣告成static讓service可以直接使用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HG = new Http_Get();
        HP = new Http_Post();
        txtMessage = (EditText) findViewById(R.id.txt_message);
        postBtn = (Button) findViewById(R.id.http_post_btn);
        getBtn = (Button) findViewById(R.id.http_get_btn);

        //讓多個Button共用一個Listener，在Listener中再去設定各按鈕要做的事
        postBtn.setOnClickListener(this);
        getBtn.setOnClickListener(this);

        //接收service傳出Post的到的回傳訊息，並透過Toast顯示出來
        handler = new Handler(){
            public void handleMessage(Message msg){
                switch (msg.what){
                    case 123:
                        String ss = (String)msg.obj;
                        Toast.makeText(MainActivity.this, ss,Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }
    //依照按下的按鈕去做相對應的任務
    public void onClick(View v){
        switch (v.getId()){
            case R.id.http_get_btn:
                HG.Get(getUrl);
                break;
            case R.id.http_post_btn:
                if (txtMessage != null) {
                    //取得EditText的內容
                    msg = txtMessage.getEditableText().toString();
                    HP.Post(msg,postUrl);
                }
                break;
        }
    }
}
