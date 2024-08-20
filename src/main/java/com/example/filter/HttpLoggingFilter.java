package com.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(HttpLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);

        var parameters = httpRequest.getParameterMap();

        StringBuilder builder = new StringBuilder();
        for (String key : parameters.keySet()) {
            builder.append(key).append("=").append(Arrays.toString(parameters.get(key))).append("&");
        }
        if (!builder.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }

        long startTime = System.currentTimeMillis();

        logger.info("Incoming request: method=[{}], uri=[{}], headers=[{}], params=[{}]",
                httpRequest.getMethod(), httpRequest.getRequestURI(), getHeaders(httpRequest), builder);
        String responseBody = "";
        try {
            chain.doFilter(request, responseWrapper);
        } catch (Exception e) {
            logger.error("Request processing failed", e);
            throw e;
        } finally {
            responseBody = new String(responseWrapper.getContentAsByteArray(), response.getCharacterEncoding());
            responseWrapper.copyBodyToResponse();
        }

        long duration = System.currentTimeMillis() - startTime;

        logger.info("Outgoing response: status=[{}], headers=[{}], body=[{}], duration=[{}ms]",
                responseWrapper.getStatus(), getHeaders(responseWrapper), responseBody, duration);

        responseWrapper.flushBuffer();
    }

    @Override
    public void destroy() {
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(
                        name -> name,
                        name -> String.join(", ", Collections.list(request.getHeaders(name)))
                ));
    }

    private Map<String, String> getHeaders(HttpServletResponse response) {
        return response.getHeaderNames()
                .stream()
                .collect(Collectors.toMap(
                        name -> name,
                        name -> String.join(", ", response.getHeaders(name))
                ));
    }
}