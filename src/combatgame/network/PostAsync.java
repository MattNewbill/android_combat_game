package combatgame.network;

import android.os.AsyncTask;

public class PostAsync extends AsyncTask<PostWrapper, Void, String> {

	private OnCompletion finished;
	
	public PostAsync(OnCompletion onCompletion) {
		finished = onCompletion;
	}

	@Override
	protected String doInBackground(PostWrapper... params) {
		if(params == null || params[0] == null)
			return "";
		return Internet.postJSON(params[0].url, params[0].obj);
	}
	
	@Override
	public void onPostExecute(String result) {
		finished.onComplete(result);
	}

}
