package com.maratodev.civeforum;

import com.google.firebase.database.Query;
import android.view.View;
import android.view.View$OnClickListener;
import com.google.firebase.auth.FirebaseAuth;
import android.os.Bundle;
import com.google.firebase.database.DatabaseReference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.HashMap;
import android.content.Context;
import android.widget.Toast;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import android.text.format.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Iterator;
import androidx.recyclerview.widget.RecyclerView$Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView$LayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class PostDetailsActivity extends AppCompatActivity
{
    AdapterComment adapterComment;
    EditText comment;
    List<ModelComment> commentList;
    boolean count;
    TextView description;
    String hisdp;
    String hisname;
    String hisuid;
    ImageView image;
    ImageView imagep;
    TextView like;
    Button likebtn;
    boolean mlike;
    ImageButton more;
    String mydp;
    String myemail;
    String myname;
    String myuid;
    TextView name;
    ImageView picture;
    String plike;
    String postId;
    LinearLayout profile;
    ProgressDialog progressDialog;
    String ptime;
    RecyclerView recyclerView;
    ImageButton sendb;
    Button share;
    TextView tcomment;
    TextView time;
    TextView title;
    String uimage;
    
    public PostDetailsActivity() {
        this.mlike = false;
        this.count = false;
    }
    
    private void loadComments() {
        this.recyclerView.setLayoutManager((RecyclerView$LayoutManager)new LinearLayoutManager(this.getApplicationContext()));
        this.commentList = (List<ModelComment>)new ArrayList();
        FirebaseDatabase.getInstance().getReference("Posts").child(this.postId).child("Comments").addValueEventListener((ValueEventListener)new ValueEventListener(this) {
            final PostDetailsActivity this$0;
            
            public void onCancelled(final DatabaseError databaseError) {
            }
            
            public void onDataChange(final DataSnapshot dataSnapshot) {
                this.this$0.commentList.clear();
                final Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    this.this$0.commentList.add((Object)((DataSnapshot)iterator.next()).getValue((Class)ModelComment.class));
                    this.this$0.adapterComment = new AdapterComment(this.this$0.getApplicationContext(), this.this$0.commentList, this.this$0.myuid, this.this$0.postId);
                    this.this$0.recyclerView.setAdapter((RecyclerView$Adapter)this.this$0.adapterComment);
                }
            }
        });
    }
    
    private void loadPostInfo() {
        FirebaseDatabase.getInstance().getReference("Posts").orderByChild("ptime").equalTo(this.postId).addValueEventListener((ValueEventListener)new ValueEventListener(this) {
            final PostDetailsActivity this$0;
            
            public void onCancelled(final DatabaseError databaseError) {
            }
            
            public void onDataChange(final DataSnapshot dataSnapshot) {
                for (final DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    final String string = dataSnapshot2.child("title").getValue().toString();
                    final String string2 = dataSnapshot2.child("description").getValue().toString();
                    this.this$0.ptime = dataSnapshot2.child("ptime").getValue().toString();
                    final Calendar instance = Calendar.getInstance(Locale.ENGLISH);
                    instance.setTimeInMillis(Long.parseLong(this.this$0.ptime));
                    final String string3 = DateFormat.format((CharSequence)"dd/MM/yyyy hh:mm aa", instance).toString();
                    this.this$0.name.setText((CharSequence)this.this$0.hisname);
                    this.this$0.title.setText((CharSequence)string);
                    this.this$0.description.setText((CharSequence)string2);
                    this.this$0.time.setText((CharSequence)string3);
                }
            }
        });
    }
    
    private void loadUserInfo() {
        ((Query)FirebaseDatabase.getInstance().getReference("Users")).orderByChild("uid").equalTo(this.myuid).addListenerForSingleValueEvent((ValueEventListener)new ValueEventListener(this) {
            final PostDetailsActivity this$0;
            
            public void onCancelled(final DatabaseError databaseError) {
            }
            
            public void onDataChange(DataSnapshot iterator) {
                iterator = (DataSnapshot)iterator.getChildren().iterator();
                while (((Iterator)iterator).hasNext()) {
                    final DataSnapshot dataSnapshot = (DataSnapshot)((Iterator)iterator).next();
                    this.this$0.myname = dataSnapshot.child("name").getValue().toString();
                    this.this$0.mydp = dataSnapshot.child("image").getValue().toString();
                    try {
                        Glide.with((FragmentActivity)this.this$0).load(this.this$0.mydp).into(this.this$0.imagep);
                    }
                    catch (final Exception ex) {}
                }
            }
        });
    }
    
    private void postComment() {
        this.progressDialog.setMessage((CharSequence)"Adding Comment");
        final String trim = this.comment.getText().toString().trim();
        if (TextUtils.isEmpty((CharSequence)trim)) {
            Toast.makeText((Context)this, (CharSequence)"Empty comment", 1).show();
            return;
        }
        this.progressDialog.show();
        final String value = String.valueOf(System.currentTimeMillis());
        final DatabaseReference child = FirebaseDatabase.getInstance().getReference("Posts").child(this.postId).child("Comments");
        final HashMap value2 = new HashMap();
        value2.put((Object)"cId", (Object)value);
        value2.put((Object)"comment", (Object)trim);
        value2.put((Object)"ptime", (Object)value);
        value2.put((Object)"uid", (Object)this.myuid);
        value2.put((Object)"uemail", (Object)this.myemail);
        value2.put((Object)"udp", (Object)this.mydp);
        value2.put((Object)"uname", (Object)this.myname);
        child.child(value).setValue((Object)value2).addOnSuccessListener((OnSuccessListener)new OnSuccessListener<Void>(this) {
            final PostDetailsActivity this$0;
            
            public void onSuccess(final Void void1) {
                this.this$0.progressDialog.dismiss();
                Toast.makeText((Context)this.this$0, (CharSequence)"Added", 1).show();
                this.this$0.comment.setText((CharSequence)"");
            }
        }).addOnFailureListener((OnFailureListener)new OnFailureListener(this) {
            final PostDetailsActivity this$0;
            
            public void onFailure(final Exception ex) {
                this.this$0.progressDialog.dismiss();
                Toast.makeText((Context)this.this$0, (CharSequence)"Failed", 1).show();
            }
        });
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.activity_post_details);
        this.postId = this.getIntent().getStringExtra("pid");
        this.recyclerView = (RecyclerView)this.findViewById(R.id.recyclecomment);
        this.picture = (ImageView)this.findViewById(R.id.pictureco);
        this.name = (TextView)this.findViewById(R.id.unameco);
        this.time = (TextView)this.findViewById(R.id.utimeco);
        this.more = (ImageButton)this.findViewById(R.id.morebtn);
        this.title = (TextView)this.findViewById(R.id.ptitleco);
        this.myemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        this.myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.description = (TextView)this.findViewById(R.id.descriptco);
        this.comment = (EditText)this.findViewById(R.id.typecommet);
        this.sendb = (ImageButton)this.findViewById(R.id.sendcomment);
        this.imagep = (ImageView)this.findViewById(R.id.commentimge);
        this.profile = (LinearLayout)this.findViewById(R.id.profilelayout);
        this.progressDialog = new ProgressDialog((Context)this);
        this.loadPostInfo();
        this.loadUserInfo();
        this.loadComments();
        this.sendb.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final PostDetailsActivity this$0;
            
            public void onClick(final View view) {
                this.this$0.postComment();
            }
        });
    }
    
    public boolean onSupportNavigateUp() {
        this.onBackPressed();
        return super.onSupportNavigateUp();
    }
}
