package com.am.font.ui.main;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.am.appcompat.app.AppCompatActivity;
import com.am.font.ui.R;
import com.am.font.ui.opentype.OpenTypeActivity;

public class MainActivity extends AppCompatActivity implements MainView,
        MainViewHolder.OnViewHolderListener {

    private final MainPresenter mPresenter =
            new MainPresenter().setViewHolder(getViewHolder());
    private final MainAdapter mAdapter = new MainAdapter(mPresenter, this);

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final RecyclerView list = findViewById(R.id.otl_content);
        final Drawable divider = ContextCompat.getDrawable(this, R.drawable.divider_common);
        if (divider != null) {
            final DividerItemDecoration decoration = new DividerItemDecoration(list.getContext(),
                    DividerItemDecoration.VERTICAL);
            decoration.setDrawable(divider);
            list.addItemDecoration(decoration);
        }

        list.setAdapter(mAdapter);
        mPresenter.loadOpenType();
    }

    // View
    @Override
    public void onOpenTypeLoaded() {
        mAdapter.notifyDataSetChanged();
    }

    // Listener
    @Override
    public void onItemClick(Object item) {
        OpenTypeActivity.start(this, mPresenter.getItemPath(item));
    }
}