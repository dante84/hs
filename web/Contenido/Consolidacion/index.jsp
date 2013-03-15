<%-- 
    Document   : index
    Created on : 13/03/2013, 05:17:01 PM
    Author     : Daniel.Meza
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
<!--        <object type="application/x-java-applet" width="500" height="300">    
                <param name="code" value="Applet" />
                <param name="archive" value="/test/Applet.jar" />
    <param name="scriptable" value="true" />
    <param name="mayscript" value="true" />
    <param name="background-color" value="#ffffff" />
    <param name="border-color" value="#8c8cad" />
        </object>-->

    <applet code="Applet" archive="Applet.jar">
        <param name="jnlp_href" value=" http://172.16.37.32:8080/hs/Try.jnlp"/>
    </applet>

    </body>
</html>
