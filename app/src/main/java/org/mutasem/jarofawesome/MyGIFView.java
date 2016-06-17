package org.mutasem.jarofawesome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.view.View;

import java.io.InputStream;

/**
 * Created by mutasemdmour on 6/14/16.
 */
public class MyGIFView extends View {
    Movie movie;
    long movieStart;
    InputStream is=null;

    public MyGIFView(Context context){
        super(context);
        is = context.getResources().openRawResource(+ R.drawable.shake_taylor);
        movie = Movie.decodeStream(is);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawColor(Color.WHITE);
        super.onDraw(canvas);
        long now = android.os.SystemClock.uptimeMillis();
        if (movieStart == 0){
            movieStart = now;
        }
        int relTime = (int)((now - movieStart) % movie.duration());
        movie.setTime(relTime);
        movie.draw(canvas, this.getWidth()/2-20,this.getHeight()/2-40);
        this.invalidate();
    }
}
