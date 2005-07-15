<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/ViewSubscriptionsAction">

	<struts-logic:equal name="ViewSubscriptionsFormular" property="errorMessage" value="">
		<div align="center">
		<table border="0" cellpadding="0" cellspacing="0" width="650">
		<tr>
			<td align="center">
				<fieldset class="fsnormal"><legend>Navigation</legend>
				<div style="text-align:left">
				&gt;&nbsp;&nbsp;<a href="/Cooperator/viewevents.do">&Uuml;bersicht</a><br />
				<struts-logic:iterate id="element" type="de.tr1.cooperator.model.web.LinkListBean"
									name="ViewSubscriptionsFormular"  property="naviPathes">
				<struts-bean:write name="element" property="additional" filter="off" />
				<struts-logic:notEqual name="element" property="additional2" value="::system::viewSubscribersForEvent::">
					&nbsp;&nbsp;&gt;&nbsp;&nbsp;<struts-html:link action="/eventinfo.do"
											paramName="element"
											paramProperty="ID"
											paramId="eventID"><struts-bean:write name="element" property="name" /></struts-html:link><br />
				</struts-logic:notEqual>
				<struts-logic:equal name="element" property="additional2" value="::system::viewSubscribersForEvent::">
					&nbsp;&nbsp;&gt;&nbsp;&nbsp;<struts-html:link action="/viewsubscriptions.do"
											paramName="element"
											paramProperty="ID"
											paramId="eventID">Einschreibungen verwalten</struts-html:link><br />
				</struts-logic:equal>
				</struts-logic:iterate>
				</div>
				</fieldset>
			</td>
		</tr>
		<tr>
			<td><br />&nbsp;
			</td>
		</tr>
		<tr>
			<td>
				<fieldset class="fsnormal"><legend>Einschreibungsinformationen</legend>
				<table border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="150">
						Einschreibungen:<br />&nbsp;</td>
					<td><struts-bean:write name="ViewSubscriptionsFormular" property="eventSubscribedCount" /> / <struts-bean:write name="ViewSubscriptionsFormular" property="eventSubscribeMax" />
					<struts-logic:equal name="ViewSubscriptionsFormular" property="eventIsFull" value="true">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Achtung, diese Gruppe ist voll!</b>
					</struts-logic:equal><br />&nbsp;
					</td>
				</tr>
				<tr>
					<td>
						Einschreibungs-Start:</td>
					<td><struts-bean:write name="ViewSubscriptionsFormular" property="eventSubscriptionStart" />
					</td>
				</tr>
				<tr>
					<td>
						Einschreibungs-Ende:</td>
					<td><struts-bean:write name="ViewSubscriptionsFormular" property="eventSubscriptionEnd" />
					</td>
				</tr>
				<tr>
					<td>
						Ausschreibungs-Ende:</td>
					<td><struts-bean:write name="ViewSubscriptionsFormular" property="eventUnsubscriptionEnd" />
					</td>
				</tr>
				</table>
			</td>
		</tr>
		<tr>
			<td>&nbsp;
			</td>
		</tr>
		<tr>
			<td align="center">
				<fieldset class="fsnormal"><legend>Teilnehmerliste</legend>
				<br>
					<table border="0" cellpadding="0" cellspacing="0" width="97%">
						<tbody>
						<tr>
							<td align="center" colspan="3">
								<table border="0" cellpadding="0" cellspacing="0" width="600">
									<thead>
									<tr bgcolor="#cad5e3">
										<td class="listinghead">Nummer</td>
										<td class="listinghead">Name</td>
										<td class="listinghead">Vorname</td>
										<td class="listinghead">E-Mail</td>
										<td class="listinghead">Pr&uuml;fungsergebnis</td>
										<td class="listinghead">&nbsp;</td>
									</tr>
									</thead>
									<tbody>
										<!-- the dynamic generated subscriberlist-->
									<struts-logic:iterate id="element" type="de.tr1.cooperator.model.web.UserResult"
										name="ViewSubscriptionsFormular" property="subscriberResultList">
										<tr>
											<td colspan="5">
												<img src="pics/pixel.gif" height="2" width="1">
											</td>
										</tr>
										<tr bgcolor="#e5e5f4">
											<td class="listing"><struts-bean:write name="element" property="personalNumber" /></td>
											<td class="listing"><struts-bean:write name="element" property="surname" /></td>
											<td class="listing"><struts-bean:write name="element" property="firstName" /></td>
											<td class="listing"><a href="mailto:<struts-bean:write name="element" property="emailAddress" />"><struts-bean:write name="element" property="emailAddress" /></a></td>
											<td class="listing" align="center"><struts-bean:write name="element" property="result" /></td>
											<td class="listing"><a href="/Cooperator/ViewSubscriptionsAction.do?action=unsubscribe&eventID=<struts-bean:write name="ViewSubscriptionsFormular" property="eventID" />&login=<struts-bean:write name="element" property="login" />">entfernen</a></td
										</tr>
									</struts-logic:iterate>
									<tr>
										<td colspan="6" align="right">
											<br />
											<struts-html:link action="/createsubscriberlist.do?"
																paramName="ViewSubscriptionsFormular"
																paramProperty="eventID"
																paramId="eventID">Druckbare Version erstellen</struts-html:link>
										</td>
									</tr>
									</tbody>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<br /><br /><hr width="100%" />
							</td>
						</tr>
						<tr>
							<td>
								Neuen Teilnehmer hinzuf&uuml;gen<br /><br />
							</td>
						</tr>
						<tr>
							<td whith="25%">
								Login:
							</td>
							<td>
								<struts-html:hidden name="ViewSubscriptionsFormular" property="eventID" />
								<struts-html:text property="newSubscriber" styleClass="input_150" />
							</td>
							<td align="right">
								<struts-html:submit property="action" value="Teilnehmer hinzufügen" styleClass="button_150" />
							</td>
						</tr>
						<tr>
							<td>
								<struts-html:messages id="error" property="viewsubs.InvalidLogin">
									<br /><div class="errorMessage"><struts-bean:write name="error"/></div>
								</struts-html:messages>
								<struts-logic:notEqual name="ViewSubscriptionsFormular" property="subscribeMessage" value="">
									<br /><div class="errorMessage"><struts-bean:write name="ViewSubscriptionsFormular" property="subscribeMessage"/></div>
								</struts-logic:notEqual>
							</td>
						</tr>
						<tr>
							<td colspan="3">
								<br /><hr width="100%" />
							</td>
						</tr>
						<tr>
							<td colspan="3">
								Neues Prüfungsergebnis hinzufügen / Prüfungsergebnis &auml;ndern<br /><br />
							</td>
						</tr>
						<tr>
							<td>
								Matrikelnummer:
							</td>
							<td colspan="2">
								<struts-html:text property="newSubscriberPersonalNumber" styleClass="input_150" /><br />
							</td>
						</tr>
						<tr>
							<td>
								Bewertung (für ',' bitte '.'):
							</td>
							<td>
								<struts-html:text property="newSubscriberResult" styleClass="input_150" />
							</td>
							<td align="right">
								<struts-html:submit property="action" value="Ergebnis hinzufügen" styleClass="button_150" />
							</td>
						</tr>
						<tr>
							<td>
								<struts-logic:notEqual name="ViewSubscriptionsFormular" property="resultMessage" value="">
								<br /><div class="errorMessage"><struts-bean:write name="ViewSubscriptionsFormular" property="resultMessage"/></div>
								</struts-logic:notEqual>
							</td>
						</tr>
						</tbody>
					</table>


					</fieldset>
				</td>
			</tr>
		</table>
		</div>
	</struts-logic:equal>
	<struts-logic:notEqual name="ViewSubscriptionsFormular" property="errorMessage" value="">
		<div class="errorMessage"><struts-bean:write name="ViewSubscriptionsFormular" property="errorMessage"/>
		<br />Gehen Sie bitte zur&uuml;ck oder zum <a href="login.do">Login</a></div>
	</struts-logic:notEqual>
</struts-html:form>