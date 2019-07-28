package com.example.android.cdhunter.ui.artist;

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

public class AlbumSummaryVerticalAdapter extends
        RecyclerView.Adapter<AlbumSummaryVerticalAdapter.AlbumViewHolder> {

    private final AlbumSummaryOnClickHandler albumSummaryOnClickHandler;
    private List<AlbumSummary> albumSummaryList;
    private Context context;

    public interface AlbumSummaryOnClickHandler {
        void onAlbumSummaryClick(String artistName, String albumName);
    }

    public AlbumSummaryVerticalAdapter(AlbumSummaryOnClickHandler clickHandler) {
        this.albumSummaryOnClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(
                R.layout.item_collection_single_textview_item, viewGroup, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        final AlbumSummary currentAlbumSummary = albumSummaryList.get(position);

        Glide.with(context)
                .load(currentAlbumSummary.getImage().get(3).getImageUrl())
                .error(R.drawable.ic_no_wifi)
                .placeholder(R.drawable.ic_logo)
                .into(holder.imageView);

        holder.albumName.setText(currentAlbumSummary.getName());
    }

    @Override
    public int getItemCount() {
        if (albumSummaryList == null) return 0;
        return albumSummaryList.size();
    }

    public void setAlbumSummaryList(List<AlbumSummary> albumSummaryList) {
        this.albumSummaryList = albumSummaryList;
        notifyDataSetChanged();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_collection_list_single_textview_item_image)
        ImageView imageView;
        @BindView(R.id.tv_collection_list_single_textview_item_album_name)
        TextView albumName;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AlbumSummary currentAlbumSummary = albumSummaryList.get(getAdapterPosition());
            String artistName = currentAlbumSummary.getArtist().getArtistName();
            String albumName = currentAlbumSummary.getName();
            albumSummaryOnClickHandler.onAlbumSummaryClick(artistName, albumName);
        }
    }
}
