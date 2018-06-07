package de.appmotion.udacitybaking.data.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import de.appmotion.udacitybaking.data.BakingContract;

/**
 * Immutable model class for a Recipe.
 */
public final class Recipe implements Parcelable {

  private long mId;
  private long mReceipeId;
  private String mImage;
  private String mName;
  private int mServing;
  private long mTimestamp;

  public Recipe() {
  }

  protected Recipe(Parcel in) {
    mId = in.readLong();
    mReceipeId = in.readLong();
    mImage = in.readString();
    mName = in.readString();
    mServing = in.readInt();
    mTimestamp = in.readLong();
  }

  /**
   * Use this constructor to return a Recipe from a Cursor
   *
   * @return {@link Recipe}
   */
  public static Recipe from(Cursor cursor) {
    final Recipe recipe = new Recipe();
    recipe.setId(cursor.getLong(cursor.getColumnIndexOrThrow(BakingContract.RecipeEntry._ID)));
    recipe.setReceipeId(cursor.getLong(cursor.getColumnIndexOrThrow(BakingContract.RecipeEntry.COLUMN_RECIPE_ID)));
    recipe.setImage(cursor.getString(cursor.getColumnIndexOrThrow(BakingContract.RecipeEntry.COLUMN_IMAGE)));
    recipe.setName(cursor.getString(cursor.getColumnIndexOrThrow(BakingContract.RecipeEntry.COLUMN_NAME)));
    recipe.setServing(cursor.getInt(cursor.getColumnIndexOrThrow(BakingContract.RecipeEntry.COLUMN_SERVING)));
    recipe.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(BakingContract.RecipeEntry.COLUMN_TIMESTAMP)));
    return recipe;
  }

  public long getId() {
    return mId;
  }

  public void setId(long id) {
    mId = id;
  }

  public long getReceipeId() {
    return mReceipeId;
  }

  public void setReceipeId(long receipeId) {
    mReceipeId = receipeId;
  }

  public String getImage() {
    return mImage;
  }

  public void setImage(String image) {
    mImage = image;
  }

  public String getName() {
    return mName;
  }

  public void setName(String name) {
    mName = name;
  }

  public int getServing() {
    return mServing;
  }

  public void setServing(int serving) {
    mServing = serving;
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
    dest.writeLong(mReceipeId);
    dest.writeString(mImage);
    dest.writeString(mName);
    dest.writeInt(mServing);
    dest.writeLong(mTimestamp);
  }

  @SuppressWarnings("unused") public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
    @Override public Recipe createFromParcel(Parcel in) {
      return new Recipe(in);
    }

    @Override public Recipe[] newArray(int size) {
      return new Recipe[size];
    }
  };
}
