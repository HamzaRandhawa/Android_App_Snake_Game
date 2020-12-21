package com.example.randhawa.snake_game.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.randhawa.snake_game.GamePlay_Activity;
import com.example.randhawa.snake_game.R;
import com.example.randhawa.snake_game.classes.Coordinates;
import com.example.randhawa.snake_game.enums.Direction;
import com.example.randhawa.snake_game.enums.Game_State;
import com.example.randhawa.snake_game.enums.Tile_type;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

public class Game_Engine
{
    public static final int Game_Width=28;      //28
    public static final int Game_Height=39;     //42

    private List<Coordinates> walls = new ArrayList<>();
    private List<Coordinates> snake = new ArrayList<>();

    private List<Coordinates> food = new ArrayList<>();
    private List<Coordinates> diamond = new ArrayList<> ();
    private List<Coordinates> bomb = new ArrayList<> ();

    Context context;

    int score;
    int highest_score=0;

    int timer = 0;      // Timer
    long diamond_timer = 0;      // Diamond time in milliseconds
    long bomb_timer = 0;      // Bomb time in milliseconds

    private boolean highest_changed = false;
    private boolean Is_diamond = false;
    private boolean Is_bomb = false;

    private Game_State cur_game_state;

    private Direction cur_direction;

    boolean record=false;
    public void Set_Hightest_Score(int h_s)
    {
        highest_score=h_s;
    }

    public boolean Is_Highest_Changed()
    {
        return highest_changed;
    }

    public Game_Engine()
    {
        cur_game_state=Game_State.Running;
        cur_direction=Direction.East;
    }

    public void Set_Context(Context c)
    {
        context=c;
    }

    public void Init_Game()
    {
        score=0;
        Add_Snake();
        Add_Walls();
        Add_Food();
    }

    public void Save_Game(SharedPreferences sp)
    {
        SharedPreferences.Editor my_editor = sp.edit ();
        int s_size=snake.size ();

        my_editor.putInt ("Score",score);
        my_editor.putString ("Snake_Direction",cur_direction.name () );

        my_editor.putInt ("Food_X" ,food.get (0).getX ());
        my_editor.putInt ("Food_Y" ,food.get (0).getY ());

        my_editor.putInt ("Snake_Size",s_size);
        for (int i = 0; i < s_size; i++)
        {
            my_editor.putInt ("Snake_" + i + "_X",snake.get (i).getX ());
            my_editor.putInt ("Snake_" + i + "_Y",snake.get (i).getY ());
        }
        my_editor.apply();
    }
    public void Load_Game(SharedPreferences sp, TextView score_text)
    {
        int s_size = sp.getInt ("Snake_Size",0);
        score = sp.getInt ("Score",0);

        cur_direction = Direction.valueOf (sp.getString ("Snake_Direction","East"));

        food.add (new Coordinates ( sp.getInt ("Food_X",10), sp.getInt ("Food_Y",10) ));

        for (int i = 0; i < s_size; i++)
        {
            snake.add (new Coordinates (sp.getInt ("Snake_" + i + "_X",10),sp.getInt ("Snake_" + i + "_Y",10)));
        }

        Add_Walls();
        score_text.setText (String.valueOf(score));
    }

    private void Add_Snake()
    {
        snake.clear();

        snake.add(new Coordinates(7,7));
        snake.add(new Coordinates(6,7));
        snake.add(new Coordinates(5,7));
        snake.add(new Coordinates(4,7));
        snake.add(new Coordinates(3,7));
        snake.add(new Coordinates(2,7));
    }

    private void Add_Walls()
    {
        // Top and Bottom Walls
        for (int x = 0; x < Game_Width; x++)
        {
            walls.add(new Coordinates(x,0));
            walls.add(new Coordinates(x,Game_Height-1));
        }

        // Left and Right Walls
        for (int y = 1; y < Game_Height; y++)
        {
            walls.add(new Coordinates(0,y));
            walls.add(new Coordinates(Game_Width-1,y));
        }
    }

    private void Add_Food()
    {
        Coordinates food_coord=null;
        Random random=new Random();
        boolean added=false;

        while(!added)
        {
            int x=2 + random.nextInt(Game_Width-4);
            int y=2 + random.nextInt(Game_Height-4);

            food_coord=new Coordinates(x,y);
            boolean collision=false;
            for (Coordinates s:snake)
            {
                if(s.equals(food_coord))
                {
                    collision=true;
                    break;
                }
            }

            added = ! collision;
        }
        food.add(food_coord);
    }

    private void Add_Diamond()
    {
        Coordinates diamond_coord=null;
        Random random=new Random();
        boolean added=false;

        while(!added)
        {
            int x = 2 + random.nextInt(Game_Width-4);
            int y = 2 + random.nextInt(Game_Height-4);

            diamond_coord=new Coordinates(x,y);
            boolean collision=false;
            for (Coordinates s:snake)
            {
                if(s.equals(diamond_coord))
                {
                    collision=true;
                    break;
                }
            }

            if(food.equals (diamond_coord))
                collision = true;

            added = ! collision;
        }
        diamond.add(diamond_coord);

        diamond_timer = System.currentTimeMillis () + 16000;
        Is_diamond = true;
    }

    private void Add_Bomb()
    {
        Coordinates bomb_coord=null;
        Random random=new Random();
        boolean added=false;

        while(!added)
        {
            int x = 2 + random.nextInt(Game_Width-4);
            int y = 2 + random.nextInt(Game_Height-4);

            bomb_coord=new Coordinates(x,y);
            boolean collision=false;
            for (Coordinates s:snake)
            {
                if(s.equals(bomb_coord))
                {
                    collision=true;
                    break;
                }
            }

            if(food.equals (bomb_coord))
                collision = true;
            if(Is_diamond)
            {
                if(diamond.equals (bomb_coord))
                    collision = true;
            }

            added = ! collision;
        }
        bomb.add(bomb_coord);

        bomb_timer = System.currentTimeMillis () + 40000;
        Is_bomb = true;
    }

    public int get_Score()
    {
        return score;
    }

    public void Update_Direction(Direction new_dir)
    {
        if(Math.abs(new_dir.ordinal() - cur_direction.ordinal()) % 2 == 1 )
        {
            cur_direction = new_dir;
        }
    }

    private Coordinates get_Snake_Head()
    {
        return snake.get(0);
    }


    public int Update(TextView score_text,TextView highest_score_text)
    {
        if(Is_diamond)
        {
            diamond_timer -= 1000;  // Diamond count down

            if(diamond_timer < System.currentTimeMillis () - 16000)
            {
                Is_diamond = false;
                diamond.clear ();
            }
        }

        if(Is_bomb)
        {
            bomb_timer  -= 1000;  // Bomb count down

            if(bomb_timer < System.currentTimeMillis () - 40000)
            {
                Is_bomb = false;
                bomb.clear ();
            }
        }
        // Update Snake
        switch (cur_direction)
        {
            case North:
                Update_Snake(0,-1);
                break;

                case East:
                    Update_Snake(1,0);
                    break;
                case South:
                    Update_Snake(0 ,1);
                    break;
                case West:
                    Update_Snake(-1,0);
                    break;
        }

        // Check Wall Collision
        for (Coordinates w: walls)
        {
            if(snake.get(0).equals(w))
            {
                cur_game_state=Game_State.Lost;
            }
        }
        // Check self Collision
        for (int i=1 ;i<snake.size(); i++)
        {
            if(snake.get(i).equals(get_Snake_Head()))
                cur_game_state=Game_State.Lost;
        }
        if(Is_diamond)      // Check Diamond Catching
        {
            if(diamond.get (0).equals (get_Snake_Head ()) )
            {
                score += 5;
                score_text.setText (String.valueOf(score));

                diamond.clear ();
                Is_diamond =  false;
            }
        }

        if(Is_bomb)      // Check Bomb Collision
        {
            if(bomb.get (0).equals (get_Snake_Head ()) )
            {
                cur_game_state=Game_State.Lost;

//                score += 5;
//                score_text.setText (String.valueOf(score));
//
//                bomb.clear ();
//                Is_bomb =  false;
            }
        }

        // Check Food Eating
        if(food.get(0).equals(get_Snake_Head()))
        {
            //MediaPlayer mp=MediaPlayer.create (this,context.getResources(). get(R.raw.coin));
            score++;

            timer++;

            //highest_score_text
            score_text.setText (String.valueOf(score));

            Snake_Regrow();
            food.clear();
            Add_Food();

            if(timer % 5 ==0)
                Add_Diamond ();

            if(timer % 6 ==0)
                Add_Bomb ();

            if(score > highest_score )
            {
                if(!record)
                {
                    record=true;
                    //Context context = android.content.ContextWrapper.getApplicationContext();
                    Toast.makeText (context,"New Record!!",Toast.LENGTH_LONG).show ();
                }

                highest_changed=true;
                highest_score_text.setText (String.valueOf (score));

                return 2;
            }

            return 1;
        }
        return 0;
    }

    private void Snake_Regrow()
    {
        int x=snake.get(snake.size()-1).getX();
        int y=snake.get(snake.size()-1).getY();

        Coordinates growing=new Coordinates(x,y);
        snake.add(growing);
    }

    private void Update_Snake(int x, int y)
    {
        for (int i = snake.size()-1; i >0 ; i--)
        {
            snake.get(i).setX(snake.get(i-1).getX());       // snake.get(i) same as snake[i]
            snake.get(i).setY(snake.get(i-1).getY());
        }

        snake.get(0).setX(snake.get(0).getX()+x);
        snake.get(0).setY(snake.get(0).getY()+y);
    }

    public Tile_type[][] get_Map()
    {
        Tile_type[][] map = new Tile_type[Game_Width][Game_Height];

        // Setting everything to Nothing
        for (int x=0; x < Game_Width; x++)
        {
            for (int y = 0; y < Game_Height; y++)
            {
                map[x][y]=Tile_type.Nothing;
            }
        }

        // SNAKE
        for (Coordinates s: snake)
        {
            map[s.getX()][s.getY()]=Tile_type.Snake_Tail;
        }
        map[snake.get(0).getX()][snake.get(0).getY()]=Tile_type.Snake_Head;

        // Food
        map[food.get(0).getX()][food.get(0).getY()]=Tile_type.food;


        for (Coordinates wall: walls)                   //Iteration for Walls' Array List       for (coordinates wall = walls[0];
        {
            map[wall.getX()][wall.getY()]=Tile_type.Wall;
        }

        if(Is_diamond)      // If Diamond
        {
            map[diamond.get (0).getX ()][diamond.get (0).getY ()] = Tile_type.Diamond;
        }

        if(Is_bomb)
        {
            map[bomb.get (0).getX ()][bomb.get (0).getY ()] = Tile_type.Bomb;

        }
        return map;
    }

    public Game_State get_Current_Game_State()
    {
        return cur_game_state;
    }



}
