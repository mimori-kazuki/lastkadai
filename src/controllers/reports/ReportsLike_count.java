package controllers.reports;

import java.io.IOException;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Like;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsLike_count
 */
@WebServlet("/reports/like_count")
public class ReportsLike_count extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsLike_count() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();
        Report r = em.find(Report.class, Integer.parseInt(request.getParameter("id")));

        //取得した日報データのいいね数に１加算して再度セットする

        int like_count = r.getLike_count() + 1;
        r.setLike_count(like_count);

        Like l = new Like();
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        l.setCreated_at(currentTime);
        l.setUpdated_at(currentTime);
        l.setEmployee((Employee)request.getSession().getAttribute("login_employee"));
        l.setReport(r);

        em.getTransaction().begin();
        em.persist(l);
        em.getTransaction().commit();
        em.close();
        request.getSession().setAttribute("flush", "いいねしました");

        response.sendRedirect(request.getContextPath() + "/reports/index");
    }

}
