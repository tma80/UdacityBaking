package de.appmotion.udacitybaking.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class BakingContentProvider extends ContentProvider {

  // It's convention to use 100, 200, 300, etc for directories,
  // and related ints (101, 102, ..) for items in that directory.
  public static final int CODE_RECIPE = 100;
  public static final int CODE_RECIPE_INGREDIENT = 200;
  public static final int CODE_RECIPE_STEP = 300;

  private static final UriMatcher sUriMatcher = buildUriMatcher();
  // Member variable for a DatabaseHelper that's initialized in the onCreate() method
  private BakingDbHelper mDbHelper;

  public static UriMatcher buildUriMatcher() {

    /*
     * All paths added to the UriMatcher have a corresponding code to return when a match is
     * found. The code passed into the constructor of UriMatcher here represents the code to
     * return for the root URI. It's common to use NO_MATCH as the code for this case.
     */
    final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /* This URI is content://de.appmotion.udacitybaking/recipe/ */
    uriMatcher.addURI(BakingContract.CONTENT_AUTHORITY, BakingContract.PATH_RECIPE, CODE_RECIPE);
    /* This URI is content://de.appmotion.udacitybaking/recipe_ingredient/ */
    uriMatcher.addURI(BakingContract.CONTENT_AUTHORITY, BakingContract.PATH_RECIPE_INGREDIENT, CODE_RECIPE_INGREDIENT);
    /* This URI is content://de.appmotion.udacitybaking/recipe_step/ */
    uriMatcher.addURI(BakingContract.CONTENT_AUTHORITY, BakingContract.PATH_RECIPE_STEP, CODE_RECIPE_STEP);

    return uriMatcher;
  }

  /**
   * In onCreate, we initialize our content provider on startup. This method is called for all
   * registered content providers on the application main thread at application launch time.
   * It must not perform lengthy operations, or application startup will be delayed.
   *
   * Nontrivial initialization (such as opening, upgrading, and scanning
   * databases) should be deferred until the content provider is used (via {@link #query},
   * {@link #bulkInsert(Uri, ContentValues[])}, etc).
   *
   * Deferred initialization keeps application startup fast, avoids unnecessary work if the
   * provider turns out not to be needed, and stops database errors (such as a full disk) from
   * halting application launch.
   *
   * @return true if the provider was successfully loaded, false otherwise
   */
  @Override public boolean onCreate() {
    /*
     * As noted in the comment above, onCreate is run on the main thread, so performing any
     * lengthy operations will cause lag in your app. Since DatabaseHelper's constructor is
     * very lightweight, we are safe to perform that initialization here.
     */
    mDbHelper = BakingDbHelper.getInstance(getContext());
    return true;
  }

  /**
   * Handle requests for data by URI
   */
  @Nullable @Override public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
      @Nullable String[] selectionArgs, @Nullable String sortOrder) {
    // Get access to underlying database (read-only for query)
    final SQLiteDatabase db = mDbHelper.getReadableDatabase();

    // Write URI matching code and set a variable to return a Cursor
    int match = sUriMatcher.match(uri);
    Cursor returnCursor;

    switch (match) {
      // Query for the recipe directory
      case CODE_RECIPE:
        returnCursor = db.query(BakingContract.RecipeEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        break;
      // Query for the recipe_ingredient directory
      case CODE_RECIPE_INGREDIENT:
        returnCursor =
            db.query(BakingContract.RecipeIngredientEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        break;
      // Query for the recipe_step directory
      case CODE_RECIPE_STEP:
        returnCursor = db.query(BakingContract.RecipeStepEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    // Set a notification URI on the Cursor and return that Cursor
    returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

    // Return the desired Cursor
    return returnCursor;
  }

  @Nullable @Override public String getType(@NonNull Uri uri) {
    return null;
  }

  /**
   * Insert a single new row of data
   */
  @Nullable @Override public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
    // Get access to the database (to write new data to)
    final SQLiteDatabase db = mDbHelper.getWritableDatabase();

    int match = sUriMatcher.match(uri);
    long id; // DB Id of inserted ContentValues
    Uri returnUri; // URI to be returned

    switch (match) {
      case CODE_RECIPE:
        // Inserting values into recipe table
        id = db.insert(BakingContract.RecipeEntry.TABLE_NAME, null, values);
        if (id > 0) {
          returnUri = ContentUris.withAppendedId(BakingContract.RecipeEntry.CONTENT_URI, id);
        } else {
          throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        break;
      case CODE_RECIPE_INGREDIENT:
        // Inserting values into top recipe_ingredient table
        id = db.insert(BakingContract.RecipeIngredientEntry.TABLE_NAME, null, values);
        if (id > 0) {
          returnUri = ContentUris.withAppendedId(BakingContract.RecipeIngredientEntry.CONTENT_URI, id);
        } else {
          throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        break;
      case CODE_RECIPE_STEP:
        // Inserting values into recipe_step table
        id = db.insert(BakingContract.RecipeStepEntry.TABLE_NAME, null, values);
        if (id > 0) {
          returnUri = ContentUris.withAppendedId(BakingContract.RecipeStepEntry.CONTENT_URI, id);
        } else {
          throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    // Notify the resolver if the uri has been changed
    getContext().getContentResolver().notifyChange(uri, null);

    // Return constructed uri (this points to the newly inserted row of data)
    return returnUri;
  }

  /**
   * Handles requests to insert a set of new rows.
   *
   * @return The number of values that were inserted.
   */
  @Override public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
    // Get access to the database
    final SQLiteDatabase db = mDbHelper.getWritableDatabase();

    int match = sUriMatcher.match(uri);
    int rowsInserted = 0;

    switch (match) {

      case CODE_RECIPE:
        db.beginTransaction();
        try {
          for (ContentValues value : values) {
            long _id = db.insert(BakingContract.RecipeEntry.TABLE_NAME, null, value);
            if (_id != -1) {
              rowsInserted++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }

        if (rowsInserted > 0) {
          getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows inserted
        return rowsInserted;

      case CODE_RECIPE_INGREDIENT:
        db.beginTransaction();
        try {
          for (ContentValues value : values) {
            long _id = db.insert(BakingContract.RecipeIngredientEntry.TABLE_NAME, null, value);
            if (_id != -1) {
              rowsInserted++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }

        if (rowsInserted > 0) {
          getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows inserted
        return rowsInserted;

      case CODE_RECIPE_STEP:
        db.beginTransaction();
        try {
          for (ContentValues value : values) {
            long _id = db.insert(BakingContract.RecipeStepEntry.TABLE_NAME, null, value);
            if (_id != -1) {
              rowsInserted++;
            }
          }
          db.setTransactionSuccessful();
        } finally {
          db.endTransaction();
        }

        if (rowsInserted > 0) {
          getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows inserted
        return rowsInserted;

      default:
        return super.bulkInsert(uri, values);
    }
  }

  @Override public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
    // Get access to the database
    final SQLiteDatabase db = mDbHelper.getWritableDatabase();

    int match = sUriMatcher.match(uri);
    int itemsDeleted;  // Keep track of the number of deleted items

    /*
     * If we pass null as the selection to SQLiteDatabase#delete, our entire table will be
     * deleted. However, if we do pass null and delete all of the rows in the table, we won't
     * know how many rows were deleted. According to the documentation for SQLiteDatabase,
     * passing "1" for the selection will delete all rows and return the number of rows
     * deleted, which is what the caller of this method expects.
     */
    if (null == selection) selection = "1";

    switch (match) {
      // Delete ALL items in the table
      case CODE_RECIPE:
        itemsDeleted = db.delete(BakingContract.RecipeEntry.TABLE_NAME, selection, selectionArgs);
        break;
      // Delete ALL items in the table
      case CODE_RECIPE_INGREDIENT:
        itemsDeleted = db.delete(BakingContract.RecipeIngredientEntry.TABLE_NAME, selection, selectionArgs);
        break;
      // Delete ALL items in the table
      case CODE_RECIPE_STEP:
        itemsDeleted = db.delete(BakingContract.RecipeStepEntry.TABLE_NAME, selection, selectionArgs);
        break;
      default:
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    // Notify the resolver of a change and return the number of items deleted
    if (itemsDeleted != 0) {
      // An item was deleted, set notification
      getContext().getContentResolver().notifyChange(uri, null);
    }
    // Return the number of items deleted
    return itemsDeleted;
  }

  @Override
  public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
    throw new RuntimeException("We are not implementing update in Udacity Baking");
  }

  /**
   * You do not need to call this method. This is a method specifically to assist the testing
   * framework in running smoothly. You can read more at:
   * http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
   */
  @Override @TargetApi(11) public void shutdown() {
    mDbHelper.close();
    super.shutdown();
  }
}
