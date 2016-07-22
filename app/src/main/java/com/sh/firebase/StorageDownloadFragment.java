package com.sh.firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

public class StorageDownloadFragment extends Fragment {

	private final String TAG = "StorageDownloadFragment";

	private final String REFERENCE_URL = "gs://fir-test-b6db6.appspot.com";
	private final String SAVE_INSTANCE_KEY = "reference";
	private final String DOWNLOAD_FILE_NAME = "image/firebase-discs.png";

	private FirebaseStorage mFirebaseStorage;
	private StorageReference mStorageReference;
	private StorageReference mDownloadStorageReference;

	private ImageView mImageView;

	public StorageDownloadFragment() {
		// Required empty public constructor
	}

	public static StorageDownloadFragment newInstance() {
		StorageDownloadFragment fragment = new StorageDownloadFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initFBStorage();
		downloadInMemory();
//		downloadToLocalFile();
//		getDownloadUrl();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_storage, container, false);
		mImageView = (ImageView) v.findViewById(R.id.storage_imageview);
		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mDownloadStorageReference != null) {
			outState.putString(SAVE_INSTANCE_KEY, mDownloadStorageReference.toString());
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
				mDownloadStorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

				List<FileDownloadTask> tasks = mDownloadStorageReference.getActiveDownloadTasks();
				if (tasks.size() > 0) {
					FileDownloadTask task = tasks.get(0);

					task.addOnSuccessListener((Executor) this, new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
						@Override
						public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
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
		mDownloadStorageReference = mStorageReference.child(DOWNLOAD_FILE_NAME);
	}

	private void downloadInMemory() {
		final long ONE_MEGABYTE = 1024 * 1024;
		mDownloadStorageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
			@Override
			public void onSuccess(byte[] bytes) {
				Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				mImageView.setImageBitmap(bitmap);
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				e.printStackTrace();
			}
		});
	}

	private void downloadToLocalFile() {
		try {
			File localFile = File.createTempFile("images", "jpg");
			mDownloadStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
				@Override
				public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
					Log.d(TAG, "downloadToLocalFile : " + taskSnapshot.getTotalByteCount());
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					e.printStackTrace();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getDownloadUrl() {
		mDownloadStorageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
			@Override
			public void onSuccess(Uri uri) {
				Log.d(TAG, "getDownloadUrl : " + uri);
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				e.printStackTrace();
			}
		});
	}

}
