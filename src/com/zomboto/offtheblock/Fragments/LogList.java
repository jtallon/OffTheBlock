package com.zomboto.offtheblock.Fragments;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import com.zomboto.offtheblock.Providers.OffTheBlockProvider;
import com.zomboto.offtheblock.R;

public class LogList extends ListFragment
        implements SearchView.OnQueryTextListener, LoaderManager.LoaderCallbacks<Cursor> {

    // This is the Adapter being used to display the list's data.
    SimpleCursorAdapter mAdapter;

    // If non-null, this is the current filter the user has provided.
    String mCurFilter;

    @Override public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setEmptyText(getString(R.string.no_logs));
        setHasOptionsMenu(true);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1, null,
                new String[] {OffTheBlockProvider.Log.DESCRIPTION},
                new int[] { android.R.id.text1 }, 0);
        setListAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Place an action bar item for searching.
        MenuItem item = menu.add("Search");
        item.setIcon(android.R.drawable.ic_menu_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        SearchView sv = new SearchView(getActivity());
        sv.setOnQueryTextListener(this);
        item.setActionView(sv);
    }

    public boolean onQueryTextChange(String newText) {
        mCurFilter = !TextUtils.isEmpty(newText) ? newText : null;
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    @Override public boolean onQueryTextSubmit(String query) {
        //mCurFilter = !TextUtils.isEmpty(query) ? query : null;
        //getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    @Override public void onListItemClick(ListView l, View v, int position, long id) {
        //NavigationHelper.NavigateTo(getFragmentManager(), new Log());
    }

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String selection;
        String[] selectionArgs;
        if (mCurFilter != null)
        {
            setEmptyText(getString(R.string.no_logs_criteria));
            selection = String.format("%s LIKE ?", OffTheBlockProvider.Log.DESCRIPTION);
            selectionArgs = new String[] {String.format("%%%s%%", mCurFilter)};
        }
        else
        {
            setEmptyText(getString(R.string.no_logs));
            selection = null;
            selectionArgs = null;
        }

        return new CursorLoader(getActivity(), OffTheBlockProvider.Log.CONTENT_URI,
                new String[] { OffTheBlockProvider.Log._ID, OffTheBlockProvider.Log.DESCRIPTION},
                selection, selectionArgs,
                String.format("%s ASC", OffTheBlockProvider.Log._ID));
    }

    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}