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

import com.activeandroid.annotation.Column;
import com.dspot.declex.example.expenses.Config;
import com.dspot.declex.api.localdb.LocalDBModel;
import com.dspot.declex.api.localdb.LocalDBTransaction;
import com.dspot.declex.api.model.AfterLoad;
import com.dspot.declex.api.model.AfterPut;
import com.dspot.declex.api.model.Model;
import com.dspot.declex.api.model.UseModel;
import com.dspot.declex.api.server.ServerModel;
import com.dspot.declex.api.server.ServerRequest;
import com.dspot.declex.event.UpdateUIEvent_;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.androidannotations.annotations.EBean;

import java.util.List;
import java.util.Locale;

/**
 * Created by Adrián Rivero.
 */

@UseModel
@LocalDBModel


@ServerModel(
    baseUrl= Config.SERVER,
    getHeaders = "Authorization=Bearer {token.getAccessToken()}",

    load = {
        @ServerRequest(
            name = "list",
            action = "expense"
        ),
        @ServerRequest(
            name = "read",
            action = "expense/{remote_id}"
        )
    },

    put = {
        @ServerRequest(
            name = "create",
            action = "expense",
            method = ServerRequest.RequestMethod.Post,
            model = "this"
        ),
        @ServerRequest(
            name = "update",
            action = "expense/{remote_id}",
            method = ServerRequest.RequestMethod.Put,
            model = "this"
        ),
        @ServerRequest(
            name = "delete",
            action = "expense/{remote_id}",
            method = ServerRequest.RequestMethod.Delete
        )
    }
)

@EBean
public class Expense extends BaseModel {
    @Model
    public static Token_ token;

    @NotEmpty
    @Column String description = "";

    @Column String comment = "";

    @Column float amount;

    @Column String date = "";
    @Column String time = "";

    @Column long user_id;

    public String getAmount() {
        if (amount == 0) return "";

        return String.format(Locale.US, "%.2f", amount);
    }

    public String datetime() {
        return date + " " + time;
    }

    @AfterLoad
    @ServerModel
    @LocalDBTransaction
    void loadedFromServer(List<Expense_> models) {
        if (models != null) {
            for (Expense_ expense : models) {
                expense.save();
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