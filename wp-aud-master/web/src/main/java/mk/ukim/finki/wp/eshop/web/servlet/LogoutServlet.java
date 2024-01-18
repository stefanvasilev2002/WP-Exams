package mk.ukim.finki.wp.eshop.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name="LogoutServlet", urlPatterns = "/servlet/logout")
public class LogoutServlet extends HttpServlet {

    /*
     * We use the service method since it is invoked for all HTTP methods
     */


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        resp.sendRedirect("/login");
    }


}
