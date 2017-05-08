package com.example.administrator.notebook;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnclickLisenner{
    private ImageButton search;
    private ImageButton addNote;
    private RecyclerView recyclerView;
    private List<Note> noteList;
    private MyAdapter adapter;
    private Button change;
    private String img;
    private String img2;
    private Context context;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.getDatabase();

        noteList= DataSupport.findAll(Note.class);



        search= (ImageButton) findViewById(R.id.searchView);
        addNote= (ImageButton) findViewById(R.id.AddNote);
        change= (Button) findViewById(R.id.tv_change);





        initnoteList();
        initadapter();
        initrecyclerView();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent();
                intent1.setClass(MainActivity.this,AddActivity.class);
                startActivityForResult(intent1,1);
            }
        });





change.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Intent intent=new Intent();
        intent.setClass(MainActivity.this,ChangeActivity.class);
        startActivity(intent);

    }

});

    }



    private void initrecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case 1:
                if(resultCode==1){
                String content=data.getStringExtra("content");
                String ti=data.getStringExtra("title");
                    String imgpath=data.getStringExtra("bitmap");
                    setImg(imgpath);
//                byte[] bitmap=data.getByteArrayExtra("bitmap");

                    if(imgpath==null){
        Toast.makeText(MainActivity.this,"imggepath is null",Toast.LENGTH_LONG).show();
                    }
//                    Bitmap img=decodeByte(bitmap);
                String type=data.getStringExtra("type");
                String Date=data.getStringExtra("time");

//                    Toast.makeText(MainActivity.this,content,Toast.LENGTH_LONG).show();


                    Note note5=new Note();
                    note5.setBitmap(imgpath);
                            note5.setContent(content);
                            note5.setTitle(ti);
                            note5.setType(type);
                            note5.setDate(Date);
                    note5.save();

                   noteList.add(note5);
//                    initnoteList();

                            adapter.notifyDataSetChanged();

            }
                break;
            case 2:if(resultCode==2){
                String BITMAP=data.getStringExtra("bitmap1");
                String imgpath=data.getStringExtra("bitmap");
                String CONTENT=data.getStringExtra("content1");
                String TI=data.getStringExtra("title1");
                String TYPE=data.getStringExtra("type1");
                String DATE=data.getStringExtra("time1");



                String content=data.getStringExtra("content");
                String ti=data.getStringExtra("title");
                String type=data.getStringExtra("type");
                String Date=data.getStringExtra("time");
                Note note=new Note();
                note.setBitmap(imgpath);
                note.setContent(content);
                note.setTitle(ti);
                note.setDate(Date);
                note.setType(type);
                note.updateAll("content = ? and title = ? and date = ? and type = ? and bitmap = ?",CONTENT,TI,DATE,TYPE,BITMAP);

                noteList.clear();
                noteList= DataSupport.findAll(Note.class);

               adapter.changeDate(noteList);

                adapter.notifyDataSetChanged();


            }break;
            default:
        }
    }

    private void initnoteList() {



    }

    private void initadapter() {
        adapter = new MyAdapter(noteList,MainActivity.this);
    }

    @Override
    public void tiaozhuang(Intent aa) {
        Intent intent=aa;


        intent.setClass(MainActivity.this,NotePage.class);
        startActivityForResult(intent,2);
    }

    @Override
    public int dialog(final int[] a,final int[] b) {
final int[] c=b;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("确认删除吗？");
            builder.setTitle("提示");
           builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                 a[0] =1;

                   adapter.deleteitem(c[0]);
                   end( dialog);



               }

               private void end(DialogInterface dialog) {
                   dialog.dismiss();
               }
           });
            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    a[0]=-1;
                    dialog.dismiss();
                }
            });
                    builder.create().show();
return a[0];
    }

    public void setImg(String imgpath){
    img=imgpath;
}
    public void setImg2(String imgpath){
        img2=imgpath;
    }



}
