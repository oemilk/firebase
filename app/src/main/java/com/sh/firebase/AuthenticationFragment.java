package com.sh.firebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationFragment extends Fragment {

	private final String TAG = "AuthenticationFragment";

	private FirebaseAuth mFirebaseAuth;
	private FirebaseAuth.AuthStateListener mAuthStateListener;

	private TextView mTextView;

	public AuthenticationFragment() {
		// Required empty public constructor
	}

	public static AuthenticationFragment newInstance() {
		AuthenticationFragment fragment = new AuthenticationFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initFBAuthentication();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_authentication, container, false);
		mTextView = (TextView) v.findViewById(R.id.authenticaion_textview);
		mTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				createUserWithEmailAndPassword();
			}
		});
		return v;
	}

	@Override
	public void onStart() {
		super.onStart();
		mFirebaseAuth.addAuthStateListener(mAuthStateListener);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mAuthStateListener != null) {
			mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
		}
	}

	private void initFBAuthentication() {
		mFirebaseAuth = FirebaseAuth.getInstance();
		mAuthStateListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
				if (firebaseUser != null) {
					Log.d(TAG, "onAuthStateChanged signed in : " + firebaseUser.getUid());
				} else {
					Log.d(TAG, "onAuthStateChanged signed out");
				}
			}
		};
	}

	private void createUserWithEmailAndPassword() {
		String email = "oemilk@naver.com";
		String password = "12345";
		mFirebaseAuth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						String message;
						if (task.isSuccessful()) {
							message = "success";
						} else {
							message = "fail";
						}
						Toast.makeText(AuthenticationFragment.this.getActivity(), message, Toast.LENGTH_SHORT).show();
					}
				});
	}

}
