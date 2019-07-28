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
import com.example.android.cdhunter.model.common.ArtistSummary;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArtistSummaryHorizontalAdapter extends
        RecyclerView.Adapter<ArtistSummaryHorizontalAdapter.ArtistSummaryViewHolder> {

    private final ArtistSummaryAdapterOnClickHandler artistSummaryAdapterOnClickHandler;
    private List<ArtistSummary> artistSummaryList;
    private Context context;

    public interface ArtistSummaryAdapterOnClickHandler {
        void onArtistSummaryClick(String artistName);
    }

    public ArtistSummaryHorizontalAdapter(ArtistSummaryAdapterOnClickHandler clickHandler) {
        this.artistSummaryAdapterOnClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ArtistSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_suggestion_artist_item, viewGroup, false);

        return new ArtistSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistSummaryViewHolder holder, int position) {
        final ArtistSummary currentArtistSummary = artistSummaryList.get(position);

        Glide.with(context)
                .load(currentArtistSummary.getImage().get(4).getImageUrl())
                .error(R.drawable.ic_no_wifi)
                .placeholder(R.drawable.ic_logo)
                .into(holder.artistImageView);

        holder.artistName.setText(currentArtistSummary.getArtistName());
    }

    @Override
    public int getItemCount() {
        if (artistSummaryList == null) return 0;
        return artistSummaryList.size();
    }

    public void setArtistSummaryList(List<ArtistSummary> artistSummaryList) {
        this.artistSummaryList = artistSummaryList;
        notifyDataSetChanged();
    }

    public class ArtistSummaryViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.iv_suggestions_list_artist_item_image)
        ImageView artistImageView;
        @BindView(R.id.tv_suggestions_list_artist_item_album_name)
        TextView artistName;

        public ArtistSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ArtistSummary currentSummary = artistSummaryList.get(getAdapterPosition());
            String artistName = currentSummary.getArtistName();
            artistSummaryAdapterOnClickHandler.onArtistSummaryClick(artistName);
        }
    }
}
