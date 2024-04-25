package com.maratodev.civeforum;

import android.widget.LinearLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.content.Intent;
import android.view.View;
import android.view.View$OnClickListener;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import android.text.format.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import androidx.recyclerview.widget.RecyclerView$ViewHolder;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import java.util.List;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView$Adapter;

public class AdapterPosts extends RecyclerView$Adapter<MyHolder>
{
    Context context;
    List<ModelPosts> modelPosts;
    String myuid;
    private DatabaseReference postref;
    
    public AdapterPosts(final Context context, final List<ModelPosts> modelPosts) {
        this.context = context;
        this.modelPosts = modelPosts;
        this.myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.postref = FirebaseDatabase.getInstance().getReference().child("Posts");
    }
    
    public int getItemCount() {
        return this.modelPosts.size();
    }
    
    public void onBindViewHolder(final MyHolder myHolder, final int n) {
        final String uname = ((ModelPosts)this.modelPosts.get(n)).getUname();
        final String title = ((ModelPosts)this.modelPosts.get(n)).getTitle();
        final String description = ((ModelPosts)this.modelPosts.get(n)).getDescription();
        final String ptime = ((ModelPosts)this.modelPosts.get(n)).getPtime();
        final String udp = ((ModelPosts)this.modelPosts.get(n)).getUdp();
        ((ModelPosts)this.modelPosts.get(n)).getPComments();
        final Calendar instance = Calendar.getInstance(Locale.ENGLISH);
        instance.setTimeInMillis(Long.parseLong(ptime));
        final String string = DateFormat.format((CharSequence)"dd/MM/yyyy hh:mm aa", instance).toString();
        myHolder.name.setText((CharSequence)uname);
        myHolder.title.setText((CharSequence)title);
        myHolder.description.setText((CharSequence)description);
        myHolder.time.setText((CharSequence)string);
        try {
            Glide.with(this.context).load(udp).into(myHolder.picture);
        }
        catch (final Exception ex) {
            Toast.makeText(this.context, (CharSequence)ex.getMessage(), 0).show();
        }
        myHolder.addForumButton.setOnClickListener((View$OnClickListener)new View$OnClickListener(this, ptime) {
            final AdapterPosts this$0;
            final String val$pTime;
            
            public void onClick(final View view) {
                final Intent intent = new Intent(this.this$0.context, (Class)PostDetailsActivity.class);
                intent.putExtra("pid", this.val$pTime);
                this.this$0.context.startActivity(intent);
            }
        });
    }
    
    public MyHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        return new MyHolder(LayoutInflater.from(this.context).inflate(R.layout.row_post, viewGroup, false));
    }
    
    class MyHolder extends RecyclerView$ViewHolder
    {
        Button addForumButton;
        TextView comment;
        TextView description;
        TextView name;
        ImageView picture;
        LinearLayout profile;
        final AdapterPosts this$0;
        TextView time;
        TextView title;
        
        public MyHolder(final AdapterPosts this$0, final View view) {
            this.this$0 = this$0;
            super(view);
            this.picture = (ImageView)view.findViewById(R.id.picturetv);
            this.name = (TextView)view.findViewById(R.id.unametv);
            this.time = (TextView)view.findViewById(R.id.utimetv);
            this.title = (TextView)view.findViewById(R.id.ptitletv);
            this.description = (TextView)view.findViewById(R.id.descript);
            this.addForumButton = (Button)view.findViewById(R.id.participateButton);
            this.profile = (LinearLayout)view.findViewById(R.id.profilelayout);
        }
    }
}
