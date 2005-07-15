<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/DeleteUserAction">

<table border="0" cellpadding="0" cellspacing="0" width="600">
	<tr>
		<td>
			<struts-logic:equal name="DeleteUserFormular" property="errorMessage" value="">
			<fieldset class="fsviewusers"><legend>User L&ouml;schen</legend>
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
				<tr>
					<td>
						<!-- if there are messages, display them and add a link back to the viewusers-site -->
						<struts-logic:notEqual name="DeleteUserFormular" property="message" value="">
						<br /><br />
						<div class="errorMessage"><struts-bean:write name="DeleteUserFormular" property="message"/></div>
						<br /><br /><br />
						</struts-logic:notEqual>

						<!-- if no messages aviable show delete-infos and buttons -->
						<struts-logic:equal name="DeleteUserFormular" property="message" value="">
						Wollen Sie den User
						<struts-html:link action="/ViewUsersAction.do?do=showUser"
							paramName="DeleteUserFormular"
							paramProperty="deleteUserLogin"
							paramId="login">
							<struts-bean:write name="DeleteUserFormular" property="deleteUserLogin"/>
						</struts-html:link> wirklich l&ouml;schen?

						<!-- if the user is an administrator, notice the deleter -->
						<struts-logic:equal name="DeleteUserFormular" property="deleteIsAdmin" value="true">
							<br /><br /><br />
							<strong>Achtung:</strong> Dieser User ist ein Administrator.
						</struts-logic:equal>

						<!-- if the user is student, list the events he´s subscribed in -->
						<struts-logic:equal name="DeleteUserFormular" property="isSubscribed" value="true">
							<br /><br /><br />
							Dieser User ist Student und würde aus folgenden Veranstaltungen ausgetragen:
							<br /><br />
							<struts-logic:iterate id="element" type="de.tr1.cooperator.model.mainframe.Event" name="DeleteUserFormular"  property="subscribedEvents">
								<struts-html:link action="/eventinfo.do"
									paramName="element"
									paramProperty="ID"
									paramId="eventID"><struts-bean:write name="element" property="name" />
								</struts-html:link><br />
							</struts-logic:iterate>
						</struts-logic:equal>

						<!-- if user is Lecturer, list the events he manages -->
						<struts-logic:equal name="DeleteUserFormular" property="isLecturing" value="true">
							<br /><br /><br />
							Dieser User ist Dozent. Folgenden Veranstaltungen würden ihren Leiter verlieren:
							<br /><br />
							<struts-logic:iterate id="element" type="de.tr1.cooperator.model.mainframe.Event" name="DeleteUserFormular"  property="lecturedEvents">
								<struts-html:link action="/eventinfo.do"
									paramName="element"
									paramProperty="ID"
									paramId="eventID"><struts-bean:write name="element" property="name" />
								</struts-html:link><br />
							</struts-logic:iterate>
						</struts-logic:equal>
						<br /><br /><br />
						</struts-logic:equal>

						<table border="0" cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<!-- show the buttons -->
							<struts-logic:equal name="DeleteUserFormular" property="message" value="">
							<td align="center">
								<a href="/Cooperator/DeleteUserAction.do?login=<struts-bean:write
											name="DeleteUserFormular" property="deleteUserLogin" />&deleted=true">User l&ouml;schen</a>
							</td>
							</struts-logic:equal>
							<td align="center">
								<struts-html:link action="/viewusers.do">Zur&uuml;ck zur Userliste</struts-html:link>
							</td>
						</tr>
						</table>
						<br />
					</td>
				</tr>
			</table>
			</fieldset>
			</struts-logic:equal>
			<struts-logic:notEqual name="DeleteUserFormular" property="errorMessage" value="">
			<div class="errorMessage"><struts-bean:write name="DeleteUserFormular" property="errorMessage"/>
				<br />Gehen Sie bitte zur&uuml;ck zum <a href="login.do">Login</a></div>
			</struts-logic:notEqual>
		</td>
	</tr>
</table>
</struts-html:form>