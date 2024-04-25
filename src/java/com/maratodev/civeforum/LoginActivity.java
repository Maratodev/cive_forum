package com.maratodev.civeforum;

import android.util.Patterns;
import android.view.View$OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.view.View;
import android.widget.LinearLayout;
import android.app.AlertDialog$Builder;
import android.content.Intent;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnFailureListener;
import android.content.Context;
import android.widget.Toast;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import android.app.ProgressDialog;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseUser;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity
{
    FirebaseUser currentUser;
    private EditText email;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuth;
    private EditText name;
    private EditText password;
    
    private void beginRecovery(final String s) {
        this.loadingBar.setMessage((CharSequence)"Sending Email...");
        this.loadingBar.setCanceledOnTouchOutside(false);
        this.loadingBar.show();
        this.mAuth.sendPasswordResetEmail(s).addOnCompleteListener((OnCompleteListener)new OnCompleteListener<Void>(this) {
            final LoginActivity this$0;
            
            public void onComplete(final Task<Void> task) {
                this.this$0.loadingBar.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText((Context)this.this$0, (CharSequence)"Done sent", 1).show();
                }
                else {
                    Toast.makeText((Context)this.this$0, (CharSequence)"Error Occurred", 1).show();
                }
            }
        }).addOnFailureListener((OnFailureListener)new OnFailureListener(this) {
            final LoginActivity this$0;
            
            public void onFailure(final Exception ex) {
                this.this$0.loadingBar.dismiss();
                Toast.makeText((Context)this.this$0, (CharSequence)"Error Failed", 1).show();
            }
        });
    }
    
    private void loginUser(final String s, final String s2) {
        this.loadingBar.setMessage((CharSequence)"Logging In...");
        this.loadingBar.show();
        this.mAuth.signInWithEmailAndPassword(s, s2).addOnCompleteListener((OnCompleteListener)new OnCompleteListener<AuthResult>(this) {
            final LoginActivity this$0;
            
            public void onComplete(final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    this.this$0.loadingBar.dismiss();
                    final FirebaseUser currentUser = this.this$0.mAuth.getCurrentUser();
                    if (((AuthResult)task.getResult()).getAdditionalUserInfo().isNewUser()) {
                        final String email = currentUser.getEmail();
                        final String uid = currentUser.getUid();
                        final HashMap value = new HashMap();
                        value.put((Object)"email", (Object)email);
                        value.put((Object)"uid", (Object)uid);
                        value.put((Object)"name", (Object)"");
                        value.put((Object)"image", (Object)"");
                        value.put((Object)"cover", (Object)"");
                        FirebaseDatabase.getInstance().getReference("Users").child(uid).setValue((Object)value);
                    }
                    final Intent intent = new Intent((Context)this.this$0, (Class)MainActivity.class);
                    intent.addFlags(268468224);
                    this.this$0.startActivity(intent);
                    this.this$0.finish();
                }
                else {
                    this.this$0.loadingBar.dismiss();
                    Toast.makeText((Context)this.this$0, (CharSequence)"Login Failed", 1).show();
                }
            }
        }).addOnFailureListener((OnFailureListener)new OnFailureListener(this) {
            final LoginActivity this$0;
            
            public void onFailure(final Exception ex) {
                this.this$0.loadingBar.dismiss();
                Toast.makeText((Context)this.this$0, (CharSequence)"Error Occurred", 1).show();
            }
        });
    }
    
    private void showRecoverPasswordDialog() {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        alertDialog$Builder.setTitle((CharSequence)"Recover Password");
        final LinearLayout view = new LinearLayout((Context)this);
        final EditText editText = new EditText((Context)this);
        editText.setHint((CharSequence)"Enter Email");
        editText.setMinEms(16);
        editText.setInputType(32);
        view.addView((View)editText);
        view.setPadding(10, 10, 10, 10);
        alertDialog$Builder.setView((View)view);
        alertDialog$Builder.setPositiveButton((CharSequence)"Recover", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener(this, editText) {
            final LoginActivity this$0;
            final EditText val$emailet;
            
            public void onClick(final DialogInterface dialogInterface, final int n) {
                this.this$0.beginRecovery(this.val$emailet.getText().toString().trim());
            }
        });
        alertDialog$Builder.setNegativeButton((CharSequence)"Cancel", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener(this) {
            final LoginActivity this$0;
            
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.dismiss();
            }
        });
        alertDialog$Builder.create().show();
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_login);
        this.email = (EditText)this.findViewById(R.id.login_email);
        this.password = (EditText)this.findViewById(R.id.login_password);
        final TextView textView = (TextView)this.findViewById(R.id.needs_new_account);
        final TextView textView2 = (TextView)this.findViewById(R.id.forgetp);
        this.mAuth = FirebaseAuth.getInstance();
        final Button button = (Button)this.findViewById(R.id.login_button);
        this.loadingBar = new ProgressDialog((Context)this);
        final FirebaseAuth instance = FirebaseAuth.getInstance();
        this.mAuth = instance;
        if (instance != null) {
            this.currentUser = instance.getCurrentUser();
        }
        button.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final LoginActivity this$0;
            
            public void onClick(final View view) {
                final String trim = this.this$0.email.getText().toString().trim();
                final String trim2 = this.this$0.password.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher((CharSequence)trim).matches()) {
                    this.this$0.email.setError((CharSequence)"Invalid Email");
                    this.this$0.email.setFocusable(true);
                }
                else {
                    this.this$0.loginUser(trim, trim2);
                }
            }
        });
        textView.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final LoginActivity this$0;
            
            public void onClick(final View view) {
                this.this$0.startActivity(new Intent((Context)this.this$0, (Class)RegistrationActivity.class));
            }
        });
        textView2.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final LoginActivity this$0;
            
            public void onClick(final View view) {
                this.this$0.showRecoverPasswordDialog();
            }
        });
    }
    
    public boolean onSupportNavigateUp() {
        this.onBackPressed();
        return super.onSupportNavigateUp();
    }
}
