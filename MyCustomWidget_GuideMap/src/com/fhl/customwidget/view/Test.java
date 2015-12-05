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
	private static final float STROKE_WIDTH = 1F / 256F, // ��߿��ռ��
			LINE_LENGTH = 3F / 32F, // �߶γ���ռ��
			CRICLE_LARGER_RADIU = 3F / 32F,// ��Բ�뾶
			CRICLE_SMALL_RADIU = 5F / 64F,// СԲ�뾶
			ARC_RADIU = 1F / 8F,// ���뾶
			ARC_TEXT_RADIU = 5F / 32F;// ��Χ�����ְ뾶

	private Paint strokePaint;// ��߻���

	private int size;// �ؼ��߳�

	private float strokeWidth;// ��߿��
	private float ccX, ccY;// ����ԲԲ������
	private float largeCricleRadiu;// ��Բ�뾶
	private float lineLength;// �߶γ���

	public Test(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// ��ʼ������
		initPaint(context);

	}

	public Test(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public Test(Context context) {
		this(context, null);
	}

	/**
	 * ��ʼ������
	 * 
	 * @param context
	 *            Fuck
	 */
	private void initPaint(Context context) {
		/*
		 * ��ʼ����߻���
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
		// ǿ�Ƴ���һ��
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		size = w;

		// ��������
		calculation();
	}

	/*
	 * ��������
	 */
	private void calculation() {
		// ������߿��
		strokeWidth = STROKE_WIDTH * size;
		// �����Բ�뾶
		largeCricleRadiu = size * CRICLE_LARGER_RADIU;

		// �����߶γ���
		lineLength = size * LINE_LENGTH;

		// ��������ԲԲ������
		ccX = size / 2;
		ccY = size / 2;
		// ccY = size / 2 ;

		// ���ò���
		setPara();
	}

	/**
	 * ���ò���
	 */
	private void setPara() {
		// ������߿��
		strokePaint.setStrokeWidth(strokeWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ���Ʊ���
		canvas.drawColor(0xFFF29B76);
		// ��������Բ
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
     * �������Ϸ�ͼ�� 
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
