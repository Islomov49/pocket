package com.jim.pocketaccounter.debt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.finance.FinanceManager;
import com.sw926.imagefileselector.ImageFileSelector;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by user on 6/4/2016.
 */

public class AddDebtFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Button cancelBtn;
    private Button okBtn;
    private Button contactBtn;
    private CircleImageView imageView;
    private EditText PersonName;
    private EditText PersonNumber;
    private EditText PersonDataGet;
    private EditText PersonDataRepeat;
    private EditText PersonSumm;
    private Spinner PersonValyuta;
    private Spinner PersonAccount;
    private String photoPath = "";
    private ImageFileSelector mImageFileSelector;
    private Calendar getDate;
    private Calendar returnDate;
    private static final int REQUEST_SELECT_CONTACT = 1;

    private DatePickerDialog.OnDateSetListener getDatesetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            arg2 = arg2 + 1;
            PersonDataGet.setText(arg3 + "-" + arg2 + "-" + arg1);
            getDate = Calendar.getInstance();
            getDate.set(arg1, arg2, arg3);
        }
    };
    private DatePickerDialog.OnDateSetListener returnDatesetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            arg2 = arg2 + 1;
            PersonDataRepeat.setText(arg3 + "-" + arg2 + "-" + arg1);
            returnDate = Calendar.getInstance();
            returnDate.set(arg1, arg2, arg3);
        }
    };
    private FinanceManager manager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_borrow_fragment_layout, container, false);
        cancelBtn = (Button) view.findViewById(R.id.btBorrowAddPopupCancel);
        okBtn = (Button) view.findViewById(R.id.btBorrowAddPopupOk);
        contactBtn = (Button) view.findViewById(R.id.btBorrowAddPopupContact);
        imageView = (CircleImageView) view.findViewById(R.id.ivBorrowAddPopup);
        PersonName = (EditText) view.findViewById(R.id.etBorrowAddPopupName);
        PersonNumber = (EditText) view.findViewById(R.id.etBorrowAddPopupNumber);
        PersonDataGet = (EditText) view.findViewById(R.id.etBorrowAddPopupDataGet);
        PersonDataRepeat = (EditText) view.findViewById(R.id.etBorrowAddPopupDataRepeat);
        PersonSumm = (EditText) view.findViewById(R.id.etBorrowAddPopupSumm);
        PersonValyuta = (Spinner) view.findViewById(R.id.spBorrowAddPopupValyuta);
        PersonAccount = (Spinner) view.findViewById(R.id.spBorrowAddPopupAccount);
        manager = new FinanceManager(getContext());

        PersonAccount.setOnItemSelectedListener(this);
        PersonValyuta.setOnItemSelectedListener(this);
        String [] accaounts = new String[manager.getAccounts().size()];
        for (int i = 0; i < accaounts.length; i++) {
            accaounts[i] = manager.getAccounts().get(i).getName();
        }
        String [] valyuts = new String[manager.getCurrencies().size()];
        for (int i = 0; i < valyuts.length; i++) {
            valyuts[i] = manager.getCurrencies().get(i).getAbbr();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, accaounts);

        ArrayAdapter<String> arrayValyuAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, valyuts);

        arrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        PersonAccount.setAdapter(arrayAdapter);

        arrayValyuAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        PersonValyuta.setAdapter(arrayValyuAdapter);

        mImageFileSelector = new ImageFileSelector(getContext());
        mImageFileSelector.setOutPutImageSize(2000, 2000);
        mImageFileSelector.setQuality(90);

        PersonDataGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calender = Calendar.getInstance();
                Dialog mDialog = new DatePickerDialog(getContext(),
                        getDatesetListener, calender.get(Calendar.YEAR),
                        calender.get(Calendar.MONTH), calender
                        .get(Calendar.DAY_OF_MONTH));
                mDialog.show();
            }
        });

        PersonDataRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calender = Calendar.getInstance();
                Dialog mDialog = new DatePickerDialog(getContext(),
                        returnDatesetListener, calender.get(Calendar.YEAR),
                        calender.get(Calendar.MONTH), calender
                        .get(Calendar.DAY_OF_MONTH));
                mDialog.show();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PocketAccounter) getContext()).replaceFragment(new DebtBorrowFragment());
            }
        });
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PersonName.getText().toString().equals("")) {
                    PersonName.setHintTextColor(Color.RED);
                } else {
                    if (PersonSumm.getText().toString().equals("")) {
                        PersonName.setHintTextColor(Color.RED);
                    } else {
                        //                        manager.getDebtBorrows().add(new DebtBorrow(new Person(PersonName.getText().toString(), PersonNumber.getText().toString(), photoPath),
//                                getDate,
//                                returnDate,
//                                false,
//                                manager.getAccounts().get(PersonAccount.getSelectedItemPosition()),
//                                manager.getCurrencies().get(PersonValyuta.getSelectedItemPosition()),
//                                Integer.valueOf(PersonSumm.getText().toString()),
//                                false));
                    }
                }
            }
        });

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_SELECT_CONTACT);
                }
            }
        });

        mImageFileSelector.setCallback(new ImageFileSelector.Callback() {
            @Override
            public void onSuccess(final String file) {
                photoPath = file;
                Toast.makeText(getContext(), "daryo", Toast.LENGTH_LONG).show();
                Glide
                        .with(getContext())
                        .load(file)
                        .centerCrop()
                        .crossFade()
                        .into(imageView);
            }

            public void onError() {
                Toast.makeText(getContext(), "daryo", Toast.LENGTH_LONG).show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageFileSelector.selectImage((PocketAccounter) getContext());
//                mImageFileSelector.selectImage(AddBorrowFragment.this);
                Toast.makeText(getContext(), "    ", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mImageFileSelector.onSaveInstanceState(outState);
    }

//    @Override
//    public void onViewStateRestored(Bundle savedInstanceState) {
//        super.onViewStateRestored(savedInstanceState);
//        mImageFileSelector.onRestoreInstanceState(savedInstanceState);
//    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mImageFileSelector.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
            // Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI
            };
            Cursor cursor = getContext().getContentResolver().query(contactUri, projection,
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int photoIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI);
                String number = cursor.getString(numberIndex);
                String name = cursor.getString(nameIndex);
                photoPath = cursor.getString(photoIndex);
                Toast.makeText(getContext(), number + "\n" + name, Toast.LENGTH_SHORT).show();
                if (photoPath != null) {
                    Glide
                            .with(this)
                            .load(name)
                            .centerCrop()
                            .crossFade()
                            .into(imageView);
                }
                PersonName.setText(name);
                PersonNumber.setText(number);
            }
        }
        else {
            mImageFileSelector.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
    @Override
    public void onNothingSelected(AdapterView<?> parent) {}
}