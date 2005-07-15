<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/ForgotPassAction">


				<table width="330" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td><fieldset class="fslogin"><legend>Passwort per E-mail zuschicken lassen</legend>

						<br />
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="90">&nbsp;&nbsp;Username:</td>
							<td>&nbsp;<struts-html:text property="login" styleClass="input_200" /></td>
						</tr>
						<struts-html:messages id="error" property="forgotPass.Login">
						<tr>
							<td>
								<td align="left">


									<div class="errorMessage"><struts-bean:write name="error"/></div>
										<br />
								</td>
							</td>
						</tr>
						</struts-html:messages>
						<tr>
							<td width="90">&nbsp;&nbsp;Matrikel Nummer:</td>
							<td>&nbsp;<struts-html:text property="personalNumber" styleClass="input_200" /></td>
						</tr>
						<struts-html:messages id="error" property="forgotPass.PersonalNumber">
						<tr>
							<td>
								<td align="left">


									<div class="errorMessage"><struts-bean:write name="error"/></div>
										<br />
								</td>
							</td>
						</tr>
						</struts-html:messages>
						<tr>
							<td colspan="2" align="center">
							<br />
							<struts-html:submit value="senden" styleClass="button_100" />

						</td>
<!-- Der folgende Satz wird nach dem erfolgreichen submit angezeigt. -->
						<tr>
							<td align="center" colspan="2"><br/ ><div class="errorMessage"><struts-bean:write name="ForgotPassFormular" property="emailError" /></div></td>
							<br />
						</tr>
						<tr>
							<td align="center" colspan="2"><br/ ><div class="errorMessage"><struts-bean:write name="ForgotPassFormular" property="userError" /></div></td>
							<br />
						</tr>
						</tr>
						<tr>
							<td ><br /><br />
						</tr>
						<tr>
							<td width="50%"><struts-html:link action="/login">Login</struts-html:link></td>
							<td align=right> <struts-html:link action="/register">neu registrieren</struts-html:link></td>
						</tr>
						</table>
						</fieldset>

					</td>
				</tr>
				<tr>
					<td align="center">
						<br />
						<br />


					</td>
				</tr>

		</table>
</struts-html:form>
