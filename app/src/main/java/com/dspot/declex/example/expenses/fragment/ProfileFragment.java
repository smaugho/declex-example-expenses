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

import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dspot.declex.api.eventbus.Event;
import com.dspot.declex.api.model.Model;
import com.dspot.declex.api.viewsinjection.Populate;
import com.dspot.declex.api.viewsinjection.Recollect;
import com.dspot.declex.example.expenses.R;
import com.dspot.declex.example.expenses.model.User;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import static com.dspot.declex.Action.$Animate;
import static com.dspot.declex.Action.$ProgressDialog;
import static com.dspot.declex.Action.$PutModel;
import static com.dspot.declex.Action.$Toast;

/**
 * Created by Adrián Rivero.
 */

@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends Fragment {

    @Model
    @Populate
    @Recollect(validate = true)
    User user;

    @ViewById
    View modalChangePassword;

    @Click
    void btnChangePass() {
        modalChangePassword.setVisibility(View.VISIBLE);
        $Animate(modalChangePassword, R.anim.dialog_show);
    }

    @Click
    void btnSavePassword() {
        Dialog progressDialog = $ProgressDialog().message("Saving...").dialog();
        progressDialog.setCanceledOnTouchOutside(false);

        $PutModel(user).fields("password, confirmedPassword").orderBy("update");
        if ($PutModel.Failed) {
            progressDialog.dismiss();
            $Toast("An error occurred");
        }

        $Toast("Password updated");

        progressDialog.dismiss();
        hideDialog();
    }

    @Click
    void btnSave() {
        Dialog progressDialog = $ProgressDialog().message("Saving...").dialog();
        progressDialog.setCanceledOnTouchOutside(false);

        $PutModel(user).fields("name, email").orderBy("update");
        if ($PutModel.Failed) {
            progressDialog.dismiss();
            $Toast("An error occurred");
        }

        progressDialog.dismiss();
    }

    void hideDialog() {
        $Animate(modalChangePassword, R.anim.dialog_hide);
        modalChangePassword.setVisibility(View.INVISIBLE);
    }

    @Event
    void onBackPressedEvent() {
        if (modalChangePassword.getVisibility() == View.VISIBLE) {
            hideDialog();
        } else {
            getActivity().finish();
        }
    }
}
