package com.sh.firebase.authentication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sh.firebase.R;

public class AuthenticationForAnonymousFragment extends Fragment {

    private final String TAG = "AuthenticationForAnonymousFragment";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private TextView mAuthStateTextview;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = view.getId();
            switch (id) {
                case R.id.anonymous_sign_in_button:
                    signInAnonymously();
                    break;
                case R.id.anonymous_convert_button:
                    convertToPermanentAccount();
                    break;
            }
        }
    };

    public AuthenticationForAnonymousFragment() {
        // Required empty public constructor
    }

    public static AuthenticationForAnonymousFragment newInstance() {
        AuthenticationForAnonymousFragment fragment = new AuthenticationForAnonymousFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFBAuthentication();
        initFBAuthState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_authentication_for_anonymous, container, false);
        mAuthStateTextview = (TextView) v.findViewById(R.id.auth_state_textview);
        Button anonymousSignInButton = (Button) v.findViewById(R.id.anonymous_sign_in_button);
        Button anonymousConvertButton = (Button) v.findViewById(R.id.anonymous_convert_button);
        anonymousSignInButton.setOnClickListener(mOnClickListener);
        anonymousConvertButton.setOnClickListener(mOnClickListener);
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
    }

    private void initFBAuthState() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                String message;
                if (firebaseUser != null) {
                    message = "onAuthStateChanged signed in : " + firebaseUser.getUid();
                } else {
                    message = "onAuthStateChanged signed out";
                }
                mAuthStateTextview.setText(message);
            }
        };
    }

    private void signInAnonymously() {
        mFirebaseAuth.signInAnonymously()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String message;
                        if (!task.isSuccessful()) {
                            message = "signInAnonymously fail : " + task.getException();
                        } else {
                            message = "signInAnonymously success";
                        }
                        mAuthStateTextview.setText(message);
                    }
                });
    }

    private void convertToPermanentAccount() {
//        AuthCredential credential = GoogleAuthProvider.getCredential(googleIdToken, null);
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        String email = "anonymoustest@test.com";
        String password = "123456";
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        mFirebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String message;
                        if (!task.isSuccessful()) {
                            message = "fail : " + task.getException();
                        } else {
                            message = "success";
                        }
                        mAuthStateTextview.setText(message);
                    }
                });
    }

}
