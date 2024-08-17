package com.clear.MainPackage;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.*;

public class Signin extends HttpServlet 
{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String userid=req.getParameter("user_id");
    String username=req.getParameter("user_name");
    String useremail=req.getParameter("user_email");
    String userphoneno=req.getParameter("user_phoneno");
    String userpassword=req.getParameter("user_password");
    String userrole=req.getParameter("role");

    Connection con=null;
    RequestDispatcher dispatcher=null;

    try{
        String Hashedpassword=hashPassword(userpassword);
        Class.forName("org.mariadb.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3310/test", "root", "root");
        PreparedStatement ps = con.prepareStatement("INSERT INTO test.user (user_id, user_name, user_email, user_phoneno, user_password,role) VALUES (?, ?, ?, ?, ?, ?)");
        ps.setString(1,userid);
        ps.setString(2,username);
        ps.setString(3,useremail);
        ps.setString(4,userphoneno);
        ps.setString(5,Hashedpassword);
        ps.setString(6,userrole);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            dispatcher=req.getRequestDispatcher("login.jsp");
        } 
        dispatcher.forward(req, resp);
    } catch (Exception e) {
        e.printStackTrace();
    }finally{
        try{
            if(con !=null){
                con.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
  }
  
  public String hashPassword(String userpassword) throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    byte[] hash = md.digest(userpassword.getBytes());
    StringBuilder hexString = new StringBuilder();
    for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
    }
    return hexString.toString();
  }
}
