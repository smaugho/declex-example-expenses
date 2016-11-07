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
package com.dspot.declex.example.expenses.auth;

import com.activeandroid.query.Delete;
import com.dspot.declex.example.expenses.Config;
import com.dspot.declex.example.expenses.model.Token_;
import com.dspot.declex.example.expenses.model.User_;
import com.dspot.declex.api.model.AfterPut;
import com.dspot.declex.api.model.Model;
import com.dspot.declex.api.model.UseModel;
import com.dspot.declex.api.server.ServerModel;
import com.dspot.declex.api.server.ServerRequest;
import com.google.gson.annotations.SerializedName;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.androidannotations.annotations.EBean;

/**
 * Created by Adrián Rivero.
 */

@ServerModel(
    baseUrl= Config.SERVER,

    put = {
        @ServerRequest(
            name = "login",
            action = "auth/login",
            method = ServerRequest.RequestMethod.Post,
            model = "this"
        ),

        @ServerRequest(
            name = "sign-up",
            action = "auth/sign-up",
            method = ServerRequest.RequestMethod.Post,
            model = "this"
        )
    }
)

@UseModel
@EBean
public class Auth {
    String grant_type = "password";
    String client_id = Config.CLIENT_ID;
    String client_secret = Config.CLIENT_SECRET;

    @NotEmpty
    String name;

    @Email
    @NotEmpty
    String username;

    @Password(scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE)
    String password;

    @ConfirmPassword
    String confirmedPassword;

    int success;
    String message;

    @Model(lazy = true)
    @SerializedName("user")
    User_ loggedUser;

    @Model(lazy = true)
    Token_ token;

    @AfterPut
    @ServerModel
    void saveTokenAndUser() {

        if (success == 1) {

            loggedUser.setCurrent(true);
            loggedUser.save();

            new Delete().from(Token_.class).execute();
            token.save();
        }

    }
}
