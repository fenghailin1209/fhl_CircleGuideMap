package com.fhl.customwidget.view;

import com.fhl.customwidget.utils.AndroidUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

public class Test extends ViewGroup {
	private static final float STROKE_WIDTH = 1F / 256F, // 描边宽度占比
			LINE_LENGTH = 3F / 32F, // 线段长度占比
			CRICLE_LARGER_RADIU = 3F / 32F,// 大圆半径
			CRICLE_SMALL_RADIU = 5F / 64F,// 小圆半径
			ARC_RADIU = 1F / 8F,// 弧半径
			ARC_TEXT_RADIU = 5F / 32F;// 弧围绕文字半径

	private Paint strokePaint;// 描边画笔

	private int size;// 控件边长

	private float strokeWidth;// 描边宽度
	private float ccX, ccY;// 中心圆圆心坐标
	private float largeCricleRadiu;// 大圆半径
	private float lineLength;// 线段长度

	public Test(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// 初始化画笔
		initPaint(context);

	}

	public Test(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Test(Context context) {
		this(context, null);
	}

	/**
	 * 初始化画笔
	 * 
	 * @param context
	 *            Fuck
	 */
	private void initPaint(Context context) {
		/*
		 * 初始化描边画笔
		 */
		strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		strokePaint.setStyle(Paint.Style.STROKE);
		strokePaint.setColor(Color.WHITE);
		strokePaint.setStrokeCap(Paint.Cap.ROUND);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 强制长宽一致
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		size = w;

		// 参数计算
		calculation();
	}

	/*
	 * 参数计算
	 */
	private void calculation() {
		// 计算描边宽度
		strokeWidth = STROKE_WIDTH * size;
		// 计算大圆半径
		largeCricleRadiu = size * CRICLE_LARGER_RADIU;

		// 计算线段长度
		lineLength = size * LINE_LENGTH;

		// 计算中心圆圆心坐标
		ccX = size / 2;
		ccY = size / 2;
		// ccY = size / 2 ;

		// 设置参数
		setPara();
	}

	/**
	 * 设置参数
	 */
	private void setPara() {
		// 设置描边宽度
		strokePaint.setStrokeWidth(strokeWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 绘制背景
		canvas.drawColor(0xFFF29B76);
		// 绘制中心圆
		canvas.drawCircle(ccX, ccY, largeCricleRadiu, strokePaint);

		for(int i = 0;i<6;i++){
			drawTopRight(canvas, 60 * i);
		}
		
		
		canvas.save();
		RectF oval = new RectF();
		oval.set(0, 0, size, size);
		float startAngle = 0;
		float sweepAngle = 30;
		canvas.drawArc(oval , startAngle, sweepAngle, false, strokePaint);
		canvas.restore();
		
//		for(int i=0;i<12;i++){
//		for(int i=0;i<1;i++){
//			drawArc(canvas,30 + i * 30);
//		}
		
	}

	private void drawArc(Canvas canvas,float degrees) {
		canvas.save();
		canvas.rotate(degrees,ccX,ccY);
		RectF oval = new RectF(0, 0, size, size);
		canvas.drawArc(oval , 0, 10, false, strokePaint);
		canvas.drawLine(ccX + ccX / 2, ccX + ccX / 4, ccX + ccX / 4, ccX + ccX / 2, strokePaint);
		canvas.drawLine(ccX + ccX / 4, ccX + ccX / 2, ccX + ccX / 2, ccX + ccX * 3 / 4, strokePaint);
//		canvas.drawRect(oval, strokePaint);
		canvas.restore();
	}

	/** 
     * 绘制右上方图形 
     *  
     * @param canvas 
     */  
    private void drawTopRight(Canvas canvas,float degrees) {  
    	canvas.save();
    	canvas.translate(ccX, ccY);
    	canvas.rotate(degrees);
    	canvas.drawLine(0, -largeCricleRadiu, 0, -lineLength * 2,strokePaint);
    	canvas.drawCircle(0, -lineLength * 3, largeCricleRadiu, strokePaint);
    	canvas.restore();
    }

}
