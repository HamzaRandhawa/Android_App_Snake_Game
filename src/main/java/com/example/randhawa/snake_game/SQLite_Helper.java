package com.example.randhawa.snake_game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite_Helper extends SQLiteOpenHelper
{
//    public SQLite_Helper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
//    {
//        super (context, name, factory, version);
//
//    }

    public static final String database_name = "Score_DB";
    public static final String table_name = "Score_Info";
    public static final String col1 = "Name";
    public static final String col2 = "Score";


    public SQLite_Helper(Context context)
    {
        super (context, database_name,null, 1);

        SQLiteDatabase db = this.getReadableDatabase ();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String SQL_Query = "create table "+table_name+
                "("
                +col1 +" Text, "
                +col2 +" integer"
                + " ) ";

        db.execSQL (SQL_Query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL ("drop table if exists "+ table_name);
        onCreate (db);
    }

    public boolean insert_Data(String name,int score)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues our_content = new ContentValues();

        our_content.put(col1, name);
        our_content.put(col2, score);

        long result = db.insert(table_name,null,our_content);
        if(result == -1)
            return false;
        else
            return true;

    }

    public void DB_Update(int update_id)
    {
        SQLiteDatabase db = this.getReadableDatabase ();
        db.execSQL("UPDATE " + table_name + " SET col2 = Ali WHERE "+ col1 +" = "+update_id);
    }

    void Update_Data()
    {

    }

    public Integer delete_Data(String name)   // Integer Class... public Class final Integer extends Number
    {
        SQLiteDatabase db = this.getWritableDatabase ();
        return db.delete (table_name,"Name = ?",new String[]{name});
    }


    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result = db.rawQuery("select * from "+table_name +" order by "+col2+" DESC limit 10 ;",null);
        return  result;
    }
}
