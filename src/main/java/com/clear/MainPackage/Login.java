package com.clear.MainPackage;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.*;

public class Login extends HttpServlet 
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String userid=req.getParameter("user_id");
    String userpassword=req.getParameter("user_password");

    Connection con=null;
    RequestDispatcher dispatcher=null;


    try{
        String Hashedpassword=hashPassword(userpassword);
        Class.forName("org.mariadb.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3310/test", "root", "root");
        PreparedStatement ps = con.prepareStatement("SELECT * FROM user WHERE user_id = ? AND user_password = ?");

        ps.setString(1,userid);
        ps.setString(2,Hashedpassword);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String username=rs.getString("user_name");
            HttpSession session = req.getSession();
            String role = rs.getString("role");
            HttpSession Session = req.getSession();
            session.setAttribute("username", username);
            Session.setAttribute("role", role);

            if ("admin".equalsIgnoreCase(role)) {
                dispatcher = req.getRequestDispatcher("admin.jsp");
            } 
            else if ("student".equalsIgnoreCase(role)) {
                dispatcher = req.getRequestDispatcher("student.jsp");
            } 
            else if ("staff".equalsIgnoreCase(role)) {
                dispatcher = req.getRequestDispatcher("staff.jsp");
            } 
            else {
                req.setAttribute("status", "invalidRole");
                dispatcher = req.getRequestDispatcher("login.jsp");
            }
        } else {
            req.setAttribute("status", "failed");
            dispatcher = req.getRequestDispatcher("login.jsp");
        }
        dispatcher.forward(req, resp);
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if(con !=null)
        {
            try 
            {
                con.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
}
    public String hashPassword(String userpassword) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hash = md.digest(userpassword.getBytes());
    StringBuilder hexString = new StringBuilder();
    for (byte b : hash) 
    {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
    }
    return hexString.toString();
}
}