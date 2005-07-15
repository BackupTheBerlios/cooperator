<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/ProfileAction">


<table align="center" width="500" border="0" cellspacing="0" cellpadding="0">
	<tr>
	<td align="center"><fieldset class="fsnormal"><legend>Profil &auml;ndern</legend>

	<struts-logic:equal name="ProfileFormular" property="errorMessage" value="">
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
		<struts-html:messages id="error" property="profile.FirstName">
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
		<struts-html:messages id="error" property="profile.Surname">
		<tr>
		<td>
			<td align="left">
				<div class="errorMessage"><struts-bean:write name="error"/></div><br />
			</td>
		</td>
		</tr>
		</struts-html:messages>
		<tr><!-- only if user is student -->
			<struts-logic:equal name="ProfileFormular" property="isOnlyStudent" value="true">
			<td> Matrikel Nummer: </td>
			<td>
				<struts-bean:write name="ProfileFormular" property="personalNumber" />
			</td>
			</struts-logic:equal>
		<!-- only shown if User is Docent or Admin -->

			<struts-logic:equal name="ProfileFormular" property="isNoStudent" value="true">
				<td>Personal Number: </td>
				<td><struts-html:text property="personalNumber" styleClass="input_200" /></td>
			</struts-logic:equal>
		</tr>
		<struts-html:messages id="error" property="profile.PersonalNumber">
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
		<tr><!-- if user is no admin -->
			<td> Username: </td>
			<td>
				<struts-bean:write name="ProfileFormular" property="login" />
				<struts-logic:equal name="ProfileFormular" property="isAdmin" value="true">
					<struts-html:hidden property="editFlag" value="true" />
					<struts-html:hidden property="login" />
				</struts-logic:equal>
			</td>
		</tr>
		<struts-html:messages id="error" property="profile.Login">
		<tr>
		<td>
		<td align="left">
				<div class="errorMessage"><struts-bean:write name="error"/></div>
			</td>
		</td>
		</tr>
		</struts-html:messages>
		<tr> <!-- if user is no Admin -->
			<struts-logic:equal name="ProfileFormular" property="isAdmin" value="false">
			<td> Rolle: </td>
			<td>
				<struts-bean:write name="ProfileFormular" property="rightsAsString" />
			</td>
			</struts-logic:equal>
		<!-- only if user is Admin -->
			<struts-logic:equal name="ProfileFormular" property="isAdmin" value="true">
				<td>Rolle:</td>
			<tr>	<td>
				    <td><struts-html:checkbox property="studentRights" /> Student </td>
				</td> </tr>
			<tr>	<td>
				    <td>	 <struts-html:checkbox property="lectRights" /> Dozent</td>
				</td> </tr>
		<!-- if username is admin do not show-->	
			<struts-logic:notEqual name="ProfileFormular" property="login" value="admin">
			<tr>	<td>
				    <td>  <struts-html:checkbox property="adminRights" /> Admin</td>
				</td> </tr>
			</struts-logic:notEqual>
			<struts-logic:equal name="ProfileFormular" property="login" value="admin">
			<tr>	<td>
				    <td>  <struts-html:checkbox disabled="true" property="adminRights" /> Admin</td>
				</td> </tr>
			</struts-logic:equal>
			</struts-logic:equal>
		</tr>
		</tr>
		<tr>
			<td>
			<br />
			</td>
		</tr>
		<tr>
			<td> E-Mail Adresse: </td>
			<td><struts-html:text property="emailAddress" styleClass="input_200" /></td>
		</tr>
		<struts-html:messages id="error" property="profile.EmailAddress">
		<tr>
		<td>
		<td align="left">
				<div class="errorMessage"><struts-bean:write name="error"/></div>
			</td>
		</td>
		</tr>
		</struts-html:messages>
		<struts-html:messages id="error" property="profile.EmailInvalid">
		<tr>
		<td>
		<td align="left">
				<div class="errorMessage"><struts-bean:write name="error"/></div>
			</td>
		</td>
		</tr>
		</struts-html:messages>
		<tr>
			<td colspan="2" align="right">
				<br />


				<br />
				<br />
				<hr width="100%">
				<br />
				<br />
				<br />
			</td>
		</tr>
		<tr>
			<td> altes Passwort: </td>
			<td><struts-html:password property="password" styleClass="input_200" /></td>
		</tr>
		<tr>
			<td> neues Passwort: </td>
			<td><struts-html:password property="newPassword" styleClass="input_200" /></td>
		</tr>
		<tr>
			<td> neues Passwort wiederholen: </td>
			<td><struts-html:password property="againPassword" styleClass="input_200" /></td>
		</tr>
		<tr>
			<td>
			<td>
				<struts-bean:write name="ProfileFormular" property="passError" />
			</td>
			</td>
		<struts-html:messages id="error" property="profile.newPassword">
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
			<td colspan="2" align="center">
				<br />
				<struts-html:submit value="uebernehmen" styleClass="button_100" />
				<!-- nicht vergessen Die PW-Felder nach submit wieder zu leeren! -->
				<br />
			</td>
		</tr>
	<!-- is shown if profile change is succesful -->
		<tr>
							<td align="center" colspan="2"><br/ ><div class="errorMessage"><struts-bean:write name="ProfileFormular" property="succes" /></div></td>
							<br />
		</tr>
	</table>
	</struts-logic:equal>
	<struts-logic:notEqual name="ProfileFormular" property="errorMessage" value="">
		<div class="errorMessage"><struts-bean:write name="ProfileFormular" property="errorMessage"/>
		<br />Gehen Sie bitte zur&uuml;ck zum <a href="login.do">Login</a></div>
	</struts-logic:notEqual>
	</fieldset></td></tr>
</table>

</struts-html:form>
