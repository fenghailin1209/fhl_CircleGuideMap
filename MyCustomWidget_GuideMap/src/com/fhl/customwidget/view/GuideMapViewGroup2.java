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
	// �˵���Ŀ����
	private int mMeanItemCount;
	// ƽ���Ƕ�
	private float averageAngle;
	// ÿ��item�ĽǶ�
	private double angle;
	// ʵ�ʵİ뾶
	private int radius;
	// ����
	private Paint paint;
	// ����Բ�İ뾶
	private int circleRadius;
	// �Ƕȼ��
	private float angleSpace;
	// MeanItem���
	private int meanWidth;
	// Բ�β˵��Ŀ��
	private int itemMeanCircleWidth;
	// ��ͷ�ĳ���
	private int arrowWidth;
	// ��Ϣ����
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
	 * ��ȡ�Զ���ؼ�����ֵ
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
	 * ����
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		layoutMeanItem();
	}

	/**
	 * ����Mean Item
	 */
	private void layoutMeanItem() {
		int count = getChildCount();

		averageAngle = 360 / (count - 1);

		radius = getMeasuredWidth() / 2;

		// Բ�ĵ��˵�Բ�ľ���
		circleRadius = radius * 2 / 3;

		// �Ƕȼ��
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

			// ���һ��View������ViewGroup�����м�
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
		// �����Ҫ��Բ�Ļ��ʹ򿪴˴�
		// canvas.drawCircle(radius, radius, circleRadius, paint);
		// ѭ������ͷ�ͻ���
		for (int i = 0; i < 4; i++) {
			float degrees = 72 + i * 72;
			drawArrow(canvas, degrees);
		}
	}

	

	/**
	 * ����ͷ�ͻ�
	 * 
	 * @param canvas
	 */
	private void drawArrow(Canvas canvas, float degrees) {
		// ��������
		canvas.save();
		// ��ת���������ԣ�radius��radius������Ϊ��ת���ĵ�
		canvas.rotate(degrees, radius, radius);

		// ����ڶ���meanItem���Բ�ཻ�ĵط������µ�����
		float d = ((float) itemMeanCircleWidth / 4) / (float) circleRadius;
		double meanAngle = Math.toDegrees(Math.asin(d));

		double meanItem2radius = averageAngle / 2 - (meanAngle * 2);

		float meanItemEndX = radius
				- ((float) Math.sin(Math.toRadians(meanItem2radius)))
				* circleRadius;
		float meanItemEndY = radius
				+ ((float) Math.cos(Math.toRadians(meanItem2radius)))
				* circleRadius;

		// ������εķ�Χ
		RectF rectF = new RectF();

		float left = radius - circleRadius;
		float top = radius - circleRadius;
		float right = radius + circleRadius;
		float bottom = radius + circleRadius;
		rectF.set(left, top, right, bottom);

		// ����һ������
		float startAngle = (float) (angleSpace + meanAngle * 2);
		float sweepAngle = (float) (meanItem2radius * 2);
		canvas.drawArc(rectF, startAngle, sweepAngle, false, paint);

		// ����ͷ
		// �����������η���õ���ͷ�����εĽǶȣ������е����⣬����Ч�������ԣ�
		double triangleAngle = Math.toDegrees(Math.atan(meanItemEndX
				/ meanItemEndY));
		float arrowX1 = (float) (meanItemEndX + (Math.cos(Math
				.toRadians(triangleAngle)) * arrowWidth));
		float arrowY1 = (float) (meanItemEndY - (Math.sin(Math
				.toRadians(triangleAngle)) * arrowWidth));
		// ����ͷ�ĵ�һ����
		canvas.drawLine(meanItemEndX, meanItemEndY, arrowX1, arrowY1, paint);

		float arrowX2 = (float) (meanItemEndX + (Math.sin(Math
				.toRadians(triangleAngle)) * arrowWidth));
		float arrowY2 = (float) (meanItemEndY + (Math.cos(Math
				.toRadians(triangleAngle)) * arrowWidth));
		// ����ͷ�ĵڶ�����
		canvas.drawLine(meanItemEndX, meanItemEndY, arrowX2, arrowY2, paint);

		// �ͷŻ���
		canvas.restore();
	}

	/**
	 * ����
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// ����ViewGroup
		setMeasuredDimension(measuredWidth(widthMeasureSpec),
				measuredHeight(heightMeasureSpec));

		// ������View
		setChilderMeasureDimension();
	}

	/**
	 * ������View
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
	 * ��ȵĲ���
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
	 * �߶ȵĲ���
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
	 * ��ͼƬ��Դ��������Դ���õ���Ӧ�Ĳ˵���
	 * 
	 * @param resIds
	 * @param texts
	 */
	public void setMeanItemIconAndText(int resIds[], String texts[],
			int msgCount) {
		if (resIds == null || texts == null) {
			new IllegalAccessException("ͼƬ���ϻ����ּ��ϲ���Ϊ��");
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

			// ����ͼƬ
			int imageId = resIds[i];
			if (imageId != 0) {
				itemImageView.setImageResource(resIds[i]);
			}

			// ��������
			String text = texts[i];
			if (!TextUtils.isEmpty(text)) {
				itemText.setText(text);
			}

			// ������Ϣ����
			// ֻ��ҵ����Ϣ����ʾ��Ϣ����
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

			// ����Id�͵���¼�
			itemImageView.setId(i);
			itemImageView.setOnClickListener(this);

			// ��ӵ�ViewGroup��
			addView(meanItemView);
		}
	}

	
	/**
	 * ������Ϣ
	 */
	private void setMessage() {
		//�õ���Ϣ��View
		View meanItemView = getChildAt(1);
		// ������Ϣֵ
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

	// �˵�����¼�����
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
