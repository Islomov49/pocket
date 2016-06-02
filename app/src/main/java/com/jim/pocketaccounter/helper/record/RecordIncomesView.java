package com.jim.pocketaccounter.helper.record;

import java.util.ArrayList;

import com.jim.pocketaccounter.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("DrawAllocation")
public class RecordIncomesView extends View {
	private final float workspaceCornerRadius, workspaceMargin;
	private Bitmap workspaceShader;
	private RectF workspace;
	private ArrayList<RecordButtonIncome> buttons;
	private int[] icons;
	private String[] categories;
	private float firstX, firstY;
	public RecordIncomesView(Context context) {
		super(context);
		workspaceCornerRadius = getResources().getDimension(R.dimen.five_dp);
		workspaceMargin = getResources().getDimension(R.dimen.twenty_dp);
		String[] tempIcons = getResources().getStringArray(R.array.icons);
		icons = new int[tempIcons.length];
		for (int i=0; i<tempIcons.length; i++) 
			icons[i] = getResources().getIdentifier(tempIcons[i], "drawable", context.getPackageName());
		categories = new String[4];
		for (int i=0; i<categories.length; i++)
			categories[i] = "Category "+i;
		buttons = new ArrayList<RecordButtonIncome>();
		for (int i=0; i<4; i++) {
			RecordButtonIncome button = null;
			int type = 0;
			switch(i) {
			case 0:
				type = RecordButtonIncome.MOST_LEFT;
				break;
			case 1:
			case 2:
				type = RecordButtonIncome.SIMPLE;
				break;
			case 3:
				type = RecordButtonIncome.MOST_RIGHT;
				break;
			}
			button = new RecordButtonIncome(getContext(), type);
			button.setIcon(icons[i]);
			button.setText(categories[i]);
			buttons.add(button);
		}
		setClickable(true);
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		workspace = new RectF(workspaceMargin, workspaceMargin, getWidth()-workspaceMargin, getHeight()-workspaceMargin);
		drawButtons(canvas);
		drawWorkspaceShader(canvas);
	}
	private void drawButtons(Canvas canvas) {
		float width, height;
		width = workspace.width()/4;
		height = workspace.height();
		for (int i=0; i<4; i++) {
			float left, top, right, bottom;
			left = workspace.left+(i%4)*width;
			top = workspace.top+((int)Math.floor(i/4)*height);
			right = workspace.left+(i%4+1)*width;
			bottom = workspace.top+((int)(Math.floor(i/4)+1)*height);
			buttons.get(i).setBounds(left, top, right, bottom, workspaceCornerRadius);
		}
		for (int i=0; i<buttons.size(); i++) 
			buttons.get(i).drawButton(canvas);
		Paint borderPaint = new Paint();
		borderPaint.setColor(ContextCompat.getColor(getContext(), R.color.record_borders));
		borderPaint.setStrokeWidth(getResources().getDimension(R.dimen.two_dp));
		for (int i=0; i<3; i++) 
			canvas.drawLine(workspace.left+(i+1)*width, workspace.top, workspace.left+(i+1)*width, workspace.bottom, borderPaint);
		
	}	
	private void drawWorkspaceShader(Canvas canvas) {
		Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.workspace_shader);
		workspaceShader = Bitmap.createScaledBitmap(temp, (int)workspace.width(), (int)workspace.height(), false);
		Paint paint = new Paint();
		paint.setAlpha(0x77);
		paint.setAntiAlias(true);
		canvas.drawBitmap(getRoundedCornerBitmap(workspaceShader), workspace.left, workspace.top, paint);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(ContextCompat.getColor(getContext(), R.color.record_outline));
		paint.setStrokeWidth(getResources().getDimension(R.dimen.one_dp));
		canvas.drawRoundRect(workspace, workspaceCornerRadius, workspaceCornerRadius, paint);
	}
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(rectF, workspaceCornerRadius, workspaceCornerRadius, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
//			firstX = event.getX();
//			firstY = event.getY();
			for (int i=0; i<buttons.size(); i++) 
				buttons.get(i).setPressed(buttons.get(i).getContainer().contains(event.getX(), event.getY()));
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
//			if (event.getX()>firstX-30 && event.getX()<firstX+30 &&
//					event.getY()>firstY-30 && event.getY()<firstY+30) {
				for (int i=0; i<buttons.size(); i++) 
					buttons.get(i).setPressed(false);
				invalidate();
//			}
			break;
		}
		
		return super.onTouchEvent(event);
	}
	
}