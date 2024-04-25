package com.maratodev.civeforum;

import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import android.os.Bundle;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask$TaskSnapshot;
import com.google.firebase.auth.EmailAuthProvider;
import android.app.AlertDialog;
import android.view.View$OnClickListener;
import android.widget.Button;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import java.util.Iterator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.Map;
import java.util.HashMap;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.os.Parcelable;
import android.content.Intent;
import android.provider.MediaStore$Images$Media;
import android.content.ContentValues;
import android.content.Context;
import androidx.core.content.ContextCompat;
import com.google.firebase.storage.StorageReference;
import android.widget.ImageView;
import android.app.ProgressDialog;
import android.net.Uri;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity
{
    private static final int CAMERA_REQUEST = 100;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    private static final int STORAGE_REQUEST = 200;
    String[] cameraPermission;
    DatabaseReference databaseReference;
    TextView editname;
    TextView editpassword;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    Uri imageuri;
    TextView logout;
    ProgressDialog pd;
    String profileOrCoverPhoto;
    TextView profilepic;
    ImageView set;
    String[] storagePermission;
    StorageReference storageReference;
    String storagepath;
    String uid;
    
    public EditProfileActivity() {
        this.storagepath = "Users_Profile_Cover_image/";
    }
    
    private Boolean checkCameraPermission() {
        final int checkSelfPermission = ContextCompat.checkSelfPermission((Context)this, "android.permission.CAMERA");
        boolean b = true;
        final boolean b2 = checkSelfPermission == 0;
        final boolean b3 = ContextCompat.checkSelfPermission((Context)this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
        if (!b2 || !b3) {
            b = false;
        }
        return b;
    }
    
    private Boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission((Context)this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }
    
    private void pickFromCamera() {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("title", "Temp_pic");
        contentValues.put("description", "Temp Description");
        this.imageuri = this.getContentResolver().insert(MediaStore$Images$Media.EXTERNAL_CONTENT_URI, contentValues);
        final Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", (Parcelable)this.imageuri);
        this.startActivityForResult(intent, 400);
    }
    
    private void pickFromGallery() {
        final Intent intent = new Intent("android.intent.action.PICK");
        intent.setType("image/*");
        this.startActivityForResult(intent, 300);
    }
    
    private void requestCameraPermission() {
        this.requestPermissions(this.cameraPermission, 100);
    }
    
    private void requestStoragePermission() {
        this.requestPermissions(this.storagePermission, 200);
    }
    
    private void showImagePicDialog() {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        alertDialog$Builder.setTitle((CharSequence)"Pick Image From");
        alertDialog$Builder.setItems((CharSequence[])new String[] { "Camera", "Gallery" }, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener(this) {
            final EditProfileActivity this$0;
            
            public void onClick(final DialogInterface dialogInterface, final int n) {
                if (n == 0) {
                    if (!this.this$0.checkCameraPermission()) {
                        this.this$0.requestCameraPermission();
                    }
                    else {
                        this.this$0.pickFromCamera();
                    }
                }
                else if (n == 1) {
                    if (!this.this$0.checkStoragePermission()) {
                        this.this$0.requestStoragePermission();
                    }
                    else {
                        this.this$0.pickFromGallery();
                    }
                }
            }
        });
        alertDialog$Builder.create().show();
    }
    
    private void showNamephoneupdate(final String s) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        alertDialog$Builder.setTitle((CharSequence)("Update" + s));
        final LinearLayout view = new LinearLayout((Context)this);
        view.setOrientation(1);
        view.setPadding(10, 10, 10, 10);
        final EditText editText = new EditText((Context)this);
        editText.setHint((CharSequence)("Enter" + s));
        view.addView((View)editText);
        alertDialog$Builder.setView((View)view);
        alertDialog$Builder.setPositiveButton((CharSequence)"Update", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener(this, editText, s) {
            final EditProfileActivity this$0;
            final EditText val$editText;
            final String val$key;
            
            public void onClick(final DialogInterface dialogInterface, final int n) {
                final String trim = this.val$editText.getText().toString().trim();
                if (!TextUtils.isEmpty((CharSequence)trim)) {
                    this.this$0.pd.show();
                    final HashMap hashMap = new HashMap();
                    hashMap.put((Object)this.val$key, (Object)trim);
                    this.this$0.databaseReference.child(this.this$0.firebaseUser.getUid()).updateChildren((Map)hashMap).addOnSuccessListener((OnSuccessListener)new OnSuccessListener<Void>(this) {
                        final EditProfileActivity$13 this$1;
                        
                        public void onSuccess(final Void void1) {
                            this.this$1.this$0.pd.dismiss();
                            Toast.makeText((Context)this.this$1.this$0, (CharSequence)" updated ", 1).show();
                        }
                    }).addOnFailureListener((OnFailureListener)new OnFailureListener(this) {
                        final EditProfileActivity$13 this$1;
                        
                        public void onFailure(final Exception ex) {
                            this.this$1.this$0.pd.dismiss();
                            Toast.makeText((Context)this.this$1.this$0, (CharSequence)"Unable to update", 1).show();
                        }
                    });
                    if (this.val$key.equals((Object)"name")) {
                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                        reference.orderByChild("uid").equalTo(this.this$0.uid).addValueEventListener((ValueEventListener)new ValueEventListener(this, reference, trim) {
                            final EditProfileActivity$13 this$1;
                            final DatabaseReference val$databaser;
                            final String val$value;
                            
                            public void onCancelled(final DatabaseError databaseError) {
                            }
                            
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final Iterator iterator = dataSnapshot.getChildren().iterator();
                                while (iterator.hasNext()) {
                                    dataSnapshot = (DataSnapshot)iterator.next();
                                    this.val$databaser.getKey();
                                    dataSnapshot.getRef().child("uname").setValue((Object)this.val$value);
                                }
                            }
                        });
                    }
                }
                else {
                    Toast.makeText((Context)this.this$0, (CharSequence)"Unable to update", 1).show();
                }
            }
        });
        alertDialog$Builder.setNegativeButton((CharSequence)"Cancel", (DialogInterface$OnClickListener)new DialogInterface$OnClickListener(this) {
            final EditProfileActivity this$0;
            
            public void onClick(final DialogInterface dialogInterface, final int n) {
                this.this$0.pd.dismiss();
            }
        });
        alertDialog$Builder.create().show();
    }
    
    private void showPasswordChangeDailog() {
        final View inflate = LayoutInflater.from((Context)this).inflate(R.layout.dialog_update_profile, (ViewGroup)null);
        final EditText editText = (EditText)inflate.findViewById(R.id.oldpasslog);
        final EditText editText2 = (EditText)inflate.findViewById(R.id.newpasslog);
        final Button button = (Button)inflate.findViewById(R.id.updatepass);
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        alertDialog$Builder.setView(inflate);
        final AlertDialog create = alertDialog$Builder.create();
        create.show();
        button.setOnClickListener((View$OnClickListener)new View$OnClickListener(this, editText, editText2, create) {
            final EditProfileActivity this$0;
            final AlertDialog val$dialog;
            final EditText val$newpass;
            final EditText val$oldpass;
            
            public void onClick(final View view) {
                final String trim = this.val$oldpass.getText().toString().trim();
                final String trim2 = this.val$newpass.getText().toString().trim();
                if (TextUtils.isEmpty((CharSequence)trim)) {
                    Toast.makeText((Context)this.this$0, (CharSequence)"Current Password cant be empty", 1).show();
                    return;
                }
                if (TextUtils.isEmpty((CharSequence)trim2)) {
                    Toast.makeText((Context)this.this$0, (CharSequence)"New Password cant be empty", 1).show();
                    return;
                }
                this.val$dialog.dismiss();
                this.this$0.updatePassword(trim, trim2);
            }
        });
    }
    
    private void updatePassword(final String s, final String s2) {
        this.pd.show();
        final FirebaseUser currentUser = this.firebaseAuth.getCurrentUser();
        currentUser.reauthenticate(EmailAuthProvider.getCredential(currentUser.getEmail(), s)).addOnSuccessListener((OnSuccessListener)new OnSuccessListener<Void>(this, currentUser, s2) {
            final EditProfileActivity this$0;
            final String val$newp;
            final FirebaseUser val$user;
            
            public void onSuccess(final Void void1) {
                this.val$user.updatePassword(this.val$newp).addOnSuccessListener((OnSuccessListener)new OnSuccessListener<Void>(this) {
                    final EditProfileActivity$12 this$1;
                    
                    public void onSuccess(final Void void1) {
                        this.this$1.this$0.pd.dismiss();
                        Toast.makeText((Context)this.this$1.this$0, (CharSequence)"Changed Password", 1).show();
                    }
                }).addOnFailureListener((OnFailureListener)new OnFailureListener(this) {
                    final EditProfileActivity$12 this$1;
                    
                    public void onFailure(final Exception ex) {
                        this.this$1.this$0.pd.dismiss();
                        Toast.makeText((Context)this.this$1.this$0, (CharSequence)"Failed", 1).show();
                    }
                });
            }
        }).addOnFailureListener((OnFailureListener)new OnFailureListener(this) {
            final EditProfileActivity this$0;
            
            public void onFailure(final Exception ex) {
                this.this$0.pd.dismiss();
                Toast.makeText((Context)this.this$0, (CharSequence)"Failed", 1).show();
            }
        });
    }
    
    private void uploadProfileCoverPhoto(final Uri uri) {
        this.pd.show();
        this.storageReference.child(this.storagepath + "" + this.profileOrCoverPhoto + "_" + this.firebaseUser.getUid()).putFile(uri).addOnSuccessListener((OnSuccessListener)new OnSuccessListener<UploadTask$TaskSnapshot>(this) {
            final EditProfileActivity this$0;
            
            public void onSuccess(final UploadTask$TaskSnapshot uploadTask$TaskSnapshot) {
                final Task downloadUrl = uploadTask$TaskSnapshot.getStorage().getDownloadUrl();
                while (!downloadUrl.isSuccessful()) {}
                final Uri uri = (Uri)downloadUrl.getResult();
                if (downloadUrl.isSuccessful()) {
                    final HashMap hashMap = new HashMap();
                    hashMap.put((Object)this.this$0.profileOrCoverPhoto, (Object)uri.toString());
                    this.this$0.databaseReference.child(this.this$0.firebaseUser.getUid()).updateChildren((Map)hashMap).addOnSuccessListener((OnSuccessListener)new OnSuccessListener<Void>(this) {
                        final EditProfileActivity$17 this$1;
                        
                        public void onSuccess(final Void void1) {
                            this.this$1.this$0.pd.dismiss();
                            Toast.makeText((Context)this.this$1.this$0, (CharSequence)"Updated", 1).show();
                        }
                    }).addOnFailureListener((OnFailureListener)new OnFailureListener(this) {
                        final EditProfileActivity$17 this$1;
                        
                        public void onFailure(final Exception ex) {
                            this.this$1.this$0.pd.dismiss();
                            Toast.makeText((Context)this.this$1.this$0, (CharSequence)"Error Updating ", 1).show();
                        }
                    });
                }
                else {
                    this.this$0.pd.dismiss();
                    Toast.makeText((Context)this.this$0, (CharSequence)"Error", 1).show();
                }
            }
        }).addOnFailureListener((OnFailureListener)new OnFailureListener(this) {
            final EditProfileActivity this$0;
            
            public void onFailure(final Exception ex) {
                this.this$0.pd.dismiss();
                Toast.makeText((Context)this.this$0, (CharSequence)"Error", 1).show();
            }
        });
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        if (n2 == -1) {
            if (n == 300) {
                this.uploadProfileCoverPhoto(this.imageuri = intent.getData());
            }
            if (n == 400) {
                this.uploadProfileCoverPhoto(this.imageuri);
            }
        }
        super.onActivityResult(n, n2, intent);
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_edit_profile);
        this.profilepic = (TextView)this.findViewById(R.id.profilepic);
        this.editname = (TextView)this.findViewById(R.id.editname);
        this.logout = (TextView)this.findViewById(R.id.logout);
        this.set = (ImageView)this.findViewById(R.id.setting_profile_image);
        (this.pd = new ProgressDialog((Context)this)).setCanceledOnTouchOutside(false);
        this.editpassword = (TextView)this.findViewById(R.id.changepassword);
        final FirebaseAuth instance = FirebaseAuth.getInstance();
        this.firebaseAuth = instance;
        this.firebaseUser = instance.getCurrentUser();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.storageReference = FirebaseStorage.getInstance().getReference();
        final DatabaseReference reference = this.firebaseDatabase.getReference("Users");
        this.databaseReference = reference;
        this.cameraPermission = new String[] { "android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE" };
        this.storagePermission = new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" };
        reference.orderByChild("email").equalTo(this.firebaseUser.getEmail()).addValueEventListener((ValueEventListener)new ValueEventListener(this) {
            final EditProfileActivity this$0;
            
            public void onCancelled(final DatabaseError databaseError) {
            }
            
            public void onDataChange(DataSnapshot iterator) {
                iterator = (DataSnapshot)iterator.getChildren().iterator();
                while (((Iterator)iterator).hasNext()) {
                    final String string = "" + ((DataSnapshot)((Iterator)iterator).next()).child("image").getValue();
                    try {
                        Glide.with((FragmentActivity)this.this$0).load(string).into(this.this$0.set);
                    }
                    catch (final Exception ex) {}
                }
            }
        });
        this.editpassword.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final EditProfileActivity this$0;
            
            public void onClick(final View view) {
                this.this$0.pd.setMessage((CharSequence)"Changing Password");
                this.this$0.showPasswordChangeDailog();
            }
        });
        this.profilepic.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final EditProfileActivity this$0;
            
            public void onClick(final View view) {
                this.this$0.pd.setMessage((CharSequence)"Changing Profile Image");
                this.this$0.profileOrCoverPhoto = "image";
                this.this$0.showImagePicDialog();
            }
        });
        this.editname.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final EditProfileActivity this$0;
            
            public void onClick(final View view) {
                this.this$0.pd.setMessage((CharSequence)"Changing Username");
                this.this$0.showNamephoneupdate("name");
            }
        });
        this.logout.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final EditProfileActivity this$0;
            
            public void onClick(final View view) {
                FirebaseAuth.getInstance().getCurrentUser();
                FirebaseAuth.getInstance().signOut();
                this.this$0.startActivity(new Intent((Context)this.this$0, (Class)LoginActivity.class));
                this.this$0.finish();
            }
        });
    }
    
    protected void onPause() {
        super.onPause();
        this.databaseReference.orderByChild("email").equalTo(this.firebaseUser.getEmail()).addValueEventListener((ValueEventListener)new ValueEventListener(this) {
            final EditProfileActivity this$0;
            
            public void onCancelled(final DatabaseError databaseError) {
            }
            
            public void onDataChange(DataSnapshot iterator) {
                iterator = (DataSnapshot)iterator.getChildren().iterator();
                while (((Iterator)iterator).hasNext()) {
                    final String string = "" + ((DataSnapshot)((Iterator)iterator).next()).child("image").getValue();
                    try {
                        Glide.with((FragmentActivity)this.this$0).load(string).into(this.this$0.set);
                    }
                    catch (final Exception ex) {}
                }
            }
        });
        this.editpassword.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final EditProfileActivity this$0;
            
            public void onClick(final View view) {
                this.this$0.pd.setMessage((CharSequence)"Changing Password");
                this.this$0.showPasswordChangeDailog();
            }
        });
    }
    
    public void onRequestPermissionsResult(int n, final String[] array, final int[] array2) {
        boolean b = false;
        final int n2 = 0;
        switch (n) {
            case 200: {
                if (array2.length <= 0) {
                    break;
                }
                n = n2;
                if (array2[0] == 0) {
                    n = 1;
                }
                if (n != 0) {
                    this.pickFromGallery();
                    break;
                }
                Toast.makeText((Context)this, (CharSequence)"Please Enable Storage Permissions", 1).show();
                break;
            }
            case 100: {
                if (array2.length <= 0) {
                    break;
                }
                if (array2[0] == 0) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                if (array2[1] == 0) {
                    b = true;
                }
                if (n != 0 && b) {
                    this.pickFromCamera();
                    break;
                }
                Toast.makeText((Context)this, (CharSequence)"Please Enable Camera and Storage Permissions", 1).show();
                break;
            }
        }
    }
    
    protected void onStart() {
        super.onStart();
        this.databaseReference.orderByChild("email").equalTo(this.firebaseUser.getEmail()).addValueEventListener((ValueEventListener)new ValueEventListener(this) {
            final EditProfileActivity this$0;
            
            public void onCancelled(final DatabaseError databaseError) {
            }
            
            public void onDataChange(DataSnapshot iterator) {
                iterator = (DataSnapshot)iterator.getChildren().iterator();
                while (((Iterator)iterator).hasNext()) {
                    final String string = "" + ((DataSnapshot)((Iterator)iterator).next()).child("image").getValue();
                    try {
                        Glide.with((FragmentActivity)this.this$0).load(string).into(this.this$0.set);
                    }
                    catch (final Exception ex) {}
                }
            }
        });
        this.editpassword.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final EditProfileActivity this$0;
            
            public void onClick(final View view) {
                this.this$0.pd.setMessage((CharSequence)"Changing Password");
                this.this$0.showPasswordChangeDailog();
            }
        });
    }
}
