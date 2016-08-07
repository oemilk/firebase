package com.sh.firebase.analytics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.sh.firebase.R;

public class AnalyticsFragment extends Fragment {

	private final String TAG = "AnalyticsFragment";

	private FirebaseAnalytics mFirebaseAnalytics;

	private TextView mTextView;

	public AnalyticsFragment() {
		// Required empty public constructor
	}

	public static AnalyticsFragment newInstance() {
		AnalyticsFragment fragment = new AnalyticsFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initFBAnalytics();
		setUserProperties();
		levelUpLogEvents();
		generateLeadLogEvents();
		logCustomEvents();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_analytics, container, false);
		mTextView = (TextView) v.findViewById(R.id.analytics_textview);
		return v;
	}
	private void initFBAnalytics() {
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
	}

	private void setUserProperties() {
		mFirebaseAnalytics.setUserProperty("test_user_property", "test user");
	}

	private void levelUpLogEvents() {
		Bundle bundle = new Bundle();
		bundle.putString(FirebaseAnalytics.Param.CHARACTER, "test character");
		bundle.putLong(FirebaseAnalytics.Param.LEVEL, 10);
		mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_UP, bundle);
	}

	private void generateLeadLogEvents() {
		Bundle bundle = new Bundle();
		bundle.putString(FirebaseAnalytics.Param.VALUE, "test value");
		bundle.putString(FirebaseAnalytics.Param.CURRENCY, "USD");
		mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.GENERATE_LEAD, bundle);
	}

	private void logCustomEvents() {
		Bundle bundle = new Bundle();
		bundle.putString("custom_id", "custom test id");
		bundle.putString("custom_name", "custom test name");
		bundle.putString("custom_location_id", "custom test location id");
		mFirebaseAnalytics.logEvent("custom_event", bundle);
	}

}
