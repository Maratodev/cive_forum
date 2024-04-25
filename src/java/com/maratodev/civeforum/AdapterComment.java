package com.maratodev.civeforum;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import android.text.format.DateFormat;
import java.util.Calendar;
import java.util.Locale;
import androidx.recyclerview.widget.RecyclerView$ViewHolder;
import java.util.List;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView$Adapter;

public class AdapterComment extends RecyclerView$Adapter<MyHolder>
{
    Context context;
    List<ModelComment> list;
    String myuid;
    String postid;
    
    public AdapterComment(final Context context, final List<ModelComment> list, final String myuid, final String postid) {
        this.context = context;
        this.list = list;
        this.myuid = myuid;
        this.postid = postid;
    }
    
    public int getItemCount() {
        return this.list.size();
    }
    
    public void onBindViewHolder(final MyHolder myHolder, final int n) {
        ((ModelComment)this.list.get(n)).getUid();
        final String uname = ((ModelComment)this.list.get(n)).getUname();
        ((ModelComment)this.list.get(n)).getUemail();
        final String udp = ((ModelComment)this.list.get(n)).getUdp();
        ((ModelComment)this.list.get(n)).getcId();
        final String comment = ((ModelComment)this.list.get(n)).getComment();
        final String ptime = ((ModelComment)this.list.get(n)).getPtime();
        final Calendar instance = Calendar.getInstance(Locale.ENGLISH);
        instance.setTimeInMillis(Long.parseLong(ptime));
        final String string = DateFormat.format((CharSequence)"dd/MM/yyyy hh:mm aa", instance).toString();
        myHolder.name.setText((CharSequence)uname);
        myHolder.time.setText((CharSequence)string);
        myHolder.comment.setText((CharSequence)comment);
        try {
            Glide.with(this.context).load(udp).into(myHolder.imagea);
        }
        catch (final Exception ex) {}
    }
    
    public MyHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        return new MyHolder(LayoutInflater.from(this.context).inflate(R.layout.row_comments, viewGroup, false));
    }
    
    class MyHolder extends RecyclerView$ViewHolder
    {
        TextView comment;
        ImageView imagea;
        TextView name;
        final AdapterComment this$0;
        TextView time;
        
        public MyHolder(final AdapterComment this$0, final View view) {
            this.this$0 = this$0;
            super(view);
            this.imagea = (ImageView)view.findViewById(R.id.loadcomment);
            this.name = (TextView)view.findViewById(R.id.commentname);
            this.comment = (TextView)view.findViewById(R.id.commenttext);
            this.time = (TextView)view.findViewById(R.id.commenttime);
        }
    }
}
