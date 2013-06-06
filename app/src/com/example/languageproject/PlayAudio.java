package com.example.languageproject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.IntentAction;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PlayAudio extends Activity {
	private static String HTTP_GET_STRING = "http://www.celebrate-language.com/public-api/?action=get_phrases_by_lesson_id&id=";
	
	private String lesson_name;
	private String lesson_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_audio);
		
		Intent i = getIntent();
		Bundle b = i.getExtras();
		lesson_name = b.getString("lessonName");
		lesson_id = b.getString("lessonID");
		
		
		final ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle(lesson_name);
        
        actionBar.setHomeAction(new IntentAction(this, Main_menu.createIntent(this), R.drawable.action_home));
        actionBar.setDisplayHomeAsUpEnabled(true);
        
        actionBar.addAction(new SearchAction());        
        
		new LongRunningGetIO().execute();		
		
	}

	private class LongRunningGetIO extends AsyncTask<Void, Void, String> {
		protected String getASCIIContentFromEntity(HttpEntity entity)
				throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n > 0) {
				byte[] b = new byte[4096];
				n = in.read(b);
				if (n > 0)
					out.append(new String(b, 0, n));
			}
			return out.toString();
		}

		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet(HTTP_GET_STRING + lesson_id);
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet,
						localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return text;
		}
		
		protected void onPostExecute(String results) {
			if (results != null) {
				
				String temp = results.substring(1, results.length()-2); // remove surrounding brackets
				
				String[] strPhraseEntries = temp.split("],"); // remove ],[				
				
				ArrayList<PhraseEntry> phraseEntries = new ArrayList<PhraseEntry>();
				
				for (int i=0; i<strPhraseEntries.length; i++) {
					String tempEntry = strPhraseEntries[i].substring(2, strPhraseEntries[i].length()-1); // remove surrounding quotes
					
					tempEntry = tempEntry.replace("\"", "");
					String[] entry = tempEntry.split(",");
					
					if (entry != null) {
						// should have four entries
						// TODO: better error handling						
						phraseEntries.add(new PhraseEntry(entry[0], entry[1], entry[2], entry[3]));
						
					}				
										
				} 				
				
				final TextView texthaut = (TextView) findViewById(R.id.txtPhrase);
				texthaut.setText(phraseEntries.get(0)._phraseText);		
				
				final TextView texthaut2 = (TextView) findViewById(R.id.txtTranslate);				
				texthaut2.setText(phraseEntries.get(0)._translatedText);
				
		        final MediaPlayer mediaPlayer = new MediaPlayer();      

				String urlfichier=phraseEntries.get(0)._audioURL;
				
				try {
					mediaPlayer.setDataSource(urlfichier);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC); 
				
				try {
					mediaPlayer.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				
				
				//calcul de la durer du morceau 
				int iduration = mediaPlayer.getDuration(); 
				int minute = iduration/60000; 
				int intreste = iduration-minute*60000; 
				int iseconde= intreste/1000; 
				String sMetminutezero=""; 
				String sMetminuteseconde="";   
				if(minute < 10) 
					sMetminutezero="0";   
				if(iseconde < 10) 
					sMetminuteseconde="0";
				
				//j'affiche le titre suivi du temp de la chanson 
				String stitre = "titre "+sMetminutezero+minute+":"+sMetminuteseconde+iseconde; 
				
				
				Button btnplay = (Button)findViewById(R.id.btnPlay);
				btnplay.setBackgroundResource(R.drawable.av_play);
				
				btnplay.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						try{ 
							mediaPlayer.start(); 
						} 
						catch (Exception e) { 
//							texthaut.setText("erreur "+e.getMessage()); 
							}   
						} 
					});
				
				
			}
		}
	}
	
	public class PhraseEntry {
		private String _phraseId;
		private String _phraseText;
		private String _translatedText;
		private String _audioURL;
		
		// Constructors
		public PhraseEntry(String id, String pText, String tText, String url) {
			_phraseId = id;
			_phraseText = pText;
			_translatedText = tText;
			_audioURL = url;
		}
		
		public PhraseEntry() {
			_phraseId = "";
			_phraseText = "";
			_translatedText = "";
			_audioURL = "";
		}
		
		// Setters
		public void setPhraseId(String id) {
			_phraseId = id;
		}
		
		public void setPhraseText(String text) {
			_phraseText = text;
		}
		
		public void setTranslatedText(String text) {
			_translatedText = text;
		}
		
		public void setAudioURL(String url) {
			_audioURL = url;
		}
		
		// Getters
		public String getPhraseId() {
			return _phraseId;
		}
		
		public String getPhraseText() {
			return _phraseText;
		}
		
		public String getTranslatedText() {
			return _translatedText;
		}
		
		public String getAudioURL() {
			return _audioURL;
		}
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
}
