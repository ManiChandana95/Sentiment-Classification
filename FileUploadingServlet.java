/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ieee;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadFile;

/**
 *
 * @author Praveen
 */
@WebServlet(urlPatterns = {"/FileUploadingServlet"})
public class FileUploadingServlet extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response) {

        Connection con = null;
        PreparedStatement pstmt = null;
        con = DbConnection.getConnections();
        try {
            HttpSession seesion = request.getSession(false);
            String mail = (String) seesion.getAttribute("user");

            String path = "E:/IEEE/store1/";
            String enc_path = "E:/IEEE/store2/";

            UploadBean upb = new UploadBean();
            upb.setFolderstore(path);
            upb.setOverwrite(false);
            Hashtable ht;
            UploadFile curfile;
            /* create a parser for parsing form data */
            MultipartFormDataRequest nreq = new MultipartFormDataRequest(request);

            ht = nreq.getFiles();
            String fname = nreq.getParameter("fname");
            Enumeration files = ht.elements();
            while (files.hasMoreElements()) {
                curfile = (UploadFile) files.nextElement();
                String path1 = curfile.getFileName();
                String filenameSplit[] = path1.split(".");
                upb.store(nreq);

                FileInputStream in = null;
                FileOutputStream out = null;
                in = new FileInputStream("E:/IEEE/store1/" + path1);
                out = new FileOutputStream(enc_path + path1);

                String input = null;
                int c;
                while ((c = in.read()) != -1) {
                    if (input == null) {
                        input = Character.toString((char) c);
                    } else {
                        input = input + Character.toString((char) c);
                    }
                }
                in.close();
                out.close();

                //delete file
                File f = new File("E:/IEEE/store1/" + path1);
                File f1 = new File("E:/IEEE/store2/" + path1);
                f.delete();
                f1.delete();               

                String data[] = input.split("\\.");
                List dataList = new ArrayList();
                for (int i = 0; i < data.length; i++) {
                    dataList.add(data[i]);
                }
                SentimentalCalculation sc = new SentimentalCalculation();
                Map sentiMap = sc.getSentimenatlMap(dataList);
                request.setAttribute("sentiMap", sentiMap);
                request.setAttribute("input", input);


//                DbConnection db = new DbConnection();
//                con = db.getConnections();
//                String query = "insert into store(mail,fname) values(?,?,?,?,?,?)";
//                pstmt = con.prepareStatement(query);
//                pstmt.setString(1, mail);
//                pstmt.setString(2, path1);
//                int q = pstmt.executeUpdate();

                RequestDispatcher rd = request.getRequestDispatcher("sentimentalResultCount.jsp");
                rd.forward(request, response);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
