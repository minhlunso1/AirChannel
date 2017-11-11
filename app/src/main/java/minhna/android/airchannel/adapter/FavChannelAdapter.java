package minhna.android.airchannel.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import minhna.android.airchannel.data.local.LocalManager;
import minhna.android.airchannel.data.model.Channel;
import minhna.android.airchannel.databinding.ItemFavBinding;
import minhna.android.airchannel.viewmodel.FavViewModel;

/**
 * Created by Minh on 11/11/2017.
 */

public class FavChannelAdapter extends RecyclerView.Adapter<FavChannelAdapter.ChannelViewHolder> {
    private List<Channel> list;
    private LocalManager localManager;

    public FavChannelAdapter(List<Channel> list, LocalManager localManager) {
        this.list = list;
        this.localManager = localManager;
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemFavBinding binding = ItemFavBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChannelViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        final Channel channel = list.get(holder.getAdapterPosition());
        holder.binding.setObj(new FavViewModel(position, channel, localManager));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size():0;
    }

    public class ChannelViewHolder extends RecyclerView.ViewHolder {
        final ItemFavBinding binding;

        public ChannelViewHolder(View rootView) {
            super(rootView);
            binding = DataBindingUtil.bind(rootView);
        }
    }
}
