
Authentication
====================

## Add the SDK

- Add the SDK ([build.gradle], [app/build.gradle])

```build.gradle
buildscript {
    // ...
    dependencies {
        // ...
        classpath 'com.google.gms:google-services:3.0.0'
    }
}
// ...
```

```app/build.gradle
apply plugin: 'com.android.application'

dependencies {
    compile 'com.google.firebase:firebase-auth:9.4.0' // for auth
    compile 'com.google.android.gms:play-services-auth:9.4.0' // google sign-in
    compile 'com.facebook.android:facebook-android-sdk:4.+' // facebook login
    compile('com.twitter.sdk.android:twitter-core:1.7.0@aar') { // twitter login
        transitive = true;
    }
    compile('com.twitter.sdk.android:twitter:1.7.0@aar') {
        transitive = true;
    }
}

apply plugin: 'com.google.gms.google-services'
```

## Email/Password Authentication

#### Create FirebaseAuth object and AuthStateListener object.

```FirebaseAuth_Object
mFirebaseAuth= FirebaseAuth.getInstance();
```
```AuthStateListener_Object
private FirebaseAuth.AuthStateListener mAuthStateListener;

// ...

private void initFBAuthState() {
    mAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            String message;
            if (firebaseUser != null) {
                // User is signed in
            } else {
                // User is signed out
            }
        }
    };
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
```

#### Sign up

- The email address has to be correctly formatted.
- Password should be at least 6 characters.

```Sign_up
private void createUserWithEmailAndPassword() {
    String email = "oemilk@naver.com"; // email address format
    String password = "123456"; // at least 6 characters
    mFirebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String message;
                    if (task.isSuccessful()) {
                        // success
                    } else {
                        // fail
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            // exception
        }
    });
}
```

#### Sign in

```Sign_in
private void signInWithEmailAndPassword() {
    String email = "test@test.com";
    String password = "123456";
    mFirebaseAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    String message;
                    if (task.isSuccessful()) {
                        // success
                    } else {
                        // fail
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            // exception
        }
    });
}
```

#### Sign out

```Sign_out
private void signOut() {
    if (mFirebaseAuth != null) {
        mFirebaseAuth.signOut();
    }
}
```

## Google Sign-In Authentication

#### Create GoogleApiClient object

```GoogleApiClient_Object

private final String WEB_CLIENT_ID = "928488830433-camll1e451a5i4a0vrart4aia0rq707i.apps.googleusercontent.com";

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
```

#### Sign in

```Sign_in
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
                        if (task.isSuccessful()) {
                            // success
                        } else {
                            // fail
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // fail
            }
        });
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
                // fail
            }
        }
    }
```

#### Sign out

```Sign_out
private void signOut() {
    if (mFirebaseAuth != null) {
        mFirebaseAuth.signOut();
    }
}
```

## Facebook Login Authentication

#### Facebook Login button
```Login_button
<com.facebook.login.widget.LoginButton
    android:id="@+id/facebook_login_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal" />
```

#### Initialize Facebook SDK

- Initialize Facebook SDK on onCreate.

```Initialize_Facebook_SDK
@Override
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   FacebookSdk.sdkInitialize(getApplicationContext()); // for facebook log in
   setContentView(R.layout.activity_main);

   initLayout();
}
```

#### Sign in

```Sign_in
private void initFBFacebookLogIn() {
    mFacebookCallbackManager = CallbackManager.Factory.create();
    mFacebookLoginButton.setFragment(this);
    mFacebookLoginButton.setReadPermissions("email", "public_profile");
    mFacebookLoginButton.registerCallback(mFacebookCallbackManager, new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            mFaceBookLogInTextView.setText("onSuccess");
            // success
        }

        @Override
        public void onCancel() {
            // cancel
        }

        @Override
        public void onError(FacebookException error) {
            // error
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
                        // success
                    } else {
                        // fail
                    }
                }
            });
}

private final int FACEBOOK_LOG_IN_REQUEST_CODE = 64206;

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == FACEBOOK_LOG_IN_REQUEST_CODE) {
        mFacebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
```

#### Sign out

```Sign_out
private void logOutWithFacebook() {
    if (mFirebaseAuth != null) {
        LoginManager.getInstance().logOut();
        signOut();
    }
}

private void signOut() {
    if (mFirebaseAuth != null) {
        mFirebaseAuth.signOut();
    }
}
```

## Twitter Login Authentication

#### Twitter Login button
```Login_button
<com.twitter.sdk.android.core.identity.TwitterLoginButton
    android:id="@+id/twitter_login_button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_gravity="center_horizontal"
    android:layout_centerInParent="true"/>
```

#### Initialize Twitter


```Initialize_Twitter
private final String TWITTER_CONSUMER_KEY = "NDPH7edclsp7gK4GVPDPKwKGm";
private final String TWITTER_CONSUMER_SECRET = "Wjne0Ct1WLipHGdn1q2tX529xG3tEyss1RVFjl7dD9HY6e7z1m";

private void initFBTwitterLogIn() {
    TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
    Fabric.with(AuthenticationFragment.this.getContext(), new Twitter(authConfig));
}
```

#### Sign in

```Sign_in
private void setTwitterLogInCallback() {
    mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> result) {
            // success
        }

        @Override
        public void failure(TwitterException exception) {
            // fail
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
                        // success
                    } else {
                        // fail
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
              // error
        }
    });
}

```

#### Sign out

```Sign_out
private void logOutWithTwitter() {
    if (mFirebaseAuth != null) {
        Twitter.logOut();
        signOut();
    }
}

private void signOut() {
    if (mFirebaseAuth != null) {
        mFirebaseAuth.signOut();
    }
}
```

## Github Login Authentication

#### Sign in

```Sign_in
private final String GITHUB_TOKEN = "4dfd90fb0ac9ef75c4aabcc87ee0cdd4062a921c";

private void initFBGithubLogIn() {
    AuthCredential credential = GithubAuthProvider.getCredential(GITHUB_TOKEN);
    mFirebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(AuthenticationFragment.this.getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // success
                    } else {
                        // fail
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
              // fail
        }
    });
}
```

#### Sign out

```Sign_out
private void signOut() {
    if (mFirebaseAuth != null) {
        mFirebaseAuth.signOut();
    }
}
```
