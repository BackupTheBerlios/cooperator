<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/DeleteEventAction">

	<struts-logic:equal name="DeleteEventFormular" property="errorMessage" value="">
		<struts-logic:equal name="DeleteEventFormular" property="message" value="">
		<table align="center" width="40%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
					<fieldset class="mainframe"><legend>Veranstaltung L&ouml;schen</legend>
					<table align="center">
					<struts-logic:equal name="DeleteEventFormular" property="deleted" value="false">
						<tr>
							<td>
								<br />M&ouml;chten Sie die Veranstaltung<br />
								<struts-html:link action="/eventinfo.do"
														paramName="DeleteEventFormular"
														paramProperty="eventID"
														paramId="eventID"><struts-bean:write name="DeleteEventFormular" property="eventName" /></struts-html:link><br />
								wirklich l&ouml;schen?<br />
								<br />
								Gel&ouml;scht werden w&uuml;rde dabei insgesamt:<br />
								<br />
								<struts-logic:iterate id="element" type="de.tr1.cooperator.model.mainframe.Event"
													name="DeleteEventFormular"  property="allEvents">
								<struts-html:link action="/eventinfo.do"
														paramName="element"
														paramProperty="ID"
														paramId="eventID"><struts-bean:write name="element" property="name" /></struts-html:link><br />
								</struts-logic:iterate>
								<br />
							</td>
						</tr>
						<tr>
							<td>
								<br />
							</td>
						</tr>
						<tr>
							<td>
								<table align="center">
									<tr>
										<td width="30%" >
										<a href="/Cooperator/DeleteEventAction.do?eventID=<struts-bean:write
											name="DeleteEventFormular" property="eventID" />&deleted=true">Alles l&ouml;schen</a>
										</td>
										<td width="20%" >
										<struts-html:link action="/eventinfo.do"
														paramName="DeleteEventFormular"
														paramProperty="eventID"
														paramId="eventID">Zur&uuml;ck</struts-html:link>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td>
								<br />
								<br />
							</td>
						</tr>
					</struts-logic:equal>
					<struts-logic:equal name="DeleteEventFormular" property="deleted" value="true">
						<tr>
							<td>
								<br />
								Es wurde alles erfolgreich gel&ouml;scht.<br />
								<a href="/Cooperator/viewevents.do">Hier</a> gelangen Sie wieder zur &Uuml;bersicht.
								<br />&nbsp;
							</td>
						</tr>
					</struts-logic:equal>
					</table>
					</fieldset>
				</td>
			</tr>
		</table>
		</struts-logic:equal>
		<struts-logic:notEqual name="DeleteEventFormular" property="message" value="">
			<div class="errorMessage"><struts-bean:write name="DeleteEventFormular" property="message"/></div>
		</struts-logic:notEqual>
	</struts-logic:equal>
	<struts-logic:notEqual name="DeleteEventFormular" property="errorMessage" value="">
		<div class="errorMessage"><struts-bean:write name="DeleteEventFormular" property="errorMessage"/>
		<br />Gehen Sie bitte zur&uuml;ck oder zum <a href="login.do">Login</a></div>
	</struts-logic:notEqual>
</struts-html:form>