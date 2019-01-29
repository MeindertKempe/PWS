package com.mk.zermelo;

import android.app.Application;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class Repository {
	private UserDao userDao;
	private AppointmentDao appointmentDao;

	private LiveData<ArrayList<Appointment>> Appointments;

	public Repository(Application application) {
		Database database = Database.getInstance(application);
		userDao = database.userDao();
	}

	//TODO get Appointments from database

	private void insertUser(User user) {
		new InsertUserAsyncTask(userDao).execute(user);
	}

	public void insertAppointment(Appointment appointment) {
		new InsertAppointmentAsyncTask(appointmentDao).execute(appointment);
	}

	public void deleteAllAppointments() {
		new DeleteAllAppointmentsAsyncTask(appointmentDao).execute();
	}

	public LiveData<List<Appointment>> getAppointments(long timeStart, long timeEnd) {
		return appointmentDao.getAppointments(timeStart, timeEnd);
	}

	private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
		private UserDao userDao;

		private InsertUserAsyncTask(UserDao userDao) {
			this.userDao = userDao;
		}

		@Override
		protected Void doInBackground(User... users) {
			userDao.insertUser(users[0]);
			return null;
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

	private static class DeleteAllAppointmentsAsyncTask extends AsyncTask<Void, Void, Void> {
		private AppointmentDao appointmentDao;

		private DeleteAllAppointmentsAsyncTask(AppointmentDao appointmentDao) {
			this.appointmentDao = appointmentDao;
		}

		@Override
		protected Void doInBackground(Void... voids) {
			appointmentDao.deleteAllAppointments();
			return null;
		}
	}
}
