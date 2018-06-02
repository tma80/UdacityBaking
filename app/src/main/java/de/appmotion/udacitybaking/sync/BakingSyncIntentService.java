package de.appmotion.udacitybaking.sync;

import android.app.IntentService;
import android.content.Intent;

public class BakingSyncIntentService extends IntentService {

  public BakingSyncIntentService() {
    super("BakingSyncIntentService");
  }

  @Override protected void onHandleIntent(Intent intent) {
    BakingSyncTask.syncRecipes(this);
  }
}