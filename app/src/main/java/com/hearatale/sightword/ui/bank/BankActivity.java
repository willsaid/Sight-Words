package com.hearatale.sightword.ui.bank;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;
import com.hearatale.sightword.R;
import com.hearatale.sightword.data.Constants;
import com.hearatale.sightword.service.AudioPlayerHelper;
import com.hearatale.sightword.ui.base.activity.ActivityMVP;
import com.hearatale.sightword.utils.Config;
import com.hearatale.sightword.utils.ImageHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BankActivity extends ActivityMVP<BankPresenter, IBank> implements IBank, ConfettoGenerator {

    private static final int COINS_TRUCK = 125;
    private static final int COINS_BAG = 25;
    private static final int COINS_20 = 20;
    private static final int COINS_15 = 15;
    private static final int COINS_10 = 10;
    private static final int COINS_5 = 5;

    @BindView(R.id.layout_root)
    FrameLayout layoutRoot;

    @BindView(R.id.image_blur)
    ImageView imageBlur;

    @BindView(R.id.text_view_no_coin)
    TextView textViewNoCoins;

    @BindView(R.id.layout_available_coins)
    ConstraintLayout layoutAvailableCoins;

    @BindView(R.id.layout_for_trucks)
    LinearLayoutCompat layoutForTrucks;

    @BindView(R.id.layout_for_trucks_second)
    LinearLayoutCompat layoutForTrucksSecond;

    @BindView(R.id.layout_for_bags)
    LinearLayoutCompat layoutForBags;

    @BindView(R.id.layout_for_stacks)
    LinearLayoutCompat layoutForStacks;

    @BindView(R.id.layout_for_coins)
    LinearLayoutCompat layoutForCoins;

    private int mTotalGoldCount = 0;
    private int mTotalSilverCount = 0;
    private int mTotalGold = 0;
    private boolean mPlayCelebration = false;

    //For Spawn coins
    private final int COIN_SIZE = 80;
    private int velocitySlow, velocityNormal;
    private List<Bitmap> mCoinBitmaps = new ArrayList<>();
    ConfettiManager mConfettiManager;
    int timePauseAudio = 0;

    Handler mHandlerSchedule = new Handler();

    private String appFeature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        ButterKnife.bind(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getArgument();
        mTotalGoldCount = mPresenter.getTotalGoldCount(appFeature);
        mTotalSilverCount = mPresenter.getTotalSilverCount(appFeature);
        mTotalGold = mTotalGoldCount + mTotalSilverCount / 2;
        prepareData();
        initViews();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mTotalGold > 0 && mTotalGold % COINS_TRUCK == 0) {
            showCelebrate();
        }
    }

    private void showCelebrate() {
        mHandlerSchedule.postDelayed(new Runnable() {
            @Override
            public void run() {
                spawnCoins();
                AudioPlayerHelper.getInstance().playWithOffset(Config.SOUND_PATH + "sight words celebration", timePauseAudio);
            }
        }, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AudioPlayerHelper player = AudioPlayerHelper.getInstance();
        timePauseAudio = player.getCurrentPosition();
        player.stopPlayer();
    }

    private void prepareData() {
        Resources res = getResources();
        velocitySlow = res.getDimensionPixelOffset(R.dimen.default_velocity_normal);
        velocityNormal = res.getDimensionPixelOffset(R.dimen.default_velocity_fast);

        Bitmap bitmapGoldCoin = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.gold_coin_94),
                COIN_SIZE, COIN_SIZE, false);
        Bitmap bitmapSilverCoin = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, R.drawable.silver_coin_94),
                COIN_SIZE, COIN_SIZE, false);
        mCoinBitmaps.add(bitmapGoldCoin);
        mCoinBitmaps.add(bitmapSilverCoin);
    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new BankPresenter();
    }

    private void initViews() {

        float availableBalance = mTotalGoldCount + (float) (mTotalSilverCount * 0.5);

        textViewNoCoins.setVisibility(availableBalance == 0.0f ? View.VISIBLE : View.GONE);
        layoutAvailableCoins.setVisibility(availableBalance == 0.0f ? View.GONE : View.VISIBLE);

        makeSureRatioScreen();
        layoutAvailableCoins.post(new Runnable() {
            @Override
            public void run() {
                decorateCoins();
            }
        });

    }

    private void makeSureRatioScreen() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float ratioScreen = displayMetrics.widthPixels / displayMetrics.heightPixels;
        if (ratioScreen > (16 / 9)) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) layoutAvailableCoins.getLayoutParams();
            layoutParams.matchConstraintPercentWidth = 1 - (ratioScreen - 16 / 9);
            layoutAvailableCoins.setLayoutParams(layoutParams);
        }

    }

    private void decorateCoins() {
        int gold = mTotalGoldCount + mTotalSilverCount / 2;
        int silver = mTotalSilverCount % 2;

        //calculate coins
        int trucks = gold / COINS_TRUCK;
        if (trucks > 5) {
            addViewsForLayoutTruck(5, layoutForTrucks, R.mipmap.truck_coins, R.mipmap.red_truck);
            addViewsForLayoutTruck(trucks - 5, layoutForTrucksSecond, R.mipmap.red_truck, R.mipmap.truck_coins);
        } else {
            addViewsForLayoutTruck(trucks, layoutForTrucks, R.mipmap.truck_coins, R.mipmap.red_truck);
        }
        gold %= COINS_TRUCK;

        int bags = gold / COINS_BAG;
        addViewsForLayout(bags, layoutForBags, R.mipmap.bag_of_coins);
        gold %= COINS_BAG;

        int coin20 = gold / COINS_20;
        addViewsForLayout(coin20, layoutForStacks, R.mipmap.coin_20);
        gold %= COINS_20;

        int coin15 = gold / COINS_15;
        addViewsForLayout(coin15, layoutForStacks, R.mipmap.coin_15);
        gold %= COINS_15;

        int coin10 = gold / COINS_10;
        addViewsForLayout(coin10, layoutForStacks, R.mipmap.coin_10);
        gold %= COINS_10;

        int coin5 = gold / COINS_5;
        addViewsForLayout(coin5, layoutForStacks, R.mipmap.coin_5);
        gold %= COINS_5;

        int coinGold = gold;
        addViewsForLayout(coinGold, layoutForCoins, R.mipmap.coin_gold);

        if (silver == 1) {
            addViewsForLayout(1, layoutForCoins, R.mipmap.coin_silver);
        }

    }

    private void addViewsForLayout(final int size, final LinearLayoutCompat layout, @DrawableRes final int drawableResImage) {
        if (size <= 0) return;
        layout.setVisibility(View.VISIBLE);
        layout.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < size; i++) {
                    addView(layout, drawableResImage);
                }
            }
        });

    }

    private void addViewsForLayoutTruck(final int size, final LinearLayoutCompat layout,
                                        @DrawableRes final int drawableResImage1,
                                        @DrawableRes final int drawableResImage2) {
        if (size <= 0) return;
        layout.setVisibility(View.VISIBLE);
        layout.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < size; i++) {
                    if (i % 2 == 0) {
                        addView(layout, drawableResImage1);
                    } else {
                        addView(layout, drawableResImage2);
                    }

                }
            }
        });

    }


    private void addView(LinearLayoutCompat layout, @DrawableRes int drawableResImage) {

        ImageView imageView = new ImageView(BankActivity.this);
        LinearLayoutCompat.LayoutParams params =
                new LinearLayoutCompat.LayoutParams(layout.getWidth() / 5,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        if (drawableResImage == R.mipmap.coin_5) {
            int padding = getResources().getDimensionPixelOffset(R.dimen.dp_8);
            imageView.setPadding(padding, padding, padding, padding);
        }

        imageView.setImageResource(drawableResImage);
        layout.addView(imageView);
    }


    private void getArgument() {
        Intent intent = getIntent();
        if (intent == null) return;
        mPlayCelebration = intent.getBooleanExtra(Constants.Arguments.ARG_PLAY_CELEBRATION, false);
        Bitmap blurBitmap = getIntent().getParcelableExtra(Constants.Arguments.ARG_BLUR_BITMAP);

        if (null != blurBitmap) {
            setTheme(R.style.Theme_Transparent);
            ImageHelper.blurImageView(blurBitmap, imageBlur);
        }

        appFeature = getIntent().getStringExtra("APP_FEATURE");

    }

    @OnClick(R.id.image_view_back)
    void back() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        setResult(Constants.ResultCode.SETUP_NEW_WORD_CODE);
        super.onBackPressed();
    }

    private void spawnCoins() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ConfettiSource source = new ConfettiSource(0, -COIN_SIZE, displayMetrics.widthPixels, -COIN_SIZE);
        int totalGoldSilver = mTotalGoldCount > 100 ? 100 : mTotalGoldCount;
        float emissionRate = (float) totalGoldSilver * ((float) 3 / 4);
        mConfettiManager = new ConfettiManager(this, this, source, layoutRoot)
                .setVelocityX(0, 0)
                .setVelocityY(velocityNormal, velocitySlow)
                .setNumInitialCount(0)
                .setEmissionDuration(ConfettiManager.INFINITE_DURATION)
                .setEmissionRate(emissionRate)
                .animate();
    }

    @Override
    protected void onStop() {
        mHandlerSchedule.removeCallbacksAndMessages(null);
        if (mConfettiManager != null) {
            mConfettiManager.terminate();
            mConfettiManager = null;
        }
        super.onStop();
    }

    @Override
    public Confetto generateConfetto(Random random) {
        Bitmap bitmap = mCoinBitmaps.get(random.nextInt(mCoinBitmaps.size()));
        return new BitmapConfetto(bitmap);
    }


    //    private void spawnCoins() {
//        int totalGoldSilver = mTotalGoldCount > 70 ? 70 : mTotalGoldCount;
//        final DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        final int endY = displayMetrics.heightPixels + COIN_SIZE * 2;
//
//        for (int i = 0; i < totalGoldSilver; i++) {
//            int waitGold = mRandom.nextInt(1000);
//            int waitSilver = mRandom.nextInt(2000);
//
//            mHandlerSchedule.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    spawnCoin(displayMetrics, endY, true);
//                }
//            }, waitGold);
//
//            mHandlerSchedule.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    spawnCoin(displayMetrics, endY, false);
//                }
//            }, waitSilver);
//
//        }
//
//    }
//
//    private void spawnCoin(final DisplayMetrics displayMetrics, final int endY, final boolean isGold) {
//        final ImageView imageViewCoin = getImageView(displayMetrics, isGold ? R.drawable.gold_coin_94 : R.drawable.silver_coin_94);
//        layoutRoot.addView(imageViewCoin);
//
//        int duration = 2000 + ((mRandom.nextInt(1000) / 250) * 1000);
//        imageViewCoin.animate().translationY(endY).setDuration(duration).withEndAction(new Runnable() {
//            @Override
//            public void run() {
//                layoutRoot.removeView(imageViewCoin);
//                spawnCoin(displayMetrics, endY, isGold);
//            }
//        });
//    }
//
//    @NonNull
//    private ImageView getImageView(DisplayMetrics displayMetrics, @DrawableRes int imageRes) {
//        int startX = mRandom.nextInt(displayMetrics.widthPixels);
//        final ImageView imageViewCoin = new ImageView(BankActivity.this);
//        FrameLayout.LayoutParams layoutParams = getLayoutParams(startX);
//        imageViewCoin.setLayoutParams(layoutParams);
//        imageViewCoin.setImageResource(imageRes);
//        return imageViewCoin;
//    }
//
//
//    @NonNull
//    private FrameLayout.LayoutParams getLayoutParams(int marginStar) {
//        FrameLayout.LayoutParams layoutParams =
//                new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.topMargin = -COIN_SIZE;
//        layoutParams.width = COIN_SIZE;
//        layoutParams.height = COIN_SIZE;
//        layoutParams.setMarginStart(marginStar);
//        return layoutParams;
//    }

}
