package org.eclipse.starter.core.plugin;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;

public class LibertyPlugin implements Plugin{

    public LibertyPlugin(){
        try(BufferedInputStream bufferedInputStream =
                    new BufferedInputStream(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("liberty/liberty.xml")))){
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
