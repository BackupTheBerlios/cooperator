<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/RegisterAction">


<table align="center" width="500" border="0" cellspacing="0" cellpadding="0">
	<tr>
	<td align="center"><fieldset class="mainframe"><legend>Neuen Benutzer anmelden</legend>


	<table width="97%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
			<br /><br />
			</td>
		</tr>
		<tr>
			<td width="150"> Vorname: </td>
			<td><struts-html:text property="firstName" styleClass="input_200" /></td>
		</tr>
		<struts-html:messages id="error" property="register.FirstName">
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
			<td> Nachname: </td>
			<td><struts-html:text property="surname" styleClass="input_200" /></td>
		</tr>
		<struts-html:messages id="error" property="register.Surname">
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


				<td>Personal Number: </td>
				<td><struts-html:text property="personalNumber" styleClass="input_200" /></td>

		</tr>
		<struts-html:messages id="error" property="register.PersonalNumber">
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
			<td>
			<br />
			</td>
		</tr>
		<tr>

				<td> Username: </td>
				<td><struts-html:text property="login" styleClass="input_200" /></td>

		</tr>
		<struts-html:messages id="error" property="register.Login">
		<tr>
		<td>
		<td align="left">
				<div class="errorMessage"><struts-bean:write name="error"/></div>
			</td>
		</td>
		</tr>
		</struts-html:messages>

		<tr>
			<td>
			<br />
			</td>
		</tr>
		<tr>
			<td valign="top"> E-Mail Adresse: </td>
			<td valign="top"><struts-html:text property="emailAddress" styleClass="input_200" />
				<struts-logic:equal name="RegisterFormular" property="useDropDown" value="true">
					<br />@ <struts-html:select property="usedHost" styleClass="dropdown_150">
						<struts-html:optionsCollection name="RegisterFormular" property="userHosts"
						 value="value" label="label" />
					</struts-html:select>
				</struts-logic:equal>
			</td>
		</tr>
		<struts-html:messages id="error" property="register.EmailAddress">
		<tr>
		<td>
		<td align="left">
				<div class="errorMessage"><struts-bean:write name="error"/></div>
			</td>
		</td>
		</tr>
		</struts-html:messages>
		<struts-html:messages id="error" property="register.EmailInvalid">
		<tr>
		<td>
		<td align="left">
				<div class="errorMessage"><struts-bean:write name="error"/></div>
			</td>
		</td>
		</tr>
		</struts-html:messages>
		<tr>
			<td colspan="2" align="center">
				<br />
				<struts-html:submit value="anmelden" styleClass="button_100" />

				<br />
			</td>
		<tr>
			<td colspan="2" align="center">
				<br />
				<div class="errorMessage"><struts-bean:write name="RegisterFormular" property="emailError" /></div>

				<br />
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">

				<div class="errorMessage"><struts-bean:write name="RegisterFormular" property="userError" /></div>

				<br />
			</td>
		</tr>
	</table>


	</fieldset></td></tr>
</table>

</struts-html:form>
