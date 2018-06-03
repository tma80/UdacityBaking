package de.appmotion.udacitybaking.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.appmotion.udacitybaking.data.BakingContract.RecipeEntry;
import de.appmotion.udacitybaking.data.BakingContract.RecipeIngredientEntry;
import de.appmotion.udacitybaking.data.BakingContract.RecipeStepEntry;

/**
 * Manages a local database for baking data.
 */
public class BakingDbHelper extends SQLiteOpenHelper {

  // The database name
  private static final String DATABASE_NAME = "baking.db";
  // If you change the database schema, you must increment the database version or the onUpgrade method will not be called.
  private static final int DATABASE_VERSION = 1;
  private static BakingDbHelper sInstance;

  /**
   * Constructor should be private to prevent direct instantiation.
   * make call to static method "getInstance()" instead.
   */
  private BakingDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public static synchronized BakingDbHelper getInstance(Context context) {

    // Use the application context, which will ensure that you
    // don't accidentally leak an Activity's context.
    // See this article for more information: http://bit.ly/6LRzfx
    if (sInstance == null) {
      sInstance = new BakingDbHelper(context.getApplicationContext());
    }
    return sInstance;
  }

  /**
   * Called when the database is created for the first time. This is where the creation of
   * tables and the initial population of the tables should happen.
   *
   * @param sqLiteDatabase The database.
   */
  @Override public void onCreate(SQLiteDatabase sqLiteDatabase) {

    /*
     * Create a table to hold recipe data
     *
     * If the INTEGER PRIMARY KEY column is not explicitly given a value, then it will be filled
     * automatically with an unused integer, usually one more than the largest _ID currently in
     * use. This is true regardless of whether or not the AUTOINCREMENT keyword is used.
     * <p>
     * If the AUTOINCREMENT keyword appears after INTEGER PRIMARY KEY, that changes the automatic
     * _ID assignment algorithm to prevent the reuse of _IDs over the lifetime of the database.
     * In other words, the purpose of AUTOINCREMENT is to prevent the reuse of _IDs from previously
     * deleted rows.
     */
    final String SQL_CREATE_RECIPE_TABLE =

        "CREATE TABLE " + RecipeEntry.TABLE_NAME + " (" +

            RecipeEntry._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE AUTOINCREMENT, " +

            RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +

            RecipeEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +

            RecipeEntry.COLUMN_NAME + " TEXT NOT NULL, " +

            RecipeEntry.COLUMN_SERVING + " INTEGER NOT NULL, " +

            RecipeEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
            /*
             * To ensure this table can only contain one Recipe entry per recipe_id, we declare
             * the recipe_id column to be unique. We also specify "ON CONFLICT REPLACE". This tells
             * SQLite that if we have a Recipe entry for a certain recipe_id and we attempt to
             * insert another Recipe entry with that recipe_id, we replace the old Recipe entry.
             */
            "UNIQUE (" + RecipeEntry.COLUMN_RECIPE_ID + ") ON CONFLICT REPLACE)";

    /*
     * After we've spelled out our SQLite table creation statement above, we actually execute
     * that SQL with the execSQL method of our SQLite database object.
     */
    sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);

    /*
     * Create a table to hold recipe ingredient data
     */
    final String SQL_CREATE_RECIPE_INGREDIENT_TABLE =

        "CREATE TABLE " + RecipeIngredientEntry.TABLE_NAME + " (" +

            RecipeIngredientEntry._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE AUTOINCREMENT, " +

            RecipeIngredientEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +

            RecipeIngredientEntry.COLUMN_INGREDIENT + " TEXT NOT NULL, " +

            RecipeIngredientEntry.COLUMN_MEASURE + " TEXT NOT NULL, " +

            RecipeIngredientEntry.COLUMN_QUANTITY + " INTEGER NOT NULL, " +

            RecipeIngredientEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +

            "FOREIGN KEY (" + RecipeIngredientEntry.COLUMN_RECIPE_ID  + ") REFERENCES " + RecipeEntry.TABLE_NAME + " (" + RecipeEntry.COLUMN_RECIPE_ID + "))";

    /*
     * After we've spelled out our SQLite table creation statement above, we actually execute
     * that SQL with the execSQL method of our SQLite database object.
     */
    sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_INGREDIENT_TABLE);

    /*
     * Create a table to hold recipe step data
     */
    final String SQL_CREATE_RECIPE_STEP_TABLE =

        "CREATE TABLE " + RecipeStepEntry.TABLE_NAME + " (" +

            RecipeStepEntry._ID + " INTEGER PRIMARY KEY ON CONFLICT REPLACE AUTOINCREMENT, " +

            RecipeStepEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +

            RecipeStepEntry.COLUMN_STEP_ID + " INTEGER NOT NULL, " +

            RecipeStepEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +

            RecipeStepEntry.COLUMN_DESCRIPTION_SHORT + " TEXT NOT NULL, " +

            RecipeStepEntry.COLUMN_THUMBNAIL_URL + " TEXT NOT NULL, " +

            RecipeStepEntry.COLUMN_VIDEO_URL + " TEXT NOT NULL, " +

            RecipeStepEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +

            "FOREIGN KEY (" + RecipeStepEntry.COLUMN_RECIPE_ID  + ") REFERENCES " + RecipeEntry.TABLE_NAME + " (" + RecipeEntry.COLUMN_RECIPE_ID + "))";

    /*
     * After we've spelled out our SQLite table creation statement above, we actually execute
     * that SQL with the execSQL method of our SQLite database object.
     */
    sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_STEP_TABLE);
  }

  /**
   * Note that this only fires if you change the version number for your database (in our case, DATABASE_VERSION).
   * It does NOT depend on the version number for your application found in your app/build.gradle file.
   *
   * @param sqLiteDatabase Database that is being upgraded
   * @param oldVersion The old database version
   * @param newVersion The new database version
   */
  @Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    // For now simply drop the tables and create new ones. This means if you change the
    // DATABASE_VERSION the tables will be dropped.
    // INFO: In a production app, this method might be modified to ALTER the tables instead of dropping them, so that existing data is not deleted.
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeEntry.TABLE_NAME);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeIngredientEntry.TABLE_NAME);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeStepEntry.TABLE_NAME);
    onCreate(sqLiteDatabase);
  }
}