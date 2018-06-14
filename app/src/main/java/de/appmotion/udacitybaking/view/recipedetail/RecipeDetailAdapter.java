package de.appmotion.udacitybaking.view.recipedetail;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.appmotion.udacitybaking.R;
import de.appmotion.udacitybaking.data.BakingContract;
import de.appmotion.udacitybaking.data.model.RecipeStep;
import de.appmotion.udacitybaking.databinding.RecipeCardBinding;

/**
 * {@link RecyclerView.Adapter} that can display a {@link RecipeStep} from a {@link Cursor}.
 */
class RecipeDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private static final int VIEW_TYPE_DEFAULT = 0;
  private final Context mContext;
  private final ListItemClickListener mOnClickListener;
  // Holds on to the cursor to display the step list
  private Cursor mCursor;

  RecipeDetailAdapter(@NonNull Context context, ListItemClickListener listener) {
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
        viewHolder = new ViewHolderSteptItem(view);
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
        final ViewHolderSteptItem viewHolder = (ViewHolderSteptItem) holder;
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
    void onListItemClick(RecipeStep step);
  }

  private class ViewHolderSteptItem extends RecyclerView.ViewHolder implements View.OnClickListener {
    RecipeCardBinding mItemBinding;

    ViewHolderSteptItem(View itemView) {
      super(itemView);
      mItemBinding = DataBindingUtil.bind(itemView);
      itemView.setOnClickListener(this);
    }

    void bind() {
      final RecipeStep step = RecipeStep.from(mCursor);

      // Step Description short
      mItemBinding.recipeName.setText(step.getDescriptionShort());
    }

    /**
     * Called whenever a user clicks on an item in the list.
     *
     * @param v The View that was clicked
     */
    @Override public void onClick(View v) {
      int adapterPosition = getAdapterPosition();
      mCursor.moveToPosition(adapterPosition);
      final RecipeStep step = RecipeStep.from(mCursor);
      mOnClickListener.onListItemClick(step);
    }
  }
}