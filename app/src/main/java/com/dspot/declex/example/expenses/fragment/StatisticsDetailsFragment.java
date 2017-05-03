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
package com.dspot.declex.example.expenses.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dspot.declex.api.eventbus.Event;
import com.dspot.declex.api.model.Model;
import com.dspot.declex.api.viewsinjection.Populate;
import com.dspot.declex.example.expenses.R;
import com.dspot.declex.example.expenses.model.Expense_;
import com.dspot.declex.example.expenses.model.WeekExpense_;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;

import java.util.List;

import static com.dspot.declex.Action.$AlertDialog;
import static com.dspot.declex.Action.$StatisticsFragment;

/**
 * Created by Adrián Rivero.
 */

@EFragment(R.layout.fragment_statistics_week_details)
public class StatisticsDetailsFragment extends Fragment {

    @FragmentArg
    @Populate
    WeekExpense_ weekExpense;

    @Model(query = "strftime('%W', date) = '{weekExpense.getWeek()}'", orderBy = "date DESC")
    @Populate
    List<Expense_> expenses;

    @Event
    void onBackPressedEvent() {
        $StatisticsFragment();
    }
}
