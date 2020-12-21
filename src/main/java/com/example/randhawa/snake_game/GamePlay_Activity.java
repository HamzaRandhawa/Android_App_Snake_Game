package com.example.randhawa.snake_game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randhawa.snake_game.engine.Game_Engine;
import com.example.randhawa.snake_game.enums.Direction;
import com.example.randhawa.snake_game.enums.Game_State;
import com.example.randhawa.snake_game.views.Snake_View;

public class GamePlay_Activity extends AppCompatActivity implements View.OnTouchListener
{
    private Game_Engine my_game_engine;
    private Snake_View my_snake_view;

    private Handler handler = new Handler();
    private final long update_delay = 100;

    ImageView my_menu_img, my_wall_col_img;
    Button pause_Butn,play_Butn,continue_Btn,reset_Btn,save_btn,home_Btn,exit_Btn;
    TextView my_score_text;
    TextView my_HighestScore_text;

    float prevX, prevY;
    int h_s;

    boolean record=false;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play_);

       // mp=MediaPlayer.create(this,R.raw.mario_song);
        mp=MediaPlayer.create(this,R.raw.mario_song);
        mp.start ();


        my_game_engine=new Game_Engine();

        my_snake_view=(Snake_View) findViewById(R.id.snake_View);

        pause_Butn=(Button) findViewById (R.id.id_btn_Pause);
        play_Butn=(Button) findViewById (R.id.id_btn_Play);

        continue_Btn=(Button) findViewById (R.id.id_btn_Continue);
        reset_Btn=(Button) findViewById (R.id.id_btn_Reset);
        save_btn=(Button) findViewById (R.id.id_btn_Save);
        home_Btn=(Button) findViewById (R.id.id_btn_Home);
        exit_Btn=(Button) findViewById (R.id.id_btn_Exit);

        my_menu_img=(ImageView) findViewById (R.id.id_img_menu);
        my_wall_col_img = (ImageView) findViewById (R.id.id_img_snake_wall_collide);

        my_score_text = (TextView) findViewById (R.id.id_text_score);
        my_HighestScore_text = (TextView) findViewById (R.id.id_text_Highest);

        Read_High_Score();               //   Reading Highest Score from File

        my_score_text.setTextColor (Color.GREEN);
        my_score_text.setText (String.valueOf(my_game_engine.get_Score ()));

        my_snake_view.setOnTouchListener(this);

        my_game_engine.Set_Context (this);

        //start_Update_Handler();

        Start();

        // --------------------------------------------------------------------------- //
//
//        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
//            private GestureDetector gestureDetector = new GestureDetector(GamePlay_Activity.this, new GestureDetector.SimpleOnGestureListener() {
//                @Override
//                public boolean onDoubleTap(MotionEvent e) {
//                    final Context context = GamePlay_Activity.this;
//
//                    Log.d("TEST", "onDoubleTap");
//                    Toast.makeText(context, "onDoubleTap", Toast.LENGTH_SHORT).show();
//                    return super.onDoubleTap(e);
//                }
//                @Override
//                public boolean onSingleTapConfirmed(MotionEvent event) {
//                    Log.d("TEST", "onSingleTap");
//                    return false;
//                }
//            });
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                gestureDetector.onTouchEvent(event);
//                return true;
//            }
//
//
//        });

        // --------------------------------------------------------------------------- //

//        final Context context = this;
//        final GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
//            @Override
//            public boolean onDoubleTap(MotionEvent e) {
//                Toast.makeText(context, "onDoubleTap", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//
//            @Override
//            public void onLongPress(MotionEvent e) {
//                Toast.makeText(context, "onLongPress", Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        final GestureDetector detector = new GestureDetector(listener);
//
//        detector.setOnDoubleTapListener(listener);
//        detector.setIsLongpressEnabled(true);
//
//        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                return detector.onTouchEvent(event);
//            }
//        });
        // --------------------------------------------------------------------------- //

    }

    public void play_food_eaten()
    {

    }

    private void Start()
    {
        my_wall_col_img.setVisibility (View.INVISIBLE);

        SharedPreferences sp = getSharedPreferences("Game_Decision",MODE_PRIVATE);
        SharedPreferences.Editor my_editor = sp.edit ();

        String which =sp.getString ( "which","");
        my_editor.apply();

        if (which.equals ("N"))
        {
            my_game_engine.Init_Game();
            my_runnable.run ();
        }
        else if(which.equals ("L"))
        {
            SharedPreferences my_sp = getSharedPreferences("Saved_Game",MODE_PRIVATE);

            Toast.makeText(this,"In GamePlay_Activity Start Function",Toast.LENGTH_SHORT).show();

            my_game_engine.Load_Game (my_sp,my_score_text);
            my_runnable.run ();

        }
    }

    private Runnable my_runnable = new Runnable () {
        @Override
        public void run()
        {
            int food_eaten_status = my_game_engine.Update(my_score_text,my_HighestScore_text);

            if(food_eaten_status == 2)
            {
                MediaPlayer food_player = MediaPlayer.create (GamePlay_Activity.this, R.raw.coin);
                food_player.start ();

                if(!record)
                {
                    record=true;
                    food_player = MediaPlayer.create (GamePlay_Activity.this, R.raw.new_record_sound);
                    food_player.start ();
                }

            }
            else if (food_eaten_status == 1 )
            {
                MediaPlayer food_player = MediaPlayer.create (GamePlay_Activity.this, R.raw.coin);
                food_player.start ();
            }

            if(!mp.isPlaying ())
            {
               // mp.stop ();
                mp.start ();
            }

            if(my_game_engine.get_Current_Game_State() == Game_State.Lost)
            {
                handler.removeCallbacks (this);
                On_Game_Lost();
            }
            my_snake_view.set_Snake_view_map(my_game_engine.get_Map());     // Feching Map and drawing it
            my_snake_view.invalidate();         //Refresh

            if(my_game_engine.get_Current_Game_State() == Game_State.Running)
            {
                handler.postDelayed(this,update_delay);  // here this is my_runnable
            }
        }
    };


    private void Read_High_Score()
    {
        SharedPreferences sp = getSharedPreferences("High_Score_info",MODE_PRIVATE);
        SharedPreferences.Editor my_editor = sp.edit ();

        h_s = sp.getInt ("Highest_score",0);

        my_editor.apply();

        Toast.makeText(this,"Highest score "+ String.valueOf (h_s),Toast.LENGTH_SHORT).show();

        my_HighestScore_text.setTextColor (Color.WHITE);
        my_HighestScore_text.setText (String.valueOf (h_s));

        my_game_engine.Set_Hightest_Score (h_s);

    }
    public void On_Pause(View v)
    {
        if(my_game_engine.get_Current_Game_State ()==Game_State.Running)
        {
            handler.removeCallbacks (my_runnable);
            pause_Butn.setVisibility (View.INVISIBLE);

            play_Butn.setVisibility (View.VISIBLE);

            my_menu_img.setVisibility (View.VISIBLE);
            continue_Btn.setVisibility (View.VISIBLE);
            reset_Btn.setVisibility (View.VISIBLE);
            save_btn.setVisibility (View.VISIBLE);
            home_Btn.setVisibility (View.VISIBLE);
            exit_Btn.setVisibility (View.VISIBLE);
        }
    }
    public void On_Play(View v)
    {
        play_Butn.setVisibility (View.INVISIBLE);

        my_menu_img.setVisibility (View.INVISIBLE);

        continue_Btn.setVisibility (View.INVISIBLE);
        reset_Btn.setVisibility (View.INVISIBLE);
        save_btn.setVisibility (View.INVISIBLE);
        home_Btn.setVisibility (View.INVISIBLE);
        exit_Btn.setVisibility (View.INVISIBLE);

        pause_Butn.setVisibility (View.VISIBLE);

        my_runnable.run ();
    }

    public void On_Reset_Click(View v)
    {
        play_Butn.setVisibility (View.INVISIBLE);

        my_menu_img.setVisibility (View.INVISIBLE);

        continue_Btn.setVisibility (View.INVISIBLE);
        reset_Btn.setVisibility (View.INVISIBLE);
        save_btn.setVisibility (View.INVISIBLE);
        home_Btn.setVisibility (View.INVISIBLE);
        exit_Btn.setVisibility (View.INVISIBLE);

        pause_Butn.setVisibility (View.VISIBLE);

        handler.removeCallbacks (my_runnable);
        my_score_text.setText (String.valueOf (0));

        Start ();
    }

    public void On_Save_Click(View v)
    {
        SharedPreferences sp = getSharedPreferences("Saved_Game",MODE_PRIVATE);

        my_game_engine.Save_Game(sp);
    }

    public void On_Home_Click(View v)
    {
        if(!mp.isPlaying ()) {
            mp.stop ();
        }

        Intent i=new Intent (this,MainActivity.class);
        startActivity (i);
    }

    public void On_Exit_Click(View v)
    {
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);

        finish ();
        System.exit (1);
    }

//    private void start_Update_Handler()
//    {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                my_game_engine.Update(my_score_text,my_HighestScore_text);
//
//                if(my_game_engine.get_Current_Game_State() == Game_State.Running)
//                {
//                    handler.postDelayed(this,update_delay);
//                }
//                else if(my_game_engine.get_Current_Game_State() == Game_State.Lost)
//                {
//                    On_Game_Lost();
//                }
//
//                my_snake_view.set_Snake_view_map(my_game_engine.get_Map());
//
//                my_snake_view.invalidate();         //Refresh
//            }
//        },update_delay);
//    }

    private void  On_Game_Lost()
    {
       // Toast.makeText(this,"You Lost!",Toast.LENGTH_SHORT).show();
        my_wall_col_img.setVisibility (View.VISIBLE);

        int now_score=my_game_engine.get_Score ();


        if(h_s < now_score)
        {
            MediaPlayer food_player = MediaPlayer.create (GamePlay_Activity.this, R.raw.new_record_sound);
            food_player.start ();

            Toast.makeText(this,"New Highest Score "+ String.valueOf (now_score),Toast.LENGTH_SHORT).show();

            SharedPreferences sp = getSharedPreferences("High_Score_info",MODE_PRIVATE);
            SharedPreferences.Editor my_editor = sp.edit ();

            my_editor.putInt ("Highest_score",now_score);

            my_editor.apply();
        }
        else
        {
            Toast.makeText(this,"Your Score "+ String.valueOf (now_score),Toast.LENGTH_SHORT).show();
        }


        SharedPreferences sp = getSharedPreferences("Score",MODE_PRIVATE);
        SharedPreferences.Editor my_editor = sp.edit ();

        my_editor.putInt ("score",now_score);
        my_editor.apply();

        Intent in = new Intent(GamePlay_Activity.this, Ending_Activity.class);
        startActivity(in);

        if(mp.isPlaying ())     // Stopping back Music
        {
            mp.stop ();
        }

        //End_Game();

    }
    public void End_Game()
    {
        System.exit (0);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(my_game_engine.get_Current_Game_State () == Game_State.Lost)
            End_Game ();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                prevX=event.getX();
                prevY=event.getY();

                break;
            case MotionEvent.ACTION_UP:

                float newX=event.getX();
                float newY=event.getY();

                // Calculate where We swiped
                if(Math.abs(newX-prevX) > Math.abs(newY-prevY))     //  Left - Right Direction
                {
                    if(newX > prevX)        // Swiping Right
                    {
                        my_game_engine.Update_Direction(Direction.East);
                    }
                    else                    // Swiping Left
                    {
                        my_game_engine.Update_Direction(Direction.West);
                    }
                }
                else                                                //  Up - Down Direction
                {
                    if(newY > prevY)        // Swiping Down
                    {
                        my_game_engine.Update_Direction(Direction.South);
                    }
                    else                   // Swiping Up
                    {
                        my_game_engine.Update_Direction(Direction.North);
                    }
                }
                break;
        }

        return true;
    }
}
