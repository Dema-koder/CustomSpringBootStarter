package com.example.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ContentCachingResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream content = new ByteArrayOutputStream();
    private final ServletOutputStream outputStream;
    private final PrintWriter writer;

    public ContentCachingResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
        this.outputStream = new ServletOutputStreamWrapper(content, response.getOutputStream());
        this.writer = new PrintWriter(content);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return writer;
    }

    public byte[] getContentAsByteArray() {
        return content.toByteArray();
    }

    @Override
    public void flushBuffer() throws IOException {
        super.flushBuffer();
        if (writer != null) {
            writer.flush();
        }
        if (outputStream != null) {
            outputStream.flush();
        }
    }

    private static class ServletOutputStreamWrapper extends ServletOutputStream {

        private final ByteArrayOutputStream content;
        private final ServletOutputStream outputStream;

        public ServletOutputStreamWrapper(ByteArrayOutputStream content, ServletOutputStream outputStream) {
            this.content = content;
            this.outputStream = outputStream;
        }

        @Override
        public void write(int b) throws IOException {
            content.write(b);
            outputStream.write(b);
        }

        @Override
        public void flush() throws IOException {
            outputStream.flush();
        }

        @Override
        public void close() throws IOException {
            outputStream.close();
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }
    }
}