/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ims.servlet.admin;

import ims.entity.LostPasswordRequest;
import ims.entity.User;
import ims.management.LoginManagement;
import ims.management.ResetPasswordManagement;
import ims.management.TemplatingManagement;
import ims.management.UserManagement;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *This servlet shows a list of all users request for reset of their passwords. 
 * @author andrazhribernik
 */
@WebServlet(name = "ResetPasswordRequestsListServlet", urlPatterns = {"/ResetPasswordRequestsList"})
public class ResetPasswordRequestsListServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *Show html table with user requests. First are shown requests which have not been
     * processed.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        LoginManagement lm = new LoginManagement(request.getSession());
        lm.userPermissionForThisPage(response, new String[]{"admin"});
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            
            String content = "<div class=\"panel panel-default\"><div class=\"panel-heading\">"
                    + "<h3>Users Requests</h3>"
                    + "</div><table class=\"table table-striped\">";
            content +="<thead><tr><th>user</th><th>Role</th><th></th></tr></thead>";
            content += "<tbody>";
            ResetPasswordManagement rpm = new ResetPasswordManagement();
            for(LostPasswordRequest lpr :rpm.getAllRequests()){
                if(lpr.getIsRead()){
                    content += "<tr>";
                }
                else{
                    content += "<tr class=\"warning\">";
                }
                content+="<td>"+lpr.getUseridUser().getUsername()+"</td>";
                content+="<td>"+lpr.getUseridUser().getRoleidRole().getName()+"</td>";
                content+="<td><a href=\"ResetPassword?requestID="+lpr.getIdLostPasswordRequest()+"\">Reset</a></td></tr>";
            }
            content+="</tbody></table></div></div>";

            String result = TemplatingManagement.getTemplateWithContent(this.getServletContext(),content , request.getSession());
            out.print(result);
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
