package com.example.todot;


import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import model.Task;


public class MainActivity extends AppCompatActivity {
    TodoFragment todoFragment;
    Fragment fragment = null;
    MenuItem searchItem;
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_todo: {
                    searchItem.setVisible(true);
                    fragment = new TodoFragment(Task.getSavedTasks(getApplicationContext(), MainActivity.this));
                    todoFragment = (TodoFragment)fragment;
                    break;
                }
                case R.id.navigation_calendar:
                    searchItem.setVisible(true);
                    fragment = new CalendarFragment();
                    break;
                case R.id.navigation_graphic:
                    searchItem.setVisible(false);
                    fragment = new GraphicFragment();
                    break;
            }
            return loadFragment(fragment);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);


        //loading the default fragment
        loadFragment(new TodoFragment(Task.getSavedTasks(getApplicationContext(), this)));

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_action, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchItem = menu.findItem(R.id.app_bar_search);
        searchItem.getIcon().setTint(Color.WHITE);
        SearchView searchView = (SearchView) searchItem.getActionView();



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                todoFragment.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                todoFragment.filter(newText);
                return false;
            }
        });

        //set search colors
        TextView textView = (TextView) searchView.findViewById(R.id.search_src_text);
        ImageView searchIcon = searchView.findViewById(R.id.search_button);
        ImageView closeIcon = searchView.findViewById(R.id.search_close_btn);
        View v = searchView.findViewById(R.id.search_plate);
        v.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        searchIcon.setColorFilter(getResources().getColor(R.color.design_default_color_on_primary));
        closeIcon.setColorFilter(getResources().getColor(R.color.design_default_color_secondary));
        textView.setTextColor(getResources().getColor(R.color.design_default_color_on_primary));
        textView.setHintTextColor(getResources().getColor(R.color.design_default_color_secondary));
        textView.setHint("Search Name +List @Tag (P)");

        return true;
    }

    @Override
    public void onResume(){
        //loading the default fragment
        if (fragment == todoFragment){
            todoFragment = new TodoFragment(Task.getSavedTasks(getApplicationContext(), this));
            fragment = todoFragment;
        }
        loadFragment(fragment);
        super.onResume();

    }


}