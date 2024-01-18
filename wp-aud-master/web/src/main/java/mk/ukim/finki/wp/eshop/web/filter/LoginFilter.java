package mk.ukim.finki.wp.eshop.web.filter;

import mk.ukim.finki.wp.eshop.model.User;
import org.springframework.context.annotation.Profile;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter
@Profile("servlet")
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        User user = (User)request.getSession().getAttribute("user");

        String path = request.getServletPath();

        if (!"/login".equals(path) &&
                !"/h2".equals(path) &&
                !"/register".equals(path) &&
                !"/main.css".equals(path) && user==null) {
            response.sendRedirect("/login");
        } else {
            filterChain.doFilter(servletRequest,servletResponse);
        }

    }

    @Override
    public void destroy() {

    }
}
