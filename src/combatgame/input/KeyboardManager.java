package combatgame.input;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

public class KeyboardManager {

	private InputMethodManager imm;
	private Activity activity;
	
	private boolean hasFocus;
	
	public KeyboardManager(Activity activity) {
		this.activity = activity;
		imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE); 
	}
	
	public void showKeyboard() {
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.RESULT_SHOWN);
		hasFocus = true;
	}
	
	public void hideKeyboard() {
		imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
		hasFocus = false;
	}
	
	public boolean hasFocus() {
		return hasFocus;
	}
	
	public void destroy() {
		imm = null;
		activity = null;
	}
	
}
