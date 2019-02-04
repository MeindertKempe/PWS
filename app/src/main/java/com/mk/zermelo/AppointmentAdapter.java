package com.mk.zermelo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentHolder> {
	private List<Appointment> appointments = new ArrayList<Appointment>();
	private Context context;

	@NonNull
	@Override
	public AppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.appointment_card, parent, false);

		// Save application context
		context = view.getContext().getApplicationContext();

		return new AppointmentHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull AppointmentHolder holder, int position) {
		Appointment appointment = appointments.get(position);

		if (appointment.isCancelled()) {
			holder.cardLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorCancelled));
		} else if (appointment.isMoved()) {
			holder.cardLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.colorMoved));
		}

		// create proper format for time

		// need milliseconds
		long startTime = appointment.getStart() * 1000;
		long endTime = appointment.getEnd() * 1000;

		Date startDate = new Date();
		startDate.setTime(startTime);
		Date endDate = new Date();
		endDate.setTime(endTime);

		// Get local date format
		DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(context);
		//DateFormat dateFormat = java.text.DateFormat.getTimeInstance();


		holder.time.setText(dateFormat.format(startDate) + " - " + dateFormat.format(endDate));
		holder.subjects.setText(appointment.getSubjects());
		holder.locations.setText(appointment.getLocations());
		holder.teachers.setText(appointment.getTeachers());
		holder.groups.setText(appointment.getGroups());
	}

	@Override
	public int getItemCount() {
		return appointments.size();
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
		notifyDataSetChanged();
	}

	class AppointmentHolder extends RecyclerView.ViewHolder {
		private TextView time;
		private TextView subjects;
		private TextView locations;
		private TextView teachers;
		private TextView groups;
		private ConstraintLayout cardLayout;

		public AppointmentHolder(@NonNull View itemView) {
			super(itemView);
			time = itemView.findViewById(R.id.time);
			subjects = itemView.findViewById(R.id.subjects);
			locations = itemView.findViewById(R.id.locations);
			teachers = itemView.findViewById(R.id.teachers);
			groups = itemView.findViewById(R.id.groups);
			cardLayout = itemView.findViewById(R.id.card_layout);
		}
	}
}
