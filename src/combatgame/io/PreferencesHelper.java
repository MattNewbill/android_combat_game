package combatgame.io;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesHelper {

	private SharedPreferences sharedPreferences;
	private Editor editor;
	private static final String APP_SHARED_PREFS = 
			PreferencesHelper.class.getSimpleName(); //name of the file
	
	@SuppressLint("CommitPrefEdits")
	public PreferencesHelper(Context context) {
		this.sharedPreferences = context.getSharedPreferences
				(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
		this.editor = sharedPreferences.edit();
	}

	public void clearPreferences(Context context) {
		this.sharedPreferences = context.getSharedPreferences
				(APP_SHARED_PREFS, Activity.MODE_PRIVATE);
		this.editor.clear();
		this.editor.commit();
	}
	
	public void putNameAndID(String name, long id) {
		editor.putString("name", name);
		editor.putLong("id", id);
		editor.apply();
	}
	
	public long getID() {
		return sharedPreferences.getLong("id", -1L);
	}
	
	public String getName() {
		return sharedPreferences.getString("name", "def");
	}
	
	
} //end PreferenceHelper class
