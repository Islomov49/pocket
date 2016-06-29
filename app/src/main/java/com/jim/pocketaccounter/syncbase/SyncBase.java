package com.jim.pocketaccounter.syncbase;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.SettingsActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by DEV on 10.06.2016.
 */

public class SyncBase {
    private static String DB_PATH;
    private static String DB_NAME;
    private static String PATH_FOR_INPUT;
    private static String META_KEY="CreatAT";
    StorageReference refStorage;
    Context context;
    ChangeStateLis eventer;
    void SyncBase(){

    }
    public SyncBase(StorageReference refStorage, Context context,String databsename) {
        this.refStorage = refStorage;
        this.context = context;
        String packageName = context.getPackageName();
        DB_PATH = String.format("//data//data//%s//databases//", packageName);
        DB_NAME=databsename;
        PATH_FOR_INPUT=DB_PATH+DB_NAME;
        }
    public void uploadBASE(String auth_uid, final ChangeStateLis even){
        if(!isNetworkAvailable()){
            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setMessage(R.string.connection_faild)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
            builder.create().show();
            even.onFailed("NotNetworkAvailable");
            return;
        }
        try {
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("sqlite/db")
                    .setCustomMetadata(META_KEY, Long.toString(System.currentTimeMillis()))
                    .build();
            InputStream stream = new FileInputStream(new File(PATH_FOR_INPUT));
            refStorage.child(auth_uid + "/" + DB_NAME).putStream(stream, metadata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    even.onSuccses();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    even.onFailed(e.getMessage());

                }
            });
        }
        catch (Exception o){
            even.onFailed(o.getMessage());
        }



    }
   public boolean downloadLast(String auth_uid, final ChangeStateLis even){
       // TODO tekwirib kor dialog ustma ust bob qomayaptimi

       final ProgressDialog A1=new ProgressDialog(context);
       A1.setMessage(context.getString(R.string.please_wait));
       A1.show();

       try {

           File file = new File(PATH_FOR_INPUT);
          refStorage.child(auth_uid+"/"+DB_NAME).getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
              @Override
              public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                      even.onSuccses();
                  A1.dismiss();
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception e) {

              }
          });




       } catch (Exception e) {
           even.onFailed(e.getMessage());
           e.printStackTrace();
           A1.dismiss();
       }
       return false;
   }
    public void meta_Message(String auth_uid, final ChangeStateLisMETA even){
         refStorage.child(auth_uid+"/"+DB_NAME).getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
             @Override
             public void onSuccess(StorageMetadata storageMetadata) {
                 even.onSuccses(Long.parseLong(storageMetadata.getCustomMetadata(META_KEY)));
             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 even.onFailed(e);
             }
         });
    }

    public interface ChangeStateLis {
        void onSuccses();
        void onFailed(String e);


    }
    public interface ChangeStateLisMETA {
        void onSuccses(long inFormat);
        void onFailed(Exception e);


    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
