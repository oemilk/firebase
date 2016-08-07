package com.sh.firebase.crash_reporing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.sh.firebase.R;

public class CrashReportingFragment extends Fragment {

	private final String TAG = "CrashReportingFragment";

	private TextView mTextView;

	public CrashReportingFragment() {
		// Required empty public constructor
	}

	public static CrashReportingFragment newInstance() {
		CrashReportingFragment fragment = new CrashReportingFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initFBCrashReporting();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crash_reporting, container, false);
		mTextView = (TextView) v.findViewById(R.id.crash_reporting_textview);
		return v;
	}

	private void initFBCrashReporting() {
		FirebaseCrash.report(new Exception("My first Android non-fatal error"));

        // custom error
		FirebaseCrash.log("Activity created");
		FirebaseCrash.logcat(Log.ERROR, TAG, "Logcat error");
	}

}
