package com.mk.zermelo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

public class ScheduleViewModel extends AndroidViewModel {
	private Repository repository;
	private Context context;

	public ScheduleViewModel(@NonNull Application application) {
		super(application);

		// Get context
		context = application.getApplicationContext();

		// Get repository
		repository = new Repository(application);
	}

	public LiveData<User> getUser() {
		SharedPreferences sharedPrefs = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
		int userId = sharedPrefs.getInt(context.getString(R.string.current_user_key), -1);
		return repository.getUser(userId);
	}

	public void refreshAppointments(User user) {
		try {
			repository.refreshAppointments(user);
		} catch (IOException | JSONException e) {
			Toast.makeText(context, context.getResources().getString(R.string.refresh_error),
					Toast.LENGTH_LONG).show();
		}
	}

	public LiveData<List<Appointment>> getAppointments(User user) {
		long timeStart = (TimeProvider.getDayStart().getTime() / 1000);
		long timeEnd = (TimeProvider.getDayEnd().getTime() / 1000);
		LiveData<List<Appointment>> appointments = repository.getAppointments(user, timeStart, timeEnd);
		return appointments;
	}
}
