package com.example.android.cdhunter.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.android.cdhunter.R;
import com.example.android.cdhunter.model.album.Album;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder> {

    private final AlbumAdapterOnClickHandler albumAdapterOnClickHandler;
    private List<Album> albumList;
    private Context context;

    public interface AlbumAdapterOnClickHandler {
        void onAlbumClick(String artistName, String albumName);
    }

    public AlbumAdapter(AlbumAdapterOnClickHandler clickHandler) {
        this.albumAdapterOnClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_collection_double_textview_item,
                viewGroup, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album currentAlbum = albumList.get(position);

        Glide.with(context)
                .load(currentAlbum.getListOfImages().get(4).getImageUrl())
                .error(R.drawable.ic_no_wifi)
                .placeholder(R.drawable.ic_logo)
                .into(holder.imageView);

        holder.albumName.setText(currentAlbum.getAlbumName());

        holder.artistName.setText(currentAlbum.getArtistName());
    }

    @Override
    public int getItemCount() {
        if (albumList == null) return 0;
        return albumList.size();
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
        notifyDataSetChanged();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_collection_list_double_textview_item_image)
        ImageView imageView;
        @BindView(R.id.tv_collection_list_double_textview_item_album_name)
        TextView albumName;
        @BindView(R.id.tv_collection_list_double_textview_item_artist_name)
        TextView artistName;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Album currentAlbum = albumList.get(getAdapterPosition());
            String artistName = currentAlbum.getArtistName();
            String albumName = currentAlbum.getAlbumName();
            albumAdapterOnClickHandler.onAlbumClick(artistName, albumName);
        }
    }
}
