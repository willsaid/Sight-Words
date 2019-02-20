package com.hearatale.sightwords.utils.glide;

import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ArtFetcher implements DataFetcher<InputStream> {
    private final StreamModel mStream;
    private InputStream mInputStream;

    public ArtFetcher(StreamModel stream) {
        mStream = stream;
    }

    private InputStream fetchStream(String downloadUrl) {
        URL url = null;
        try {
            url = new URL(downloadUrl);
            URLConnection connection = url.openConnection();
            connection.connect();

            return new BufferedInputStream(url.openStream(), 8192);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
        String url = mStream.getStreamUrl();

        if (mStream.isLocal()) {
            MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
            metaRetriver.setDataSource(url);

            mInputStream = new ByteArrayInputStream(metaRetriver.getEmbeddedPicture());
            callback.onDataReady(mInputStream);
        }

        mInputStream = fetchStream(url);
        callback.onDataReady(mInputStream);
    }

    @Override
    public void cleanup() {
        // v4 no
//        if (mInputStream != null) {
//            try {
//                mInputStream.close();
//            } catch (IOException e) {
//            } finally {
//                mInputStream = null;
//            }
//        }
    }

//    @Override
//    public String getId() {
//        return mStream.getStreamUrl();
//    }

    @Override
    public void cancel() {
//        v3 no, v4 no
//        mIsCanceled = true;
//        if (mFetchStreamCall != null)
//        {
//            mFetchStreamCall.cancel();
//        }
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;

    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }
}
