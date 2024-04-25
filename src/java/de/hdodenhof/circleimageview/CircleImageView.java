package de.hdodenhof.circleimageview;

import android.graphics.Rect;
import android.graphics.Outline;
import android.view.View;
import android.net.Uri;
import android.view.MotionEvent;
import android.graphics.Paint$Style;
import android.graphics.Shader;
import android.graphics.Shader$TileMode;
import android.view.ViewOutlineProvider;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.ColorFilter;
import android.graphics.RectF;
import android.graphics.BitmapShader;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.widget.ImageView$ScaleType;
import android.graphics.Bitmap$Config;
import android.widget.ImageView;

public class CircleImageView extends ImageView
{
    private static final Bitmap$Config BITMAP_CONFIG;
    private static final int COLORDRAWABLE_DIMENSION = 2;
    private static final int DEFAULT_BORDER_COLOR = -16777216;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;
    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_CIRCLE_BACKGROUND_COLOR = 0;
    private static final ImageView$ScaleType SCALE_TYPE;
    private Bitmap mBitmap;
    private int mBitmapHeight;
    private final Paint mBitmapPaint;
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBorderColor;
    private boolean mBorderOverlay;
    private final Paint mBorderPaint;
    private float mBorderRadius;
    private final RectF mBorderRect;
    private int mBorderWidth;
    private int mCircleBackgroundColor;
    private final Paint mCircleBackgroundPaint;
    private ColorFilter mColorFilter;
    private boolean mDisableCircularTransformation;
    private float mDrawableRadius;
    private final RectF mDrawableRect;
    private boolean mReady;
    private boolean mSetupPending;
    private final Matrix mShaderMatrix;
    
    static {
        SCALE_TYPE = ImageView$ScaleType.CENTER_CROP;
        BITMAP_CONFIG = Bitmap$Config.ARGB_8888;
    }
    
    public CircleImageView(final Context context) {
        super(context);
        this.mDrawableRect = new RectF();
        this.mBorderRect = new RectF();
        this.mShaderMatrix = new Matrix();
        this.mBitmapPaint = new Paint();
        this.mBorderPaint = new Paint();
        this.mCircleBackgroundPaint = new Paint();
        this.mBorderColor = -16777216;
        this.mBorderWidth = 0;
        this.mCircleBackgroundColor = 0;
        this.init();
    }
    
    public CircleImageView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public CircleImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mDrawableRect = new RectF();
        this.mBorderRect = new RectF();
        this.mShaderMatrix = new Matrix();
        this.mBitmapPaint = new Paint();
        this.mBorderPaint = new Paint();
        this.mCircleBackgroundPaint = new Paint();
        this.mBorderColor = -16777216;
        this.mBorderWidth = 0;
        this.mCircleBackgroundColor = 0;
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.CircleImageView, n, 0);
        this.mBorderWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.CircleImageView_civ_border_width, 0);
        this.mBorderColor = obtainStyledAttributes.getColor(R.styleable.CircleImageView_civ_border_color, -16777216);
        this.mBorderOverlay = obtainStyledAttributes.getBoolean(R.styleable.CircleImageView_civ_border_overlay, false);
        this.mCircleBackgroundColor = obtainStyledAttributes.getColor(R.styleable.CircleImageView_civ_circle_background_color, 0);
        obtainStyledAttributes.recycle();
        this.init();
    }
    
    private void applyColorFilter() {
        final Paint mBitmapPaint = this.mBitmapPaint;
        if (mBitmapPaint != null) {
            mBitmapPaint.setColorFilter(this.mColorFilter);
        }
    }
    
    private RectF calculateBounds() {
        final int n = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
        final int n2 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
        final int min = Math.min(n, n2);
        final float n3 = this.getPaddingLeft() + (n - min) / 2.0f;
        final float n4 = this.getPaddingTop() + (n2 - min) / 2.0f;
        return new RectF(n3, n4, min + n3, min + n4);
    }
    
    private Bitmap getBitmapFromDrawable(final Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }
        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(2, 2, CircleImageView.BITMAP_CONFIG);
            }
            else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), CircleImageView.BITMAP_CONFIG);
            }
            final Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        }
        catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    private boolean inTouchableArea(final float n, final float n2) {
        final boolean empty = this.mBorderRect.isEmpty();
        boolean b = true;
        if (empty) {
            return true;
        }
        if (Math.pow((double)(n - this.mBorderRect.centerX()), 2.0) + Math.pow((double)(n2 - this.mBorderRect.centerY()), 2.0) > Math.pow((double)this.mBorderRadius, 2.0)) {
            b = false;
        }
        return b;
    }
    
    private void init() {
        super.setScaleType(CircleImageView.SCALE_TYPE);
        this.mReady = true;
        this.setOutlineProvider((ViewOutlineProvider)new OutlineProvider());
        if (this.mSetupPending) {
            this.setup();
            this.mSetupPending = false;
        }
    }
    
    private void initializeBitmap() {
        if (this.mDisableCircularTransformation) {
            this.mBitmap = null;
        }
        else {
            this.mBitmap = this.getBitmapFromDrawable(this.getDrawable());
        }
        this.setup();
    }
    
    private void setup() {
        if (!this.mReady) {
            this.mSetupPending = true;
            return;
        }
        if (this.getWidth() == 0 && this.getHeight() == 0) {
            return;
        }
        if (this.mBitmap == null) {
            this.invalidate();
            return;
        }
        this.mBitmapShader = new BitmapShader(this.mBitmap, Shader$TileMode.CLAMP, Shader$TileMode.CLAMP);
        this.mBitmapPaint.setAntiAlias(true);
        this.mBitmapPaint.setDither(true);
        this.mBitmapPaint.setFilterBitmap(true);
        this.mBitmapPaint.setShader((Shader)this.mBitmapShader);
        this.mBorderPaint.setStyle(Paint$Style.STROKE);
        this.mBorderPaint.setAntiAlias(true);
        this.mBorderPaint.setColor(this.mBorderColor);
        this.mBorderPaint.setStrokeWidth((float)this.mBorderWidth);
        this.mCircleBackgroundPaint.setStyle(Paint$Style.FILL);
        this.mCircleBackgroundPaint.setAntiAlias(true);
        this.mCircleBackgroundPaint.setColor(this.mCircleBackgroundColor);
        this.mBitmapHeight = this.mBitmap.getHeight();
        this.mBitmapWidth = this.mBitmap.getWidth();
        this.mBorderRect.set(this.calculateBounds());
        this.mBorderRadius = Math.min((this.mBorderRect.height() - this.mBorderWidth) / 2.0f, (this.mBorderRect.width() - this.mBorderWidth) / 2.0f);
        this.mDrawableRect.set(this.mBorderRect);
        if (!this.mBorderOverlay) {
            final int mBorderWidth = this.mBorderWidth;
            if (mBorderWidth > 0) {
                this.mDrawableRect.inset(mBorderWidth - 1.0f, mBorderWidth - 1.0f);
            }
        }
        this.mDrawableRadius = Math.min(this.mDrawableRect.height() / 2.0f, this.mDrawableRect.width() / 2.0f);
        this.applyColorFilter();
        this.updateShaderMatrix();
        this.invalidate();
    }
    
    private void updateShaderMatrix() {
        float n = 0.0f;
        float n2 = 0.0f;
        this.mShaderMatrix.set((Matrix)null);
        float n3;
        if (this.mBitmapWidth * this.mDrawableRect.height() > this.mDrawableRect.width() * this.mBitmapHeight) {
            n3 = this.mDrawableRect.height() / this.mBitmapHeight;
            n = (this.mDrawableRect.width() - this.mBitmapWidth * n3) * 0.5f;
        }
        else {
            n3 = this.mDrawableRect.width() / this.mBitmapWidth;
            n2 = (this.mDrawableRect.height() - this.mBitmapHeight * n3) * 0.5f;
        }
        this.mShaderMatrix.setScale(n3, n3);
        this.mShaderMatrix.postTranslate((int)(n + 0.5f) + this.mDrawableRect.left, (int)(0.5f + n2) + this.mDrawableRect.top);
        this.mBitmapShader.setLocalMatrix(this.mShaderMatrix);
    }
    
    public int getBorderColor() {
        return this.mBorderColor;
    }
    
    public int getBorderWidth() {
        return this.mBorderWidth;
    }
    
    public int getCircleBackgroundColor() {
        return this.mCircleBackgroundColor;
    }
    
    public ColorFilter getColorFilter() {
        return this.mColorFilter;
    }
    
    public ImageView$ScaleType getScaleType() {
        return CircleImageView.SCALE_TYPE;
    }
    
    public boolean isBorderOverlay() {
        return this.mBorderOverlay;
    }
    
    public boolean isDisableCircularTransformation() {
        return this.mDisableCircularTransformation;
    }
    
    protected void onDraw(final Canvas canvas) {
        if (this.mDisableCircularTransformation) {
            super.onDraw(canvas);
            return;
        }
        if (this.mBitmap == null) {
            return;
        }
        if (this.mCircleBackgroundColor != 0) {
            canvas.drawCircle(this.mDrawableRect.centerX(), this.mDrawableRect.centerY(), this.mDrawableRadius, this.mCircleBackgroundPaint);
        }
        canvas.drawCircle(this.mDrawableRect.centerX(), this.mDrawableRect.centerY(), this.mDrawableRadius, this.mBitmapPaint);
        if (this.mBorderWidth > 0) {
            canvas.drawCircle(this.mBorderRect.centerX(), this.mBorderRect.centerY(), this.mBorderRadius, this.mBorderPaint);
        }
    }
    
    protected void onSizeChanged(final int n, final int n2, final int n3, final int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        this.setup();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        if (this.mDisableCircularTransformation) {
            return super.onTouchEvent(motionEvent);
        }
        return this.inTouchableArea(motionEvent.getX(), motionEvent.getY()) && super.onTouchEvent(motionEvent);
    }
    
    public void setAdjustViewBounds(final boolean b) {
        if (!b) {
            return;
        }
        throw new IllegalArgumentException("adjustViewBounds not supported.");
    }
    
    public void setBorderColor(final int n) {
        if (n == this.mBorderColor) {
            return;
        }
        this.mBorderColor = n;
        this.mBorderPaint.setColor(n);
        this.invalidate();
    }
    
    public void setBorderOverlay(final boolean mBorderOverlay) {
        if (mBorderOverlay == this.mBorderOverlay) {
            return;
        }
        this.mBorderOverlay = mBorderOverlay;
        this.setup();
    }
    
    public void setBorderWidth(final int mBorderWidth) {
        if (mBorderWidth == this.mBorderWidth) {
            return;
        }
        this.mBorderWidth = mBorderWidth;
        this.setup();
    }
    
    public void setCircleBackgroundColor(final int n) {
        if (n == this.mCircleBackgroundColor) {
            return;
        }
        this.mCircleBackgroundColor = n;
        this.mCircleBackgroundPaint.setColor(n);
        this.invalidate();
    }
    
    public void setCircleBackgroundColorResource(final int n) {
        this.setCircleBackgroundColor(this.getContext().getResources().getColor(n));
    }
    
    public void setColorFilter(final ColorFilter mColorFilter) {
        if (mColorFilter == this.mColorFilter) {
            return;
        }
        this.mColorFilter = mColorFilter;
        this.applyColorFilter();
        this.invalidate();
    }
    
    public void setDisableCircularTransformation(final boolean mDisableCircularTransformation) {
        if (this.mDisableCircularTransformation == mDisableCircularTransformation) {
            return;
        }
        this.mDisableCircularTransformation = mDisableCircularTransformation;
        this.initializeBitmap();
    }
    
    public void setImageBitmap(final Bitmap imageBitmap) {
        super.setImageBitmap(imageBitmap);
        this.initializeBitmap();
    }
    
    public void setImageDrawable(final Drawable imageDrawable) {
        super.setImageDrawable(imageDrawable);
        this.initializeBitmap();
    }
    
    public void setImageResource(final int imageResource) {
        super.setImageResource(imageResource);
        this.initializeBitmap();
    }
    
    public void setImageURI(final Uri imageURI) {
        super.setImageURI(imageURI);
        this.initializeBitmap();
    }
    
    public void setPadding(final int n, final int n2, final int n3, final int n4) {
        super.setPadding(n, n2, n3, n4);
        this.setup();
    }
    
    public void setPaddingRelative(final int n, final int n2, final int n3, final int n4) {
        super.setPaddingRelative(n, n2, n3, n4);
        this.setup();
    }
    
    public void setScaleType(final ImageView$ScaleType imageView$ScaleType) {
        if (imageView$ScaleType == CircleImageView.SCALE_TYPE) {
            return;
        }
        throw new IllegalArgumentException(String.format("ScaleType %s not supported.", new Object[] { imageView$ScaleType }));
    }
    
    private class OutlineProvider extends ViewOutlineProvider
    {
        final CircleImageView this$0;
        
        private OutlineProvider(final CircleImageView this$0) {
            this.this$0 = this$0;
        }
        
        public void getOutline(final View view, final Outline outline) {
            if (this.this$0.mDisableCircularTransformation) {
                ViewOutlineProvider.BACKGROUND.getOutline(view, outline);
            }
            else {
                final Rect rect = new Rect();
                this.this$0.mBorderRect.roundOut(rect);
                outline.setRoundRect(rect, rect.width() / 2.0f);
            }
        }
    }
}
