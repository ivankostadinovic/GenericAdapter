package com.ivankostadinovic.genericadapter;

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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.Disposable;
import kotlin.collections.CollectionsKt;

/**
 * @author manoj.bhadane manojbhadane777@gmail.com
 * edited by ivankostadinovic1994@outlook.com
 */

public abstract class GenericFilterAdapter<T, D extends ViewDataBinding> extends RecyclerView.Adapter<GenericFilterAdapter<T, D>.ItemViewHolder> {

    private List<T> list = new ArrayList<>();
    private List<T> filteredList = new ArrayList<>();
    private final int layoutResId;
    private Disposable searchDisposable;
    private final EditText searchView;
    public static final int DEFAULT_PAGINATION_OFFSET = 3;

    public int paginationOffset = DEFAULT_PAGINATION_OFFSET;

    public abstract void onBindData(T model, int position, D dataBinding);

    public abstract void onItemClick(T model, int position);

    public abstract boolean filter(T item, String text);

    public void onCreateHolder(D dataBinding) {
    }

    public void loadMoreItems(int loadedCount) {

    }

    public GenericFilterAdapter(List<T> arrayList, @LayoutRes int layoutResId, EditText searchView) {
        this.list.addAll(arrayList);
        this.filteredList.addAll(arrayList);
        this.layoutResId = layoutResId;
        this.searchView = searchView;
    }

    public GenericFilterAdapter(List<T> arrayList, @LayoutRes int layoutResId, EditText searchView, int paginationOffset) {
        this.list.addAll(arrayList);
        this.filteredList.addAll(arrayList);
        this.layoutResId = layoutResId;
        this.searchView = searchView;
        this.paginationOffset = paginationOffset;
    }

    private void filterList(CharSequence text) {
        filteredList = CollectionsKt.filter(list, item -> filter(item, text.toString()));
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        if (searchDisposable != null) {
            searchDisposable.dispose();
        }
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        searchDisposable = RxTextView
            .textChanges(searchView)
            .subscribe(this::filterList);
        super.onAttachedToRecyclerView(recyclerView);
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
        final T item = filteredList.get(position);
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
        return filteredList.size();
    }

    public void setItems(List<T> arrayList) {
        if (list != arrayList) {
            list = new ArrayList<>();
            list.addAll(arrayList);
            filteredList = new ArrayList<>();
            filteredList.addAll(arrayList);
            filterList(searchView.getText().toString());
        }
    }

    public void clearItems() {
        list.clear();
        filteredList.clear();
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return filteredList.get(position);
    }

    public void removeItem(int position) {
        T itemToRemove = filteredList.get(position);
        filteredList.remove(position);
        list.remove(itemToRemove);
        notifyItemRemoved(position);
    }

    public void removeItem(T item) {
        int position = list.indexOf(item);
        filteredList.remove(item);
        list.remove(item);
        notifyItemRemoved(position);
    }

    public void addItems(List<T> items) {
        list.addAll(items);
        List<T> newFilteredItems = CollectionsKt.filter(items, item -> filter(item, searchView.getText().toString()));
        filteredList.addAll(newFilteredItems);
        notifyItemRangeInserted(filteredList.size(), newFilteredItems.size());
    }

    public void updateItem(T item, int position) {
        int unfilteredListPosition = list.indexOf(filteredList.get(position));
        list.set(unfilteredListPosition, item);
        filteredList.set(position, item);
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