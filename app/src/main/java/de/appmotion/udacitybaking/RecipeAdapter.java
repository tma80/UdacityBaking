package de.appmotion.udacitybaking;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import de.appmotion.udacitybaking.data.BakingContract;
import de.appmotion.udacitybaking.data.model.Recipe;
import de.appmotion.udacitybaking.databinding.RecipeCardBinding;
import eightbitlab.com.blurview.RenderScriptBlur;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Recipe} from a {@link Cursor}.
 */
class RecipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int VIEW_TYPE_DEFAULT = 0;
  private final Context mContext;
  private final ListItemClickListener mOnClickListener;
  // Holds on to the cursor to display the recipe list
  private Cursor mCursor;

  RecipeAdapter(@NonNull Context context, ListItemClickListener listener) {
    mContext = context;
    mOnClickListener = listener;
    setHasStableIds(true);
  }

  @Override public int getItemViewType(int position) {
    return VIEW_TYPE_DEFAULT;
  }

  @NonNull @Override public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(mContext);

    RecyclerView.ViewHolder viewHolder;

    switch (viewType) {
      case VIEW_TYPE_DEFAULT: {
        final View view = inflater.inflate(R.layout.recipe_card, parent, false);
        viewHolder = new ViewHolderRecipeItem(view);
        break;
      }
      default:
        throw new IllegalArgumentException("Invalid view type, value of " + viewType);
    }

    return viewHolder;
  }

  // Replace the contents of a view (invoked by the layout manager)
  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    // Move the mCursor to the position of the item to be displayed
    if (!mCursor.moveToPosition(position)) {
      return; // bail if returned null
    }

    int viewType = getItemViewType(position);

    switch (viewType) {
      case VIEW_TYPE_DEFAULT: {
        final ViewHolderRecipeItem viewHolder = (ViewHolderRecipeItem) holder;
        viewHolder.bind();
        break;
      }
      default:
        throw new IllegalArgumentException("Invalid view type, value of " + viewType);
    }
  }

  @Override public int getItemCount() {
    if (mCursor == null) {
      return 0;
    }
    return mCursor.getCount();
  }

  @Override public long getItemId(int position) {
    if (mCursor != null && mCursor.moveToPosition(position)) {
      return mCursor.getLong(mCursor.getColumnIndexOrThrow(BakingContract.RecipeEntry._ID));
    }
    return super.getItemId(position);
  }

  /**
   * Swaps the Cursor currently held in the adapter with a new one
   * and triggers a UI refresh
   *
   * @param newCursor the new cursor that will replace the existing one
   */
  public void swapCursor(Cursor newCursor) {
    mCursor = newCursor;
    notifyDataSetChanged();
  }

  /**
   * The interface that receives onClick messages.
   */
  public interface ListItemClickListener {
    void onListItemClick(Recipe recipe);
  }

  private class ViewHolderRecipeItem extends RecyclerView.ViewHolder implements View.OnClickListener {
    RecipeCardBinding mItemBinding;

    ViewHolderRecipeItem(View itemView) {
      super(itemView);
      mItemBinding = DataBindingUtil.bind(itemView);
      itemView.setOnClickListener(this);
    }

    void bind() {
      final Recipe recipe = Recipe.from(mCursor);

      // Recipe Name
      mItemBinding.recipeName.setText(recipe.getName());

      // Recipe Image
      String recipeImage = recipe.getImage();
      if (recipeImage.trim().length() != 0) {
        Picasso.with(itemView.getContext())
            .load(recipeImage)
            .placeholder(android.R.drawable.screen_background_light_transparent)
            .error(R.drawable.ic_image_black_24dp)
            .into(mItemBinding.recipeImage, new Callback() {
              @Override public void onSuccess() {
              }

              @Override public void onError() {
              }
            });
      } else {
        mItemBinding.recipeImage.setImageResource(R.drawable.ic_image_black_24dp);
      }

      // Recipe Image BlurView
      mItemBinding.recipeImageBlurView.setupWith(mItemBinding.recipeCard).blurAlgorithm(new RenderScriptBlur(mContext)).blurRadius(4.0f);
    }

    /**
     * Called whenever a user clicks on an item in the list.
     *
     * @param v The View that was clicked
     */
    @Override public void onClick(View v) {
      int adapterPosition = getAdapterPosition();
      mCursor.moveToPosition(adapterPosition);
      final Recipe recipe = Recipe.from(mCursor);
      mOnClickListener.onListItemClick(recipe);
    }
  }
}