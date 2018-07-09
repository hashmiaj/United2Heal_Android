package com.u2h.user.united2healandroid;
import android.content.Context;
import android.graphics.Typeface;


/**
 * Created by udit on 13/02/16.
 */
public class FontAwesomeManager {


    public static final String FONTAWESOME ="fa-regular-400.ttf";

    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}