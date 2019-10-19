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

import java.util.Calendar;

import model.Task;


public class MainActivity extends AppCompatActivity {
    TodoFragment todoFragment;
    CalendarFragment calendarFragment;
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
                    makeSearchVisible(true);
                    fragment = new TodoFragment(Task.getSavedTasks(getApplicationContext(), MainActivity.this));
                    todoFragment = (TodoFragment)fragment;
                    break;
                }
                case R.id.navigation_calendar: {
                    makeSearchVisible(true);
                    fragment = new CalendarFragment();
                    calendarFragment = (CalendarFragment)fragment;
                    break;
                }
                case R.id.navigation_graphic: {
                    makeSearchVisible(false);
                    fragment = new GraphicFragment();
                    break;
                }
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
        searchItem.getIcon().setTint(getResources().getColor(R.color.design_default_color_on_primary));
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
        cleanSearchBar();
        if (fragment == todoFragment){
            todoFragment = new TodoFragment(Task.getSavedTasks(getApplicationContext(), this));
            fragment = todoFragment;
        }
        else if (fragment == calendarFragment){
            calendarFragment.onResume();
            fragment = calendarFragment;
        }
        loadFragment(fragment);
        super.onResume();

    }

    public void makeSearchVisible(boolean isVisible){
        searchItem.setVisible(isVisible);
        cleanSearchBar();
    }

    @Override
    public void onBackPressed() {
        SearchView searchView = (SearchView) searchItem.getActionView();
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        } else {
            super.onBackPressed();
        }
    }

    private void cleanSearchBar(){
        if (searchItem != null) {
            SearchView searchView = (SearchView) searchItem.getActionView();
            searchView.setIconified(true);
        }
    }

    public TodoFragment getTodoFragment(){
        return todoFragment;
    }

    public void setTodoFragment(TodoFragment fgm){
        todoFragment = fgm;
    }

    public MenuItem getSearchItem() {
        return searchItem;
    }
}