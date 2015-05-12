package com.sanat.tinderactionbar.tabbarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;

import com.sanat.tinderactionbar.R;

public class TabBarView extends LinearLayout {
    public interface IconTabProvider {
        public int getPageIconResId(int position);
    }

    // My Stuff
    private TabView tabView1, tabView2, tabView3;

    private static final int STRIP_HEIGHT = 6;

    public final Paint mPaint;

    private int mStripHeight;
    private float mOffset = 0f;
    public static int mSelectedTab = 0;
    public ViewPager pager;

    public static int tabCount;
    public OnPageChangeListener delegatePageListener;

    private View child;

    private View nextChild;

    public static int a;

    public TabBarView(Context context) {
        this(context, null);
    }

    public TabBarView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.actionBarTabBarStyle);
    }

    public TabBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT));

        setWillNotDraw(false);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);

        mStripHeight = (int) (STRIP_HEIGHT * getResources().getDisplayMetrics().density + .5f);
    }

    public void setStripColor(int color) {
        if (mPaint.getColor() != color) {
            mPaint.setColor(color);
            invalidate();
        }
    }

    public void setStripHeight(int height) {
        if (mStripHeight != height) {
            mStripHeight = height;
            invalidate();
        }
    }

    public void setSelectedTab(int tabIndex) {
        if (tabIndex < 0) {
            tabIndex = 0;
        }
        final int childCount = getChildCount();
        if (tabIndex >= childCount) {
            tabIndex = childCount - 1;
        }
        if (mSelectedTab != tabIndex) {
            mSelectedTab = tabIndex;
            invalidate();
        }
    }

    public void setOffset(int position, float offset) {
        if (mOffset != offset) {
            mOffset = offset;
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw the strip manually
        child = getChildAt(mSelectedTab);
        int height = getHeight();
        if (child != null) {
            float left = child.getLeft();
            float right = child.getRight();
            if (mOffset > 0f && mSelectedTab < tabCount - 1) {
                nextChild = getChildAt(mSelectedTab + 1);
                if (nextChild != null) {
                    final float nextTabLeft = nextChild.getLeft();
                    final float nextTabRight = nextChild.getRight();
                    left = (mOffset * nextTabLeft + (1f - mOffset) * left);
                    right = (mOffset * nextTabRight + (1f - mOffset) * right);
                }
            }
            canvas.drawRect(left, height - mStripHeight, right, height, mPaint);
        }
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        notifyDataSetChanged();
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        mSelectedTab = position;
        mOffset = positionOffset;

        invalidate();

        if (delegatePageListener != null) {
            delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        if (position == 0) {
            tabView1.setIcon(((IconTabProvider) pager.getAdapter()).getPageIconResId(3));
            tabView2.setIcon(((IconTabProvider) pager.getAdapter()).getPageIconResId(1));
            tabView3.setIcon(((IconTabProvider) pager.getAdapter()).getPageIconResId(2));
        } else if (position == 1) {
            tabView1.setIcon(((IconTabProvider) pager.getAdapter()).getPageIconResId(0));
            tabView2.setIcon(((IconTabProvider) pager.getAdapter()).getPageIconResId(4));
            tabView3.setIcon(((IconTabProvider) pager.getAdapter()).getPageIconResId(2));
        } else if (position == 2) {
            tabView1.setIcon(((IconTabProvider) pager.getAdapter()).getPageIconResId(0));
            tabView2.setIcon(((IconTabProvider) pager.getAdapter()).getPageIconResId(1));
            tabView3.setIcon(((IconTabProvider) pager.getAdapter()).getPageIconResId(5));
        }
    }

    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {

        }

        if (delegatePageListener != null) {
            delegatePageListener.onPageScrollStateChanged(state);
        }
    }

    public void onPageSelected(int position) {
        if (delegatePageListener != null) {
            delegatePageListener.onPageSelected(position);
        }
    }

    public void notifyDataSetChanged() {

        int resId;

        this.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {

            resId = ((IconTabProvider) pager.getAdapter()).getPageIconResId(i);

            if (getResources().getConfiguration().orientation == 1)
                addTabViewP(i, pager.getAdapter().getPageTitle(i).toString(), resId);
            else
                addTabViewL(i, pager.getAdapter().getPageTitle(i).toString(), resId);
        }

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                getViewTreeObserver().removeOnGlobalLayoutListener(this);

                mSelectedTab = pager.getCurrentItem();

            }
        });

    }

    private void addTabViewL(final int i, String string, int pageIconResId) {
        // TODO Auto-generated method stub
        TabView tab = new TabView(getContext());
//		tab.setIcon(pageIconResId);
        tab.setText(string, pageIconResId);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(i);
            }
        });

        this.addView(tab);
    }


    private void addTabViewP(final int i, final String string, int pageIconResId) {
        // TODO Auto-generated method stub

        if (i == 0) {
            tabView1 = new TabView(getContext());
            tabView1.setIcon(pageIconResId);
            tabView1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(i);
                }
            });
            CheatSheet.setup(tabView1, string);
            this.addView(tabView1);
        } else if (i == 1) {
            tabView2 = new TabView(getContext());
            tabView2.setIcon(pageIconResId);
            tabView2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(i);
                }
            });
            CheatSheet.setup(tabView2, string);
            this.addView(tabView2);
        } else if (i == 2) {
            tabView3 = new TabView(getContext());
            tabView3.setIcon(pageIconResId);
            tabView3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(i);
                }
            });
            CheatSheet.setup(tabView3, string);
            this.addView(tabView3);
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }
}