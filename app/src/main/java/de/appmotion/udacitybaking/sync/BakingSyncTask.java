package de.appmotion.udacitybaking.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import de.appmotion.udacitybaking.data.BakingContract;
import de.appmotion.udacitybaking.utilities.NetworkUtils;
import de.appmotion.udacitybaking.utilities.RecipesJsonUtils;
import java.net.URL;
import java.util.HashMap;
import org.json.JSONException;

public class BakingSyncTask {

  private static final String TAG = BakingSyncTask.class.getSimpleName();

  /**
   * Performs the network request for recipes, parses the JSON from that request, and
   * inserts the new recipe information into our ContentProvider.
   *
   * @param context Used to access utility methods and the ContentResolver
   */
  synchronized public static void syncRecipes(Context context) {

    try {
      URL recipesRequestUrl = NetworkUtils.buildBakingUrl();

      /* Use the URL to retrieve the JSON */
      String jsonRecipesResponse = NetworkUtils.getWithOkHttp(recipesRequestUrl);
      /* Cancel sync if the response contains an error */
      switch (jsonRecipesResponse) {
        case NetworkUtils.EMPTY:
          return;
        case NetworkUtils.API_ERROR:
          return;
        case NetworkUtils.API_FORBIDDEN:
          return;
        case NetworkUtils.OFFLINE:
          return;
      }

      /* Parse the JSON into a HashMap of recipe values */
      HashMap<String, ContentValues[]> recipeValuesMap = RecipesJsonUtils.getRecipeContentValuesFromJson(jsonRecipesResponse);

      ContentValues[] recipeContentValues = recipeValuesMap.get("recipe");
      ContentValues[] recipeIngredientContentValues = recipeValuesMap.get("recipe_ingredient");
      ContentValues[] recipeStepContentValues = recipeValuesMap.get("recipe_step");

      ContentResolver bakingContentResolver = context.getContentResolver();

      // Insert Recipe values
      if (recipeContentValues.length != 0) {
        // Delete old data because we do not want duplicates
        bakingContentResolver.delete(BakingContract.RecipeEntry.CONTENT_URI, null, null);
        // Insert values
        bakingContentResolver.bulkInsert(BakingContract.RecipeEntry.CONTENT_URI, recipeContentValues);
      }
      // Insert Recipe Ingredient
      if (recipeIngredientContentValues.length != 0) {
        // Delete old data because we do not want duplicates
        bakingContentResolver.delete(BakingContract.RecipeIngredientEntry.CONTENT_URI, null, null);
        // Insert values
        bakingContentResolver.bulkInsert(BakingContract.RecipeIngredientEntry.CONTENT_URI, recipeIngredientContentValues);
      }
      // Insert Recipe Step
      if (recipeStepContentValues.length != 0) {
        // Delete old data because we do not want duplicates
        bakingContentResolver.delete(BakingContract.RecipeStepEntry.CONTENT_URI, null, null);
        // Insert values
        bakingContentResolver.bulkInsert(BakingContract.RecipeStepEntry.CONTENT_URI, recipeStepContentValues);
      }
    } catch (JSONException e) {
      Log.e(TAG, "JSON data cannot be properly parsed: " + e);
    }
  }
}