package mk.ukim.finki.wp.eshop.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name="messy-category-servlet", urlPatterns = "/servlet/messy/category")
public class MessyCategoryServlet extends HttpServlet {

    class Category {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Category(String name) {
            this.name = name;
        }
    }

    private List<Category> categoryList = null;

    @Override
    public void init() throws ServletException {
        super.init();
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Books"));
        categoryList.add(new Category("Movies"));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ipAddress = req.getRemoteAddr();
        String clientAgent = req.getHeader("User-Agent");
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h2>User Info</h2>");
        out.format("IP address: %s<br/>",ipAddress);
        out.format("Client Agent: %s",clientAgent);
        out.println("<h2>Category List</h2>");
        out.println("<ul>");
        categoryList.forEach(r->
                out.format("<li>%s</li>",r.getName())
        );
        out.println("</ul>");

        out.println("<h2>Add New Category</h2>");

        out.println("<form method='POST' action='/servlet/category'/>");
        out.println("<label for='name'>Name:<label>");
        out.println("<input id='name' type='text' name='name'/>");
        out.println("<input type='submit' value='Submit'/>");
        out.println("</form>");

        out.println("</body>");
        out.println("</html>");
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryName = req.getParameter("name");
        addCategory(categoryName);
        resp.sendRedirect("/servlet/category");
    }

    public void addCategory(String name) {
        if (name!=null && !name.isEmpty()) {
            categoryList.add(new Category(name));
        }
    }
}

