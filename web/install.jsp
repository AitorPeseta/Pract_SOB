<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.SQLException" %>

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
                Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/" + dbname, "root", "root");
                Statement stmt = con.createStatement();

                // Ingresar datos
                String data[] = new String[]{
                    "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Computer Science')",
                    "INSERT INTO " + schema + ".COMMENT VALUES (NEXT VALUE FOR COMMENT_GEN, 'Skeleton code', 1)",
                    "INSERT INTO " + schema + ".COMMENT VALUES (NEXT VALUE FOR COMMENT_GEN, 'for homework1', 1)",
                    "INSERT INTO " + schema + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'sob', 'sob')",
                    "INSERT INTO " + schema + ".CUSTOMER VALUES (NEXT VALUE FOR CUSTOMER_GEN, 'johndoe', 'password123', 'johndoe@example.com', false, NULL, NULL, NULL)",
                    "INSERT INTO " + schema + ".CUSTOMER VALUES (NEXT VALUE FOR CUSTOMER_GEN, 'janedoe', 'securepassword', 'janedoe@example.com', false, NULL)",
                    "INSERT INTO " + schema + ".CUSTOMER VALUES (NEXT VALUE FOR CUSTOMER_GEN, 'writerX', 'passWriter', 'writerX@example.com', true, 1)",
                    "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, 'Understanding Databases','Content about databases...', 'A brief overview of databases', 1, 'Databases, SQL, Data Management', true)",
                    "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, 'Introduction to Java','Content about Java programming...', 'Summary of Java programming', 2, 'Programming, Java, Object-Oriented', false)",
                    "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, 'Web Development Basics', 'Learn about HTML, CSS, and JavaScript...', 'Quick start guide to web development', 3, 'Web Development, HTML, CSS', true)"
                };

                for (String datum : data) {
                    if (stmt.executeUpdate(datum) <= 0) {
                        out.println("<span class='error'>Error inserting data: " + datum + "</span>");
                        return;
                    }
                    out.println("<pre> -> " + datum + "</pre>");
                }

                // Cerrar la conexión a la base de datos
                stmt.close();
                con.close();

            } catch (Exception e) {
                out.println("<span class='error'>Error: " + e.getMessage() + "</span>");
            }
        %>
        <button onclick="window.location='<%=request.getSession().getServletContext().getContextPath()%>'">Go home</button>
    </body>
</html>
