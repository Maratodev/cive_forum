package com.maratodev.civeforum;

import android.content.Intent;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.content.Context;
import android.view.animation.AnimationUtils;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseUser;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity
{
    FirebaseUser currentUser;
    TextView f;
    private FirebaseAuth mAuth;
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(1);
        this.getWindow().setFlags(1024, 1024);
        this.setContentView(R.layout.activity_splash_screen);
        final FirebaseAuth instance = FirebaseAuth.getInstance();
        this.mAuth = instance;
        if (instance != null) {
            this.currentUser = instance.getCurrentUser();
        }
        (this.f = (TextView)this.findViewById(R.id.f)).startAnimation(AnimationUtils.loadAnimation((Context)this, R.anim.splash));
        final RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, 1, 0.5f, 1, 0.5f);
        rotateAnimation.setDuration(2000L);
        rotateAnimation.setRepeatCount(0);
        this.f.startAnimation((Animation)rotateAnimation);
        new Handler().postDelayed((Runnable)new Runnable(this) {
            final SplashScreenActivity this$0;
            
            public void run() {
                if (this.this$0.mAuth.getCurrentUser() == null) {
                    this.this$0.startActivity(new Intent((Context)this.this$0, (Class)LoginActivity.class));
                    this.this$0.finish();
                }
                else {
                    final Intent intent = new Intent((Context)this.this$0, (Class)MainActivity.class);
                    intent.addFlags(268468224);
                    this.this$0.startActivity(intent);
                    this.this$0.finish();
                }
            }
        }, 2500L);
    }
}
