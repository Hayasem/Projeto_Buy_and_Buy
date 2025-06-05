package com.example.tela_login_projetointegrador.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

public class AccessibilityUtils {
    private static final String PREFS_NAME = "AccessibilityPrefs";
    private static final String KEY_FONT_SCALE = "font_scale";
    private static final String KEY_DARK_MODE = "dark_mode_enabled";

    public static final float FONT_SCALE_SMALL = 0.85f; // ligeiramente menor que normal
    public static final float FONT_SCALE_NORMAL = 1.0f;
    public static final float FONT_SCALE_LARGE = 1.15f;

    public static float getCurrentFontScale(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getFloat(KEY_FONT_SCALE, FONT_SCALE_NORMAL);
    }
    public static void saveFontScale(Context context, float scale) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(KEY_FONT_SCALE, scale);
        editor.apply();
    }
    public static boolean isDarkModeEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }
    public static void saveDarkModeEnabled(Context context, boolean enabled) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_DARK_MODE, enabled);
        editor.apply();
    }
    public static void applyFontScale(View view, float fontScale) {
        if (view == null) return;

        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            // Obtém o tamanho da fonte original definido no XML (em SP)
            // É crucial que suas TextViews tenham um tamanho de texto SP definido no XML para que isso funcione corretamente.
            float originalSpSize = textView.getTextSize() / view.getResources().getDisplayMetrics().scaledDensity;
            // Calcula o novo tamanho com base na escala
            float newSpSize = originalSpSize * fontScale;
            // Define o novo tamanho da fonte
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, newSpSize);
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                applyFontScale(viewGroup.getChildAt(i), fontScale);
            }
        }
    }
    public static void applyDarkMode(Context context) {
        boolean isDarkMode = isDarkModeEnabled(context);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
    public static void toggleDarkMode(Context context) {
        boolean isCurrentlyDarkMode = isDarkModeEnabled(context);
        saveDarkModeEnabled(context, !isCurrentlyDarkMode);
        applyDarkMode(context);
    }
}
