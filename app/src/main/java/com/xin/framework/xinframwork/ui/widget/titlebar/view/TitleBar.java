package com.xin.framework.xinframwork.ui.widget.titlebar.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.LinkedList;

/**
 * . 左边文字，左边返回图片，左边点击事件
 * <p>
 * 左边可设置图片，文字单独显示，也可以设置图片文字同时显示
 * <p>
 * titleBar.setLeftImageResource(R.mipmap.back_green);
 * titleBar.setLeftText("返回");
 * titleBar.setLeftTextColor(Color.WHITE);
 * titleBar.setLeftClickListener(new View.OnClickListener() {
 *
 * @Override public void onClick(View v) {
 * finish();
 * }
 * });
 * 2. 中间文字
 * <p>
 * 中间文字根据左右控件始终居中显示，自动排版
 * <p>
 * titleBar.setTitle("文章详情");
 * titleBar.setTitleColor(Color.WHITE);
 * 3. 右边action按钮或者文字
 * <p>
 * 通过addAction添加操作控件，通过removeAction删除控件。可以使用图片或者文字。
 * <p>
 * titleBar.setActionTextColor(Color.WHITE);
 * mCollectView = (ImageView) titleBar.addAction(new TitleBar.ImageAction(R.mipmap.collect) {
 * @Override public void performAction(View view) {
 * Toast.makeText(MainActivity.this, "点击了收藏", Toast.LENGTH_SHORT).show();
 * mCollectView.setImageResource(R.mipmap.fabu);
 * }
 * });
 * <p>
 * titleBar.addAction(new TitleBar.TextAction("发布") {
 * @Override public void performAction(View view) {
 * Toast.makeText(MainActivity.this, "点击了发布", Toast.LENGTH_SHORT).show();
 * }
 * });
 * 4. 下划分割线
 * <p>
 * titleBar.setDividerColor(Color.GRAY);
 * 5. 一行代码适配沉浸式
 * <p>
 * 如果你的项目使用了沉浸式，布局时候加上这行代码，TitleBar会自动填充状态栏
 * <p>
 * titleBar.setImmersive(true);
 * 6. 一行代码设置TitleBar高度
 * <p>
 * 如果你的TitleBar没有固定高度，会默认设置48dip作为高度，你也可以自定义高度
 * <p>
 * titleBar.setHeight(48 * 2);
 * 7. 设置简单的副标题
 * <p>
 * 如果TitleBar分主副标题，用\n和\t区分，如果\n,主副标题上下排列，如果\t，主副标题左右排列
 * <p>
 * titleBar.setTitle("文章详情\n副标题");
 * titleBar.setTitle("文章详情\t副标题");
 */

public class TitleBar extends ViewGroup implements View.OnClickListener, ScrollAnimInterface {

    private static final int DEFAULT_MAIN_TEXT_SIZE = 18;
    private static final int DEFAULT_SUB_TEXT_SIZE = 12;
    private static final int DEFAULT_ACTION_TEXT_SIZE = 15;
    private static final int DEFAULT_TITLE_BAR_HEIGHT = 48;

    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

    private TextView mLeftText;
    private LinearLayout mRightLayout;
    private LinearLayout mCenterLayout;
    private TextView mCenterText;
    private TextView mSubTitleText;
    private View mCustomCenterView;
    private View mDividerView;

    private boolean mImmersive;

    private int mScreenWidth;
    private int mStatusBarHeight;
    private int mActionPadding;
    private int mOutPadding;
    private int mActionTextColor;
    private int mHeight;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        }
        mActionPadding = dip2px(5);
        mOutPadding = dip2px(8);
        mHeight = dip2px(DEFAULT_TITLE_BAR_HEIGHT);
        initView(context);
    }

    private void initView(Context context) {
        mLeftText = new TextView(context);
        mCenterLayout = new LinearLayout(context);
        mRightLayout = new LinearLayout(context);
        mDividerView = new View(context);

        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        mLeftText.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
        mLeftText.setSingleLine();
        mLeftText.setGravity(Gravity.CENTER_VERTICAL);
        mLeftText.setPadding(mOutPadding + mActionPadding, 0, mOutPadding, 0);

        mCenterText = new TextView(context);
        mSubTitleText = new TextView(context);
        mCenterLayout.addView(mCenterText);
        mCenterLayout.addView(mSubTitleText);

        mCenterLayout.setGravity(Gravity.CENTER);
        mCenterText.setTextSize(DEFAULT_MAIN_TEXT_SIZE);
        mCenterText.setSingleLine();
        mCenterText.setGravity(Gravity.CENTER);
        mCenterText.setEllipsize(TextUtils.TruncateAt.END);

        mSubTitleText.setTextSize(DEFAULT_SUB_TEXT_SIZE);
        mSubTitleText.setSingleLine();
        mSubTitleText.setGravity(Gravity.CENTER);
        mSubTitleText.setEllipsize(TextUtils.TruncateAt.END);

        mRightLayout.setPadding(mOutPadding, 0, mOutPadding, 0);

        addView(mLeftText, layoutParams);
        addView(mCenterLayout);
        addView(mRightLayout, layoutParams);
        addView(mDividerView, new LayoutParams(LayoutParams.MATCH_PARENT, 1));
//        addViewToFadeList(mCenterText);
//        addViewToFadeList(mSubTitleText);
    }

    public void setImmersive(boolean immersive) {
        mImmersive = immersive;
        if (mImmersive) {
            mStatusBarHeight = getStatusBarHeight();
        } else {
            mStatusBarHeight = 0;
        }
    }

    public void setHeight(int height) {
        mHeight = height;
        setMeasuredDimension(getMeasuredWidth(), mHeight);
    }

    public void setLeftImageResource(int resId) {
        mLeftText.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
    }

    public void setLeftClickListener(OnClickListener l) {
        mLeftText.setOnClickListener(l);
    }

    public void setLeftText(CharSequence title) {
        mLeftText.setText(title);
    }

    public void setLeftText(int resid) {
        mLeftText.setText(resid);
    }

    public void setLeftTextSize(float size) {
        mLeftText.setTextSize(size);
    }

    public void setLeftTextColor(int color) {
        mLeftText.setTextColor(color);
    }

    public void setLeftVisible(boolean visible) {
        mLeftText.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setTitle(CharSequence title) {
        int index = title.toString().indexOf("\n");
        if (index > 0) {
            setTitle(title.subSequence(0, index), title.subSequence(index + 1, title.length()), LinearLayout.VERTICAL);
        } else {
            index = title.toString().indexOf("\t");
            if (index > 0) {
                setTitle(title.subSequence(0, index), "  " + title.subSequence(index + 1, title.length()), LinearLayout.HORIZONTAL);
            } else {
                mCenterText.setText(title);
                mSubTitleText.setVisibility(View.GONE);
            }
        }
    }

    private void setTitle(CharSequence title, CharSequence subTitle, int orientation) {
        mCenterLayout.setOrientation(orientation);
        mCenterText.setText(title);

        mSubTitleText.setText(subTitle);
        mSubTitleText.setVisibility(View.VISIBLE);
    }

    public void setCenterClickListener(OnClickListener l) {
        mCenterLayout.setOnClickListener(l);
    }

    public void setTitle(int resid) {
        setTitle(getResources().getString(resid));
    }

    public void setTitleColor(@ColorRes int resid) {
        mCenterText.setTextColor(getResources().getColor(resid));
        mOriginBarTitleColor = mCenterText.getTextColors();
    }

    public void setTitleSize(float size) {
        mCenterText.setTextSize(size);
    }

    public void setTitleBackground(int resid) {
        mCenterText.setBackgroundResource(resid);
    }

    public void setSubTitleColor(int resid) {
        mSubTitleText.setTextColor(resid);
    }

    public void setSubTitleSize(float size) {
        mSubTitleText.setTextSize(size);
    }

    public void setCustomTitle(View titleView) {
        if (titleView == null) {
            mCenterText.setVisibility(View.VISIBLE);
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }

        } else {
            if (mCustomCenterView != null) {
                mCenterLayout.removeView(mCustomCenterView);
            }
            LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            mCustomCenterView = titleView;
            mCenterLayout.addView(titleView, layoutParams);
            mCenterText.setVisibility(View.GONE);
        }
    }

    public void setDivider(Drawable drawable) {
        mDividerView.setBackgroundDrawable(drawable);
    }

    public void setDividerColor(int color) {
        mDividerView.setBackgroundColor(color);
    }

    public void setDividerHeight(int dividerHeight) {
        mDividerView.getLayoutParams().height = dividerHeight;
    }

    public void setActionTextColor(int colorResId) {
        mActionTextColor = colorResId;
    }

    /**
     * Function to set a click listener for Title TextView
     *
     * @param listener the onClickListener
     */
    public void setOnTitleClickListener(OnClickListener listener) {
        mCenterText.setOnClickListener(listener);
    }

    @Override
    public void onClick(View view) {
        final Object tag = view.getTag();
        if (tag instanceof Action) {
            final Action action = (Action) tag;
            action.performAction(view);
        }
    }

    /**
     * Adds a list of {@link Action}s.
     *
     * @param actionList the actions to add
     */
    public void addActions(ActionList actionList) {
        int actions = actionList.size();
        for (int i = 0; i < actions; i++) {
            addAction(actionList.get(i));
        }
    }

    /**
     * Adds a new {@link Action}.
     *
     * @param action the action to add
     */
    public View addAction(Action action) {
        final int index = mRightLayout.getChildCount();
        return addAction(action, index);
    }

    /**
     * Adds a new {@link Action} at the specified index.
     *
     * @param action the action to add
     * @param index  the position at which to add the action
     */
    public View addAction(Action action, int index) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        View view = inflateAction(action);
        mRightLayout.addView(view, index, params);
        return view;
    }

    /**
     * Removes all action views from this action bar
     */
    public void removeAllActions() {
        mRightLayout.removeAllViews();
    }

    /**
     * Remove a action from the action bar.
     *
     * @param index position of action to remove
     */
    public void removeActionAt(int index) {
        mRightLayout.removeViewAt(index);
    }

    /**
     * Remove a action from the action bar.
     *
     * @param action The action to remove
     */
    public void removeAction(Action action) {
        int childCount = mRightLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = mRightLayout.getChildAt(i);
            if (view != null) {
                final Object tag = view.getTag();
                if (tag instanceof Action && tag.equals(action)) {
                    mRightLayout.removeView(view);
                }
            }
        }
    }

    /**
     * Returns the number of actions currently registered with the action bar.
     *
     * @return action count
     */
    public int getActionCount() {
        return mRightLayout.getChildCount();
    }

    /**
     * Inflates a {@link View} with the given {@link Action}.
     *
     * @param action the action to inflate
     * @return a view
     */
    private View inflateAction(Action action) {
        View view;
        if (TextUtils.isEmpty(action.getText())) {
            ImageView img = new ImageView(getContext());
            img.setImageResource(action.getDrawable());
            view = img;
        } else {
            TextView text = new TextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText(action.getText());
            text.setTextSize(DEFAULT_ACTION_TEXT_SIZE);
            if (mActionTextColor != 0) {
                text.setTextColor(mActionTextColor);
            }
            view = text;
        }

        view.setPadding(mActionPadding, 0, mActionPadding, 0);
        view.setTag(action);
        view.setOnClickListener(this);
        return view;
    }

    public View getViewByAction(Action action) {
        View view = findViewWithTag(action);
        return view;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height;
        if (heightMode != MeasureSpec.EXACTLY) {
            height = mHeight + mStatusBarHeight;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.getSize(heightMeasureSpec) + mStatusBarHeight;
        }
        mScreenWidth = MeasureSpec.getSize(widthMeasureSpec);
        measureChild(mLeftText, widthMeasureSpec, heightMeasureSpec);
        measureChild(mRightLayout, widthMeasureSpec, heightMeasureSpec);
        if (mLeftText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mLeftText.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        } else {
            mCenterLayout.measure(
                    MeasureSpec.makeMeasureSpec(mScreenWidth - 2 * mRightLayout.getMeasuredWidth(), MeasureSpec.EXACTLY)
                    , heightMeasureSpec);
        }
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLeftText.layout(0, mStatusBarHeight, mLeftText.getMeasuredWidth(), mLeftText.getMeasuredHeight() + mStatusBarHeight);
        mRightLayout.layout(mScreenWidth - mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                mScreenWidth, mRightLayout.getMeasuredHeight() + mStatusBarHeight);
        if (mLeftText.getMeasuredWidth() > mRightLayout.getMeasuredWidth()) {
            mCenterLayout.layout(mLeftText.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mLeftText.getMeasuredWidth(), getMeasuredHeight());
        } else {
            mCenterLayout.layout(mRightLayout.getMeasuredWidth(), mStatusBarHeight,
                    mScreenWidth - mRightLayout.getMeasuredWidth(), getMeasuredHeight());
        }
        mDividerView.layout(0, getMeasuredHeight() - mDividerView.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
    }

    public static int dip2px(int dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 计算状态栏高度高度
     * getStatusBarHeight
     *
     * @return
     */
    public static int getStatusBarHeight() {
        return getInternalDimensionSize(Resources.getSystem(), STATUS_BAR_HEIGHT_RES_NAME);
    }

    private static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void addViewToFadeListTitle() {
        mOriginBarTitleColor = mCenterText.getTextColors();
        addViewToFadeList(mCenterText);
        addViewToFadeList(mSubTitleText);
        addViewToFadeList(mLeftText);
        //……
    }


    /**
     * A {@link LinkedList} that holds a list of {@link Action}s.
     */
    @SuppressWarnings("serial")
    public static class ActionList extends LinkedList<Action> {
    }

    /**
     * Definition of an action that could be performed, along with a icon to
     * show.
     */
    public interface Action {
        String getText();

        int getDrawable();

        void performAction(View view);
    }

    public static abstract class ImageAction implements Action {
        private int mDrawable;

        public ImageAction(int drawable) {
            mDrawable = drawable;
        }

        @Override
        public int getDrawable() {
            return mDrawable;
        }

        @Override
        public String getText() {
            return null;
        }
    }

    public static abstract class TextAction implements Action {
        final private String mText;

        public TextAction(String text) {
            mText = text;
        }

        @Override
        public int getDrawable() {
            return 0;
        }

        @Override
        public String getText() {
            return mText;
        }
    }


    //-----------------------------------------------------------scroll  tr
    private static final String TAG = "CustomTitleBar";

    // ============================================================================
    // Scroll animation
    // ============================================================================

    // store original color
    private static final int ORIGIN_COLOR = -12345;

    private boolean mTransparentEnabled = false;
    // start position of fading animation
    private int mStartFadePosition = 80;
    // end position of fading animation
    private int mEndFadePosition = 380;
    // Max alpha, by default 247(0.97*255)
    public int mMaxAlpha = 247;
    // fade area
    private int mFadeDuration = mEndFadePosition - mStartFadePosition;
    // text shadow when completely transparent
    private float mShadowRadius = 0;
    private int mBarTitleTextShadowColor;

    // View needs to do gradual change during scroll animation
    private LinkedList<View> mFadeViewList = null;

    private ColorStateList mOriginBarTitleColor = null;
    public int mAlph;


    /**
     * Set transparent title bar anim.
     *
     * @param enabled whether to open anim
     */
    public void setTransparentEnabled(boolean enabled) {
        setTransparentEnabled(enabled, mStartFadePosition, mEndFadePosition, mMaxAlpha);
    }

    /**
     * Set transparent title bar anim.
     *
     * @param enabled whether to open anim
     * @param start   start position of fading animation
     * @param end     end position of fading animation
     */
    public void setTransparentEnabled(boolean enabled, int start, int end) {
        setTransparentEnabled(enabled, start, end, mMaxAlpha);
    }

    /**
     * Set transparent title bar anim.
     *
     * @param enabled  whether to open anim
     * @param start    start position of fading animation
     * @param end      end position of fading animation
     * @param maxAlpha max alpha(0-255)
     */
    public void setTransparentEnabled(boolean enabled, int start, int end, int maxAlpha) {
        mTransparentEnabled = enabled;
        if (mTransparentEnabled) {
            mStartFadePosition = start;
            mEndFadePosition = end;
            mMaxAlpha = maxAlpha;
            mFadeDuration = mEndFadePosition - mStartFadePosition;
            mAlph = 0;
        }
    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int endFadeItem) {
        if (mTransparentEnabled && firstVisibleItem <= endFadeItem) {
            View v = view.getChildAt(0);
            // Top of visible window, originally 0, turn to negative when scroll to lower position
            int top = (v == null) ? 0 : v.getTop();
            // distance from top of current window to ending position of fade
            int delta = top + mEndFadePosition - 4;
            mAlph = interpolate(delta);
            setTitleBarTranslate(mAlph);

        }
    }

    @Override
    public void onScroll(int y) {
        if (mTransparentEnabled) {
            setTitleBarTranslate(interpolate(mEndFadePosition - y));
        }
    }


    public void setTitleBarTranslate(int alpha) {
        if (getBackground() == null) {
            return;
        }
        getBackground().mutate().setAlpha(alpha);
        if (mFadeViewList == null || mCenterText == null) {
            return;
        }
        if (alpha > 0) {
            setTitleBarShadowLayer(0);
        }
        if (alpha >= mMaxAlpha) {
            setTitleBarColor(ORIGIN_COLOR);
        } else {
            setTitleBarColor(interpolateColor(Color.WHITE, mOriginBarTitleColor.getDefaultColor(), alpha, mMaxAlpha));
            if (alpha == 0) {
                setTitleBarShadowLayer(1f);
            }
        }
    }

    private void setTitleBarColor(int color) {
        for (View view : mFadeViewList) {
            setViewColor(view, color);
        }
    }

    private void setViewColor(View view, int color) {
        if (view == null || view.getVisibility() != VISIBLE) {
            return;
        }
        if (view instanceof TextView) {
            setViewColor((TextView) view, color);
        }
        if (view instanceof ImageView) {
            setViewColor((ImageView) view, color);
        }
    }

    private void setViewColor(TextView view, int color) {
        if (color == ORIGIN_COLOR) {
            view.setTextColor(mOriginBarTitleColor);
            if (view.getBackground() != null) {
                view.getBackground().clearColorFilter();
            }
        } else {
            view.setTextColor(color);
            if (view.getBackground() != null) {
                view.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    private void setViewColor(ImageView view, int color) {
        if (color == ORIGIN_COLOR) {
            view.clearColorFilter();
        } else {
            view.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void setTitleBarShadowLayer(float radius) {
        if (mShadowRadius != radius) {
            mShadowRadius = radius;
            for (View view : mFadeViewList) {
                if (view instanceof TextView) {
                    ((TextView) view).setShadowLayer(mShadowRadius, 0, 1, mBarTitleTextShadowColor);
                }
            }
        }
    }


    /**
     * @param delta distance to ending position
     * @return alpha value at current position
     */
    private int interpolate(int delta) {
        if (delta > mFadeDuration) {
            return 0;
        } else if (delta <= 0) {
            return mMaxAlpha;
        } else {
            float temp = ((float) delta) / mFadeDuration;
            return (int) ((1 - temp) * mMaxAlpha);
        }
    }

    /**
     * Add view that needs gradual change
     */
    public void addViewToFadeList(View view) {
        if (mFadeViewList == null) {
            mFadeViewList = new LinkedList<>();
        }
        if (view != null) {
            mFadeViewList.add(view);
        }
    }

    /**
     * remove view that has been added by addViewToFadeList
     */
    public void removeViewFromFadeList(View view) {
        if (mFadeViewList != null && view != null) {
            mFadeViewList.remove(view);
        }
    }


    public static int interpolateColor(int colorFrom, int colorTo, int posFrom, int posTo) {
        float delta = posTo - posFrom;
        int red = (int) ((Color.red(colorFrom) - Color.red(colorTo)) * delta / posTo + Color.red(colorTo));
        int green = (int) ((Color.green(colorFrom) - Color.green(colorTo)) * delta / posTo + Color.green(colorTo));
        int blue = (int) ((Color.blue(colorFrom) - Color.blue(colorTo)) * delta / posTo) + Color.blue(colorTo);
        return Color.argb(255, red, green, blue);
    }


    public TextView getmLeftText() {
        return mLeftText;
    }
}