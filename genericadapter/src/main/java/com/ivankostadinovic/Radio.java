package com.ivankostadinovic;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import org.jetbrains.annotations.NotNull;

public class Radio {
    public long id;
    public String name;

    public static class ItemCallback extends DiffUtil.ItemCallback<Radio> {

        @Override
        public boolean areItemsTheSame(@NonNull @NotNull Radio oldItem, @NonNull @NotNull Radio newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull @NotNull Radio oldItem, @NonNull @NotNull Radio newItem) {
            return oldItem.id == newItem.id
                && TextUtils.equals(oldItem.name, newItem.name);
        }
    }
}