/*
 * Copyright (C) 2014-2021 SgrAlpha
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package ingress.data.gdpr.models.records;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * @author SgrAlpha
 */
public class StorePurchase {

    public static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final ZonedDateTime time;
    private final String transactionType;
    private final String item;
    private final Integer cmuBalance;
    private final String transactionDescription;

    public StorePurchase(final ZonedDateTime time, final String transactionType, final String item, final Integer cmuBalance, final String transactionDescription) {
        this.time = time;
        this.transactionType = transactionType;
        this.item = item;
        this.cmuBalance = cmuBalance;
        this.transactionDescription = transactionDescription;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getItem() {
        return item;
    }

    public Optional<Integer> getCmuBalance() {
        return Optional.ofNullable(cmuBalance);
    }

    public String getTransactionDescription() {
        return transactionDescription;
    }
}
