<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr>
	<td align="center">
		<table width="97%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td align="center">
				<fieldset class="mainframe"><legend>Cooperator v1.0</legend>
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td>
						<!--<img src="pics/coopLogoCenter.gif" height="64" width="64">-->
							<img src="pics/coopLogoLeft.gif" height="64" width="16"><img
							src="pics/coopLogoPlayer.gif" height="64" width="64"><img
							src="pics/coopLogoRight.gif" height="64" width="176">
						</td>
						<td>
						<div style="text-align:right">
					<struts-html:form action="/HeaderAction">

					<struts-logic:equal name="HeaderFormular" property="showLogin" value="true">
						<struts-html:link action="/login">Login</struts-html:link>&nbsp;&nbsp;&nbsp;
					</struts-logic:equal>
					<struts-logic:equal name="HeaderFormular" property="showLogout" value="true">
						<struts-html:link action="/logout">Logout</struts-html:link>&nbsp;&nbsp;&nbsp;
					</struts-logic:equal>
					<struts-logic:equal name="HeaderFormular" property="showProfile" value="true">
						<struts-html:link action="/profile">Profil</struts-html:link>&nbsp;&nbsp;&nbsp;
					</struts-logic:equal>
					<struts-logic:equal name="HeaderFormular" property="showGlobalSettings" value="true">
						<struts-html:link action="/editglobalsettings">Einstellungen</struts-html:link>&nbsp;&nbsp;&nbsp;
					</struts-logic:equal>
					<struts-logic:equal name="HeaderFormular" property="showUsers" value="true">
						<struts-html:link action="/viewusers">User</struts-html:link>&nbsp;&nbsp;&nbsp;
					</struts-logic:equal>
					<struts-logic:equal name="HeaderFormular" property="showEvents" value="true">
						<struts-html:link action="/viewevents">Veranstaltungen</struts-html:link>&nbsp;&nbsp;&nbsp;
					</struts-logic:equal>

					<struts-html:link action="/impressum">Impressum</struts-html:link>

					</struts-html:form>
				</div>

				<br />
<!--	Platz für Title eventuell???					-->
				<br />
				</td>
				</tr>
				</table>
<!--	HEAD-END, BODY-START							-->
<!--	ab hier kann die Page frei angepasst werden		-->