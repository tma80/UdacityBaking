package de.appmotion.udacitybaking.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class BakingSyncIntentService extends IntentService {

  public BakingSyncIntentService() {
    super("BakingSyncIntentService");
  }

  @Override protected void onHandleIntent(Intent intent) {
    BakingSyncTask.syncRecipes(this);
  }

  /**
   * Helper method to perform a sync immediately.
   *
   * @param context The Context used to start the IntentService for the sync.
   */
  public static void startImmediateSync(@NonNull final Context context) {
    Intent intent = new Intent(context, BakingSyncIntentService.class);
    context.startService(intent);
  }
}