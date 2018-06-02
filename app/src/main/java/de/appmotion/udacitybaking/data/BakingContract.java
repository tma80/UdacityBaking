package de.appmotion.udacitybaking.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the baking database. Furthermore the Content provider constants for accessing data in this
 * contract are defined.
 */
public final class BakingContract {

  /*
   * Content provider constants
   * Define the possible paths for accessing data in this contract
   */
  // The authority, which is how your code knows which Content Provider to access
  public static final String CONTENT_AUTHORITY = "de.appmotion.udacitybaking";

  // The base content URI = "content://" + <authority>
  public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

  public static final String PATH_RECIPE = "recipe";
  public static final String PATH_RECIPE_INGREDIENT = "recipe_ingredient";
  public static final String PATH_RECIPE_STEP = "recipe_step";

  // To prevent someone from accidentally instantiating the contract class,
  // make the constructor private.
  private BakingContract() {
  }

  public static class RecipeEntry implements BaseColumns {
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPE).build();
    // Table name
    public static final String TABLE_NAME = "recipe";
    // Table columns
    public static final String COLUMN_RECIPE_ID = "recipe_id";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SERVING = "serving";
    public static final String COLUMN_TIMESTAMP = "timestamp";
  }

  public static class RecipeIngredientEntry implements BaseColumns {
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPE_INGREDIENT).build();
    // Table name
    public static final String TABLE_NAME = "recipe_ingredient";
    // Table columns
    public static final String COLUMN_RECIPE_ID = "recipe_id"; // corresponding recipe id
    public static final String COLUMN_INGREDIENT = "ingredient";
    public static final String COLUMN_MEASURE = "measure";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_TIMESTAMP = "timestamp";
  }

  public static class RecipeStepEntry implements BaseColumns {
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPE_STEP).build();
    // Table name
    public static final String TABLE_NAME = "recipe_step";
    // Table columns
    public static final String COLUMN_RECIPE_ID = "recipe_id"; // corresponding recipe id
    public static final String COLUMN_STEP_ID = "step_id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DESCRIPTION_SHORT = "description_short";
    public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
    public static final String COLUMN_VIDEO_URL = "video_url";
    public static final String COLUMN_TIMESTAMP = "timestamp";
  }
}
