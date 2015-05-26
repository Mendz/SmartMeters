package il.ac.hit.android.smartmeters.utils;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.widget.Toast;
import il.ac.hit.android.smartmeters.R;
import il.ac.hit.android.smartmeters.login.LoginActivity;
import il.ac.hit.android.smartmeters.login.LoginPreferences;

/**
 * Change the login preference to false
 */
public class UtilsSignOut
{
    public static void logOff(Context context)
    {
        SharedPreferences loginPreferences;
        SharedPreferences.Editor loginPrefsEditor;

        loginPreferences = context.getSharedPreferences(LoginPreferences.NAME, Context.MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        loginPrefsEditor.clear();
        loginPrefsEditor.apply();

        Toast.makeText(context, context.getString(R.string.toast_sign_out), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(context, LoginActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        context.startActivity(intent);
    }
}
