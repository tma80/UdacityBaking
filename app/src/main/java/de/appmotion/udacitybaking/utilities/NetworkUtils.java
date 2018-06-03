package de.appmotion.udacitybaking.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import de.appmotion.udacitybaking.App;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * These utilities will be used to communicate with the network.
 */
public final class NetworkUtils {

  private static final String TAG = NetworkUtils.class.getSimpleName();

  // Define {@link ErrorType} Types
  public static final String EMPTY = "empty";
  public static final String API_ERROR = "api_error";
  public static final String API_FORBIDDEN = "api_forbidden";
  public static final String OFFLINE = "offline";
  // Url
  private final static String BAKING_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

  /**
   * Builds the URL used to query BAKING_URL for recipe data.
   *
   * @return The URL to use to get configuration data for the API.
   */
  public static URL buildBakingUrl() {
    Uri builtUri = Uri.parse(BAKING_URL).buildUpon().build();

    URL url = null;
    try {
      url = new URL(builtUri.toString());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }

    return url;
  }

  /**
   * This method uses {@link OkHttpClient} for returning the entire result from the HTTP response.
   *
   * @param queryUrl The URL to fetch the HTTP response from.
   * @return The contents of the HTTP response, or an error response
   */
  public static String getWithOkHttp(URL queryUrl) {
    try {
      Request request = new Request.Builder().url(queryUrl).get().build();
      Response response = App.getInstance().getOkHttpClient().newCall(request).execute();
      switch (response.code()) {
        case HttpURLConnection.HTTP_OK:
          ResponseBody responseBody = response.body();
          if (responseBody == null) {
            Log.d(TAG, queryUrl.toString() + ": Empty response from server");
            return EMPTY;
          }
          Scanner scanner = new Scanner(responseBody.byteStream());
          scanner.useDelimiter("\\A");
          boolean hasInput = scanner.hasNext();
          if (hasInput) {
            String body = scanner.next();
            responseBody.close();
            return body;
          } else {
            Log.d(TAG, queryUrl.toString() + ": Empty response from server");
            responseBody.close();
            return EMPTY;
          }
        case HttpURLConnection.HTTP_FORBIDDEN:
          Log.d(TAG, queryUrl.toString() + ": HTTP_FORBIDDEN ");
          return API_FORBIDDEN;
        default:
          Log.d(TAG, queryUrl.toString() + ": error, HTTP status code: " + response.code());
          return API_ERROR;
      }
    } catch (IOException e) {
      Log.d(TAG, queryUrl.toString() + ": device is offline");
      return OFFLINE;
    }
  }

  public static boolean isAnyNetworkOn() {
    ConnectivityManager connMgr = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connMgr == null) {
      return false;
    }
    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    return (networkInfo != null && networkInfo.isConnected());
  }
}