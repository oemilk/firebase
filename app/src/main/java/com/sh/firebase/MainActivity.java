package com.sh.firebase;

import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

	private final String TAG = "MainActivity";

	private Fragment mAnalyticsFragment;
	private Fragment mRemoteConfigFragment;
	private Fragment mRealtimeDatabaseFragment;
	private Fragment mStorgeFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initLayout();
	}

	private void initLayout() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
				this, drawerLayout, toolbar, android.R.string.ok, android.R.string.no);
		drawerLayout.setDrawerListener(actionBarDrawerToggle);
		actionBarDrawerToggle.syncState();

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

		mAnalyticsFragment = AnalyticsFragment.newInstance();
		mRemoteConfigFragment = RemoteConfigFragment.newInstance();
		mRealtimeDatabaseFragment = RealtimeDatabaseFragment.newInstance();
		mStorgeFragment = StorageFragment.newInstance();
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, mAnalyticsFragment).commit();
	}

	@Override
	public void onBackPressed() {
		DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
			drawerLayout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	private NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
			new NavigationView.OnNavigationItemSelectedListener() {
				@Override
				public boolean onNavigationItemSelected(MenuItem item) {
					int id = item.getItemId();
					switch (id) {
						case R.id.nav_analytics:
							getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, mAnalyticsFragment).commit();
							break;
						case R.id.nav_remote_config:
							getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, mRemoteConfigFragment).commit();
							break;
						case R.id.nav_realtime_database:
							getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, mRealtimeDatabaseFragment).commit();
							break;
						case R.id.nav_storage:
							getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, mStorgeFragment).commit();
							break;
					}

					DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
					drawer.closeDrawer(GravityCompat.START);
					return true;
				}
			};


}
