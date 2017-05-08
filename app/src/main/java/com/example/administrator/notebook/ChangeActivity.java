package com.example.administrator.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class ChangeActivity extends AppCompatActivity {
    private Button save;
    private EditText change_title;
    private EditText change_type;
    private EditText change_content;
    private EditText change_time;
    private List<Note> noteList;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        save= (Button) findViewById(R.id.btn_save);
        change_content= (EditText) findViewById(R.id.change_content);
        change_title= (EditText) findViewById(R.id.change_Title);
        change_type= (EditText) findViewById(R.id.change_Type);
        change_time= (EditText) findViewById(R.id.change_time);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content=String.valueOf(change_content.getText());
                String title=String.valueOf(change_title.getText());
                String type=String.valueOf(change_type.getText());
                String time=String.valueOf(change_time.getText());
                Intent intent=new Intent();
                intent.setClass(v.getContext(),MainActivity.class);
                intent.putExtra("content",content);
                intent.putExtra("title",title);
                intent.putExtra("type",type);
                intent.putExtra("time",time);
                startActivity(intent);


            }
        });
    }
}
