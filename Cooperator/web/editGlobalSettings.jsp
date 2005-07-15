<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="struts-html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="struts-bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="struts-logic" %>

<struts-html:form action="/EditGlobalSettingsAction">
<table width="540" border="0" cellspacing="0" cellpadding="0" align="center">
	<tr>
		<td>
			<struts-logic:equal name="EditGlobalSettingsFormular" property="hasAdminRights" value="true">
			<fieldset class="fsnormal"><legend>Globale Einstellungen</legend>
			<struts-logic:equal name="EditGlobalSettingsFormular" property="errorMessage" value="">
			<br />
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="3">Diese Einstellungen sind wichtig bei Anmeldungen von neuen Benutzern<br />&nbsp;</td>
			</tr>
			<tr>
				<td width="160">User-Mails erzwingen:</td>
				<td colspan="2"><struts-html:checkbox property="forceUserMails" /></td>
			</tr>
			<struts-logic:equal name="EditGlobalSettingsFormular" property="userHasMails" value="true">
			<tr>
				<td colspan="3">&nbsp;
				</td>
			</tr>
			<struts-logic:iterate id="element" type="de.tr1.cooperator.model.web.DropDownEntry"
						name="EditGlobalSettingsFormular"  property="userMails">
				<tr>
					<struts-logic:equal name="element" property="value" value="0">
						<td>User-Mails:</td>
					</struts-logic:equal>
					<struts-logic:notEqual name="element" property="value" value="0">
						<td>&nbsp;</td>
					</struts-logic:notEqual>
					<td>
						<struts-bean:write name="element" property="label"/>
					</td>
					<td>
						-&nbsp;&nbsp;&nbsp;<struts-html:link action="/EditGlobalSettingsAction.do?action=deleteUserHost"
								paramName="element"
								paramProperty="value"
								paramId="mail">
						<img src="pics/trash.gif" align="absmiddle" alt="diesen Host löschen" />
						</struts-html:link>
					</td>
				</tr>
			</struts-logic:iterate>
			</struts-logic:equal>
			<tr>
				<td valign="top">
					<br />
					Neuer User-Host:
				</td>
				<td valign="top">
					<br />
					<struts-html:text property="newUserMail" styleClass="input_100"/>
					<struts-html:messages id="error" property="global.UserHostInvalid">
						<br /><div class="errorMessage"><struts-bean:write name="error"/></div>
					</struts-html:messages>
					<struts-logic:notEqual name="EditGlobalSettingsFormular" property="userMessage" value="">
						<br /><b><struts-bean:write name="EditGlobalSettingsFormular" property="userMessage" /></b>
						<br />
					</struts-logic:notEqual>
				</td>
				<td valign="top">
					<br /><struts-html:submit property="action" value="Neuer User-Host" styleClass="button_150" />
				</td>
			</tr>
<%
/*
			<tr>
				<td><br />Dozenten-Mails erzwingen:</td>
				<td colspan="2"><br /><struts-html:checkbox property="forceLecturerMails" /></td>
			</tr>
			<struts-logic:equal name="EditGlobalSettingsFormular" property="lecturerHasMails" value="true">
			<tr>
				<td colspan="3">&nbsp;
				</td>
			</tr>
			<struts-logic:iterate id="element" type="de.tr1.cooperator.model.web.DropDownEntry"
									name="EditGlobalSettingsFormular"  property="lecturerMails">
				<tr>
					<struts-logic:equal name="element" property="value" value="0">
						<td>Dozenten-Mails:</td>
					</struts-logic:equal>
					<struts-logic:notEqual name="element" property="value" value="0">
						<td>&nbsp;</td>
					</struts-logic:notEqual>
					<td>
						<struts-bean:write name="element" property="label"/>
					</td>
					<td>
						-&nbsp;&nbsp;&nbsp;<struts-html:link action="./EditGlobalSettingsAction.do?action=deleteLectHost"
								paramName="element"
								paramProperty="value"
								paramId="mail">
						<img src="pics/trash.gif" align="absmiddle" alt="diesen Host löschen" />
						</struts-html:link>
					</td>
				<tr>
			</struts-logic:iterate>
			</struts-logic:equal>
			<tr>
				<td valign="top">
					<br />
					Neuer Dozent-Host:
				</td>
				<td valign="top">
					<br />
					<struts-html:text property="newLecturerMail" styleClass="input_100"/>
					<struts-html:messages id="error" property="global.LectHostInvalid">
						<br /><div class="errorMessage"><struts-bean:write name="error"/></div>
					</struts-html:messages>
					<struts-logic:notEqual name="EditGlobalSettingsFormular" property="lecturerMessage" value="">
						<br /><b><struts-bean:write name="EditGlobalSettingsFormular" property="lecturerMessage" /></b>
						<br />
					</struts-logic:notEqual>
				</td>
				<td valign="top">
					<br /><struts-html:submit property="action" value="Neuer Dozent-Host" styleClass="button_150" />
				</td>
			</tr>
*/ %>
			<tr>
				<td colspan="3"><br />
					<hr width="100%"><br />Folgende Daten sind wichtig, wenn eine Registrierungs-Mail
									versendet werden soll<br />&nbsp;
				</td>
			</tr>
			<tr>
				<td valign="top">
					System-EMail:
				</td>
				<td valign="top">
					<struts-html:text property="systemMail" styleClass="input_100"/>
				</td>
				<td valign="top">
					<struts-html:messages id="error" property="global.SystemInvalid">
						<div class="errorMessage"><struts-bean:write name="error"/></div>
					</struts-html:messages>
				</td>
			</tr>
			<tr>
				<td valign="top">
					Anmeldungs-Betreff:
				</td>
				<td valign="top">
					<struts-html:text property="registerSubject" styleClass="input_100"/>
				</td>
				<td valign="top">
					<struts-html:messages id="error" property="global.SubjectInvalid">
						<div class="errorMessage"><struts-bean:write name="error"/></div>
					</struts-html:messages>
				</td>
			</tr>
			<tr>
				<td colspan="3"><br />
					<hr width="100%"><br />Dies sind alle Typen von Veranstaltungen und Kathegorien:<br />&nbsp;
				</td>
			</tr>
			<tr>
				<td valign="top" rowspan="<struts-bean:write name="EditGlobalSettingsFormular" property="eventTypeCount" />">Event-Typen:</td>
			<struts-logic:iterate id="element" type="de.tr1.cooperator.model.web.LinkListBean"
									name="EditGlobalSettingsFormular" property="eventTypes">
					<td>
						<struts-bean:write name="element" property="name"/>
						<struts-logic:equal name="element" property="additional2" value="true">
							<struts-html:text property="editType" styleClass="input_100"/>
							<struts-html:hidden property="edit" />
							<struts-html:messages id="error" property="global.TypeNameInvalid">
								<br /><div class="errorMessage"><struts-bean:write name="error"/></div>
							</struts-html:messages>
						</struts-logic:equal>
					</td>
					<td>&nbsp;
						-&nbsp;&nbsp;&nbsp;<struts-logic:equal name="element" property="additional" value="false"><struts-html:link
								action="./EditGlobalSettingsAction.do?action=deleteType"
								paramName="element"
								paramProperty="ID"
								paramId="type"><img
									src="pics/trash.gif" align="absmiddle" alt="diesen Typ löschen" /></struts-html:link></struts-logic:equal><struts-logic:equal
									name="element" property="additional" value="true"><img src="/Cooperator/web/pics/false.gif" align="absmiddle" />&nbsp;&nbsp;&nbsp;</struts-logic:equal>
								&nbsp;&nbsp;&nbsp;<struts-html:link
								action="./editglobalsettings.do?action=editType"
								paramName="element"
								paramProperty="ID"
								paramId="editType"><img src="/Cooperator/web/pics/pen.gif" align="absmiddle" /></struts-html:link>
						<struts-logic:equal name="element" property="additional2" value="true">
							<struts-html:submit property="action" value="Umbenennen" styleClass="button_150" />
						</struts-logic:equal>
					</td>
				</tr>
				<tr>
			</struts-logic:iterate>
				<td></td>
				<td colspan="2">
					<struts-logic:notEqual name="EditGlobalSettingsFormular" property="typeNameMessage" value="">
						<b><struts-bean:write name="EditGlobalSettingsFormular" property="typeNameMessage" /></b>
					</struts-logic:notEqual>
				</td>
			</tr>
			<tr>
				<td valign="top">
					<br />Neuer Typ:
				</td>
				<td valign="top">
					<br />
					<struts-html:text property="newType" styleClass="input_100"/>
					<struts-html:messages id="error" property="global.TypeInvalid">
						<br /><div class="errorMessage"><struts-bean:write name="error"/></div>
					</struts-html:messages>
					<struts-logic:notEqual name="EditGlobalSettingsFormular" property="typeMessage" value="">
						<b><struts-bean:write name="EditGlobalSettingsFormular" property="typeMessage" /></b>
						<br />
					</struts-logic:notEqual>
				</td>
				<td valign="top">
					<br /><struts-html:submit property="action" value="Neuer Typ" styleClass="button_150" />
				</td>
			</tr>
			<tr>
				<td colspan="3"><br />
					<hr width="100%">
				</td>
			</tr>
			<tr>
				<td colspan="3" align="center">
				<br /><struts-html:submit property="action" value="Speichern" styleClass="button_100" /><br />&nbsp;
				</td>
			</tr>
			</table>
			</struts-logic:equal>
			<struts-logic:notEqual name="EditGlobalSettingsFormular" property="errorMessage" value="">
			<b><struts-bean:write name="EditGlobalSettingsFormular" property="errorMessage"/>
				<br />Gehen Sie bitte zur&uuml;ck zum <a href="login.do">Login</a></b>
			</struts-logic:notEqual>
			</fieldset>
		</struts-logic:equal>
		<struts-logic:equal name="EditGlobalSettingsFormular" property="hasAdminRights" value="false">
			Sie haben nicht genug Rechte um hier zu sein, also gehen Sie  bitte zur&uuml;ck oder zum <a href="login.do">Login</a>
			<br />
			<br />
			<br />
			<br />
			<br />
			<br />
			<br />
			<br />
			<br />
			<br />
			<br />
		</struts-logic:equal>
		</td>
	</tr>
	<tr>
		<td align="center">
			<br />
			<b><struts-bean:write name="EditGlobalSettingsFormular" property="saveMessage" /></b>
		</td>
	</tr>
</table>

</struts-html:form>