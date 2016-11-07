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

import android.support.v4.app.Fragment;

import com.dspot.declex.example.expenses.R;
import com.dspot.declex.example.expenses.model.WeekExpense;
import com.dspot.declex.example.expenses.model.WeekExpense_;
import com.dspot.declex.api.eventbus.Event;
import com.dspot.declex.api.eventbus.UseEventBus;
import com.dspot.declex.api.model.Model;
import com.dspot.declex.api.populator.Populator;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;

import java.util.List;

import static com.dspot.declex.Action.$StatisticsDetailsFragment;

/**
 * Created by Adrián Rivero.
 */

@UseEventBus
@EFragment(R.layout.fragment_statistics)
public class StatisticsFragment extends Fragment {

    @Model
    @Populator
    List<WeekExpense> weekExpenses;

    @ItemClick
    void weekExpenses(WeekExpense_ model) {
        $StatisticsDetailsFragment().weekExpense(model);
    }

    @Event
    void onBackPressedEvent() {
        getActivity().finish();
    }
}