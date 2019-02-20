package com.hearatale.sightwords.service;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.hearatale.sightwords.Application;


import com.hearatale.sightwords.data.model.phonics.letters.TimedAudioInfoModel;
import com.hearatale.sightwords.ui.custom_view.PHListener;
import com.hearatale.sightwords.utils.DebugLog;

import java.io.IOException;

public class AudioPlayerHelper {
    private static final String EXTENSION_FILE = ".mp3";

    private static volatile AudioPlayerHelper Instance = null;

    MediaPlayer mMediaPlayer;
    Handler mHandlerDelay;
    Runnable mRunnableDelay;
    MediaPlayer mIdiomPlayer;


    private AudioPlayerHelper() {
        mHandlerDelay = new Handler();
        mMediaPlayer = new MediaPlayer();
    }

    public static AudioPlayerHelper getInstance() {
        AudioPlayerHelper localInstance = Instance;
        if (localInstance == null) {
            synchronized (AudioPlayerHelper.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new AudioPlayerHelper();
                }
            }
        }
        return localInstance;
    }

    public synchronized void playAudio(String prefixPath, final TimedAudioInfoModel audioInfo) {
        playAudio(prefixPath, audioInfo, new DonePlayingListener() {
            @Override
            public void donePlaying() {

            }
        });
    }

    public void playAudio(String prefixPath, final TimedAudioInfoModel audioInfo, @Nullable final DonePlayingListener listener) {
        try {
            stopPlayer();
            String pathFile = prefixPath + audioInfo.getFileName() + EXTENSION_FILE;
            prepareDataForPlayer(pathFile);

            int positionStart = convertSecondStringToMilliSeconds(audioInfo.getWordStart()) - 300;
            mMediaPlayer.seekTo(positionStart < 0 ? 0 : positionStart);
            mMediaPlayer.start();
            mRunnableDelay = new Runnable() {
                @Override
                public void run() {
                    stopPlayer();
                    if (listener != null) {
                        listener.donePlaying();
                    }
                }
            };
            mHandlerDelay.postDelayed(mRunnableDelay, convertSecondStringToMilliSeconds(audioInfo.getWordDuration()) + 500);
        } catch (IOException e) {
            DebugLog.e("fileName: " + audioInfo.getFileName());
            stopPlayer();
            e.printStackTrace();
        }
    }

    private void prepareDataForPlayer(String pathFile) throws IOException {
        AssetFileDescriptor afd = Application.Context.getAssets().openFd(pathFile);
        mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
        mMediaPlayer.prepare();
    }

    public void playAudio(String prefixPath, final TimedAudioInfoModel audioInfo, @Nullable final CompletedListener listener) {
        try {
            stopPlayer();
            prepareDataForPlayer(prefixPath + audioInfo.getFileName() + EXTENSION_FILE);

            int positionStart = convertSecondStringToMilliSeconds(audioInfo.getWordStart()) - 300;
            mMediaPlayer.seekTo(positionStart < 0 ? 0 : positionStart);
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (null != listener) {
                        listener.onCompleted();
                    }
                }
            });
        } catch (IOException e) {
            stopPlayer();
            e.printStackTrace();
        }
    }


    public void playAudio(String pathFile) {
       playWithOffset(pathFile, 0);
    }

    public void playAudio(String pathFile, final PHListener.AudioListener listener) {
        try {
            stopPlayer();
            prepareDataForPlayer(pathFile + EXTENSION_FILE);
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (null != listener) {
                        listener.onAudioCompleted();
                    }
                    stopPlayer();
                }
            });
        } catch (IOException e) {
            stopPlayer();
            if (null != listener) {
                listener.onAudioCompleted();
            }
            e.printStackTrace();
        }
    }


    private int convertSecondStringToMilliSeconds(String durationString) {
        if (TextUtils.isEmpty(durationString)) return 0;
        return (int) (Float.parseFloat(durationString) * 1000);
    }

    public void stopPlayer() {
//        mHandlerDelay.removeCallbacks(mRunnableDelay);
        if (null != mHandlerDelay) {
            mHandlerDelay.removeCallbacksAndMessages(null);
        }
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        mMediaPlayer.reset();
    }

    public void playCorrect(CompletedListener listener) {
        playNewPlayer("Sounds/correct", listener);

    }

    public void playInCorrect() {
        playNewPlayer("Sounds/incorrect", null);
    }


    public void playIdiom(String path, final CompletedListener listener) {
        mIdiomPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor afd = Application.Context.getAssets().openFd(path + EXTENSION_FILE);
            mIdiomPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mIdiomPlayer.prepare();
            mIdiomPlayer.start();
            mIdiomPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopIdiom();
                    if (null != listener) {
                        listener.onCompleted();
                    }
                }
            });
        } catch (IOException e) {
            stopIdiom();
            if (null != listener) {
                listener.onCompleted();
            }
            e.printStackTrace();
        }
    }

    public void stopIdiom() {
        if (null != mIdiomPlayer) {
            if (mIdiomPlayer.isPlaying()) {
                mIdiomPlayer.stop();
            }
            mIdiomPlayer.reset();
            mIdiomPlayer.release();
            mIdiomPlayer = null;
        }
    }


    private void playNewPlayer(String path, final CompletedListener listener) {
        try {
            final MediaPlayer mediaPlayer = new MediaPlayer();
            AssetFileDescriptor afd = Application.Context.getAssets().openFd(path + EXTENSION_FILE);
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.release();
                    if (null != listener) {
                        listener.onCompleted();
                    }
                }
            });
        } catch (IOException e) {
            stopPlayer();
            if (null != listener) {
                listener.onCompleted();
            }
            e.printStackTrace();
        }
    }

    public void playWithOffset(String path, int offset){
        try {
            stopPlayer();
            prepareDataForPlayer(path + EXTENSION_FILE);
            mMediaPlayer.seekTo(offset);
            mMediaPlayer.start();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        } catch (IOException e) {
            stopPlayer();
            e.printStackTrace();
        }
    }

    public int getCurrentPosition(){
        return mMediaPlayer.getCurrentPosition();
    }


    /**
     * Listener done playing after delay time
     */
    public interface DonePlayingListener {
        void donePlaying();
    }


    /**
     * Listener play to end file audio
     */
    public interface CompletedListener {
        void onCompleted();
    }

}
