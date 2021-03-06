package com.ivankostadinovic.genericadapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

/**
 * @author manoj.bhadane manojbhadane777@gmail.com
 * edited by ivankostadinovic1994@outlook.com
 */

public abstract class GenericAdapter<T, D extends ViewDataBinding> extends RecyclerView.Adapter<GenericAdapter<T, D>.ItemViewHolder> {

    public static final int DEFAULT_PAGINATION_OFFSET = 3;

    public List<T> list = new ArrayList<>();
    private final int layoutResId;
    private int paginationOffset = DEFAULT_PAGINATION_OFFSET;

    public GenericAdapter(List<T> arrayList, @LayoutRes int layoutResId) {
        this.list.addAll(arrayList);
        this.layoutResId = layoutResId;
    }

    public GenericAdapter(List<T> arrayList, @LayoutRes int layoutResId, int paginationOffset) {
        this.list = new ArrayList<>();
        this.list.addAll(arrayList);
        this.layoutResId = layoutResId;
        this.paginationOffset = paginationOffset;
    }

    public abstract void onBindData(T model, int position, D dataBinding);

    public abstract void onItemClick(T model, int position);

    public void onCreateHolder(D dataBinding) {
    }

    public void loadMoreItems(int loadedCount) {

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        D dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId, parent, false);
        onCreateHolder(dataBinding);
        return new ItemViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        final T item = list.get(position);
        holder.dataBinding.setVariable(BR.data, item);
        onBindData(item, position, holder.dataBinding);
        holder.dataBinding.executePendingBindings();
        holder.dataBinding.getRoot().setOnClickListener(view -> onItemClick(item, position));
        if (position == getItemCount() - paginationOffset) {
            loadMoreItems(list.size());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(List<T> arrayList) {
        if (list != arrayList) {
            list = new ArrayList<>();
            list.addAll(arrayList);
            notifyDataSetChanged();
        }
    }

    public void addItems(List<T> list) {
        int currentSize = this.list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(currentSize, list.size());
    }

    public void clearItems() {
        list.clear();
        notifyDataSetChanged();
    }

    public void removeItem(T item) {
        int position = list.indexOf(item);
        list.remove(item);
        notifyItemRemoved(position);
    }

    public void removeItem(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    public T getItem(int position) {
        return list.get(position);
    }

    public void addItem(T item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public void updateItem(T item, int position) {
        list.set(position, item);
        notifyItemChanged(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private final D dataBinding;

        private ItemViewHolder(D binding) {
            super(binding.getRoot());
            dataBinding = binding;
        }
    }
}