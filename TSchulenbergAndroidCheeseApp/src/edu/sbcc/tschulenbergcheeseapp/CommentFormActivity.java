package edu.sbcc.tschulenbergcheeseapp;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class CommentFormActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_form);
	}

	public void onCancelClicked(View v) {
		Toast.makeText(this, "Thanks for nothing...", Toast.LENGTH_LONG).show();
	}

	public void onOkClicked(View v) {
		RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		// EditText commentEdit = (EditText) findViewById(R.id.commentEdit);
		String message = "Thank you for the " + ratingBar.getRating() + " star rating.";
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}

}
