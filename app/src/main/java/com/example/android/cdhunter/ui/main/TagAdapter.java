package com.example.android.cdhunter.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.cdhunter.R;
import com.example.android.cdhunter.model.common.Tag;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.TagViewHolder> {

    private final TagAdapterOnClickHandler tagAdapterOnClickHandler;
    private List<Tag> tagList;
    Context context;

    public interface TagAdapterOnClickHandler {
        void onTagClick(String tag);
    }

    public TagAdapter(TagAdapterOnClickHandler tagAdapterOnClickHandler) {
        this.tagAdapterOnClickHandler = tagAdapterOnClickHandler;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_tag_list_item, viewGroup, false);

        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        final Tag currentTag = tagList.get(position);

        holder.tag.setText(currentTag.getTagName());
    }

    @Override
    public int getItemCount() {
        if(tagList == null) return 0;
        return tagList.size();
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }

    public class TagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_tag)
        TextView tag;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Tag currentTag = tagList.get(getAdapterPosition());
            String tagName = currentTag.getTagName();
            tagAdapterOnClickHandler.onTagClick(tagName);
        }
    }
}
