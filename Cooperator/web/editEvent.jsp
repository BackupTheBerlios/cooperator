<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/EditEventAction">
<input name="wrapEventID" type="hidden" value="0" />
<table align="center" width="70%">
	<tr>
		<td>
			<fieldset class="fseditevent">
				<legend>
					<struts-logic:equal name="EditEventFormular" property="create" value="true">
						Veranstaltung anlegen
					</struts-logic:equal>
					<struts-logic:notEqual name="EditEventFormular" property="create" value="true">
						Veranstaltung editieren
					</struts-logic:notEqual>
				</legend>
			<struts-logic:equal name="EditEventFormular" property="errorMessage" value="">
				<table align="center" width="85%">
					<tr>
						<td colspan="2">
							<br />
							<br />
						</td>
					</tr>
					<tr>
						<td>
							Name der Veranstaltung:
						</td>
						<td >
							<struts-html:text property="eventName" styleClass="input_350" />
 						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							<struts-html:messages id="error" property="editEvent.EventNameInvalid">
								<div class="errorMessage"><struts-bean:write name="error"/></div>
							</struts-html:messages>
						</td>
					</tr>
					<struts-logic:notEqual name="EditEventFormular" property="hideTypeSelection" value="true">
						<tr>
							<td>
								Typ der Veranstaltung:
							</td>
							<td >
								<struts-html:select property="selectedEventType" styleClass="dropdown_350">
									<struts-html:optionsCollection name="EditEventFormular" property="eventTypeList"
									 value="value" label="label" />
								</struts-html:select>
							</td>
						</tr>
						<tr>
							<td>
							</td>
							<td>
								<struts-html:messages id="error" property="editEvent.EventTypeNotChoosen">
									<div class="errorMessage"><struts-bean:write name="error"/></div>
								</struts-html:messages>
							</td>
						</tr>
					</struts-logic:notEqual>
					<struts-logic:equal name="EditEventFormular" property="hideTypeSelection" value="true">
						<tr>
							<td>
								Typ der Veranstaltung:
							</td>
							<td >
								<struts-html:text property="eventType" disabled="true" styleClass="input_350" />
							</td>
						</tr>
					</struts-logic:equal>
					<tr>
						<td>
							Verantwortlicher der Veranstaltung
						</td>
						<td >
							<struts-logic:equal name="EditEventFormular" property="isAdmin" value="true">
								<struts-html:select property="selectedLecturer" styleClass="dropdown_350">
									<struts-html:optionsCollection name="EditEventFormular" property="lecturerList"
									 value="value" label="label" />
								</struts-html:select>
							</struts-logic:equal>
							<struts-logic:notEqual name="EditEventFormular" property="isAdmin" value="true">
								<struts-html:text property="eventLecturer" disabled="true" styleClass="input_350" />
							</struts-logic:notEqual>
	 						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							<struts-html:messages id="error" property="editEvent.LecturereNotChoosen">
								<div class="errorMessage"><struts-bean:write name="error"/></div>
							</struts-html:messages>
						</td>
					</tr>
					<tr>
						<td width="45%" valign="top">
							Beschreibung
						</td>
						<td>
							<struts-html:textarea rows="10" property="eventInfoText" styleClass="textarea_350" />
						</td>
					</tr>
					<tr>
						<td>
							Format der Eingaben:
						</td>
						<td >
							<struts-html:text property="timeStampFormat" disabled="true" styleClass="input_350" />
 						</td>
					</tr>
					<tr>
						<td>
							Veranstaltungsbeginn:
						</td>
						<td >
							<struts-html:text property="eventStart" styleClass="input_350" />
 						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							<struts-html:messages id="error" property="editEvent.EventStartBadFormat">
								<div class="errorMessage"><struts-bean:write name="error"/></div>
							</struts-html:messages>
						</td>
					</tr>
					<tr>
						<td>
							Einschreibungsbeginn:
						</td>
						<td >
							<struts-html:text property="eventSubscriptionStart" styleClass="input_350" />
 						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							<struts-html:messages id="error" property="editEvent.EventSubscriptionStartBadFormat">
								<div class="errorMessage"><struts-bean:write name="error"/></div>
							</struts-html:messages>
						</td>
					</tr>
					<tr>
						<td>
							Einschreibungsende:
						</td>
						<td >
							<struts-html:text property="eventSubscriptionEnd" styleClass="input_350" />
 						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							<struts-html:messages id="error" property="editEvent.EventSubscriptionEndBadFormat">
								<div class="errorMessage"><struts-bean:write name="error"/></div>
							</struts-html:messages>
						</td>
					</tr>
					<tr>
						<td>
							Ausschreibungsende:
						</td>
						<td >
							<struts-html:text property="eventUnsubscriptionEnd" styleClass="input_350" />
 						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							<struts-html:messages id="error" property="editEvent.EventUnsubscriptionEndBadFormat">
								<div class="errorMessage"><struts-bean:write name="error"/></div>
							</struts-html:messages>
						</td>
					</tr>
					<tr>
						<td>
							Max. Teilnehmeranzahl: <br />
						</td>
						<td>
							<struts-html:text property="eventMaxSubscriptions" styleClass="input_350" />
 						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							<struts-html:messages id="error" property="editEvent.EventMaxSubscriptionsBadFormat">
								<div class="errorMessage"><struts-bean:write name="error"/></div>
							</struts-html:messages>
						</td>
					</tr>
					<tr>
						<td colspan=2>
							Termine:
							<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<thead>
									<tr bgcolor="#cad5e3">
											<td class="listinghead">Tag</td>
											<td class="listinghead">Zeit</td>
											<td class="listinghead">Rhytmus</td>
											<td class="listinghead">Ort</td>
											<td class="listinghead"></td>
									</tr>
								</thead>
								<tbody>

									<struts-logic:equal name="EditEventFormular" property="hasEventTimes" value="true">

										<struts-logic:iterate id="eventtimelist" type="de.tr1.cooperator.model.web.EventTimeEntry" name="EditEventFormular" property="eventTimeEntryList">

											<tr>
												<td>
													<img src="pics/pixel.gif" height="2" width="1">
												</td>
											</tr>
											<tr bgcolor="#e5e5f4">
											<td class="listing">
													<struts-bean:write name="eventtimelist" property="dayName"/>
												</td>
												<td class="listing">
													<struts-bean:write name="eventtimelist" property="clockTime"/>
												</td>
												<td class="listing">
													<struts-bean:write name="eventtimelist" property="rhythmAsString"/>
												</td>
												<td class="listing">
													<struts-bean:write name="eventtimelist" property="location"/>
												</td>
												<td class="listing" align="center">
													<struts-html:link action="/EditEventAction.do?do=delete"
																		paramName="eventtimelist"
																		paramProperty="ID"
																		paramId="entry">
																		<img src="pics/trash.gif" align="absmiddle" alt="" />
													</struts-html:link>
												</td>
											</tr>

										</struts-logic:iterate>
									</struts-logic:equal>

									<tr>
										<td>
											<img src="pics/pixel.gif" height="2" width="1">
										</td>
									</tr>

									<tr bgcolor="#e5e5f4">
										<td>
											<struts-html:select property="newTimeSelectedDayName" styleClass="dropdown_80">
												<struts-html:optionsCollection name="EditEventFormular" property="newTimeDayNameList"
												 value="value" label="label" />
											</struts-html:select>
										</td>
										<td>
											<struts-html:text property="newTimeClockTime" styleClass="input_80" />
										</td>
										<td>
											<struts-html:select property="newTimeSelectedRhythm" styleClass="dropdown_150">
												<struts-html:optionsCollection name="EditEventFormular" property="newTimeRhythmList"
												 value="value" label="label" />
											</struts-html:select>
										</td>
										<td>
											<struts-html:text property="newTimeLocation" styleClass="input_80" />
										</td>
										<td>
											<struts-html:submit property="do" styleClass="button_80"><struts-bean:message key="editEvent.buttonAddTimeEntry" /></struts-html:submit>
										</td>
									</tr>
									<tr>
										<td>
										</td>
										<td>
											<struts-html:messages id="error" property="editEvent.addTime.ClockTimeInvalid">
												<div class="errorMessage"><struts-bean:write name="error"/></div>
											</struts-html:messages>
										</td>
										<td>
										</td>
										<td>
											<struts-html:messages id="error" property="editEvent.addTime.LocationInvalid">
												<div class="errorMessage"><struts-bean:write name="error"/></div>
											</struts-html:messages>
										</td>
										<td>
										</td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
					<tr>
						<td valign="top">
							Zulassungsbeschr&auml;nkung:
						</td>
						<td>
							<struts-html:textarea rows="6" property="eventAllowanceList" styleClass="textarea_350" />
						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td>
							<struts-html:messages id="error" property="editEvent.NumberList">
								<div class="errorMessage"><struts-bean:write name="error"/></div>
							</struts-html:messages>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<br />
						</td>
					</tr>
					<tr>
						<td>
						</td>
						<td align="right" >
							<struts-logic:equal name="EditEventFormular" property="create" value="true">
								<struts-html:submit property="do" styleClass="button_150"><struts-bean:message key="editEvent.buttonCreateEvent" /></struts-html:submit>
							</struts-logic:equal>
							<struts-logic:notEqual name="EditEventFormular" property="create" value="true">
								<struts-html:submit property="do" styleClass="button_150"><struts-bean:message key="editEvent.buttonEditEvent" /></struts-html:submit>
							</struts-logic:notEqual>
						</td>
					</tr>
				</table>
			</struts-logic:equal>
			<struts-logic:notEqual name="EditEventFormular" property="errorMessage" value="">
				<div class="errorMessage">
					<struts-bean:write name="EditEventFormular" property="errorMessage"/>
					<br />Gehen Sie bitte zur&uuml;ck zum <a href="login.do">Login</a>
				</div>
			</struts-logic:notEqual>
			</fieldset>
		</td>
	</tr>
</table>

</struts-html:form>