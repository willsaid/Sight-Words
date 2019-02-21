package com.hearatale.sightword.ui.quiz_puzzle.content;

import com.hearatale.sightword.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hearatale.sightword.R;
import com.hearatale.sightword.data.AppDataManager;
import com.hearatale.sightword.data.Constants;
import com.hearatale.sightword.data.model.event.ProgressPuzzleEvent;
import com.hearatale.sightword.data.model.phonics.letters.LetterModel;
import com.hearatale.sightword.data.model.phonics.letters.PuzzlePieceModel;
import com.hearatale.sightword.data.model.phonics.letters.TimedAudioInfoModel;
import com.hearatale.sightword.data.model.phonics.piece.DataPieceModel;
import com.hearatale.sightword.data.model.phonics.piece.NubModel;
import com.hearatale.sightword.data.model.typedef.NubDef;
import com.hearatale.sightword.data.network.api.base.async.BackgroundTask;
import com.hearatale.sightword.service.AudioPlayerHelper;
import com.hearatale.sightword.ui.custom_view.PHPuzzleListener;
import com.hearatale.sightword.ui.idiom.IdiomActivity;
import com.hearatale.sightword.ui.quiz_puzzle.QuizPuzzleActivity;
import com.hearatale.sightword.utils.Config;
import com.hearatale.sightword.utils.DebugLog;
import com.hearatale.sightword.utils.Helper;
import com.hearatale.sightword.utils.ImageHelper;
import com.hearatale.sightword.utils.glide.GlideApp;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PuzzleLetterFragment extends Fragment {

    private static final String ARG_LETTER = "ARG_LETTER";
    private static final String ARG_PUZZLE_RANDOM = "ARG_PUZZLE_RANDOM";
    private static final String ARG_PUZZLE_COLOR = "ARG_PUZZLE_COLOR";
    private static final int NUB_PERCENT = 5; // 20%

    @BindView(R.id.layout_puzzle)
    RelativeLayout layoutPuzzle;

    @BindView(R.id.layout_puzzle_fake)
    RelativeLayout layoutPuzzleFake;

    @BindView(R.id.image_puzzle_completed)
    ImageView imagePuzzleCompleted;

    @BindView(R.id.text_title)
    TextView textTitle;


    @IdRes
    int mPuzzleColor;

    private String mDisplayLetter = "";
    private LetterModel mLetter = new LetterModel();
    private boolean mPuzzleRandom = false;
    private Gson mGson;
    private String imagePuzzlePath = "Images/Puzzles/";
    private String dirPuzzle = "puzzle-";
    private DataPieceModel dataPiece = new DataPieceModel();
    private Map<String, NubModel> mapNub = new HashMap<>();
    private List<PuzzlePieceModel> mCompletedPuzzlePiece = new ArrayList<>();
    private Queue<PuzzlePieceModel> mQueue = new LinkedBlockingQueue<>();
    private List<PuzzlePieceModel> mData = new ArrayList<>();
    private boolean isShowIdiomActivity = false;

    private PuzzleLetterListener mListener;

    private PuzzleLetterLifeCycleListener mLifeCycleListener;

    public PuzzleLetterFragment() {
        // Required empty public constructor
    }


    public static PuzzleLetterFragment newInstance(LetterModel letter, boolean puzzleRandom, int puzzleColor) {
        PuzzleLetterFragment fragment = new PuzzleLetterFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_LETTER, letter);
        args.putBoolean(ARG_PUZZLE_RANDOM, puzzleRandom);
        args.putInt(ARG_PUZZLE_COLOR, puzzleColor);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(PuzzleLetterListener listener) {
        this.mListener = listener;
    }

    public void setLifeCycleListener(PuzzleLetterLifeCycleListener lifeCycleListener) {
        this.mLifeCycleListener = lifeCycleListener;
    }

    public TextView getTextTitle() {
        return textTitle;
    }

    private QuizPuzzleActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof QuizPuzzleActivity) {
            mActivity = (QuizPuzzleActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLetter = getArguments().getParcelable(ARG_LETTER);
            mPuzzleRandom = getArguments().getBoolean(ARG_PUZZLE_RANDOM, false);
            mPuzzleColor = getArguments().getInt(ARG_PUZZLE_COLOR, getResources().getColor(R.color.blue_ez));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_puzzle_letter, container, false);
        ButterKnife.bind(this, rootView);

        initViews();

        initControls();


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (null != mLifeCycleListener) {
            mLifeCycleListener.onViewCreated();
        }
    }


    @Override
    public void onDestroyView() {
        if (null != mLifeCycleListener) {
            mLifeCycleListener.onViewDestroyView();
            mLifeCycleListener = null;
        }

        if (null != mHandlerShowIdiom) {
            mHandlerShowIdiom.removeCallbacksAndMessages(null);
            mHandlerShowIdiom = null;
        }

        mDisplayLetter = null;
        mLetter = null;
        imagePuzzlePath = null;
        dirPuzzle = null;
        dataPiece = null;

        if (null != mapNub) {
            mapNub.clear();
            mapNub = null;
        }

        if (mCompletedPuzzlePiece != null) {
            mCompletedPuzzlePiece.clear();
            mCompletedPuzzlePiece = null;
        }

        if (mQueue != null) {
            mQueue.clear();
            mQueue = null;
        }

        if (mData != null) {
            mData.clear();
            mData = null;
        }

        if (mListener != null) {
            mListener = null;
        }


        super.onDestroyView();

    }

    private void initViews() {

        layoutPuzzleFake.setBackgroundColor(mPuzzleColor);

        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
                .create();


        updatePuzzle();

    }

    private void initControls() {

    }

    private void updatePuzzle() {

        mDisplayLetter = mLetter.getSourceLetter() + "-" + mLetter.getSoundId();
        dirPuzzle = dirPuzzle + mDisplayLetter;

        textTitle.setText(mLetter.getDisplayString().toLowerCase());
        mCompletedPuzzlePiece = AppDataManager.getInstance().getCompletedPuzzlePieces(mDisplayLetter);
        mDisplayLetter = mLetter.getSourceLetter() + "-" + mLetter.getSoundId();

        // getPuzzle
        if (mCompletedPuzzlePiece.size() == Config.PUZZLE_COLUMNS * Config.PUZZLE_ROWS) {
            // show image base 64
            String base64 = AppDataManager.getInstance().getPuzzleBase64(mDisplayLetter);

            if (!TextUtils.isEmpty(base64)) {
                Bitmap bitmap = ImageHelper.convert(base64);
                ImageHelper.load(imagePuzzleCompleted, bitmap);
                imagePuzzleCompleted.setVisibility(View.VISIBLE);
                layoutPuzzle.setVisibility(View.GONE);
            }

            //
        } else {
//            imagePuzzleCompleted.setVisibility(View.GONE);
            layoutPuzzle.setVisibility(View.VISIBLE);
            initPuzzle();
        }


    }


    private void initPuzzle() {


        mData.clear();
        mQueue.clear();

        // read nub data from spec.json
        try {
            String jsonPieces = "";
            String absPath = imagePuzzlePath + dirPuzzle + "/" + dirPuzzle + "-spec.json";
            InputStream inputStream = Application.Context.getAssets().open(absPath);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonPieces = new String(buffer, "UTF-8");

            Type type = new TypeToken<DataPieceModel>() {
            }.getType();

            dataPiece = mGson.fromJson(jsonPieces, type);

        } catch (IOException | IllegalStateException e) {
            e.printStackTrace();
        }

        // put nub puzzle
        if (null != dataPiece && dataPiece.getPieces() != null) {
            mapNub.put("0" + "0", dataPiece.getPieces().getRow0Col0());
            mapNub.put("0" + "1", dataPiece.getPieces().getRow0Col1());
            mapNub.put("0" + "2", dataPiece.getPieces().getRow0Col2());
            mapNub.put("1" + "0", dataPiece.getPieces().getRow1Col0());
            mapNub.put("1" + "1", dataPiece.getPieces().getRow1Col1());
            mapNub.put("1" + "2", dataPiece.getPieces().getRow1Col2());
            mapNub.put("2" + "0", dataPiece.getPieces().getRow2Col0());
            mapNub.put("2" + "1", dataPiece.getPieces().getRow2Col1());
            mapNub.put("2" + "2", dataPiece.getPieces().getRow2Col2());
            mapNub.put("3" + "0", dataPiece.getPieces().getRow3Col0());
            mapNub.put("3" + "1", dataPiece.getPieces().getRow3Col1());
            mapNub.put("3" + "2", dataPiece.getPieces().getRow3Col2());

        }


        ViewTreeObserver vto = layoutPuzzle.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutPuzzle.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int width = layoutPuzzle.getMeasuredWidth();
                int height = layoutPuzzle.getMeasuredHeight();

                int pieceW = width / Config.PUZZLE_COLUMNS;
                int pieceH = height / Config.PUZZLE_ROWS;
                if (mListener != null) {
                    mListener.onSizePiece(pieceW, pieceW);
                }

                int yCoord = 0;
                for (int row = 0; row < Config.PUZZLE_ROWS; row++) {
                    int xCoord = 0;

                    for (int column = 0; column < Config.PUZZLE_COLUMNS; column++) {

                        String pieceId = String.valueOf(row) + String.valueOf(column);

                        if (!puzzleCompleted(pieceId)) {

                            // get image in assets folder
                            String urlArtworkPiece = Config.ASSETS_PATH + imagePuzzlePath + dirPuzzle + "/" + dirPuzzle + "-row" + row + "-col" + column + ".webp";

                            // calculator real width and height of piece

                            NubModel nubModel = mapNub.get(pieceId);

                            int realWidth = pieceW, realHeight = pieceH;
                            int realXCoord = xCoord, realYCoord = yCoord;

                            if (nubModel != null) {
                                // left
                                if (nubModel.getLeftNub().equals(NubDef.OUTSIDE)) {
                                    realXCoord -= pieceW / NUB_PERCENT;
                                    realWidth += pieceW / NUB_PERCENT;
                                }

                                // right
                                if (nubModel.getRightNub().equals(NubDef.OUTSIDE)) {
                                    realWidth += pieceW / NUB_PERCENT;
                                }

                                //top
                                if (nubModel.getTopNub().equals(NubDef.OUTSIDE)) {
                                    realYCoord -= pieceH / NUB_PERCENT;
                                    realHeight += pieceH / NUB_PERCENT;
                                }

                                //bot
                                if (nubModel.getBottomNub().equals(NubDef.OUTSIDE)) {
                                    realHeight += realHeight / NUB_PERCENT;
                                }
                            } else {
                                DebugLog.e(String.valueOf(row) + String.valueOf(column));
                            }

                            PuzzlePieceModel piece = new PuzzlePieceModel(pieceId, realXCoord, realYCoord, realWidth, realHeight, urlArtworkPiece);
//                        mQueue.offer(piece);

                            mData.add(piece);
                        }

                        xCoord += pieceW;
                    }

                    yCoord += pieceH;
                }

                addPuzzleCompleted();
                Collections.shuffle(mData);
                mQueue.addAll(mData);
            }
        });
    }

    private boolean puzzleCompleted(String puzzleId) {
        if (!Helper.isListValid(mCompletedPuzzlePiece)) {
            return false;
        }

        for (PuzzlePieceModel puzzle : mCompletedPuzzlePiece) {
            if (puzzle.getId().equals(puzzleId)) {
                return true;
            }
        }

        return false;
    }

    private void addPuzzleCompleted() {
        for (int i = 0; i < mCompletedPuzzlePiece.size(); i++) {
            PuzzlePieceModel data = mCompletedPuzzlePiece.get(i);
            addPuzzleIntoLayoutInternal(data, null);
        }

    }

    private void addPuzzleIntoLayoutInternal(PuzzlePieceModel data, ImageView imageView) {
        ImageView iv = new ImageView(Application.Context);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(data.getWidth(), data.getHeight());
        params.leftMargin = data.getXCoord();
        params.topMargin = data.getYCoord();

        if (null != imageView) {
            iv.setImageDrawable(imageView.getDrawable());
        } else {
            GlideApp.with(Application.Context)
                    .load(data.getUrlArtwork())
                    .disallowHardwareConfig()
                    .into(iv);
        }
        layoutPuzzle.addView(iv, params);
    }


    Handler mHandlerShowIdiom = new Handler(Looper.getMainLooper());
    boolean mShowIdiom = false;

    public void addPuzzleIntoLayout(PuzzlePieceModel data, ImageView imageView, final PHPuzzleListener listener) {
        addPuzzleIntoLayoutInternal(data, imageView);
        AppDataManager.getInstance().savePuzzlePiece(mDisplayLetter, data);
        EventBus.getDefault().post(new ProgressPuzzleEvent(mDisplayLetter));
        if (mQueue != null && mQueue.isEmpty() && !isShowIdiomActivity) {
            isShowIdiomActivity = true;
            AudioPlayerHelper.getInstance().stopPlayer();
            mShowIdiom = true;
            mHandlerShowIdiom.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showIdiomActivity(true);
                }
            }, 100);
        }

        if (null != listener) {
            listener.onPuzzleCompeted(mShowIdiom);
        }
    }


    public void addPuzzle(int totalPuzzleCompleted, final AddedPuzzleListener listener) {
        if (!mQueue.isEmpty()) {

            for (int i = 0; i < totalPuzzleCompleted && !mQueue.isEmpty(); i++) {
                PuzzlePieceModel data = mQueue.poll();
                mCompletedPuzzlePiece.add(data);
                listener.onAddCompleted(data,
                        Helper.getRelativeLeft(layoutPuzzle) + data.getXCoord(),
                        Helper.getRelativeTop(layoutPuzzle) + data.getYCoord());
            }
        }
    }

    @OnClick(R.id.text_title)
    void playAudioAndAnimationLetter() {
        if (QuizPuzzleActivity.PLAYING) {
            return;
        }
        if (null != mListener) {
            mListener.onViewClick(textTitle, mLetter.getPronunciationTiming());
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        isShowIdiomActivity = false;
    }

    @OnClick({R.id.image_puzzle_completed, R.id.layout_puzzle})
    void showIdiomActivity() {
        showIdiomActivity(false);
    }

    // puzzleCompleted: true if want to destroy activity if idiomActivity destroy
    private void showIdiomActivity(final boolean puzzleCompleted) {
        DebugLog.e("puzzleCompleted: " + puzzleCompleted);

        if (mListener != null) {
            mListener.onPuzzleClick();
        }

        new BackgroundTask() {
            boolean layoutPuzzleVisible = layoutPuzzle.getVisibility() == View.VISIBLE;

            @Override
            public void run() {

                if (layoutPuzzleVisible) {
                    Bitmap bm = ImageHelper.getBitmapFromView(layoutPuzzle);
                    String puzzleBase64 = ImageHelper.convert(bm);
                    if (!TextUtils.isEmpty(puzzleBase64)) {
                        AppDataManager.getInstance().savePuzzleBase64(mDisplayLetter, puzzleBase64);
                    }
                }
            }

            @Override
            public void onFinish() {

                Intent intent = new Intent(getActivity(), IdiomActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(mActivity,
                                layoutPuzzleVisible ? layoutPuzzle : imagePuzzleCompleted,
                                ViewCompat.getTransitionName(layoutPuzzleVisible ? layoutPuzzle : imagePuzzleCompleted));

                Bundle args = new Bundle();
                args.putParcelable(Constants.Arguments.ARG_LETTER, mLetter);

                // invisible image puzzle when blur
                if (layoutPuzzleVisible) {
                    layoutPuzzle.setVisibility(View.INVISIBLE);
                } else {
                    imagePuzzleCompleted.setVisibility(View.INVISIBLE);
                }

//                // screenshot
//                Bitmap bitmap = ImageHelper.getBitmapFromView(mActivity.findViewById(R.id.layout_activity), Bitmap.Config.RGB_565);
//                // compress
//                Bitmap bitmapCompress = ImageHelper.compressBySampleSize(bitmap, 12);

//                args.putParcelable(Constants.Arguments.ARG_BLUR_BITMAP, bitmapCompress);
                args.putBoolean(Constants.Arguments.ARG_BACKGROUND_COLOR, true);
                intent.putExtras(args);

                if (puzzleCompleted) {
                    ActivityCompat.startActivityForResult(mActivity, intent, QuizPuzzleActivity.REQUEST_CODE, options.toBundle());
                } else {
                    mActivity.startActivity(intent, options.toBundle());
                }

            }
        }.execute();

    }

    public interface PuzzleLetterListener {
        void onViewClick(View v, TimedAudioInfoModel audioInfo);

        void onSizePiece(int w, int h);

        void onPuzzleClick();
    }

    public interface PuzzleLetterLifeCycleListener {
        void onViewCreated();

        void onViewDestroyView();
    }

    public interface AddedPuzzleListener {
        void onAddCompleted(PuzzlePieceModel data, int xRootPiece, int yRootPiece);
    }

}






