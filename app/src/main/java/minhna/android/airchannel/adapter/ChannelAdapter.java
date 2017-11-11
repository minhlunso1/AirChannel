package minhna.android.airchannel.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import minhna.android.airchannel.data.pojo.Channel;
import minhna.android.airchannel.databinding.ItemChannelBinding;
import minhna.android.airchannel.view.MainActivity;
import minhna.android.airchannel.viewmodel.ChannelViewModel;

/**
 * Created by Minh on 11/11/2017.
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder> {
    private Context context;
    private List<Channel> list;
    private IChannelItem iChannelItem;

    public interface IChannelItem {
        void onFavPick(boolean isFav);
    }

    public ChannelAdapter(Context context, List<Channel> list, IChannelItem iChannelItem) {
        this.context = context;
        this.list = list;
        this.iChannelItem = iChannelItem;
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemChannelBinding binding = ItemChannelBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ChannelViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        final Channel channel = list.get(holder.getAdapterPosition());
        holder.binding.setObj(new ChannelViewModel(channel));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size():0;
    }

    public class ChannelViewHolder extends RecyclerView.ViewHolder {
        final ItemChannelBinding binding;

        public ChannelViewHolder(View rootView) {
            super(rootView);
            binding = DataBindingUtil.bind(rootView);
        }
    }
}
