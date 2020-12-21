package com.example.randhawa.snake_game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Ending_Activity extends AppCompatActivity
{
    EditText my_Name_text;

    SQLite_Helper my_Database;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_ending_);

        my_Name_text = (EditText) findViewById (R.id.id_editText_Name);
        my_Database = new SQLite_Helper(this);
    }



    public void On_Save_Button_Click(View v)
    {
        String name = my_Name_text.getText ().toString ();

        SharedPreferences sp = getSharedPreferences("Score",MODE_PRIVATE);
        int score=sp.getInt ("score",-1);

        boolean is_inserted = my_Database.insert_Data (name, score );

        if(is_inserted==true)
        {
            Toast.makeText(Ending_Activity.this,"Game Saved!",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(Ending_Activity.this,"DATA NOT INSERTED",Toast.LENGTH_LONG).show();
        }

        Intent i=new Intent (this,MainActivity.class);
        startActivity (i);
    }
}
