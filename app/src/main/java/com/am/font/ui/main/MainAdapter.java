/*
 * Copyright (C) 2018 AlexMofer
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
package com.am.font.ui.main;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adapter
 */
class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {

    private final MainDataAdapter mAdapter;
    private final MainViewHolder.OnViewHolderListener mListener;

    MainAdapter(MainDataAdapter adapter,
                MainViewHolder.OnViewHolderListener listener) {
        mAdapter = adapter;
        mListener = listener;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        return new MainViewHolder(parent, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        holder.bind(position, mAdapter);
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount();
    }
}
