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
 * edited by ivambass1@gmail.com
 */

public abstract class GenericAdapter<T, D extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> mArrayList;
    private int layoutResId;

    public GenericAdapter(List<T> arrayList, @LayoutRes int layoutResId) {
        this.mArrayList = new ArrayList<>();
        this.mArrayList.addAll(arrayList);
        this.layoutResId = layoutResId;
    }

    public abstract void onBindData(T model, int position, D dataBinding);

    public abstract void onItemClick(T model, int position);

    public void onCreateHolder(D dataBinding) {
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
        ((ItemViewHolder) holder).mDataBinding.setVariable(BR.data, mArrayList.get(position));
        onBindData(mArrayList.get(position), position, ((ItemViewHolder) holder).mDataBinding);
        ((ItemViewHolder) holder).mDataBinding.executePendingBindings();
        ((ItemViewHolder) holder).mDataBinding.getRoot().setOnClickListener(view -> onItemClick(mArrayList.get(position), position));

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
        mArrayList.addAll(arrayList);
        notifyDataSetChanged();
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

    public T getItem(int position) {
        return mArrayList.get(position);
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private D mDataBinding;

        private ItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            mDataBinding = (D) binding;
        }
    }
}