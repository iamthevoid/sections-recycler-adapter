package iam.thevoid.headersrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by iam on 25.11.17.
 */

public abstract class BindableViewHolder<T> extends RecyclerView.ViewHolder {

    private int viewType;

    public BindableViewHolder(View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public abstract void onBind(T data);

    public int getViewType() {
        return viewType;
    }
}
