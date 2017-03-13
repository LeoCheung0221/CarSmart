package com.cn.smart.baselib.uiframework.navigation;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.IntDef;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.smart.baselib.R;
import com.cn.smart.baselib.uiframework.navigation.behaviour.SmartNavBarFabBehaviour;
import com.cn.smart.baselib.uiframework.navigation.behaviour.SmartVerticalScrollBehavior;
import com.cn.smart.baselib.uiframework.navigation.utils.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import static com.tencent.open.utils.ThreadManager.init;

/**
 * author：leo on 2017/3/2 15:59
 * email： leocheung4ever@gmail.com
 * description: 自定义底部导航栏
 * what & why is modified:
 */

@CoordinatorLayout.DefaultBehavior(SmartVerticalScrollBehavior.class)
public class SmartNavigationBar extends FrameLayout {

    //Constant
    private static String TAG = SmartNavigationBar.class.getSimpleName();

    //Static Variables (Styles)
    public static final int MODE_DEFAULT = 0;
    public static final int MODE_FIXED = 1;
    public static final int MODE_SHIFTING = 2;

    @IntDef({MODE_DEFAULT, MODE_FIXED, MODE_SHIFTING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    public static final int BACKGROUND_STYLE_DEFAULT = 0;
    public static final int BACKGROUND_STYLE_STATIC = 1;
    public static final int BACKGROUND_STYLE_RIPPLE = 2;

    @IntDef({BACKGROUND_STYLE_DEFAULT, BACKGROUND_STYLE_STATIC, BACKGROUND_STYLE_RIPPLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BackgroundStyle {
    }

    private static final int FAB_BEHAVIOUR_TRANSLATE_AND_STICK = 0;
    private static final int FAB_BEHAVIOUR_DISAPPEAR = 1;
    private static final int FAB_BEHAVIOUR_TRANSLATE_OUT = 2;

    @IntDef({FAB_BEHAVIOUR_TRANSLATE_AND_STICK, FAB_BEHAVIOUR_DISAPPEAR, FAB_BEHAVIOUR_TRANSLATE_OUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FabBehaviour {
    }

    @Mode
    private int mMode = MODE_DEFAULT;
    @BackgroundStyle
    private int mBackgroundStyle = BACKGROUND_STYLE_DEFAULT;

    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
    private static final int MIN_SIZE = 3;
    private static final int MAX_SIZE = 5;

    //Variable
    private ViewPropertyAnimatorCompat mTranslationAnimator;
    private boolean mScrollable = false;
    private OnTabSelectedListener mTabSelectedListener;
    ArrayList<SmartNavigationItem> mSmartNavigationItems = new ArrayList<>();
    ArrayList<SmartNavigationTab> mSmartNavigationTabs = new ArrayList<>();

    private static final int DEFAULT_SELECTED_POSITION = -1;
    private int mSelectedPosition = DEFAULT_SELECTED_POSITION;
    private int mFirstSelectedPosition = 0;

    private int mActiveColor;
    private int mInActiveColor;
    private int mBackgroundColor;

    private FrameLayout mBackgroundOverlay;
    private FrameLayout mContainer;
    private LinearLayout mTabContainer;

    private static final int DEFAULT_ANIMATION_DURATION = 200;
    private int mAnimationDuration = DEFAULT_ANIMATION_DURATION;
    private int mRippleAnimationDuration = (int) (DEFAULT_ANIMATION_DURATION * 2.5);

    private float mElevation;

    private boolean mAutoHideEnabled;
    private boolean mIsHidden = false;

    //Listener

    /**
     * Callback interface invoked when a tab's selection state changes.
     */
    public interface OnTabSelectedListener {

        /**
         * Called when a tab enters the selected state.
         *
         * @param position The position of the tab that was selected
         */
        void onTabSelected(int position);

        /**
         * Called when a tab exits the selected state.
         *
         * @param position The position of the tab that was unselected
         */
        void onTabUnselected(int position);

        /**
         * Called when a tab that is already selected is chosen again by the user. Some applications
         * may use this action to return to the top level of a category.
         *
         * @param position The position of the tab that was reselected.
         */
        void onTabReselected(int position);
    }

    ///////////////////////////////////////////////////////////////////////////
    // View Default Constructors and Methods
    ///////////////////////////////////////////////////////////////////////////
    public SmartNavigationBar(Context context) {
        this(context, null);
    }

    public SmartNavigationBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartNavigationBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SmartNavigationBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttrs(context, attrs);
        init();
    }

    ///////////////////////////////////////////////////////////////////////////
    // View Data Setter methods, Called before Initialize method
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Used to add a new tab.
     *
     * @param item bottom navigation tab details
     * @return this, to allow builder pattern
     */
    public SmartNavigationBar addItem(SmartNavigationItem item) {
        mSmartNavigationItems.add(item);
        return this;
    }

    /**
     * Used to remove a tab.
     * you should call initialise() after this to see the results effected.
     *
     * @param item bottom navigation tab details
     * @return this, to allow builder pattern
     */
    public SmartNavigationBar removeItem(SmartNavigationItem item) {
        mSmartNavigationItems.remove(item);
        return this;
    }

    /**
     * @param mode any of the three Modes supported by library
     * @return this, to allow builder pattern
     */
    public SmartNavigationBar setMode(@Mode int mode) {
        this.mMode = mode;
        return this;
    }

    /**
     * @param backgroundStyle any of the three Background Styles supported by library
     * @return this, to allow builder pattern
     */
    public SmartNavigationBar setBackgroundStyle(@BackgroundStyle int backgroundStyle) {
        this.mBackgroundStyle = backgroundStyle;
        return this;
    }

    /**
     * @param activeColor res code for the default active color
     * @return this, to allow builder pattern
     */
    public SmartNavigationBar setActiveColor(@ColorRes int activeColor) {
        this.mActiveColor = ContextCompat.getColor(getContext(), activeColor);
        return this;
    }

    /**
     * @param activeColorCode color code in string format for the default active color
     * @return this, to allow builder pattern
     */
    public SmartNavigationBar setActiveColor(String activeColorCode) {
        this.mActiveColor = Color.parseColor(activeColorCode);
        return this;
    }

    /**
     * @param inActiveColor res code for the default in-active color
     * @return this, to allow builder pattern
     */
    public SmartNavigationBar setInActiveColor(@ColorRes int inActiveColor) {
        this.mInActiveColor = ContextCompat.getColor(getContext(), inActiveColor);
        return this;
    }

    /**
     * @param inActiveColorCode color code in string format for the default in-active color
     * @return this, to allow builder pattern
     */
    public SmartNavigationBar setInActiveColor(String inActiveColorCode) {
        this.mInActiveColor = Color.parseColor(inActiveColorCode);
        return this;
    }

    /**
     * @param backgroundColor res code for the default background color
     * @return this, to allow builder pattern
     */
    public SmartNavigationBar setBarBackgroundColor(@ColorRes int backgroundColor) {
        this.mBackgroundColor = ContextCompat.getColor(getContext(), backgroundColor);
        return this;
    }

    /**
     * @param backgroundColorCode color code in string format for the default background color
     * @return this, to allow builder pattern
     */
    public SmartNavigationBar setBarBackgroundColor(String backgroundColorCode) {
        this.mBackgroundColor = Color.parseColor(backgroundColorCode);
        return this;
    }

    /**
     * @param firstSelectedPosition position of tab that needs to be selected by default
     * @return this, to allow builder pattern
     */
    public SmartNavigationBar setFirstSelectedPosition(int firstSelectedPosition) {
        this.mFirstSelectedPosition = firstSelectedPosition;
        return this;
    }

    /**
     * will be public once all bugs are ressolved.
     */
    public SmartNavigationBar setScrollable(boolean scrollable) {
        mScrollable = scrollable;
        return this;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Initialise Method
    ///////////////////////////////////////////////////////////////////////////

    /**
     * This method should be called at the end of all customisation method.
     * This method will take all changes in to consideration and redraws tabs.
     */
    public void initialise() {
        mSelectedPosition = DEFAULT_SELECTED_POSITION;
        mSmartNavigationTabs.clear();

        if (!mSmartNavigationItems.isEmpty()) {
            mTabContainer.removeAllViews();
            if (mMode == MODE_DEFAULT) {
                if (mSmartNavigationItems.size() <= MIN_SIZE) {
                    mMode = MODE_FIXED;
                } else {
                    mMode = MODE_SHIFTING;
                }
            }
            if (mBackgroundStyle == BACKGROUND_STYLE_DEFAULT) {
                if (mMode == MODE_FIXED) {
                    mBackgroundStyle = BACKGROUND_STYLE_STATIC;
                } else {
                    mBackgroundStyle = BACKGROUND_STYLE_RIPPLE;
                }
            }

            if (mBackgroundStyle == BACKGROUND_STYLE_STATIC) {
                mBackgroundOverlay.setVisibility(View.GONE);
                mContainer.setBackgroundColor(mBackgroundColor);
            }

            int screenWidth = Utils.getScreenWidth(getContext());

            if (mMode == MODE_FIXED) {

                int[] widths = SmartNavigationHelper.getMeasurementsForFixedMode(getContext(), screenWidth, mSmartNavigationItems.size(), mScrollable);
                int itemWidth = widths[0];

                for (SmartNavigationItem currentItem : mSmartNavigationItems) {
                    FixedSmartNavigationTab bottomNavigationTab = new FixedSmartNavigationTab(getContext());
                    setUpTab(bottomNavigationTab, currentItem, itemWidth, itemWidth);
                }

            } else if (mMode == MODE_SHIFTING) {

                int[] widths = SmartNavigationHelper.getMeasurementsForShiftingMode(getContext(), screenWidth, mSmartNavigationItems.size(), mScrollable);

                int itemWidth = widths[0];
                int itemActiveWidth = widths[1];

                for (SmartNavigationItem currentItem : mSmartNavigationItems) {
                    ShiftingSmartNavigationTab bottomNavigationTab = new ShiftingSmartNavigationTab(getContext());
                    setUpTab(bottomNavigationTab, currentItem, itemWidth, itemActiveWidth);
                }
            }

            if (mSmartNavigationTabs.size() > mFirstSelectedPosition) {
                selectTabInternal(mFirstSelectedPosition, true, false, false);
            } else if (!mSmartNavigationTabs.isEmpty()) {
                selectTabInternal(0, true, false, false);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Anytime Setter methods that can be called irrespective of whether we call initialise or not
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param tabSelectedListener callback listener for tabs
     * @return this, to allow builder pattern
     */
    public SmartNavigationBar setTabSelectedListener(OnTabSelectedListener tabSelectedListener) {
        this.mTabSelectedListener = tabSelectedListener;
        return this;
    }

    /**
     * ripple animation will be 2.5 times this animation duration.
     *
     * @param animationDuration animation duration for tab animations
     * @return this, to allow builder pattern
     */
    public SmartNavigationBar setAnimationDuration(int animationDuration) {
        this.mAnimationDuration = animationDuration;
        this.mRippleAnimationDuration = (int) (animationDuration * 2.5);
        return this;
    }

    /**
     * Clears all stored data and this helps to re-initialise tabs from scratch
     */
    public void clearAll() {
        mTabContainer.removeAllViews();
        mSmartNavigationTabs.clear();
        mSmartNavigationItems.clear();
        mBackgroundOverlay.setVisibility(View.GONE);
        mContainer.setBackgroundColor(Color.TRANSPARENT);
        mSelectedPosition = DEFAULT_SELECTED_POSITION;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Setter methods that should called only after initialise is called
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Should be called only after initialization of BottomBar(i.e after calling initialize method)
     *
     * @param newPosition to select a tab after bottom navigation bar is initialised
     */
    public void selectTab(int newPosition) {
        selectTab(newPosition, true);
    }

    /**
     * Should be called only after initialization of BottomBar(i.e after calling initialize method)
     *
     * @param newPosition  to select a tab after bottom navigation bar is initialised
     * @param callListener should this change call listener callbacks
     */
    public void selectTab(int newPosition, boolean callListener) {
        selectTabInternal(newPosition, false, callListener, callListener);
    }


    ///////////////////////////////////////////////////////////////////////////
    // Animating methods
    ///////////////////////////////////////////////////////////////////////////

    /**
     * show BottomNavigationBar if it is hidden and hide if it is shown
     */
    public void toggle() {
        toggle(true);
    }

    /**
     * show BottomNavigationBar if it is hidden and hide if it is shown
     *
     * @param animate is animation enabled for toggle
     */
    public void toggle(boolean animate) {
        if (mIsHidden) {
            show(animate);
        } else {
            hide(animate);
        }
    }

    /**
     * hide with animation
     */
    public void hide() {
        hide(true);
    }

    /**
     * @param animate is animation enabled for hide
     */
    public void hide(boolean animate) {
        mIsHidden = true;
        setTranslationY(this.getHeight(), animate);
    }

    /**
     * show with animation
     */
    public void show() {
        show(true);
    }

    /**
     * @param animate is animation enabled for show
     */
    public void show(boolean animate) {
        mIsHidden = false;
        setTranslationY(0, animate);
    }

    public boolean isHidden() {
        return mIsHidden;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Behaviour Handing Handling
    ///////////////////////////////////////////////////////////////////////////

    public boolean isAutoHideEnabled() {
        return mAutoHideEnabled;
    }

    public void setAutoHideEnabled(boolean mAutoHideEnabled) {
        this.mAutoHideEnabled = mAutoHideEnabled;
    }

    public void setFab(FloatingActionButton fab) {
        ViewGroup.LayoutParams layoutParams = fab.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams coLayoutParams = (CoordinatorLayout.LayoutParams) layoutParams;
            SmartNavBarFabBehaviour smartNavBarFabBehaviour = new SmartNavBarFabBehaviour();
            coLayoutParams.setBehavior(smartNavBarFabBehaviour);
        }
    }

    // scheduled for next
    private void setFab(FloatingActionButton fab, @FabBehaviour int fabBehaviour) {
        ViewGroup.LayoutParams layoutParams = fab.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof CoordinatorLayout.LayoutParams) {
            CoordinatorLayout.LayoutParams coLayoutParams = (CoordinatorLayout.LayoutParams) layoutParams;
            SmartNavBarFabBehaviour smartNavBarFabBehaviour = new SmartNavBarFabBehaviour();
            coLayoutParams.setBehavior(smartNavBarFabBehaviour);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // ＩＮＮＥＲ　ＣＬＡＳＳ
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Simple implementation of the OnTabSelectedListener interface with stub implementations of each method.
     * Extend this if you do not intend to override every method of OnTabSelectedListener.
     */
    public static class SimpleOnTabSelectedListener implements OnTabSelectedListener {
        @Override
        public void onTabSelected(int position) {
        }

        @Override
        public void onTabUnselected(int position) {
        }

        @Override
        public void onTabReselected(int position) {
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // Getters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @return activeColor
     */
    public int getActiveColor() {
        return mActiveColor;
    }

    /**
     * @return in-active color
     */
    public int getInActiveColor() {
        return mInActiveColor;
    }

    /**
     * @return background color
     */
    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    /**
     * @return current selected position
     */
    public int getCurrentSelectedPosition() {
        return mSelectedPosition;
    }

    /**
     * @return animation duration
     */
    public int getAnimationDuration() {
        return mAnimationDuration;
    }

    ///////////////////////////////////////////////////////////////////////////
    // ＰＲＩＶＡＴＥ
    ///////////////////////////////////////////////////////////////////////////

    /**
     * This method initiates the smartNavigationBar properties,
     * Tries to get them form XML if not preset sets them to their default values.
     *
     * @param context context of the bottomNavigationBar
     * @param attrs   attributes mentioned in the layout XML by user
     */
    private void parseAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SmartNavigationBar, 0, 0);

            mActiveColor = typedArray.getColor(R.styleable.SmartNavigationBar_carsmartActiveColor, Utils.fetchContextColor(context, R.attr.colorAccent));
            mInActiveColor = typedArray.getColor(R.styleable.SmartNavigationBar_carsmartInactiveColor, Color.LTGRAY);
            mBackgroundColor = typedArray.getColor(R.styleable.SmartNavigationBar_carsmartBackgroundColor, Color.WHITE);
            mAutoHideEnabled = typedArray.getBoolean(R.styleable.SmartNavigationBar_carsmartAutoHideEnabled, true);
            mElevation = typedArray.getDimension(R.styleable.SmartNavigationBar_carsmartElevation, getResources().getDimension(R.dimen.bottom_navigation_elevation));

            setAnimationDuration(typedArray.getInt(R.styleable.SmartNavigationBar_carsmartAnimationDuration, DEFAULT_ANIMATION_DURATION));

            switch (typedArray.getInt(R.styleable.SmartNavigationBar_carsmartMode, MODE_DEFAULT)) {
                case MODE_FIXED:
                    mMode = MODE_FIXED;
                    break;

                case MODE_SHIFTING:
                    mMode = MODE_SHIFTING;
                    break;

                case MODE_DEFAULT:
                default:
                    mMode = MODE_DEFAULT;
                    break;
            }

            switch (typedArray.getInt(R.styleable.SmartNavigationBar_carsmartBackgroundStyle, BACKGROUND_STYLE_DEFAULT)) {
                case BACKGROUND_STYLE_STATIC:
                    mBackgroundStyle = BACKGROUND_STYLE_STATIC;
                    break;

                case BACKGROUND_STYLE_RIPPLE:
                    mBackgroundStyle = BACKGROUND_STYLE_RIPPLE;
                    break;

                case BACKGROUND_STYLE_DEFAULT:
                default:
                    mBackgroundStyle = BACKGROUND_STYLE_DEFAULT;
                    break;
            }

            typedArray.recycle();
        } else {
            mActiveColor = Utils.fetchContextColor(context, R.attr.colorAccent);
            mInActiveColor = Color.LTGRAY;
            mBackgroundColor = Color.WHITE;
            mElevation = getResources().getDimension(R.dimen.bottom_navigation_elevation);
        }
    }

    /**
     * This method initiates the bottomNavigationBar and handles layout related values
     */
    private void init() {

//        MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(
//                  new ViewGroup.LayoutParams(
//                      ViewGroup.LayoutParams.MATCH_PARENT, 
//                      (int) getContext().getResources().getDimension(R.dimen.bottom_navigation_padded_height)));
//        marginParams.setMargins(0, (int) getContext().getResources().getDimension(R.dimen.bottom_navigation_top_margin_correction), 0, 0);

        setLayoutParams(new ViewGroup.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)));

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View parentView = inflater.inflate(R.layout.bottom_navigation_bar_container, this, true);
        mBackgroundOverlay = (FrameLayout) parentView.findViewById(R.id.bottom_navigation_bar_overLay);
        mContainer = (FrameLayout) parentView.findViewById(R.id.bottom_navigation_bar_container);
        mTabContainer = (LinearLayout) parentView.findViewById(R.id.bottom_navigation_bar_item_container);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.setOutlineProvider(ViewOutlineProvider.BOUNDS);
        } else {
            //to do
        }

        ViewCompat.setElevation(this, mElevation);
        setClipToPadding(false);
    }

    /**
     * Internal method to setup tabs
     *
     * @param bottomNavigationTab Tab item
     * @param currentItem         data structure for tab item
     * @param itemWidth           tab item in-active width
     * @param itemActiveWidth     tab item active width
     */
    private void setUpTab(SmartNavigationTab bottomNavigationTab, SmartNavigationItem currentItem, int itemWidth, int itemActiveWidth) {
        bottomNavigationTab.setInactiveWidth(itemWidth);
        bottomNavigationTab.setActiveWidth(itemActiveWidth);
        bottomNavigationTab.setPosition(mSmartNavigationItems.indexOf(currentItem));

        bottomNavigationTab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SmartNavigationTab bottomNavigationTabView = (SmartNavigationTab) v;
                selectTabInternal(bottomNavigationTabView.getPosition(), false, true, false);
            }
        });

        mSmartNavigationTabs.add(bottomNavigationTab);

        SmartNavigationHelper.bindTabWithData(currentItem, bottomNavigationTab, this);

        bottomNavigationTab.initialise(mBackgroundStyle == BACKGROUND_STYLE_STATIC);

        mTabContainer.addView(bottomNavigationTab);
    }

    /**
     * Internal Method to select a tab
     *
     * @param newPosition     to select a tab after bottom navigation bar is initialised
     * @param firstTab        if firstTab the no ripple animation will be done
     * @param callListener    is listener callbacks enabled for this change
     * @param forcedSelection if bottom navigation bar forced to select tab (in this case call on selected irrespective of previous state
     */
    private void selectTabInternal(int newPosition, boolean firstTab, boolean callListener, boolean forcedSelection) {
        int oldPosition = mSelectedPosition;
        if (mSelectedPosition != newPosition) {
            if (mBackgroundStyle == BACKGROUND_STYLE_STATIC) {
                if (mSelectedPosition != -1)
                    mSmartNavigationTabs.get(mSelectedPosition).unSelect(true, mAnimationDuration);
                mSmartNavigationTabs.get(newPosition).select(true, mAnimationDuration);
            } else if (mBackgroundStyle == BACKGROUND_STYLE_RIPPLE) {
                if (mSelectedPosition != -1)
                    mSmartNavigationTabs.get(mSelectedPosition).unSelect(false, mAnimationDuration);
                mSmartNavigationTabs.get(newPosition).select(false, mAnimationDuration);

                final SmartNavigationTab clickedView = mSmartNavigationTabs.get(newPosition);
                if (firstTab) {
                    // Running a ripple on the opening app won't be good so on firstTab this won't run.
                    mContainer.setBackgroundColor(clickedView.getActiveColor());
                    mBackgroundOverlay.setVisibility(View.GONE);
                } else {
                    mBackgroundOverlay.post(new Runnable() {
                        @Override
                        public void run() {
//                            try {
                            SmartNavigationHelper.setBackgroundWithRipple(clickedView, mContainer, mBackgroundOverlay, clickedView.getActiveColor(), mRippleAnimationDuration);
//                            } catch (Exception e) {
//                                mContainer.setBackgroundColor(clickedView.getActiveColor());
//                                mBackgroundOverlay.setVisibility(View.GONE);
//                            }
                        }
                    });
                }
            }
            mSelectedPosition = newPosition;
        }

        if (callListener) {
            sendListenerCall(oldPosition, newPosition, forcedSelection);
        }
    }

    /**
     * Internal method used to send callbacks to listener
     *
     * @param oldPosition     old selected tab position, -1 if this is first call
     * @param newPosition     newly selected tab position
     * @param forcedSelection if bottom navigation bar forced to select tab (in this case call on selected irrespective of previous state
     */
    private void sendListenerCall(int oldPosition, int newPosition, boolean forcedSelection) {
        if (mTabSelectedListener != null) {
//                && oldPosition != -1) {
            if (forcedSelection) {
                mTabSelectedListener.onTabSelected(newPosition);
            } else {
                if (oldPosition == newPosition) {
                    mTabSelectedListener.onTabReselected(newPosition);
                } else {
                    mTabSelectedListener.onTabSelected(newPosition);
                    if (oldPosition != -1) {
                        mTabSelectedListener.onTabUnselected(oldPosition);
                    }
                }
            }
        }
    }

    /**
     * @param offset  offset needs to be set
     * @param animate is animation enabled for translation
     */
    private void setTranslationY(int offset, boolean animate) {
        if (animate) {
            animateOffset(offset);
        } else {
            if (mTranslationAnimator != null) {
                mTranslationAnimator.cancel();
            }
            this.setTranslationY(offset);
        }
    }

    /**
     * Internal Method
     * <p>
     * used to set animation and
     * takes care of cancelling current animation
     * and sets duration and interpolator for animation
     *
     * @param offset translation offset that needs to set with animation
     */
    private void animateOffset(final int offset) {
        if (mTranslationAnimator == null) {
            mTranslationAnimator = ViewCompat.animate(this);
            mTranslationAnimator.setDuration(mRippleAnimationDuration);
            mTranslationAnimator.setInterpolator(INTERPOLATOR);
        } else {
            mTranslationAnimator.cancel();
        }
        mTranslationAnimator.translationY(offset).start();
    }
}
