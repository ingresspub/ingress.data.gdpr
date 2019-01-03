/*
 * Copyright (C) 2014-2019 SgrAlpha
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
 */

package ingress.data.gdpr.models.records.profile;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * @author SgrAlpha
 */
public class SmsVerification {

    private final boolean verified;
    private final ZonedDateTime verificationTime;

    private SmsVerification(final boolean verified, final ZonedDateTime verificationTime) {
        this.verified = verified;
        this.verificationTime = verificationTime;
    }

    public static SmsVerification verified(final ZonedDateTime time) {
        return new SmsVerification(true, time);
    }

    public static SmsVerification unverified() {
        return new SmsVerification(false, null);
    }

    public boolean isVerified() {
        return verified;
    }

    public Optional<ZonedDateTime> getVerificationTime() {
        return Optional.ofNullable(verificationTime);
    }

}
