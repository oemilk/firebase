package com.sh.firebase;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Executor;

public class StorageUploadFragment extends Fragment {

	private final String TAG = "StorageUploadFragment";

	private final String REFERENCE_URL = "gs://fir-test-b6db6.appspot.com";
	private final String SAVE_INSTANCE_KEY = "reference";
	private final String UPLOAD_FILE_NAME = "upload.jpg";

	private FirebaseStorage mFirebaseStorage;
	private StorageReference mStorageReference;
	private StorageReference mUploadStorageReference;

	private ImageView mImageView;

	public StorageUploadFragment() {
		// Required empty public constructor
	}

	public static StorageUploadFragment newInstance() {
		StorageUploadFragment fragment = new StorageUploadFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initFBStorage();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_storage, container, false);
		mImageView = (ImageView) v.findViewById(R.id.storage_imageview);
		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				uploadDataInMemory();
//				uploadStream();
//				uploadLocalFile();
			}
		});
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mUploadStorageReference != null) {
			outState.putString(SAVE_INSTANCE_KEY, mUploadStorageReference.toString());
		}
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState == null) {
			return;
		} else {
			final String stringRef = savedInstanceState.getString(SAVE_INSTANCE_KEY);
			if (stringRef == null) {
				return;
			} else {
				mUploadStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

				List<UploadTask> tasks = mUploadStorageReference.getActiveUploadTasks();
				if (tasks.size() > 0) {
					UploadTask task = tasks.get(0);

					task.addOnSuccessListener((Executor) this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
						@Override
						public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
							// taskSnapshot
						}
					});
				}
			}
		}
	}

	private void initFBStorage() {
		mFirebaseStorage = FirebaseStorage.getInstance();
		mStorageReference = mFirebaseStorage.getReferenceFromUrl(REFERENCE_URL);
		mUploadStorageReference = mStorageReference.child(UPLOAD_FILE_NAME);
	}

	private void uploadDataInMemory() {
		mImageView.setDrawingCacheEnabled(true);
		mImageView.buildDrawingCache();
		Bitmap bitmap = mImageView.getDrawingCache();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
		byte[] data = byteArrayOutputStream.toByteArray();

		UploadTask uploadTask = mUploadStorageReference.putBytes(data);
		uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
				Log.d(TAG, "uploadDataInMemory : " + taskSnapshot.getMetadata());
			}
		}).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
				double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
				Log.d(TAG, "uploadDataInMemory progress : " + progress);
			}
		}).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
				Log.d(TAG, "uploadDataInMemory pause");
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void uploadStream() {
		try {
			InputStream inputStream = new FileInputStream(new File("image/default.jpg"));
			UploadTask uploadTask = mUploadStorageReference.putStream(inputStream);
			uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
				@Override
				public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
					Log.d(TAG, "uploadStream : " + taskSnapshot.getTotalByteCount());
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					e.printStackTrace();
				}
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void uploadLocalFile() {
		Uri file = Uri.fromFile(new File("image/default.jpg"));

		// Add File Metadata
		StorageMetadata storageMetadata = new StorageMetadata.Builder()
				.setContentType("image/jpg").build();

		UploadTask uploadTask = mUploadStorageReference.putFile(file, storageMetadata);
		uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
				Log.d(TAG, "uploadLocalFile : " + taskSnapshot.getTotalByteCount());
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				e.printStackTrace();
			}
		});
	}

}
