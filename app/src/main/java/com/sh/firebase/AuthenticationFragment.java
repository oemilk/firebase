package com.sh.firebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
		Button signUpButton = (Button) v.findViewById(R.id.sign_up_button);
		Button signInButton = (Button) v.findViewById(R.id.sign_in_button);
		Button signOutButton = (Button) v.findViewById(R.id.sign_out_button);
		signUpButton.setOnClickListener(mOnClickListener);
		signInButton.setOnClickListener(mOnClickListener);
		signOutButton.setOnClickListener(mOnClickListener);
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

	private View.OnClickListener mOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			int id = view.getId();
			switch (id) {
				case R.id.sign_up_button:
					createUserWithEmailAndPassword();
					break;
				case R.id.sign_in_button:
					signInWithEmailAndPassword();
					break;
				case R.id.sign_out_button:
					signOut();
					break;
			}
		}
	};

	private void initFBAuthentication() {
		mFirebaseAuth = FirebaseAuth.getInstance();
		mAuthStateListener = new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
				String message;
				if (firebaseUser != null) {
					message= "onAuthStateChanged signed in : " + firebaseUser.getUid();
				} else {
					message= "onAuthStateChanged signed out";
				}
				mTextView.setText(message);
			}
		};
	}

	private void createUserWithEmailAndPassword() {
		String email = "oemilk@naver.com"; // email address format
		String password = "123456"; // at least 6 characters
		mFirebaseAuth.createUserWithEmailAndPassword(email, password)
				.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						String message;
						if (task.isSuccessful()) {
							message = "success createUserWithEmailAndPassword";
						} else {
							message = "fail createUserWithEmailAndPassword";
						}
						mTextView.setText(message);
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				mTextView.setText(e.getMessage());
				e.printStackTrace();
			}
		});
	}

	private void signInWithEmailAndPassword() {
		String email = "test@test.com";
		String password = "123456";
		mFirebaseAuth.signInWithEmailAndPassword(email, password)
				.addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						String message;
						if (task.isSuccessful()) {
							message = "success signInWithEmailAndPassword";
						} else {
							message = "fail signInWithEmailAndPassword";
						}
						mTextView.setText(message);
					}
				}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				mTextView.setText(e.getMessage());
				e.printStackTrace();
			}
		});
	}

	private void signOut() {
		if (mFirebaseAuth != null) {
			mFirebaseAuth.signOut();
		}
	}

}
