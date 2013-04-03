package oncotcap.display.editor.persistibleeditorpanel;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.event.DocumentEvent.EventType;
import javax.swing.text.*;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.persistible.parameter.*;
import oncotcap.display.editor.EditorFrame;
import oncotcap.engine.ValueMap;
import oncotcap.util.*;

public class StatementTemplateEditorPane extends EditorPanel implements SaveListener
{
	private static final long serialVersionUID = 393820234l;
	
	//used for statementtemplate display, name only
	public static final int NORMAL = 1;
	//used for statementbundle display, displays value if it exists then
	//name
	public static final int WITH_VALUES = 2;
	
	private StatementTemplateEditorPane me = this;
	
	JTextPane editorPane = null;
	private JScrollPane editorPaneSP = null;
	
	private boolean shiftHeld = false;
	private boolean altHeld = false;
	private boolean ctrlHeld = false;
	private StatementTemplateAndParametersPanel stpp = null;
	protected ParameterList parameters;
	private MyUndoManager undoManager = new MyUndoManager();
	private Vector<ParameterDeleteListener> parameterDeleteListeners = new Vector<ParameterDeleteListener>();
	private Vector<ParameterAddListener> parametersAddedListeners = new Vector<ParameterAddListener>();
	private ParameterSelectionListener parameterSelectionListener = null;
	private int editMode = NORMAL;
	protected ValueMap valueMap;
	private StatementTemplate statementTemplate;
	private boolean selected = false;
	private boolean lastEditRemovedParameters = false;
	private Vector<Parameter> deletedParameters = new Vector<Parameter>();
	private MyUndoableEditListener undoableEditListener = new MyUndoableEditListener();
	private boolean deletedOnUndo = false;
	private String pasteBuffer = null;
	
	public static void main(String [] args)
	{
		final StatementTemplateEditorPane editor = new StatementTemplateEditorPane();
		JFrame frame = new JFrame();
		frame.setSize(300,200);
//		editor.setWidth(700);
		editor.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(editor, BorderLayout.CENTER);
		JButton doit = new JButton("Print");
		Box buttonBox = Box.createHorizontalBox();
		frame.getContentPane().add(buttonBox, BorderLayout.SOUTH);
		buttonBox.add(doit);
		doit.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ev)
				{
					editor.printDocument();
				}
			}
		);
		JButton doit2 = new JButton("Insert");
		buttonBox.add(doit2);
		doit2.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ev)
				{
					//editor.insertParameterText("blech1", "ad1", editor.editorPane.getCaretPosition());
					//editor.insertParameterText("blech2", "ad2", editor.editorPane.getCaretPosition());
					//editor.insertParameterText("blech3", "ad3", editor.editorPane.getCaretPosition());
					//editor.setHTMLText("adsfasfd<a id=\"123\">1234</a><a id=\"567\">5678</a>cdef<p></p></p><p>hij</p>");
					editor.setHTMLText("<html>\n					<head>\n					</head>\n					<body>\n						!   1a  <a id=\"888eaae5000000710000010d27db1b00TreatmentSchedule.DayList\" href=\"888eaae5000000710000010d27db1b00TreatmentSchedule.DayList\">Day List</a> <a id=\"888eaae5000000710000010d27db1b00TreatmentSchedule.Duration\" href=\"888eaae5000000710000010d27db1b00TreatmentSchedule.Duration\">Duration</a> <a id=\"888eaae5000000710000010d27db1b00TreatmentSchedule.Courses\" href=\"888eaae5000000710000010d27db1b00TreatmentSchedule.Courses\">Courses</a> <BR>\n						<BR>\n						<BR>\n						asdfasdf<BR>\n						<BR>\n						<a id=\"888eaae5000000720000010d27db3b96TcapFloat.Float\" href=\"888eaae5000000720000010d27db3b96TcapFloat.Float\">Float Value</a>\n					</body>\n				</html>\n");
				}
			}
		);
		
		frame.setVisible(true);
	}
	public StatementTemplateEditorPane()
	{
		init();
	}
	public StatementTemplateEditorPane(StatementTemplate st)
	{
		parameters = st.getParameters();
		init();
	}

	public StatementTemplateEditorPane(StatementBundle sb)
	{
		parameters = sb.getStatementTemplate().getParameters();
		valueMap = sb.getValueMap();
		this.editMode = WITH_VALUES;
		init();
	}

	public StatementTemplateEditorPane(int editMode)
	{
		this.editMode = editMode;
		init();
	}

	public JTextPane getEditorPane()
	{
		return (editorPane);
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
		if (selected) setBorder(BorderFactory.createLineBorder(Color.red, 2));
		else
			setBorder(null);
	}

	public boolean isSelected()
	{
		return (selected);
	}

	public String getHTMLText()
	{
		String out = "<html>\n\t<head>\n\t</head>\n\t<body>\n\t\t";
		out = out + doc2HTML(0, editorPane.getDocument().getLength());
		out = out + "\n\t</body>\n</html>";
		return(out);
	}
	private String doc2HTML(int start, int end)
	{
		String out = "";
		String charOut = "";
		String currentID = null;
		for(int i = start; i <= end; i++)
		{
			String id = getSingleParameterId(i);
			if(id != null && currentID != null && (!id.equalsIgnoreCase(currentID)))
				out = out + "</a><a id=\"" + id + "\" href=\"" + id + "\">";
			else if(id != null && currentID == null)
				out = out + "<a id=\"" + id + "\" href=\"" + id + "\">";
			else if(id == null && currentID != null)
				out = out + "</a>";
			currentID = id;
			
			try{charOut = editorPane.getDocument().getText(i, 1);}
			catch(BadLocationException e){System.err.println("WARNING: Bad location in doc2HTML");}
			if(charOut.equals("\n") && i != end)
				out = out + "<BR>\n\t\t";
			else if(!charOut.equals("\n"))
				out = out + charOut;
		}
		if(currentID != null)
			out = out + "</a>";
		return(out);
	}
	private void copyToClipboard()
	{
		int mark = editorPane.getCaret().getMark();
		int dot = editorPane.getCaret().getDot();
		if(mark != dot)
		{
			String copyTxt = doc2HTML(Math.min(mark, dot), Math.max(mark, dot)-1);
			pasteBuffer = copyTxt;
			//StringSelection copySelection = new StringSelection(copyTxt);
			//Toolkit.getDefaultToolkit().getSystemClipboard().setContents(copySelection, null);
		}
	}
	private void pasteFromClipboard()
	{
		deleteSelected();
		String text2Paste = "";
		//Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
		//try{text2Paste = t.getTransferData(t.getTransferDataFlavors()[0]).toString();}
		//catch(UnsupportedFlavorException e){System.err.println("WARNING: unsupported flavor in StatementTemplateEditorPane.pasteFromClipboard.");}
		//catch(IOException e2){System.err.println("WARNING: io error in StatementTemplateEditorPane.pasteFromClipboard.");}
		if(pasteBuffer != null)
			text2Paste = pasteBuffer;
		insertHTMLText(text2Paste, editorPane.getCaretPosition());
	}
	private void deleteSelected()
	{
		Caret c = editorPane.getCaret();
		int dot = c.getDot();
		int mark = c.getMark();
		if(dot != mark)
		{
			int start = Math.min(dot, mark);
			int len = Math.max(dot, mark) - start;
			try{editorPane.getDocument().remove(start, len);}
			catch(BadLocationException e){System.err.println("WARNING: Bad location error in StatementTemplateEditorPane.deleteSelected(0");}
		}
	}
	public void setHTMLText(String text)
	{
		editorPane.setText("");
		insertHTMLText(text, 0);
		editorPane.revalidate();
		editorPane.repaint();
	}

	public void setStatement(String st)
	{
		String statement = st;
		
		if (statement == null)
			statement = "";

		setHTMLText(statement);
	}

	public void insertHTMLText(String text, int position)
	{
		String tString;
		Vector<DisplayString> displayStrings = new Vector<DisplayString>();
		String html = HTMLParserHelper.removeSpecialChars(HTMLParserHelper.replaceWhiteSpace(HTMLParserHelper.removeHTMLHead(text)));
		Matcher mat = HTMLParserHelper.aTag.matcher(html);
		int sectionStart = 0;
		int tagStart;
		int tagEnd;
		while(mat.find())
		{
			tagStart = mat.start();
			tagEnd = mat.end();
			if(sectionStart < tagStart)
			{
				tString = HTMLParserHelper.convertHTMLToStdText(html.substring(sectionStart, tagStart));
				displayStrings.add(new DisplayString(tString, null));
			}
			if(tagStart < tagEnd)
			{
				tString = html.substring(tagStart, tagEnd);
				String id = getIdFromTag(tString);
				tString = tString = HTMLParserHelper.convertHTMLToStdText(tString);
				displayStrings.add(new DisplayString(tString, id));
			}
			sectionStart = tagEnd;
		}
		if(sectionStart < html.length())
		{
			tString = tString = HTMLParserHelper.convertHTMLToStdText(html.substring(sectionStart, html.length()));
			displayStrings.add(new DisplayString(tString, null));
		}
		int nextPosition = position;
		for(DisplayString dString : displayStrings)
		{
			if(dString.getId() != null)
			{
				insertParameterText(dString.getText(), dString.getId(), nextPosition);
			}
			else
				insertStandardText(dString.getText(), nextPosition);
			nextPosition = nextPosition + dString.getText().length();
		}
	}
	public static final Pattern idProperty = Pattern.compile("id=\\s*\".*?\"");
	private String getIdFromTag(String tag)
	{
		String rVal = "";
		Matcher mat = idProperty.matcher(tag);
		if(mat.find())
		{
			String tVal = tag.substring(mat.start(), mat.end());
			if(tVal.indexOf("\"") >= 0)
			{
				rVal = tVal.substring(tVal.indexOf("\"") + 1, tVal.length());
				if(rVal.endsWith("\""))
					rVal = rVal.substring(0, rVal.length() - 1);
			}
		}
		return(rVal);
	}
	public void edit(Object obj)
	{
		if(obj instanceof StatementTemplate)
		{
			statementTemplate = (StatementTemplate) obj;
			setHTMLText(statementTemplate.getStatement());
			parameters = (statementTemplate.getParameters());
			editorPane.revalidate();
			editorPane.repaint();
			revalidate();
			repaint();
		}		
	}
	public void save()
	{
		if(editMode == NORMAL)
		{
			String text = getHTMLText();
			statementTemplate.setStatement(text);
		}
	}
	public Object getValue()
	{
		return(statementTemplate);
	}
	
	
	private static StyleContext styleContext = StyleContext.getDefaultStyleContext();
	private static Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
	
	private void printDocument()
	{
		System.out.println(getHTMLText());
	}
	private static void printElementAsHTML(Element e)
	{
		if(e.getElementCount() == 0)
		{
			String singleParamID = (String) e.getAttributes().getAttribute("SingleParameterID");
			if(singleParamID != null)
				System.out.print("<a id=\"" + singleParamID + "\" href=\"" + singleParamID + "\">");
			for(int n = e.getStartOffset(); n < e.getEndOffset(); n++)
			{
				try{
					String oc = e.getDocument().getText(n, 1);
					if(! oc.equals("\n"))
						System.out.print(oc);
					else
						if(n < e.getDocument().getLength()) //don't save the last CR, because it is automatically added by the editor
							System.out.print("<BR>\n\t\t");
				}
				catch(Exception ex){System.out.println("Exception");}
			}
			if(singleParamID != null)
				System.out.print("</a>");
		}
		for (int i = 0; i < e.getElementCount(); i++)
			printElementAsHTML(e.getElement(i));
	}
	private void init()
	{
		editorPane = new JTextPane();
		editorPaneSP = new JScrollPane(editorPane);
		setLayout(new BorderLayout());
		editorPaneSP
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		editorPaneSP.getViewport().setBackground(TcapColor.lightBrown);
		setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
		editorPane.setEditable(true);
		editorPane.setBackground(TcapColor.lightBrown);
		editorPane.setAutoscrolls(false);
		editorPane.setNavigationFilter(new SingleParameterNavigator());
		Document doc = editorPane.getDocument();
		doc.addUndoableEditListener(undoableEditListener);
		editorPane.addMouseMotionListener(new MouseMotionListen());
		editorPane.addMouseListener(new MouseListen());
		editorPane.addKeyListener(new SBKeyListen());
		add(editorPaneSP, BorderLayout.CENTER);
	}

	/**
	 * Inserts a parameter at the current cursor position
	 */
	public void insertParameter(Parameter parameter)
	{
		int position = editorPane.getCaretPosition();
		ParameterAddedUndoableEdit pae = new ParameterAddedUndoableEdit(parameter, position);
		undoManager.addEdit(pae);
		insertParameter(parameter, position);
	}
	
	private void insertParameter(Parameter parameter, int position)
	{
		Iterator singleParams = parameter.getSingleParameters();
		int localPosition = position;
		boolean first = true;
		for(SingleParameter singleParam : parameter.getSingleParameterList().getValues())
		{
			if(!first)
			{
				try{editorPane.getDocument().insertString(localPosition, " ", defaultStyle);}
				catch(BadLocationException ble){}
				localPosition++;
			}
			else
				first = false;
			localPosition = localPosition + insertSingleParameter(singleParam, localPosition);
		}
	}

	private int insertSingleParameter(SingleParameter singleParam, int position)
	{
		String displayTxt = getDisplayText(singleParam);
		insertParameterText(displayTxt, singleParam.getID(), position);
		return(displayTxt.length());
	}

	private String getDisplayText(SingleParameter singleParam)
	{
		String displayTxt;
		if(! (editMode == WITH_VALUES))
			displayTxt = singleParam.getDisplayName();
		else
			displayTxt = valueMap.getSBDisplayValue(singleParam);
		return(displayTxt);
	}		
	private void insertParameterText(String name, String id, int position)
	{
		Style s = (Style) styleContext.getStyle(StyleContext.DEFAULT_STYLE).copyAttributes();
		StyleConstants.setUnderline(s, true);
		StyleConstants.setForeground(s, Color.BLUE);
		s.addAttribute("SingleParameterID", id);
		try{editorPane.getDocument().insertString(position, name, s);}
		catch (BadLocationException e){System.err.println("WARNING: Bad location in StatementTemplateEditorPane.insertParameterText()");};
	}

	private void insertStandardText(String text, int position)
	{
		try{editorPane.getDocument().insertString(position, text, defaultStyle);}
		catch (BadLocationException e){System.err.println("WARNING: Bad location in StatementTemplateEditorPane.insertStandardText()");};
	}

	private AttributeSet getAttributeSet(int position)
	{
		Document d = editorPane.getDocument();
		Element root = d.getDefaultRootElement();
		return (getAttributeSet(position, root));
	}

	private AttributeSet getAttributeSet(int position, Element element)
	{
		// if it is a bottom level element, check to see if it contains the
		// position we're looking for
		if (element.getElementCount() == 0)
		{
			if (position >= element.getStartOffset()
					&& position < element.getEndOffset())
			{
				return (element.getAttributes());
			}
		} 
		else
		{
			// otherwise, check any elements contained under this element
			for (int i = 0; i < element.getElementCount(); i++)
			{
				AttributeSet as = getAttributeSet(position, element
						.getElement(i));
				if (as != null)
					return (as);
			}
		}
		return (null);
	}

	private boolean mouseOverLink(MouseEvent e)
	{
		int pos = editorPane.viewToModel(e.getPoint());
		if (getSingleParameterId(pos) != null)
			return (true);
		else
			return (false);
	}

	private String getSingleParameterId(int dot)
	{
		AttributeSet as = getAttributeSet(dot);
		if (as == null)
			return(null);
		String singleParamId = (String) as.getAttribute("SingleParameterID");
		return(singleParamId);
	}
	private SingleParameter getSingleParameter(int position)
	{
		return(parameters.getSingleParameterByID(getSingleParameterId(position)));
	}
	private Parameter getParameter(int cursorPosition)
	{
		String singleParamId = getSingleParameterId(cursorPosition);
		if(singleParamId != null)
			return((Parameter) parameters.getParameterByID(singleParamId));
		else
			return(null);
	}
	
	private void conditionallyDeleteSingleParams(Vector params, KeyEvent e)
	{
		Vector<Parameter> paramsToDelete = new Vector<Parameter>();
		Object obj;
		Parameter p;
		Iterator it = params.iterator();
		while (it.hasNext())
		{
			obj = it.next();
			if (obj instanceof SingleParameter)
			{
				p = parameters.getParameter((SingleParameter) obj);
				if (p != null && (!paramsToDelete.contains(p)))
					paramsToDelete.add(p);
			}
		}
		deleteParameters(paramsToDelete);
	}
	private Vector<Parameter> getParametersInSelection()
	{
		return(getContainingParameters(getSingleParametersInSelection()));
	}
	private Vector<Parameter> getContainingParameters(Vector<SingleParameter> singleParams)
	{
		Vector<Parameter> params = new Vector<Parameter>();
		for(SingleParameter singleParam : singleParams)
		{
			Parameter p = parameters.getParameter(singleParam);
			if(p != null && !params.contains(p))
				params.add(p);
		}
		return(params);
	}
	private Vector<SingleParameter> getSingleParametersInSelection()
	{
		String id;
		SingleParameter p;
		Vector<SingleParameter> containedParams = new Vector<SingleParameter>();
		int selStart = Math.min(editorPane.getSelectionStart(), editorPane.getSelectionEnd());
		int selEnd = Math.max(editorPane.getSelectionEnd(), editorPane.getSelectionStart());
		for(int n = selStart; n<=selEnd; n++)
		{
			if((id = getSingleParameterId(n)) != null)
			{
				p = parameters.getSingleParameterByID(id);
				if(!containedParams.contains(p))
					containedParams.add(p);
			}
		}
		return(containedParams);
	}
	private void deleteParameter(Parameter param)
	{
		Vector<Parameter> pVec = new Vector<Parameter>();
		pVec.add(param);
		deleteParameters(pVec);
	}
	
	private void deleteParameters(Vector<Parameter> params)
	{
			deletedParameters.clear();
			deletedParameters.addAll(params);
			lastEditRemovedParameters = true;					
			fireParameterDeleted();
	}

	public void removeParameter(Parameter param)
	{
		Iterator it = param.getSingleParameterList().getIterator();
		while (it.hasNext())
			removeSingleParameter((SingleParameter) it.next());

	}

	public void updateParameter(Parameter param)
	{
		if (param != null)
		{
			Iterator it = param.getSingleParameterList().getIterator();
			while (it.hasNext())
			{
				SingleParameter sp = (SingleParameter) it.next();
				updateSingleParameter(sp);
			}

		}
	}

	public void updateSingleParameter(SingleParameter singleParam)
	{
		Vector<Integer> foundLocations = new Vector<Integer>();
		int n;
		String id = singleParam.getID().toString();
		String tName;
		n = 0;
		boolean found = false;
		int oldParamLength = 0;
		while(n < editorPane.getDocument().getLength())
		{
			tName = getSingleParameterId(n);
			if(!found)
			{
				if(tName != null && tName.equalsIgnoreCase(id))
				{
					foundLocations.add(n);
					oldParamLength = 1;
					found = true;
				}					
			}
			else if(tName == null || (tName != null && ! tName.equalsIgnoreCase(id)))
				found = false;
			else
				oldParamLength++;
			
			n++;
		}
		removeSingleParameter(singleParam);
		int newParamLength = getDisplayText(singleParam).length();
		int nameLengthDiff = oldParamLength - newParamLength;
		int offset = 0;
		for(int location : foundLocations)
		{
			insertSingleParameter(singleParam, location - offset);
			offset = offset + nameLengthDiff;
		}
		parameters.fireTableDataChanged();
	}

	public void removeSingleParameter(SingleParameter singleParam)
	{
		String singleParamId = singleParam.getID();
		for(int n = 0; n < editorPane.getDocument().getLength(); n++)
		{
			String id = getSingleParameterId(n);
			if(id != null && id.equalsIgnoreCase(singleParamId))
			{
				try{editorPane.getDocument().remove(n,1); n--;}
				catch(BadLocationException e){System.err.println("WARNING: Bad location error in StatementTemplateEditorPane.removeSingleParameter");}
			}
		}
	}

	public void setStatementTemplateAndParametersPanel(
			StatementTemplateAndParametersPanel pan)
	{
		stpp = pan;
	}

	public void addParameterDeletedListener(ParameterDeleteListener pdl)
	{
		parameterDeleteListeners.add(pdl);
	}

	public void fireParameterDeleted()
	{
		for(ParameterDeleteListener pdl : parameterDeleteListeners)
			pdl.parametersDeleted(deletedParameters);
		if(!deletedOnUndo)
			this.undoableEditListener.undoableEditHappened(new ParameterDeleteUndoableEdit(deletedParameters));
		else
			deletedOnUndo = false;
	}
	public void addParameterAddedListener(ParameterAddListener pal)
	{
		parametersAddedListeners.add(pal);
	}

	public void fireParameterAdded(Collection<Parameter> addedParameters)
	{
		for(ParameterAddListener pal : parametersAddedListeners)
			pal.parametersAdded(addedParameters);
	}

	public void undo()
	{
		if(unhandledUndoableEdit != null)
		{
			undoManager.addEdit(unhandledUndoableEdit);
			unhandledUndoableEdit = null;
		}
		undoManager.undo();
	}

	public void setParameterSelectionListener(ParameterSelectionListener listener)
	{
		parameterSelectionListener = listener;
	}

	public int getBottom()
	{
		return ((int) (getLocation().getY() + getHeight()) + 5);
	}

	public int getTop()
	{
		return ((int) (getLocation().getY() - 5));
	}

	public int setWidth(int width)
	{
		int height = 100;
		
		editorPane.setSize(width, 100);
		editorPane.setMinimumSize(new Dimension(width, 30));
		editorPane.setMaximumSize(new Dimension(width, Short.MAX_VALUE));

		height = (int) editorPaneSP.getViewport().getPreferredSize().getHeight();
		editorPaneSP.setSize(width + 8, height + 8);
		setSize(width + 10, height + 10);
		return(width);
	}

	public void setPreferredSize(Dimension d)
	{
		super.setPreferredSize(d);
		if(editorPaneSP != null)
			editorPaneSP.setPreferredSize(d);

		if(editorPane != null)
			editorPane.setMinimumSize(d);
	}
	public void setMinimumSize(Dimension d)
	{
		super.setMinimumSize(d);
		if(editorPaneSP != null)
			editorPaneSP.setMinimumSize(d);

		if(editorPane != null)
			editorPane.setMinimumSize(d);
	}
	public void setMaximumSize(Dimension d)
	{
		super.setMaximumSize(d);
		if(editorPaneSP != null)
			editorPaneSP.setMaximumSize(d);
		
		if(editorPane != null)
			editorPane.setMinimumSize(d);
	}
	public void setSize(Dimension d)
	{
		super.setSize(d);
		if(editorPaneSP != null)
			editorPaneSP.setSize(d);

		if(editorPane != null)
			editorPane.setMinimumSize(d);
	}
	public void setSize(int w, int h)
	{
		super.setSize(w, h);
		if(editorPaneSP != null)
			editorPaneSP.setSize(w, h);

		if(editorPane != null)
			editorPane.setMinimumSize(new Dimension(w,h));
	}
	public void resize(Dimension d)
	{
		super.resize(d);
		if(editorPaneSP != null)
			editorPaneSP.setSize(d);

		if(editorPane != null)
			editorPane.setMinimumSize(d);
	}
	public void resize(int x, int y)
	{
		super.resize(x,y);
		if(editorPaneSP != null)
			editorPaneSP.setSize(x,y);

		if(editorPane != null)
			editorPane.setMinimumSize(new Dimension(x, y));
	}

	public void objectSaved(SaveEvent e)
	{
		Object p = e.getSavedObject();
		if (p != null && p instanceof Parameter)
		{
			Parameter origParam = ((Parameter) p).getOriginalSibling();
			updateParameter(origParam);
		}
	}
	public void objectDeleted(SaveEvent e){}
	
	private Position.Bias biasOverride = null;
	
	class SingleParameterNavigator extends NavigationFilter
	{
		public int getNextVisualPositionFrom(JTextComponent text, int pos, Position.Bias bias, int direction,
				Position.Bias[] biasRet) throws BadLocationException
		{
			if(direction == 7 || direction == 1)
				biasOverride = Position.Bias.Backward;
			
			if(direction == 3 || direction == 5)
				biasOverride = Position.Bias.Forward;
			
			return(super.getNextVisualPositionFrom(text, pos, bias, direction, biasRet));
		}
		public void moveDot(NavigationFilter.FilterBypass fb, int dot, Position.Bias bias)
		{
			Position.Bias inBias; // = setBias(dot);
			if(biasOverride != null)
			{
				inBias = biasOverride;
				biasOverride = null;
			}
			else if( dot <= editorPane.getCaret().getMark())
				inBias = Position.Bias.Backward;
			else
				inBias = Position.Bias.Forward;
			
			fb.moveDot(nextAvailableSpace(dot, inBias), bias);
		}
		public void  setDot(NavigationFilter.FilterBypass fb, int dot, Position.Bias bias)
		{		
			Position.Bias inBias; // = setBias(dot);
			if(biasOverride != null)
			{
				inBias = biasOverride;
				biasOverride = null;
			}
			else if( dot <= editorPane.getCaretPosition())
				inBias = Position.Bias.Backward;
			else
				inBias = Position.Bias.Forward;
			fb.setDot(nextAvailableSpace(dot, inBias), bias);
		}
		private Position.Bias setBias(int dot)
		{
			Position.Bias rBias;
			if(biasOverride != null)
			{
				rBias = biasOverride;
				biasOverride = null;
			}
			else if( dot <= editorPane.getCaretPosition())
				rBias = Position.Bias.Backward;
			else
				rBias = Position.Bias.Forward;
			
			return(rBias);
		}
		private int nextAvailableSpace(int dot, Position.Bias bias)
		{
			int tDot = dot;
			if(bias == Position.Bias.Backward)
			{
				while(!isGoodSpace(tDot))
					tDot--;
				
				return(tDot);
			}
			else if(bias == Position.Bias.Forward)
			{
				while(!isGoodSpace(tDot))
					tDot++;
			}
			return(tDot);
		}
		private boolean isGoodSpace(int dot)
		{
			int docLength = editorPane.getDocument().getLength();
			if(dot == 0 || dot == docLength)
				return(true);
			String singleParamId = getSingleParameterId(dot);
			if(singleParamId == null)
				return(true);
			else
			{
				String prevSingleParamId = getSingleParameterId(dot - 1);
				if(prevSingleParamId == null || !(prevSingleParamId.equalsIgnoreCase(singleParamId)))
					return(true);
				else
					return(false);
			}	
		}
		
	}

	class DisplayString
	{
		private String text;

		private String id;

		DisplayString(String text, String id)
		{
			this.text = text;
			this.id = id;
		}

		String getId()
		{
			return (id);
		}

		String getText()
		{
			return (text);
		}
	}

	private class MouseListen implements MouseListener
	{
		public void mouseClicked(MouseEvent e)
		{
			int pos;
			final Parameter inP;
			// Parameter editedParam;
			ValueMap editedValueMap;
			Point p;
			Object attrib;
			if (mouseOverLink(e))
			{
				p = e.getPoint();
				pos = editorPane.viewToModel(p);
				inP = getParameter(pos);
				if (editMode == NORMAL)
				{
					EditorPanel ep = EditorFrame.showEditor(inP);
					if (ep instanceof ParameterEditor)
						((ParameterEditor) ep).enableDescriptionEditing(true);
				}
				else
				{
					Parameter clonedParam;
					clonedParam = valueMap.getClonedParameter(inP);
					if (clonedParam != null)
					{
						clonedParam.addSaveListener(me);
						EditorPanel ep = EditorFrame.showEditor(clonedParam);
						if (ep instanceof ParameterEditor)
							((ParameterEditor) ep)
									.enableDescriptionEditing(false);
					}
				}
			}
		}

		public void mousePressed(MouseEvent e){}
		public void mouseReleased(MouseEvent e){}
		public void mouseEntered(MouseEvent e){}
		public void mouseExited(MouseEvent e){}
	}

	private class MouseMotionListen implements MouseMotionListener
	{
		public void mouseDragged(MouseEvent e)
		{
		}

		public void mouseMoved(MouseEvent e)
		{
			if (mouseOverLink(e))
			{
				editorPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				if (parameterSelectionListener != null)
					parameterSelectionListener.selected(getSingleParameter(editorPane.viewToModel(e.getPoint())));
			}
			else
			{
				editorPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		}
	}
	private class SBKeyListen implements KeyListener
	{
		
		public void keyPressed(KeyEvent e)
		{

			if(editorPane.getCaret().getDot() == editorPane.getCaret().getMark())
				editorPane.setCharacterAttributes(defaultStyle ,true);
			
			String pName;
			String pTName;
			int caretPosition;
	   
			if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			{
				caretPosition = editorPane.getCaretPosition();
				checkForParametersToDelete(e, caretPosition - 1);
			}
			else if(e.getKeyCode() == KeyEvent.VK_DELETE )
			{
				caretPosition = editorPane.getCaretPosition();
				checkForParametersToDelete(e, caretPosition);
			}
			else if(e.getKeyCode() == KeyEvent.VK_A && altHeld)
			{
				if(stpp != null)
				{
					stpp.addParameter();
					altHeld = false;
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_C && ctrlHeld)
			{
				copyToClipboard();
				e.consume();
			}
			else if(e.getKeyCode() == KeyEvent.VK_V && ctrlHeld)
			{
				pasteFromClipboard();
				e.consume();
			}
			else if(e.getKeyCode() == KeyEvent.VK_X && ctrlHeld)
			{
				copyToClipboard();
				deleteSelected();
				e.consume();
			}
			else if(e.getKeyCode() == KeyEvent.VK_Z && ctrlHeld)
			{
				try{undo();}
				catch(CannotUndoException e2){
					System.out.println("WARNING: Can not undo exception. [StatementTemplateEditorPanel.KeyListener.keyPressed()]");
					System.out.println(e2.getMessage());
					e2.printStackTrace();
					if(e2.getCause() != null)
					{
						System.out.println("CAUSE: ");
						e2.getCause().printStackTrace();
					}
				}
			}
			else if(isVisibleCharacter(e))
			{
				if(this.checkForParameterInDeletedSelection(e))
				{
					e.consume();
					if(e.getKeyCode() == KeyEvent.VK_TAB)
					{
						try{editorPane.getDocument().insertString(editorPane.getCaretPosition(), "\t", defaultStyle);}
						catch(BadLocationException ble){}
					}
				}
			}
			
			int kc = e.getKeyCode();
			if(kc == KeyEvent.VK_SHIFT && !shiftHeld)
			{
				shiftHeld = true;
			}
			if(kc == KeyEvent.VK_ALT)
			{
				altHeld = true;
			}
			if(kc == KeyEvent.VK_CONTROL)
			{
				ctrlHeld = true;
			}			
		}
		private void checkForParametersToDelete(KeyEvent e, int caretPosition)
		{
			if(! checkForParameterInDeletedSelection(e))
			{
				if(getSingleParameterId(caretPosition) != null)
				{
					int st = editorPane.getSelectionStart();
					int end = editorPane.getSelectionEnd();
					int selStart = Math.min(st, end);
					int selEnd = Math.max(st, end);
					Parameter p = getParameter(caretPosition);
					deleteParameter(p);
					e.consume();
					try{editorPane.getDocument().remove(selStart, selEnd - selStart);}
					catch(BadLocationException ble){}
					deleteParameterText(p);
				}
			}
		}
		private boolean checkForParameterInDeletedSelection(KeyEvent e)
		{
			int st = editorPane.getSelectionStart();
			int end = editorPane.getSelectionEnd();
			int selStart = Math.min(st, end);
			int selEnd = Math.max(st, end);
			if(selStart != selEnd)
			{
				Vector<SingleParameter> containedParams;
				containedParams = getSingleParametersInSelection();
				conditionallyDeleteSingleParams(containedParams, e);
				e.consume();
				try{editorPane.getDocument().remove(selStart, selEnd - selStart);}
				catch(BadLocationException ble){}
				int n;
				Vector<Parameter> params = new Vector<Parameter>();
				for(SingleParameter singleParam : containedParams)
				{
					if(!params.contains(singleParam.getParameter()))
					{
						params.add(singleParam.getParameter());
					}
				}
				for(Parameter p : params)
					deleteParameterText(p);
				return(true);
			}
			else
				return(false);
		}
		public void keyReleased(KeyEvent e)
		{
			if(e.getKeyCode() == KeyEvent.VK_SHIFT)
			{
				shiftHeld = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_ALT)
			{
				altHeld = false;
			}
			if(e.getKeyChar() == KeyEvent.VK_CONTROL)
			{
				ctrlHeld = false;
			}
		}
		public void keyTyped(KeyEvent e){}
		private boolean isMovementKey(KeyEvent e)
		{
			int kc = e.getKeyCode();
			if( kc == KeyEvent.VK_RIGHT ||
				kc == KeyEvent.VK_DOWN ||
				kc == KeyEvent.VK_UP ||
				kc == KeyEvent.VK_LEFT ||
				kc == KeyEvent.VK_END ||
				kc == KeyEvent.VK_HOME ||
				kc == KeyEvent.VK_PAGE_UP ||
				kc == KeyEvent.VK_PAGE_DOWN ||
				kc == KeyEvent.VK_KP_UP ||
				kc == KeyEvent.VK_KP_RIGHT ||
				kc == KeyEvent.VK_KP_DOWN ||
				kc == KeyEvent.VK_KP_LEFT )
					return(true);
			else
				return(false);
		}
		private boolean isVisibleCharacter(KeyEvent e)
		{
			int code = e.getKeyCode();
			if( 
				(code == 9) ||
				(code == 10) ||
				(code == 32) ||
				(code >= 44 && code <= 57) ||
				(code == 59) ||
				(code == 61) ||
				(code >= 65 && code <= 93) ||
				(code >= 96 && code <= 111) ||
				(code == 192) ||
				(code == 222)
			)
				return(true);
			else
				return(false);
		}
	}
	private void deleteParameterText(Parameter param)
	{
		deleteParameterText(param.getSingleParameterList().getValues());
	}
	private void deleteParameterText(Collection<SingleParameter> singleParams)
	{
		SortedList<Integer> toRemove = new SortedList<Integer>(new ReverseIntegerCompare());
		for(SingleParameter singleParam : singleParams)
		{
			for(int n = 0 ; n < editorPane.getDocument().getLength(); n++)
			{
				String singleParamID = getSingleParameterId(n);
				String pi = singleParam.getID();
				if(singleParamID != null && singleParamID.equals(singleParam.getID()) )
					toRemove.add(n);
			}
		}
		
		for(int n : toRemove)
		{
			try{editorPane.getDocument().remove(n, 1);}
			catch(BadLocationException ble){System.out.println("Bad location trying to delete a parameter.");}
		}
	}
	private UndoableEdit unhandledUndoableEdit = null;
	private class MyUndoableEditListener implements UndoableEditListener
	{
		public void undoableEditHappened(UndoableEditEvent e)
		{
			UndoableEdit ed = e.getEdit();
			undoableEditHappened(ed);
		}
		public void undoableEditHappened(UndoableEdit ed)
		{	
			if(ed instanceof ParameterDeleteUndoableEdit)
			{
				if(unhandledUndoableEdit != null)
				{
					undoManager.addEdit(unhandledUndoableEdit);
					undoManager.addEdit(ed);
					unhandledUndoableEdit = null;
				}
				else
					undoManager.addEdit(ed);
			}
			else if(unhandledUndoableEdit != null)
			{
				undoManager.addEdit(unhandledUndoableEdit);
				unhandledUndoableEdit = ed;
			}
			else
				unhandledUndoableEdit = ed;
		}
	}
	public interface ParameterSelectionListener
	{
		public void selected(SingleParameter param);
	}
	
	class StatementTransferFlavor extends DataFlavor
	{
		static final long serialVersionUID = 293723487234l;
		
		GUID [] parameterIds;
	}
	
	
	class ParameterAddedUndoableEdit implements UndoableEdit
	{
		Parameter addedParameter;
		int locationAdded;
	
		String undoName = "Parameter deletion [";
		boolean undid = false;
		public ParameterAddedUndoableEdit(Parameter param, int location)
		{
			init(param, location);
		}
		private void init(Parameter param, int location)
		{
			addedParameter = param;
			locationAdded = location;
			boolean firstParam = true;
			for(SingleParameter sparam : param.getSingleParameterList().getValues())
			{
				if(! firstParam )
					undoName = undoName + " ,";
				else
					firstParam = false;
				undoName = undoName + sparam.getDisplayName();
			}
			undoName = undoName + "]";
		}
		public void undo() throws CannotUndoException
		{
			deletedOnUndo = true;
			deleteParameter(addedParameter);
			deleteParameterText(addedParameter);
			undid = true;
		} 

		public boolean canUndo()
		{
			return !undid;
		}

		public void redo() throws CannotRedoException
		{
			if(undid)
			{
				insertParameter(addedParameter, locationAdded);
				undid = false;
			}	
		}

		public boolean canRedo()
		{
			return undid;
		}

		public void die()
		{
			
		}

		public boolean addEdit(UndoableEdit anEdit)
		{
			return false;
		}

		public boolean replaceEdit(UndoableEdit anEdit)
		{
			return false;
		}

		public boolean isSignificant()
		{
			return true;
		}

		public String getPresentationName()
		{
			return undoName;
		}

		public String getUndoPresentationName()
		{
			return "Undo " + undoName;
		}

		public String getRedoPresentationName()
		{
			return "Redo" + undoName;
		}
	}
	class ParameterDeleteUndoableEdit implements UndoableEdit
	{
		Vector<Parameter> localDeletedParameters = new Vector<Parameter>();
		//Vector<UndoableEdit> extraEdits = new Vector<UndoableEdit>();
		
		String undoName = "Parameter deletion [";
		boolean undid = false;
		public ParameterDeleteUndoableEdit(Parameter param)
		{
			Vector<Parameter> v = new Vector<Parameter>();
			v.add(param);
			init(v);
		}
		public ParameterDeleteUndoableEdit(Collection<Parameter> params)
		{
			init(params);
		}	
		private void init(Collection<Parameter> params)
		{
			localDeletedParameters.addAll(params);
			boolean firstParam = true;
			for(Parameter param : params)
			{
				for(SingleParameter sparam : param.getSingleParameterList().getValues())
				{
					if(! firstParam )
						undoName = undoName + " ,";
					else
						firstParam = false;
					undoName = undoName + sparam.getDisplayName();
				}
			}
			undoName = undoName + "]";
		}
		public void undo() throws CannotUndoException
		{
			//for(UndoableEdit ed : extraEdits)
			//{
			//	if(ed.canUndo())
			//		ed.undo();
			//}
			fireParameterAdded(localDeletedParameters);
			undid = true;
		} 

		public boolean canUndo()
		{
			return !undid;
		}

		public void redo() throws CannotRedoException
		{
			if(undid)
			{
				deletedParameters.clear();
				deletedParameters.addAll(localDeletedParameters);
				fireParameterDeleted();
				undid = false;
			}	
		}

		public boolean canRedo()
		{
			return undid;
		}

		public void die()
		{
			//localDeletedParameters.clear();
		}

		public boolean addEdit(UndoableEdit anEdit)
		{
			//extraEdits.add(anEdit);
			return false;
		}

		public boolean replaceEdit(UndoableEdit anEdit)
		{
			return false;
		}

		public boolean isSignificant()
		{
			return true;
		}

		public String getPresentationName()
		{
			return undoName;
		}

		public String getUndoPresentationName()
		{
			return "Undo " + undoName;
		}

		public String getRedoPresentationName()
		{
			return "Redo" + undoName;
		}
	}
	
	public class MyUndoManager extends UndoManager
	{
		public void undo()
		{
			if(super.canUndo())
			{
				super.undo();
				UndoableEdit nextEd = this.editToBeUndone();
				if(isRemoveOrParameterDeleteEdit(nextEd))
				{
					while(isRemoveOrParameterDeleteEdit(nextEd))
					{
						super.undo();
						nextEd = editToBeUndone();
					}
				}
				else if(isParameterAddEdit(nextEd) || isInsertEdit(nextEd))
				{
					while(isInsertEdit(nextEd))
					{
						super.undo();
						nextEd = editToBeUndone();
					}
					if(isParameterAddEdit(nextEd))
					{
						super.undo();
					}
				}
			}
		}
		private boolean isParameterAddEdit(UndoableEdit ed)
		{
			return(ed instanceof ParameterAddedUndoableEdit);
		}
		private boolean isInsertEdit(UndoableEdit ed)
		{
			return(ed instanceof DefaultDocumentEvent && ((DefaultDocumentEvent) ed).getType() == EventType.INSERT);
		}
		private boolean isRemoveOrParameterDeleteEdit(UndoableEdit ed)
		{
			if(ed == null)
				return(false);
			else if(ed instanceof ParameterDeleteUndoableEdit)
				return(true);
			else if(ed instanceof DefaultDocumentEvent && ((DefaultDocumentEvent) ed).getType() == EventType.REMOVE)
				return(true);
			else
				return(false);
		}
	}
}

