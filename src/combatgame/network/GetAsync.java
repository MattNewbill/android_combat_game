package combatgame.network;

import android.os.AsyncTask;

public class GetAsync extends AsyncTask<String, Void, String> {

	private OnCompletion finished;
	
	public GetAsync(OnCompletion onComplete) {
		finished = onComplete;
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		if(params == null || params[0] == null)
			return "";
		return Internet.getJSON(params[0]);
	}
	
	@Override
	protected void onPostExecute(String result) {
		finished.onComplete(result);
	}

}
