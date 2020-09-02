
package com.example.httppost;

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

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends Activity
{
    //UI����ǥΪ� (�򤶭�������)
    private Handler mUI_Handler = new Handler();
    private ListView listview;
    private HandlerThread mThread;
    //�c������ǥΪ� (�ɶ��W�L3��)
    private Handler mThreadHandler;
    

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	listview = (ListView) findViewById(R.id.listView1);
	//�o�ذ���ǥi�H���W�r!
	mThread = new HandlerThread("net");
	mThread.start();
	mThreadHandler = new Handler(mThread.getLooper());
	mThreadHandler.post(new Runnable()
	{
	    @Override
	    public void run()
	    {
		// TODO �۰ʲ��ͪ���k Stub
		final String jsonString = executeQuery("society");
		mUI_Handler.post(new Runnable()
		{

		    @Override
		    public void run()
		    {
			// TODO �۰ʲ��ͪ���k Stub
			renewListView(jsonString);
		    }
		});
	    }
	});
    }

    private String executeQuery(String query)
    {
	String result = "";
	try
	{
	    HttpClient httpClient = new DefaultHttpClient();
	    HttpPost post = new HttpPost("http://www.defendmobi1e.com/query.php");
	    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    nameValuePairs.add(new BasicNameValuePair("category", query));
	    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    HttpResponse httpResponse = httpClient.execute(post);
	    HttpEntity httpEntity = httpResponse.getEntity();
	    InputStream inputStream = httpEntity.getContent();
	    BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
	    StringBuilder builder = new StringBuilder();
	    String line = null;
	    while ((line = bufReader.readLine()) != null)
	    {
		builder.append(line + "\n");
	    }
	    inputStream.close();
	    result = builder.toString();
	}
	catch (Exception e)
	{
	    Log.e("log_tag", e.toString());
	}
	return result;
    }

    public final void renewListView(String input)
    {
	/*
	 * SQL ���G���h����Ʈɨϥ�JSONArray
	 * �u���@����Ʈɪ����إ�JSONObject����
	 * JSONObject jsonData = new JSONObject(result);
	 */
	try
	{
	    JSONArray jsonArray = new JSONArray(input);
	    ArrayList<HashMap<String, Object>> users = new ArrayList<HashMap<String, Object>>();
	    setTitle(jsonArray.length() + "�����");
	    for (int i = 0; i < jsonArray.length(); i++)
	    {
		JSONObject jsonData = jsonArray.getJSONObject(i);
		HashMap<String, Object> h2 = new HashMap<String, Object>();
		h2.put("title", jsonData.getString("title"));
		h2.put("date", jsonData.getString("date"));
		users.add(h2);
		SimpleAdapter adapter = new SimpleAdapter(this, users, R.layout.list, new String[]
		{ "title", "date", }, new int[]
		{ R.id.textView1, R.id.textView2 });
		listview.setAdapter(adapter);
	    }
	}
	catch (JSONException e)
	{
	    // TODO �۰ʲ��ͪ� catch �϶�
	    e.printStackTrace();
	}
    }

}
