package org.eclipse.starter.core.plugin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class PayaraPlugin implements Plugin {

    public PayaraPlugin(){
        try(BufferedInputStream bufferedInputStream =
                    new BufferedInputStream(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("payara/payara.xml")))){
            pluginDefinition = new String(bufferedInputStream.readAllBytes());
        }
        catch (IOException ioException){
            System.out.println(ioException);
        }

    }

    private String pluginDefinition;

    public String getPluginDefinition(){
        return pluginDefinition;
    }
}
