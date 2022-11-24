package org.eclipse.starter.mavengenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.maven.cli.CliRequest;
import org.apache.maven.cli.MavenCli;

public class CliMavenContext extends MavenContext<CliMavenContext> {

    @Override
    public void run() {
        System.setProperty(MavenCli.MULTIMODULE_PROJECT_DIRECTORY, workingDir.toAbsolutePath().toString()); // for newer maven versions
        MavenCli cli = new MavenCli();
        List<String> args = new ArrayList<>(opts);
        args.addAll(goals);
        for (Map.Entry<Object, Object> prop : properties.entrySet()) {
            if (prop.getValue() != null) {
                args.add("-D" + prop.getKey() + "=" + prop.getValue());
            } else {
                args.add("-D" + prop.getKey());
            }
        }
        if (0 != cli.doMain(args.toArray(new String[0]),
                workingDir.toAbsolutePath().toString(),
                System.out, System.err) ) {
            throw new RuntimeException("Maven CLI didn't complete successfully");
        }
    }
}

