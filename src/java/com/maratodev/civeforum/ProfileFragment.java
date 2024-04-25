package com.maratodev.civeforum;

import java.util.Iterator;
import android.content.Intent;
import android.view.View$OnClickListener;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment
{
    ImageView avatartv;
    DatabaseReference databaseReference;
    TextView email;
    FloatingActionButton fab;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    TextView nam;
    ProgressDialog pd;
    RecyclerView postrecycle;
    
    public void onCreate(final Bundle bundle) {
        this.setHasOptionsMenu(true);
        super.onCreate(bundle);
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(R.layout.fragment_profile, viewGroup, false);
        final FirebaseAuth instance = FirebaseAuth.getInstance();
        this.firebaseAuth = instance;
        this.firebaseUser = instance.getCurrentUser();
        final FirebaseDatabase instance2 = FirebaseDatabase.getInstance();
        this.firebaseDatabase = instance2;
        this.databaseReference = instance2.getReference("Users");
        this.avatartv = (ImageView)inflate.findViewById(R.id.avatartv);
        this.nam = (TextView)inflate.findViewById(R.id.nametv);
        this.email = (TextView)inflate.findViewById(R.id.emailtv);
        this.fab = (FloatingActionButton)inflate.findViewById(R.id.fab);
        (this.pd = new ProgressDialog((Context)this.getActivity())).setCanceledOnTouchOutside(false);
        this.databaseReference.orderByChild("email").equalTo(this.firebaseUser.getEmail()).addValueEventListener((ValueEventListener)new ValueEventListener(this) {
            final ProfileFragment this$0;
            
            public void onCancelled(final DatabaseError databaseError) {
            }
            
            public void onDataChange(DataSnapshot iterator) {
                iterator = (DataSnapshot)iterator.getChildren().iterator();
                while (((Iterator)iterator).hasNext()) {
                    final DataSnapshot dataSnapshot = (DataSnapshot)((Iterator)iterator).next();
                    final String string = "" + dataSnapshot.child("name").getValue();
                    final String string2 = "" + dataSnapshot.child("email").getValue();
                    final String string3 = "" + dataSnapshot.child("image").getValue();
                    this.this$0.nam.setText((CharSequence)string);
                    this.this$0.email.setText((CharSequence)string2);
                    try {
                        Glide.with(this.this$0.getActivity()).load(string3).into(this.this$0.avatartv);
                    }
                    catch (final Exception ex) {}
                }
            }
        });
        this.fab.setOnClickListener((View$OnClickListener)new View$OnClickListener(this) {
            final ProfileFragment this$0;
            
            public void onClick(final View view) {
                this.this$0.startActivity(new Intent((Context)this.this$0.getActivity(), (Class)EditProfileActivity.class));
            }
        });
        return inflate;
    }
}
