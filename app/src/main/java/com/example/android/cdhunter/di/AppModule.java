package com.example.android.cdhunter.di;

import android.app.Application;

import androidx.room.Room;

import com.example.android.cdhunter.api.LastFmService;
import com.example.android.cdhunter.db.AlbumDao;
import com.example.android.cdhunter.db.CdHunterDb;
import com.example.android.cdhunter.db.SimilarArtistDao;
import com.example.android.cdhunter.db.TagDao;
import com.example.android.cdhunter.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Singleton @Provides
    LastFmService provideLastFmService() {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(LastFmService.class);
    }

    @Singleton @Provides
    CdHunterDb provideDb(Application app) {
        return Room.databaseBuilder(app, CdHunterDb.class, "cdhunter.db").build();
    }

    @Singleton @Provides
    AlbumDao provideAlbumDao(CdHunterDb db) {
        return db.albumDao();
    }

    @Singleton @Provides
    SimilarArtistDao provideSimilarArtistDao(CdHunterDb db) {
        return db.similarArtistDao();
    }

    @Singleton @Provides
    TagDao provideTagDao(CdHunterDb db) {
        return db.tagDao();
    }
}
