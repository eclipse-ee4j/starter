package org.eclipse.starter.ui;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StarterServletTest {

    private StarterServlet sut;

    @Mock
    private HttpServletRequest httpServletRequestMock;

    @Mock
    private HttpServletResponse httpServletResponseMock;

    private ServletOutputStream servletOutputStreamStub;

    @BeforeEach
    void setUp() {
        this.servletOutputStreamStub = new StubServletOutputStream();

        this.sut = new StarterServlet();
    }

    @Test
    void doGet() throws Exception {
        //given
        when(this.httpServletRequestMock.getParameter("archetypeGroupId")).thenReturn("org.eclipse.starter");
        when(this.httpServletRequestMock.getParameter("archetypeArtifactId")).thenReturn("jakartaee10-minimal");
        when(this.httpServletRequestMock.getParameter("archetypeVersion")).thenReturn("1.1.0");
        when(this.httpServletRequestMock.getParameter("groupId")).thenReturn("com.sample");
        when(this.httpServletRequestMock.getParameter("artifactId")).thenReturn("hello_world");
        when(this.httpServletRequestMock.getParameter("version")).thenReturn("42.0.0-SNAPSHOT");
        when(this.httpServletResponseMock.getOutputStream()).thenReturn(this.servletOutputStreamStub);

        //when
        this.sut.doGet(this.httpServletRequestMock, this.httpServletResponseMock);

        //then
        // Verify mock interactions
        verify(this.httpServletResponseMock).setContentType("application/zip");
        verify(this.httpServletResponseMock).setHeader("Content-Disposition", "attachment;filename=\"hello_world.zip\"");
        verify(this.httpServletResponseMock, atLeastOnce()).getOutputStream();

        // Verify the zip file
        final StubServletOutputStream stubServletOutputStream = ((StubServletOutputStream) this.servletOutputStreamStub);
        assertThat(stubServletOutputStream.isclosed()).isTrue();

        final var outputStream = stubServletOutputStream.getByteArrayOutputStream();
        assertThat(outputStream).isNotNull();
        assertThat(outputStream.toString()).isNotEmpty();
        assertThat(outputStream.toByteArray()).isNotEmpty();

        final var zipInputStream = new ZipInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        final var zipFilenames = Stream.generate(() -> {
            try {
                return zipInputStream.getNextEntry();
            } catch (final IOException e) {
                e.printStackTrace();
            }
            return null;
        }).takeWhile(Objects::nonNull).map(ZipEntry::getName).toArray(String[]::new);

        String ps = File.separator;
        assertThat(zipFilenames).containsAll(() -> Stream.of(String.join(ps, "hello_world", "pom.xml"),
                String.join(ps, "hello_world", "src", "main", "resources", "META-INF", "beans.xml"),
                String.join(ps, "hello_world", "src", "main", "java", "com", "sample", "hello_world", "ApplicationConfig.java"),
                String.join(ps, "hello_world", "src", "main", "java", "com", "sample", "hello_world", "resources", "HelloRecord.java"),
                String.join(ps, "hello_world", "src", "main", "java", "com", "sample", "hello_world", "resources", "RestResource.java"))
                .iterator());
    }

    private static class StubServletOutputStream extends ServletOutputStream {

        private final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        private boolean isclosed = false;

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(final WriteListener writeListener) {

        }

        @Override
        public void write(final int b) {
            this.byteArrayOutputStream.write(b);
        }

        public ByteArrayOutputStream getByteArrayOutputStream() {
            return this.byteArrayOutputStream;
        }

        @Override
        public void close() throws IOException {
            this.byteArrayOutputStream.close();
            this.isclosed = true;
        }

        public boolean isclosed() {
            return this.isclosed;
        }
    }
}
