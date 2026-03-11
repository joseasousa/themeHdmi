package br.com.joseasousa.theme.hdmi;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class WriteSettingsPermissionManager {
    private static final String TAG = "WriteSettingsPermMgr";
    private static volatile Boolean testCanWrite;

    private WriteSettingsPermissionManager() {
    }

    public static boolean canWrite(Context context) {
        Boolean override = testCanWrite;
        if (override != null) {
            return override;
        }
        return Settings.System.canWrite(context);
    }

    public static Intent buildManageWriteSettingsIntent(Context context) {
        return buildCandidateIntents(context).get(0);
    }

    public static boolean openBestSettingsScreen(Activity activity) {
        for (Intent candidate : buildCandidateIntents(activity)) {
            if (candidate.resolveActivity(activity.getPackageManager()) == null) {
                continue;
            }
            try {
                activity.startActivity(candidate);
                return true;
            } catch (ActivityNotFoundException | SecurityException ex) {
                Log.w(TAG, "Settings intent failed: " + candidate.getAction(), ex);
            }
        }
        return false;
    }

    static void setTestCanWrite(@Nullable Boolean value) {
        testCanWrite = value;
    }

    static void clearTestOverride() {
        testCanWrite = null;
    }

    private static List<Intent> buildCandidateIntents(Context context) {
        String packageUri = "package:" + context.getPackageName();
        List<Intent> intents = new ArrayList<>(5);

        Intent manageWriteSettingsForApp = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        manageWriteSettingsForApp.setData(Uri.parse(packageUri));
        intents.add(manageWriteSettingsForApp);

        intents.add(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS));

        Intent appDetails = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        appDetails.setData(Uri.parse(packageUri));
        intents.add(appDetails);

        intents.add(new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS));
        intents.add(new Intent(Settings.ACTION_SETTINGS));
        return intents;
    }
}
