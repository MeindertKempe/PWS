package com.mk.zermelo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;

public class Repository {
	private UserDao userDao;
	private AppointmentDao appointmentDao;
	private Context context;
	private static final String TAG = "Repository";

	public Repository(Application application) {
		// Get context
		context = application.getApplicationContext();
		// Get database instance
		Database database = Database.getInstance(application);
		// Get daos
		userDao = database.userDao();
		appointmentDao = database.appointmentDao();
	}

	public User addUser(String url, String authCode) throws IOException {
		// Create a new user

		// Save school url
		User user = new User(url);

		// Get api token from zermelo
		Zermelo zermelo = new Zermelo(user);
		String accessToken = zermelo.requestApiToken(url, authCode);
		if (accessToken == null) {
			// Throw an exception if no token has been returned.
			throw new IOException();
		}

		// Set the users api token
		user.setApiToken(accessToken);

		// Initialise temporary user id
		long id = -1;

		// Add user to database
		try {
			id = insertUser(user);
		} catch (InterruptedException | ExecutionException e) {
			// If error occurs throw new exception
			e.printStackTrace();
			throw new IOException();
		}

		// Cast long to integer
		// TODO fix this
		int intID = ((int) id);


		// Set current user in preferences.
		SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(context.getString(R.string.current_user_key), intID);
		editor.apply();

		// Set id for User object
		user.setId(intID);

		// Return user
		return user;
	}

	public void refreshAppointments(User user) throws IOException, JSONException {
		long startTime = (TimeProvider.getDayStart().getTime()) / 1000;
		long endTime = (TimeProvider.getDayEnd().getTime()) / 1000;

		Log.i(TAG, "starttime: " + Long.toString(startTime));
		Log.i(TAG, "endtime: " + Long.toString(endTime));


		// get new Zermelo instance
		Zermelo zermelo = new Zermelo(user);

		JSONObject json = zermelo.requestAppointments(startTime, endTime);
		if (json == null) {
			throw new IOException();
		}

		Log.i(TAG, json.toString(4));

		int userId = user.getId();

		JSONArray array = json.getJSONObject("response").getJSONArray("data");

		ArrayList<Appointment> appointments = new ArrayList<Appointment>();

		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);

			JSONArray subjectsArray = object.getJSONArray("subjects");
			String subjects = "";
			for (int j = 0; j < subjectsArray.length(); j++) {
				String subject = subjectsArray.getString(j);
				subjects += subject + " ";
			}

			JSONArray teachersArray = object.getJSONArray("teachers");
			String teachers = "";
			for (int j = 0; j < teachersArray.length(); j++) {
				String teacher = teachersArray.getString(j);
				teachers += teacher + " ";
			}

			JSONArray groupsInDepartmentsArray = object.getJSONArray("groupsInDepartments");
			String groupsInDepartments = "";
			for (int j = 0; j < groupsInDepartmentsArray.length(); j++) {
				String groupInDepartment = groupsInDepartmentsArray.getString(j);
				groupsInDepartments += groupInDepartment + " ";
			}

			JSONArray groupsArray = object.getJSONArray("groups");
			String groups = "";
			for (int j = 0; j < groupsArray.length(); j++) {
				String group = groupsArray.getString(j);
				groups += group + " ";
			}

			JSONArray locationsOfBranchArray = object.getJSONArray("locationsOfBranch");
			String locationsOfBranch = "";
			for (int j = 0; j < locationsOfBranchArray.length(); j++) {
				String locationOfBranch = locationsOfBranchArray.getString(j);
				locationsOfBranch += locationOfBranch + " ";
			}

			JSONArray locationsArray = object.getJSONArray("locations");
			String locations = "";
			for (int j = 0; j < locationsArray.length(); j++) {
				String location = locationsArray.getString(j);
				locations += location + " ";
			}

			Appointment appointment = new Appointment(
					object.getInt("id"),
					object.getLong("start"),
					object.getLong("end"),
					object.getInt("startTimeSlot"),
					object.getInt("endTimeSlot"),
					object.getString("startTimeSlotName"),
					object.getString("endTimeSlotName"),
					object.getBoolean("valid"),
					object.getBoolean("cancelled"),
					object.getBoolean("modified"),
					object.getBoolean("moved"),
					object.getBoolean("hidden"),
					object.getBoolean("new"),
					object.getString("remark"),
					object.getString("changeDescription"),
					object.getString("branch"),
					object.getInt("branchOfSchool"),
					object.getLong("created"),
					object.getLong("lastModified"),
					object.getInt("appointmentInstance"),
					object.getString("type"),
					subjects,
					teachers,
					groupsInDepartments,
					groups,
					locationsOfBranch,
					locations,
					userId
			);

			appointments.add(appointment);
		}

		// Delete old appointments
		deleteAllAppointments(user);

		// Insert new appointments
		for (Appointment appointment : appointments) {
			insertAppointment(appointment);
		}

	}

	// Dao methods
	private long insertUser(User user) throws ExecutionException, InterruptedException {
		long id = new InsertUserAsyncTask(userDao).execute(user).get();
		Log.i(TAG, "Adding user: " + Long.toString(id));
		return id;
	}

	public LiveData<User> getUser(int userId) {
		return userDao.getUser(userId);
	}

	public void insertAppointment(Appointment appointment) {
		new InsertAppointmentAsyncTask(appointmentDao).execute(appointment);
	}

	public void deleteAllAppointments(User user) {
		new DeleteAllAppointmentsAsyncTask(appointmentDao).execute(user);
	}

	public LiveData<List<Appointment>> getAppointments(User user, long timeStart, long timeEnd) {
		int userId = user.getId();
		return appointmentDao.getAppointments(userId, timeStart, timeEnd);
	}

	// AsyncTasks
	private static class InsertUserAsyncTask extends AsyncTask<User, Void, Long> {
		private UserDao userDao;

		private InsertUserAsyncTask(UserDao userDao) {
			this.userDao = userDao;
		}

		@Override
		protected Long doInBackground(User... users) {
			long id = userDao.insertUser(users[0]);
			return id;
		}
	}

	private static class InsertAppointmentAsyncTask extends AsyncTask<Appointment, Void, Void> {
		private AppointmentDao appointmentDao;

		private InsertAppointmentAsyncTask(AppointmentDao appointmentDao) {
			this.appointmentDao = appointmentDao;
		}

		@Override
		protected Void doInBackground(Appointment... appointments) {
			appointmentDao.insertAppointment(appointments[0]);
			return null;
		}
	}

	private static class DeleteAllAppointmentsAsyncTask extends AsyncTask<User, Void, Void> {
		private AppointmentDao appointmentDao;

		private DeleteAllAppointmentsAsyncTask(AppointmentDao appointmentDao) {
			this.appointmentDao = appointmentDao;
		}

		@Override
		protected Void doInBackground(User... users) {
			appointmentDao.deleteAllAppointments(users[0].getId());
			return null;
		}
	}
}
