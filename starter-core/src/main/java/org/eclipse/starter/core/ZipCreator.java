/********************************************************************************
 * Copyright (c) 2020 Jeyvison Nascimento and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Jeyvison Nascimento - initial API and implementation
 ********************************************************************************/

package org.eclipse.starter.core;

import javax.enterprise.context.SessionScoped;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@SessionScoped
public class ZipCreator implements Serializable {

    private Map<String, byte[]> archiveContent = new HashMap<>();

    public void writeContents(String directory, String fileName, String contents) {
        archiveContent.put(directory + File.separator + fileName, contents.getBytes());
    }

    public byte[] createArchive() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {

            for (Map.Entry<String, byte[]> entry : archiveContent.entrySet()) {

                ZipEntry zipEntry = new ZipEntry(entry.getKey());

                zos.putNextEntry(zipEntry);
                zos.write(entry.getValue());
                zos.closeEntry();

            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        archiveContent.clear();
        return baos.toByteArray();
    }

}
