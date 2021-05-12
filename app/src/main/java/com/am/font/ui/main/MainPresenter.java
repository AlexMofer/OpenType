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

import com.am.mvp.core.MVPPresenter;

/**
 * Presenter
 */
class MainPresenter extends MVPPresenter<MainView, MainModel> implements
        MainView, MainDataAdapter {

    MainPresenter() {
        setModel(new MainModel());
    }

    // View
    @Override
    public void onOpenTypeLoaded() {
        final MainView view = getView();
        if (view != null)
            view.onOpenTypeLoaded();
    }

    // AdapterViewModel
    @Override
    public int getItemCount() {
        return getModel().getItemCount();
    }

    @Override
    public Object getItem(int position) {
        return getModel().getItem(position);
    }

    @Override
    public String getItemName(Object item) {
        return getModel().getItemName(item);
    }

    // ViewModel
    @Override
    public void loadOpenType() {
        getModel().loadOpenType();
    }

    @Override
    public String getItemPath(Object item) {
        return getModel().getItemPath(item);
    }
}