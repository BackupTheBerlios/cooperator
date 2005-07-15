<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/ViewUsersAction">

<table border="0" cellpadding="0" cellspacing="0" width="650">
	<tr>
		<td >
			<fieldset class="fsviewusers"><legend>User</legend>

			<struts-logic:equal name="ViewUsersFormular" property="errorMessage" value="">
			<br />
			<table border="0" cellpadding="0" cellspacing="0" width="97%">
				<tbody>
					<tr>
						<td>
								Auflistung von:
									<struts-html:select property="selectedUserValue" styleClass="dropdown_200">
										<struts-html:optionsCollection name="ViewUsersFormular" property="selectUserList"
										 value="value" label="label" />
									</struts-html:select>
									&#160;<struts-html:submit property="do" styleClass="button_100"><struts-bean:message key="viewUsers.buttonChangeType" /></struts-html:submit>

						</td>
					</tr>
					<tr>
						<td align="center">
							<br />

							<!-- check if there are some users to show , is probably useless, becourse min one admin has to be registerd -->
							<struts-logic:equal name="ViewUsersFormular" property="hasUsers" value="true">
							<!-- now the chosen userlist -->
								<table border="0" cellpadding="0" cellspacing="0" width="100%">
									<thead>
										<tr bgcolor="#cad5e3">
												<td class="listinghead" width="20%">Login</td>
												<td class="listinghead" width="25%">Rolle</td>
												<td class="listinghead" width="25%">Name</td>
												<td class="listinghead" width="20%">Vorname</td>
												<td class="listinghead" align="right"></td>
												<td class="listinghead" align="right"></td>
										</tr>
									</thead>
									<tbody>
									<!-- iterator -->
									<struts-logic:iterate id="element" type="de.tr1.cooperator.model.mainframe.User" name="ViewUsersFormular" property="curUserList">
										<tr>
											<td>
												<img src="pics/pixel.gif" height="2" width="1">
											</td>
										</tr>
										<tr bgcolor="#e5e5f4">
											<td class="listing">
												<struts-html:link action="/ViewUsersAction.do?do=showUser"
																		paramName="element"
																		paramProperty="login"
																		paramId="login">
													<struts-bean:write name="element" property="login"/>
												</struts-html:link>
											</td>
											<td class="listing">
												<struts-bean:write name="element" property="rightsAsString"/>
											</td>
											<td class="listing">
												<struts-bean:write name="element" property="surname"/>
											</td>
											<td class="listing">
												<struts-bean:write name="element" property="firstName"/>
											</td>
											<td class="listing">
												<struts-html:link action="/ViewUsersAction.do?do=overtake"
																		paramName="element"
																		paramProperty="login"
																		paramId="login">einloggen</struts-html:link>&nbsp;&nbsp;&nbsp;&nbsp;
											</td>
											<td class="listing" align="right">
												<struts-html:link action="/ViewUsersAction.do?do=deleteUser"
																		paramName="element"
																		paramProperty="login"
																		paramId="login">
													<img src="pics/trash.gif" align="absmiddle" alt="Delete User" />
												</struts-html:link>
											</td>
										</tr>
									</struts-logic:iterate>
									<!-- iterator end -->
									</tbody>
								</table>
								<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td width="25%">
										<br />
										<struts-logic:equal name="ViewUsersFormular" property="curCountFirst" value="true">
											<struts-html:link action="/ViewUsersAction.do?do=back"
																	paramName="ViewUsersFormular"
																	paramProperty="selectedUserValueCurCountDesiredCount"
																	paramId="selectedUserValueCurCountDesiredCount">
												zur&uuml;ck
											</struts-html:link>
										</struts-logic:equal>
									</td>
									<td align="center">
										<br />
										User <struts-bean:write name="ViewUsersFormular" property="curCount"/> bis <struts-bean:write name="ViewUsersFormular" property="curCountAddDesired"/> von <struts-bean:write name="ViewUsersFormular" property="userCount"/>
										<!--muß noch implementiert werden-->
										<br /><struts-html:text property="desiredCount" styleClass="input_40" /> User anzeigen.
									</td>
									<td width="25%" align="right">
										<br />
										<struts-logic:equal name="ViewUsersFormular" property="curCountLast" value="true">
											<struts-html:link action="/ViewUsersAction.do?do=next"
																	paramName="ViewUsersFormular"
																	paramProperty="selectedUserValueCurCountDesiredCount"
																	paramId="selectedUserValueCurCountDesiredCount">
												weiter
											</struts-html:link>
										</struts-logic:equal>
									</td>
								</tr>
								</table>
							</struts-logic:equal>

							<struts-logic:notEqual name="ViewUsersFormular" property="hasUsers" value="true">
								Keine User vorhanden
							</struts-logic:notEqual>

							<br />
							<br />
							<struts-logic:equal name="ViewUsersFormular" property="isAdmin" value="true">
								<struts-html:submit property="do" styleClass="button_220"><struts-bean:message key="viewUsers.buttonCreateUser" /></struts-html:submit>
							</struts-logic:equal>
						</td>
					</tr>
					<tr>
						<td align="center">
							<br /><br /><br />
							<hr width="100%"><br /><br />
							Zurzeit am System eingeloggte User:<br /><br />
						</td>
					</tr>
					<tr>
						<td align="center">
							<!-- check if there are some logged in users to show , is probably useless, becourse min one admin has to be registerd -->
							<struts-logic:equal name="ViewUsersFormular" property="hasLoggedUsers" value="true">
							<!--  logged-in-userlist -->
					  			<table border="0" cellpadding="0" cellspacing="0" width="300">
									<thead>
										<tr bgcolor="#cad5e3">
												<td class="listinghead">Login</td>
												<td class="listinghead" align="right"></td>
										</tr>
									</thead>
									<tbody>
									<!-- iterator -->

									<struts-logic:iterate id="logged" type="de.tr1.cooperator.model.web.UserSession" name="ViewUsersFormular" property="loggedUserList">

										<tr>
											<td>
												<img src="pics/pixel.gif" height="2" width="1">
											</td>
										</tr>
										<tr bgcolor="#e5e5f4">
											<td class="listing">
												<struts-bean:write name="logged" property="userLogin" />
											</td>
											<td class="listing" align="right">
												<a href="/Cooperator/ViewUsersAction.do?do=killUser&sessid=<struts-bean:write name="logged" property="sessionId" />">User ausloggen</a>
											</td>
										</tr>

									</struts-logic:iterate>
									<!-- iterator end -->
									</tbody>
								</table>
								<br />
								<br />
								<struts-html:submit property="do" styleClass="button_220"><struts-bean:message key="viewUsers.buttonKillAllExeptMe" /></struts-html:submit>
							</struts-logic:equal>

							<struts-logic:notEqual name="ViewUsersFormular" property="hasLoggedUsers" value="true">
								Es sind zu Zeit keine User eingeloggt
							</struts-logic:notEqual>
							<br />
							<br />
							<br />
						</td>
					</tr>
				</tbody>
			</table>
			</struts-logic:equal>
			<struts-logic:notEqual name="ViewUsersFormular" property="errorMessage" value="">
			<div class="errorMessage"><struts-bean:write name="ViewUsersFormular" property="errorMessage"/>
				<br />Gehen Sie bitte zur&uuml;ck zum <a href="login.do">Login</a></div>
			</struts-logic:notEqual>
			</fieldset>
		</td>
	</tr>
</table>

</struts-html:form>