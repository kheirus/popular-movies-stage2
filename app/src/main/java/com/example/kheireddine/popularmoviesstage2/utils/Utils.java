package com.example.kheireddine.popularmoviesstage2.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * This class contains util functions ready to use
 * Created by kheireddine on 30/01/17.
 */


public class Utils {
    public final static String TAG = "pm_debug";

    /**
     * Show a short toast Message
     * @param msg : Message to display
     */
    public static void showShortToastMessage(Context context, String msg){
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * Show a long toast Message
     * @param msg : Message to display
     */
    public static void showLongToastMessage(Context context, String msg){
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Check the network connection
     * @param context : Activity context
     */
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    /**
     * Check the api key
     */
    public static boolean isValidApiKey(){
        if (Constants.API_KEY.equals("YOUR_API") ||
                Constants.API_KEY.isEmpty() ||
                Constants.API_KEY.equals("")){
            return false;
        }
        else return true;
    }


    /**
     * Show an alert dialog
     * @param context : Activity context
     * @param title   : Dialog title
     * @param message : Dialog message
     */
    public static void showDialog(final Context context, String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ((Activity)context).finish();
                    }
                });
        alertDialog.show();
    }

    /**
     * Returns the year of the given date
     * @param dateString : date in String format
     */
    public static String getYear(String dateString){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = parser.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    /**
     * Convert a duration (minutes) in hours and minutes
     * @param duration : duration on minutes
     * @return String value of hours and minutes
     */
    public static String timeToDisplay(String duration){
        String timeToDisplay = "";
        if (duration == null || duration.isEmpty())
            return "";
        else {
            int runtime = Integer.parseInt(duration);
            int hours = runtime / 60;
            int min = runtime % 60;
            if(min < 10)
                timeToDisplay = hours + "h0" + min;
            else
                timeToDisplay = hours + "h" + min;

            return timeToDisplay;
        }
    }

    /**
     *  Dynamically calculate the number of columns and the layout would adapt to the screen size and orientation
     * @param context : Activity context
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }
}
