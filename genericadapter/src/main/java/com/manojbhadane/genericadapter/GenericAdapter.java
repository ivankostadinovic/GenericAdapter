package com.manojbhadane.genericadapter;

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

public abstract class GenericAdapter<T, D extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int DEFAULT_PAGINATION_OFFSET = 3;

    public List<T> mArrayList = new ArrayList<>();
    private int layoutResId;
    private int paginationOffset = DEFAULT_PAGINATION_OFFSET;

    public GenericAdapter(List<T> arrayList, @LayoutRes int layoutResId) {
        this.mArrayList.addAll(arrayList);
        this.layoutResId = layoutResId;
    }

    public GenericAdapter(List<T> arrayList, @LayoutRes int layoutResId, int paginationOffset) {
        this.mArrayList = new ArrayList<>();
        this.mArrayList.addAll(arrayList);
        this.layoutResId = layoutResId;
        this.paginationOffset = paginationOffset;
    }

    public abstract void onBindData(T model, int position, D dataBinding);

    public abstract void onItemClick(T model, int position);

    public void onCreateHolder(D dataBinding) {
    }

    public void loadMoreItems() {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding dataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), layoutResId, parent, false);
        onCreateHolder((D) dataBinding);
        return new ItemViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        final T item = mArrayList.get(position);
        ((ItemViewHolder) holder).mDataBinding.setVariable(BR.data, mArrayList.get(position));
        onBindData(mArrayList.get(position), position, ((ItemViewHolder) holder).mDataBinding);
        ((ItemViewHolder) holder).mDataBinding.executePendingBindings();
        ((ItemViewHolder) holder).mDataBinding.getRoot().setOnClickListener(view -> onItemClick(item, position));
        if (position == getItemCount() - paginationOffset) {
            loadMoreItems();
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public void setItems(List<T> arrayList) {
        if (mArrayList != arrayList) {
            mArrayList = new ArrayList<>();
            mArrayList.addAll(arrayList);
            notifyDataSetChanged();
        }
    }

    public void addItems(ArrayList<T> arrayList) {
        int currentSize = mArrayList.size();
        mArrayList.addAll(arrayList);
        notifyItemRangeInserted(currentSize, arrayList.size());
    }

    public void clearItems() {
        mArrayList.clear();
        notifyDataSetChanged();
    }

    public void removeItem(T item) {
        int position = mArrayList.indexOf(item);
        mArrayList.remove(item);
        notifyItemRemoved(position);
    }

    public void removeItem(int position) {
        mArrayList.remove(position);
        notifyItemRemoved(position);
    }

    public T getItem(int position) {
        return mArrayList.get(position);
    }

    public void addItem(T item, int position) {
        mArrayList.add(position, item);
        notifyItemInserted(position);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private D mDataBinding;

        private ItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            mDataBinding = (D) binding;
        }
    }
}