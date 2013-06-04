package com.example.languageproject;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
//import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;


public class Main_menu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle("Home");
		
        actionBar.addAction(new SearchAction());
        
		Button countries = (Button) findViewById(R.id.button1);
		countries.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent countriesActivity = new Intent();
				countriesActivity.setClass(arg0.getContext(), CountriesList.class);
				startActivity(countriesActivity);
				
			}
		});
		
		
		Button help = (Button) findViewById(R.id.button2);
		help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent countriesActivity = new Intent();
//				countriesActivity.setClass(arg0.getContext(), CountriesList.class);
//				startActivity(countriesActivity);
				
			}
		});
		
		
		Button about = (Button) findViewById(R.id.button3);
		about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent countriesActivity = new Intent();
//				countriesActivity.setClass(arg0.getContext(), CountriesList.class);
//				startActivity(countriesActivity);
				
			}
		});
	}
	
	private class SearchAction extends AbstractAction{

		public SearchAction() {			
			// TODO Auto-generated constructor stub
			super(R.drawable.action_search);
		}

		@Override
		public void performAction(View view) {
			// TODO Auto-generated method stub
			Intent searchActivity = new Intent();
			searchActivity.setClass(view.getContext(), Search_activity.class);
			startActivity(searchActivity);
		}
		
	}
	
	 public static Intent createIntent(Context context) {
	        Intent i = new Intent(context, Main_menu.class);
	        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        return i;
	    }

}
