<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/ViewEventsAction">

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td >
			<fieldset class="fsviewevents"><legend>Veranstaltungen</legend>
			<struts-logic:equal name="ViewEventsFormular" property="errorMessage" value="">
			<br>
			<table border="0" cellpadding="0" cellspacing="0" width="97%">
				<tbody>
					<tr>
						<td>
								Veranstaltungs-Typ:
									<struts-html:select property="selectedEventValue" styleClass="dropdown_200">
										<struts-html:optionsCollection name="ViewEventsFormular" property="selectEventList"
										 value="value" label="label" />
									</struts-html:select>
									&#160;<struts-html:submit property="do" styleClass="button_150"><struts-bean:message key="viewEvents.buttonChangeType" /></struts-html:submit>
						</td>
					</tr>
					<tr>
						<td align="center">
							<br />

							<!-- check if there are some events to show -->
							<struts-logic:equal name="ViewEventsFormular" property="hasEvents" value="true">
								<table border="0" cellpadding="0" cellspacing="0" width="100%">
									<thead>
										<tr bgcolor="#cad5e3">
												<td class="listinghead">Name</td>
												<td class="listinghead" width="25%">Dozent</td>
												<td class="listinghead" width="20%" align="center">eingeschrieben</td>
										</tr>
									</thead>
									<tbody>

										<struts-logic:iterate id="eventlist" type="de.tr1.cooperator.model.web.EventEntry" name="ViewEventsFormular" property="eventList">

											<tr>
												<td>
													<img src="pics/pixel.gif" height="2" width="1">
												</td>
											</tr>

											<tr bgcolor="#e5e5f4">
											
												<struts-logic:equal name="eventlist" property="topic" value="">
													<td class="listing">
														<struts-bean:write name="eventlist" property="shiftIn" filter="false" />

														<struts-html:link action="/eventinfo.do?"
																			paramName="eventlist"
																			paramProperty="idAsString"
																			paramId="eventID">
															<struts-bean:write name="eventlist" property="name" />
														</struts-html:link>
													</td>
													<td class="listing">
														<struts-bean:write name="eventlist" property="lecturerName"/>
													</td>
													<td class="listing" align="center">
														<struts-html:img action="/ImageAction?do=subscriptionImage" paramName="eventlist" paramId="subscriptionState" paramProperty="subscriptionStatusAsString" />
													</td>
												</struts-logic:equal>
												<struts-logic:notEqual name="eventlist" property="topic" value="">
													<td class="listing">
														<struts-bean:write name="eventlist" property="topic" />
													</td>
													<td class="listing">
													</td>
													<td class="listing">
													</td>
												</struts-logic:notEqual>
											</tr>
										</struts-logic:iterate>
									</tbody>
								</table>
					  		</struts-logic:equal>
							<struts-logic:notEqual name="ViewEventsFormular" property="hasEvents" value="true">
								Keine Veranstaltungen vorhanden
							</struts-logic:notEqual>



							<!-- This button has to be dynamicly shown or not -->
							<br />
							<br />
							<br />
							<struts-logic:equal name="ViewEventsFormular" property="isAdmin" value="true">
								<struts-logic:equal name="ViewEventsFormular" property="isLecturer" value="true">
									<struts-html:submit property="do" styleClass="button_220"><struts-bean:message key="viewEvents.buttonCreateEvent" /></struts-html:submit>
								</struts-logic:equal>
							</struts-logic:equal>
							<struts-logic:equal name="ViewEventsFormular" property="isAdmin" value="true">
								<struts-logic:equal name="ViewEventsFormular" property="isLecturer" value="false">
									<struts-html:submit property="do" styleClass="button_220"><struts-bean:message key="viewEvents.buttonCreateEvent" /></struts-html:submit>
								</struts-logic:equal>
							</struts-logic:equal>
							<struts-logic:equal name="ViewEventsFormular" property="isAdmin" value="false">
								<struts-logic:equal name="ViewEventsFormular" property="isLecturer" value="true">
									<struts-html:submit property="do" styleClass="button_220"><struts-bean:message key="viewEvents.buttonCreateEvent" /></struts-html:submit>
								</struts-logic:equal>
							</struts-logic:equal>
							<br />
						</td>
					</tr>
				</tbody>
			</table>
			</struts-logic:equal>
			<struts-logic:notEqual name="ViewEventsFormular" property="errorMessage" value="">
			<div class="errorMessage"><struts-bean:write name="ViewEventsFormular" property="errorMessage"/>
				<br />Gehen Sie bitte zur&uuml;ck zum <a href="login.do">Login</a></div>
			</struts-logic:notEqual>
			</fieldset>
		</td>
	</tr>
</table>

</struts-html:form>