package com.example.musicplay;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class PagerBarHelper {
	private Animation mAnimAppear;
	private Animation mAnimDisappear;

	// private ImageButton mPrevBtn = null;
	private Button mNextBtn = null;
	// private TextView mTVPageNo = null;

	private View mView = null;

	public PagerBarHelper(View rootView, Context ctx) {
		mView = rootView;
		mAnimDisappear = AnimationUtils.loadAnimation(ctx,
				R.anim.footer_disappear);
		mAnimAppear = AnimationUtils.loadAnimation(ctx, R.anim.footer_appear);
		mNextBtn = (Button) rootView.findViewById(R.id.NextBtn);
	}

	public void showFooter() {
		if (mView.getVisibility() != View.VISIBLE) {
			mView.setVisibility(View.VISIBLE);
			mView.startAnimation(mAnimAppear);
		}
	}

	public void hideFooter() {
		if (mView.getVisibility() != View.GONE) {
			mView.setVisibility(View.GONE);
			mView.startAnimation(mAnimDisappear);
		}
	}

	public void hideFooterNoAnim() {
		mView.setVisibility(View.GONE);
	}

	/*
	 * public void setPageNo(int no) { mTVPageNo.setText("" + no); }
	 */

	public void showNext() {
		mNextBtn.setVisibility(View.VISIBLE);
	}

	public void hideNext() {
		mNextBtn.setVisibility(View.INVISIBLE);
	}

	/*
	 * public void showPrev() { mPrevBtn.setVisibility(View.VISIBLE); }
	 */

	/*
	 * public void hidePrev() { mPrevBtn.setVisibility(View.INVISIBLE); }
	 */

	public void setNextOnClickListener(OnClickListener l) {
		mNextBtn.setOnClickListener(l);
	}

	/*
	 * public void setPrevOnClickListener(OnClickListener l) {
	 * mPrevBtn.setOnClickListener(l); }
	 */
}
