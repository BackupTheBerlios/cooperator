<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/LogoutAction">

<table width="330" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td>
			<fieldset class="fslogin"><legend>Logout</legend>
			<br />
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td align="center">
					Wollen sie sich wirklich abmelden?
				</td>
			</tr>
			<tr>
				<td align="center">
				<br /><struts-html:submit value="Abmelden" styleClass="button_100" />
				</td>
			</tr>
			</table>
			</fieldset>
		</td>
	</tr>
</table>

</struts-html:form>