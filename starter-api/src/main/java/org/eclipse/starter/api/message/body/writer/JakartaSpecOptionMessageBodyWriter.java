package org.eclipse.starter.api.message.body.writer;

import org.eclipse.starter.core.JakartaSpecOption;

import javax.json.*;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JakartaSpecOptionMessageBodyWriter implements MessageBodyWriter<JakartaSpecOption[]> {
    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return aClass == JakartaSpecOption[].class;
    }

    @Override
    public void writeTo(JakartaSpecOption[] jakartaSpecOptions, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for(JakartaSpecOption jakartaSpecOption: jakartaSpecOptions){
            JsonObject jsonObject =
                    Json.createObjectBuilder(
                    ).add(
                            "name", jakartaSpecOption.getName()
                    ).add(
                            "versions", Json.createArrayBuilder(Arrays.asList(jakartaSpecOption.getVersions()))
                    ).build();

             jsonArrayBuilder.add(jsonObject);
        }

        outputStream.write(jsonArrayBuilder.build().toString().getBytes());
        outputStream.flush();
    }
}
