package com.jim.pocketaccounter.helper.record;

import com.jim.pocketaccounter.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

public class RecordButtonIncome {
	public static final int MOST_LEFT = 0, SIMPLE = 1, MOST_RIGHT = 2;
	private boolean pressed = false;
	private RectF container;
	private int icon, type;
	private String text;
	private Path shape;
	private Bitmap shadow;
	private float shadowWidth, radius, clearance;
	private Context context;
	public RecordButtonIncome(Context context, int type) {
		this.context = context;
		clearance = context.getResources().getDimension(R.dimen.one_dp);
		shape = new Path();
		this.type = type;
	}
	public void setBounds(float left, float top, float right, float bottom, float radius) {
		container = new RectF(left, top, right, bottom);
		this.radius = radius;
		switch(type) {
		case MOST_LEFT:
			initMostLeft();
			break;
		case SIMPLE:
			initSimple();
			break;
		case MOST_RIGHT:
			initMostRight();
			break;
		}
	}
	private void initMostLeft() {
		shape.moveTo(container.left+2*radius, container.top);
		shape.lineTo(container.right, container.top);
		shape.lineTo(container.right, container.bottom);
		shape.lineTo(container.left+2*radius, container.bottom);
		shape.arcTo(new RectF(container.left, container.bottom-2*radius, container.left+2*radius, container.bottom), 90.0f, 90.0f);
		shape.lineTo(container.left, container.top+2*radius);
		shape.arcTo(new RectF(container.left, container.top, container.left+2*radius, container.top+2*radius), 180.0f, 90.0f);
		shape.close();
		float bitmapWidth, bitmapHeight;
		bitmapHeight = 6*container.height()/5;
		bitmapWidth = bitmapHeight;
		Bitmap temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.left_bottom);
		shadow = Bitmap.createScaledBitmap(temp, (int)(bitmapWidth-2*clearance), (int)(bitmapHeight-2*clearance), false);
	}
	private void initMostRight() {
		shape.moveTo(container.left, container.top);
		shape.lineTo(container.right, container.top);
		shape.lineTo(container.right, container.bottom-2*radius);
		shape.arcTo(new RectF(container.right-2*radius, container.bottom-2*radius, container.right, container.bottom), 0.0f, 90.0f);
		shape.lineTo(container.left, container.bottom);
		shape.lineTo(container.left, container.top);
		shape.close();
		float bitmapWidth, bitmapHeight;
		bitmapHeight = container.height()/5;
		bitmapWidth = bitmapHeight*6;
		Bitmap temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bottom_shadow);
		shadow = Bitmap.createScaledBitmap(temp, (int)(bitmapWidth-2*clearance), (int)bitmapHeight, false);
	}
	private void initSimple() {
		shape.moveTo(container.left, container.top);
		shape.lineTo(container.right, container.top);
		shape.lineTo(container.right, container.bottom);
		shape.lineTo(container.left, container.bottom);
		shape.lineTo(container.left, container.top);
		shape.close();
		float bitmapWidth, bitmapHeight;
		bitmapHeight = container.height()/5;
		bitmapWidth = bitmapHeight*6;
		Bitmap temp = BitmapFactory.decodeResource(context.getResources(), R.drawable.bottom_shadow);
		shadow = Bitmap.createScaledBitmap(temp, (int)(bitmapWidth-2*clearance), (int)bitmapHeight, false);
	}
	public void drawButton(Canvas canvas) {
		Bitmap temp, scaled;
		Paint bitmapPaint = new Paint();
		bitmapPaint.setAntiAlias(true);
		bitmapPaint.setAlpha(0xAA);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		switch(type) {
		case MOST_LEFT:
			if (!pressed) 
				canvas.drawBitmap(shadow, container.right-shadow.getWidth(), container.top, bitmapPaint);
			canvas.drawPath(shape, paint);
			if (pressed) {
				Paint innerShadowPaint = new Paint();
				innerShadowPaint.setColor(Color.BLACK);
				innerShadowPaint.setAlpha(0x22);
				innerShadowPaint.setAntiAlias(true);
				canvas.drawPath(shape, innerShadowPaint);
				Bitmap inTemp = BitmapFactory.decodeResource(context.getResources(), R.drawable.record_pressed_first);
				Bitmap innerShadow = Bitmap.createScaledBitmap(inTemp, (int)(container.width()/5), (int)container.height(), false);
				innerShadowPaint.setAlpha(0x66);
				canvas.drawBitmap(innerShadow, container.right-innerShadow.getWidth(), container.top, innerShadowPaint);
			}
			break;
		case SIMPLE:
			if (!pressed) 
				canvas.drawBitmap(shadow, container.left-shadow.getHeight(), container.bottom, bitmapPaint);
			canvas.drawPath(shape, paint);
			if (pressed) {
				Paint innerShadowPaint = new Paint();
				innerShadowPaint.setColor(Color.BLACK);
				innerShadowPaint.setAlpha(0x22);
				canvas.drawPath(shape, innerShadowPaint);
				Bitmap inTemp = BitmapFactory.decodeResource(context.getResources(), R.drawable.record_pressed_first);
				Bitmap innerShadow = Bitmap.createScaledBitmap(inTemp, (int)(container.width()/5), (int)container.height(), false);
				innerShadowPaint.setAlpha(0x66);
				canvas.drawBitmap(innerShadow, container.right-innerShadow.getWidth(), container.top, innerShadowPaint);
			}
			break;
		case MOST_RIGHT:
			if (!pressed) 
				canvas.drawBitmap(shadow, container.left-shadow.getHeight(), container.bottom, bitmapPaint);
			canvas.drawPath(shape, paint);
			if (pressed) {
				Paint innerShadowPaint = new Paint();
				innerShadowPaint.setColor(Color.BLACK);
				innerShadowPaint.setAlpha(0x22);
				canvas.drawPath(shape, innerShadowPaint);
			}
			break;
		}
		temp = BitmapFactory.decodeResource(context.getResources(), icon);
		scaled = Bitmap.createScaledBitmap(temp, (int)context.getResources().getDimension(R.dimen.thirty_dp), (int)context.getResources().getDimension(R.dimen.thirty_dp), false);
		canvas.drawBitmap(scaled, container.centerX()-scaled.getWidth()/2, container.centerY()-scaled.getHeight(), bitmapPaint);
		Paint textPaint = new Paint();
		textPaint.setColor(Color.BLACK);
		textPaint.setTextSize(context.getResources().getDimension(R.dimen.ten_sp));
		Rect bounds = new Rect();
		textPaint.getTextBounds(text, 0, text.length(), bounds);
		canvas.drawText(text, container.centerX()-bounds.width()/2, container.centerY()+2*bounds.height(), textPaint);
	}
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	public RectF getContainer() {
		return container;
	}
	public Path getShape() {
		return shape;
	}
	public Bitmap getShadow() {
		return shadow;
	}
	public float getShadowWidth() {
		return shadowWidth;
	}
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
