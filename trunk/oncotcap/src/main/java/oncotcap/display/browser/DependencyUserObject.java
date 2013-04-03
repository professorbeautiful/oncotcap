/*
 * Educational Resource for Tumor Heterogeneity
 *             Oncology Thinking Cap
 *
 * Copyright (c) 2003  University of Pittsburgh
 * All rights reserved.
 *
 *  SourceSafe Info:
 *               $Header: $
 *               Revision: $Revision$
 *               Author: $Author$
 *
 * Code Review History:
 *     (mm.dd.yyyy initials)
 *
 * Test History:
 *     (mm.dd.yyyy initials)
 */
package oncotcap.display.browser;

import java.awt.datatransfer.*;
import javax.swing.tree.*;
import java.awt.Color;

import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.action.*;
//import oncotcap.codegeneration.CodeSection;

public class DependencyUserObject extends Object{ 
		StatementBundle sb;
		StatementTemplate st;
		CodeBundle cb;
		OncAction action;
		Object actor;
		Object influence;
		Object recipient;
//		CodeSection cs;
		Object notes;
		String ifStatement;
		Color highlightColor;

		public void setStatementBundle(StatementBundle sb) {
				this.sb = sb;
		}
		public void setStatementTemplate(StatementTemplate st) {
				this.st = st;
		}
		public void setCodeBundle(CodeBundle cb) {
				this.cb = cb;
		}
		public void setAction(OncAction action) {
				this.action = action;
		}
		public void setActor(Object actor) {
				this.actor = actor;
		}
		public void setInfluence(Object influence) {
				this.influence = influence;
		}
		public void setRecipient(Object recipient) {
				this.recipient = recipient;
		}
//		public void setCodeSection(CodeSection cs) {
//				this.cs = cs;
//		}
		public void setNotes(Object notes) {
				this.notes = notes;
		}
		public void setIfStatement(String ifStatement) {
				this.ifStatement = ifStatement;
		}
		public void setHighlightColor(Color color) {
				this.highlightColor = color;
		}
		public StatementBundle getStatementBundle() {
				return this.sb;
		}
		public StatementTemplate getStatementTemplate() {
				return this.st;
		}
		public CodeBundle getCodeBundle() {
				return this.cb;
		}
		public OncAction getAction() {
				return this.action;
		}
		public Object getActor() {
				return this.actor;
		}
		public Object getInfluence() {
				return this.influence;
		}
		public Object getRecipient() {
				return this.recipient;
		}
//		public CodeSection getCodeSection() {
//				return this.cs;
//		}
		public Object getNotes() {
				return this.notes;
		}
		public String getIfStatement() {
				return this.ifStatement;
		}
		public Color getHighlightColor() {
				return this.highlightColor;
		}
		public String toString() {
				//"CodeSection sb " + sb + " st" + st +
				//" cb " + cb + "
				return  "actor " + actor + " influence " + influence 
						+ " recipient " + recipient ;
		}
	
		
}
