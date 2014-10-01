package com.fly.swipeleftrightmenulistview;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;

/**
 * 
 * @Title: SwipeMenuLayout.java
 * @Author: zhaotf
 * 
 */
public class SwipeMenuLayout extends FrameLayout {

	private static final int CONTENT_VIEW_ID = 1;
	private static final int MENU_LEFT_VIEW_ID = 2;
	private static final int MENU_RIGHT_VIEW_ID = 3;

	private static final int STATE_CLOSE = 0;
	private static final int STATE_OPEN_LEFT = 1;
	private static final int STATE_OPEN_RIGHT = 2;

	private View mContentView;
	private SwipeMenuView mLeftMenuView;
	private SwipeMenuView mRightMenuView;
	private int mDownX;
	private int state = STATE_CLOSE;
	private GestureDetectorCompat mGestureDetector;
	private OnGestureListener mGestureListener;
	private boolean isFlingToLeft;
	private boolean isFlingToRight;
	/**
	 * 最小投掷，标量
	 */
	private int MIN_FLING = dp2px(15);
	/**
	 * 最大速度，标量
	 */
	private int MAX_VELOCITYX = dp2px(500);
	private ScrollerCompat mOpenScroller;
	private ScrollerCompat mCloseScroller;
	private int mBaseX;
	private int position;
	private Interpolator mCloseInterpolator;
	private Interpolator mOpenInterpolator;

	public SwipeMenuLayout(View contentView, SwipeMenuView menuLeftView, SwipeMenuView menuRightView) {
		this(contentView, menuLeftView, menuRightView, null, null);
	}

	public SwipeMenuLayout(View contentView, SwipeMenuView menuLeftView, SwipeMenuView menuRightView, Interpolator closeInterpolator,
			Interpolator openInterpolator) {
		super(contentView.getContext());
		mCloseInterpolator = closeInterpolator;
		mOpenInterpolator = openInterpolator;
		mContentView = contentView;

		mLeftMenuView = menuLeftView;
		mLeftMenuView.setLayout(this);
		mRightMenuView = menuRightView;
		mRightMenuView.setLayout(this);

		init();
	}

	// private SwipeMenuLayout(Context context, AttributeSet attrs, int
	// defStyle) {
	// super(context, attrs, defStyle);
	// }

	private SwipeMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private SwipeMenuLayout(Context context) {
		super(context);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
		mRightMenuView.setPosition(position);
		mLeftMenuView.setPosition(position);
	}

	/**
	 * 移动方向
	 */
	private int moveDirection = 0;
	/**
	 * 向左移动
	 */
	private static final int MOVE_TO_LEFT = 1;
	/**
	 * 向右移动
	 */
	private static final int MOVE_TO_RIGHT = 2;

	private void init() {
		setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mGestureListener = new SimpleOnGestureListener() {
			@Override
			public boolean onDown(MotionEvent e) {
				isFlingToLeft = false;
				isFlingToRight = false;
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
				// TODO
				if (velocityX > 0) {
					moveDirection = MOVE_TO_RIGHT;
					if (Math.abs(e1.getX() - e2.getX()) > MIN_FLING && Math.abs(velocityX) > MAX_VELOCITYX) {
						isFlingToRight = true;
					}
				} else {
					moveDirection = MOVE_TO_LEFT;
					if (Math.abs(e1.getX() - e2.getX()) > MIN_FLING && Math.abs(velocityX) > MAX_VELOCITYX) {
						isFlingToLeft = true;
					}
				}

				return super.onFling(e1, e2, velocityX, velocityY);
			}
		};
		mGestureDetector = new GestureDetectorCompat(getContext(), mGestureListener);

		// mScroller = ScrollerCompat.create(getContext(), new
		// BounceInterpolator());
		if (mCloseInterpolator != null) {
			mCloseScroller = ScrollerCompat.create(getContext(), mCloseInterpolator);
		} else {
			mCloseScroller = ScrollerCompat.create(getContext());
		}
		if (mOpenInterpolator != null) {
			mOpenScroller = ScrollerCompat.create(getContext(), mOpenInterpolator);
		} else {
			mOpenScroller = ScrollerCompat.create(getContext());
		}

		LayoutParams contentParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mContentView.setLayoutParams(contentParams);
		if (mContentView.getId() < 1) {
			mContentView.setId(CONTENT_VIEW_ID);
		}

		mRightMenuView.setId(MENU_RIGHT_VIEW_ID);
		mRightMenuView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mLeftMenuView.setId(MENU_LEFT_VIEW_ID);
		mLeftMenuView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addView(mLeftMenuView);
		addView(mContentView);
		addView(mRightMenuView);

		// if (mContentView.getBackground() == null) {
		// mContentView.setBackgroundColor(Color.WHITE);
		// }

		// in android 2.x, MenuView height is MATCH_PARENT is not work.
		// getViewTreeObserver().addOnGlobalLayoutListener(
		// new OnGlobalLayoutListener() {
		// @Override
		// public void onGlobalLayout() {
		// setMenuHeight(mContentView.getHeight());
		// // getViewTreeObserver()
		// // .removeGlobalOnLayoutListener(this);
		// }
		// });

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private int mTrueDownX;

	public boolean onSwipe(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mTrueDownX = (int) event.getX();
			mDownX = mTrueDownX;
			if (state == STATE_OPEN_LEFT) {
				mDownX -= mLeftMenuView.getWidth();
			} else if (state == STATE_OPEN_RIGHT) {
				mDownX += mRightMenuView.getWidth();
			}
			isFlingToLeft = false;
			isFlingToRight = false;
			moveDirection = 0;
			break;
		case MotionEvent.ACTION_MOVE:
			// 判断滑动方向
			if (event.getX() > mTrueDownX) {
				moveDirection = MOVE_TO_RIGHT;
			} else if (event.getX() < mTrueDownX) {
				moveDirection = MOVE_TO_LEFT;
			} else {
				moveDirection = 0;
			}
			int dis = (int) (event.getX() - mDownX);
			if (state == STATE_OPEN_RIGHT && moveDirection == MOVE_TO_LEFT) {
				return true;
			}
			if (state == STATE_OPEN_LEFT && moveDirection == MOVE_TO_RIGHT) {// 如果已经打开左侧menu，还往右滑动，则不作处理
				return true;
			}
			swipe(dis);
			break;
		case MotionEvent.ACTION_UP:
			if (isFlingToLeft || isFlingToRight) {// 是不是左右抛
				if (isFlingToLeft) {
					smoothOpenRightMenu();
				} else {
					smoothOpenLeftMenu();
				}
			} else {
				if (mContentView.getLeft() > 0 && mContentView.getLeft() > (mLeftMenuView.getWidth() / 2)) {
					smoothOpenLeftMenu();
				} else if (mContentView.getLeft() < 0 && ((-mContentView.getLeft()) > (mRightMenuView.getWidth() / 2))) {
					smoothOpenRightMenu();
				} else {
					smoothCloseMenu();
					return false;
				}
			}
			break;
		}
		return true;
	}

	public boolean isOpen() {
		return state == STATE_OPEN_RIGHT || state == STATE_OPEN_LEFT;
	}

	public boolean isOpenLeft() {
		return state == STATE_OPEN_LEFT;
	}

	public boolean isOpenRight() {
		return state == STATE_OPEN_RIGHT;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	private void swipe(int dis) {

		if (mContentView.getLeft() < -mRightMenuView.getWidth()) {
			dis = -mRightMenuView.getWidth();
			state = STATE_OPEN_RIGHT;
		}
		if (mContentView.getLeft() > mLeftMenuView.getWidth()) {
			dis = mLeftMenuView.getWidth();
			state = STATE_OPEN_LEFT;
		}
		mLeftMenuView.layout(dis - mLeftMenuView.getWidth(), mLeftMenuView.getTop(), dis, mLeftMenuView.getBottom());
		mContentView.layout(dis, mContentView.getTop(), mContentView.getWidth() + dis, getMeasuredHeight());
		mRightMenuView.layout(mContentView.getWidth() + dis, mRightMenuView.getTop(), mContentView.getWidth() + mRightMenuView.getWidth() + dis,
				mRightMenuView.getBottom());
	}

	/**
	 * 向左滑动（原来方法名swipe1）
	 * 
	 * @param dis
	 */
	private void swipeToLeft(int dis) {
		if (dis < -mRightMenuView.getWidth()) {
			dis = -mRightMenuView.getWidth();
		}
		mLeftMenuView.layout(dis - mLeftMenuView.getWidth(), mLeftMenuView.getTop(), dis, mLeftMenuView.getBottom());
		mContentView.layout(dis, mContentView.getTop(), mContentView.getWidth() + dis, getMeasuredHeight());
		mRightMenuView.layout(mContentView.getWidth() + dis, mRightMenuView.getTop(), mContentView.getWidth() + mRightMenuView.getWidth() + dis,
				mRightMenuView.getBottom());
	}

	/**
	 * 向右滑
	 * 
	 * @param dis
	 */
	private void swipeToRight(int dis) {
		if (dis > mLeftMenuView.getWidth()) {
			dis = mLeftMenuView.getWidth();
		}
		mLeftMenuView.layout(dis - mLeftMenuView.getWidth(), mLeftMenuView.getTop(), dis, mLeftMenuView.getBottom());
		mContentView.layout(dis, mContentView.getTop(), mContentView.getWidth() + dis, getMeasuredHeight());
		mRightMenuView.layout(mContentView.getWidth() + dis, mRightMenuView.getTop(), mContentView.getWidth() + mRightMenuView.getWidth() + dis,
				mRightMenuView.getBottom());
	}

	@Override
	public void computeScroll() {
		if (state == STATE_OPEN_RIGHT) {
			if (mOpenScroller.computeScrollOffset()) {
				swipeToLeft(mOpenScroller.getCurrX());
				postInvalidate();
			}
		} else if (state == STATE_OPEN_LEFT) {
			if (mOpenScroller.computeScrollOffset()) {
				swipeToRight(mOpenScroller.getCurrX());
				postInvalidate();
			}
		} else {
			if (mContentView.getLeft() > 0) {
				if (mCloseScroller.computeScrollOffset()) {
					swipe(mCloseScroller.getCurrX() - mBaseX);
					postInvalidate();
				}
			} else {
				if (mCloseScroller.computeScrollOffset()) {
					swipe(mBaseX - mCloseScroller.getCurrX());
					postInvalidate();
				}
			}

		}
	}

	public void smoothCloseMenu() {
		state = STATE_CLOSE;
		if (mContentView.getLeft() > 0) {
			mBaseX = -mContentView.getLeft();
		} else {
			mBaseX = mContentView.getLeft();
		}
		mCloseScroller.startScroll(0, 0, mBaseX, 0, 350);
		postInvalidate();
	}

	/**
	 * 打开右侧menu
	 */
	public void smoothOpenRightMenu() {
		state = STATE_OPEN_RIGHT;
		int dis;
		if (mContentView.getLeft() > 0) {
			dis = -(mRightMenuView.getWidth() + mContentView.getLeft());
		} else {
			dis = -(mRightMenuView.getWidth() - mContentView.getLeft());
		}
		mOpenScroller.startScroll(mContentView.getLeft(), 0, dis, 0, 350);
		postInvalidate();
	}

	/**
	 * 打开左侧menu
	 */
	public void smoothOpenLeftMenu() {
		state = STATE_OPEN_LEFT;
		int dis;
		if (mContentView.getLeft() > 0) {
			dis = mLeftMenuView.getWidth() - mContentView.getLeft();
		} else {
			dis = mLeftMenuView.getWidth() + mContentView.getLeft();
		}
		mOpenScroller.startScroll(mContentView.getLeft(), 0, dis, 0, 350);
		postInvalidate();
	}

	public void closeMenu() {
		if (mCloseScroller.computeScrollOffset()) {
			mCloseScroller.abortAnimation();
		}
		state = STATE_CLOSE;
		swipe(0);
	}

	public void openRightMenu() {
		if (state == STATE_CLOSE) {
			state = STATE_OPEN_RIGHT;
			swipe(-mRightMenuView.getWidth());
		}
	}

	public void openLeftMenu() {
		if (state == STATE_CLOSE) {
			state = STATE_OPEN_LEFT;
			swipe(mLeftMenuView.getWidth());
		}
	}

	public View getContentView() {
		return mContentView;
	}

	public SwipeMenuView getRightMenuView() {
		return mRightMenuView;
	}

	public SwipeMenuView getLeftMenuView() {
		return mLeftMenuView;
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mRightMenuView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
		mLeftMenuView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mLeftMenuView.layout(-mLeftMenuView.getMeasuredWidth(), 0, 0, mContentView.getMeasuredHeight());
		mContentView.layout(0, 0, getMeasuredWidth(), mContentView.getMeasuredHeight());
		mRightMenuView.layout(getMeasuredWidth(), 0, getMeasuredWidth() + mRightMenuView.getMeasuredWidth(), mContentView.getMeasuredHeight());
		// setMenuHeight(mContentView.getMeasuredHeight());
		// bringChildToFront(mContentView);
	}

	public void setRightMenuHeight(int measuredHeight) {
		//Log.i("byz", "pos = " + position + ", height = " + measuredHeight);
		LayoutParams params = (LayoutParams) mRightMenuView.getLayoutParams();
		if (params.height != measuredHeight) {
			params.height = measuredHeight;
			mRightMenuView.setLayoutParams(mRightMenuView.getLayoutParams());
		}
	}

	public void setLeftMenuHeight(int measuredHeight) {
		//Log.i("byz", "pos = " + position + ", height = " + measuredHeight);
		LayoutParams params = (LayoutParams) mLeftMenuView.getLayoutParams();
		if (params.height != measuredHeight) {
			params.height = measuredHeight;
			mLeftMenuView.setLayoutParams(mLeftMenuView.getLayoutParams());
		}
	}
}
