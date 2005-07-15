<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="struts-tiles" %>



<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<struts-html:html locale="true" xhtml="true">
<struts-html:base/>
<head>

<title>
<struts-tiles:getAsString name="title" />
</title>

<link rel="stylesheet" type="text/css" href="style.css">
</head>
<body>
	<tbody>
		<struts-tiles:insert attribute="header" />
		<struts-tiles:insert attribute="body" />
		<struts-tiles:insert attribute="footer" />
	</tbody>
</body>

</struts-html:html>