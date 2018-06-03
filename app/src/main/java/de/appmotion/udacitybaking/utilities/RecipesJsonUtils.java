package de.appmotion.udacitybaking.utilities;

import android.content.ContentValues;
import de.appmotion.udacitybaking.data.BakingContract;
import java.lang.reflect.Array;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility functions to handle Recipes JSON data.
 */
public final class RecipesJsonUtils {

  private static final String TAG = RecipesJsonUtils.class.getSimpleName();

  /* Recipe information */
  private static final String RECIPE_ID = "id";
  private static final String RECIPE_IMAGE = "image";
  private static final String RECIPE_NAME = "name";
  private static final String RECIPE_SERVING = "servings";

  /* Recipe Ingredient information */
  private static final String RECIPE_INGREDIENT_ARRAY = "ingredients";
  private static final String RECIPE_INGREDIENT = "ingredient";
  private static final String RECIPE_INGREDIENT_MEASURE = "measure";
  private static final String RECIPE_INGREDIENT_QUANTITY = "quantity";

  /* Recipe Step information */
  private static final String RECIPE_STEP_ARRAY = "steps";
  private static final String RECIPE_STEP_ID = "id";
  private static final String RECIPE_STEP_DESCRIPTION = "description";
  private static final String RECIPE_STEP_DESCRIPTION_SHORT = "shortDescription";
  private static final String RECIPE_STEP_THUMBNAIL_URL = "thumbnailURL";
  private static final String RECIPE_STEP_VIDEO_URL = "videoURL";

  /**
   * This method parses JSON from a web response and returns an array of Strings
   * describing recipes.
   *
   * @param recipeJsonStr JSON response from server
   * @return An {@link HashMap} of {@link ContentValues[]} describing recipes data
   * @throws JSONException If JSON data cannot be properly parsed
   */
  public static HashMap<String, ContentValues[]> getRecipeContentValuesFromJson(String recipeJsonStr) throws JSONException {

    HashMap<String, ContentValues[]> mapOfContentValues = new HashMap<>(0);
    ContentValues[] recipeContentValues = new ContentValues[0];
    ContentValues[] recipeIngredientContentValues = new ContentValues[0];
    ContentValues[] recipeStepContentValues = new ContentValues[0];

    JSONArray recipeJsonArray = new JSONArray(recipeJsonStr);
    recipeContentValues = new ContentValues[recipeJsonArray.length()];
    for (int i = 0; i < recipeJsonArray.length(); i++) {
      JSONObject recipeJsonObject = recipeJsonArray.getJSONObject(i);
      if (recipeJsonObject != null) {
        /* Recipe data */
        long recipeId = recipeJsonObject.optLong(RECIPE_ID, -1L);
        String recipeImage = recipeJsonObject.optString(RECIPE_IMAGE, "");
        String recipeName = recipeJsonObject.optString(RECIPE_NAME, "");
        int recipeServing = recipeJsonObject.optInt(RECIPE_SERVING, 0);

        ContentValues recipeValues = new ContentValues();
        recipeValues.put(BakingContract.RecipeEntry.COLUMN_RECIPE_ID, recipeId);
        recipeValues.put(BakingContract.RecipeEntry.COLUMN_IMAGE, recipeImage);
        recipeValues.put(BakingContract.RecipeEntry.COLUMN_NAME, recipeName);
        recipeValues.put(BakingContract.RecipeEntry.COLUMN_SERVING, recipeServing);

        recipeContentValues[i] = recipeValues;

        /* Recipe Ingredient data */
        JSONArray recipeIngredientJsonArray = recipeJsonObject.optJSONArray(RECIPE_INGREDIENT_ARRAY);
        if (recipeIngredientJsonArray != null) {
          ContentValues[] recipeIngredientContentValuesTemp = new ContentValues[recipeIngredientJsonArray.length()];
          for (int j = 0; j < recipeIngredientJsonArray.length(); j++) {
            JSONObject recipeIngredientJsonObject = recipeIngredientJsonArray.optJSONObject(j);

            if (recipeIngredientJsonObject != null) {
              String recipeIngredient = recipeIngredientJsonObject.optString(RECIPE_INGREDIENT, "");
              String recipeIngredientMeasure = recipeIngredientJsonObject.optString(RECIPE_INGREDIENT_MEASURE, "");
              int recipeIngredientQuantity = recipeIngredientJsonObject.optInt(RECIPE_INGREDIENT_QUANTITY, 0);

              ContentValues recipeIngredientValues = new ContentValues();
              recipeIngredientValues.put(BakingContract.RecipeIngredientEntry.COLUMN_RECIPE_ID, recipeId); // foreign key
              recipeIngredientValues.put(BakingContract.RecipeIngredientEntry.COLUMN_INGREDIENT, recipeIngredient);
              recipeIngredientValues.put(BakingContract.RecipeIngredientEntry.COLUMN_MEASURE, recipeIngredientMeasure);
              recipeIngredientValues.put(BakingContract.RecipeIngredientEntry.COLUMN_QUANTITY, recipeIngredientQuantity);

              recipeIngredientContentValuesTemp[j] = recipeIngredientValues;
            }
          }
          recipeIngredientContentValues = concatenate(recipeIngredientContentValues, recipeIngredientContentValuesTemp);
        }

        /* Recipe Step data */
        JSONArray recipeStepJsonArray = recipeJsonObject.optJSONArray(RECIPE_STEP_ARRAY);
        if (recipeStepJsonArray != null) {
          ContentValues[] recipeStepContentValuesTemp = new ContentValues[recipeStepJsonArray.length()];
          for (int k = 0; k < recipeStepJsonArray.length(); k++) {
            JSONObject recipeStepJsonObject = recipeStepJsonArray.optJSONObject(k);
            if (recipeStepJsonObject != null) {
              int recipeStepId = recipeStepJsonObject.optInt(RECIPE_STEP_ID, -1);
              String recipeStepDescription = recipeStepJsonObject.optString(RECIPE_STEP_DESCRIPTION, "");
              String recipeStepDescriptionShort = recipeStepJsonObject.optString(RECIPE_STEP_DESCRIPTION_SHORT, "");
              String recipeStepThumbnailUrl = recipeStepJsonObject.optString(RECIPE_STEP_THUMBNAIL_URL, "");
              String recipeStepVideoUrl = recipeStepJsonObject.optString(RECIPE_STEP_VIDEO_URL, "");

              ContentValues recipeStepValues = new ContentValues();
              recipeStepValues.put(BakingContract.RecipeStepEntry.COLUMN_RECIPE_ID, recipeId); // foreign key
              recipeStepValues.put(BakingContract.RecipeStepEntry.COLUMN_STEP_ID, recipeStepId);
              recipeStepValues.put(BakingContract.RecipeStepEntry.COLUMN_DESCRIPTION, recipeStepDescription);
              recipeStepValues.put(BakingContract.RecipeStepEntry.COLUMN_DESCRIPTION_SHORT, recipeStepDescriptionShort);
              recipeStepValues.put(BakingContract.RecipeStepEntry.COLUMN_THUMBNAIL_URL, recipeStepThumbnailUrl);
              recipeStepValues.put(BakingContract.RecipeStepEntry.COLUMN_VIDEO_URL, recipeStepVideoUrl);

              recipeStepContentValuesTemp[k] = recipeStepValues;
            }
          }
          recipeStepContentValues = concatenate(recipeStepContentValues, recipeStepContentValuesTemp);
        }
      }
    }

    mapOfContentValues.put("recipe", recipeContentValues);
    mapOfContentValues.put("recipe_ingredient", recipeIngredientContentValues);
    mapOfContentValues.put("recipe_step", recipeStepContentValues);

    return mapOfContentValues;
  }

  /**
   * Concatenate two {@link Array}s
   *
   * @param a Array a
   * @param b Array b
   * @param <T> Type of Array
   * @return new Array consisting of Array a and Array b
   */
  private static <T> T[] concatenate(T[] a, T[] b) {
    int aLen = a.length;
    int bLen = b.length;

    @SuppressWarnings("unchecked") T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
    System.arraycopy(a, 0, c, 0, aLen);
    System.arraycopy(b, 0, c, aLen, bLen);

    return c;
  }
}