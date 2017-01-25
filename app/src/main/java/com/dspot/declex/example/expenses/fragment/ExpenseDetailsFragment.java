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
import android.view.View;
import android.widget.TextView;

import com.dspot.declex.api.action.Action;
import com.dspot.declex.api.eventbus.Event;
import com.dspot.declex.api.eventbus.UpdateOnEvent;
import com.dspot.declex.api.model.Model;
import com.dspot.declex.api.populator.Populator;
import com.dspot.declex.api.populator.Recollector;
import com.dspot.declex.event.UpdateUIEvent;
import com.dspot.declex.example.expenses.R;
import com.dspot.declex.example.expenses.model.Expense_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

import java.util.Locale;

import static com.dspot.declex.Action.$AlertDialog;
import static com.dspot.declex.Action.$Animate;
import static com.dspot.declex.Action.$DateDialog;
import static com.dspot.declex.Action.$ExpensesListFragment;
import static com.dspot.declex.Action.$PutModel;
import static com.dspot.declex.Action.$TimeDialog;

/**
 * Created by Adrián Rivero.
 */

@EFragment(R.layout.fragment_expense_details)
public class ExpenseDetailsFragment extends Fragment {

    @Populator
    @FragmentArg
    @UpdateOnEvent(UpdateUIEvent.class)
    Expense_ info;

    @Model(query = "remote_id={info.getRemoteId()}")
    @Populator
    @Recollector
    Expense_ expense;

    @ViewById
    View modalEditExpense;

    @Click
    void deleteExpense() {
        $AlertDialog().title("Are you sure?").message("Are you sure you want to remove this expense?")
                .negativeButton("Cancel").positiveButton("Ok");

        $PutModel(expense).query("delete").orderBy("delete");
        $ExpensesListFragment();
    }

    @Click
    void editExpense() {
        $Animate(modalEditExpense, R.anim.dialog_show);
        modalEditExpense.setVisibility(View.VISIBLE);
    }

    @Click
    void btnSave() {
        hideDialog();
        $PutModel(expense).orderBy("update");
    }

    @Click
    void expense_date(TextView expense_date) {
        $DateDialog();

        int $year = 0, $month = 0, $day = 0;
        expense_date.setText(String.format(Locale.US, "%04d-%02d-%02d", $year, $month + 1, $day));
    }

    @Click
    void expense_time(TextView expense_time) {
        $TimeDialog();

        int $hour = 0, $minute = 0;
        expense_time.setText(String.format(Locale.US, "%02d:%02d", $hour, $minute));
    }

    @Action
    void hideDialog() {
        $Animate(modalEditExpense, R.anim.dialog_hide);
        if ($Animate.Ended) {
            modalEditExpense.setVisibility(View.INVISIBLE);
        }
    }

    @Event
    void onBackPressedEvent() {
        if (modalEditExpense.getVisibility() == View.VISIBLE) {
            hideDialog();
        } else {
            $ExpensesListFragment();
        }
    }
}
