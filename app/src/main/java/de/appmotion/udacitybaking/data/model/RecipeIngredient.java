package de.appmotion.udacitybaking.data.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import de.appmotion.udacitybaking.data.BakingContract;

/**
 * Immutable model class for a RecipeIngredient.
 */
public final class RecipeIngredient implements Parcelable {

  private long mId;
  private long mRecipeId;
  private String mIngredient;
  private String mMeasure;
  private int mQuantity;
  private long mTimestamp;

  public RecipeIngredient() {
  }

  protected RecipeIngredient(Parcel in) {
    mId = in.readLong();
    mRecipeId = in.readLong();
    mIngredient = in.readString();
    mMeasure = in.readString();
    mQuantity = in.readInt();
    mTimestamp = in.readLong();
  }

  /**
   * Use this constructor to return a RecipeIngredient from a Cursor
   *
   * @return {@link RecipeIngredient}
   */
  public static RecipeIngredient from(Cursor cursor) {
    final RecipeIngredient ingredient = new RecipeIngredient();
    ingredient.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BakingContract.RecipeIngredientEntry._ID)));
    ingredient.setRecipeId(cursor.getLong(cursor.getColumnIndexOrThrow(BakingContract.RecipeIngredientEntry.COLUMN_RECIPE_ID)));
    ingredient.setIngredient(cursor.getString(cursor.getColumnIndexOrThrow(BakingContract.RecipeIngredientEntry.COLUMN_INGREDIENT)));
    ingredient.setMeasure(cursor.getString(cursor.getColumnIndexOrThrow(BakingContract.RecipeIngredientEntry.COLUMN_MEASURE)));
    ingredient.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow(BakingContract.RecipeIngredientEntry.COLUMN_QUANTITY)));
    ingredient.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(BakingContract.RecipeIngredientEntry.COLUMN_TIMESTAMP)));
    return ingredient;
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

  public String getIngredient() {
    return mIngredient;
  }

  public void setIngredient(String ingredient) {
    mIngredient = ingredient;
  }

  public String getMeasure() {
    return mMeasure;
  }

  public void setMeasure(String measure) {
    mMeasure = measure;
  }

  public int getQuantity() {
    return mQuantity;
  }

  public void setQuantity(int quantity) {
    mQuantity = quantity;
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
    dest.writeString(mIngredient);
    dest.writeString(mMeasure);
    dest.writeInt(mQuantity);
    dest.writeLong(mTimestamp);
  }

  @SuppressWarnings("unused") public static final Creator<RecipeIngredient> CREATOR = new Creator<RecipeIngredient>() {
    @Override public RecipeIngredient createFromParcel(Parcel in) {
      return new RecipeIngredient(in);
    }

    @Override public RecipeIngredient[] newArray(int size) {
      return new RecipeIngredient[size];
    }
  };
}
