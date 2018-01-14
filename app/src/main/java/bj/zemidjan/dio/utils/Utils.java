package bj.zemidjan.dio.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

import bj.zemidjan.dio.EducArsenalService;
import bj.zemidjan.dio.R;
//import bj.zemidjan.educarsenal.EducarsenalService;

import java.util.regex.Pattern;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;*/

/**
 * Created by Hnarech on 30-04-2015.
 */
public class Utils {

    private static final String PREFERENCES_FILE = "educarsenal_settings";


    public static int getToolbarHeight(Context context) {
        int height = (int) context.getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
        return height;
    }

   /* public static int getStatusBarHeight(Context context) {
        int height = (int) context.getResources().getDimension(R.dimen.statusbar_size);
        return height;
    }
*/
    public static EducArsenalService retrofitBuilder(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(EducArsenalService.ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        EducArsenalService educArsenalService =
                retrofit.create(EducArsenalService.class);
        return educArsenalService;
    }

    public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    public static boolean isValiEmail(String email)
    {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    public static Drawable tintMyDrawable(Drawable drawable, int color) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, color);
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_IN);
        return drawable;
    }


    public static String readSharedSetting(Context ctx, String settingName, String defaultValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(settingName, defaultValue);
    }

    public static void saveSharedSetting(Context ctx, String settingName, String settingValue) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(settingName, settingValue);
        editor.apply();
    }

}
