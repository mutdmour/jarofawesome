package org.mutasem.jarofawesome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.Serializable;

/**
 * Created by mutasemdmour on 6/5/16.
 * https://stackoverflow.com/questions/5972388/drawing-multiple-lines-in-edittext-e-g-notepad
 */
public class LinedEditText extends EditText {
    private Rect rect;
    private Paint paint;

//    private int maxCharacters = 700;
    private int modValue = 100;
    private int maxLines = 16;

    private Context context;

    public LinedEditText(Context context){
        super(context);
        this.context = context;
        rect = new Rect();
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(ContextCompat.getColor(context,R.color.black));
        this.setBackground(null);
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
        boolean fill = false;
        Paint paint1 = null;
        if (count > maxLines){
            count = maxLines;
            fill = true;
            paint1 = new Paint();
            paint1.setStyle(Paint.Style.FILL_AND_STROKE);
            paint1.setColor(ContextCompat.getColor(context,R.color.colorPrimaryLight));
        }
        for (int i = 0; i < count; i++){
            canvas.drawLine(
                    r.left,
                    baseline + 1,
                    r.right,
                    baseline + 1,
                    paint);
            baseline += getLineHeight();
        }

        if (fill == true){
            baseline -= getLineHeight()/2;
            int x1 = r.left;
            int y1 = baseline + 1;
            int len = r.right;
            int x2 = x1;
            int y2 = height;
            int num = len/getLineHeight()/2;
            for (int i = 1; i < num+1; i++){
                x2 = x2 + len/num;
                canvas.drawLine(
                        x1,
                        y1,
                        x2,
                        y2,
                        paint1
                );
//                canvas.drawLine(
//                        x2,
//                        y1,
//                        x1,
//                        y2,
//                        paint1
//                );
                x1 = x1 + len/num;
            }
        }
        super.onDraw(canvas);

    }

    //https://stackoverflow.com/questions/7092961/edittext-maxlines-not-working-user-can-still-input-more-lines-than-set
    protected void setWatcher(){
        super.onFinishInflate();
//        Log.d("onFinishInflate","hello");
        TextWatcher textWatcher = new TextWatcher() {
//
            private String text;
            private int beforeCursorPosition = 0;
//
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                Log.d("onTextChanged","before");
                text = s.toString();
                beforeCursorPosition = start;
            }
//
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                System.out.println("fuck");

//                Log.d("onTextChanged","on");
            }
//
            @Override
            public void afterTextChanged(Editable s) {
//                Log.d("onTextChanged","after");
//
//            /* turning off listener */
                removeTextChangedListener(this);
//
//            /* handling lines limit exceed */
                if (LinedEditText.this.getLineCount() > maxLines) {
                    LinedEditText.this.setText(text);
                    LinedEditText.this.setSelection(beforeCursorPosition);
                    Toast.makeText(context,
                            context.getString(R.string.too_much_text),
                            Toast.LENGTH_SHORT)
                            .show();
                }
//
//            /* handling character limit exceed */
//                int len = s.toString().length();
//                if (len > maxCharacters) {
//                    LinedEditText.this.setText(text);
//                    LinedEditText.this.setSelection(beforeCursorPosition);
//                    Toast.makeText(context,
//                            context.getString(R.string.too_much_text),
//                            Toast.LENGTH_SHORT)
//                            .show();
//                }
//                if (len % modValue == 0){
//                    String num = String.valueOf(len) + "/" + maxCharacters;
//                    Toast.makeText(context,
//                            num,
//                            Toast.LENGTH_SHORT)
//                            .show();
//                }
//
//            /* turning on listener */
                addTextChangedListener(this);
            }
        };
//
        this.addTextChangedListener(textWatcher);
//
    }

//    public int getMaxCharacters() {
//        return maxCharacters;
//    }
//
//    public void setMaxCharacters(int maxCharacters) {
//        this.maxCharacters = maxCharacters;
//    }

    @Override
    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }


}
