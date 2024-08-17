package com.clear.MainPackage;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class AuthorizationFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String role = (String) session.getAttribute("role");

        String uri = req.getRequestURI();
        boolean authorized = false;

        if (uri.endsWith("admin.jsp") && "admin".equalsIgnoreCase(role)) {
            authorized = true;
        } else if (uri.endsWith("student.jsp") && "student".equalsIgnoreCase(role)) {
            authorized = true;
        } else if (uri.endsWith("staff.jsp") && "staff".equalsIgnoreCase(role)) {
            authorized = true;
        }

        if (authorized) {
            chain.doFilter(request, response);
        } else {
            res.sendRedirect("login.jsp");
        }
    }

    public void init(FilterConfig fConfig) throws ServletException {}
    public void destroy() {}
}
