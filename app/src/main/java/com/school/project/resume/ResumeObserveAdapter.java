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

package com.school.project.resume;

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
import com.school.project.bean.ResumeEntity;
import com.school.project.databinding.LayoutJobItemBinding;
import com.school.project.databinding.LayoutSkillItemBinding;
import com.school.project.utils.DialogUtils;

public class ResumeObserveAdapter extends PagedListAdapter<ResumeEntity, ResumeObserveAdapter.ResumeViewHolder> {

    private boolean mIsItemEnable;

    public ResumeObserveAdapter(boolean isItemEnable) {
        super(new DiffUtil.ItemCallback<ResumeEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull ResumeEntity resume, @NonNull ResumeEntity t1) {
                return resume.rid == t1.rid;
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull ResumeEntity resume, @NonNull ResumeEntity t1) {
                return resume == t1;
            }
        });
        mIsItemEnable = isItemEnable;
    }

    @Override
    public ResumeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ResumeEntity.TYPE_SKILL) {
            LayoutSkillItemBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_skill_item, parent, false);
            return new ResumeViewHolder(viewDataBinding.getRoot());
        } else {
            LayoutJobItemBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_job_item, parent, false);
            return new ResumeViewHolder(viewDataBinding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(ResumeViewHolder holder, int position) {
        ViewDataBinding viewDataBinding = DataBindingUtil.getBinding(holder.itemView);
        final ResumeEntity item = getItem(position);
        item.isEnable = mIsItemEnable;
        if (viewDataBinding instanceof LayoutSkillItemBinding) {
            ((LayoutSkillItemBinding) viewDataBinding).setResume(getItem(position));
        }
        if (viewDataBinding instanceof LayoutJobItemBinding) {
            ((LayoutJobItemBinding) viewDataBinding).setResume(getItem(position));
        }
        viewDataBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                if (item.isEnable) {
                    DialogUtils.showNormalDialog(v.getContext(), "是否删除？", new DialogUtils.OnAppDialogListener() {
                        @Override
                        public void onCommit() {
                            item.delete();
                        }
                    });
                }
                return false;
            }
        });
        viewDataBinding.executePendingBindings();
    }

    @Override
    public int getItemViewType(int position) {
        ResumeEntity item = getItem(position);
        return item != null ? item.type : 0;
    }

    static class ResumeViewHolder extends RecyclerView.ViewHolder {

        public ResumeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
