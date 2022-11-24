package org.eclipse.starter.mavengenerator;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCodec {

    Optional<Consumer<EntryEvent>> entryListener = Optional.empty();

    public ZipCodec entryListener(final Consumer<EntryEvent> value) {
        this.entryListener = Optional.ofNullable(value);
        return this;
    }

    public ZipCodec defaultEntryListener() {
        this.entryListener = Optional.of((entry) -> System.out.println((entry.isDirectory ? "+" : "   ") + entry.entryName));
        return this;
    }

    public void addDirToZipArchive(File dirToZip, ZipOutputStream zos) throws Exception {
        addDirToZipArchive(dirToZip, zos, null);
    }

    private void addDirToZipArchive(File dirToZip, ZipOutputStream zos, String parrentDirectoryName) throws Exception {
        if (dirToZip == null || !dirToZip.exists()) {
            return;
        }

        String zipEntryName = dirToZip.getName();
        if (parrentDirectoryName != null && !parrentDirectoryName.isEmpty()) {
            zipEntryName = parrentDirectoryName + "/" + dirToZip.getName();
        }

        final String entryListenerEntry = zipEntryName;
        if (dirToZip.isDirectory()) {
            entryListener.ifPresent(listener -> listener.accept(new EntryEvent(true, entryListenerEntry)));
            for (File file : dirToZip.listFiles()) {
                addDirToZipArchive(file, zos, zipEntryName);
            }
        } else {
            entryListener.ifPresent(listener -> listener.accept(new EntryEvent(false, entryListenerEntry)));
            byte[] buffer = new byte[1024];
            FileInputStream fis = new FileInputStream(dirToZip);
            zos.putNextEntry(new ZipEntry(zipEntryName));
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
            zos.closeEntry();
            fis.close();
        }
    }
    
    public static class EntryEvent {
        public final boolean isDirectory;
        public final String entryName;

        public EntryEvent(boolean isDirectory, String entryName) {
            this.isDirectory = isDirectory;
            this.entryName = entryName;
        }
        
    }
}
