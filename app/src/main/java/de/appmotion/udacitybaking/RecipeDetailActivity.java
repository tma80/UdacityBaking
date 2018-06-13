package de.appmotion.udacitybaking;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import de.appmotion.udacitybaking.data.model.Recipe;
import de.appmotion.udacitybaking.dummy.DummyContent;

import java.util.List;

/**
 * An activity representing a list of TestItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailStepActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeDetailActivity extends BaseActivity {

  // Constant for logging
  private static final String TAG = RecipeDetailActivity.class.getSimpleName();

  public final static String EXTRA_RECIPE_OBJECT = BuildConfig.APPLICATION_ID + ".recipe_object";

  private Recipe mRecipe;

  /**
   * Whether or not the activity is in two-pane mode, i.e. running on a tablet
   * device.
   */
  private boolean mTwoPane;
  private Toolbar toolbar;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_recipe_detail);

    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if (getIntent() != null && getIntent().hasExtra(EXTRA_RECIPE_OBJECT)) {
      mRecipe = getIntent().getParcelableExtra(EXTRA_RECIPE_OBJECT);
    }

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
      }
    });
    // Show the Up button in the action bar.
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }

    if (findViewById(R.id.recipe_detail_container) != null) {
      // The detail container view will be present only in the
      // large-screen layouts (res/values-w900dp).
      // If this view is present, then the
      // activity should be in two-pane mode.
      mTwoPane = true;
    }

    View recyclerView = findViewById(R.id.recipe_detail_list);
    assert recyclerView != null;
    setupRecyclerView((RecyclerView) recyclerView);
  }

  @Override protected void onResume() {
    super.onResume();
    if (mRecipe != null) {
      toolbar.setTitle(mRecipe.getName());
      setTitle(mRecipe.getName());
    } else {
      toolbar.setTitle(getTitle());
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

  private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
    recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, DummyContent.ITEMS, mTwoPane));
  }

  public static class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

    private final RecipeDetailActivity mParentActivity;
    private final List<DummyContent.DummyItem> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
      @Override public void onClick(View view) {
        DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();
        if (mTwoPane) {
          Bundle arguments = new Bundle();
          arguments.putString(RecipeDetailStepFragment.ARG_ITEM_ID, item.id);
          RecipeDetailStepFragment fragment = new RecipeDetailStepFragment();
          fragment.setArguments(arguments);
          mParentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.recipe_detail_container, fragment).commit();
        } else {
          Context context = view.getContext();
          Intent intent = new Intent(context, RecipeDetailStepActivity.class);
          intent.putExtra(RecipeDetailStepFragment.ARG_ITEM_ID, item.id);

          context.startActivity(intent);
        }
      }
    };

    SimpleItemRecyclerViewAdapter(RecipeDetailActivity parent, List<DummyContent.DummyItem> items, boolean twoPane) {
      mValues = items;
      mParentActivity = parent;
      mTwoPane = twoPane;
    }

    @NonNull @Override public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_detail_list_content, parent, false);
      return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
      holder.mIdView.setText(mValues.get(position).id);
      holder.mContentView.setText(mValues.get(position).content);

      holder.itemView.setTag(mValues.get(position));
      holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override public int getItemCount() {
      return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
      final TextView mIdView;
      final TextView mContentView;

      ViewHolder(View view) {
        super(view);
        mIdView = view.findViewById(R.id.id_text);
        mContentView = view.findViewById(R.id.content);
      }
    }
  }
}
