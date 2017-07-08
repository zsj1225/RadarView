package com.zsj.radarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * 创建者     朱胜军
 * 创建时间   2017/6/29 21:40
 * 描述	      ${TODO}
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RadarView extends View {

    private Paint mPaintCircle;
    private int mWidth;
    private int mHeight;

    //圆圈大小的比例
    private float[] circleProportion = {1 / 13f, 2 / 13f, 3 / 13f, 4 / 13f, 5 / 13f, 6 / 13f};
    //圆的最大半径
    private int maxRadius;
    private SweepGradient mSweepGradient;
    private Paint mPaintScan;


    public RadarView(Context context) {
        super(context);
        init();
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        //动起来
        post(run);
    }

    Matrix matrix = new Matrix();
    int speed = 5;
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            matrix.preRotate(speed,mWidth/2,mHeight/2);
            invalidate();
            postDelayed(run,130);
        }
    };

    private void init() {
        //用来画圆的笔
        mPaintCircle = new Paint();
        mPaintCircle.setColor(getResources().getColor(R.color.line_color_blue));
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setStrokeWidth(2);

        //用来画扫描的笔
        mPaintScan = new Paint();
        mPaintScan.setStyle(Paint.Style.FILL_AND_STROKE);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec));
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        //初始化扫描渲染
        mSweepGradient = new SweepGradient(mWidth / 2, mHeight / 2, new int[]{Color.TRANSPARENT,
                Color.parseColor("#84B5CA")}, null);

        //圆的最大半径
        maxRadius = Math.min(mWidth, mHeight);
    }

    private int measureSize(int measureSpec) {
        int size = 0;
        int measureMode = MeasureSpec.getMode(measureSpec);
        int measureSize = MeasureSpec.getSize(measureSpec);

        if (measureMode == MeasureSpec.EXACTLY) {
            size = measureSize;
        } else {
            size = 300;
            if (measureMode == MeasureSpec.AT_MOST) {
                size = Math.min(size, measureSize);
            }
        }
        return size;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画一环环的圆圈
        drawCircle(canvas);
        //画扫描
        drawScan(canvas);
    }

    private void drawScan(Canvas canvas) {
        //给画笔设置渲染效果
        mPaintScan.setShader(mSweepGradient);
        canvas.concat(matrix);
        canvas.drawCircle(mWidth / 2, mHeight / 2, maxRadius * circleProportion[4], mPaintScan);
    }

    private void drawCircle(Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mHeight / 2, maxRadius * circleProportion[0], mPaintCircle);
        canvas.drawCircle(mWidth / 2, mHeight / 2, maxRadius * circleProportion[1], mPaintCircle);
        canvas.drawCircle(mWidth / 2, mHeight / 2, maxRadius * circleProportion[2], mPaintCircle);
        canvas.drawCircle(mWidth / 2, mHeight / 2, maxRadius * circleProportion[3], mPaintCircle);
        canvas.drawCircle(mWidth / 2, mHeight / 2, maxRadius * circleProportion[4], mPaintCircle);
        canvas.drawCircle(mWidth / 2, mHeight / 2, maxRadius * circleProportion[5], mPaintCircle);
    }
}
