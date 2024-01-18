package mk.ukim.finki.wp.eshop.web.servlet;

import mk.ukim.finki.wp.eshop.model.Category;
import mk.ukim.finki.wp.eshop.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name="category_servlet",urlPatterns = "/servlet/category")
public class CategoryServlet extends HttpServlet {

    private final CategoryService categoryService;

    public CategoryServlet(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Category> categories = categoryService.listCategories();
        String ipAddress = req.getRemoteAddr();
        String clientAgent = req.getHeader("User-Agent");
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter writer = resp.getWriter();
        writer.println("<html>");
        writer.println("<head>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("<h2>Info about our request</h2>");
        writer.format("IP Address:%s, Browser: %s",ipAddress,clientAgent);
        writer.println("<h2>Categories</h2>");
        writer.println("<ul>");
        categories.forEach(r->
                writer.format("<li>%s (%s)</li>",r.getName(),r.getDescription()));
        writer.println("</ul>");

        writer.println("<html>");
        writer.println("<head>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("<h3>Add New Category</h3>");
        writer.println("<form method='post' action='/servlet/category'>" +
                "<label for='name'>Name:</label><input id='name' type='text' name='name'/>"+
                "<label for='desc'>Description:</label><input id='desc' type='text' name='description'/>"+
                "<input type='submit' value='Submit'/>"+
                "</form>");
        writer.println("</body>");
        writer.println("<html>");

        writer.println("</body>");
        writer.println("</html>");
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryName = (String)req.getParameter("name");
        String categoryDesc = (String) req.getParameter("description");
        categoryService.create(categoryName,categoryDesc);
        resp.sendRedirect("/servlet/category");
    }


}
