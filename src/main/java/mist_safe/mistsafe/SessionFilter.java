package mist_safe.mistsafe;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class SessionFilter implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        // Check if the session cookie is still valid
        String sessionCookie = (String) request.getSession().getAttribute("sessionCookie");
        if (sessionCookie == null) {
            response.sendRedirect("login"); // redirect to the login page
            return false;
        }

        // Check if the idToken cookie is still valid
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            response.sendRedirect("login"); // redirect to the login page
            return false;
        }

        boolean found = false;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("idToken") && cookie.getValue().equals(sessionCookie)) {
                found = true;
                break;
            }
        }

        if (!found) {
            response.sendRedirect("login"); // redirect to the login page
            return false;
        }

        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // Do nothing
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception exception) throws Exception {
        // Do nothing
    }
}
