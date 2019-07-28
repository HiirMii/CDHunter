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
import com.example.android.cdhunter.model.topalbums.AlbumSummary;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumSummaryHorizontalAdapter extends RecyclerView.Adapter<AlbumSummaryHorizontalAdapter.AlbumSummaryViewHolder> {

    private final AlbumSummaryAdapterOnClickHandler albumSummaryAdapterOnClickHandler;
    private List<AlbumSummary> albumSummaryList;
    private Context context;

    public interface AlbumSummaryAdapterOnClickHandler {
        void onAlbumSummaryClick(String artistName, String albumName);
    }

    public AlbumSummaryHorizontalAdapter(AlbumSummaryAdapterOnClickHandler clickHandler) {
        this.albumSummaryAdapterOnClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public AlbumSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_suggestion_album_item, viewGroup, false);

        return new AlbumSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumSummaryViewHolder holder, int position) {
        final AlbumSummary currentAlbumSummary = albumSummaryList.get(position);

        Glide.with(context)
                .load(currentAlbumSummary.getImage().get(3).getImageUrl())
                .error(R.drawable.ic_no_wifi)
                .placeholder(R.drawable.ic_logo)
                .into(holder.albumImage);

        holder.albumName.setText(currentAlbumSummary.getName());

        holder.artistName.setText(currentAlbumSummary.getArtist().getArtistName());
    }

    @Override
    public int getItemCount() {
        if (albumSummaryList == null) return 0;
        return albumSummaryList.size();
    }

    public void setAlbumSummaryList(List<AlbumSummary> albumSummaryList) {
        this.albumSummaryList = albumSummaryList;
    }

    public class AlbumSummaryViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.iv_suggestions_list_album_item_image)
        ImageView albumImage;
        @BindView(R.id.tv_suggestions_list_album_item_album_name)
        TextView albumName;
        @BindView(R.id.tv_suggestions_list_album_item_artist_name)
        TextView artistName;

        public AlbumSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AlbumSummary albumSummary = albumSummaryList.get(getAdapterPosition());
            String albumName = albumSummary.getName();
            String artistName = albumSummary.getArtist().getArtistName();
            albumSummaryAdapterOnClickHandler.onAlbumSummaryClick(artistName, albumName);
        }
    }
}
