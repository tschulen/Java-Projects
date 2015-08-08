package edu.sbcc.tschulenbergcheeseapp;

import org.w3c.dom.*;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import edu.sbcc.cs123.webutils.*;

public class MainActivity extends Activity implements OnItemClickListener {

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			float rating = data.getExtras().getFloat("rating");
			String comment = data.getExtras().getString("comment");
			int position = data.getExtras().getInt("position");
			cheeses[position] = cheeses[position] + " - " + rating + " stars. " + comment;
			cheesesAdapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private String[] cheeses = new String[400];
	private ArrayAdapter<String> cheesesAdapter;
	private ListView cheesesListView;
	public String[] test = new String[400];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		cheeses = getResources().getStringArray(R.array.Cheeses);
		cheesesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cheeses);

		cheesesListView = (ListView) findViewById(R.id.listView1);
		cheesesListView.setAdapter(cheesesAdapter);
		cheesesListView.setOnItemClickListener(this);

		new UpdateCheesesAsyncTask().execute(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this, CommentFormActivity.class);
		intent.putExtra("position", position);
		intent.putExtra("item", cheesesAdapter.getItem(position));
		startActivityForResult(intent, 0);

	}

	private void updateView() {
		int count = 0;

		while (count < 350) {
			cheeses[count] = test[count];
			count++;
		}
		cheesesAdapter.notifyDataSetChanged();
	}

	private class UpdateCheesesAsyncTask extends AsyncTask<Object, Integer, Exception> {

		@Override
		protected Exception doInBackground(Object... params) {
			try {

				String wikipediaUrl = "http://en.wikipedia.org/w/api.php?action=parse&page=List_of_cheeses&format=xml";
				Document doc = XmlUtil.getDocument(wikipediaUrl);

				int slot = 0;
				int count = 9;
				while (count < 350) {
					String path = "/api/parse/links/pl[" + count + "]";
					test[slot] = XmlUtil.selectNodeAsString(doc, path);
					slot++;
					count++;
				}

				return null;
			} catch (Exception ex) {
				return ex;
			}

		}

		@Override
		protected void onPostExecute(Exception error) {
			updateView();
		}

	}

}