package com.jim.pocketaccounter.syncbase;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.jim.pocketaccounter.PocketAccounter;
import com.jim.pocketaccounter.R;
import com.jim.pocketaccounter.credit.CreditDetials;
import com.jim.pocketaccounter.credit.ReckingCredit;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by DEV on 16.06.2016.
 */

public class SignInGoogleMoneyHold {
    FirebaseAuth.AuthStateListener mAuthListener;
    GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    SyncBase mySync;
    String TAG="MainAct";
    Context context;
    UpdateSucsess succsesEvent;
    SharedPreferences spref;
    SharedPreferences.Editor ed  ;

    public interface UpdateSucsess{
        public void updateToSucsess();
        public void updateToFailed();

    }
    public SignInGoogleMoneyHold(Context con,UpdateSucsess succsesEvent){
        context=con;
        spref=con.getSharedPreferences("infoFirst",con.MODE_PRIVATE);
        ed=spref.edit();

        GoogleApiClient.OnConnectionFailedListener conFaild=new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Log.d(TAG,connectionResult.getErrorMessage());
            }
        };
        this.succsesEvent=succsesEvent;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage((PocketAccounter)context /* FragmentActivity */, conFaild /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {

                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }};
        mAuth=FirebaseAuth.getInstance();
    }
    public static final String DATA_BASE="PocketAccounterDatabase.db";
    public static final int RC_SIGN_IN=10011;

    public void regitUser(){
   Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        ((PocketAccounter)context).startActivityForResult(signInIntent, RC_SIGN_IN);

    }
    public void regitRequstGet(Intent data){

        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        Log.d(TAG,data.toString());

        if (result.isSuccess()) {

            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            Log.d(TAG,account.getDisplayName());
           if(account.getPhotoUrl()!=null)
            Log.d(TAG,account.getPhotoUrl().toString());
            Log.d(TAG,account.getEmail());
            firebaseAuthWithGoogle(account);
            ed.putBoolean("FIRSTSYNC",false);
            ed.commit();
        } else {
            Log.d("FIREBSEE", "Failed GOOGLE");
            succsesEvent.updateToFailed();
        }
    }
    public void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //  ((TextView)findViewById(R.id.userStatus)).setText("SIGN OUTED");
                        Log.d(TAG, "Revoke");
                    }
                });
    }



    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        showProgressDialog();
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(((PocketAccounter)context ), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            succsesEvent.updateToFailed();
                        }
                        else {
                            succsesEvent.updateToSucsess();
                        }
                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }



    private ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(context.getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
    public void openDialog() {
        final Dialog dialog = new Dialog(context);
        final View dialogView = ((PocketAccounter) context).getLayoutInflater().inflate(R.layout.first_login_info, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        final SignInButton singinbut = (SignInButton) dialogView.findViewById(R.id.signg);

        ImageView cancel = (ImageView) dialogView.findViewById(R.id.ivInfoDebtBorrowCancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        singinbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                regitUser();
                dialog.dismiss();

            }
        });
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        dialog.getWindow().setLayout(7 * width / 8, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }
    //download
    /*
          final FirebaseUser userik=FirebaseAuth.getInstance().getCurrentUser();

                        showProgressDialog();
                        mySync.meta_Message(userik.getUid(), new SyncBase.ChangeStateLisMETA() {
                            @Override
                            public void onSuccses(final long inFormat) {

                                Date datee=new Date();
                                datee.setTime(inFormat);
                               // ((TextView)findViewById(R.id.userStatus)).setText("META DATA GET");
                                Log.d(TAG,"META DATA GET");
                               // hideProgressDialog();
                                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(PocketAccounter.this);
                                builder.setMessage("WONT YOU SYNC DATA FROM DATE :" + (new SimpleDateFormat("dd-MM-yyyy kk:mm")).format(datee))
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                             //   showProgressDialog();
                                                mySync.downloadLast(userik.getUid(), new SyncBase.ChangeStateLis() {
                                                    @Override
                                                    public void onSuccses() {
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                              Log.d(TAG,"CHANGED SUCCESFULL!!! )))");
//stuff that updates ui
                                                                hideProgressDialog();

                                                           //     ((TextView)findViewById(R.id.userStatus)).setText("ROLLBACKED ");

                                                        //        readFromDatabase();
                                                            }
                                                        });

                                                    }

                                                    @Override
                                                    public void onFailed(Exception e) {
                                                    //    ((TextView)findViewById(R.id.userStatus)).setText("ROLLBACKED FAILED");

                                                    }
                                                });
                                            }
                                        }) .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                                builder.create().show();

                                Log.d("SyncListen",(new SimpleDateFormat("dd-MM-yyyy kk:mm")).format(datee));



                            }

                            @Override
                            public void onFailed(Exception e) {

                            }
                        });

     */


}
