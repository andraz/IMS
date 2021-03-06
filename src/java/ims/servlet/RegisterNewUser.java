/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ims.servlet;

import ims.management.LoginManagement;
import ims.management.UserManagement;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *This servlet handles user request for creating new user account
 * @author andrazhribernik
 */
@WebServlet(name = "RegisterNewUser", urlPatterns = {"/RegisterNewUser"})
public class RegisterNewUser extends HttpServlet {

    /**
     * Method checks if the email is valid or not
     * @param email email that you want to check
     * @return True if email is valid
     */
    private static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
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
        //empty method
        System.out.print("Register user getRequest");
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     * Create a new user if username does not exist in table with existing users 
     * and if passwords match.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");
        
        if(username == null){
            throw new ServletException("Parameter username is not set");
        }
        
        if(password == null){
            throw new ServletException("Parameter password is not set");
        }
        
        if(repassword == null){
            throw new ServletException("Parameter re-type password is not set");
        }
        //if email is not valid redirect with message
        if(!isValidEmailAddress(username)){
            response.sendRedirect("./register.jsp?usernameMessage=Email is not valid email address.");
            return;
        }
        //if password is too short redirect with message
        if(password.length() < 4){
            response.sendRedirect("./register.jsp?passwordMessage=Password must contain at least 4 characters.");
            return;
        }
        //if passwords do not match redirect with message
        if(!password.equals(repassword)){
            response.sendRedirect("./register.jsp?rePasswordMessage=Passwords do not match.");
            return;
        }
        
        UserManagement um = new UserManagement();
        try {
            um.getUserByUsername(username);
            //if username already exists redirect with message
            response.sendRedirect("./register.jsp?usernameMessage=Username already exists.");
            return;
        } catch (Exception ex) {
            //validation OK. Add new user, login user and redirect to home page
            um.addUser(username, password);
            LoginManagement lm = new LoginManagement(request.getSession(true));
            lm.logIn(username, password);
            response.sendRedirect("./index.jsp");
            return;
        }
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
