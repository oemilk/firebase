package com.sh.firebase.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sh.firebase.R;

public class AuthenticationFragment extends Fragment {

    private final String TAG = "AuthenticationFragment";

    private final String WEB_CLIENT_ID = "928488830433-camll1e451a5i4a0vrart4aia0rq707i.apps.googleusercontent.com";
    private final int GOOGLE_SIGN_IN_REQUEST_CODE = 0;
    private final int FACEBOOK_LOG_IN_REQUEST_CODE = 64206;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private GoogleApiClient mGoogleApiClient; // for google sign in
    private CallbackManager mFacebookCallbackManager; // for facebook log in

    private TextView mPasswordTextView;
    private TextView mGoogleSignInTextView;
    private TextView mFaceBookLogInTextView;
    private TextView mAuthStateTextview;

    private LoginButton mFacebookLoginButton;

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

        initFBPasswordAuthentication();
        initFBGoogleSignIn();
        initFBAuthState();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_authentication, container, false);
        mPasswordTextView = (TextView) v.findViewById(R.id.pw_authenticaion_textview);
        mGoogleSignInTextView = (TextView) v.findViewById(R.id.google_sign_in_textview);
        mAuthStateTextview = (TextView) v.findViewById(R.id.auth_state_textview);
        mFaceBookLogInTextView = (TextView) v.findViewById(R.id.facebook_login_textview);

        Button pwSignUpButton = (Button) v.findViewById(R.id.pw_sign_up_button);
        Button pwSignInButton = (Button) v.findViewById(R.id.pw_sign_in_button);
        Button pwSignOutButton = (Button) v.findViewById(R.id.pw_sign_out_button);
        Button googlesignInButton = (Button) v.findViewById(R.id.google_sign_in_button);
        Button googlesignOutButton = (Button) v.findViewById(R.id.google_sign_out_button);

        mFacebookLoginButton = (LoginButton) v.findViewById(R.id.facebook_login_button);
        initFBFacebookLogIn();

        pwSignUpButton.setOnClickListener(mOnClickListener);
        pwSignInButton.setOnClickListener(mOnClickListener);
        pwSignOutButton.setOnClickListener(mOnClickListener);
        googlesignInButton.setOnClickListener(mOnClickListener);
        googlesignOutButton.setOnClickListener(mOnClickListener);
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
                case R.id.pw_sign_up_button:
                    createUserWithEmailAndPassword();
                    break;
                case R.id.pw_sign_in_button:
                    signInWithEmailAndPassword();
                    break;
                case R.id.pw_sign_out_button:
                    signOut();
                    break;
                case R.id.google_sign_in_button:
                    signInWithGoogleSignIn();
                    break;
                case R.id.google_sign_out_button:
                    signOut();
                    break;
            }
        }
    };

    private void initFBPasswordAuthentication() {
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
                        mPasswordTextView.setText(message);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mPasswordTextView.setText(e.getMessage());
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
                        mPasswordTextView.setText(message);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mPasswordTextView.setText(e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void signOut() {
        if (mFirebaseAuth != null) {
            mFirebaseAuth.signOut();
        }
    }

    private void initFBGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        Context context = getContext();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        mGoogleSignInTextView.setText(connectionResult.getErrorMessage());
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    private void signInWithGoogleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                mGoogleSignInTextView.setText("failed");
            }
        } else if (requestCode == FACEBOOK_LOG_IN_REQUEST_CODE) {
            mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(AuthenticationFragment.this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String message;
                        if (task.isSuccessful()) {
                            message = "success firebaseAuthWithGoogle";
                        } else {
                            message = "fail firebaseAuthWithGoogle";
                        }
                        mGoogleSignInTextView.setText(message);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mGoogleSignInTextView.setText(e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void initFBFacebookLogIn() {
        mFacebookCallbackManager = CallbackManager.Factory.create();
        mFacebookLoginButton.setFragment(this);
        mFacebookLoginButton.setReadPermissions("email", "public_profile");
        mFacebookLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mFaceBookLogInTextView.setText("onSuccess");
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                mFaceBookLogInTextView.setText("onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                mFaceBookLogInTextView.setText("error : " + error.getMessage());
            }

        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(AuthenticationFragment.this.getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String message;
                        if (task.isSuccessful()) {
                            message = "success firebaseAuthWithFacebook";
                        } else {
                            message = "fail firebaseAuthWithFacebook";
                        }
                        mFaceBookLogInTextView.setText(message);
                    }
                });
    }

}
