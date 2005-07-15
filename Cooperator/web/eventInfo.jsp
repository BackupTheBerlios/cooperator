<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/EventInfoAction">


	<struts-logic:equal name="EventInfoFormular" property="errorMessage" value="">
				<table align="center" width="97%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td align="center">
							<fieldset class="fsnormal"><legend>Navigation</legend>
							<div style="text-align:left">
							&gt;&nbsp;&nbsp;<a href="/Cooperator/viewevents.do">&Uuml;bersicht</a><br />
							<struts-logic:iterate id="element" type="de.tr1.cooperator.model.web.LinkListBean"
												name="EventInfoFormular"  property="naviPathes">
							<struts-bean:write name="element" property="additional" filter="off" />
							&nbsp;&nbsp;&gt;&nbsp;&nbsp;<struts-html:link action="/eventinfo.do"
													paramName="element"
													paramProperty="ID"
													paramId="eventID"><struts-bean:write name="element" property="name" /></struts-html:link><br />
							</struts-logic:iterate>
							</div>
							</fieldset>
					</td>
     			</tr>
				<tr>
					<td align="center"><br />&nbsp;
					</td>
     			</tr>
<!-- Veranstaltungs-Info + Einschreiben + Edit ----------------------------------------------------------------------- -->
     			<tr>
					<td align="center"><fieldset class="fsnormal"><legend>
								<struts-bean:write name="EventInfoFormular" property="eventName" /></legend>
						<table width="97%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td colspan="2">
								<br />
								<strong>Allgemeine Informationen:</strong><br /><br /><br />
								<struts-bean:write name="EventInfoFormular" property="eventInfo" filter="false" />
							</td>
						</tr>
						<struts-logic:equal name="EventInfoFormular" property="eventHasTimes" value="true">
						<tr><td colspan="2"<br /><hr width="100%" /><br />&nbsp;</td></tr>
						<tr>
							<td width="200">Leiter der Veranstaltung:</td>
							<td>
								<struts-logic:notEqual name="EventInfoFormular" property="eventLecturerMail" value="">
									<a href="mailto:<struts-bean:write name="EventInfoFormular" property="eventLecturerMail" />"><struts-bean:write name="EventInfoFormular" property="eventLecturerCompleteName" /></a>
								</struts-logic:notEqual>
								<struts-logic:equal name="EventInfoFormular" property="eventLecturerMail" value="">
									<struts-bean:write name="EventInfoFormular" property="eventLecturerCompleteName" />
								</struts-logic:equal>
							</td>
						</tr>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr>
							<td width="200" valign="top">Die Veranstaltung findet statt:</td>
							<td valign="top">
							<table border="0" cellspacing="0" cellpadding="0" width="80%">
								<thead>
									<tr bgcolor="#cad5e3">
											<td class="listinghead" width="20%" align="center">Zeit</td>
											<td class="listinghead" width="20%" align="center">Tag</td>
											<td class="listinghead" width="25%" align="center">Ort</td>
											<td class="listinghead" width="35%" align="center">&nbsp;</td>
									</tr>
								</thead>
								<tbody>
								<tr>
									<td>
										<img src="pics/pixel.gif" height="2" width="1">
									</td>
								</tr>
								<struts-logic:iterate id="element" type="de.tr1.cooperator.model.mainframe.EventTime"
											name="EventInfoFormular"  property="eventTimes">
								<tr bgcolor="#e5e5f4">
									<td width="100" class="listing" align="center">
										<struts-bean:write name="element" property="clockTime" />
									</td>
									<td width="100" class="listing" align="center">
										<struts-bean:write name="element" property="dayName" />
									</td>
									<td width="100" class="listing" align="center">
										<struts-bean:write name="element" property="location" />
									</td>
									<td width="100" class="listing" align="center">
										<struts-bean:write name="element" property="rhythmAsString" />
									</td>
								</tr>
								<tr>
									<td>
										<img src="pics/pixel.gif" height="2" width="1">
									</td>
								</tr>
								</struts-logic:iterate>
								</tbody>
							</table>
							</td>
						</tr>
						<struts-logic:equal name="EventInfoFormular" property="eventHasMultipleTimes" value="true">
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr>
							<td width="200">Veranstaltungsbeginn ist am:</td>
							<td><struts-bean:write name="EventInfoFormular" property="eventStart" /></td>
						</tr>
						</struts-logic:equal>
						<struts-logic:equal name="EventInfoFormular" property="eventIsSubscribable" value="true">
						<tr>
							<td colspan="2">&nbsp;<br />
								<hr width="100%" />
								<br /><br />
								<strong>Informationen zur Anmeldung:</strong><br /><br /><br />
							</td>
						</tr>
						<tr>
							<td width="200">Anmeldungsbeginn:</td>
							<td><struts-bean:write name="EventInfoFormular" property="eventSubscriptionStart" /></td>
						</tr>
						<tr>
							<td width="200">Anmeldungsende:</td>
							<td><struts-bean:write name="EventInfoFormular" property="eventSubscriptionEnd" /></td>
						</tr>
						<tr>
							<td width="200">Abmeldungsende:</td>
							<td><struts-bean:write name="EventInfoFormular" property="eventUnsubscriptionEnd" /></td>
						</tr>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr>
							<td width="200">Belegung:</td>
							<td><struts-bean:write name="EventInfoFormular" property="eventSubscribedCount" /> / <struts-bean:write name="EventInfoFormular" property="eventSubscribeMax" /></td>
						</tr>
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr>
							<td colspan="2" align="center">
							<struts-logic:equal name="EventInfoFormular" property="eventIsSubscribed" value="true">
								<struts-logic:equal name="EventInfoFormular" property="eventCanUnsubscribe" value="true">
									Sie sind für diese Veranstaltung eingeschrieben und k&ouml;nnen sich auch wieder <a
										href="/Cooperator/EventInfoAction.do?action=unsubscribe&eventID=<struts-bean:write name="EventInfoFormular" property="eventID" />">austragen</a>.
								</struts-logic:equal>
								<struts-logic:equal name="EventInfoFormular" property="eventCanUnsubscribe" value="false">
									Sie sind für diese Veranstaltung eingeschrieben, k&ouml;nnen sich aber nicht wieder austragen, da die Fristen abgelaufen sind.
								</struts-logic:equal>
							</struts-logic:equal>
							<struts-logic:equal name="EventInfoFormular" property="eventIsSubscribed" value="false">
								<struts-logic:equal name="EventInfoFormular" property="eventCanSubscribe" value="true">
									Sie sind für diese Veranstaltung noch nicht eingeschrieben, aber k&ouml;nnen sich jetzt <a
										href="/Cooperator/EventInfoAction.do?action=subscribe&eventID=<struts-bean:write name="EventInfoFormular" property="eventID" />">einschreiben</a>.
								</struts-logic:equal>
								<struts-logic:equal name="EventInfoFormular" property="eventCanSubscribe" value="false">
									<struts-logic:equal name="EventInfoFormular" property="eventCanSwapHere" value="true">
										Sie sind für eine andere Veranstaltung dieser Gruppe eingeschrieben, aber k&ouml;nnen jetzt <a
											href="/Cooperator/EventInfoAction.do?action=swapHere&eventID=<struts-bean:write name="EventInfoFormular" property="eventID" />">hierher wechseln</a>.
									</struts-logic:equal>
									<struts-logic:equal name="EventInfoFormular" property="eventCanSwapHere" value="false">
										Sie sind für diese Veranstaltung nicht eingeschrieben und k&ouml;nnen dies auch nicht, da gewisse Vorraussetzungen nicht erf&uuml;llt sind.
										<struts-logic:equal name="EventInfoFormular" property="eventIsInGroup" value="true">
											<br />Sie sind allerdings in einer anderen Veranstaltung dieser Gruppe eingeschrieben.
										</struts-logic:equal>
									</struts-logic:equal>
								</struts-logic:equal>
							</struts-logic:equal>
							<struts-logic:equal name="EventInfoFormular" property="hasResult" value="true">
								<br /><br />
								<hr /><br />
								Sie haben bisher ein Ergebnis f&uuml;r diese Veranstaltung erreicht von <struts-bean:write name="EventInfoFormular" property="result" />.
							</struts-logic:equal>
							</td>
						</tr>
						</struts-logic:equal><!-- end-is-subscribable -->
						</struts-logic:equal><!-- end-event-has-times -->
<!-- Edit ----------------------------------------------------------------------- -->
						<struts-logic:equal name="EventInfoFormular" property="hasEditRights" value="true">
						<tr><td colspan="2">&nbsp;</td></tr>
						<tr>
							<td colspan="2" align="center">
								<br />
								<hr width="100%" />
								<br />
								<table border="0" cellpadding="0" cellspacing="0" width="100%">
								<tr>
									<td width="25%" align="left">
										<struts-logic:equal name="EventInfoFormular" property="allowedToEdit" value="true">
											<a href="/Cooperator/editevent.do?eventID=<struts-bean:write name="EventInfoFormular" property="eventID" />">Veranstaltung bearbeiten</a>
										</struts-logic:equal>
									</td>
									<td width="25%" align="center">
										<a href="/Cooperator/editevent.do?create=true&parent=<struts-bean:write name="EventInfoFormular" property="eventID" />">Unterveranstaltung einfügen</a>
									</td>
									<td width="25%" align="center">
									<struts-logic:equal name="EventInfoFormular" property="eventIsSubscribable" value="true">
										<struts-logic:equal name="EventInfoFormular" property="allowedToEdit" value="true">
											<struts-html:link action="/viewsubscriptions.do"
													paramName="EventInfoFormular"
													paramProperty="eventID"
													paramId="eventID">Teilnehmer anzeigen</struts-html:link>
										</struts-logic:equal>
									</struts-logic:equal>
									</td>
									<td width="25%" align="right">
										<struts-logic:equal name="EventInfoFormular" property="allowedToEdit" value="true">
											<a href="/Cooperator/deleteevent.do?eventID=<struts-bean:write name="EventInfoFormular" property="eventID" />">Veranstaltung l&ouml;schen</a>
										</struts-logic:equal>
									</td>
								</tr>
								</table>
							</td>
						</tr>
						<tr><td colspan="2"><br /><hr width="100%" /></td></tr>
						</struts-logic:equal><!-- end-can-edit -->
						</table>

						<br />
						<br />
<!-- Sub-Event-Info ----------------------------------------------------------------------- -->
						<table align="center" width="650" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td align="center">
					<struts-logic:iterate id="element" type="de.tr1.cooperator.model.web.SubEventBean"
										name="EventInfoFormular"  property="subEvents">
						<struts-logic:equal name="element" property="isFirst" value="true">
								<fieldset class="fsbright"><legend><struts-bean:write name="element" property="typeName" /></legend>
								<table width="600" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td align="center">
									<br>
										<table border="0" cellpadding="0" cellspacing="0" width="100%">
										<tbody>
										<tr bgcolor="#cad5e3">
											<td class="listinghead" width="270">Name</td>
											<td class="listinghead" width="100">Dozent</td>
											<td class="listinghead">&nbsp;</td>
											<td class="listinghead">
											<struts-logic:equal name="EventInfoFormular" property="atLeastLecturer" value="true">
												<a href="/Cooperator/editevent.do?create=true&parent=<struts-bean:write name="EventInfoFormular" property="eventID"/>&group=-1&type=<struts-bean:write name="element" property="typeID"/>">Neue Unterteilung</a>
											</struts-logic:equal>
											</td>
										</tr>
										<tr><td colspan="4"><hr width="100%" /></td></tr>
						</struts-logic:equal>
										<tr bgcolor="#ebf2fa">
											<td width="270"><struts-html:link action="/eventinfo.do"
													paramName="element"
													paramProperty="ID"
													paramId="eventID"><struts-bean:write name="element" property="name" /></struts-html:link></a></td>
											<td width="100"><struts-bean:write name="element" property="lecturer"/></td>
											<td >
												<struts-logic:equal name="element" property="canUnsubscribe" value="true"><a
													href="/Cooperator/EventInfoAction.do?action=unsubscribe&eventID=<struts-bean:write
														name="EventInfoFormular" property="eventID" />&subEvent=<struts-bean:write
														name="element" property="ID" />">austragen</a>
												</struts-logic:equal>
												<struts-logic:equal name="element" property="canSubscribe" value="true"><a
													href="/Cooperator/EventInfoAction.do?action=subscribe&eventID=<struts-bean:write
														name="EventInfoFormular" property="eventID" />&subEvent=<struts-bean:write
														name="element" property="ID" />">einschreiben</a>
												</struts-logic:equal>
												<struts-logic:equal name="element" property="canChangeHere" value="true"><a
													href="/Cooperator/EventInfoAction.do?action=swapHere&eventID=<struts-bean:write
														name="EventInfoFormular" property="eventID" />&subEvent=<struts-bean:write
														name="element" property="ID" />">hierher wechseln</a>
												</struts-logic:equal>
											</td>
											<td >
												<struts-logic:equal name="element" property="isFirstInGroup" value="true">
													<struts-logic:equal name="EventInfoFormular" property="atLeastLecturer" value="true">
														<a href="/Cooperator/editevent.do?create=true&parent=<struts-bean:write name="EventInfoFormular" property="eventID"/>&group=<struts-bean:write name="element" property="group"/>&type=<struts-bean:write name="element" property="typeID"/>">Neues Gruppen-Event</a>
													</struts-logic:equal>
												</struts-logic:equal>
											</td>
										</tr>
						<struts-logic:equal name="element" property="isLastInGroup" value="true">
							<struts-logic:equal name="element" property="isLast" value="false">
										<tr><td colspan="4"><hr width="100%" /></td></tr>
							</struts-logic:equal>
						</struts-logic:equal>
						<struts-logic:equal name="element" property="isLastInGroup" value="false">
										<tr>
											<td colspan="4">
												<img src="pics/pixel.gif" height="2" width="1">
											</td>
										</tr>
						</struts-logic:equal>
						<struts-logic:equal name="element" property="isLast" value="true">
										</tboby>
										</table>
									</td>
								</tr>
								</table>
								</fieldset>
								<br />&nbsp;
						</struts-logic:equal>
					</struts-logic:iterate>
							</td>
						</tr>
						</table>
					<br />
					<struts-logic:notEqual name="EventInfoFormular" property="subscribeMessage" value="">
					<b><struts-bean:write name="EventInfoFormular" property="subscribeMessage"/></b>
					</struts-logic:notEqual>
					<br />&nbsp;
					</fieldset>
     			</td>
     		</tr>
     	</table>
	</struts-logic:equal>
	<struts-logic:notEqual name="EventInfoFormular" property="errorMessage" value="">
		<div class="errorMessage"><struts-bean:write name="EventInfoFormular" property="errorMessage"/>
		<br />Gehen Sie bitte zur&uuml;ck oder zum <a href="login.do">Login</a></div>
	</struts-logic:notEqual>
</struts-html:form>