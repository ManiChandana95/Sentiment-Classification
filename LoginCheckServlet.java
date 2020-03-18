/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ieee;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Praveen
 */
@WebServlet(name = "LoginCheckServlet", urlPatterns = {"/LoginCheckServlet"})
public class LoginCheckServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {

        try {
            String username = request.getParameter("uname");
            String password = request.getParameter("pwd");
            Connection con = DbConnection.getConnections();

            HttpSession session = request.getSession(true);
            session.setAttribute("user", username);

            String q = "select * from USERDET where mail=? and pwd=?";
            PreparedStatement pstmt = con.prepareStatement(q);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("SuccessUserLogin.jsp");
                dispatcher.forward(request, response);
            } else {
                if (username.equals("admin") && password.equals("admin")) {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("AdminSuccessLogin.jsp");
                    dispatcher.forward(request, response);
                } else {
                    RequestDispatcher dispatcher = request.getRequestDispatcher("InvalidLogin.jsp");
                    dispatcher.forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
