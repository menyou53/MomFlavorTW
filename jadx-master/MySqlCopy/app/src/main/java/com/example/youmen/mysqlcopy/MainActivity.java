package com.example.youmen.mysqlcopy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


        private String[] rank = new String[10];
        private String[] name = new String[10];

        private String sSrc;
        private int xml_index;

        TextView textView1;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            textView1 = (TextView) this.findViewById(R.id.textView1);
            getXml();
        }


        /** 非同期での通信処理 **/
        private void getXml() {
            AsyncTask<String, Integer, Short> task = new AsyncTask<String, Integer, Short>() {
                @Override
                protected Short doInBackground(String...value) {
                    // データを取得する
                    HttpURLConnection http = null;
                    InputStream in = null;

                    try {
                        // HTTP接続
                        URL url = new URL("http:///menyou.servehttp.com/extras/MydbTest.xml");
                        http = (HttpURLConnection) url.openConnection();
                        http.setRequestMethod("GET");
                        http.connect();

                        // データを取得
                        in = http.getInputStream();

                        // ソースを読み出す
                        sSrc = new String();
                        byte[] line = new byte[1024];
                        int size;
                        while (true) {
                            size = in.read(line);
                            if (size <= 0) {
                                break;
                            }
                            sSrc += new String(line);
                        }
                    } catch (Exception e) {
                        return 1;
                    } finally {
                        try {
                            if (http != null){
                                http.disconnect();
                            }
                            if (in != null){
                                in.close();
                            }
                        } catch (Exception e) {
                            return 2;
                        }
                    }

                    // パースデータ格納用配列初期化
                    rank = new String[5];
                    name = new String[5];
                    xml_index = 0;

                    // XML解析
                    try{
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        factory.setNamespaceAware(true);
                        XmlPullParser xpp = factory.newPullParser();

                        xpp.setInput(new StringReader(sSrc));
                        int eventType = xpp.getEventType();
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("no")) {
                                // ランキング（順位）
                                eventType = xpp.next();
                                rank[xml_index] = xpp.getText();
                            }else if(eventType == XmlPullParser.START_TAG && xpp.getName().equals("name")) {
                                // 名前
                                eventType = xpp.next();
                                name[xml_index] = xpp.getText();
                                xml_index++;
                            }
                            // 次へ
                            eventType = xpp.next();
                        }
                    }catch (Exception e){
                        return 3;
                    }
                    return 0;
                }

                @Override
                protected void onPostExecute(Short iRet) {
                    if (iRet == 0) {
                        // 成功
                        textView1.setText("名字ランキング\n");
                        for(int i = 0; i < rank.length; i++){
                            textView1.setText(textView1.getText() + "No." + rank[i] + " >> 名前:" + name[i] + "\n");
                        }
                        showToast("成功");
                    } else {
                        // エラー
                        showToast("エラーコード：" + iRet);
                    }
                }
            };
            task.execute();
        }

        // トースト表示処理
        public void showToast(String text) {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }


}
