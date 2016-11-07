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
package com.dspot.declex.example.expenses.model;

import android.content.Context;

import com.activeandroid.annotation.Column;
import com.dspot.declex.example.expenses.Config;
import com.dspot.declex.api.localdb.LocalDBModel;
import com.dspot.declex.api.localdb.LocalDBTransaction;
import com.dspot.declex.api.model.AfterLoad;
import com.dspot.declex.api.model.AfterPut;
import com.dspot.declex.api.model.Model;
import com.dspot.declex.api.model.UseModel;
import com.dspot.declex.api.server.SerializeCondition;
import com.dspot.declex.api.server.ServerModel;
import com.dspot.declex.api.server.ServerRequest;
import com.dspot.declex.api.util.annotation.CopyIgnore;
import com.dspot.declex.event.UpdateUIEvent_;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.List;

/**
 * Created by Adrián Rivero.
 */

@LocalDBModel( defaultQuery = "current=1")

@ServerModel(
    baseUrl= Config.SERVER,
    getHeaders = "Authorization=Bearer {token.getAccessToken()}",

    load = {
        @ServerRequest(
            name = "list",
            action = "user"
        ),
        @ServerRequest(
            name = "read",
            action = "user/{remote_id}"
        )
    },

    put = {
        @ServerRequest(
            name = "create",
            method = ServerRequest.RequestMethod.Post,
            action = "user",
            model = "this"
        ),
        @ServerRequest(
            name = "update",
            method = ServerRequest.RequestMethod.Put,
            action = "user/{remote_id}",
            model = "this"
        ),
        @ServerRequest(
            name = "delete",
            action = "user/{remote_id}",
            method = ServerRequest.RequestMethod.Delete
        )
    }
)

@EBean
@UseModel
public class User extends BaseModel {
    @Model
    static Token_ token;

    @Model(lazy = true)
    static User_ currentUser;

    @RootContext
    transient Context context;

    @NotEmpty
    @Column String name;

    @Email
    @NotEmpty
    @Column String email;

    @Column String image;

    @Password(scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE)
    @Column String password;

    @ConfirmPassword
    String confirmedPassword;

    @CopyIgnore
    @SerializeCondition("false")
    @Column boolean current;

    @AfterLoad
    @ServerModel
    @LocalDBTransaction
    void loadedFromServer(List<User_> models) {
        if (models != null) {
            for (User_ user : models) {
                if (user.getRemoteId() == User_.getCurrentUser(context).getRemoteId()) {
                    user.setCurrent(true);
                }
                user.save();
            }

            //Update the UI with the new data
            UpdateUIEvent_.post();
        }
    }

    @AfterPut
    @ServerModel
    void modelUpdated() {
        //Update the UI with the new data
        UpdateUIEvent_.post();
    }
}