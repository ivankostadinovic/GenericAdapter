package com.manojbhadane.genericadapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

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

/**
 * @author manoj.bhadane manojbhadane777@gmail.com
 * edited by ivankostadinovic1994@outlook.com
 */

public abstract class GenericAdapter<T, D extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private List<T> mArrayList;
    private List<T> mArrayListFilter;
    private int layoutResId;
    private EditText searchView;
    private Disposable searchDisposable;

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

    public GenericAdapter(List<T> arrayList, @LayoutRes int layoutResId, EditText searchView) {
        this.mArrayList = new ArrayList<>();
        this.mArrayList.addAll(arrayList);
        this.layoutResId = layoutResId;
        this.searchView = searchView;

        searchDisposable = RxTextView
            .textChanges(this.searchView)
            .subscribe(text -> getFilter().filter(text));
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        if (searchDisposable != null) {
            searchDisposable.dispose();
        }
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                List<T> arrayList = new ArrayList<>();

                try {
                    if (charSequence.toString().isEmpty()) {
                        arrayList = mArrayList;
                    } else {
                        arrayList = performFilter(charSequence.toString().trim(), mArrayList);
                        if (arrayList == null) {
                            arrayList = mArrayList;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mArrayListFilter = (ArrayList<T>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public List<T> performFilter(String text, List<T> originalList) {
        return null;
    }
}