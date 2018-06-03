package de.appmotion.udacitybaking;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import java.util.List;
import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {

  protected Locale mLocale;
  protected String mDefaultLanguage;
  protected String mDefaultCountry;
  private Toast mToast;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mLocale = getResources().getConfiguration().locale;
    mDefaultLanguage = mLocale.getLanguage();
    mDefaultCountry = mLocale.getCountry();
  }

  @Override protected void onResume() {
    mLocale = getResources().getConfiguration().locale;
    mDefaultLanguage = mLocale.getLanguage();
    mDefaultCountry = mLocale.getCountry();
    super.onResume();
  }

  @Override protected void onPause() {
    if (mToast != null) {
      mToast.cancel();
    }
    super.onPause();
  }

  /**
   * Finding out if an Intent is available
   */
  protected boolean isIntentAvailable(Intent intent) {
    final PackageManager mgr = getPackageManager();
    List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
    return list.size() > 0;
  }

  /**
   * Show a Toast Message.
   *
   * @param message the Message to show
   */
  protected void showMessage(String message) {
    if (mToast != null) {
      mToast.cancel();
    }
    mToast = Toast.makeText(this, message, Toast.LENGTH_LONG);
    mToast.show();
  }
}
