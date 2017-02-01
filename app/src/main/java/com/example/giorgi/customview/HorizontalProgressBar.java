package com.example.giorgi.customview;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by GIorgi on 12/23/2016.
 */

public class HorizontalProgressBar extends LinearLayout {

    protected final static float DEFAULT_MAX_PROGRESS = 100f;
    protected final static float DEFAULT_PROGRESS = 30f;
    protected final static float DEFAULT_PROGRESS_RADIUS = 0;
    protected final static float DEFAULT_BACKGROUND_PADDING = 0;

    GradientDrawable backgroundDrawable;
    GradientDrawable progreeColor;

    LinearLayout backgroundLinearLayout;
    LinearLayout progressLinear;
    LinearLayout.LayoutParams backgroundParams;
    LinearLayout.LayoutParams progressParams;

    private int radius;
    private int padding;
    private float max;
    private float progress;
    private int colorBackground;
    private int colorProgress;
    private boolean isReverse;
    private int parentHeight;
    private int parentWidth;
    private int test;
    private ImageView slideImage;
    private ImageView backgroundImage;
    private boolean click=false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupStyleable(context, attrs);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupStyleable(context, attrs);
    }


    //custom view simaghle da sigane
    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);

        progressParams.width = (int) ((parentWidth / DEFAULT_MAX_PROGRESS) * progress);
        progressParams.width = (int) ((parentWidth / 2)-dp2px(padding));
        progressParams.height = (int) (parentHeight-(dp2px(padding)*2));
        progressParams.gravity = Gravity.CENTER_VERTICAL;

        slideImage.setLayoutParams(new LinearLayout.LayoutParams(parentHeight/2,parentHeight/2));

        test=parentWidth-(parentWidth / 2);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //region
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setupStyleable(Context context, AttributeSet attrs) {

        slideImage =new ImageView(context);
        slideImage.setImageResource(R.drawable.btn_selected);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerProgress);

        radius = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_Radius, DEFAULT_PROGRESS_RADIUS);
        padding = (int) typedArray.getDimension(R.styleable.RoundCornerProgress_BackgroundPadding, dp2px(DEFAULT_BACKGROUND_PADDING));
        isReverse = typedArray.getBoolean(R.styleable.RoundCornerProgress_rcReverse, false);
        max = typedArray.getFloat(R.styleable.RoundCornerProgress_rcMax, DEFAULT_MAX_PROGRESS);
        progress = typedArray.getFloat(R.styleable.RoundCornerProgress_Progress, DEFAULT_PROGRESS);

        int colorBackgroundDefault = context.getResources().getColor(R.color.colorAccent);
        colorBackground = typedArray.getColor(R.styleable.RoundCornerProgress_BackgroundColor, colorBackgroundDefault);
        int colorProgressDefault = context.getResources().getColor(R.color.colorPrimary);
        colorProgress = typedArray.getColor(R.styleable.RoundCornerProgress_ProgressColor, colorProgressDefault);
        typedArray.recycle();

        init(context);
    }
//endregiond

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void init(final Context context) {

        backgroundDrawable = createGradientDrawable(colorBackground);
        progreeColor = createGradientDrawable(colorProgress);

        backgroundLinearLayout = new LinearLayout(context);
        progressLinear = new LinearLayout(context);

        backgroundParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        progressParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        progressParams.setMargins((int) dp2px(padding), 0, 0, 0);
        progressLinear.setGravity(Gravity.CENTER_VERTICAL);
        progressLinear.setPadding(20,0,0,0);

        backgroundLinearLayout.setLayoutParams(backgroundParams);
        progressLinear.setLayoutParams(progressParams);

        backgroundLinearLayout.setGravity(Gravity.LEFT);

        backgroundLinearLayout.setBackground(backgroundDrawable);
        progressLinear.setBackground(progreeColor);

        //progressLinear.addView(slideImage);

        backgroundLinearLayout.addView(progressLinear);
        //backgroundDrawable.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});
        backgroundDrawable.setCornerRadius(radius);
        progreeColor.setCornerRadii(new float[]{radius, radius, radius, radius, radius, radius, radius, radius});//cotati naklebi unda iyos progresis kutxe
        //progreeColor.setCornerRadius(radius);

        addView(backgroundLinearLayout);
        backgroundLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!click) {
                    click = true;
                    Toast.makeText(context, "click_right", Toast.LENGTH_SHORT).show();
                    final ObjectAnimator oa = ObjectAnimator.ofFloat(progressLinear, "x", test);
                    oa.setDuration(200);
                    oa.start();
                }else {
                    click = false;
                    Toast.makeText(context, "click_left", Toast.LENGTH_SHORT).show();
                    final ObjectAnimator oa = ObjectAnimator.ofFloat(progressLinear, "x", dp2px(padding));
                    oa.setDuration(200);
                    oa.start();
                }
            }
        });
        progressLinear.setOnTouchListener(new OnSwipeTouchListener() {
            public void onSwipeTop() {
                Toast.makeText(context, "top", Toast.LENGTH_SHORT).show();
            }

            public void onSwipeRight() {
                
                Toast.makeText(context, "right", Toast.LENGTH_SHORT).show();
                final ObjectAnimator oa = ObjectAnimator.ofFloat(progressLinear, "x", test);
                oa.setDuration(200);
                oa.start();
            }

            public void onSwipeLeft() {
                Toast.makeText(context, "left", Toast.LENGTH_SHORT).show();
                final ObjectAnimator oa = ObjectAnimator.ofFloat(progressLinear, "x", dp2px(padding));
                oa.setDuration(200);
                oa.start();
            }

            public void onSwipeBottom() {
                Toast.makeText(context, "bottom", Toast.LENGTH_SHORT).show();
            }

            });
        }

    protected GradientDrawable createGradientDrawable(int color) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);

        gradientDrawable.setColor(color);
        return gradientDrawable;
    }

    protected float dp2px(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
