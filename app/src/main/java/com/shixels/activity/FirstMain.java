package com.shixels.activity;

import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.shixels.R;
import com.shixels.activity.Game.GameType;
import com.shixels.activity.Game.Tickets;
import com.shixels.activity.profile.Help;
import com.shixels.activity.profile.QrScan;
import com.shixels.activity.profile.SettingProfile;
import com.shixels.activity.profile.TicketsHistory;

import mehdi.sakout.fancybuttons.FancyButton;


public class FirstMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FancyButton menuoprn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.first_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        menuoprn = (FancyButton) findViewById(R.id.menuopen);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment frag = new GameType();
        openFragment(frag,R.id.container,"gameType");
        menuoprn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void openDrawer(boolean status){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
          if(status){
              if(!drawer.isDrawerOpen(GravityCompat.START)){
                 drawer.openDrawer(GravityCompat.START);
              }
          }
        else {
              if(drawer.isDrawerOpen(GravityCompat.START));
              drawer.closeDrawer(GravityCompat.START);
          }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.myaccount) {
            Fragment frag = new SettingProfile();
            openFragment(frag,R.id.container,"settings");

        } else if (id == R.id.menugames) {
            Fragment frag = new GameType();
            openFragment(frag,R.id.container,"GameType");

        } else if (id == R.id.tickethistory) {
            Fragment frag = new TicketsHistory();
            openFragment(frag,R.id.container,"TicketHistory");
        } /*else if (id == R.id.menu_qr_code) {
            Fragment frag = new QrScan();
            openFragment(frag,R.id.container,"QrScan");
        } */else if (id == R.id.help) {
            Fragment frag = new Help();
            openFragment(frag,R.id.container,"Help");
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openFragment(Fragment frag, int containerId, String stack){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(stack);
        ft.replace(containerId,frag);
        ft.commit();
    }


    //Todo Delete this
    private void getScreensize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i("Width", width + "px");
        Log.i("heigh", height + "px");

    }
    public void changeBack(){
        menuoprn.setIconResource("\uf053");
        menuoprn.setOnClickListener(null);
        menuoprn.setFontIconSize(30);
        menuoprn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment2(new GameType(),R.id.container);
            }
        });
    }
    public void resetMenu(){
        menuoprn.setIconResource("\uf0c9");
        menuoprn.setOnClickListener(null);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuoprn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });

    }
    public void openFragment2(Fragment frag, int containerId){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(containerId,frag);
        ft.commit();
    }


}
