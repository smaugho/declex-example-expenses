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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Adrián Rivero.
 */

@LocalDBModel(
    table = "expenses",
    defaultQuery = "SELECT Id, date, strftime('%W', date) as week, SUM(amount) AS total "
        + "FROM expenses GROUP BY week",
    hasTable = false
)
public class WeekExpense extends Model {

    @Column String date;
    @Column String week;
    @Column float total;

    public String average() {
        return String.format(Locale.US, "%.2f", total/7);
    }

    public String range() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        Date date;
        try {
            date = sdf.parse(this.date);
        } catch (ParseException e) {
            date = new Date();
        }

        sdf = new SimpleDateFormat("MMM dd", Locale.US);

        Calendar cal = new GregorianCalendar();
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.setTime(date);
        cal.set(Calendar.WEEK_OF_YEAR, Integer.valueOf(week));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        String startDate = sdf.format(cal.getTime());

        cal.add(Calendar.DAY_OF_YEAR, 6);
        String endDate = sdf.format(cal.getTime());

        return startDate + " - " + endDate;
    }
}
