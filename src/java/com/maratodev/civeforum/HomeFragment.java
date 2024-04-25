package com.maratodev.civeforum;

import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView$LayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import java.util.Iterator;
import androidx.recyclerview.widget.RecyclerView$Adapter;
import com.google.firebase.database.DataSnapshot;
import android.content.Context;
import android.widget.Toast;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.google.firebase.auth.FirebaseAuth;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment
{
    AdapterPosts adapterPosts;
    FirebaseAuth firebaseAuth;
    String myuid;
    List<ModelPosts> posts;
    RecyclerView recyclerView;
    
    private void loadPosts() {
        FirebaseDatabase.getInstance().getReference("Posts").addValueEventListener((ValueEventListener)new ValueEventListener(this) {
            final HomeFragment this$0;
            
            public void onCancelled(final DatabaseError databaseError) {
                Toast.makeText((Context)this.this$0.getActivity(), (CharSequence)databaseError.getMessage(), 1).show();
            }
            
            public void onDataChange(final DataSnapshot dataSnapshot) {
                this.this$0.posts.clear();
                final Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()) {
                    this.this$0.posts.add((Object)((DataSnapshot)iterator.next()).getValue((Class)ModelPosts.class));
                    this.this$0.adapterPosts = new AdapterPosts((Context)this.this$0.getActivity(), this.this$0.posts);
                    this.this$0.recyclerView.setAdapter((RecyclerView$Adapter)this.this$0.adapterPosts);
                }
            }
        });
    }
    
    public void onCreate(final Bundle bundle) {
        this.setHasOptionsMenu(true);
        super.onCreate(bundle);
    }
    
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
        this.firebaseAuth = FirebaseAuth.getInstance();
        (this.recyclerView = (RecyclerView)inflate.findViewById(R.id.postrecyclerview)).setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager((Context)this.getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        this.recyclerView.setLayoutManager((RecyclerView$LayoutManager)layoutManager);
        this.posts = (List<ModelPosts>)new ArrayList();
        this.loadPosts();
        return inflate;
    }
}
