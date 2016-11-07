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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.dspot.declex.example.expenses.R;
import com.dspot.declex.example.expenses.model.User;
import com.dspot.declex.api.eventbus.Event;
import com.dspot.declex.api.eventbus.UseEventBus;
import com.dspot.declex.api.model.Model;
import com.dspot.declex.api.populator.Populator;
import com.dspot.declex.api.populator.Recollector;
import com.dspot.declex.example.expenses.util.BaseAnimationListener;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.AnimationRes;

import static com.dspot.declex.Action.$PutModel;

/**
 * Created by Adrián Rivero.
 */

@UseEventBus
@EFragment(R.layout.fragment_profile)
public class ProfileFragment extends Fragment {

    @Model
    @Populator
    @Recollector(validate = true)
    User user;

    @AnimationRes
    Animation dialog_show, dialog_hide;

    @ViewById
    View modalChangePassword;

    @Click
    void btnChangePass() {
        modalChangePassword.startAnimation(dialog_show);
        modalChangePassword.setVisibility(View.VISIBLE);
    }

    @Click
    void btnSavePassword() {
        $PutModel(user).orderBy("update");
        hideDialog();
    }

    @Click
    void btnSave() {
        $PutModel(user).orderBy("update");
    }

    void hideDialog() {
        dialog_hide.setAnimationListener(new BaseAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                modalChangePassword.setVisibility(View.INVISIBLE);
            }
        });
        modalChangePassword.startAnimation(dialog_hide);
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
