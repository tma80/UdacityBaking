package de.appmotion.udacitybaking.view.recipedetail;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import de.appmotion.udacitybaking.BuildConfig;
import de.appmotion.udacitybaking.R;
import de.appmotion.udacitybaking.data.BakingContract;
import de.appmotion.udacitybaking.data.model.Recipe;
import de.appmotion.udacitybaking.data.model.RecipeStep;
import de.appmotion.udacitybaking.databinding.ActivityRecipeDetailBinding;
import de.appmotion.udacitybaking.view.BaseActivity;
import de.appmotion.udacitybaking.view.recipedetailstep.RecipeDetailStepActivity;
import de.appmotion.udacitybaking.view.recipedetailstep.RecipeDetailStepFragment;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An activity representing a list of TestItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailStepActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailActivity extends BaseActivity
    implements LoaderManager.LoaderCallbacks<Cursor>, RecipeDetailAdapter.ListItemClickListener {

  // Constant for logging
  private static final String TAG = RecipeDetailActivity.class.getSimpleName();

  public final static String EXTRA_RECIPE_OBJECT = BuildConfig.APPLICATION_ID + ".recipe_object";

  // This number will uniquely identify a CursorLoader for loading data from 'recipe_step' DB table.
  private static final int CURSOR_LOADER_RECIPE_STEP = 1;

  // Save Position of recipe step list via onSaveInstanceState
  private static final String STATE_LIST_POSITION = "list_position";

  private int mListPosition = RecyclerView.NO_POSITION;

  private Recipe mRecipe;

  private ActivityRecipeDetailBinding mDetailBinding;

  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet
   * device.
   */
  private boolean mTwoPane;
  private AtomicBoolean mFirstLoad;
  private RecipeDetailAdapter mRecipeDetailAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail);

    updateValuesFromBundle(savedInstanceState);
    mFirstLoad = new AtomicBoolean(true);

    setSupportActionBar(mDetailBinding.toolbar);

    if (getIntent() != null && getIntent().hasExtra(EXTRA_RECIPE_OBJECT)) {
      mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE_OBJECT);
    }

    mDetailBinding.fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
      }
    });
    // Show the Up button in the action bar.
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    if (mDetailBinding.detailList.recipeDetailContainer != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-w900dp).
      // If this view is present, then the
      // activity should be in two-pane mode.
      mTwoPane = true;
    }

    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    mRecipeDetailAdapter = new RecipeDetailAdapter(this, this);
    mDetailBinding.detailList.recyclerview.setLayoutManager(layoutManager);
    mDetailBinding.detailList.recyclerview.setHasFixedSize(true);
    mDetailBinding.detailList.recyclerview.setAdapter(mRecipeDetailAdapter);

    getSupportLoaderManager().initLoader(CURSOR_LOADER_RECIPE_STEP, null, this);
  }

  @Override protected void onPause() {
    super.onPause();
    mListPosition = ((LinearLayoutManager) mDetailBinding.detailList.recyclerview.getLayoutManager()).findFirstVisibleItemPosition();
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putInt(STATE_LIST_POSITION, mListPosition);
  }

  @Override protected void onResume() {
    super.onResume();
    if (mRecipe != null) {
      mDetailBinding.toolbar.setTitle(mRecipe.getName());
      setTitle(mRecipe.getName());
    } else {
      mDetailBinding.toolbar.setTitle(getTitle());
    }
  }

  /*
  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }
  */

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    if (id == android.R.id.home) {
      // This ID represents the Home or Up button. In the case of this
      // activity, the Up button is shown. Use NavUtils to allow users
      // to navigate up one level in the application structure. For
      // more details, see the Navigation pattern on Android Design:
      //
      // http://developer.android.com/design/patterns/navigation.html#up-vs-back
      //
      NavUtils.navigateUpFromSameTask(this);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @NonNull @Override public Loader<Cursor> onCreateLoader(int loaderId, @Nullable Bundle args) {
    switch (loaderId) {

      case CURSOR_LOADER_RECIPE_STEP:
        /* URI for all rows of recipe step data in our recipe_step table, which contain ID of mRecipe */
        Uri recipeQueryUri = BakingContract.RecipeStepEntry.CONTENT_URI;
        String selection = BakingContract.RecipeStepEntry.COLUMN_RECIPE_ID + " = " + mRecipe.getRecipeId();
        return new CursorLoader(this, recipeQueryUri, null, selection, null, null);

      default:
        throw new RuntimeException("Loader Not Implemented: " + loaderId);
    }
  }

  @Override public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
    switch (loader.getId()) {

      case CURSOR_LOADER_RECIPE_STEP:
        if (cursor != null) {
          // Data loaded
          if (cursor.getCount() != 0) {
            // Show data from ContentProvider query

            //showRecipeDataView();

            mRecipeDetailAdapter.swapCursor(cursor);
            if (mListPosition == RecyclerView.NO_POSITION) {
              mListPosition = 0;
            }
            mDetailBinding.detailList.recyclerview.smoothScrollToPosition(mListPosition);
          }
          // Data empty
          else {
            mRecipeDetailAdapter.swapCursor(cursor);
            if (!mFirstLoad.compareAndSet(true, false)) {

              //showEmptyView();

            }
          }
        }
        // Data not available
        else {
          mRecipeDetailAdapter.swapCursor(null);
          if (!mFirstLoad.compareAndSet(true, false)) {

            //showEmptyView();

          }
        }
        break;
    }
  }

  @Override public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    mRecipeDetailAdapter.swapCursor(null);
  }

  @Override public void onListItemClick(@NonNull RecipeStep step) {
    //showMessage(step.getDescriptionShort());
    if (mTwoPane) {
      Bundle arguments = new Bundle();
      arguments.putParcelable(RecipeDetailStepFragment.EXTRA_RECIPE_STEP_OBJECT, step);
      RecipeDetailStepFragment fragment = new RecipeDetailStepFragment();
      fragment.setArguments(arguments);
      getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, fragment).commit();
    } else {
      // Show RecipeDetailStepActivity
      Intent intent = new Intent(this, RecipeDetailStepActivity.class);
      intent.putExtra(RecipeDetailStepActivity.EXTRA_RECIPE_STEP_OBJECT, step);
      startActivity(intent);
    }
  }

  private void updateValuesFromBundle(Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      if (savedInstanceState.keySet().contains(STATE_LIST_POSITION)) {
        mListPosition = savedInstanceState.getInt(STATE_LIST_POSITION, RecyclerView.NO_POSITION);
      }
    }
  }
}
