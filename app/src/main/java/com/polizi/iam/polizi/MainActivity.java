package com.polizi.iam.polizi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.polizi.iam.polizi.adapters.FragmentPageAdapter;
import com.polizi.iam.polizi.coordinators.OnFragmentInteractionListener;
import com.polizi.iam.polizi.coordinators.OnLoginListener;
import com.polizi.iam.polizi.models.PoliziUser;
import com.polizi.iam.polizi.service.LocationService;
import com.polizi.iam.polizi.settings.SettingsActivity;

import static com.parse.ParseInstallation.getCurrentInstallation;
import static com.polizi.iam.polizi.R.id.fab;

public class MainActivity extends AppCompatActivity
        implements OnFragmentInteractionListener, OnLoginListener {

    private FloatingActionButton mCreateUserFAB;
    private ViewPager mViewPager;
    private View mNavHeaderView;
    private NavigationView mNavigationView;
    private boolean isLoggedIn;
    private Menu mMenu;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Fragment Loader for
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new FragmentPageAdapter(getSupportFragmentManager(),
                MainActivity.this));


        mCreateUserFAB = (FloatingActionButton) findViewById(fab);
        mCreateUserFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFragmentInteraction(1);
                mCreateUserFAB.hide();
            }
        });
        mHandler = new Handler();


        onLoginUpdate();


        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }


    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() > 0) {
            if (isLoggedIn)
                onFragmentInteraction(3);
            else
                onFragmentInteraction(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mMenu = menu;
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_log_out) {
            PoliziUser.logOutInBackground(
                    new LogOutCallback() {
                        @Override
                        public void done(ParseException e) {
                            //clear the installation backend

                            mMenu.findItem(R.id.action_log_out).setVisible(false);
                            onFragmentInteraction(0);
                            onLoginUpdate();
                        }
                    }
            );
        if(id == R.id.settings_menu){
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onLogoutParse() {
        getCurrentInstallation().put("isLoggedIn",false);
    }

    @Override
    public void onFragmentInteraction(int pageNumber) {
        if (pageNumber == 0)
            mCreateUserFAB.show();
        else
            mCreateUserFAB.hide();
        mViewPager.setCurrentItem(pageNumber, true);
    }

    @Override
    public void onLoginUpdate() {

        Log.d("Main", "here reached");
        ParseUser parseUser = PoliziUser.getCurrentUser();
        if (parseUser == null) {
            Runnable menuItem = new Runnable() {
                @Override
                public void run() {
                    stopService(new Intent(getApplicationContext(), LocationService.class));
                    if(mMenu!=null) {
                        MenuItem item = mMenu.findItem(R.id.action_log_out);
                        if (item != null)
                            item.setVisible(false);
                        else
                            mHandler.postDelayed(this, 500);
                    }else{
                        mHandler.postDelayed(this, 500);
                    }
                }
            };
            mHandler.postDelayed(menuItem, 1000);
            isLoggedIn = false;
            ParseInstallation installation =ParseInstallation.getCurrentInstallation();
            installation.put("isLoggedIn",false);
            installation.saveInBackground();
        } else if (parseUser instanceof PoliziUser) {
            ParseInstallation installation =ParseInstallation.getCurrentInstallation();
            installation.put("isLoggedIn",true);
            installation.saveInBackground();
            isLoggedIn = true;
            onFragmentInteraction(2);
            Runnable menuItem = new Runnable() {
                @Override
                public void run() {
                    startService(new Intent(getApplicationContext(), LocationService.class));
                    mMenu.findItem(R.id.action_log_out).setVisible(true);
                }
            };
            mHandler.postDelayed(menuItem, 1000);
            getCurrentInstallation().put("isLoggedIn",true);
        }

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    @Override
    public void onStop() {
        super.onStop();
     }
}
