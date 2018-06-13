package de.appmotion.udacitybaking;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.appmotion.udacitybaking.dummy.DummyContent;

/**
 * A fragment representing a single TestItem detail screen.
 * This fragment is either contained in a {@link RecipeDetailActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailStepActivity}
 * on handsets.
 */
public class RecipeDetailStepFragment extends Fragment {

  // Constant for logging
  private static final String TAG = RecipeDetailStepFragment.class.getSimpleName();
  /**
   * The fragment argument representing the item ID that this fragment
   * represents.
   */
  public static final String ARG_ITEM_ID = "item_id";

  /**
   * The dummy content this fragment is presenting.
   */
  private DummyContent.DummyItem mItem;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public RecipeDetailStepFragment() {
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getArguments().containsKey(ARG_ITEM_ID)) {
      // Load the dummy content specified by the fragment
      // arguments. In a real-world scenario, use a Loader
      // to load content from a content provider.
      mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

      Activity activity = this.getActivity();
      CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
      if (appBarLayout != null) {
        appBarLayout.setTitle(mItem.content);
      }
      activity.setTitle(mItem.content);
    }
  }

  @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_recipe_detail_step, container, false);

    // Show the dummy content as text in a TextView.
    if (mItem != null) {
      ((TextView) rootView.findViewById(R.id.recipe_detail_step_text)).setText(mItem.details);
    }

    return rootView;
  }
}
