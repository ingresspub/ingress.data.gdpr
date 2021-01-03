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

package ingress.data.gdpr.web.models;

import static com.google.common.base.Strings.isNullOrEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author SgrAlpha
 */
public class LocaleValidator implements ConstraintValidator<LocaleConstraint, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocaleValidator.class);

    @Override public boolean isValid(final String value, final ConstraintValidatorContext context) {
        if (isNullOrEmpty(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Missing timezone ID.").addConstraintViolation();
            return false;
        }
        try {
            Locale.forLanguageTag(value);
        } catch (Exception e) {
            LOGGER.warn("Unsupported timezone ID: {}", value);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format("Unsupported timezone ID: %s", value)).addConstraintViolation();
            return false;
        }
        return true;
    }
}
