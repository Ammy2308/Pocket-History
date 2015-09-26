package project.com.pockethistory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class PaletteRecyclerAdapter extends RecyclerView.Adapter<PaletteRecyclerAdapter.YourRecyclerViewHolder> {
    private List<Object> obj;
    private LayoutInflater inflater;

    public PaletteRecyclerAdapter(Context context, List<Object> obj) {
        inflater = LayoutInflater.from(context);
        this.obj = obj;
    }

    @Override
    public YourRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View root = inflater.inflate(R.layout.custom_row, viewGroup, false);
        YourRecyclerViewHolder holder = new YourRecyclerViewHolder(root);
        return holder;
    }

    @Override
    public void onBindViewHolder(YourRecyclerViewHolder yourRecyclerViewHolder, int i) {
        yourRecyclerViewHolder.heading.setVisibility(View.GONE);
        yourRecyclerViewHolder.textView.setVisibility(View.GONE);
        RecyclerContent content = (RecyclerContent) obj.get(i);

        yourRecyclerViewHolder.heading.setText(content.getPaletteHeading());
        yourRecyclerViewHolder.textView.setText(content.getPaletteContent());
        if(!content.getPaletteContent().equals("null")) {
            yourRecyclerViewHolder.heading.setVisibility(View.VISIBLE);
            yourRecyclerViewHolder.textView.setVisibility(View.VISIBLE);
        } else {
            yourRecyclerViewHolder.textView.setText(content.getPaletteHeading());
            yourRecyclerViewHolder.heading.setVisibility(View.GONE);
            yourRecyclerViewHolder.textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return obj.size();
    }

    static class YourRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView textView, heading;

        public YourRecyclerViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_superhero);
            heading = (TextView) itemView.findViewById(R.id.palette_heading);
        }
    }

    public void animateTo(List<Object> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private void applyAndAnimateRemovals(List<Object> newModels) {
        for (int i = obj.size() - 1; i >= 0; i--) {
            final RecyclerContent model = (RecyclerContent) obj.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Object> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final RecyclerContent model = (RecyclerContent) newModels.get(i);
            if (!obj.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Object> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final RecyclerContent model = (RecyclerContent) newModels.get(toPosition);
            final int fromPosition = obj.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public RecyclerContent removeItem(int position) {
        final RecyclerContent model = (RecyclerContent) obj.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, RecyclerContent model) {
        obj.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final RecyclerContent model = (RecyclerContent) obj.remove(fromPosition);
        obj.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
