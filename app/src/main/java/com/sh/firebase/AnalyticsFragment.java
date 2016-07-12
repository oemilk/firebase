package com.sh.firebase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

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
		logEvents();
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

	private void logEvents() {
		Bundle bundle = new Bundle();
		bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "test id");
		bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "test name");
		bundle.putString(FirebaseAnalytics.Param.ITEM_LOCATION_ID, "test location id");
		mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_UP, bundle);
	}

	private void logCustomEvents() {
		Bundle bundle = new Bundle();
		bundle.putString("custom_id", "custom test id");
		bundle.putString("custom_name", "custom test name");
		bundle.putString("custom_location_id", "custom test location id");
		mFirebaseAnalytics.logEvent("custom_event", bundle);
	}

}
