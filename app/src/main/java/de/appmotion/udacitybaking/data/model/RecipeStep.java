package de.appmotion.udacitybaking.data.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import de.appmotion.udacitybaking.data.BakingContract;

/**
 * Immutable model class for a RecipeStep.
 */
public final class RecipeStep implements Parcelable {

  private long mId;
  private long mRecipeId;
  private long mStepId;
  private String mDescription;
  private String mDescriptionShort;
  private String mThumbnailUrl;
  private String mVideoUrl;
  private long mTimestamp;

  public RecipeStep() {
  }

  protected RecipeStep(Parcel in) {
    mId = in.readLong();
    mRecipeId = in.readLong();
    mStepId = in.readLong();
    mDescription = in.readString();
    mDescriptionShort = in.readString();
    mThumbnailUrl = in.readString();
    mVideoUrl = in.readString();
    mTimestamp = in.readLong();
  }

  /**
   * Use this constructor to return a RecipeStep from a Cursor
   *
   * @return {@link RecipeStep}
   */
  public static RecipeStep from(Cursor cursor) {
    final RecipeStep step = new RecipeStep();
    step.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BakingContract.RecipeStepEntry._ID)));
    step.setRecipeId(cursor.getLong(cursor.getColumnIndexOrThrow(BakingContract.RecipeStepEntry.COLUMN_RECIPE_ID)));
    step.setStepId(cursor.getLong(cursor.getColumnIndexOrThrow(BakingContract.RecipeStepEntry.COLUMN_STEP_ID)));
    step.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(BakingContract.RecipeStepEntry.COLUMN_DESCRIPTION)));
    step.setDescriptionShort(cursor.getString(cursor.getColumnIndexOrThrow(BakingContract.RecipeStepEntry.COLUMN_DESCRIPTION_SHORT)));
    step.setThumbnailUrl(cursor.getString(cursor.getColumnIndexOrThrow(BakingContract.RecipeStepEntry.COLUMN_THUMBNAIL_URL)));
    step.setVideoUrl(cursor.getString(cursor.getColumnIndexOrThrow(BakingContract.RecipeStepEntry.COLUMN_VIDEO_URL)));
    step.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(BakingContract.RecipeStepEntry.COLUMN_TIMESTAMP)));
    return step;
  }

  public long getId() {
    return mId;
  }

  public void setId(long id) {
    mId = id;
  }

  public long getRecipeId() {
    return mRecipeId;
  }

  public void setRecipeId(long recipeId) {
    mRecipeId = recipeId;
  }

  public long getStepId() {
    return mStepId;
  }

  public void setStepId(long stepId) {
    mStepId = stepId;
  }

  public String getDescription() {
    return mDescription;
  }

  public void setDescription(String description) {
    mDescription = description;
  }

  public String getDescriptionShort() {
    return mDescriptionShort;
  }

  public void setDescriptionShort(String descriptionShort) {
    mDescriptionShort = descriptionShort;
  }

  public String getThumbnailUrl() {
    return mThumbnailUrl;
  }

  public void setThumbnailUrl(String thumbnailUrl) {
    mThumbnailUrl = thumbnailUrl;
  }

  public String getVideoUrl() {
    return mVideoUrl;
  }

  public void setVideoUrl(String videoUrl) {
    mVideoUrl = videoUrl;
  }

  public long getTimestamp() {
    return mTimestamp;
  }

  public void setTimestamp(long timestamp) {
    mTimestamp = timestamp;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(mId);
    dest.writeLong(mRecipeId);
    dest.writeLong(mStepId);
    dest.writeString(mDescription);
    dest.writeString(mDescriptionShort);
    dest.writeString(mThumbnailUrl);
    dest.writeString(mVideoUrl);
    dest.writeLong(mTimestamp);
  }

  @SuppressWarnings("unused") public static final Creator<RecipeStep> CREATOR = new Creator<RecipeStep>() {
    @Override public RecipeStep createFromParcel(Parcel in) {
      return new RecipeStep(in);
    }

    @Override public RecipeStep[] newArray(int size) {
      return new RecipeStep[size];
    }
  };
}
