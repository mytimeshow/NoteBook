package com.example.administrator.notebook;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class AddActivity extends AppCompatActivity {
    private EditText title;
    private Button button;
    private EditText type;
    private ImageView bitmap;
    private EditText time;
    private EditText content;
    private Button save;
    public static final int CHOOSE_PHOTO=2;
    private Bitmap img;
    private String imgpath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        button= (Button) findViewById(R.id.addactivity_button);
        bitmap= (ImageView) findViewById(R.id.addactivity_bitmap);
        title= (EditText) findViewById(R.id.addactivity_title);
        type= (EditText) findViewById(R.id.addactivity_type);
        time= (EditText) findViewById(R.id.addactivity_time);
        content= (EditText) findViewById(R.id.addactivity_content);
        save= (Button) findViewById(R.id.addactivity_save);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                )!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                }else{
                    openAlbum();
                }
            }






        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String img=imgpath;
                String ti=title.getText().toString();
                String ty=type.getText().toString();
                String tm=time.getText().toString();
                String cn=content.getText().toString();
//                List<Note> list=new ArrayList<Note>();
//                Note note=new Note();
//                note.setContent(cn);
//                note.setDate(tm);
//                note.setType(ty);
//                note.setTitle(ti);
//                list.add(note);
                Intent intent=new Intent();

                intent.putExtra("bitmap",img);
               intent.putExtra("title",ti);
                intent.putExtra("type",ty);
                intent.putExtra("time",tm);
                intent.putExtra("content",cn);
               setResult(1,intent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openAlbum();
            }else {
                Toast.makeText(AddActivity.this,"you denied the permission",Toast.LENGTH_LONG).show();
            }

                break;

            default:
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHOOSE_PHOTO:if(resultCode==RESULT_OK){
                if(Build.VERSION.SDK_INT>=19){
                    handleImageOnKitkat(data);
                }else{
                    handleImageBrforeKitkat(data);
                }
            }break;
            default:
        }
    }

    private void handleImageBrforeKitkat(Intent data) {
        Uri uri=data.getData();
        String imagePath=getImagePath(uri,null);
        displayImage(imagePath);
    }



    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitkat(Intent data) {
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];//解释出数字id
                String selection= MediaStore.Images.Media._ID +"=" + id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);

            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
        }else  if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的uri ，使用普通处理
            imagePath=uri.getPath();
        }
        displayImage(imagePath);

    }

    private void displayImage(String imagePath) {
        if(imagePath != null){
            Bitmap bitmap1= BitmapFactory.decodeFile(imagePath);
            getimgpath(imagePath);
            getBitmap(bitmap1);
            bitmap.setImageBitmap(bitmap1);

        }else{
            Toast.makeText(AddActivity.this,"failed to get image",Toast.LENGTH_LONG).show();
        }
    }

    private void getimgpath(String a) {
        imgpath=a;

    }

    private String getImagePath(Uri uri, String selection) {
        String path=null;
        //通过Uri和selection来获取真实的图片路径
        Cursor curson=getContentResolver().query(uri,null,selection,null,null);
        if(curson != null) {
            if (curson.moveToFirst()) {
                path = curson.getString(curson.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            curson.close();
        }
        return path;
    }

    private void openAlbum() {
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
    }
    public byte[] Bitmapbyte(Bitmap c){
        ByteArrayOutputStream b=new ByteArrayOutputStream();
        c.compress(Bitmap.CompressFormat.PNG,100,b);

        return  b.toByteArray();
    }
    public Bitmap getBitmap(Bitmap a){
        img=a;
        return img;
    }
}
