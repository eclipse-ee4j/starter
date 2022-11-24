package org.eclipse.starter.mavengenerator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public abstract class MavenContext<THIS extends MavenContext> {

    protected Path workingDir = Paths.get(System.getProperty("java.io.tmpdir", "noWorkingDir"));
    protected Properties properties = new Properties();
    protected List<String> goals = new ArrayList<>();
    protected List<String> opts = new ArrayList<>();
    
    public MavenContext() {
    }

    public THIS workingDirectory(Path workingDir) {
        this.workingDir = workingDir;
        return (THIS)this;
    }
    
    public THIS addGoals(List<String> args) {
        this.goals.addAll(args);
        return (THIS)this;
    }
    
    public THIS addOptions(List<String> options) {
        this.opts.addAll(options);
        return (THIS)this;
    }
    
    public THIS addOptions(String... options) {
        this.opts.addAll(Arrays.asList(options));
        return (THIS)this;
    }
    
    public THIS addProperties(Properties properties) {
        this.properties.putAll(properties);
        return (THIS)this;
    }
    
    abstract public boolean run();
    
}
