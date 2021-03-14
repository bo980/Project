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
import com.school.project.bean.UserEntity;
import com.school.project.databinding.LayoutStudentItemBinding;

public class StudentObserveAdapter extends PagedListAdapter<UserEntity, StudentObserveAdapter.UserEntityViewHolder> {

    protected StudentObserveAdapter() {
        super(new DiffUtil.ItemCallback<UserEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull UserEntity user, @NonNull UserEntity t1) {
                return user.uid == t1.uid;
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull UserEntity user, @NonNull UserEntity t1) {
                return user == t1;
            }
        });
    }

    @Override
    public UserEntityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutStudentItemBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_student_item, parent, false);
        return new UserEntityViewHolder(viewDataBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(UserEntityViewHolder holder, int position) {
        ViewDataBinding viewDataBinding = DataBindingUtil.getBinding(holder.itemView);
        if (viewDataBinding instanceof LayoutStudentItemBinding) {
            ((LayoutStudentItemBinding) viewDataBinding).setStudent(getItem(position));
        }
        viewDataBinding.executePendingBindings();
    }

    static class UserEntityViewHolder extends RecyclerView.ViewHolder {
        public UserEntityViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
