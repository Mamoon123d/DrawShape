package com.moon.drawline;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawLine extends View {
    Path path;
    Paint paint;
    float length;
    Canvas canvas;
    int animTime=1000;
    int lineColor;
    private int lineWidth;

    public DrawLine(Context context) {
        super(context);

      //  init();

    }

    public DrawLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context,attrs);
    }



    public DrawLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DrawLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void init()
    {
        paint = new Paint();
        paint.setColor(this.lineColor);
        paint.setStrokeWidth(this.lineWidth);
        paint.setStyle(Paint.Style.STROKE);

        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
       //int width=this.getMeasuredWidth();
       //int height=DrawLine.this.getMeasuredHeight();
        int height=(this.getLayoutParams().height/2);
        int width=this.getLayoutParams().width/2;

        Log.e("DrawLine", "height: "+height);
        path = new Path();
        path.moveTo(width/2, this.lineWidth);
        //r to l from half
        path.lineTo(this.lineWidth, this.lineWidth);
        //top to bottom
        path.lineTo(this.lineWidth, height);
        //left to right
        path.lineTo(width, height);
        //bottom to top
        path.lineTo(width, this.lineWidth);
        //right to left
        path.lineTo(width/2, this.lineWidth);

      //  path.lineTo(500, 0);
       // path.addRect(500,500,500,500,Path.Direction.CCW);

        //path.lineTo(200, 500);
       // path.lineTo(200, 300);
       // path.lineTo(350, 300);

        // Measure the path
        PathMeasure measure = new PathMeasure(path, false);
        length = measure.getLength();

        float[] intervals = new float[]{length, length};

        ObjectAnimator animator = ObjectAnimator.ofFloat(DrawLine.this, "phase", 1.0f, 0.0f);
        animator.setDuration(this.animTime);
        Log.d("TAG", "init: "+this.animTime);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
              //  animListener.animComplete();
            }
        });
    }

    public  void setAnimTime(int animTime){
        this.animTime=animTime;
    }

    //is called by animator object
    public void setPhase(float phase)
    {
        Log.d("pathview","setPhase called with:" + String.valueOf(phase));
        paint.setPathEffect(createPathEffect(length, phase, 0.0f));
        invalidate();//will calll onDraw
    }

    private static PathEffect createPathEffect(float pathLength, float phase, float offset)
    {
        return new DashPathEffect(new float[] { pathLength, pathLength },
                Math.max(phase * pathLength, offset));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
        this.canvas=canvas;
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PathView);
        this.animTime=attributes.getInteger(R.styleable.PathView_animTime,3000);
        this.lineWidth=attributes.getInteger(R.styleable.PathView_lineWidth,10);
        this.lineColor=attributes.getColor(R.styleable.PathView_lineColor,Color.BLUE);
    }
}
