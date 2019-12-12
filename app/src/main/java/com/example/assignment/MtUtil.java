package com.example.assignment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.app.ActivityCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by mohit on 25/7/16.
 */
public class MtUtil {


    public static ProgressDialog pd;


    public static void showToastS(String msg, Context ctx) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }




    public static boolean isConnectedToInternet(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }




    /**
     * Make a server request
     *
     * @param uri
     * @param dataJson
     * @param ctx
     * @return Response from server
     */
    public static String makeRequest(String uri, String dataJson, Context ctx, int requestMethod) {
        HttpURLConnection httpURLConnection = null;
        HttpsURLConnection httpsURLConnection = null;
        String response = null;
        try {

            if(requestMethod == MtConstant.postRequest)
            {
                if(!uri.contains("https")){
                    httpURLConnection = (HttpURLConnection) ((new URL(uri).openConnection()));
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(true);
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpURLConnection.setRequestProperty("Accept", "application/json");
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.connect();
                }else {
                    httpsURLConnection = (HttpsURLConnection) ((new URL(uri).openConnection()));
                    httpsURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(true);
                    httpsURLConnection.setRequestProperty("Content-Type", "application/json");
                    httpsURLConnection.setRequestProperty("Accept", "application/json");
                    httpsURLConnection.setRequestMethod("POST");
                    httpsURLConnection.setSSLSocketFactory(new MtSSLSocketFactory(httpsURLConnection.getSSLSocketFactory()));
                    httpsURLConnection.connect();
                }

                OutputStream outputStream;
                if(!uri.contains("https")){
                    outputStream = httpURLConnection.getOutputStream();
                }else{
                    outputStream = httpsURLConnection.getOutputStream();
                }
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                writer.write(dataJson);
                writer.close();
                outputStream.close();
            }
            else if(requestMethod == MtConstant.getRequest)
            {
                if(!uri.contains("https")){
                    httpURLConnection = (HttpURLConnection) ((new URL(uri).openConnection()));
                    httpURLConnection.addRequestProperty("Cache-Control", "max-stale=" + 60 * 60);
                    httpURLConnection.setUseCaches(true);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                }else {
                    httpsURLConnection = (HttpsURLConnection) ((new URL(uri).openConnection()));
//                    httpURLConnection.setUseCaches(true);
                    httpsURLConnection.setRequestMethod("GET");
                    httpsURLConnection.setSSLSocketFactory(new MtSSLSocketFactory(httpsURLConnection.getSSLSocketFactory()));
                    httpsURLConnection.connect();
                }
            }
            BufferedReader bufferedReader;
            if(!uri.contains("https")){
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            }else{
                bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream(), "UTF-8"));
            }

            String line = null;
            StringBuilder sb = new StringBuilder();

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

            bufferedReader.close();
            response = sb.toString();

        } catch (UnsupportedEncodingException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getMessage();
        } catch (Exception e) {
            e.getMessage();
        } finally {
            if(httpURLConnection != null)
                httpURLConnection.disconnect();
            if(httpsURLConnection != null)
                httpsURLConnection.disconnect();
        }
        return response;
    }



    public static boolean isPortrait(Context context) {
        Configuration config = context.getResources().getConfiguration();
        return config.orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static int dp(Context context, int dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dm);
    }

    public static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public static boolean hasNavigationBar(Context context) {
        if (Build.VERSION.SDK_INT < 19) return false; // could have but won't be translucent
        return !ViewConfiguration.get(context).hasPermanentMenuKey();
    }

    public static int getNavigationBarHeight(Context context) {
        if (!hasNavigationBar(context)) return 0;
        Resources r = context.getResources();
        DisplayMetrics dm = r.getDisplayMetrics();
        Configuration config = r.getConfiguration();
        boolean canMove = dm.widthPixels != dm.heightPixels && config.smallestScreenWidthDp < 600;
        if (canMove && config.orientation == Configuration.ORIENTATION_LANDSCAPE) return 0;
        String s = isPortrait(context) ? "navigation_bar_height" : "navigation_bar_height_landscape";
        int id = r.getIdentifier(s, "dimen", "android");
        if (id > 0) return r.getDimensionPixelSize(id);
        return 0;
    }

    public static int getNavigationBarWidth(Context context) {
        if (!hasNavigationBar(context)) return 0;
        Resources r = context.getResources();
        DisplayMetrics dm = r.getDisplayMetrics();
        Configuration config = r.getConfiguration();
        boolean canMove = dm.widthPixels != dm.heightPixels && config.smallestScreenWidthDp < 600;
        if (canMove && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            int id = r.getIdentifier("navigation_bar_width", "dimen", "android");
            if (id > 0) return r.getDimensionPixelSize(id);
        }
        return 0;
    }


    public static void showProgressDialog(String msg, Context ctx) {
        pd = new ProgressDialog(ctx);
        pd.setMessage(msg);
        pd.setIndeterminate(true);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.show();
    }

    public static void hideProgressDialog() {
        if (pd != null)
            pd.dismiss();
    }


    public static void alertDailog(final Context context, String title, String msg, final String Type, final String LatLong, final String number
    ) {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        if (Type.equals("Map")) {
                            String geoUri = LatLong;
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                            context.startActivity(intent);
                        } else if (Type.equals("WhatsApp")) {
                            try {
                                String toNumber = number;
                                Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + "" + toNumber + "?body=" + ""));
                                sendIntent.setPackage("com.whatsapp");
                                context.startActivity(sendIntent);
                            } catch (Exception e) {
                                Toast.makeText(context, "it may be you don't have whats app", Toast.LENGTH_LONG).show();
                            }
                        } else if (Type.equals("Phone")) {
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:" + number));

                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            context.startActivity(callIntent);
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }






}
