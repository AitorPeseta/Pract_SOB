<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.Statement" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>



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
                
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String data = formatter.format(new Date());
                  
                String insertCred = "INSERT INTO " + schema + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'sob', 'sob')";
                stmt.executeUpdate(insertCred);
                
                out.println("<pre> -> " + insertCred + "</pre>");
                
                String insertTopic1 = "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Computer Science')";
                stmt.executeUpdate(insertTopic1);
                String idTopic1 = "SELECT id FROM " + schema + ".TOPIC WHERE name = 'Computer Science'";
                ResultSet rst1 = stmt.executeQuery(idTopic1);
                Long topicid1 = null;

                if (rst1.next()) {
                    topicid1 = rst1.getLong(1);
                }
                
                out.println("<pre> -> " + insertTopic1 + "</pre>");
                
                String insertTopic2 = "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Databases')";
                stmt.executeUpdate(insertTopic2);
                String idTopic2 = "SELECT id FROM " + schema + ".TOPIC WHERE name = 'Databases'";
                ResultSet rst2 = stmt.executeQuery(idTopic2);
                Long topicid2 = null;

                if (rst2.next()) {
                    topicid2 = rst2.getLong(1);
                }
                
                out.println("<pre> -> " + insertTopic2 + "</pre>");
                
                String insertTopic3 = "INSERT INTO " + schema + ".TOPIC VALUES (NEXT VALUE FOR TOPIC_GEN, 'Programming')";
                stmt.executeUpdate(insertTopic3);
                String idTopic3 = "SELECT id FROM " + schema + ".TOPIC WHERE name = 'Programming'";
                ResultSet rst3 = stmt.executeQuery(idTopic3);
                Long topicid3 = null;

                if (rst3.next()) {
                    topicid3 = rst3.getLong(1);
                }
                
                out.println("<pre> -> " + insertTopic3 + "</pre>");
                
                String insertCredentialsCustomerSQL1 = "INSERT INTO " + schema + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN, 'abc', 'abc')";
                stmt.executeUpdate(insertCredentialsCustomerSQL1);
                String identityCredentialsQuery1 = "SELECT id FROM " + schema + ".CREDENTIALS WHERE username = 'abc'";
                ResultSet rsc1 = stmt.executeQuery(identityCredentialsQuery1);
                Long credentialid1 = null;

                if (rsc1.next()) {
                    credentialid1 = rsc1.getLong(1);
                }
                String insertCustomerSQL1 = "INSERT INTO " + schema + ".CUSTOMER VALUES (NEXT VALUE FOR CUSTOMER_GEN , 'johndoe@example.com', 0, NULL, '"+ data +"'," + credentialid1 +")";
                stmt.executeUpdate(insertCustomerSQL1);
                // Recuperar el último valor de clave generada
                String identityQuery1 = "SELECT id FROM " + schema + ".CUSTOMER WHERE credentials_id = "+credentialid1+"";
                ResultSet rs1 = stmt.executeQuery(identityQuery1);
                Long customerid1 = null;

                if (rs1.next()) {
                    customerid1 = rs1.getLong(1);
                }

                String insertCommentSQL1 = "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, 'Content about databases...', NULL, 1,'"+ data +"', 'A brief overview of databases', 'Understanding Databases', 0, " + customerid1 + ")";   //Topics . 'Computer Science', 'Databases',
                stmt.executeUpdate(insertCommentSQL1);
                String idArticle1 = "SELECT id FROM " + schema + ".ARTICLE WHERE title = 'Understanding Databases'";
                ResultSet rsa1 = stmt.executeQuery(idArticle1);
                Long articleid1 = null;

                if (rsa1.next()) {
                    articleid1 = rsa1.getLong(1);
                }
                out.println("<pre> -> " + insertCommentSQL1 + "</pre>");
                
                String updateCustomerSQL1 = "UPDATE " + schema + ".CUSTOMER SET lastArticleId = " + articleid1 + ", isAuthor = 1 WHERE id = " + customerid1 + "";
                stmt.executeUpdate(updateCustomerSQL1);
                out.println("<pre> -> " + updateCustomerSQL1 + "</pre>");
               
                String insertCredentialsCustomerSQL2 = "INSERT INTO " + schema + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN,'passWriter','writerX')";
                stmt.executeUpdate(insertCredentialsCustomerSQL2);
                String identityCredentialsQuery2 = "SELECT id FROM " + schema + ".CREDENTIALS WHERE username = 'writerX'";
                ResultSet rsc2 = stmt.executeQuery(identityCredentialsQuery2);
                Long credentialid2 = null;

                if (rsc2.next()) {
                    credentialid2 = rsc2.getLong(1);
                }
                
                String insertCustomerSQL2 = "INSERT INTO " + schema + ".CUSTOMER VALUES (NEXT VALUE FOR CUSTOMER_GEN,'writerX@example.com', 0, NULL,'"+ data +"',"+credentialid2+")";
                stmt.executeUpdate(insertCustomerSQL2);
                // Recuperar el último valor de clave generada
                String identityQuery2 = "SELECT id FROM " + schema + ".CUSTOMER WHERE credentials_id = "+credentialid2+"";
                ResultSet rs2 = stmt.executeQuery(identityQuery2);
                
                Long customerid2 = null;

                if (rs2.next()) {
                    customerid2 = rs2.getLong(1);
                }

                String insertCommentSQL2 = "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, 'Content about Java programming...', NULL, 1,'"+ data +"', 'Summary of Java programming', 'Introduction to Java', 0, " + customerid2 + ")"; //topics : 'Databases', 'Programming'
                stmt.executeUpdate(insertCommentSQL2);
                String idArticle2 = "SELECT id FROM " + schema + ".ARTICLE WHERE title = 'Introduction to Java'";
                ResultSet rsa2 = stmt.executeQuery(idArticle2);
                Long articleid2 = null;

                if (rsa2.next()) {
                    articleid2 = rsa2.getLong(1);
                }
                out.println("<pre> -> " + insertCommentSQL2 + "</pre>");
                
                String updateCustomerSQL2 = "UPDATE " + schema + ".CUSTOMER SET lastArticleId = " + articleid2 + ", isAuthor = 1 WHERE id = " + customerid2 + "";
                stmt.executeUpdate(updateCustomerSQL2);
                out.println("<pre> -> " + updateCustomerSQL2 + "</pre>");
                
                String insertCredentialsCustomerSQL3 = "INSERT INTO " + schema + ".CREDENTIALS VALUES (NEXT VALUE FOR CREDENTIALS_GEN,'carmanyola','Carmanyola')";
                stmt.executeUpdate(insertCredentialsCustomerSQL3);
                String identityCredentialsQuery3 = "SELECT id FROM " + schema + ".CREDENTIALS WHERE username = 'Carmanyola'";
                ResultSet rsc3 = stmt.executeQuery(identityCredentialsQuery3);
                Long credentialid3 = null;

                if (rsc3.next()) {
                    credentialid3 = rsc3.getLong(1);
                }
                
                String insertCustomerSQL3 = "INSERT INTO " + schema + ".CUSTOMER VALUES (NEXT VALUE FOR CUSTOMER_GEN, 'carmanyola@example.com', 0, NULL,'"+ data +"',"+credentialid3+")";
                stmt.executeUpdate(insertCustomerSQL3);
                // Recuperar el último valor de clave generada
                String identityQuery3 = "SELECT id FROM " + schema + ".CUSTOMER WHERE credentials_id = "+credentialid3+"";
                ResultSet rs3 = stmt.executeQuery(identityQuery3);
                Long customerid3 = null;

                if (rs3.next()) {
                    customerid3 = rs3.getLong(1);
                }

                String insertCommentSQL3 = "INSERT INTO " + schema + ".ARTICLE VALUES (NEXT VALUE FOR ARTICLE_GEN, 'Learn about HTML, CSS, and JavaScript...', NULL, 1,'"+ data +"', 'Quick start guide to web development', 'Web Development Basics', 0, " + customerid3 + ")"; //Topic : 'Programming'
                stmt.executeUpdate(insertCommentSQL3);
                String idArticle3 = "SELECT id FROM " + schema + ".ARTICLE WHERE title = 'Web Development Basics'";
                ResultSet rsa3 = stmt.executeQuery(idArticle3);
                Long articleid3 = null;

                if (rsa3.next()) {
                    articleid3 = rsa3.getLong(1);
                }
                out.println("<pre> -> " + insertCommentSQL3 + "</pre>");
                
                String updateCustomerSQL3 = "UPDATE " + schema + ".CUSTOMER SET lastArticleId = " + articleid3 + ", isAuthor = 1 WHERE id = " + customerid3 + "";
                stmt.executeUpdate(updateCustomerSQL3);
                out.println("<pre> -> " + updateCustomerSQL3 + "</pre>");
                
                
                String insertArticleTopic = "INSERT INTO " + schema + ".ARTICLE_TOPIC VALUES ("+articleid1+","+topicid1+")";
                stmt.executeUpdate(insertArticleTopic);
                
                out.println("<pre> -> " + insertArticleTopic + "</pre>");
                
                insertArticleTopic = "INSERT INTO " + schema + ".ARTICLE_TOPIC VALUES ("+articleid1+","+topicid2+")";
                stmt.executeUpdate(insertArticleTopic);
                
                out.println("<pre> -> " + insertArticleTopic + "</pre>");

                
                insertArticleTopic = "INSERT INTO " + schema + ".ARTICLE_TOPIC VALUES ("+articleid2+","+topicid2+")";
                stmt.executeUpdate(insertArticleTopic);
                
                out.println("<pre> -> " + insertArticleTopic + "</pre>");
                
                insertArticleTopic = "INSERT INTO " + schema + ".ARTICLE_TOPIC VALUES ("+articleid2+","+topicid3+")";
                stmt.executeUpdate(insertArticleTopic);
                
                out.println("<pre> -> " + insertArticleTopic + "</pre>");
                
                insertArticleTopic = "INSERT INTO " + schema + ".ARTICLE_TOPIC VALUES ("+articleid3+","+topicid3+")";
                stmt.executeUpdate(insertArticleTopic);
                
                out.println("<pre> -> " + insertArticleTopic + "</pre>");
                
                
                
                
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
