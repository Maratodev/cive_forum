package com.maratodev.civeforum;

import android.text.TextUtils;
import android.view.View$OnClickListener;
import java.util.Iterator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.view.View;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnFailureListener;
import android.content.Intent;
import android.widget.Toast;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import com.google.firebase.storage.UploadTask$TaskSnapshot;
import com.google.android.gms.tasks.OnSuccessListener;
import java.io.ByteArrayOutputStream;
import com.google.firebase.storage.FirebaseStorage;
import android.widget.Button;
import android.app.ProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.EditText;
import com.google.firebase.database.DatabaseReference;
import androidx.fragment.app.Fragment;

public class AddTopicFragment extends Fragment
{
    DatabaseReference databaseReference;
    EditText des;
    String dp;
    String email;
    FirebaseAuth firebaseAuth;
    String name;
    ProgressDialog pd;
    EditText title;
    String uid;
    Button upload;
    
    private void uploadData(final String s, final String s2) {
        this.pd.setMessage((CharSequence)"Adding...");
        this.pd.show();
        final String value = String.valueOf(System.currentTimeMillis());
        FirebaseStorage.getInstance().getReference().child("Posts/post" + value).putBytes(new ByteArrayOutputStream().toByteArray()).addOnSuccessListener((OnSuccessListener)new OnSuccessListener<UploadTask$TaskSnapshot>(this, s, s2, value) {
            final AddTopicFragment this$0;
            final String val$description;
            final String val$timestamp;
            final String val$titl;
            
            public void onSuccess(final UploadTask$TaskSnapshot uploadTask$TaskSnapshot) {
                final Task downloadUrl = uploadTask$TaskSnapshot.getStorage().getDownloadUrl();
                while (!downloadUrl.isSuccessful()) {}
                if (downloadUrl.isSuccessful()) {
                    final HashMap value = new HashMap();
                    value.put((Object)"uid", (Object)this.this$0.uid);
                    value.put((Object)"uname", (Object)this.this$0.name);
                    value.put((Object)"uemail", (Object)this.this$0.email);
                    value.put((Object)"udp", (Object)this.this$0.dp);
                    value.put((Object)"title", (Object)this.val$titl);
                    value.put((Object)"description", (Object)this.val$description);
                    value.put((Object)"ptime", (Object)this.val$timestamp);
                    FirebaseDatabase.getInstance().getReference("Posts").child(this.val$timestamp).setValue((Object)value).addOnSuccessListener((OnSuccessListener)new OnSuccessListener<Void>(this) {
                        final AddTopicFragment$4 this$1;
                        
                        public void onSuccess(final Void void1) {
                            this.this$1.this$0.pd.dismiss();
                            Toast.makeText(this.this$1.this$0.getContext(), (CharSequence)"Added Successfully", 1).show();
                            this.this$1.this$0.title.setText((CharSequence)"");
                            this.this$1.this$0.des.setText((CharSequence)"");
                            this.this$1.this$0.startActivity(new Intent(this.this$1.this$0.getContext(), (Class)MainActivity.class));
                            this.this$1.this$0.getActivity().finish();
                        }
                    }).addOnFailureListener((OnFailureListener)new OnFailureListener(this) {
                        final AddTopicFragment$4 this$1;
                        
                        public void onFailure(final Exception ex) {
                            this.this$1.this$0.pd.dismiss();
                            Toast.makeText(this.this$1.this$0.getContext(), (CharSequence)"Failed to Add topic", 1).show();
                        }
                    });
                }
            }
        }).addOnFailureListener((OnFailureListener)new OnFailureListener(this) {
            final AddTopicFragment this$0;
            
            public void onFailure(final Exception ex) {
                this.this$0.pd.dismiss();
                Toast.makeText(this.this$0.getContext(), (CharSequence)"Failed", 1).show();
            }
        });
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.firebaseAuth = FirebaseAuth.getInstance();
        final View inflate = layoutInflater.inflate(R.layout.fragment_add_topic, viewGroup, false);
        this.title = (EditText)inflate.findViewById(R.id.ptitle);
        this.des = (EditText)inflate.findViewById(R.id.pdes);
        this.upload = (Button)inflate.findViewById(R.id.pupload);
        (this.pd = new ProgressDialog(this.getContext())).setCanceledOnTouchOutside(false);
        this.getActivity().getIntent();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        this.databaseReference = reference;
        reference.orderByChild("email").equalTo(this.email).addValueEventListener((ValueEventListener)new ValueEventListener(this) {
            final AddTopicFragment this$0;
            
            public void onCancelled(final DatabaseError databaseError) {
            }
            
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    dataSnapshot = (DataSnapshot)iterator.next();
                    this.this$0.name = dataSnapshot.child("name").getValue().toString();
                    this.this$0.email = "" + dataSnapshot.child("email").getValue();
                    this.this$0.dp = "" + dataSnapshot.child("image").getValue().toString();
                }
            }
        });
        this.upload.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final AddTopicFragment this$0;
            
            public void onClick(final View view) {
                final String string = "" + this.this$0.title.getText().toString().trim();
                final String string2 = "" + this.this$0.des.getText().toString().trim();
                if (TextUtils.isEmpty((CharSequence)string)) {
                    this.this$0.title.setError((CharSequence)"Title Cant be empty");
                }
                if (TextUtils.isEmpty((CharSequence)string2)) {
                    this.this$0.des.setError((CharSequence)"Description Cant be empty");
                }
                else {
                    this.this$0.uploadData(string, string2);
                }
            }
        });
        return inflate;
    }
}
