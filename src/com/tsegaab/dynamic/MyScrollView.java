package com.tsegaab.dynamic;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView {

	private OnScrollViewListener mOnScrollViewListener;


	public MyScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public interface OnScrollViewListener {
        void onScrollChanged( MyScrollView v, int l, int t, int oldl, int oldt );
    }
    
    public void setOnScrollViewListener(OnScrollViewListener l) {
        this.mOnScrollViewListener = l;
    }
    
    
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
	    mOnScrollViewListener.onScrollChanged( this, l, t, oldl, oldt );
	    super.onScrollChanged( l, t, oldl, oldt );
	}
}
