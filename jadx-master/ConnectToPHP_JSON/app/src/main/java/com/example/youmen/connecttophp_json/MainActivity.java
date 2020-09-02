package com.example.youmen.connecttophp_json;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    JSONArray jArray;
    String result = null;
    InputStream is = null;
    StringBuilder sb=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                EditText tv = (EditText) findViewById(R.id.editView);
                ArrayList nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("name","asaya"));
                //nameValuePairs.add(new BasicNameValuePair("name","Guo"));
                //http post
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://menyou.servehttp.com/extras/TestAndroid.php");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                }catch(Exception e){
                    Log.e("log_tag", "Error in http connection"+e.toString());
                }
                //convert response to string
                try{
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                    sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");

                    String line=null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result=sb.toString();
                }catch(Exception e){
                    Log.e("log_tag", "Error converting result "+e.toString());
                }
                //paring data
                int ct_id;
                String ct_name;
                try{
                    jArray = new JSONArray(result);
                    JSONObject json_data=null;

                    for(int i=0;i<jArray.length();i++){
                        json_data = jArray.getJSONObject(i);
                        ct_name=json_data.getString("name");
                        tv.append(ct_name+" \n");
                    }
                }catch(JSONException e1){
                    //   Toast.makeText(getBaseContext(), "No City Found" ,Toast.LENGTH_LONG).show();
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
