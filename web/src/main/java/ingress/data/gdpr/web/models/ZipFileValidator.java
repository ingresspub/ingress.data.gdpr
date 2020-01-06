/*
 * Copyright (C) 2014-2020 SgrAlpha
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author SgrAlpha
 */
public class ZipFileValidator implements ConstraintValidator<ZipFileConstraint, MultipartFile> {

    private static Logger LOGGER = LoggerFactory.getLogger(ZipFileValidator.class);

    @Override public boolean isValid(final MultipartFile value, final ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Must pick up a zip file.").addConstraintViolation();
            return false;
        }
        final String filename = value.getOriginalFilename();
        final String contentType = value.getContentType();
        if (
                !"application/zip".equalsIgnoreCase(contentType)
                && !"application/x-zip-compressed".equalsIgnoreCase(contentType)
        ) {
            LOGGER.warn("Unsupported file type for {}: {}", filename, contentType);
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format("%s(%s) is not a zip file.", filename, contentType)).addConstraintViolation();
            return false;
        }
        return true;
    }
}
