package de.appmotion.udacitybaking.view.recipedetailstep;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.appmotion.udacitybaking.BuildConfig;
import de.appmotion.udacitybaking.R;
import de.appmotion.udacitybaking.data.model.RecipeStep;
import de.appmotion.udacitybaking.view.recipedetail.RecipeDetailActivity;

/**
 * A fragment representing a single TestItem detail screen.
 * This fragment is either contained in a {@link RecipeDetailActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailStepActivity}
 * on handsets.
 */
public class RecipeDetailStepFragment extends Fragment {

  // Constant for logging
  private static final String TAG = RecipeDetailStepFragment.class.getSimpleName();

  public final static String EXTRA_RECIPE_STEP_OBJECT = BuildConfig.APPLICATION_ID + ".recipe_step_object";

  private RecipeStep mRecipeStep;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public RecipeDetailStepFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(EXTRA_RECIPE_STEP_OBJECT)) {
      mRecipeStep = getArguments().getParcelable(EXTRA_RECIPE_STEP_OBJECT);
    }
  }

  @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_detail_step, container, false);

    if (mRecipeStep != null) {
      ((TextView) rootView.findViewById(R.id.recipe_detail_step_text)).setText(mRecipeStep.getDescription());
    }

    return rootView;
  }
}
