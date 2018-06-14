package de.appmotion.udacitybaking.view.recipedetailstep;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import de.appmotion.udacitybaking.BuildConfig;
import de.appmotion.udacitybaking.R;
import de.appmotion.udacitybaking.data.model.RecipeStep;
import de.appmotion.udacitybaking.databinding.ActivityRecipeDetailStepBinding;
import de.appmotion.udacitybaking.view.BaseActivity;
import de.appmotion.udacitybaking.view.recipedetail.RecipeDetailActivity;

/**
 * An activity representing a single TestItem detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeDetailActivity}.
 */
public class RecipeDetailStepActivity extends BaseActivity {

  // Constant for logging
  private static final String TAG = RecipeDetailStepActivity.class.getSimpleName();

  public final static String EXTRA_RECIPE_STEP_OBJECT = BuildConfig.APPLICATION_ID + ".recipe_step_object";

  private RecipeStep mRecipeStep;

  private ActivityRecipeDetailStepBinding mStepBinding;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mStepBinding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail_step);

    setSupportActionBar(mStepBinding.toolbar);

    if (getIntent() != null && getIntent().hasExtra(EXTRA_RECIPE_STEP_OBJECT)) {
      mRecipeStep = getIntent().getParcelableExtra(EXTRA_RECIPE_STEP_OBJECT);
    }

    mStepBinding.fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
      }
    });

    // Show the Up button in the action bar.
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // savedInstanceState is non-null when there is fragment state
    // saved from previous configurations of this activity
    // (e.g. when rotating the screen from portrait to landscape).
    // In this case, the fragment will automatically be re-added
    // to its container so we don't need to manually add it.
    // For more information, see the Fragments API guide at:
    //
    // http://developer.android.com/guide/components/fragments.html
    //
    if (savedInstanceState == null) {
      // Create the detail fragment and add it to the activity
      // using a fragment transaction.
      Bundle arguments = new Bundle();
      arguments.putParcelable(RecipeDetailStepFragment.EXTRA_RECIPE_STEP_OBJECT, mRecipeStep);
      RecipeDetailStepFragment fragment = new RecipeDetailStepFragment();
      fragment.setArguments(arguments);
      getSupportFragmentManager().beginTransaction().add(R.id.recipe_detail_step_container, fragment).commit();
    }
  }

  @Override protected void onResume() {
    super.onResume();
    if (mRecipeStep != null) {
      mStepBinding.toolbar.setTitle(mRecipeStep.getDescriptionShort());
      setTitle(mRecipeStep.getDescriptionShort());
    } else {
      mStepBinding.toolbar.setTitle(getTitle());
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
      // activity, the Up button is shown. For
      // more details, see the Navigation pattern on Android Design:
      //
      // http://developer.android.com/design/patterns/navigation.html#up-vs-back
      //
      navigateUpTo(new Intent(this, RecipeDetailActivity.class));
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
