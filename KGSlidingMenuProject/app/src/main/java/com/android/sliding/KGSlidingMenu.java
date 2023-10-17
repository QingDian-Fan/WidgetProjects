package com.custom.project.record.day_12;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;//2月

import androidx.core.view.ViewCompat;

import com.android.sliding.R;


public class KGSlidingMenu extends HorizontalScrollView {

    // ViewDragHelper  ViewCompat

    private View mMenuView;
    private View mContainerView;
    private Context mContext;
    private GestureDetector mGestureDetector;//系统自带的手势处理类

    private boolean isMenuOpen = false;//菜单是否打开

    private int mMenuWidth;//需要自定义属性获取


    public KGSlidingMenu(Context context) {
        this(context, null);
    }

    public KGSlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KGSlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.KGSlidingMenu);
        mMenuWidth = Math.round(ta.getDimension(R.styleable.KGSlidingMenu_menu_width, 325));
        ta.recycle();

        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {//快速滑动
                //只要快速滑动就会回调
                //打开的时候往右快速滑动，就去关闭  关闭的时候往左边快速滑动就去打开
                //快速往右边滑动是一个正数  快速往左边滑动是一个负数
                Log.e("TAGTAG-->", "velocityX--->" + velocityX + "velocityY--->" + velocityY);
                if (Math.abs(velocityX) < Math.abs(velocityY) * 2 / 3) return false;
                if (isMenuOpen) {
                    //打开的时候往右快速滑动，就去关闭
                    if (velocityX < 0) {
                        closeMenu();
                        return true;
                    }
                } else {
                    //关闭的时候往左边快速滑动就去打开
                    if (velocityX > 0) {
                        openMenu();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onFinishInflate() {//在onCreate中执行
        super.onFinishInflate();
        //这个方法代表整个布局解析完毕
        //指定宽高 ：内容页的宽度为屏幕的宽度、菜单页的宽度由使用者自己在xml中定义  app:menu_width=""
        //获取菜单和容器View
        //获取的是LinearLayout
        ViewGroup mRootView = (ViewGroup) getChildAt(0);
        if (mRootView == null) {
            throw new NullPointerException("child can not be null !!!");
        }
        if (mRootView.getChildCount() != 2) {
            throw new RuntimeException("Only two can be placed View !!!");
        }
        //获取的是菜单和容器的跟布局
        mMenuView = mRootView.getChildAt(0);
        mContainerView = mRootView.getChildAt(1);
        //给菜单布局设置指定宽度  给容器布局设置屏幕的宽高
        //只能通过layoutParams设置宽高
        mMenuView.getLayoutParams().width = mMenuWidth;
        mContainerView.getLayoutParams().width = getScreenWidth(mContext);
        //7.0以下的手机必须采用下面的方式
        //menuLayoutPrams = mMenuView.getLayoutParams()
        //menuLayoutPrams.width = mMenuWidth;
        //mMenuView.setLayoutParams(menuLayoutPrams)
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {//在onResume中执行
        super.onLayout(changed, l, t, r, b);
        //初始化进来 mMenuView 模式是不显示的
        //用来摆放子View，等所有子View  摆放完毕才能滚动
        scrollTo(mMenuWidth, 0);
    }

    //滚动回调  处理右边View的缩放  左边View的缩放和透明度
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //l:left 变化mMenuWidth -> 0

        //算一个梯度值
        float scale = 1f * l / mMenuWidth; //从1->0
        Log.e("TAGTAG--->", "left:" + l + "   scale:" + scale);

        //计算右边的缩放值
        float rightScaleValue = 0.85f + 0.15f * scale;
        //设置右边的缩放  默认以view的中心点缩放
        ViewCompat.setPivotX(mContainerView, 0);
        ViewCompat.setPivotY(mContainerView, mContainerView.getMeasuredHeight() / 2);
        ViewCompat.setScaleX(mContainerView, rightScaleValue);
        ViewCompat.setScaleY(mContainerView, rightScaleValue);

        //设置右边的菜单的透明度  由半透明到全透明 0。85-1
        //缩放 0。85-1

        //透明度
        float leftAlphaValue = (1 - scale) * 0.5f + 0.5f;
        ViewCompat.setAlpha(mMenuView, leftAlphaValue);

        float leftScaleValue = (1 - scale) * 0.15f + 0.85f;
        ViewCompat.setScaleX(mMenuView, leftScaleValue);
        ViewCompat.setScaleY(mMenuView, leftScaleValue);

        //刚开始退出是在右边而不是左边
        //设置平移
        // ViewCompat.setTranslationX(mMenuView,l);抽屉效果
        ViewCompat.setTranslationX(mMenuView, 0.2f * l);


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
       // Log.e("TAGTAG---->", "event.getX()-->" + event.getX() + "   mMenuWidth-->" + mMenuView.getWidth() + "   mContainerView-->" + mContainerView.getWidth());
        if (isMenuOpen && event.getX() > mMenuView.getWidth() && event.getAction() == MotionEvent.ACTION_UP) {
            closeMenu();
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            if (mGestureDetector.onTouchEvent(event)) return true; //快速滑动执行了，就不要执行onTouch中手指抬起事件
        }
        if (onInterceptTouchEvent(event)) return true;

        //手指抬起
        if (MotionEvent.ACTION_UP == event.getAction()) {
            int mCurrentScrollX = getScrollX();
            float mCurrentScrollY = getScrollY();
            Log.e("TAGTAG-->", "ScrollX--->" + mCurrentScrollX + "ScrollY--->" + mCurrentScrollY);
            if (Math.abs(mCurrentScrollX) > Math.abs(mCurrentScrollY) * 2 / 3) {
                if (mCurrentScrollX < mMenuWidth / 2) {//未滚动到mMenuView宽度的一半
                    //打开菜单
                    openMenu();
                } else {
                    //关闭菜单
                    closeMenu();
                }
            }

            return false;//确保 super.onTouchEvent(event)  不会执行
        }

        return super.onTouchEvent(event);
    }

    private void closeMenu() {
        smoothScrollTo(mMenuWidth, 0);
        isMenuOpen = false;
    }


    private void openMenu() {
        smoothScrollTo(0, 0);
        isMenuOpen = true;
    }


    public int getScreenWidth(Context mContext) {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public int dp2px(float dp) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }

    public int dip2px(float dip) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dip * density);
    }
}
