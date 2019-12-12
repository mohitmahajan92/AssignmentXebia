package com.example.assignment;


import android.os.AsyncTask;


public class MtThread extends AsyncTask<VoidFunction, Integer, Long> {
	
	VoidFunction uiFunction;

	@Override
	protected Long doInBackground(VoidFunction... arg0) {
		assert(arg0.length == 2);
		
		VoidFunction f = arg0[0];
		f.function();
		
		uiFunction = arg0[1];
		
		return null;
	}
    
	protected void onProgressUpdate(Integer... progress) {
        
    }

    protected void onPostExecute(Long result) {
        uiFunction.function();
    }

}
