package minhna.android.airchannel.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import minhna.android.airchannel.data.model.Event;
import minhna.android.airchannel.databinding.ItemEventBinding;
import minhna.android.airchannel.util.UIUtil;
import minhna.android.airchannel.viewmodel.EventViewModel;

/**
 * Created by Minh on 11/13/2017.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> list;
    private int colorJump = 0;

    public EventAdapter(List<Event> list) {
        this.list = list;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemEventBinding binding = ItemEventBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EventViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = list.get(holder.getAdapterPosition());
        holder.binding.setObj(new EventViewModel(holder.getAdapterPosition(),
                UIUtil.getColorRes(colorJump++), event));
        if (colorJump == UIUtil.MAX_JUMP)
            colorJump = 0;
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size():0;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        final ItemEventBinding binding;

        public EventViewHolder(View rootView) {
            super(rootView);
            binding = DataBindingUtil.bind(rootView);
        }
    }
}
