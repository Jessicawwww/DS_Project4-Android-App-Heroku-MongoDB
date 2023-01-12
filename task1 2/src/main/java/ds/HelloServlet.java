package ds;

import java.io.*;
import java.lang.annotation.Documented;
import java.util.ArrayList;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.bson.Document;

import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "helloServlet", value = "")
public class HelloServlet extends HttpServlet {
    HelloModel hm=null;
    private String message;

    public void init() {
        hm=new HelloModel();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");
        ArrayList<Document> a=hm.getTopSearch();
        ArrayList<Document> b=hm.getFullLog();
        long c=hm.getAvgLatency();
        ArrayList<Document> d=hm.getTopDevice();

        request.setAttribute("topTen",a);
        request.setAttribute("fullLog",b);
        request.setAttribute("avgLatency",c);
        request.setAttribute("topDevice",d);

//        a.forEach(doc -> System.out.println(doc.toJson()));
        RequestDispatcher view = request.getRequestDispatcher("index.jsp");
        view.forward(request, response);
    }

    public void destroy() {
    }
}