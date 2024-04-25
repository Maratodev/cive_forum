package com.maratodev.civeforum;

import android.util.Patterns;
import android.view.View;
import android.view.View$OnClickListener;
import android.widget.Button;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;
import android.content.Context;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import android.app.ProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity
{
    private EditText email;
    private FirebaseAuth mAuth;
    private EditText name;
    private EditText password;
    private ProgressDialog progressDialog;
    
    private void registerUser(final String s, final String s2, final String s3) {
        this.progressDialog.show();
        this.mAuth.createUserWithEmailAndPassword(s, s2).addOnCompleteListener((OnCompleteListener)new OnCompleteListener<AuthResult>(this, s3) {
            final RegistrationActivity this$0;
            final String val$uname;
            
            public void onComplete(final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    this.this$0.progressDialog.dismiss();
                    final FirebaseUser currentUser = this.this$0.mAuth.getCurrentUser();
                    final String email = currentUser.getEmail();
                    final String uid = currentUser.getUid();
                    final HashMap value = new HashMap();
                    value.put((Object)"email", (Object)email);
                    value.put((Object)"uid", (Object)uid);
                    value.put((Object)"name", (Object)this.val$uname);
                    value.put((Object)"image", (Object)"");
                    FirebaseDatabase.getInstance().getReference("Users").child(uid).setValue((Object)value);
                    Toast.makeText((Context)this.this$0, (CharSequence)("Registered User " + currentUser.getEmail()), 1).show();
                    final Intent intent = new Intent((Context)this.this$0, (Class)MainActivity.class);
                    intent.addFlags(268468224);
                    this.this$0.startActivity(intent);
                    this.this$0.finish();
                }
                else {
                    this.this$0.progressDialog.dismiss();
                    Toast.makeText((Context)this.this$0, (CharSequence)"Error", 1).show();
                }
            }
        }).addOnFailureListener((OnFailureListener)new OnFailureListener(this) {
            final RegistrationActivity this$0;
            
            public void onFailure(final Exception ex) {
                this.this$0.progressDialog.dismiss();
                Toast.makeText((Context)this.this$0, (CharSequence)"Error Occurred", 1).show();
            }
        });
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_registration);
        this.email = (EditText)this.findViewById(R.id.register_email);
        this.name = (EditText)this.findViewById(R.id.register_name);
        this.password = (EditText)this.findViewById(R.id.register_password);
        final Button button = (Button)this.findViewById(R.id.register_button);
        this.mAuth = FirebaseAuth.getInstance();
        (this.progressDialog = new ProgressDialog((Context)this)).setMessage((CharSequence)"Register");
        button.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final RegistrationActivity this$0;
            
            public void onClick(final View view) {
                final String trim = this.this$0.email.getText().toString().trim();
                final String trim2 = this.this$0.name.getText().toString().trim();
                final String trim3 = this.this$0.password.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher((CharSequence)trim).matches()) {
                    this.this$0.email.setError((CharSequence)"Invalid Email");
                    this.this$0.email.setFocusable(true);
                }
                else if (trim3.length() < 6) {
                    this.this$0.password.setError((CharSequence)"Length Must be greater than 6 character");
                    this.this$0.password.setFocusable(true);
                }
                else {
                    this.this$0.registerUser(trim, trim3, trim2);
                }
            }
        });
    }
    
    public boolean onSupportNavigateUp() {
        this.onBackPressed();
        return super.onSupportNavigateUp();
    }
}
