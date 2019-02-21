package com.hearatale.sightword.ui.secret_stuff;

import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hearatale.sightword.R;
import com.hearatale.sightword.service.AudioPlayerHelper;
import com.hearatale.sightword.ui.base.activity.ActivityMVP;
import com.hearatale.sightword.utils.Config;
import com.hearatale.sightword.utils.FontsHelper;
import com.transitionseverywhere.ChangeBounds;
import com.transitionseverywhere.TransitionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SecretStuffActivity extends ActivityMVP<SecretStuffPresenter, ISecretStuff> implements ISecretStuff {

    //content image
    @BindView(R.id.image_view_center_view)
    ImageView mainImage;


    //layout content text
    @BindView(R.id.sub_layout_centerview)
    LinearLayout linearLayout;


    Handler mHandler = new Handler();


    // data time from xcode convert to ms :
    private static final long[] TIME_ARRAY = {
            0,
            6000,
            12500,
            22150,
            24610,
            28000,
            33360,
            39110,
            43200,
            47930,
            52540,
            54300,
            56770,
            61260,
            66680,
            70490,
            71880,
            77150,
            81400,
            86270,
            96190,
            97980,
            101640,
            103500,
            106700,
            108090,
            110950,
            112890,
            116000,
            117610,
            120280,
            122190,
            125300,
            126690,
            129510,
            131530,
            134090,
            135330,
            138450,
            140060,
            142110,
            143460,
            144520,
            145620,
            146900,
            148150,
            150050,
            152400,
            153570,
            154700,
            155770,
            156970,
            158260,
            159350,
            161700,
            163620,
            185020,
            188100,
            189340,
            192200,
            193570,
            198500,
            204200,
            211390,
            213980,
            217060,
            220060,
            223140,
            226650,
            230320,
            233170,
            236170,
            239210,
            242070,
            243420,
            245000,
            246320,
            251460,
            265610,
            267770,
            269350,
            272420,
            274420,
            276420,
            278420,
            279490
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_stuff);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        runAudio();

    }


    private void createAnimationFadeIn(View view) {
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(600);
        view.startAnimation(fadeIn);
    }

    private void startAnimationTransfromRightToLeft(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(400);
        view.startAnimation(anim);
    }


    private void increaseWidth(final View view) {
        final int dimension32 = getResources().getDimensionPixelOffset(R.dimen.dp_32) * 2;

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.getLayoutParams().width = (int) (dimension32 * interpolatedTime);
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };


        // 1dp/10ms
        a.setDuration((int) (10 * dimension32 / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(a);
    }

    private void increaseLeftMargin(final View view) {
        final int dimension32 = getResources().getDimensionPixelOffset(R.dimen.dp_32);

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                ((LinearLayout.LayoutParams) view.getLayoutParams()).leftMargin = (int) (dimension32 * interpolatedTime);
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/10ms
        a.setDuration((int) (10 * dimension32 / view.getContext().getResources().getDisplayMetrics().density));
        view.startAnimation(a);
    }

    //english Mode
    private void english(String header, String footer) {

        linearLayout.removeAllViews();


        TextView textViewOne = createTexView("");
        TextView textViewTwo = createTexView(header);
        TextView textViewThree = createTexView(footer);
        TextView textViewFour = createTexView("");


        linearLayout.addView(textViewOne);
        linearLayout.addView(textViewTwo);
        linearLayout.addView(textViewThree);
        linearLayout.addView(textViewFour);


    }

    // prefix Mode

    /**
     * textView1 : setBackground = shape custom in xml
     * spacing = 64dp
     */
    private void prefix() {
        TextView textViewOne = (TextView) linearLayout.getChildAt(1);

        textViewOne.setWidth(getResources().getDimensionPixelSize(R.dimen.dp_30));
        textViewOne.setBackground(getResources().getDrawable(R.drawable.shape_text_secret_stuff));
        textViewOne.setGravity(Gravity.CENTER_HORIZONTAL);


        TextView textViewThree = (TextView) linearLayout.getChildAt(2);
        //spacing
        increaseLeftMargin(textViewThree);
    }

    //partialConstruction Mode

    /**
     * jump TextView 1 of linearLayout to : endX,endY
     */
    private void partialConstruction() {

        TextView textViewTwo = (TextView) linearLayout.getChildAt(1);

        float startX = textViewTwo.getX();
        float startY = textViewTwo.getY();


        TextView textViewThree = (TextView) linearLayout.getChildAt(2);
        int dimension30 = getResources().getDimensionPixelOffset(R.dimen.dp_30);

        /**
         * textView_three : with = 60dp
         * calculator endX, endY :
         * 12 is magic number
         */
        float endX = startX + dimension30 * 2 + dimension30 / 2 - 12 + textViewThree.getWidth();
        float endY = startY;

        /**
         * create path bezier with startX, startY, endX, endY,midpointX,midpointY
         */
        float midpointX = (endX + startX) / 2;
        float midpointY = startY - 200;
        /**
         *
         *           (midpoint)*
         *
         * (start) *                       *(end)
         */

        Path path = new Path();
        path.moveTo(startX, startY);
        path.quadTo(midpointX, midpointY, endX, endY);

        /**
         * create animation
         */
        ObjectAnimator animator = ObjectAnimator.ofFloat(textViewTwo, View.X, View.Y, path);
        animator.setDuration(700);
        animator.start();
        TextView textViewFour = (TextView) linearLayout.getChildAt(3);
        textViewFour.setWidth(1);//if no setWidth it's not working
        //width + 64dp
        increaseWidth(textViewFour);

    }

    //TODO: transition
    private void createTransition() {

        TextView textViewD = (TextView) linearLayout.getChildAt(1);//ex : "d" || "c"
        String textD = (String) textViewD.getText();

        TextView textViewOG = (TextView) linearLayout.getChildAt(2);//ex : "og" || "at"
        String textOG = (String) textViewOG.getText();


        TextView textViewSymbol = (TextView) linearLayout.getChildAt(3);//ex : "-"
        String textSymbol = (String) textViewSymbol.getText();

        TransitionManager.setTransitionName(textViewD, textD);
        TransitionManager.setTransitionName(textViewOG, textOG);
        TransitionManager.setTransitionName(textViewSymbol, textSymbol);

    }

    // Pulse Mode
    private void pulse() {
        //animation scale
        scaleView(linearLayout.getChildAt(2));
        //TODO: add transition to "og","-","d" ("at","-","c" )
        createTransition();
        //-----------
    }


    //fullConstruction Mode

    /**
     * textview2 : setbackgroundColor = white
     * add :textview5 = "-", textview6 = "ay"
     * start animation alpha and AnimationTransfromRightToLeft
     */
    private void fullConstruction() {
        //make this is smooth. ex : og - d - ay (at - c - ay)
        TransitionManager.beginDelayedTransition(linearLayout, new ChangeBounds());

        //-----------
        TextView textViewTwo = (TextView) linearLayout.getChildAt(1);
        textViewTwo.setBackgroundColor(Color.WHITE);


        TextView textViewFour = (TextView) linearLayout.getChildAt(3);
        textViewFour.setText(" -");
        createAnimationFadeIn(textViewFour);


        TextView textViewFive = createTexView("-");
        textViewFive.setWidth(getResources().getDimensionPixelSize(R.dimen.dp_30));
        textViewFive.setVisibility(View.INVISIBLE);
        textViewFive.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(textViewFive);
        textViewFive.setVisibility(View.VISIBLE);
        startAnimationTransfromRightToLeft(textViewFive);


        TextView textViewSix = createTexView("ay");
        textViewSix.setVisibility(View.INVISIBLE);
        textViewSix.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(textViewSix);
        textViewSix.setVisibility(View.VISIBLE);
        startAnimationTransfromRightToLeft(textViewSix);
    }

    //pigLatin Mode
    private void pigLatin() {

        //collapse textview 5 of linearLayuot :"-"
        collapse(linearLayout.getChildAt(4));
    }

    //sideBySide Mode
    private void sideBySide(String s) {
        TextView textViewOne = (TextView) linearLayout.getChildAt(0);
        textViewOne.setText(s);
        startAnimationTransfromRightToLeft(textViewOne);
    }


    //create textView with textSize = 40 px
    private TextView createTexView(String s) {
        TextView textView = new TextView(this);
        textView.setText(s);
        textView.setTextSize(40f);
        textView.setTypeface(FontsHelper.getComicSans(this));
        textView.setTextColor(Color.BLACK);
        return textView;
    }

    //create textView with textSize = 80 px
    private TextView createTexViewAsNumber(String s) {
        TextView textView = new TextView(this);
        textView.setText(s);
        textView.setTextSize(80f);
        textView.setTypeface(FontsHelper.getComicSans(this));
        textView.setTextColor(Color.BLACK);
        return textView;
    }


    //play "pig latin content.mp3" delay 1000ms becaus onCreate of SecretStuffActivity run before onDestroy of Splash
    private void runAudio() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playAudio("pig latin content");
                scriptWork();
            }
        }, 1000);

    }

    //show text
    private void text(String s) {
        linearLayout.removeAllViews();
        linearLayout.addView(createTexView(s));
    }

    //show number
    private void number(String s) {
        linearLayout.removeAllViews();


        TextView textView = createTexViewAsNumber(s);
        linearLayout.addView(textView);

    }

    // textview with animation rotate
    private void funText(String s) {
        linearLayout.removeAllViews();
        linearLayout.addView(createTexView(s));

        TextView textView = (TextView) linearLayout.getChildAt(0);
        textView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate));
    }

    /**
     * 0->2 : image
     * 3->9 : example of "DOG"
     * 10->16 : example of "CAT"
     * 17 -> 60 : text
     * 61->71 : number
     * 72->77 : text
     * 78 ->84 : funText
     */
    private void scriptWork() {


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                linearLayout.setVisibility(View.INVISIBLE);
                mainImage.setImageResource(R.mipmap.secret_stuff_logo);
            }
        }, TIME_ARRAY[0]);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainImage.setImageResource(R.mipmap.grouped_pig_latin);
            }
        }, TIME_ARRAY[1]);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mainImage.setImageResource(R.mipmap.nobodys_secret_stuff_kids_8_kids_preview);
            }
        }, TIME_ARRAY[2]);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                linearLayout.setVisibility(View.VISIBLE);
                mainImage.setVisibility(View.INVISIBLE);

                english("d", "og");
            }
        }, TIME_ARRAY[3]);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                prefix();
            }
        }, TIME_ARRAY[4]);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                partialConstruction();
            }
        }, TIME_ARRAY[5]);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pulse();

            }
        }, TIME_ARRAY[6]);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fullConstruction();

            }
        }, TIME_ARRAY[7]);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pigLatin();

            }
        }, TIME_ARRAY[8]);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sideBySide("dog");

            }
        }, TIME_ARRAY[9]);
        //chua lam gi ca
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                english("c", "at");
            }
        }, TIME_ARRAY[10]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                prefix();

            }
        }, TIME_ARRAY[11]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                partialConstruction();
            }
        }, TIME_ARRAY[12]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pulse();

            }
        }, TIME_ARRAY[13]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fullConstruction();

            }
        }, TIME_ARRAY[14]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pigLatin();

            }
        }, TIME_ARRAY[15]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                sideBySide("cat");
            }
        }, TIME_ARRAY[16]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("boy   oy - bay");

            }
        }, TIME_ARRAY[17]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //sideBySide("g", "irl", "ay");
                text("girl   irl - gay");

            }
        }, TIME_ARRAY[18]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("mother");

            }
        }, TIME_ARRAY[19]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("other-may");

            }
        }, TIME_ARRAY[20]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("mother");

            }
        }, TIME_ARRAY[21]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("other-may");

            }
        }, TIME_ARRAY[22]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("father");

            }
        }, TIME_ARRAY[23]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("ather-fay");
            }
        }, TIME_ARRAY[24]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("father");
            }
        }, TIME_ARRAY[25]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("ather-fay");

            }
        }, TIME_ARRAY[26]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("teacher");

            }
        }, TIME_ARRAY[27]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("eacher-tay");

            }
        }, TIME_ARRAY[28]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("teacher");
            }
        }, TIME_ARRAY[29]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("eacher-tay");

            }
        }, TIME_ARRAY[30]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("school");

            }
        }, TIME_ARRAY[31]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("ool-schay");

            }
        }, TIME_ARRAY[32]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("school");
            }
        }, TIME_ARRAY[33]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("ool-schay");

            }
        }, TIME_ARRAY[34]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("brother");

            }
        }, TIME_ARRAY[35]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("other-bray");
            }
        }, TIME_ARRAY[36]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("sister");

            }
        }, TIME_ARRAY[37]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("ister-say");

            }
        }, TIME_ARRAY[38]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("ood-gay ob-jay!");

            }
        }, TIME_ARRAY[39]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("Good job!");
            }
        }, TIME_ARRAY[40]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("an-cay");
            }
        }, TIME_ARRAY[41]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("an-cay ou-yay");
            }
        }, TIME_ARRAY[42]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("an-cay ou-yay alk-tay");

            }
        }, TIME_ARRAY[43]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("an-cay ou-yay alk-tay ig-pay");

            }
        }, TIME_ARRAY[44]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("an-cay ou-yay alk-tay ig-pay atin-lay?");
            }
        }, TIME_ARRAY[45]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("Can you talk pig latin?");
            }
        }, TIME_ARRAY[46]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("es-yay");

            }
        }, TIME_ARRAY[47]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("es-yay ou-yay");
            }
        }, TIME_ARRAY[48]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("es-yay ou-yay an-can");

            }
        }, TIME_ARRAY[49]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("es-yay ou-yay an-cay alk-tay");

            }
        }, TIME_ARRAY[50]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("es-yay ou-yay an-cay alk-tay ig-pay");

            }
        }, TIME_ARRAY[51]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("es-yay ou-yay an-cay alk-tay ig-pay atin-lay!");
            }
        }, TIME_ARRAY[51]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("ood-gay ob-jay!");

            }
        }, TIME_ARRAY[53]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("Good job!");

            }
        }, TIME_ARRAY[54]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("o    u    a    e    i");

            }
        }, TIME_ARRAY[55]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("is");

            }
        }, TIME_ARRAY[56]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("is-ay");

            }
        }, TIME_ARRAY[57]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("am");
            }
        }, TIME_ARRAY[58]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("am-ay");

            }
        }, TIME_ARRAY[59]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("I am a good talker.");
            }
        }, TIME_ARRAY[60]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("I-ay am-ay a-ay ood-gay alker-tay.");

            }
        }, TIME_ARRAY[61]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                number("1    2    3");

            }
        }, TIME_ARRAY[62]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                number("1");
            }
        }, TIME_ARRAY[63]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                number("2");

            }
        }, TIME_ARRAY[64]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                number("3");
            }
        }, TIME_ARRAY[65]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                number("4");
            }
        }, TIME_ARRAY[66]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                number("5");
            }
        }, TIME_ARRAY[67]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                number("6");

            }
        }, TIME_ARRAY[68]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                number("7");

            }
        }, TIME_ARRAY[69]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                number("8");
            }
        }, TIME_ARRAY[70]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                number("9");

            }
        }, TIME_ARRAY[71]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                number("10");

            }
        }, TIME_ARRAY[72]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("ood-gay!");

            }
        }, TIME_ARRAY[73]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("errific-tay!");

            }
        }, TIME_ARRAY[74]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                text("onderful-way!");
            }
        }, TIME_ARRAY[75]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("ou-yay an-cay alk-tay ig-pay atin-lay!");

            }
        }, TIME_ARRAY[76]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text("You can talk in pig latin!");

            }
        }, TIME_ARRAY[77]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                funText("ood-gay uck-lay!");

            }
        }, TIME_ARRAY[78]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                funText("Good luck!");
            }
        }, TIME_ARRAY[79]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                funText("And-ay ave-hay un-fay!");

            }
        }, TIME_ARRAY[80]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                funText("🎉  🎉  🎉");
            }
        }, TIME_ARRAY[81]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                funText("🎉  🎉  🎉");

            }
        }, TIME_ARRAY[82]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                funText("🎉  🎉  🎉");

            }
        }, TIME_ARRAY[83]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                funText("🎉  🎉  🎉");

            }
        }, TIME_ARRAY[84]);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();

            }
        }, TIME_ARRAY[85]);


    }

    public void scaleView(View view) {
        Animation zoomInAnimation = new ScaleAnimation(
                1f, 1.2f, // Start and end values for the X axis scaling
                1f, 1.2f, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        zoomInAnimation.setFillAfter(true); // Needed to keep the result of the animation
        zoomInAnimation.setDuration(500);

        Animation zoomOutAnimation = new ScaleAnimation(1f, 1f / 1.2f, 1f, 1f / 1.2f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        int startOffset = 500;
        zoomOutAnimation.setDuration(500);
        zoomOutAnimation.setStartOffset(startOffset);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.setInterpolator(new DecelerateInterpolator());
        animationSet.addAnimation(zoomInAnimation);
        animationSet.addAnimation(zoomOutAnimation);

        view.startAnimation(animationSet);

    }


    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new SecretStuffPresenter();
    }


    // scale width of view --> 1 and gone view.
    public void collapse(final View v) {
        final int initialWidth = v.getMeasuredWidth();
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().width = initialWidth - (int) (initialWidth * interpolatedTime);
                    v.getLayoutParams().height = initialHeight;
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/10ms
        a.setDuration((int) (10 * initialWidth / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }


    private void playAudio(String fileName) {
        String path = Config.SOUND_PATH + fileName;
        AudioPlayerHelper.getInstance().playAudio(path);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    //click icon Home
    @OnClick(R.id.image_view_home)
    void backToHome() {
        finish();
    }

    /**
     * onDestroy with stopPlayer + remove callback
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacksAndMessages(null);
        AudioPlayerHelper.getInstance().stopPlayer();
    }
}
