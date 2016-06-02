package com.jim.pocketaccounter;

import java.text.DecimalFormat;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecordEditFragment extends Fragment implements OnClickListener {
	private RelativeLayout rlZero, rlOne, rlTwo, rlThree, rlFour, rlFive, rlSix, rlSeven, rlEight, rlNine, rlDot, rlEqualSign,
						   rlPlusSign, rlMinusSign, rlMultipleSign, rlDivideSign, rlClearSign, rlBackspaceSign, rlCategory, rlSubCategory;
	private TextView tvRecordEditDisplay, tvRecordEditCurrency;
	private final double MAX_SIGNS = 22;
	private DecimalFormat format = new DecimalFormat("0.00##");
    float mValueOne , mValueTwo ;
    boolean mAddition , mSubtract ,mMultiplication ,mDivision ;
    private String text;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.record_edit, container, false);
		rlZero = (RelativeLayout) rootView.findViewById(R.id.rlZero);
		rlZero.setOnClickListener(this);
		rlOne = (RelativeLayout) rootView.findViewById(R.id.rlOne);
		rlOne.setOnClickListener(this);
		rlTwo = (RelativeLayout) rootView.findViewById(R.id.rlTwo);
		rlTwo.setOnClickListener(this);
		rlThree = (RelativeLayout) rootView.findViewById(R.id.rlThree);
		rlThree.setOnClickListener(this);
		rlFour = (RelativeLayout) rootView.findViewById(R.id.rlFour);
		rlFour.setOnClickListener(this);
		rlFive = (RelativeLayout) rootView.findViewById(R.id.rlFive);
		rlFive.setOnClickListener(this);
		rlSix = (RelativeLayout) rootView.findViewById(R.id.rlSix);
		rlSix.setOnClickListener(this);
		rlSeven = (RelativeLayout) rootView.findViewById(R.id.rlSeven);
		rlSeven.setOnClickListener(this);
		rlEight = (RelativeLayout) rootView.findViewById(R.id.rlEight);
		rlEight.setOnClickListener(this);
		rlNine = (RelativeLayout) rootView.findViewById(R.id.rlNine);
		rlNine.setOnClickListener(this);
		rlDot = (RelativeLayout) rootView.findViewById(R.id.rlDot);
		rlDot.setOnClickListener(this);
		rlEqualSign = (RelativeLayout) rootView.findViewById(R.id.rlEqualSign);
		rlEqualSign.setOnClickListener(this);
		rlPlusSign = (RelativeLayout) rootView.findViewById(R.id.rlPlusSign);
		rlPlusSign.setOnClickListener(this);
		rlMinusSign = (RelativeLayout) rootView.findViewById(R.id.rlMinusSign);
		rlMinusSign.setOnClickListener(this);
		rlMultipleSign = (RelativeLayout) rootView.findViewById(R.id.rlMultipleSign);
		rlMultipleSign.setOnClickListener(this);
		rlDivideSign = (RelativeLayout) rootView.findViewById(R.id.rlDivideSign);
		rlDivideSign.setOnClickListener(this);
		rlClearSign = (RelativeLayout) rootView.findViewById(R.id.rlCancelSign);
		rlClearSign.setOnClickListener(this);
		rlBackspaceSign = (RelativeLayout) rootView.findViewById(R.id.rlBackspaceSign);
		rlBackspaceSign.setOnClickListener(this);
		rlCategory = (RelativeLayout) rootView.findViewById(R.id.rlCategory);
		rlCategory.setOnClickListener(this);
		rlSubCategory = (RelativeLayout) rootView.findViewById(R.id.rlSubcategory);
		rlSubCategory.setOnClickListener(this);
		tvRecordEditDisplay = (TextView) rootView.findViewById(R.id.tvRecordEditDisplay);
		tvRecordEditCurrency = (TextView) rootView.findViewById(R.id.tvRecordEditCurrency);
		tvRecordEditCurrency.setOnClickListener(this);
		return rootView;
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()) {
		case R.id.rlZero:
			if (Double.parseDouble(tvRecordEditDisplay.getText().toString()) == 0.0)
				tvRecordEditDisplay.setText("");
			tvRecordEditDisplay.append("0");
			text = tvRecordEditDisplay.getText().toString();
			break;
		case R.id.rlOne:
			if (Double.parseDouble(tvRecordEditDisplay.getText().toString()) == 0.0)
				tvRecordEditDisplay.setText("");
			tvRecordEditDisplay.append("1");
			text = tvRecordEditDisplay.getText().toString();
			break;
		case R.id.rlTwo:
			if (Double.parseDouble(tvRecordEditDisplay.getText().toString()) == 0.0)
				tvRecordEditDisplay.setText("");
			tvRecordEditDisplay.append("2");
			text = tvRecordEditDisplay.getText().toString();
			break;
		case R.id.rlThree:
			if (Double.parseDouble(tvRecordEditDisplay.getText().toString()) == 0.0)
				tvRecordEditDisplay.setText("");
			tvRecordEditDisplay.append("3");
			text = tvRecordEditDisplay.getText().toString();
			break;
		case R.id.rlFour:
			if (Double.parseDouble(tvRecordEditDisplay.getText().toString()) == 0.0)
				tvRecordEditDisplay.setText("");
			tvRecordEditDisplay.append("4");
			text = tvRecordEditDisplay.getText().toString();
			break;
		case R.id.rlFive:
			if (Double.parseDouble(tvRecordEditDisplay.getText().toString()) == 0.0)
				tvRecordEditDisplay.setText("");
			tvRecordEditDisplay.append("5");
			text = tvRecordEditDisplay.getText().toString();
			break;
		case R.id.rlSix:
			if (Double.parseDouble(tvRecordEditDisplay.getText().toString()) == 0.0)
				tvRecordEditDisplay.setText("");
			tvRecordEditDisplay.append("6");
			text = tvRecordEditDisplay.getText().toString();
			break;
		case R.id.rlSeven:
			if (Double.parseDouble(tvRecordEditDisplay.getText().toString()) == 0.0)
				tvRecordEditDisplay.setText("");
			tvRecordEditDisplay.append("7");
			text = tvRecordEditDisplay.getText().toString();
			break;
		case R.id.rlEight:
			if (Double.parseDouble(tvRecordEditDisplay.getText().toString()) == 0.0)
				tvRecordEditDisplay.setText("");
			tvRecordEditDisplay.append("8");
			text = tvRecordEditDisplay.getText().toString();
			break;
		case R.id.rlNine:
			if (Double.parseDouble(tvRecordEditDisplay.getText().toString()) == 0.0)
				tvRecordEditDisplay.setText("");
			tvRecordEditDisplay.append("9");
			text = tvRecordEditDisplay.getText().toString();
			break;
		case R.id.rlDot:
			if (Double.parseDouble(tvRecordEditDisplay.getText().toString()) == 0.0)
				tvRecordEditDisplay.setText("");
			if (tvRecordEditDisplay.getText().toString().contains("."))
				return;
			tvRecordEditDisplay.append(".");
			text = tvRecordEditDisplay.getText().toString();
			break;
		case R.id.rlPlusSign:
            mValueOne = Float.parseFloat(text);
            mAddition = true;
            text = "";
			break;
		case R.id.rlMinusSign:
			mValueOne = Float.parseFloat(text);
            mSubtract = true ;
            text = "";
			break;
		case R.id.rlMultipleSign:
			mValueOne = Float.parseFloat(text);
            mMultiplication = true ;
            text = "";
			break;
		case R.id.rlDivideSign:
			mValueOne = Float.parseFloat(text);
            mDivision = true ;
            text = "";
			break;
		case R.id.rlCancelSign:
			tvRecordEditDisplay.setText("");
			text = null;
			break;
		case R.id.rlEqualSign:
			mValueTwo = Float.parseFloat(text);
            if (mAddition == true){
            	tvRecordEditDisplay.setText(mValueOne + mValueTwo +"");
                mAddition=false;
            }
            if (mSubtract == true){
            	tvRecordEditDisplay.setText(mValueOne - mValueTwo+"");
                mSubtract=false;
            }
            if (mMultiplication == true){
            	tvRecordEditDisplay.setText(mValueOne * mValueTwo+"");
                mMultiplication=false;
            }
            if (mDivision == true){
            	tvRecordEditDisplay.setText(mValueOne / mValueTwo+"");
                mDivision=false;
            }
			break;
		}
	}
}