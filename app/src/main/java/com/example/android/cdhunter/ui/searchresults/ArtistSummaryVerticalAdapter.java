package com.example.android.cdhunter.ui.searchresults;

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

public class ArtistSummaryVerticalAdapter extends
        RecyclerView.Adapter<ArtistSummaryVerticalAdapter.ArtistSummaryViewHolder> {

    private final ArtistSummaryAdapterOnClickHandler artistSummaryAdapterOnClickHandler;
    private List<ArtistSummary> artistSummaryList;
    private Context context;

    public interface ArtistSummaryAdapterOnClickHandler {
        void onArtistSummaryClick(String artistName);
    }

    public ArtistSummaryVerticalAdapter(ArtistSummaryAdapterOnClickHandler clickHandler) {
        this.artistSummaryAdapterOnClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ArtistSummaryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(
                R.layout.item_collection_single_textview_item, viewGroup, false);

        return new  ArtistSummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistSummaryViewHolder holder, int position) {
        ArtistSummary currentArtistSummary = artistSummaryList.get(position);

        Glide.with(context)
                .load(currentArtistSummary.getImage().get(4).getImageUrl())
                .error(R.drawable.ic_no_wifi)
                .placeholder(R.drawable.ic_logo)
                .into(holder.imageView);

        holder.artistName.setText(currentArtistSummary.getArtistName());
    }

    @Override
    public int getItemCount() {
        if (artistSummaryList == null) return 0;
        return artistSummaryList.size();
    }

    public void setArtistSummaryList(List<ArtistSummary> artistSummaryList) {
        this.artistSummaryList = artistSummaryList;
    }

    public class ArtistSummaryViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.iv_collection_list_single_textview_item_image)
        ImageView imageView;
        @BindView(R.id.tv_collection_list_single_textview_item_album_name)
        TextView artistName;

        public ArtistSummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            ArtistSummary currentArtistSummary = artistSummaryList.get(getAdapterPosition());
            String artistName = currentArtistSummary.getArtistName();
            artistSummaryAdapterOnClickHandler.onArtistSummaryClick(artistName);
        }
    }
}
