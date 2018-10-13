package com.quicklib.android.ratioview.viewgroup;


import android.content.Context;
import android.content.res.TypedArray;
import androidx.cardview.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

import com.quicklib.android.ratioview.R;
import com.quicklib.android.ratioview.RatioView;


public class RatioCardView extends CardView implements RatioView {

    private int reference = 0;
    private float ratio = -1;


    public RatioCardView(Context context) {
        super(context);
        onCreate(context, null);
    }

    public RatioCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onCreate(context, attrs);
    }

    public RatioCardView(Context context, AttributeSet attrs, int defStyleAttr) {
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
                refSize = View.MeasureSpec.getSize(heightMeasureSpec);
            }else{
                refSize = View.MeasureSpec.getSize(widthMeasureSpec);
            }
            if(refSize != 0) {
                float targetSize = refSize * ratio;
                int targetSpec = View.MeasureSpec.makeMeasureSpec((int) targetSize, View.MeasureSpec.EXACTLY);

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
