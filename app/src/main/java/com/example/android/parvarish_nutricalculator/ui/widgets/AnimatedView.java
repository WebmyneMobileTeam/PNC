package com.example.android.parvarish_nutricalculator.ui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.Log;
import android.view.View;

import com.example.android.parvarish_nutricalculator.R;

import java.io.InputStream;

/**
 * Created by Android on 25-06-2015.
 */
public class AnimatedView extends View {
    Movie movie;
    InputStream is=null;
    long moviestart;
    public AnimatedView(Context context) {
        super(context);
        is=context.getResources().openRawResource(R.raw.b);
        movie=Movie.decodeStream(is);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0x00000000);
        super.onDraw(canvas);
        long now=android.os.SystemClock.uptimeMillis();
        System.out.println("now="+now);
        if (moviestart == 0) {   // first time
            moviestart = now;

        }

        System.out.println("\tmoviestart="+moviestart);
        int relTime = (int)((now - moviestart) % movie.duration()) ;

        System.out.println("time="+relTime+"\treltime="+movie.duration());
        movie.setTime(relTime);

        Log.e("total height -: ", "" + this.getHeight());
        //canvas , 0 for x-axis and this.getHieght()/2 for y-axis
        movie.draw(canvas,0,(this.getHeight()-240)/2);

        this.invalidate();
    }
}
