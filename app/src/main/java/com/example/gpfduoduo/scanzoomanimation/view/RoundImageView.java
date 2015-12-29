package com.example.gpfduoduo.scanzoomanimation.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;

import com.example.gpfduoduo.scanzoomanimation.R;


public class RoundImageView extends ImageView
{
    private static final String tag = RoundImageView.class.getSimpleName();

    private Paint mPaint;
    private Xfermode mXfermode = new PorterDuffXfermode(Mode.DST_IN);
    private Bitmap mMaskBitmap;

    /**
     * 图片的类型，圆形or圆角
     */
    private int type;
    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;

    private int x = 0;
    private int y = 0;

    /**
     * 圆角大小的默认值
     */
    private static final int BODER_RADIUS_DEFAULT = 10;
    /**
     * 圆角的大小
     */
    private int mBorderRadius;

    private Bitmap mBitmap;
    private Drawable mDrawable;
    private int mDrawableW;
    private int mDrawableH;
    private float scale = 2.0f;

    public RoundImageView(Context context)
    {
        this(context, null);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public RoundImageView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs,
            R.styleable.RoundImageView);

        mBorderRadius = a.getDimensionPixelSize(
            R.styleable.RoundImageView_borderRadius,
            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                BODER_RADIUS_DEFAULT, getResources().getDisplayMetrics()));// 默认为10dp
        Log.e(tag, mBorderRadius + "");
        type = a.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);// 默认为Circle

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (type == TYPE_CIRCLE)
        {
            int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(width, width);
        }
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mDrawable = getDrawable();
        mDrawableW = mDrawable.getIntrinsicWidth();
        mDrawableH = mDrawable.getIntrinsicHeight();
    }

    @Override
    public void invalidate()
    {
        if (mMaskBitmap != null)
        {
            mMaskBitmap.recycle();
            mMaskBitmap = null;
        }
        super.invalidate();
    }

    public void setTransValue(int x, int y)
    {
        this.x = x;
        this.y = y;
        invalidate();
    }

    public int getBackWidth()
    {
        Drawable drawable = getDrawable();
        if (drawable != null)
            return drawable.getIntrinsicWidth();
        return 0;
    }

    public int getBackHeight()
    {
        Drawable drawable = getDrawable();
        if (drawable != null)
            return drawable.getIntrinsicHeight();
        return 0;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (mDrawable != null)
        {
            if (mBitmap != null)
                mBitmap.recycle();

            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);

            //创建画布
            Canvas drawCanvas = new Canvas(mBitmap);

            //根据缩放比例，设置bounds，相当于缩放图片了
            mDrawable.setBounds(x, y, (int) (scale * mDrawableW),
                (int) (scale * mDrawableH));

            mDrawable.draw(drawCanvas);
            if (mMaskBitmap == null || mMaskBitmap.isRecycled())
            {
                mMaskBitmap = getBitmap();
            }
            // Draw Bitmap.
            mPaint.reset();
            mPaint.setFilterBitmap(false);
            mPaint.setXfermode(mXfermode);
            //绘制形状
            drawCanvas.drawBitmap(mMaskBitmap, 0, 0, mPaint);
            mPaint.setXfermode(null);
            //将准备好的bitmap绘制出来
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    /**
     * 绘制形状
     * 
     * @return
     */
    public Bitmap getBitmap()
    {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);

        if (type == TYPE_ROUND)
        {
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), mBorderRadius,
                mBorderRadius, paint);
        }
        else
        {
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, getWidth() / 2, paint);
        }

        return bitmap;
    }
}
