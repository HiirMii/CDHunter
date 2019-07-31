package com.example.android.cdhunter.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.NonNull;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.model.album.Album;
import com.example.android.cdhunter.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class WishlistWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Intent intent;

    private List<Album> albumList = new ArrayList<>();
    private Album currentAlbum;

    private void setWishlist() throws NullPointerException {
        try {
            albumList.clear();

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            assert firebaseUser != null;
            String userId = firebaseUser.getUid();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference().child(Constants.ALBUMS).child(userId);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot album : dataSnapshot.getChildren()) {
                        currentAlbum = album.getValue(Album.class);
                        assert currentAlbum != null;
                        if (currentAlbum.getOwnershipStatus().equals(Constants.WISHLIST)) {
                            albumList.add(currentAlbum);
                        }
                    }
                    // logs data
                    if (!albumList.isEmpty()) {
                        Timber.d(albumList.get(0).getAlbumName());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public WishlistWidgetFactory(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }


    @Override
    public void onCreate() {
        setWishlist();
    }

    @Override
    public void onDataSetChanged() {
        setWishlist();
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        if (albumList == null) return 0;
        return albumList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews album = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

        //fails to log data
        if (!albumList.isEmpty()) {
            Timber.d(albumList.get(0).getAlbumName());
        }

        album.setTextViewText(R.id.tv_wishlist_item_album_name, albumList.get(position).getAlbumName());
        album.setTextViewText(R.id.tv_wishlist_item_artist_name, albumList.get(position).getArtistName());

        return album;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}