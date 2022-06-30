package com.u2h.user.united2healandroid;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
Fragment fragmentCurrent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
DrawerLayout drawer=(DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //Display search menu as default page for now
        DisplayFrame(0);
    }

    @Override
    public void onBackPressed() {
        BoxStatsBoxes mainFragment= (BoxStatsBoxes)getSupportFragmentManager().findFragmentByTag("mainFrag");
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {

                    super.onBackPressed();


            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {

        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
       // if (item != null && item.getItemId() == ID_HOME && mDrawerIndicatorEnabled) {



            //noinspection SimplifiableIfStatement

            return super.onOptionsItemSelected(item);

    }
    //Handles main navigation between fragments
    
    //For password
    public void DisplayFrame(int id)
    {
        ((UserInfo)getApplication()).allowAsync=false;
        Fragment fragment = null;
        //Create fragment based off of id of clicked navigation item
        switch(id){
            case 0:
                fragment=new ChooseSchool();
                getSupportActionBar().setTitle("Choose School");
                break;
            case R.id.nav_search_items:
                fragment=new SearchItems();
                getSupportActionBar().setTitle("Search Items");
                break;
            case R.id.nav_export_excel:
                fragment=new ExportExcel();
                getSupportActionBar().setTitle("Export Excel");
                break;

            case R.id.nav_add_item_page:
                fragment=new AddItemPage();
                getSupportActionBar().setTitle("Add Item");
                break;
            case R.id.nav_box_stats:
                fragment=new BoxStatsMain();
                getSupportActionBar().setTitle("Categories");
                break;
//            case R.id.nav_logout:
//              fragment=new ChooseSchool();
//              getSupportActionBar().setTitle("Choose School");
//              break;
        }

        if(fragment != null)
        {
            fragmentCurrent=fragment;
            FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
            //Replace main frame with newly selected fragment
            fragmentTransaction.replace(R.id.MainFrame,fragment);
            fragmentTransaction.commit();
        }
        //Close nav menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        getSupportFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        DisplayFrame(item.getItemId());
        return true;
    }
}
