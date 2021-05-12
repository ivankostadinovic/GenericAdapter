package com.manojbhadane.genericadapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.rxbinding4.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;
import kotlin.collections.CollectionsKt;

/**
 * @author manoj.bhadane manojbhadane777@gmail.com
 * edited by ivankostadinovic1994@outlook.com
 */

public abstract class GenericFilterAdapter<T, D extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> mArrayList = new ArrayList<>();
    private List<T> mArrayListFilter = new ArrayList<>();
    private final int layoutResId;
    private final Disposable searchDisposable;

    public abstract void onBindData(T model, int position, D dataBinding);

    public abstract void onItemClick(T model, int position);

    public abstract boolean filter(T item, String text);

    public void onCreateHolder(D dataBinding) {
    }

    public GenericFilterAdapter(List<T> arrayList, @LayoutRes int layoutResId, EditText searchView) {
        this.mArrayList.addAll(arrayList);
        this.mArrayListFilter.addAll(arrayList);
        this.layoutResId = layoutResId;

        searchDisposable = RxTextView
            .textChanges(searchView)
            .subscribe(this::filterList);
    }

    private void filterList(CharSequence text) {
        mArrayListFilter = CollectionsKt.filter(mArrayList, item -> filter(item, text.toString()));
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        if (searchDisposable != null) {
            searchDisposable.dispose();
        }
        super.onDetachedFromRecyclerView(recyclerView);
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
        ((ItemViewHolder) holder).mDataBinding.setVariable(BR.data, mArrayListFilter.get(position));
        onBindData(mArrayListFilter.get(position), position, ((ItemViewHolder) holder).mDataBinding);
        ((ItemViewHolder) holder).mDataBinding.executePendingBindings();
        ((ItemViewHolder) holder).mDataBinding.getRoot().setOnClickListener(view -> onItemClick(mArrayListFilter.get(position), position));

    }

    @Override
    public int getItemCount() {
        return mArrayListFilter.size();
    }

    public void setItems(List<T> arrayList) {
        if (mArrayList != arrayList) {
            mArrayList = new ArrayList<>();
            mArrayList.addAll(arrayList);
            mArrayListFilter = new ArrayList<>();
            mArrayListFilter.addAll(arrayList);
            notifyDataSetChanged();
        }
    }

    public void clearItems() {
        mArrayList.clear();
        mArrayListFilter.clear();
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mArrayListFilter.get(position);
    }

    public void removeItem(int position) {
        T itemToRemove = mArrayListFilter.get(position);
        mArrayListFilter.remove(position);
        mArrayList.remove(itemToRemove);
        notifyItemRemoved(position);
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        private D mDataBinding;

        private ItemViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            mDataBinding = (D) binding;
        }
    }
}