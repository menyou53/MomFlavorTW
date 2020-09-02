package com.example.youmen.sql2;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db=null;
    private final static
        String CREATE_TABLE="CREATE TABLE table01(_id INTEGER PRIMARY KEY,name TEXT,price INTEGER)";

    ListView listView01;
    Button btnSearch,btnSearchALL;
    EditText edtID;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtID=(EditText)findViewById(R.id.edtID);
        btnSearch=(Button)findViewById(R.id.btnSerch);
        btnSearchALL=(Button)findViewById(R.id.btnSerchAll);
        listView01=(ListView)findViewById(R.id.ListView01);

        //監聽
        btnSearch.setOnClickListener(myListener);
        btnSearchALL.setOnClickListener(myListener);
        listView01.setOnClickListener(myListener);
        //資料庫建立
        db=openOrCreateDatabase("db1.db",MODE_PRIVATE,null);
        try{
            db.execSQL(CREATE_TABLE);
            db.execSQL("INSERT INTO table01(name,time)values('song1',240)");
            db.execSQL("INSERT INTO table01(name,time)values('song2',320)");
            db.execSQL("INSERT INTO table01(name,time)values('song3',170)");
            db.execSQL("INSERT INTO table01(name,time)values('song4',190)");
        }catch(Exception e){
        }
        cursor=getAll();
        UpdateAdapter(cursor);
    }


    private ListView.OnItemClickListener listview01Listener= new ListView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            cursor.moveToPosition(position);
            Cursor c=get(id);
            String s="id="+id+"\r\n"+"name"+c.getString(1)+"\r\n"+"price="+c.getInt(2);
            Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.close();
    }
    private Button.OnClickListener myListener=new Button.OnClickListener(){
        public void onClick(View v){
            try{
                switch(v.getId()){
                    case R.id.btnSerch:{
                        long id=Integer.parseInt(edtID.getText().toString());
                        cursor=get(id);
                        UpdateAdapter(cursor);
                        break;
                    }
                    case R.id.btnSerchAll:{
                        cursor=getAll();
                        UpdateAdapter(cursor);
                        break;
                    }
                }
            }catch(Exception err){
                Toast.makeText(getApplicationContext(),"查無此資料",Toast.LENGTH_SHORT).show();
            }
        }
    };

    public void UpdateAdapter(Cursor cursor){
        if (cursor!=null&&cursor.getCount()>=0){
            SimpleCursorAdapter adapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,cursor,
                    new String[]{"sname","time"},
                    new int[]{android.R.id.text1,android.R.id.text2},0);
            listView01.setAdapter(adapter);
        }
    }

    public Cursor getAll(){
        Cursor cursor=db.rawQuery("SELECT _id,_id||'.'||name sname,time FROM table01",null);
        return cursor;
    }

    public Cursor get(long rowId) throws SQLException{
        Cursor cursor=db.rawQuery("SELECT _id,_id||'.'||name sname,time FROM table01 WHERE _id="+rowId,null);
        if(cursor.getCount()>0)
            cursor.moveToFirst();
        else
            Toast.makeText(getApplicationContext(),"查無此筆資料",Toast.LENGTH_SHORT).show();
        return cursor;
    }
}
