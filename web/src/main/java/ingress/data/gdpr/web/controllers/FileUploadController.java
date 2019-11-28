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

package ingress.data.gdpr.web.controllers;

import ingress.data.gdpr.web.dao.Importer;
import ingress.data.gdpr.web.models.FileUploadForm;
import ingress.data.gdpr.web.utils.ExceptionUtil;
import ingress.data.gdpr.web.utils.zip.ZipFileExtractor;

import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.validation.Valid;

/**
 * @author SgrAlpha
 */
@Controller
public class FileUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

    private final Importer importer;

    public FileUploadController(@Autowired final Importer importer) {
        this.importer = importer;
    }

    @GetMapping("/upload")
    public String showUploadPage(final ModelMap map) {
        map.addAttribute("uploadForm", new FileUploadForm());
        return "upload";
    }

    @PostMapping("/upload")
    public Callable<String> handleFileUpload(
            @Valid @ModelAttribute("uploadForm") final FileUploadForm uploadForm,
            final BindingResult result, final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return () -> "upload";
        }
        return () -> {
            final MultipartFile uploaded = uploadForm.getFile();
            final String unzipPassword = uploadForm.getPassword();
            File tempZipFile = Files.createTempFile("ingress-data-" + uploaded.getOriginalFilename(), null).toFile();
            uploaded.transferTo(tempZipFile);
            final List<Path> files = ZipFileExtractor.extract(tempZipFile, unzipPassword).collect(Collectors.toList());
            importer.parseAndSaveRawDataReport(files);
            if (!tempZipFile.delete()) {
                LOGGER.warn("Unable to clean up temp zip file: {}", tempZipFile);
            }
            files.stream().map(Path::toFile).forEach(file -> {
                if (!file.delete()) {
                    LOGGER.warn("Unable to clean up temp data file: {}", file);
                }
            });
            redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + uploaded.getOriginalFilename() + "!");
            return "redirect:/";
        };
    }

    @ExceptionHandler(ZipException.class)
    public String handleZipException(final ZipException exception, final RedirectAttributes redirectAttributes) {
        final Throwable rootCause = ExceptionUtil.findRootCause(exception);
        LOGGER.warn("Unable to extract zip file because: {}", rootCause.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(IOException.class)
    public String handleIOException(final IOException exception, final RedirectAttributes redirectAttributes) {
        final Throwable rootCause = ExceptionUtil.findRootCause(exception);
        LOGGER.error(rootCause.getMessage(), exception);
        return "redirect:/";
    }

}
