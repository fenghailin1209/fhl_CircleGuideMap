package com.fhl.customwidget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.renderscript.Type;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fhl.customwidget.R;
import com.fhl.customwidget.utils.AndroidUtil;

public class GuideMapViewGroup2 extends ViewGroup implements OnClickListener {
	// 菜单条目个数
	private int mMeanItemCount;
	// 平均角度
	private float averageAngle;
	// 每个item的角度
	private double angle;
	// 实际的半径
	private int radius;
	// 画笔
	private Paint paint;
	// 外面圆的半径
	private int circleRadius;
	// 角度间隔
	private float angleSpace;
	// MeanItem宽度
	private int meanWidth;
	// 圆形菜单的宽度
	private int itemMeanCircleWidth;
	// 箭头的长度
	private int arrowWidth;
	// 消息条数
	private int messageCount;

	public GuideMapViewGroup2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		getAttributeSet(context, attrs, defStyle);
		
		init();
		
		setWillNotDraw(false);

	}

	public GuideMapViewGroup2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GuideMapViewGroup2(Context context) {
		this(context, null);
	}
	
	/**
	 * 获取自定义控件属性值
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	private void getAttributeSet(Context context, AttributeSet attrs, int defStyle) {
		TypedArray t = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GuideMapSetting, defStyle, 0);
		itemMeanCircleWidth = (int) t.getDimension(R.styleable.GuideMapSetting_MeanItemWidth, AndroidUtil.dip2px(getContext(), 70));
		arrowWidth = (int) t.getDimension(R.styleable.GuideMapSetting_ArrowsWidth, AndroidUtil.dip2px(getContext(), 10));
		paintColor = t.getColor(R.styleable.GuideMapSetting_PaintColor, Color.BLACK);
		paintWidth = t.getInt(R.styleable.GuideMapSetting_PaintWidth, 5);
		Log.d("", "--->>>paintColor:"+paintColor);
	}

	private void init() {
		paint = new Paint();
		paint.setColor(paintColor);
//		paint.setColor(getContext().getResources().getColor(R.color.orange));
		paint.setStrokeWidth(paintWidth);
		paint.setAntiAlias(true);
		paint.setStyle(Style.STROKE);
		paint.setStrokeCap(Cap.ROUND);
	}

	/**
	 * 布局
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layoutMeanItem();
	}

	/**
	 * 布局Mean Item
	 */
	private void layoutMeanItem() {
		int count = getChildCount();

		averageAngle = 360 / (count - 1);

		radius = getMeasuredWidth() / 2;

		// 圆心到菜单圆的距离
		circleRadius = radius * 2 / 3;

		// 角度间隔
		angleSpace = (averageAngle - (90 - averageAngle));

		for (int i = 0; i < count; i++) {
			angle = averageAngle * i + angleSpace;
			// angle = averageAngle * i + 54;

			View meanItemView = getChildAt(i);

			meanWidth = meanItemView.getMeasuredWidth();

			int left = (int) (radius
					+ (Math.cos(Math.toRadians(angle)) * circleRadius) - meanWidth / 2);

			int top = (int) (radius
					+ (Math.sin(Math.toRadians(angle)) * circleRadius) - meanWidth / 2);

			int right = left + meanWidth;

			int bottom = top + meanWidth;

			// 最后一个View在整个ViewGroup的正中间
			if (i == (count - 1)) {
				meanItemView.layout(radius - meanWidth / 2, radius - meanWidth
						/ 2, radius + meanWidth / 2, radius + meanWidth / 2);
			} else {
				meanItemView.layout(left, top, right, bottom);
			}
		}
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 如果需要画圆的画就打开此处
		// canvas.drawCircle(radius, radius, circleRadius, paint);
		// 循环画箭头和弧形
		for (int i = 0; i < 4; i++) {
			float degrees = 72 + i * 72;
			drawArrow(canvas, degrees);
		}
	}

	

	/**
	 * 画箭头和弧
	 * 
	 * @param canvas
	 */
	private void drawArrow(Canvas canvas, float degrees) {
		// 锁定画布
		canvas.save();
		// 旋转画布并且以（radius，radius）点作为旋转中心点
		canvas.rotate(degrees, radius, radius);

		// 计算第二个meanItem与大圆相交的地方的右下点坐标
		float d = ((float) itemMeanCircleWidth / 4) / (float) circleRadius;
		double meanAngle = Math.toDegrees(Math.asin(d));

		double meanItem2radius = averageAngle / 2 - (meanAngle * 2);

		float meanItemEndX = radius
				- ((float) Math.sin(Math.toRadians(meanItem2radius)))
				* circleRadius;
		float meanItemEndY = radius
				+ ((float) Math.cos(Math.toRadians(meanItem2radius)))
				* circleRadius;

		// 定义矩形的范围
		RectF rectF = new RectF();

		float left = radius - circleRadius;
		float top = radius - circleRadius;
		float right = radius + circleRadius;
		float bottom = radius + circleRadius;
		rectF.set(left, top, right, bottom);

		// 画第一条弧线
		float startAngle = (float) (angleSpace + meanAngle * 2);
		float sweepAngle = (float) (meanItem2radius * 2);
		canvas.drawArc(rectF, startAngle, sweepAngle, false, paint);

		// 画箭头
		// 用相似三角形法则得到箭头三角形的角度（好像有点问题，但是效果还可以）
		double triangleAngle = Math.toDegrees(Math.atan(meanItemEndX
				/ meanItemEndY));
		float arrowX1 = (float) (meanItemEndX + (Math.cos(Math
				.toRadians(triangleAngle)) * arrowWidth));
		float arrowY1 = (float) (meanItemEndY - (Math.sin(Math
				.toRadians(triangleAngle)) * arrowWidth));
		// 画箭头的第一条线
		canvas.drawLine(meanItemEndX, meanItemEndY, arrowX1, arrowY1, paint);

		float arrowX2 = (float) (meanItemEndX + (Math.sin(Math
				.toRadians(triangleAngle)) * arrowWidth));
		float arrowY2 = (float) (meanItemEndY + (Math.cos(Math
				.toRadians(triangleAngle)) * arrowWidth));
		// 画箭头的第二条线
		canvas.drawLine(meanItemEndX, meanItemEndY, arrowX2, arrowY2, paint);

		// 释放画布
		canvas.restore();
	}

	/**
	 * 测量
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 测量ViewGroup
		setMeasuredDimension(measuredWidth(widthMeasureSpec),
				measuredHeight(heightMeasureSpec));

		// 测量子View
		setChilderMeasureDimension();
	}

	/**
	 * 测量子View
	 */
	private void setChilderMeasureDimension() {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View meanItemView = getChildAt(i);

			int meanItemSize = AndroidUtil.dip2px(getContext(), 110);
			int meanItemMode = MeasureSpec.EXACTLY;

			int parentWidthMeasureSpec;
			int parentHeightMeasureSpec;

			parentHeightMeasureSpec = parentWidthMeasureSpec = MeasureSpec
					.makeMeasureSpec(meanItemSize, meanItemMode);

			measureChild(meanItemView, parentWidthMeasureSpec,
					parentHeightMeasureSpec);
		}
	}

	/**
	 * 宽度的测量
	 * 
	 * @param widthMeasureSpec
	 * @return
	 */
	private int measuredWidth(int widthMeasureSpec) {
		int width;
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width = AndroidUtil.getScreenWidth(getContext());
		}
		return width;
	}

	/**
	 * 高度的测量
	 * 
	 * @param heightMeasureSpec
	 * @return
	 */
	private int measuredHeight(int heightMeasureSpec) {
		int height;
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = AndroidUtil.getScreenWidth(getContext());
		}
		return height;
	}

	/**
	 * 将图片资源和文字资源设置到相应的菜单中
	 * 
	 * @param resIds
	 * @param texts
	 */
	public void setMeanItemIconAndText(int resIds[], String texts[],
			int msgCount) {
		if (resIds == null || texts == null) {
			new IllegalAccessException("图片集合或文字集合不能为空");
		}
		mMeanItemCount = Math.min(resIds.length, texts.length);

		for (int i = 0; i < mMeanItemCount; i++) {
			View meanItemView = inflate(getContext(),
					R.layout.circle_mean_item, null);
			ImageView itemImageView = (ImageView) meanItemView
					.findViewById(R.id.id_circle_menu_item_image);
			TextView itemText = (TextView) meanItemView
					.findViewById(R.id.id_circle_menu_item_text);
			TextView itemMsg = (TextView) meanItemView
					.findViewById(R.id.id_circle_menu_item_msg);

			// 设置图片
			int imageId = resIds[i];
			if (imageId != 0) {
				itemImageView.setImageResource(resIds[i]);
			}

			// 设置文字
			String text = texts[i];
			if (!TextUtils.isEmpty(text)) {
				itemText.setText(text);
			}

			// 设置消息提醒
			// 只有业务消息才显示消息条数
			if (i == 1) {
				if (msgCount != 0) {
					itemMsg.setVisibility(View.VISIBLE);
					itemMsg.setText(msgCount + "");
				} else {
					itemMsg.setVisibility(View.GONE);
				}
			} else {
				itemMsg.setVisibility(View.GONE);
			}

			// 设置Id和点击事件
			itemImageView.setId(i);
			itemImageView.setOnClickListener(this);

			// 添加到ViewGroup中
			addView(meanItemView);
		}
	}

	
	/**
	 * 设置消息
	 */
	private void setMessage() {
		//得到消息的View
		View meanItemView = getChildAt(1);
		// 设置消息值
		TextView itemImageView = (TextView) meanItemView
				.findViewById(R.id.id_circle_menu_item_msg);
		if (messageCount > 0) {
			itemImageView.setVisibility(View.VISIBLE);
			itemImageView.setText(messageCount + "");
		} else {
			itemImageView.setVisibility(View.GONE);
		}
	}
	
	
	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
		setMessage();
	}

	@Override
	public void onClick(View v) {
		if (onMeanItemClick != null) {
			onMeanItemClick.onMeanItemClick(v.getId());
		}
	}

	// 菜单点击事件设置
	private OnMeanItemClick onMeanItemClick;
	private int paintWidth;
	private int paintColor;

	public interface OnMeanItemClick {
		public void onMeanItemClick(int position);
	}

	public void setOnMeanItemClickListener(OnMeanItemClick onMeanItemClick) {
		this.onMeanItemClick = onMeanItemClick;
	}

}
