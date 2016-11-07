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

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.dspot.declex.api.extension.Extension;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Adrián Rivero.
 */

@Extension
public class BaseModel extends Model {

    protected static final String ID_FIELD_NAME = "remote_id";

    @Column(name = ID_FIELD_NAME, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    @SerializedName("id") long remote_id;

    @Column String created_at;
    @Column String updated_at;

}