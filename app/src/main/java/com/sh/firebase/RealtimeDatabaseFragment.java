package com.sh.firebase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RealtimeDatabaseFragment extends Fragment {

	private final String TAG = "RealtimeDatabaseFragment";

	private final String REFERENCE_NAME = "first_reference";
	private final String REFERENCE_NAME_FOR_JAVA_OBJECT = "java_object_reference";

	private FirebaseDatabase mFirebaseDatabase;
	private DatabaseReference mDatabaseReference;
	private DatabaseReference mDatabaseReferenceForJavaObject;

	private TextView mTextView;

	public RealtimeDatabaseFragment() {
		// Required empty public constructor
	}

	public static RealtimeDatabaseFragment newInstance() {
		RealtimeDatabaseFragment fragment = new RealtimeDatabaseFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initFBRealtimeDatabase();
		write();
		writeByJavaObject();
		read();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_realtime_database, container, false);
		mTextView = (TextView) v.findViewById(R.id.realtime_database_textview);
		return v;
	}

	private void initFBRealtimeDatabase() {
		mFirebaseDatabase = FirebaseDatabase.getInstance();
		mDatabaseReference = mFirebaseDatabase.getReference(REFERENCE_NAME);
		mDatabaseReferenceForJavaObject = mFirebaseDatabase.getReference(REFERENCE_NAME_FOR_JAVA_OBJECT);
	}

	private void write() {
		mDatabaseReference.setValue("Test 1");
	}

	private void writeByJavaObject() {
		RealtimeDatabaseData data = new RealtimeDatabaseData("test code", "test title", "test text");
		mDatabaseReferenceForJavaObject.setValue(data);
	}

	private void read() {
		mDatabaseReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				String value = dataSnapshot.getValue(String.class);
				mTextView.setText(value);
			}

			@Override
			public void onCancelled(DatabaseError error) {
				mTextView.setText(error.getMessage());
			}
		});
		mDatabaseReferenceForJavaObject.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				RealtimeDatabaseData data = dataSnapshot.getValue(RealtimeDatabaseData.class);
				String text = data.code + data.title + data.text;
				mTextView.setText(text);
			}

			@Override
			public void onCancelled(DatabaseError error) {
				mTextView.setText(error.getMessage());
			}
		});
	}

}
