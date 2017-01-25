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
import com.dspot.declex.api.localdb.LocalDBModel;
import com.dspot.declex.api.model.AfterLoad;
import com.dspot.declex.event.Logout_;

/**
 * Created by Adrián Rivero.
 */

@LocalDBModel( //This will trigger logout when the token expires
	defaultQuery = "access_token IS NOT NULL AND NOT access_token=='' " +
		"AND ({System.currentTimeMillis()}-(expires_in*1000)) < time"
)

public class Token extends Model {

	@Column	String access_token;
	@Column	String token_type;
	@Column int expires_in;

	@Column long time = System.currentTimeMillis();

	@AfterLoad
	void logoutIfExpired() {
		if (access_token == null) {
			Logout_.post();
		}
	}
}
