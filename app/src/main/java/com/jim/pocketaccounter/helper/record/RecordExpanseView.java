package com.jim.pocketaccounter.helper.record;
import java.util.ArrayList;
import java.util.Calendar;
import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.RecordEditFragment;
import com.jim.pocketaccounter.RootCategoryEditFragment;
import com.jim.pocketaccounter.finance.CategoryAdapterForDialog;
import com.jim.pocketaccounter.finance.RootCategory;
import com.jim.pocketaccounter.helper.PocketAccounterGeneral;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@SuppressLint("DrawAllocation")
public class RecordExpanseView extends View implements 	GestureDetector.OnGestureListener {
	private final float workspaceCornerRadius, workspaceMargin;
	private Bitmap workspaceShader;
	private RectF workspace;
	private ArrayList<RecordButtonExpanse> buttons;
	private GestureDetectorCompat gestureDetector;
	private Calendar date;
	public RecordExpanseView(Context context, Calendar date) {
		super(context);
		this.date = (Calendar) date.clone();
		gestureDetector = new GestureDetectorCompat(getContext(),this);
		workspaceCornerRadius = getResources().getDimension(R.dimen.five_dp);
		workspaceMargin = getResources().getDimension(R.dimen.twenty_dp);
		initButtons();
		setClickable(true);
	}
	private void initButtons() {
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
			button = new RecordButtonExpanse(getContext(), type, date);
			if (PocketAccounter.financeManager.getExpanses().isEmpty())
				button.setCategory(null);
			else
				button.setCategory(PocketAccounter.financeManager.getExpanses().get(i));
			buttons.add(button);
		}
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
		borderPaint.setColor(ContextCompat.getColor(getContext(), R.color.belt_balanse));
		borderPaint.setStrokeWidth(getResources().getDimension(R.dimen.one_dp));
		for (int i=0; i<3; i++) {
			canvas.drawLine(workspace.left, workspace.top+(i+1)*width, workspace.right, workspace.top+(i+1)*width, borderPaint);
			canvas.drawLine(workspace.left+(i+1)*width, workspace.top, workspace.left+(i+1)*width, workspace.bottom, borderPaint);
		}
	}	
	private void drawWorkspaceShader(Canvas canvas) {
		Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.workspace_shader);
		workspaceShader = Bitmap.createScaledBitmap(temp, (int)workspace.width(), (int)workspace.height(), false);
		Paint paint = new Paint();
		paint.setAlpha(0x55);
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
		if (PocketAccounter.PRESSED) return false;
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
						RootCategory category;
						if(PocketAccounter.financeManager.getExpanses().isEmpty())
							category = null;
						else
							category = PocketAccounter.financeManager.getExpanses().get(position);
						if (category != null)
							((PocketAccounter)getContext()).replaceFragment(new RecordEditFragment(category, date, null, PocketAccounterGeneral.MAIN));
						else
							openChooseDialog(position);
				}
				}, 150);
				invalidate();
				break;
			}
		}
		PocketAccounter.PRESSED = true;
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		Vibrator vibr = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
		vibr.vibrate(60);
		float x = e.getX(), y = e.getY();
		int size = buttons.size();
		for (int i=0; i<size; i++) {
			if (buttons.get(i).getContainer().contains(x, y)) {
				buttons.get(i).setPressed(true);
				final int position = i;
				if (PocketAccounter.financeManager.getExpanses().get(position) == null) {
					for (int j=0; j<buttons.size(); j++)
						buttons.get(j).setPressed(false);
					invalidate();
					return;
				}
				postDelayed(new Runnable() {
					@Override
					public void run() {
						openChooseDialogLongPress(position);
					}
				}, 250);
				invalidate();
				break;
			}
		}
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}
	private void openChooseDialogLongPress(final int pos) {
		final Dialog dialog=new Dialog(getContext());
		View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_with_listview, null);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(dialogView);
		String change = getResources().getString(R.string.change);
		String clear = getResources().getString(R.string.clear);
		String[] items = new String[2];
		items[0] = change;
		items[1] = clear;
		ListView lvDialog = (ListView) dialogView.findViewById(R.id.lvDialog);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items);
		lvDialog.setAdapter(adapter);
		lvDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0)
					openCategoryChooseDialog(pos);
				else {
					PocketAccounter.financeManager.getExpanses().set(pos, null);
					initButtons();
					for (int i=0; i<buttons.size(); i++)
						buttons.get(i).setPressed(false);
					invalidate();
				}
				PocketAccounter.PRESSED = false;
				dialog.dismiss();
			}
		});
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				for (int i=0; i<buttons.size(); i++)
					buttons.get(i).setPressed(false);
				PocketAccounter.PRESSED = false;
				invalidate();
			}
		});
		dialog.show();
	}
	private void openChooseDialog(final int pos) {
		final Dialog dialog=new Dialog(getContext());
		View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_with_listview, null);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(dialogView);
		String add, create;
		add = getResources().getString(R.string.add);
		create = getResources().getString(R.string.create);
		String[] items = new String[2];
		items[0] = add;
		items[1] = create;
		ListView lvDialog = (ListView) dialogView.findViewById(R.id.lvDialog);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items);
		lvDialog.setAdapter(adapter);
		lvDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0) {
					boolean expanseCategoryFound = false;
					for (int i = 0; i < PocketAccounter.financeManager.getCategories().size(); i++) {
						if (PocketAccounter.financeManager.getCategories().get(i).getType() == PocketAccounterGeneral.EXPANCE) {
							expanseCategoryFound = true;
							break;
						}
					}
					if (expanseCategoryFound)
						openCategoryChooseDialog(pos);
					else
						((PocketAccounter)getContext()).replaceFragment(new RootCategoryEditFragment(null, PocketAccounterGeneral.EXPANSE_MODE, pos, date));
				}
				else
					((PocketAccounter)getContext()).replaceFragment(new RootCategoryEditFragment(null, PocketAccounterGeneral.EXPANSE_MODE, pos, date));
				PocketAccounter.PRESSED = false;
				dialog.dismiss();
			}
		});
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				for (int i=0; i<buttons.size(); i++)
					buttons.get(i).setPressed(false);
				invalidate();
				PocketAccounter.PRESSED = false;
			}
		});
		dialog.show();
	}
	private void openCategoryChooseDialog(final int pos) {
		final Dialog dialog=new Dialog(getContext());
		View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_with_listview, null);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(dialogView);
		final ArrayList<RootCategory> categories = new ArrayList<RootCategory>();
		for (int i=0; i<PocketAccounter.financeManager.getCategories().size(); i++) {
			if (PocketAccounter.financeManager.getCategories().get(i).getType() == PocketAccounterGeneral.EXPANCE)
				categories.add(PocketAccounter.financeManager.getCategories().get(i));
		}
		CategoryAdapterForDialog adapter = new CategoryAdapterForDialog(getContext(), categories);
		ListView lvDialog = (ListView) dialogView.findViewById(R.id.lvDialog);
		lvDialog.setAdapter(adapter);
		lvDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PocketAccounter.financeManager.getExpanses().set(pos, categories.get(position));
				initButtons();
				for (int i=0; i<buttons.size(); i++)
					buttons.get(i).setPressed(false);
				invalidate();
				PocketAccounter.PRESSED = false;
				dialog.dismiss();
			}
		});
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				for (int i=0; i<buttons.size(); i++)
					buttons.get(i).setPressed(false);
				invalidate();
				PocketAccounter.PRESSED = false;
			}
		});
		dialog.show();
	}
}