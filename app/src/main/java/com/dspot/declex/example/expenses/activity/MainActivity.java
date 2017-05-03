/**
 * Copyright (C) 2016 DSpot Sp. z o.o
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dspot.declex.example.expenses.activity;

import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.activeandroid.query.Delete;
import com.dspot.declex.api.eventbus.Event;
import com.dspot.declex.api.eventbus.UpdateOnEvent;
import com.dspot.declex.api.model.Model;
import com.dspot.declex.api.runwith.RunWith;
import com.dspot.declex.api.viewsinjection.Populate;
import com.dspot.declex.event.UpdateUIEvent;
import com.dspot.declex.example.expenses.R;
import com.dspot.declex.example.expenses.model.Expense_;
import com.dspot.declex.example.expenses.model.User_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static com.dspot.declex.Action.$AlertDialog;
import static com.dspot.declex.Action.$BackPressedEvent;
import static com.dspot.declex.Action.$ExpensesListFragment;
import static com.dspot.declex.Action.$LoginActivity;
import static com.dspot.declex.Action.$Logout;
import static com.dspot.declex.Action.$ProfileFragment;
import static com.dspot.declex.Action.$StatisticsFragment;
import static com.dspot.declex.Action.$Toast;

/**
 * Created by Adrián Rivero.
 */

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    @Model
    @Populate
    @UpdateOnEvent(UpdateUIEvent.class)
    User_ user;

    @ViewById
    NavigationView nav_view;

    @ViewById
    DrawerLayout drawer_layout;

    MenuItem prevCheckedItem = null;

    @RunWith
    $ExpensesListFragment onCreate;

    @AfterViews
    void initMaterial(Toolbar toolbar) {
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar,
                0, 0
        );
        drawer_layout.setDrawerListener(toggle);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        if (prevCheckedItem != null) prevCheckedItem.setChecked(false);
        menuItem.setChecked(true);
        prevCheckedItem = menuItem;

        switch (menuItem.getItemId()) {
            case R.id.nav_expenses_list:
                $ExpensesListFragment();
                break;

            case R.id.nav_expenses_per_week_list:
                $StatisticsFragment();
                break;

            case R.id.nav_new_expense:
                $ExpensesListFragment().addExpense(true);
                break;

            case R.id.nav_profile:
                $ProfileFragment();
                break;

            case R.id.nav_logout:
                $Logout();
                break;
        }

        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Event
    void onLogout(String message) {
        if (message != null) {
            {$Toast(message);}
            logout();
            return;
        }

        $AlertDialog().message("Are you sure you want to log out?")
                      .positiveButton("OK").negativeButton("Cancel");
        logout();
    }

    void logout() {
        new Delete().from(User_.class).execute();
        new Delete().from(Expense_.class).execute();

        $LoginActivity();
        finish();
    }

    @Override
    public void onBackPressed() {
        $BackPressedEvent();
    }

}
