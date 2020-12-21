package com.example.randhawa.snake_game.views;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.example.randhawa.snake_game.R;
import com.example.randhawa.snake_game.enums.Tile_type;

public class Snake_View extends View
{
    private Paint my_paint= new Paint();
    private Tile_type snake_view_map[][];

    Bitmap food_bm_1,food_bm_2,food_bm_3,food_bm_4, wall_stone, diamond, bomb;
    float food_x, food_y;

    Bitmap scaled_bm;

    public Snake_View(Context context,  AttributeSet attrs) {
        super(context, attrs);

        food_bm_1 = BitmapFactory.decodeResource (getResources (), R.drawable.apple_2,null);
        food_bm_2 = BitmapFactory.decodeResource (getResources (), R.drawable.mango_2,null);
        food_bm_3 = BitmapFactory.decodeResource (getResources (), R.drawable.single_strayberry_1,null);
        food_bm_4 = BitmapFactory.decodeResource (getResources (), R.drawable.orange_2,null);

        wall_stone = BitmapFactory.decodeResource (getResources (), R.drawable.wood_1,null);

        diamond = BitmapFactory.decodeResource (getResources (), R.drawable.button_3,null);

        bomb = BitmapFactory.decodeResource (getResources (), R.drawable.wall_1,null);

    }

    public void set_Snake_view_map(Tile_type map[][])
    {
        this.snake_view_map=map;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);


        if(snake_view_map!=null)
        {
            float tile_Size_X=canvas.getWidth()/snake_view_map.length;      // [width][height.. that's why
            float tile_Size_Y=(canvas.getHeight()/snake_view_map[0].length)-1;

            float circle_size=Math.min(tile_Size_X,tile_Size_Y)/2;

            float dia_x ;
            float dia_y ;

            int t_x= Math.round (tile_Size_X) +6;
            int t_y= Math.round (tile_Size_Y) +6;


            for (int x=0;x<snake_view_map.length;x++)
            {
                for (int y=0;y<snake_view_map[x].length;y++)
                {
                    switch (snake_view_map[x][y])
                    {
                        case Nothing:
                            my_paint.setColor(Color.WHITE);
                            break;
                        case Wall:
                            my_paint.setColor(Color.GREEN);
                            break;
                        case Snake_Head:
                            my_paint.setColor(Color.CYAN);
                            circle_size=(Math.min(tile_Size_X,tile_Size_Y)/2)+2;
                            canvas.drawCircle((x*tile_Size_X+tile_Size_X/2f +circle_size/2)-1, (y*tile_Size_Y + tile_Size_Y/2f + circle_size/2)+70-1, circle_size,my_paint);

                            break;
                        case Snake_Tail:
                            my_paint.setColor(Color.WHITE);
                            break;

                        case Diamond:
                            dia_x = x*tile_Size_X;
                            dia_y = (y*tile_Size_Y)+70;

                            t_x= Math.round (tile_Size_X) +6;
                            t_y= Math.round (tile_Size_Y) +6;

                            scaled_bm = Bitmap.createScaledBitmap (diamond,t_x+7,t_y+7,false);

                            canvas.drawBitmap (scaled_bm,dia_x,dia_y,null);

                            break;

                        case Bomb:
                            dia_x = x*tile_Size_X;
                            dia_y = (y*tile_Size_Y)+70;

                            t_x= Math.round (tile_Size_X) +6;
                            t_y= Math.round (tile_Size_Y) +6;

                            scaled_bm = Bitmap.createScaledBitmap (bomb,t_x+7,t_y+7,false);

                            canvas.drawBitmap (scaled_bm,dia_x,dia_y,null);

                            break;

                        case food:
                            my_paint.setColor(Color.BLUE);


                            break;
                    }
                    if(snake_view_map[x][y]==Tile_type.food)
                    {
                        food_x = x*tile_Size_X;
                        food_y = (y*tile_Size_Y)+70;

                        t_x= Math.round (tile_Size_X) +6;
                        t_y= Math.round (tile_Size_Y) +6;


                        int rand=(x+y)%4;

                        if( rand==0 )
                            scaled_bm = Bitmap.createScaledBitmap (food_bm_1,t_x+7,t_y+7,false);
                        else if (rand==1)
                            scaled_bm = Bitmap.createScaledBitmap (food_bm_2,t_x+5,t_y+5,false);
                        else if (rand==2)
                            scaled_bm = Bitmap.createScaledBitmap (food_bm_3,t_x+7,t_y+7,false);
                        else
                            scaled_bm = Bitmap.createScaledBitmap (food_bm_4,t_x+7,t_y+7,false);

                        canvas.drawBitmap (scaled_bm,food_x,food_y,null);
                    }
                    else if(snake_view_map[x][y] == Tile_type.Wall)
                    {
                        t_x= Math.round (tile_Size_X);
                        t_y= Math.round (tile_Size_Y);

                        scaled_bm = Bitmap.createScaledBitmap (wall_stone,t_x+4,t_y+4,false);
                        canvas.drawBitmap (scaled_bm,x*tile_Size_X,(y*tile_Size_Y)+70,null);
                    }
                    else if(snake_view_map[x][y]==Tile_type.Nothing || snake_view_map[x][y]==Tile_type.Snake_Head ||
                            snake_view_map[x][y]==Tile_type.Diamond || snake_view_map[x][y]==Tile_type.Bomb)
                    {

                    }
                    else
                        canvas.drawCircle(x*tile_Size_X+tile_Size_X/2f +circle_size/2, (y*tile_Size_Y + tile_Size_Y/2f + circle_size/2)+70, circle_size,my_paint);
//                    Picture mypic=new Picture ()
//
//                    canvas.drawPicture ();
                }
            }
        }
    }

}


