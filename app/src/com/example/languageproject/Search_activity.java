package com.example.languageproject;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.IntentAction;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class Search_activity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle("Search");
        
      actionBar.setHomeAction(new IntentAction(this, Main_menu.createIntent(this), R.drawable.action_home));
      actionBar.setDisplayHomeAsUpEnabled(true);
      
      actionBar.addAction(new SearchAction());
	}
	
	private class SearchAction extends AbstractAction{

		public SearchAction() {			
			// TODO Auto-generated constructor stub
			super(R.drawable.action_search);
		}

		@Override
		public void performAction(View view) {
			// TODO Auto-generated method stub
			
		}
		
	}	

}
