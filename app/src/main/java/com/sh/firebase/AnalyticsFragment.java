package com.sh.firebase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AnalyticsFragment extends Fragment {

	private final String TAG = "AnalyticsFragment";

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_analytics, container, false);
		mTextView = (TextView) v.findViewById(R.id.analytics_textview);
		return v;
	}

}
