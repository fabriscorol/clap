package com.example.languageproject;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class LessonList extends Activity {

	private static String HTTP_GET_STRING = "http://www.celebrate-language.com/public-api/?action=get_lessons_by_lang&lang=";
	private static Context context;
	
	private String language_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lesson_list);
		
		Intent i=getIntent();
		Bundle b=i.getExtras();
		language_name=b.getString("languageName");
		
		setTitle(language_name);
		
		LessonList.context = getApplicationContext();
		
		new LongRunningGetIO().execute();
	}

	private class LongRunningGetIO extends AsyncTask<Void, Void, String> {
		private ListView lv;

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
			HttpGet httpGet = new HttpGet(HTTP_GET_STRING + language_name);
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
				
				lv = (ListView) findViewById(R.id.lesson_list);

				results = results.replace("\"", "");
				results = results.substring(1,results.length()-1);
				String[] strArray = results.split(",");

				ArrayAdapter<String> adapter;
				adapter = new ArrayAdapter<String>(context,R.layout.lesson_item, R.id.language_item_id, strArray);
				lv.setAdapter(adapter);
				
				lv.setOnItemClickListener(new OnItemClickListener(){

					@Override
					public void onItemClick(AdapterView<?> parent, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub
						
						String selectedFromList =(parent.getAdapter().getItem(position).toString());
						Intent lesson_name =new Intent(context, LessonList.class);
						lesson_name.putExtra("lessonName", selectedFromList);
						startActivity(lesson_name);
						
					}
					
				});

			}
		}

	}
}
