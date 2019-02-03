package com.mk.zermelo;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Use main activity layout file
		setContentView(R.layout.activity_main);

		// Get application context
		context = this.getApplicationContext();

		if(savedInstanceState == null) {
			// Check for fist time startup
			SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
			int userId = sharedPrefs.getInt(context.getString(R.string.current_user_key), -1);

			// Get fragment manager
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

			// If first time startup load setup fragment, else load regular app
			if (userId == -1) {
				SetupFragment fragment = new SetupFragment();
				fragmentTransaction.add(R.id.fragmentContainer, fragment);
			} else {
				ScheduleFragment fragment = new ScheduleFragment();
				fragmentTransaction.add(R.id.fragmentContainer, fragment);
			}

			fragmentTransaction.commit();
		}
	}

	public void goToSchedule() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		ScheduleFragment fragment = new ScheduleFragment();
		fragmentTransaction.replace(R.id.fragmentContainer, fragment);
		fragmentTransaction.commit();
	}
}
