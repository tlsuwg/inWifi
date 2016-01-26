package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp;

import android.content.Context;
import android.hxuehh.com.R;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;


/**
 * Created by yl on 15-5-25.
 */
public class LoadingView extends RelativeLayout {
    private Context mContext;
    private ImageView mPointImage1;
    private ImageView mPointImage2;
    private ImageView mPointImage3;
    private ImageView mPointImage4;
    private ImageView mPointImage5;
    private ImageView mTipText;
    private boolean isNeedStopAnimation;
    private boolean isDoingAnimation;

    public LoadingView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layer_load_view, this);

        mPointImage1 = (ImageView) findViewById(R.id.img_1);
        mPointImage2 = (ImageView) findViewById(R.id.img_2);
        mPointImage3 = (ImageView) findViewById(R.id.img_3);
        mPointImage4 = (ImageView) findViewById(R.id.img_4);
        mPointImage5 = (ImageView) findViewById(R.id.img_5);
        mTipText = (ImageView) findViewById(R.id.tip_text);
    }


    public void isShowLoading(boolean isShow){
        if(isShow){
            isNeedStopAnimation = false;
            if (!isDoingAnimation) {
                isDoingAnimation = true;
                doAnimation();
            }
        }else{
            isNeedStopAnimation = true;
            isDoingAnimation = false;
            if(mPointImage1 != null){
                mPointImage1.clearAnimation();
            }
            if(mPointImage2 != null){
                mPointImage2.clearAnimation();
            }
            if(mPointImage3 != null){
                mPointImage3.clearAnimation();
            }
            if(mPointImage4 != null){
                mPointImage4.clearAnimation();
            }
            if(mPointImage5 != null){
                mPointImage5.clearAnimation();
            }

            if(mTipText != null){
                mTipText.clearAnimation();
            }
        }

    }

    private Animation getAnimationResource() {
        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.load_view_rotate);
        /*AccelerateInterpolator Acc = new AccelerateInterpolator(1.2F);
        operatingAnim.setInterpolator(Acc);*/
        operatingAnim.setFillAfter(true);
        return operatingAnim;
    }

    private void doAnimation() {
        Animation alphaAnim = AnimationUtils.loadAnimation(mContext, R.anim.load_view_text_alpha_in);
        alphaAnim.setFillAfter(true);
        mTipText.startAnimation(alphaAnim);
        alphaAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation alphaAnim = AnimationUtils.loadAnimation(mContext, R.anim.load_view_text_alpha_out);
                mTipText.startAnimation(alphaAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        Animation rotateAnimation = getAnimationResource();
        mPointImage1.startAnimation(rotateAnimation);

        rotateAnimation = getAnimationResource();
        rotateAnimation.setStartOffset(100);
        mPointImage2.startAnimation(rotateAnimation);

        rotateAnimation = getAnimationResource();
        rotateAnimation.setStartOffset(200);
        mPointImage3.startAnimation(rotateAnimation);

        rotateAnimation = getAnimationResource();
        rotateAnimation.setStartOffset(300);
        mPointImage4.startAnimation(rotateAnimation);

        rotateAnimation = getAnimationResource();
        rotateAnimation.setStartOffset(400);
        mPointImage5.startAnimation(rotateAnimation);

        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(!isNeedStopAnimation){
                    doAnimation();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


    }
}
