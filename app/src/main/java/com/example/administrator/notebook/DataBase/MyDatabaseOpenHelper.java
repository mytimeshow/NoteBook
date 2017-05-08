package com.example.administrator.notebook.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/5/6 0006.
 */

public class MyDatabaseOpenHelper extends SQLiteOpenHelper {
    private Context mcontext;
    public static final String CREATE_TABLE="create table Note(_id integer PRIMARY KEY AUTOINCREMENT NOT NULL,name varchar,age int)";

    public MyDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mcontext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        Toast.makeText(mcontext,"create success",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
