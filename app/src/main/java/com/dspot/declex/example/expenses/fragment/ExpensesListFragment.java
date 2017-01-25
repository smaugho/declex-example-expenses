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
import com.dspot.declex.api.localdb.LocalDBModel;
import com.dspot.declex.api.model.Model;
import com.dspot.declex.api.populator.Populator;
import com.dspot.declex.api.populator.Recollector;
import com.dspot.declex.api.server.ServerModel;
import com.dspot.declex.event.UpdateUIEvent;
import com.dspot.declex.example.expenses.R;
import com.dspot.declex.example.expenses.model.Expense_;
import com.dspot.declex.example.expenses.model.User_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.List;
import java.util.Locale;

import static com.dspot.declex.Action.$AlertDialog;
import static com.dspot.declex.Action.$Animate;
import static com.dspot.declex.Action.$DateDialog;
import static com.dspot.declex.Action.$ExpenseDetailsFragment;
import static com.dspot.declex.Action.$GetModel;
import static com.dspot.declex.Action.$Populate;
import static com.dspot.declex.Action.$PutModel;
import static com.dspot.declex.Action.$TimeDialog;

/**
 * Created by Adrián Rivero.
 */

@OptionsMenu(R.menu.filter)
@EFragment(R.layout.fragment_expense_list)
public class ExpensesListFragment extends Fragment {

    @FragmentArg
    boolean addExpense;

    @Model
    User_ currentUser;

    @Model(orderBy = "list", async = true)
    @ServerModel
    List<Expense_> serverExpenses;

    @Model(orderBy = "date DESC")
    @LocalDBModel
    @Populator
    @UpdateOnEvent(UpdateUIEvent.class)
    List<Expense_> expenses;

    @Model(lazy = true)
    @Populator
    @Recollector
    Expense_ expense;

    @ViewById
    View modalFilterAmount, modalFilterDate, modalEditExpense;

    @ViewById
    TextView txtDialogTitle;

    @ItemClick
    void expenses(Expense_ model) {
        $ExpenseDetailsFragment().info(model);
    }

    @Click
    void deleteExpense() {
        $AlertDialog().title("Are you sure?").message("Are you sure you want to remove this expense?")
            .negativeButton("Cancel").positiveButton("Ok");

        $PutModel(expense).query("delete").orderBy("delete");
    }

    @AfterViews
    void addExpenseInit() {
        if (addExpense) {
            btnAddExpense();
        }
    }

    @Click
    void btnAddExpense() {
        txtDialogTitle.setText(R.string.create_modal_title);

        expense = new Expense_();
        expense.setUserId(currentUser.getRemoteId());
        $Populate(expense);

        $Animate(modalEditExpense, R.anim.dialog_show);
        modalEditExpense.setVisibility(View.VISIBLE);
    }

    @Click
    void editExpense(Expense_ model) {
        txtDialogTitle.setText(R.string.edit_modal_title);

        expense = model;
        $Populate(expense);

        $Animate(modalEditExpense, R.anim.dialog_show);
        modalEditExpense.setVisibility(View.VISIBLE);
    }

    @Click
    void btnSave() {
        hideModals();
        $PutModel(expense).orderBy(expense.getRemoteId() == 0? "create" : "update");
    }

    boolean hideModals() {
        View[] modals = new View[] {modalEditExpense, modalFilterDate, modalFilterAmount};
        for (View modal : modals) {
            if (modal.getVisibility() == View.VISIBLE) {
                hideDialog(modal);
                return true;
            }
        }

        return false;
    }

    @Action
    void hideDialog(final View dialog) {
        $Animate(dialog, R.anim.dialog_hide);
        if ($Animate.Ended) {
            dialog.setVisibility(View.INVISIBLE);
        }
    }

    @Event
    void onBackPressedEvent() {
        if (!hideModals()) {
            getActivity().finish();
        }
    }

    //==============================FILTERS=============================

    @ViewById
    View filter_form;

    @ViewById
    TextView filterDateFrom, filterDateTo, filterAmountMin, filterAmountMax, filterResultFrom, filterResultTo;

    @OptionsItem
    void filter_date() {
        hideModals();
        $Animate(modalFilterDate, R.anim.dialog_show);
        modalFilterDate.setVisibility(View.VISIBLE);
    }

    @OptionsItem
    void filter_amount() {
        hideModals();
        $Animate(modalFilterAmount, R.anim.dialog_show);
        modalFilterAmount.setVisibility(View.VISIBLE);
    }

    @Click
    void btnFilterAmount() {
        hideModals();
        filter_form.setVisibility(View.VISIBLE);

        final String min = filterAmountMin.getText().toString();
        final String max = filterAmountMax.getText().toString();

        filterResultFrom.setText(min);
        filterResultTo.setText(max);

        String query = "";
        if (!min.equals("")) {
            query = "amount >= '" + min + "'";

            if (!max.equals("")) {
                query = query + " AND ";
            }
        }

        if (!max.equals("")) {
            query = query + "amount <= '" + max + "'";
        }

        $GetModel(expenses).query(query);
    }

    @Click
    void btnFilterDate() {
        hideModals();
        filter_form.setVisibility(View.VISIBLE);

        final String from  = filterDateFrom.getText().toString();
        final String to = filterDateTo.getText().toString();

        filterResultFrom.setText(from);
        filterResultTo.setText(to);

        String query = "";
        if (!from.equals("")) {
            query = "date >= '" + from + "'";

            if (!to.equals("")) {
                query = query + " AND ";
            }
        }

        if (!to.equals("")) {
            query = query + "date <= '" + to + "'";
        }

        $GetModel(expenses).query(query);
    }

    @Click
    void closeSearch() {
        filter_form.setVisibility(View.GONE);
        $GetModel(expenses);
    }


    //====================DATE DIALOGS================================

    @Click({R.id.expense_date, R.id.filterDateFrom, R.id.filterDateTo})
    void showDateDialog(View view) {
        TextView textView = (TextView) view;

        $DateDialog();
        int $year = 0, $month = 0, $day = 0;
        textView.setText(String.format(Locale.US, "%04d-%02d-%02d", $year, $month + 1, $day));
    }

    @Click
    void expense_time(TextView expense_time) {
        $TimeDialog();

        int $hour = 0, $minute = 0;
        expense_time.setText(String.format(Locale.US, "%02d:%02d", $hour, $minute));
    }

}
