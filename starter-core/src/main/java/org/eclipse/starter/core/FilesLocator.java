package org.eclipse.starter.core;


import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@ApplicationScoped
public class FilesLocator {

    private List<FileIdentification> fileIdentifications;
    private List<String> fileNames;

    @PostConstruct
    public void init() {
        defineResources(Pattern.compile(".*\\.tpl"));
    }

    public String findFile(String name) {
        List<FileIdentification> candidates = fileIdentifications
                .stream()
                .filter(fi -> fi.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());

        int result = -1; // not found
        if (!candidates.isEmpty()) {
            if (candidates.size() == 1) {
                result = fileIdentifications.indexOf(candidates.get(0));
            }
        }

        if (result == -1) {
            return null;
        }

        return fileNames.get(result);
    }

    private void defineResources(Pattern pattern) {
        String path = "src/main/resources/";
        Set<String> resources = new HashSet<>();

        try (Scanner scanner = new Scanner(FilesLocator.class.getClassLoader().getResourceAsStream("/files.lst"))) {

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (pattern.matcher(line).matches()) {
                    resources.add(line.substring(path.length()));
                }
            }
        }

        fileIdentifications = new ArrayList<>();
        fileNames = new ArrayList<>();
        for (String resource : resources) {
            // Strip .tpl
            String fileName = resource.substring(0, resource.length() - 4);

            fileIdentifications.add(new FileIdentification(fileName));
            fileNames.add(resource);
        }

    }

    private static class FileIdentification {
        private static final Pattern FILE_PATH_PATTERN_SPLIT = Pattern.compile("\\\\|/");
        private String name;

        public FileIdentification(String fileName) {
            String[] fileParts = FILE_PATH_PATTERN_SPLIT.split(fileName);
            this.name = fileParts[fileParts.length - 1];
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof FileIdentification)) {
                return false;
            }

            FileIdentification that = (FileIdentification) o;

            return name.equals(that.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }
}
