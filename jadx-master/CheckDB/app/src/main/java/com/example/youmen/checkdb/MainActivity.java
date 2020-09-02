package com.example.youmen.checkdb;
        import java.io.BufferedReader;
        import java.io.InputStream;
        import java.io.InputStreamReader;
        import java.util.ArrayList;
        import java.util.HashMap;

        import org.apache.http.HttpEntity;
        import org.apache.http.HttpResponse;
        import org.apache.http.NameValuePair;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.entity.UrlEncodedFormEntity;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.message.BasicNameValuePair;
        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import android.content.Intent;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.app.Activity;
        import android.os.Parcelable;
        import android.os.StrictMode;
        import android.util.Log;
        import android.view.Menu;
        import android.view.View;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.ListView;
        import android.widget.TextView;
        import android.widget.Toast;


public class MainActivity extends Activity {

    String id;
    String Mname;
    private static final String name ="name";
    String UsID;
    private static final String UserID="UserID";
    InputStream is=null;
    String result=null;
    String line=null;
    String data1;
    ArrayList music=new ArrayList();
    ArrayList DLhttp=new ArrayList();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tv = new TextView(this);

        final EditText e_id=(EditText) findViewById(R.id.editText1);
        final LinearLayout ll=(LinearLayout)findViewById(R.id.viewObj);
        Button select=(Button) findViewById(R.id.button1);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                id=e_id.getText().toString();
                //select();
                TestTask task = new TestTask();
                task.execute();

            }
        });

        Button searchresult=(Button)findViewById(R.id.searhresult);
        searchresult.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i=new Intent();
                i.setClass(MainActivity.this,DLresult.class);
                i.putStringArrayListExtra("list",music);
                i.putStringArrayListExtra("listdl",DLhttp);
                startActivity(i);
            }
        });
    }

    class TestTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://124.6.27.87/extras/ConnectMcdata.php");
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("id", id));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }

            try {
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                is.close();
                result = sb.toString();

                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject json_data = jArray.getJSONObject(i);
                    Mname=(json_data.getString(name));
                    UsID=(json_data.getString(UserID));
                    String string=new String();
                    string=Mname+"        by:"+UsID;
                    music.add(string);
                    String dlurl=new String();
                    dlurl="http://menyou.servehttp.com/extras/uploads/"+Mname+"-by-"+UsID+".mp3";
                    DLhttp.add(dlurl);
                }

                Log.e("pass 2", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
            }


            return null;
        }

    }

}
