package org.mutasem.jarofawesome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by mutasemdmour on 6/5/16.
 * https://stackoverflow.com/questions/5972388/drawing-multiple-lines-in-edittext-e-g-notepad
 */
public class LinedTextView extends TextView {
    private Rect rect;
    private Paint paint;

    public LinedTextView(Context context){
        super(context);

        rect = new Rect();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(ContextCompat.getColor(context,R.color.black));
    }
    @Override
    protected void onDraw(Canvas canvas){
        int height = this.getHeight();
        int line_height = this.getLineHeight();

        int count = height / line_height;

        if (this.getLineCount() > count)
            count = getLineCount();

        Rect r = rect;
        Paint p = paint;

        int baseline = this.getLineBounds(0,r);

        for (int i = 0; i < count; i++){
            canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
            baseline += getLineHeight();
        }

        super.onDraw(canvas);
    }
}
