package com.hearatale.sightword.utils.glide;

import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

public class ArtLoader implements ModelLoader<StreamModel, InputStream> {

    private final ModelLoader<StreamModel, InputStream> mModelCache;

    public ArtLoader() {
        this(null);
    }

    public ArtLoader(ModelLoader<StreamModel, InputStream> modelCache) {
        mModelCache = modelCache;
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(StreamModel streamModel, int width, int height, Options options) {
//        return new LoadData<>(new ObjectKey(streamModel), new ArtFetcher(streamModel));
        return mModelCache.buildLoadData(streamModel, width, height, options);
    }

    @Override
    public boolean handles(StreamModel streamModel) {
        return true;
    }

    public static class Factory implements ModelLoaderFactory<StreamModel, InputStream> {
        @Override
        public ModelLoader<StreamModel, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new ArtLoader(multiFactory.build(StreamModel.class, InputStream.class));
        }

        @Override
        public void teardown() {

        }
    }

}

