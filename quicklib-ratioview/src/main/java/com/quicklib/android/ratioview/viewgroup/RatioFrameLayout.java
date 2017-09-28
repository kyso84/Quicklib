package com.quicklib.android.ratioview.viewgroup;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.quicklib.android.ratioview.R;
import com.quicklib.android.ratioview.RatioView;


public class RatioFrameLayout extends FrameLayout implements RatioView {

    private int reference = 0;
    private float ratio = -1;


    public RatioFrameLayout(Context context) {
        super(context);
        onCreate(context, null);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate(context, attrs);
    }

    public RatioFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onCreate(context, attrs);
    }

    private void onCreate(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RatioLayout, 0, 0);
            reference = array.getInt(R.styleable.RatioLayout_refAxis, 0);
            ratio = array.getFloat(R.styleable.RatioLayout_refRatio, -1);
            array.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if( ratio != -1 ) {
            float refSize;
            if( reference == 1 ){
                refSize = MeasureSpec.getSize(heightMeasureSpec);
            }else{
                refSize = MeasureSpec.getSize(widthMeasureSpec);
            }
            if(refSize != 0) {
                float targetSize = refSize * ratio;
                int targetSpec = MeasureSpec.makeMeasureSpec((int) targetSize, MeasureSpec.EXACTLY);

                if (reference == 1) {
                    super.onMeasure(targetSpec, heightMeasureSpec);
                } else {
                    super.onMeasure(widthMeasureSpec, targetSpec);
                }
            }else{
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }else{
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    public void setRatio(float ratio) {
        this.ratio = ratio;
        this.requestLayout();
    }
}
