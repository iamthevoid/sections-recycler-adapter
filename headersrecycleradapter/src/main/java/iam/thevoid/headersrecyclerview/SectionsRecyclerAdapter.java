package iam.thevoid.headersrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by iam on 27.11.17.
 */

public abstract class SectionsRecyclerAdapter<KEY, ITEM> extends RecyclerView.Adapter<BindableViewHolder> {

    private final List<ITEM> mData;
    private final Map<KEY, List<ITEM>> mSections;

    private static final int HEADER = 0;
    private static final int ITEM = 1;

    private LayoutInflater mInflater;

    public SectionsRecyclerAdapter(Context context, List<ITEM> data) {
        this.mData = data;
        initAdditionalParameters(context);
        mSections = initSections(data);
    }

    // override for init any params before sections will be create
    protected void initAdditionalParameters(Context context) {}

    // divide data by sections. Each entry will be available in header holder
    protected abstract Map<KEY, List<ITEM>> initSections(List<ITEM> data);

    protected abstract BindableViewHolder<ITEM> getItemHolder(LayoutInflater inflater, ViewGroup parent);

    protected abstract BindableViewHolder<Map.Entry<KEY, List<ITEM>>> getHeaderHolder(LayoutInflater inflater, ViewGroup parent);

    @Override
    public final BindableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEADER) {
            return getHeaderHolder(mInflater, parent);
        }
        return getItemHolder(mInflater, parent);
    }

    @Override
    public final void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mInflater = LayoutInflater.from(recyclerView.getContext());
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onBindViewHolder(BindableViewHolder holder, int position) {
        int realPos = getRealPos(position);
        holder.onBind(getItemViewType(position) == HEADER ? getEntryAt(realPos) : mData.get(realPos));
    }

    public final void setData(List<ITEM> data) {
        this.mData.clear();
        this.mData.addAll(data);
        this.mSections.clear();
        this.mSections.putAll(initSections(data));
        notifyDataSetChanged();
    }

    private int getRealPos(int position) {
        int counter = 0;
        int headerPos = 0;
        for (Map.Entry<KEY, List<ITEM>> entry : mSections.entrySet()) {

            if (counter == position) {
                return headerPos;
            }

            headerPos++;
            counter++;

            int intervalEnd = counter + entry.getValue().size();
            if (position >= counter && position < intervalEnd) {
                return position - headerPos;
            }

            counter = intervalEnd;
        }

        return position - headerPos;
    }

    @Override
    public final int getItemCount() {
        return mSections.size() + mData.size();
    }

    private Map.Entry<KEY, List<ITEM>> getEntryAt(int position) {
        int counter = 0;
        for (Map.Entry<KEY, List<ITEM>> entry : mSections.entrySet()) {
            if (counter == position) return entry;
            counter++;
        }
        return null;
    }

    @Override
    public final int getItemViewType(int position) {
        int counter = 0;
        for (Map.Entry<KEY, List<ITEM>> entry : mSections.entrySet()) {

            if (counter == position) return HEADER;
            counter++;

            int intervalEnd = counter + entry.getValue().size();
            if (position >= counter && position < intervalEnd) {
                return ITEM;
            }

            counter = intervalEnd;
        }

        return ITEM;
    }

    public List<ITEM> getData() {
        return mData;
    }
}
