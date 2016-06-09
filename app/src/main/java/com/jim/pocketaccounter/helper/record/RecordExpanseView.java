package com.jim.pocketaccounter.helper.record;

import java.util.ArrayList;
import java.util.Calendar;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.RecordEditFragment;
import com.jim.pocketaccounter.finance.RootCategory;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;

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
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

@SuppressLint("DrawAllocation")
public class RecordExpanseView extends View implements 	GestureDetector.OnGestureListener {
	private final float workspaceCornerRadius, workspaceMargin;
	private Bitmap workspaceShader;
	private RectF workspace;
	private ArrayList<RecordButtonExpanse> buttons;
	private ArrayList<RootCategory> expanses;
	private GestureDetectorCompat gestureDetector;
	public RecordExpanseView(Context context) {
		super(context);
		gestureDetector = new GestureDetectorCompat(getContext(),this);
		workspaceCornerRadius = getResources().getDimension(R.dimen.five_dp);
		workspaceMargin = getResources().getDimension(R.dimen.twenty_dp);
		expanses = PocketAccounter.financeManager.getExpanses();
		buttons = new ArrayList<RecordButtonExpanse>();
		for (int i=0; i<PocketAccounterGeneral.EXPANCE_BUTTONS_COUNT; i++) {
			RecordButtonExpanse button = null;
			int type = 0;
			switch(i) {
			case 0:
				type = RecordButtonExpanse.TOP_LEFT;
				break;
			case 1:
			case 2:
				type = RecordButtonExpanse.TOP_SIMPLE;
				break;
			case 3:
				type = RecordButtonExpanse.TOP_RIGHT;
				break;
			case 4:
			case 8:
				type = RecordButtonExpanse.LEFT_SIMPLE;
				break;
			case 5:
			case 6:
			case 9:
			case 10:
				type = RecordButtonExpanse.SIMPLE;
				break;
			case 7:
			case 11:
				type = RecordButtonExpanse.RIGHT_SIMPLE;
				break;
			case 12:
				type = RecordButtonExpanse.LEFT_BOTTOM;
				break;
			case 13:
			case 14:
				type = RecordButtonExpanse.BOTTOM_SIMPLE;
				break;
			case 15:
				type = RecordButtonExpanse.BOTTOM_RIGHT;
				break;
			}
			button = new RecordButtonExpanse(getContext(), type);
			button.setCategory(expanses.get(i));
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
		height = workspace.height()/4;
		for (int i=0; i<PocketAccounterGeneral.EXPANCE_BUTTONS_COUNT; i++) {
			float left, top, right, bottom;
			left = workspace.left+(i%4)*width;
			top = workspace.top+((int)Math.floor(i/4)*height);
			right = workspace.left+(i%4+1)*width;
			bottom = workspace.top+((int)(Math.floor(i/4)+1)*height);
			buttons.get(i).setBounds(left, top, right, bottom, workspaceCornerRadius);
		}
		int buttonsCount = buttons.size();
		for (int i=0; i<buttonsCount; i++)
			buttons.get(i).drawButton(canvas);
		
		Paint borderPaint = new Paint();
		borderPaint.setColor(ContextCompat.getColor(getContext(), R.color.record_borders));
		borderPaint.setStrokeWidth(getResources().getDimension(R.dimen.two_dp));
		for (int i=0; i<3; i++) {
			canvas.drawLine(workspace.left, workspace.top+(i+1)*width, workspace.right, workspace.top+(i+1)*width, borderPaint);
			canvas.drawLine(workspace.left+(i+1)*width, workspace.top, workspace.left+(i+1)*width, workspace.bottom, borderPaint);
		}
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
		this.gestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		int size = buttons.size();
		float x = e.getX();
		float y = e.getY();
		for (int i=0; i<size; i++) {
			if (buttons.get(i).getContainer().contains(x, y)) {
				buttons.get(i).setPressed(true);
				final int position = i;
				postDelayed(new Runnable() {
					@Override
					public void run() {
						RootCategory category = PocketAccounter.financeManager.getExpanses().get(position);
						if (category != null)
							((PocketAccounter) getContext()).replaceFragment(new RecordEditFragment(category, Calendar.getInstance(), null));
						else
							((PocketAccounter) getContext()).replaceFragment(new RecordEditFragment(PocketAccounter.financeManager.getExpanses().get(position), Calendar.getInstance(), null));
					}
				}, 250);
				invalidate();
				break;
			}
		}
		return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		float x = e.getX(), y = e.getY();
		int size = buttons.size();
		for (int i=0; i<size; i++) {
			if (buttons.get(i).getContainer().contains(x, y)) {

				break;
			}
		}
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}
}