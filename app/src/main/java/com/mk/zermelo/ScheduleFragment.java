package com.mk.zermelo;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import org.json.JSONException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ScheduleFragment extends Fragment {

	private ScheduleViewModel viewModel;
	private AppointmentAdapter adapter;
	private User currentUser;
	private boolean appointmentObserver = false;

	public static ScheduleFragment newInstance() {
		return new ScheduleFragment();
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.schedule_fragment, container, false);


		// RecyclerView
		RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
		recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
		recyclerView.setHasFixedSize(true);

		adapter = new AppointmentAdapter();
		recyclerView.setAdapter(adapter);

		// Pull to refresh
		final SwipeRefreshLayout pullRefresh = view.findViewById(R.id.pull_refresh);
		pullRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				pullRefresh.setRefreshing(true);
				// Refresh data
				viewModel.refreshAppointments(currentUser);
				// Stop refresh animation
				pullRefresh.setRefreshing(false);
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		this.viewModel = ViewModelProviders.of(this).get(ScheduleViewModel.class);

		viewModel.getUser().observe(this, new Observer<User>() {
			@Override
			public void onChanged(User user) {
				currentUser = user;
				viewModel.refreshAppointments(user);

				// Add appointment observer once user has been retrieved
				if(!appointmentObserver) {
					addAppointmentObserver();
				}
			}
		});

	}

	private void addAppointmentObserver() {
		// Indicate appointmentObserver has been set.
		appointmentObserver = true;
		viewModel.getAppointments(currentUser).observe(this, new Observer<List<Appointment>>() {
			@Override
			public void onChanged(List<Appointment> appointments) {
				Collections.sort(appointments);

				adapter.setAppointments(appointments);
			}
		});
	}
}
