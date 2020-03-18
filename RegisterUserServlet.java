/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ieee;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Praveen
 */
@WebServlet(name = "RegisterUserServlet", urlPatterns = {"/RegisterUserServlet"})
public class RegisterUserServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {

        try {

            Connection con = DbConnection.getConnections();            
            String name = request.getParameter("name");
            String mob = request.getParameter("mob");
            String mail = request.getParameter("c");
            String pwd = request.getParameter("pwd");
            
            String q = "insert into USERDET(name,mail,mobile,pwd) values(?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(q);
            pstmt.setString(1, name);
            pstmt.setString(2, mail);
            pstmt.setString(3, mob);
            pstmt.setString(4, pwd);          

            int i = pstmt.executeUpdate();

            if (i >= 1) {
                RequestDispatcher rd = request.getRequestDispatcher("SuccessRegister.jsp");
                rd.forward(request, response);
            } else {
                RequestDispatcher rd = request.getRequestDispatcher("FailRegister.jsp");
                rd.forward(request, response);

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
