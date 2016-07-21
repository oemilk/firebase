
firebase
====================

This is a project for sample about android firebase remote config.

![alt tag](https://github.com/oemilk/images/blob/master/firebase_screenshot_01.png)

## Add Firebase

- Download a [google-services.json] file.
- Copy this into your project's module folder, typically app/.
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
    // ...
    compile 'com.google.firebase:firebase-core:9.2.1'
    compile 'com.google.firebase:firebase-config:9.2.1' // for remote config
    compile 'com.google.firebase:firebase-database:9.2.1' // for remote database
}

apply plugin: 'com.google.gms.google-services'
```

## Initial Analytics

- Create the singleton Analytics object. [[analytics codes]]

```Analytics_Object
mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());
```

#### Log Events

```Log_evnets
Bundle bundle = new Bundle();
bundle.putString(FirebaseAnalytics.Param.CHARACTER, "test character");
bundle.putLong(FirebaseAnalytics.Param.LEVEL, 10);
mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_UP, bundle);

Bundle bundle = new Bundle();
bundle.putString("custom_id", "custom test id");
bundle.putString("custom_name", "custom test name");
bundle.putString("custom_location_id", "custom test location id");
mFirebaseAnalytics.logEvent("custom_event", bundle);
```

#### Set User Properties

```Set_user_properties
mFirebaseAnalytics.setUserProperty("test_user_property", "test user");
```

## Initial Remote Config

- Create the singleton Remote Config object. [[remote config codes]]

```RemoteConfig_Object
mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
	.setDeveloperModeEnabled(true)
	.build();
mFirebaseRemoteConfig.setConfigSettings(configSettings);
```
- Set in-app default parameter values.

```Default_parameter
private final String FB_REMOTE_CONFIG_STRING = "remote_config_string";
private final String FB_REMOTE_CONFIG_BOOLEAN = "remote_config_boolean";
private final String FB_REMOTE_CONFIG_BYTE_ARRAY = "remote_config_byte_array";
private final String FB_REMOTE_CONFIG_DOUBLE = "remote_config_double";
private final String FB_REMOTE_CONFIG_LONG = "remote_config_long";
private final String FB_REMOTE_CONFIG_COLOR = "remote_config_color";

Map<String, Object> map = new HashMap<>();
  map.put(FB_REMOTE_CONFIG_STRING, "default");
  map.put(FB_REMOTE_CONFIG_BOOLEAN, false);
  map.put(FB_REMOTE_CONFIG_BYTE_ARRAY, new byte[0]);
  map.put(FB_REMOTE_CONFIG_DOUBLE, 0.0);
  map.put(FB_REMOTE_CONFIG_LONG, 555);
  map.put(FB_REMOTE_CONFIG_COLOR, "#000000");
  mFirebaseRemoteConfig.setDefaults(map);
```

#### Fetch

- Fetch and activate values from the server [[fetch codes]]

```Fetch
private final long CACHE_EXPIRATION = 0;

mFirebaseRemoteConfig.fetch(CACHE_EXPIRATION).addOnCompleteListener(new OnCompleteListener<Void>() {
			@Override
			public void onComplete(@NonNull Task<Void> task) {
				if (task.isSuccessful()) {
					mFirebaseRemoteConfig.activateFetched();

					String str = mFirebaseRemoteConfig.getString(FB_REMOTE_CONFIG_STRING);
					boolean b = mFirebaseRemoteConfig.getBoolean(FB_REMOTE_CONFIG_BOOLEAN);
					byte [] arrBytes = mFirebaseRemoteConfig.getByteArray(FB_REMOTE_CONFIG_BYTE_ARRAY);
					double d = mFirebaseRemoteConfig.getDouble(FB_REMOTE_CONFIG_DOUBLE);
					long l = mFirebaseRemoteConfig.getLong(FB_REMOTE_CONFIG_LONG);
					int color = Color.parseColor(mFirebaseRemoteConfig.getString(FB_REMOTE_CONFIG_COLOR));
				} else {
					//
				}
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				L//
			}
```

## Initial Realtime database

- Create the singleton Database object. [[database codes]]
- Create reference the location you want to write to.

```Database_Object
private final String REFERENCE_NAME = "first_reference";

...

mFirebaseDatabase = FirebaseDatabase.getInstance();
mDatabaseReference = mFirebaseDatabase.getReference(REFERENCE_NAME);
```

#### Write to your database

```Write
mDatabaseReference.setValue("Test 1");
```

#### Read from your database

```Read
mDatabaseReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				String value = dataSnapshot.getValue(String.class);
			}

			@Override
			public void onCancelled(DatabaseError error) {
				// error
			}
		});
```

If you have any questions about this project.
Please send an email to "oemilk@naver.com".

[google-services.json]: https://support.google.com/firebase/answer/
[build.gradle]: https://github.com/oemilk/firebase/blob/master/build.gradle
[app/build.gradle]: https://github.com/oemilk/firebase/blob/master/app/build.gradle
[remote config codes]: https://github.com/oemilk/firebase/blob/master/app/src/main/java/com/sh/firebase/RemoteConfigFragment.java#L64-L77
[fetch codes]: https://github.com/oemilk/firebase/blob/master/app/src/main/java/com/sh/firebase/RemoteConfigFragment.java#L80-L115
[analytics codes]: https://github.com/oemilk/firebase/blob/0c0b2ca4283a9867ffdefb0a62c99d39569391be/app/src/main/java/com/sh/firebase/AnalyticsFragment.java#L46-L48
[log events]: https://github.com/oemilk/firebase/blob/0c0b2ca4283a9867ffdefb0a62c99d39569391be/app/src/main/java/com/sh/firebase/AnalyticsFragment.java#L54-L68
[set user properties]: https://github.com/oemilk/firebase/blob/0c0b2ca4283a9867ffdefb0a62c99d39569391be/app/src/main/java/com/sh/firebase/AnalyticsFragment.java#L50-L52
[database codes]: https://github.com/oemilk/firebase/blob/656340563ac64f52c1998e783d78160d18f67ff7/app/src/main/java/com/sh/firebase/RealtimeDatabaseFragment.java#L53-L56
