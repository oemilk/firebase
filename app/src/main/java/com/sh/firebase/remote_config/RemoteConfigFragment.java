package com.sh.firebase.remote_config;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.sh.firebase.R;

import java.util.HashMap;
import java.util.Map;

public class RemoteConfigFragment extends Fragment {

	private final String TAG = "RemoteConfigFragment";

	private final long CACHE_EXPIRATION = 0;
	private final String FB_REMOTE_CONFIG_STRING = "remote_config_string";
	private final String FB_REMOTE_CONFIG_BOOLEAN = "remote_config_boolean";
	private final String FB_REMOTE_CONFIG_BYTE_ARRAY = "remote_config_byte_array";
	private final String FB_REMOTE_CONFIG_DOUBLE = "remote_config_double";
	private final String FB_REMOTE_CONFIG_LONG = "remote_config_long";
	private final String FB_REMOTE_CONFIG_COLOR = "remote_config_color";

	private FirebaseRemoteConfig mFirebaseRemoteConfig;

	private TextView mTextView;

	public RemoteConfigFragment() {
		// Required empty public constructor
	}

	public static RemoteConfigFragment newInstance() {
		RemoteConfigFragment fragment = new RemoteConfigFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initFBRemoteConfig();
		fetch();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_remote_config, container, false);
		mTextView = (TextView) v.findViewById(R.id.remote_config_textview);
		return v;
	}

	private void initFBRemoteConfig() {
		mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
		FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
				.setDeveloperModeEnabled(true)
				.build();
		mFirebaseRemoteConfig.setConfigSettings(configSettings);

		Map<String, Object> map = new HashMap<>();
		map.put(FB_REMOTE_CONFIG_STRING, "default");
		map.put(FB_REMOTE_CONFIG_BOOLEAN, false);
		map.put(FB_REMOTE_CONFIG_BYTE_ARRAY, new byte[0]);
		map.put(FB_REMOTE_CONFIG_DOUBLE, 0.0);
		map.put(FB_REMOTE_CONFIG_LONG, 555);
		map.put(FB_REMOTE_CONFIG_COLOR, "#000000");
		mFirebaseRemoteConfig.setDefaults(map);
	}

	private void fetch() {
		mFirebaseRemoteConfig.fetch(CACHE_EXPIRATION).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (task.isSuccessful()) {
					Log.d(TAG, "onComplete Succeeded");
					mFirebaseRemoteConfig.activateFetched();

					String str = mFirebaseRemoteConfig.getString(FB_REMOTE_CONFIG_STRING);
					boolean b = mFirebaseRemoteConfig.getBoolean(FB_REMOTE_CONFIG_BOOLEAN);
//					byte [] arrBytes = mFirebaseRemoteConfig.getByteArray(FB_REMOTE_CONFIG_BYTE_ARRAY);
					double d = mFirebaseRemoteConfig.getDouble(FB_REMOTE_CONFIG_DOUBLE);
					long l = mFirebaseRemoteConfig.getLong(FB_REMOTE_CONFIG_LONG);
					int color = Color.parseColor(mFirebaseRemoteConfig.getString(FB_REMOTE_CONFIG_COLOR));

					StringBuilder builder = new StringBuilder();
					builder.append("String : ").append(str).append("\n");
					builder.append("boolean : ").append(b).append("\n");
//					builder.append("byte : ").append(arrBytes[0]).append("\n");
					builder.append("double : ").append(d).append("\n");
					builder.append("long : ").append(l).append("\n");
					builder.append("color : ").append(color).append("\n");

					mTextView.setText(builder);
					mTextView.setBackgroundColor(color);
				} else {
					Log.d(TAG, "onComplete failed");
				}
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Log.d(TAG, "onFailure : " + e.getMessage());
			}
		});
	}

}
