<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/LoginAction">

<table width="330" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td>
			<fieldset class="fslogin"><legend>Login</legend>
			<br />
				<struts-logic:equal name="LoginFormular" property="alreadyLoggedIn" value="false">

					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="90">&nbsp;&nbsp;Username:</td>
							<td>&nbsp;<struts-html:text property="loginName" styleClass="input_200" /></td>
						</tr>
						<struts-html:messages id="error" property="login.username">
							<tr>
								<td></td>
								<td>
									<div class="errorMessage"><struts-bean:write name="error"/></div>
								</td>
							</tr>
						</struts-html:messages><br />
						<tr>
							<td width="90">&nbsp;&nbsp;Password:</td>
							<td>&nbsp;<struts-html:password property="loginPW" styleClass="input_200" /></td>
						</tr>
						<struts-html:messages id="error" property="login.password">
							<tr>
								<td></td>
								<td>
									<div class="errorMessage"><struts-bean:write name="error"/></div>
								</td>
							</tr>
						</struts-html:messages>
						<tr>
							<td colspan="2" align="center">
							<br /><struts-html:submit value="anmelden" styleClass="button_100" />
							</td>
						</tr>
						<tr>
							<td><br /><br /><struts-html:link action="/forgotPass.do">Passwort vergessen</struts-html:link></td>
							<td align="right"><br /><br /><struts-html:link action="/register.do">Als neuer Benutzter registrieren</struts-html:link></td>
						</tr>
					</table>
				</struts-logic:equal>

				<struts-logic:equal name="LoginFormular" property="alreadyLoggedIn" value="true">
					Sie sind bereits eingeloggt. Klicken sie <struts-html:link action="viewevents">hier</struts-html:link>, um fortzufahren.<br />
				</struts-logic:equal>
			</fieldset>
		</td>
	</tr>
</table>

</struts-html:form>