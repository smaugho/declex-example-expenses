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

import android.support.v7.app.AppCompatActivity;

import com.dspot.declex.example.expenses.R;
import com.dspot.declex.example.expenses.model.User_;
import com.dspot.declex.api.action.Action;
import com.dspot.declex.api.model.Model;
import com.dspot.declex.event.BackPressedEvent_;

import org.androidannotations.annotations.EActivity;

import static com.dspot.declex.Action.$LoginFragment;
import static com.dspot.declex.Action.$MainActivity;

/**
 * Created by Adrián Rivero.
 */

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @Model
    User_ currentUser;

    @Action
    void onCreate() {
        if (currentUser.exists()) {
            $MainActivity();
            finish();
        } else {
            $LoginFragment();
        }
    }

    @Override
    public void onBackPressed() {
        BackPressedEvent_.post();
    }
}
