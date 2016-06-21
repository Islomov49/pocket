package com.jim.pocketaccounter.debt;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.finance.Currency;
import com.jim.pocketaccounter.finance.FinanceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by user on 6/4/2016.
 */

public class AddBorrowFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FrameLayout contactBtn;
    private CircleImageView imageView;
    private EditText PersonName;
    private EditText PersonNumber;
    private EditText PersonDataGet;
    private EditText PersonDataRepeat;
    private EditText PersonSumm;
    private Spinner PersonValyuta;
    private Spinner PersonAccount;
    private String photoPath = "";
    private Calendar getDate;
    private Calendar returnDate;
    private CheckBox calculate;
    private int TYPE = 0;
    private static final int REQUEST_SELECT_CONTACT = 2;
    private FinanceManager manager;
    private int RESULT_LOAD_IMAGE = 1;
    private ImageView ivToolbarMostRight;
    private EditText firstPay;

    public static Fragment getInstance(int type) {
        AddBorrowFragment fragment = new AddBorrowFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TYPE = getArguments().getInt("type", 0);
    }

    private DatePickerDialog.OnDateSetListener getDatesetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
//            arg2 = arg2 + 1;
            PersonDataGet.setText(arg3 + "." + (arg2 + 1) + "." + arg1);
            getDate = Calendar.getInstance();
            getDate.set(arg1, arg2, arg3);
        }
    };
    private DatePickerDialog.OnDateSetListener returnDatesetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
//            arg2 = arg2 + 1;
            PersonDataRepeat.setText(arg3 + "." + (arg2 + 1) +"." + arg1);
            returnDate = Calendar.getInstance();
            returnDate.set(arg1, arg2, arg3);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_borrow_fragment_layout_mod, container, false);
        contactBtn = (FrameLayout) view.findViewById(R.id.btBorrowAddPopupContact);
        imageView = (CircleImageView) view.findViewById(R.id.ivBorrowAddPopup);
        PersonName = (EditText) view.findViewById(R.id.etBorrowAddPopupName);
        PersonNumber = (EditText) view.findViewById(R.id.etBorrowAddPopupNumber);
        PersonDataGet = (EditText) view.findViewById(R.id.etBorrowAddPopupDataGet);
        PersonDataRepeat = (EditText) view.findViewById(R.id.etBorrowAddPopupDataRepeat);
        PersonSumm = (EditText) view.findViewById(R.id.etBorrowAddPopupSumm);
        firstPay = (EditText) view.findViewById(R.id.etDebtBorrowFirstPay);
        PersonValyuta = (Spinner) view.findViewById(R.id.spBorrowAddPopupValyuta);
        PersonAccount = (Spinner) view.findViewById(R.id.spBorrowAddPopupAccount);
        calculate = (CheckBox) view.findViewById(R.id.chbAddDebtBorrowCalculate);

        manager = PocketAccounter.financeManager;

        for (int i = 0; i < manager.getDebtBorrows().size(); i++) {
            Toast.makeText(getContext(), "" + i, Toast.LENGTH_SHORT).show();
        }

        PersonAccount.setOnItemSelectedListener(this);
        PersonValyuta.setOnItemSelectedListener(this);
        String[] accaounts = new String[manager.getAccounts().size()];
        for (int i = 0; i < accaounts.length; i++) {
            accaounts[i] = manager.getAccounts().get(i).getName();
        }
        String[] valyuts = new String[manager.getCurrencies().size()];
        for (int i = 0; i < valyuts.length; i++) {
            valyuts[i] = manager.getCurrencies().get(i).getAbbr();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item, accaounts);

        ArrayAdapter<String> arrayValyuAdapter = new ArrayAdapter<String>(
                getContext(), android.R.layout.simple_spinner_item, valyuts);

        arrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        PersonAccount.setAdapter(arrayAdapter);

        arrayValyuAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        PersonValyuta.setAdapter(arrayValyuAdapter);

        PersonDataGet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Calendar calender = Calendar.getInstance();
                    Dialog mDialog = new DatePickerDialog(getContext(),
                            getDatesetListener, calender.get(Calendar.YEAR),
                            calender.get(Calendar.MONTH), calender
                            .get(Calendar.DAY_OF_MONTH));
                    mDialog.show();
                }
                return true;
            }
        });

        PersonDataRepeat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Calendar calender = Calendar.getInstance();
                    Dialog mDialog = new DatePickerDialog(getContext(),
                            returnDatesetListener, calender.get(Calendar.YEAR),
                            calender.get(Calendar.MONTH), calender
                            .get(Calendar.DAY_OF_MONTH));
                    mDialog.show();
                    return true;
                }
                return false;
            }
        });

        ivToolbarMostRight = (ImageView) PocketAccounter.toolbar.findViewById(R.id.ivToolbarMostRight);
        ivToolbarMostRight.setImageResource(R.drawable.check_sign);
        ivToolbarMostRight.setVisibility(View.VISIBLE);

        ivToolbarMostRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PersonName.getText().toString().equals("")) {
                    PersonName.setHintTextColor(Color.RED);
                } else {
                    if (PersonSumm.getText().toString().equals("")) {
                        PersonName.setHintTextColor(Color.RED);
                    } else {
                        if (PersonDataGet.getText().toString().matches("")) {
                            PersonDataGet.setHintTextColor(Color.RED);
                        } else {
                            ArrayList<DebtBorrow> list = manager.getDebtBorrows();
                            Currency currency = manager.getCurrencies().get(PersonValyuta.getSelectedItemPosition());
                            DebtBorrow debtBorrow = new DebtBorrow(new Person(PersonName.getText().toString(), PersonNumber.getText().toString(), photoPath),
                                    getDate,
                                    returnDate,
                                    "borrow_" + UUID.randomUUID().toString(),
                                    PersonAccount.getSelectedItem().toString(),
                                    currency,
                                    Double.parseDouble(PersonSumm.getText().toString()),
                                    TYPE, calculate.isChecked()
                            );
                            if (!firstPay.getText().toString().isEmpty()) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                                ArrayList<Recking> reckings = new ArrayList<Recking>();
                                reckings.add(new Recking(
                                        dateFormat.format(Calendar.getInstance().getTime()),
                                        Double.parseDouble(firstPay.getText().toString()), debtBorrow.getId(),
                                        debtBorrow.getAccount(), ""));
                                  debtBorrow.setReckings(reckings);
                            }
                            list.add(debtBorrow);
//                            if (returnDate == null) {

//                                list.add(new DebtBorrow(new Person(PersonName.getText().toString(), PersonNumber.getText().toString(), photoPath),
//                                        getDate,
//                                        returnDate,
//                                        "borrow_" + UUID.randomUUID().toString(),
//                                        PersonAccount.getSelectedItem().toString(),
//                                        currency,
//                                        Double.parseDouble(PersonSumm.getText().toString()),
//                                        TYPE, calculate.isChecked()));
//
//                            } else {
//                                list.add(new DebtBorrow());
//                            }
                            ivToolbarMostRight.setVisibility(View.INVISIBLE);
                            manager.setDebtBorrows(list);
                            manager.saveDebtBorrows();
                            manager.loadDebtBorrows();
                            ((PocketAccounter) getContext()).replaceFragment(new DebtBorrowFragment(), PockerTag.DEBTS);
//                            getActivity().getSupportFragmentManager().popBackStack();
                        }
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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_CONTACT && resultCode == RESULT_OK) {
//             Get the URI and query the content provider for the phone number
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
                if (photoPath != null) {
                    imageView.setImageDrawable(Drawable.createFromPath(photoPath));
                }
                PersonName.setText(name);
                PersonNumber.setText(number);
            }
        }
        if (requestCode == RESULT_LOAD_IMAGE && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            photoPath = picturePath;
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}