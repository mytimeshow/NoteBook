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
import android.widget.ImageButton;
import android.widget.Toast;

import static com.example.administrator.notebook.AddActivity.CHOOSE_PHOTO;

public class NotePage extends AppCompatActivity {
    private ImageButton img;
    private EditText write;
    private EditText title;
    private EditText type;
    private EditText time;
    private Button save;
    private String realimgPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_page);
        img= (ImageButton) findViewById(R.id.addImg);
        write= (EditText) findViewById(R.id.edt_write);
        title= (EditText) findViewById(R.id.note_page_Title);
        type= (EditText) findViewById(R.id.note_page_Type);
        time= (EditText) findViewById(R.id.note_page_time);
        save= (Button) findViewById(R.id.btn_save);

      final   Intent intent=getIntent();
        final String BITMAP=intent.getStringExtra("bitmap");
        final  String TITLE=intent.getStringExtra("title");
        final  String TYPE=intent.getStringExtra("type");
        final  String TIME=intent.getStringExtra("time");
        final   String CONTENT=intent.getStringExtra("content");
        img.setImageBitmap(getBitmap(BITMAP));
        write.setText(CONTENT);
        time.setText(TIME);
        type.setText(TYPE);
        title.setText(TITLE);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(NotePage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE
                )!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(NotePage.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                }else{
                    openAlbum();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String  tle=title.getText().toString();
                String te=type.getText().toString();
                String tm=time.getText().toString();
                String cn=write.getText().toString();
                String getpath=realimgPath;

                Intent intent1=new Intent();
                intent1.putExtra("bitmap",getpath);
                intent1.putExtra("title1",TITLE);
                intent1.putExtra("type1",TYPE);
                intent1.putExtra("time1",TIME);
                intent1.putExtra("content1",CONTENT);


                intent1.putExtra("bitmap1",BITMAP);
                intent1.putExtra("title",tle);
                intent1.putExtra("type",te);
                intent1.putExtra("time",tm);
                intent1.putExtra("content",cn);
                setResult(2,intent1);

//                LitePal.getDatabase();
//                Note note=new Note();
//                note.setContent(cn);
//                note.setTitle(tle);
//                note.setDate(tm);
//                note.setType(te);
//                note.updateAll("content = ? and title = ?",CONTENT,TI);
                finish();
            }
        });
    }
    public Bitmap getBitmap(String a){
        Bitmap bitmap= BitmapFactory.decodeFile(a);
        return  bitmap;
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                openAlbum();
            }else {
                Toast.makeText(NotePage.this,"you denied the permission",Toast.LENGTH_LONG).show();
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
          voidimg(imagePath);

            img.setImageBitmap(bitmap1);

        }else{
            Toast.makeText(NotePage.this,"failed to get image",Toast.LENGTH_LONG).show();
        }
    }

    private void voidimg(String imagePath) {
        realimgPath=imagePath;
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
}
