package com.example.randhawa.snake_game;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class
MainActivity extends AppCompatActivity
{

    Button my_start_button;
    Button my_new_button;
    Button my_load_button;
    Button my_score_button;

    boolean Is_Saved;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        my_start_button=findViewById (R.id.id_btn_Start);
        my_new_button=findViewById (R.id.id_btn_New);
        my_load_button=findViewById (R.id.id_btn_Load);
        my_score_button = findViewById (R.id.id_btn_Scorecard);

        MediaPlayer mp=MediaPlayer.create(this,R.raw.crowd);
        mp.start ();


        check_if_Saved_found();
    }

    private void check_if_Saved_found()
    {
        SharedPreferences sp = getSharedPreferences("Saved_Game",MODE_PRIVATE);
        int s_size=sp.getInt ("Snake_Size",-1);



        if(s_size == -1)
            Is_Saved = false;
        else
            Is_Saved = true;
    }


    @SuppressLint("Range")
    public void On_Start(View view)
    {
        my_start_button.setVisibility (View.INVISIBLE);
        my_new_button.setVisibility (View.VISIBLE);
        my_load_button.setVisibility (View.VISIBLE);
        my_score_button.setVisibility (View.VISIBLE);

        if(!Is_Saved)
        {
            my_load_button.setAlpha((float) 0.4);
        }
    }

    public void On_New_Button_Click(View view)
    {
        SharedPreferences sp = getSharedPreferences("Game_Decision",MODE_PRIVATE);
        SharedPreferences.Editor my_editor = sp.edit ();

        my_editor.putString ("which","N");
        my_editor.apply();

        Intent i=new Intent(this, GamePlay_Activity.class);

        startActivity(i);
    }



    public void On_Load_Button_Click(View view)
    {
        if(Is_Saved)
        {
            SharedPreferences sp = getSharedPreferences("Game_Decision",MODE_PRIVATE);
            SharedPreferences.Editor my_editor = sp.edit ();

            my_editor.putString ("which","L");
            my_editor.apply();


            Intent i=new Intent(this, GamePlay_Activity.class);
            startActivity(i);
        }

    }

    public void On_Scorecard_Click(View view)
    {
        SQLite_Helper my_Database = new SQLite_Helper(this);

        Cursor res = my_Database.getAllData();
        if(res.getCount()==0)
        {
            show_Message("Error","nothing found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        int i=1;
        while (res.moveToNext())
        {
            String num = String.valueOf (i);
            buffer.append(num + ". "+res.getString(0)+"\n");
            buffer.append("Score : "+res.getString(1)+"\n\n");

            i++;
        }
        show_Message("Scorecard",buffer.toString());

    }

    public void show_Message(String title,String Message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);
        builder.setIcon (R.drawable.trophy);
        builder.setTitle(title);

        setTitleColor(Color.BLUE);

        builder.setMessage(Message);

        Dialog d = builder.show();

//        int dividerId = d.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
//        View divider = d.findViewById(dividerId);
////        divider.setBackgroundColor(getResources().getColor(R.color.my_color));
//
//                divider.setBackgroundColor(Color.BLACK);

    }

}
