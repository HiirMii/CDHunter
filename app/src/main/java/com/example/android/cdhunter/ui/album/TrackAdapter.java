package com.example.android.cdhunter.ui.album;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.model.album.Track;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {

    private List<Track> trackList;

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_track_list_item, viewGroup, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        final Track currentTrack = trackList.get(position);

        holder.trackName.setText(String.format("%d. " + currentTrack.getTrackName(), position + 1));

        Long trackDuration = Long.valueOf(currentTrack.getTrackDuration());

        holder.trackDuration.setText(DateUtils.formatElapsedTime(trackDuration));
    }

    @Override
    public int getItemCount() {
        if (trackList == null) return 0;
        return trackList.size();
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
        notifyDataSetChanged();
    }

    public class TrackViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_track_list_track_name)
        TextView trackName;
        @BindView(R.id.tv_track_list_track_duration)
        TextView trackDuration;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
