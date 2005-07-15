<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/CreateSubscriberListAction">

	<struts-logic:equal name="CreateSubscriberListFormular" property="errorMessage" value="">

		<table width="70%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td>
					<fieldset class="fscreatesubscriberlist"><legend>Einschreibungsliste erzeugen</legend>
					<br />
					<table align="center" width="85%">
						<tr>
							<td valign="top">
								Kopf, links:
							</td>
							<td>
								<struts-html:textarea rows="3" property="headerLeft" styleClass="textarea_350" />
							</td>
						</tr>
						<tr>
							<td valign="top">
								Kopf, rechts:
							</td>
							<td>
								<struts-html:textarea rows="3" property="headerRight" styleClass="textarea_350" />
							</td>
						</tr>
						<tr>
							<td valign="top">
								Informationen:
							</td>
							<td>
								<struts-html:textarea rows="10" property="infoText" styleClass="textarea_350" />
							</td>
						</tr>
						<tr>
							<td valign="top">
								Felder anzeigen:
							</td>
							<td>
								<struts-html:checkbox property="showNumber"			/>Laufende Nummer<br />
								<struts-html:checkbox property="showName"			/>Name/Vorname<br />
								<struts-html:checkbox property="showPersonalNumber"	/>Personalnummer<br />
								<struts-html:checkbox property="showEmail"			/>eMail<br />
								<struts-html:checkbox property="showResult"			/>Ergebnisse<br />
								<struts-html:checkbox property="addInfoField"		/>Notzifeld<br />
								<struts-html:checkbox property="addSignField"		/>Unterschriftsfeld<br />
							</td>
						</tr>
						<struts-html:messages id="error" property="createsubscriberlist.noidentityidentifier">
							<tr>
								<td></td>
								<td>
									<div class="errorMessage"><struts-bean:write name="error"/></div>
								</td>
							</tr>
						</struts-html:messages>
						<tr>
							<td valign="top">
								Sortieren nach:
							</td>
							<td>
								<struts-html:radio property="sortBy" value="1" />Personal-Nummer<br />
								<struts-html:radio property="sortBy" value="2" />Namen<br />
							</td>
						</tr>
						<struts-html:messages id="error" property="createsubscriberlist.badsortby">
							<tr>
								<td></td>
								<td>
									<div class="errorMessage"><struts-bean:write name="error"/></div>
								</td>
							</tr>
						</struts-html:messages>
						<tr>
							<td colspan="2" align="center">
								<!-- this is needed to save the eventID -->
								<struts-html:hidden property="eventIDString" />
								<struts-html:submit value="PDF erzeugen" styleClass="button_100" />
							</td>
						</tr>
					</table>
					</fieldset>
				</td>
			</tr>
		</table>
	</struts-logic:equal>
	<struts-logic:notEqual name="CreateSubscriberListFormular" property="errorMessage" value="">
		<div class="errorMessage"><struts-bean:write name="CreateSubscriberListFormular" property="errorMessage"/>
		<br />Gehen Sie bitte zur&uuml;ck oder zum <a href="login.do">Login</a></div>
	</struts-logic:notEqual>

</struts-html:form>