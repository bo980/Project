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

package com.school.project.message;

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
import com.school.project.bean.MessageEntity;
import com.school.project.databinding.LayoutMessageItemBinding;
import com.school.project.utils.DialogUtils;

public class MessageObserveAdapter extends PagedListAdapter<MessageEntity, MessageObserveAdapter.MessageViewHolder> {
    private int mUid;

    protected MessageObserveAdapter(int uid) {
        super(new DiffUtil.ItemCallback<MessageEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull MessageEntity msg, @NonNull MessageEntity t1) {
                return msg.mid == t1.mid;
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull MessageEntity msg, @NonNull MessageEntity t1) {
                return msg == t1;
            }
        });
        mUid = uid;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutMessageItemBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.layout_message_item, parent, false);
        return new MessageViewHolder(viewDataBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        ViewDataBinding viewDataBinding = DataBindingUtil.getBinding(holder.itemView);
        MessageEntity item = getItem(position);
        item.type = item.outUid == mUid || item.type == MessageEntity.TYPE_OPEN
                ? MessageEntity.TYPE_OPEN : MessageEntity.TYPE_NONE;
        if (viewDataBinding instanceof LayoutMessageItemBinding) {
            LayoutMessageItemBinding itemBinding = ((LayoutMessageItemBinding) viewDataBinding);
            itemBinding.setMessage(item);
            itemBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    DialogUtils.showNormalDialog(v.getContext(),"是否删除此条消息？",new DialogUtils.OnAppDialogListener(){
                        @Override
                        public void onCommit() {
                            LayoutMessageItemBinding itemBinding = DataBindingUtil.getBinding(v);
                            assert itemBinding != null;
                            itemBinding.getMessage().delete();
                        }
                    });
                    return false;
                }
            });
        }
        viewDataBinding.executePendingBindings();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}
