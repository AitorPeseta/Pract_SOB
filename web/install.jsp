<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.PreparedStatement" %>



<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Database SQL Load</title>
    </head>
    <style>
        .error {
            color: red;
        }
        pre {
            color: green;
        }
    </style>
    <body>
        <h2>Database SQL Load</h2>
        <%
            try {
                String dbname = "homework1";
                String schema = "ROOT";
                Class.forName("org.apache.derby.jdbc.ClientDriver");

                // Conectar a la base de datos
                Connection connection = DriverManager.getConnection("jdbc:derby://localhost:1527/" + dbname, "root", "root");
                Statement stmt = connection.createStatement();

               String data[] = new String[]{
                    "INSERT INTO " + schema + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'sob', 'sob')",
                    "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Computer Science')",
                    "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Databases')",
                    "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Programming')"
                };

                for (String datum : data) {
                    if (stmt.executeUpdate(datum) <= 0) {
                        out.println("<span class='error'>Error inserting data: " + datum + "</span>");
                        return;
                    }
                    out.println("<pre> -> " + datum + "</pre>");
                }

                String insertCustomerSQL1 = "INSERT INTO " + schema + ".CUSTOMER VALUES (NEXT VALUE FOR CUSTOMER_GEN, 'johndoe@example.com', 0, NULL, 'password123', NULL, 'johndoe')";
                stmt.executeUpdate(insertCustomerSQL1);
                // Recuperar el último valor de clave generada
                String identityQuery1 = "SELECT id FROM " + schema + ".CUSTOMER WHERE username = 'johndoe'";
                ResultSet rs1 = stmt.executeQuery(identityQuery1);
                Long customerid1 = null;

                if (rs1.next()) {
                    customerid1 = rs1.getLong(1);
                }

                out.println("<pre>Generated topic ID: " + customerid1 + "</pre>");
                String insertCommentSQL1 = "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, 'Content about databases...', NULL, 1, NULL, 'A brief overview of databases', 'Understanding Databases', 'Computer Science', 'Databases', 0, " + customerid1 + ")";
                stmt.executeUpdate(insertCommentSQL1);
                out.println("<pre> -> " + insertCommentSQL1 + "</pre>");
               
                String insertCustomerSQL2 = "INSERT INTO " + schema + ".CUSTOMER VALUES (NEXT VALUE FOR CUSTOMER_GEN, 'writerX@example.com', 0, NULL, 'passWriter', NULL, 'writerX')";
                stmt.executeUpdate(insertCustomerSQL2);
                // Recuperar el último valor de clave generada
                String identityQuery2 = "SELECT id FROM " + schema + ".CUSTOMER WHERE username = 'writerX'";
                ResultSet rs2 = stmt.executeQuery(identityQuery2);
                Long customerid2 = null;

                if (rs2.next()) {
                    customerid2 = rs2.getLong(1);
                }

                out.println("<pre>Generated topic ID: " + customerid2 + "</pre>");
                String insertCommentSQL2 = "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, 'Content about Java programming...', NULL, 1, NULL, 'Summary of Java programming', 'Introduction to Java', 'Databases', 'Programming', 0, " + customerid2 + ")";
                stmt.executeUpdate(insertCommentSQL2);
                out.println("<pre> -> " + insertCommentSQL2 + "</pre>");
                
                String insertCustomerSQL3 = "INSERT INTO " + schema + ".CUSTOMER VALUES (NEXT VALUE FOR CUSTOMER_GEN, 'carmanyola@example.com', 0, NULL, 'carmanyola', NULL, 'Carmanyola')";
                stmt.executeUpdate(insertCustomerSQL3);
                // Recuperar el último valor de clave generada
                String identityQuery3 = "SELECT id FROM " + schema + ".CUSTOMER WHERE username = 'Carmanyola'";
                ResultSet rs3 = stmt.executeQuery(identityQuery3);
                Long customerid3 = null;

                if (rs3.next()) {
                    customerid3 = rs3.getLong(1);
                }

                out.println("<pre>Generated topic ID: " + customerid3 + "</pre>");
                String insertCommentSQL3 = "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, 'Learn about HTML, CSS, and JavaScript...', NULL, 1, NULL, 'Quick start guide to web development', 'Web Development Basics', 'Computer Science', 'Programming', 0, " + customerid3 + ")";
                stmt.executeUpdate(insertCommentSQL3);
                out.println("<pre> -> " + insertCommentSQL3 + "</pre>");
                
                // Cerrar la conexión a la base de datos
                stmt.close();
                connection.close();


            } catch (Exception e) {
                out.println("<span class='error'>Error: " + e.getMessage() + "</span>");
            }
        %>
        <button onclick="window.location='<%=request.getSession().getServletContext().getContextPath()%>'">Go home</button>
    </body>
</html>
