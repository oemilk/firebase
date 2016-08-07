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
import com.facebook.login.LoginManager;
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
import com.google.firebase.auth.TwitterAuthProvider;
import com.sh.firebase.R;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import io.fabric.sdk.android.Fabric;

public class AuthenticationFragment extends Fragment {

    private final String TAG = "AuthenticationFragment";

    // for google sign in
    private final String WEB_CLIENT_ID = "928488830433-camll1e451a5i4a0vrart4aia0rq707i.apps.googleusercontent.com";
    private final int GOOGLE_SIGN_IN_REQUEST_CODE = 0;

    // for facebook log in
    private final int FACEBOOK_LOG_IN_REQUEST_CODE = 64206;

    // for twitter log in
    private final String TWITTER_CONSUMER_KEY = "NDPH7edclsp7gK4GVPDPKwKGm";
    private final String TWITTER_CONSUMER_SECRET = "Wjne0Ct1WLipHGdn1q2tX529xG3tEyss1RVFjl7dD9HY6e7z1m";
    private final int TWITTER_LOG_IN_REQUEST_CODE = 140;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private GoogleApiClient mGoogleApiClient; // for google sign in
    private CallbackManager mFacebookCallbackManager; // for facebook log in

    private TextView mPasswordTextView;
    private TextView mGoogleSignInTextView;
    private TextView mFaceBookLogInTextView;
    private TextView mTwitterLogInTextView;
    private TextView mAuthStateTextview;

    private LoginButton mFacebookLoginButton;
    private TwitterLoginButton mTwitterLoginButton;

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
        initFBGoogleSignIn();
        initFBTwitterLogIn();
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
        mTwitterLogInTextView = (TextView) v.findViewById(R.id.twitter_login_textview);

        Button pwSignUpButton = (Button) v.findViewById(R.id.pw_sign_up_button);
        Button pwSignInButton = (Button) v.findViewById(R.id.pw_sign_in_button);
        Button pwSignOutButton = (Button) v.findViewById(R.id.pw_sign_out_button);
        Button googlesignInButton = (Button) v.findViewById(R.id.google_sign_in_button);
        Button googlesignOutButton = (Button) v.findViewById(R.id.google_sign_out_button);
        Button facebookLogOutButton = (Button) v.findViewById(R.id.facebook_logout_button);
        Button twitterLogOutButton = (Button) v.findViewById(R.id.twitter_logout_button);

        mFacebookLoginButton = (LoginButton) v.findViewById(R.id.facebook_login_button);
        mTwitterLoginButton = (TwitterLoginButton) v.findViewById(R.id.twitter_login_button);
        initFBFacebookLogIn();
        setTwitterLogInCallback();

        pwSignUpButton.setOnClickListener(mOnClickListener);
        pwSignInButton.setOnClickListener(mOnClickListener);
        pwSignOutButton.setOnClickListener(mOnClickListener);
        googlesignInButton.setOnClickListener(mOnClickListener);
        googlesignOutButton.setOnClickListener(mOnClickListener);
        facebookLogOutButton.setOnClickListener(mOnClickListener);
        twitterLogOutButton.setOnClickListener(mOnClickListener);
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
        } else if (requestCode == TWITTER_LOG_IN_REQUEST_CODE) {
            mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
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
                case R.id.facebook_logout_button:
                    logOutWithFacebook();
                    break;
                case R.id.twitter_logout_button:
                    logOutWithTwitter();
                    break;
            }
        }
    };

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

    // [START auth_with_email_and_password]
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
    // [END auth_with_email_and_password]

    // [START auth_with_google]
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
    // [END auth_with_google]

    // [START auth_with_facebook]
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

    private void logOutWithFacebook() {
        if (mFirebaseAuth != null) {
            LoginManager.getInstance().logOut();
            signOut();
            mFaceBookLogInTextView.setText("facebook log out");
        }
    }
    // [END auth_with_facebook]

    // [START auth_with_twitter]
    private void initFBTwitterLogIn() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
        Fabric.with(AuthenticationFragment.this.getContext(), new Twitter(authConfig));
    }

    private void setTwitterLogInCallback() {
        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                mTwitterLogInTextView.setText(exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    private void handleTwitterSession(TwitterSession session) {
        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

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
                        mTwitterLogInTextView.setText(message);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mTwitterLogInTextView.setText(e.getMessage());
                e.printStackTrace();
            }
        });
    }

    private void logOutWithTwitter() {
        if (mFirebaseAuth != null) {
            Twitter.logOut();
            signOut();
            mTwitterLogInTextView.setText("twitter log out");
        }
    }
    // [END auth_with_twitter]

}
