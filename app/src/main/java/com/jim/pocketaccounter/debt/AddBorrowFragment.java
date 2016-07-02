package com.jim.pocketaccounter.debt;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.finance.Account;
import com.jim.pocketaccounter.finance.Currency;
import com.jim.pocketaccounter.finance.FinanceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private final int PERMISSION_REQUEST_CONTACT = 5;
    private int PICK_CONTACT = 10;
    private final int PERMISSION_READ_STORAGE = 6;

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
            getDate.set(arg1, arg2, arg3);
            if (returnDate != null && getDate.compareTo(returnDate) > 0) {
                returnDate = getDate;
                PersonDataRepeat.setText(simpleDateFormat.format(returnDate.getTime()));
            }
            PersonDataGet.setText(simpleDateFormat.format(getDate.getTime()));
        }
    };
    private DatePickerDialog.OnDateSetListener returnDatesetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            returnDate = (Calendar) getDate.clone();
            returnDate.set(arg1, arg2, arg3);
            if (returnDate.compareTo(getDate) < 0) {
                returnDate = getDate;
            }
            PersonDataRepeat.setText(simpleDateFormat.format(returnDate.getTime()));
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.add_borrow_fragment_layout_mod, container, false);
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
        getDate = Calendar.getInstance();
        if (TYPE == DebtBorrow.DEBT) {
            PersonSumm.setHint(getResources().getString(R.string.enter_borrow_amoount));
            ((TextView) view.findViewById(R.id.summ_zayma)).setText(R.string.amount_borrow);
        }
        manager = PocketAccounter.financeManager;

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
                getContext(), R.layout.spiner_gravity_right, accaounts);

        ArrayAdapter<String> arrayValyuAdapter = new ArrayAdapter<String>(
                getContext(), R.layout.spiner_gravity_right, valyuts);

        arrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        PersonAccount.setAdapter(arrayAdapter);

        arrayValyuAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        PersonValyuta.setAdapter(arrayValyuAdapter);
        PersonDataGet.setText(simpleDateFormat.format(getDate.getTime()));
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

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calculate.isChecked()) {
                    PersonAccount.setVisibility(View.VISIBLE);
                } else {
                    PersonAccount.setVisibility(View.GONE);
                }
            }
        });

        ivToolbarMostRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PersonName.getText().toString().equals("")) {
                    PersonName.setError(getString(R.string.enter_name_error));
                } else {
                    if (PersonSumm.getText().toString().equals("")) {
                        PersonSumm.setError(getString(R.string.enter_amount_error));
                    } else {
                        if (PersonDataGet.getText().toString().matches("")) {
                            PersonDataGet.setError(getString(R.string.enter_takendate_error));
                        } else {
                            ArrayList<DebtBorrow> list = manager.getDebtBorrows();
                            Currency currency = manager.getCurrencies().get(PersonValyuta.getSelectedItemPosition());
                            ArrayList<Recking> reckings = new ArrayList<Recking>();
                            Account account = manager.getAccounts().get(PersonAccount.getSelectedItemPosition());
                            File file = null;
                            if (!photoPath.matches("")) {
                                try {
                                    Integer.parseInt(photoPath);
                                } catch (Exception e) {
                                    Bitmap bitmap = decodeFile(new File(photoPath));
                                    Bitmap C;

                                    if (bitmap.getWidth() >= bitmap.getHeight()) {
                                        C = Bitmap.createBitmap(
                                                bitmap,
                                                bitmap.getWidth() / 2 - bitmap.getHeight() / 2,
                                                0,
                                                bitmap.getHeight(),
                                                bitmap.getHeight()
                                        );
                                    } else {
                                        C = Bitmap.createBitmap(
                                                bitmap,
                                                0,
                                                bitmap.getHeight() / 2 - bitmap.getWidth() / 2,
                                                bitmap.getWidth(),
                                                bitmap.getWidth()
                                        );
                                    }
                                    try {
                                        file = new File(getContext().getFilesDir(), Uri.parse(photoPath).getLastPathSegment());
                                        FileOutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
                                        C.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                                        outputStream.flush();
                                        outputStream.close();
                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }

                            DebtBorrow debtBorrow = new DebtBorrow(new Person(PersonName.getText().toString(),
                                    PersonNumber.getText().toString(), file != null ? file.getAbsolutePath() : photoPath=="" ? "" : photoPath),
                                    getDate,
                                    returnDate,
                                    "borrow_" + UUID.randomUUID().toString(),
                                    account,
                                    currency,
                                    Double.parseDouble(PersonSumm.getText().toString()),
                                    TYPE, calculate.isChecked()
                            );
                            if (!firstPay.getText().toString().isEmpty()) {
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                                reckings.add(new Recking(
                                        dateFormat.format(Calendar.getInstance().getTime()),
                                        Double.parseDouble(firstPay.getText().toString()), debtBorrow.getId(),
                                        debtBorrow.getAccount().getId(), ""));
                            }
                            debtBorrow.setReckings(reckings);
                            ivToolbarMostRight.setVisibility(View.INVISIBLE);
                            list.add(0, debtBorrow);
                            manager.setDebtBorrows(list);
                            DebtBorrowFragment fragment = new DebtBorrowFragment();
                            Bundle bundle = new Bundle();
                            bundle.putInt("pos", debtBorrow.getType());
                            fragment.setArguments(bundle);
                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            ((PocketAccounter) getContext()).replaceFragment(fragment, PockerTag.DEBTS);
                        }
                    }
                }
            }
        });

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForContactPermission();
            }
        });

        imageView.setImageResource(R.drawable.no_photo);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permission = ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(((PocketAccounter) getContext()),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Permission to access the SD-CARD is required for this app to Download PDF.")
                                .setTitle("Permission required");

                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ActivityCompat.requestPermissions((PocketAccounter) getContext(),
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSION_READ_STORAGE);
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        ActivityCompat.requestPermissions((PocketAccounter) getContext(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSION_READ_STORAGE);
                    }
                } else {
                    getPhoto();
                }
            }
        });

        return view;
    }

    private Bitmap decodeFile(File f) {
        try {
//            Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
//            The new size we want to scale to
            final int REQUIRED_SIZE = 128;
//            Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
//             Get the URI and query the content provider for the phone number
            Uri contactUri = data.getData();
            String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.PHOTO_ID
            };
            Cursor cursor = getContext().getContentResolver().query(contactUri, projection,
                    null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                int photoIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID);
                String number = cursor.getString(numberIndex);
                String name = cursor.getString(nameIndex);
                photoPath = String.valueOf(cursor.getInt(photoIndex));
                if (queryContactImage(cursor.getInt(photoIndex)) != null)
                    imageView.setImageBitmap(queryContactImage(cursor.getInt(photoIndex)));
                else
                    imageView.setImageResource(R.drawable.no_photo);
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
            imageView.setImageBitmap(decodeFile(new File(photoPath)));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Bitmap queryContactImage(int imageDataRow) {
        Cursor c = getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{
                ContactsContract.CommonDataKinds.Photo.PHOTO
        }, ContactsContract.Data._ID + "=?", new String[]{
                Integer.toString(imageDataRow)
        }, null);
        byte[] imageBytes = null;
        if (c != null) {
            if (c.moveToFirst()) {
                imageBytes = c.getBlob(0);
            }
            c.close();
        }
        if (imageBytes != null) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return null;
        }
    }

    public void askForContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Contacts access needed");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setMessage("please confirm Contacts access");//TODO put real question
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            requestPermissions(
                                    new String[]
                                            {Manifest.permission.READ_CONTACTS}
                                    , PERMISSION_REQUEST_CONTACT);
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_CONTACTS},
                            PERMISSION_REQUEST_CONTACT);
                }
            } else {
                getContact();
            }
        } else {
            getContact();
        }
    }

    private void getContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(intent, PICK_CONTACT);
    }

    private void getPhoto() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CONTACT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContact();
                } else {
                }
                return;
            }
            case PERMISSION_READ_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPhoto();
                }
                break;
            }
        }
    }
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {}
    public void onNothingSelected(AdapterView<?> parent) {}
}