/*
 * Copyright (C) 2014-2018 SgrAlpha
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

package ingress.data.gdpr.web.utils.zip;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class ZipFileExtractor {

    private static final String TEMP_DIR_PREFIX = "ingress-data-";

    public static Stream<Path> extract(final File uploaded, final String password) throws IOException, ZipException {
        ZipFile zipFile = new ZipFile(uploaded);
        zipFile.setPassword(password);
        Path unzipToDir = Files.createTempDirectory(TEMP_DIR_PREFIX);
        zipFile.extractAll(unzipToDir.toFile().getAbsolutePath());
        return Files.list(unzipToDir);
    }

}
