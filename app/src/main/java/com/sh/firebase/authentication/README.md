
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