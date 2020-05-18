package com.example.demo.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * Custom filter
 * 
 * @author hao.cu
 * @since 2020/5/16
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomFilter implements Filter {

    public CustomFilter() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        if(request.getHeader("user-name")!= null ){
            chain.doFilter(req, res);
        } 
        ((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}