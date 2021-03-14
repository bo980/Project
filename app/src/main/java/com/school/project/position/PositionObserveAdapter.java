/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.school.project.position;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.school.project.R;
import com.school.project.bean.PositionEntity;
import com.school.project.databinding.LayoutPositionItemBinding;

public class PositionObserveAdapter extends PagedListAdapter<PositionEntity, PositionObserveAdapter.ResumeViewHolder> {

    private boolean mIsItemEnable;

    public PositionObserveAdapter(boolean isItemEnable) {
        super(new DiffUtil.ItemCallback<PositionEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull PositionEntity resume, @NonNull PositionEntity t1) {
                return resume.pid == t1.pid;
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull PositionEntity resume, @NonNull PositionEntity t1) {
                return resume == t1;
            }
        });
        mIsItemEnable = isItemEnable;
    }

    @Override
    public ResumeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutPositionItemBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_position_item, parent, false);
        return new ResumeViewHolder(viewDataBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(ResumeViewHolder holder, int position) {
        ViewDataBinding viewDataBinding = DataBindingUtil.getBinding(holder.itemView);
        getItem(position).isEnable = mIsItemEnable;
        if (viewDataBinding instanceof LayoutPositionItemBinding) {
            ((LayoutPositionItemBinding) viewDataBinding).setPosition(getItem(position));
        }
        viewDataBinding.executePendingBindings();
    }

    static class ResumeViewHolder extends RecyclerView.ViewHolder {
        public ResumeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
