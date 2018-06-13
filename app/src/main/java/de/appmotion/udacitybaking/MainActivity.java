package de.appmotion.udacitybaking;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import de.appmotion.udacitybaking.data.BakingContract;
import de.appmotion.udacitybaking.data.model.Recipe;
import de.appmotion.udacitybaking.databinding.ActivityMainBinding;
import de.appmotion.udacitybaking.sync.BakingSyncIntentService;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends BaseActivity
    implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>, RecipeAdapter.ListItemClickListener {

  // Constant for logging
  private static final String TAG = MainActivity.class.getSimpleName();

  // This number will uniquely identify a CursorLoader for loading data from 'recipe' DB table.
  private static final int CURSOR_LOADER_RECIPE = 1;

  // Save {@link MenuState} via onSaveInstanceState
  private static final String STATE_RECIPE_LIST_POSITION = "recipe_list_position";

  private int mRecipeListPosition = RecyclerView.NO_POSITION;

  private ActivityMainBinding mMainBinding;
  private RecipeAdapter mRecipeAdapter;

  private AtomicBoolean mFirstLoad;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

    updateValuesFromBundle(savedInstanceState);
    mFirstLoad = new AtomicBoolean(true);

    /*
     * AppBarLayout
     */
    setSupportActionBar(mMainBinding.appBar.toolbar);

    mMainBinding.appBar.fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
      }
    });

    /*
     * DrawerLayout
     */
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mMainBinding.drawerLayout, mMainBinding.appBar.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    mMainBinding.drawerLayout.addDrawerListener(toggle);
    toggle.syncState();

    /*
     * NavigationView
     */
    mMainBinding.navView.setNavigationItemSelectedListener(this);


    /*
     * Content
     */
    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    mRecipeAdapter = new RecipeAdapter(this, this);
    mMainBinding.appBar.content.recyclerview.setLayoutManager(layoutManager);
    mMainBinding.appBar.content.recyclerview.setHasFixedSize(true);
    mMainBinding.appBar.content.recyclerview.setAdapter(mRecipeAdapter);

    showLoadingView();

    /*
     * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
     * created and (if the activity/fragment is currently started) starts the loader. Otherwise
     * the last created loader is re-used.
     */
    getSupportLoaderManager().initLoader(CURSOR_LOADER_RECIPE, null, this);

    BakingSyncIntentService.startImmediateSync(this);
  }

  @Override protected void onPause() {
    super.onPause();
    mRecipeListPosition = ((LinearLayoutManager) mMainBinding.appBar.content.recyclerview.getLayoutManager()).findFirstVisibleItemPosition();
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(STATE_RECIPE_LIST_POSITION, mRecipeListPosition);
  }


  @Override public void onBackPressed() {
    if (mMainBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
      mMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
    } else {
      super.onBackPressed();
    }
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @SuppressWarnings("StatementWithEmptyBody") @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_camera) {
      // Handle the camera action
    } else if (id == R.id.nav_gallery) {

    } else if (id == R.id.nav_slideshow) {

    } else if (id == R.id.nav_manage) {

    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

    }

    mMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  private void showRecipeDataView() {
    mMainBinding.appBar.content.loadingView.setVisibility(View.INVISIBLE);
    mMainBinding.appBar.content.recyclerview.setVisibility(View.VISIBLE);
    mMainBinding.appBar.content.emptyView.setVisibility(View.INVISIBLE);
  }

  private void showEmptyView() {
    mMainBinding.appBar.content.loadingView.setVisibility(View.INVISIBLE);
    mMainBinding.appBar.content.recyclerview.setVisibility(View.INVISIBLE);
    mMainBinding.appBar.content.emptyView.setVisibility(View.VISIBLE);
  }

  private void showLoadingView() {
    mMainBinding.appBar.content.loadingView.setVisibility(View.VISIBLE);
    mMainBinding.appBar.content.recyclerview.setVisibility(View.INVISIBLE);
    mMainBinding.appBar.content.emptyView.setVisibility(View.INVISIBLE);
  }

  @NonNull @Override public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
    switch (loaderId) {

      case CURSOR_LOADER_RECIPE:
        /* URI for all rows of recipe data in our recipe table */
        Uri recipeQueryUri = BakingContract.RecipeEntry.CONTENT_URI;

        return new CursorLoader(this, recipeQueryUri, null, null, null, null);

      default:
        throw new RuntimeException("Loader Not Implemented: " + loaderId);
    }
  }

  @Override public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
    switch (loader.getId()) {

      case CURSOR_LOADER_RECIPE:
        if (cursor != null) {
          // Data loaded
          if (cursor.getCount() != 0) {
            // Show data from ContentProvider query
            showRecipeDataView();
            mRecipeAdapter.swapCursor(cursor);
            if (mRecipeListPosition == RecyclerView.NO_POSITION) {
              mRecipeListPosition = 0;
            }
            mMainBinding.appBar.content.recyclerview.smoothScrollToPosition(mRecipeListPosition);
          }
          // Data empty
          else {
            mRecipeAdapter.swapCursor(cursor);
            if (!mFirstLoad.compareAndSet(true, false)) {
              showEmptyView();
            }
          }
        }
        // Data not available
        else {
          mRecipeAdapter.swapCursor(null);
          if (!mFirstLoad.compareAndSet(true, false)) {
            showEmptyView();
          }
        }
        break;
    }
  }

  @Override public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    mRecipeAdapter.swapCursor(null);
  }

  @Override public void onListItemClick(@NonNull Recipe recipe) {
    //showMessage(recipe.getName());
    // Show RecipeDetailActivity
    Intent intent = new Intent(this, RecipeDetailActivity.class);
    intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_OBJECT, recipe);
    startActivity(intent);
  }

  private void updateValuesFromBundle(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      if (savedInstanceState.keySet().contains(STATE_RECIPE_LIST_POSITION)) {
        mRecipeListPosition = savedInstanceState.getInt(STATE_RECIPE_LIST_POSITION, RecyclerView.NO_POSITION);
      }
    }
  }
}
