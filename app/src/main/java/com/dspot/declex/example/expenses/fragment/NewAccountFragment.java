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

import com.dspot.declex.api.eventbus.Event;
import com.dspot.declex.api.model.Model;
import com.dspot.declex.api.viewsinjection.Recollect;
import com.dspot.declex.example.expenses.R;
import com.dspot.declex.example.expenses.auth.Auth_;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;

import static com.dspot.declex.Action.$LoginFragment;
import static com.dspot.declex.Action.$MainActivity;
import static com.dspot.declex.Action.$ProgressDialog;
import static com.dspot.declex.Action.$PutModel;
import static com.dspot.declex.Action.$Toast;

/**
 * Created by Adrián Rivero.
 */

@EFragment(R.layout.fragment_new_account)
public class NewAccountFragment extends Fragment {

    @Model(orderBy = "sign-up")
    @Recollect(validate = true)
    Auth_ signUp;

    @Click
    void btnSignUp() {
        Dialog progressDialog = $ProgressDialog().message("Creating Account...").dialog();
        progressDialog.setCanceledOnTouchOutside(false);

        $PutModel(signUp);
        if ($PutModel.Failed) {
            progressDialog.dismiss();
            $Toast("An error occurred");
        }

        progressDialog.dismiss();

        if (signUp.getSuccess() == 1) {
            $MainActivity();
            getActivity().finish();
        } else {
            $Toast(signUp.getMessage());
        }
    }

    @Event
    void  onBackPressedEvent() {
        $LoginFragment();
    }
}
