package oncotcap.display.modelcontroller.cellkineticsdemo;

import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import oncotcap.display.common.*;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.util.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.engine.ModelDefinition;
public class StatementListPanel extends JPanel
{
	private ModelDefinition model;
	private Hashtable sbInfoTable = new Hashtable();
	private int statementWidth = 300;
	private JPanel listPanel = new JPanel();
	private JPanel infoPanel = new JPanel();
	private TcapScrollPane scrollPane = new TcapScrollPane(listPanel);
	private JButton btnDelete = new JButton("Remove Statement");
	private JButton btnMore = new JButton("Another Statement");
	private static final int BUTTON_PANEL_WIDTH = 150;
	private int listWidth = 500;
	private static boolean setAlready = false;
	private JLabel lblInfo = new JLabel(OncoTcapIcons.getImageIcon("info.gif"));
	private StatementBundleEditorPanel selectedStatement = null;
	
	public StatementListPanel(ModelDefinition model)
	{
		this.model = model;
		init();
	}
	private void init()
	{
		setLayout(null);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setLocation(0,0);
		setBorder(BorderFactory.createEmptyBorder());
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
//		scrollPane.setBorder(BorderFactory.createLineBorder(Color.red, 4));
		listPanel.setLayout(null);
		listPanel.setAutoscrolls(false);
		listPanel.setBackground(TcapColor.darkBrown);
		//listPanel.add(lblList);
		btnDelete.setSize(BUTTON_PANEL_WIDTH, 30);
		btnDelete.setVisible(true);
		btnDelete.setEnabled(false);
		btnDelete.setMnemonic('V');
		btnMore.setSize(BUTTON_PANEL_WIDTH, 30);
		btnMore.setVisible(true);
		btnMore.setEnabled(false);
		btnMore.setMnemonic('A');
		infoPanel.setVisible(true);
		infoPanel.setBackground(TcapColor.darkBrown);
		lblInfo.setSize(BUTTON_PANEL_WIDTH, 30);
		lblInfo.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH,30));
		lblInfo.setLocation(0,0);
		scrollPane.setVisible(true);
		add(scrollPane);
		add(btnDelete);
		add(btnMore);
		add(infoPanel);
	}
	public void resizePanel(int width, int height)
	{
		setSize(width, height);
		setPreferredSize(new Dimension(width, height));
		listWidth = width - BUTTON_PANEL_WIDTH;
		int listHeight = height - 5;
		scrollPane.setSize(listWidth, listHeight);
		btnDelete.setLocation(listWidth, 0);
		btnMore.setLocation(listWidth, btnDelete.getHeight());
		infoPanel.setLocation(listWidth, btnDelete.getHeight()+btnMore.getHeight());
		infoPanel.setSize(BUTTON_PANEL_WIDTH, listHeight - btnDelete.getHeight()-btnMore.getHeight());
		refreshEditors();
	}
	public void refreshEditors()
	{
		refreshEditors(false);
	}
	public void refreshEditors(boolean positionOnly)
	{
		//int nextPosition = 5 + lblList.getHeight();
		boolean callAgain = false;
		int nextPosition = 5;
		StatementBundle sb;
		StatementBundleEditorPanel se;
		Iterator iter = model.getSortedStatements().iterator();
		Collection infoColl = null;
		clearInfoPanel();
		while(iter.hasNext())
		{
			sb = (StatementBundle) iter.next();
			if((se = sb.getStatementEditor()) != null)
			{
					se.setWidth(listWidth - 27);
					se.setWidth(listWidth - 27);
					//if(!positionOnly)
					se.setBorder(BorderFactory.createEmptyBorder());
					se.setBackground(TcapColor.lightBrown);
					se.refreshStatement();
					if(SelectionManager.isSelected(se))
					{
						se.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
						se.setBackground(TcapColor.lightBlue);
						se.setBackground(TcapColor.lightBlue);
						se.setForeground(Color.red);
						se.setBorder(BorderFactory.createLineBorder(Color.red, 2));
						/*infoColl = sbvm.getStatementBundle().getHelpInfo();
						if(infoColl != null)
							setupInfoPanel(infoColl);*/
					} 
					if(!se.addedToPanel)
					{
						se.setWidth(listWidth - 27);
						listPanel.add(se);
						se.addedToPanel = true;
						se.setLocation(5,nextPosition);
						se.setVisible(true);
						se.refreshStatement();
						listPanel.revalidate();
						listPanel.repaint();
						scrollPane.revalidate();
						scrollPane.repaint();
						se.setWidth(listWidth - 27);
						callAgain = true;
					}
					se.setLocation(5,nextPosition);
					se.setVisible(true);
					nextPosition = nextPosition + se.getHeight() + 5;
					listPanel.setSize(listWidth, nextPosition);
					listPanel.setPreferredSize(new Dimension(listWidth-20, nextPosition));

				}
				else
					se.setVisible(false);
		}
		listPanel.revalidate();
		listPanel.repaint();
		scrollPane.revalidate();
		scrollPane.repaint();
		revalidate();
		repaint();
/*		if(callAgain)
		{
			long waitTime = System.currentTimeMillis() + 2000;
			while(System.currentTimeMillis() <= waitTime)
			{
				try{Thread.sleep(1000);}
				catch(InterruptedException e){System.out.println("Wait interrupted");}
				
				System.out.println("Waiting");
			}
			refreshEditors(positionOnly);
			resizePanel(getWidth(), getHeight());
		} */
	}
	public void listPanelAdd(Component comp)
	{
		listPanel.add(comp);
		if(comp instanceof StatementBundleEditorPanel)
			((StatementBundleEditorPanel) comp).setWidth(listWidth);
		refreshEditors();
	}
	public void removeStatement(StatementBundleEditorPanel panel)
	{
		if(panel == selectedStatement)
		{
			selectedStatement = null;
			btnDelete.setEnabled(false);
		}
		listPanel.remove(panel);
		refreshEditors();
	}
	public void selectStatement(StatementBundleEditorPanel panel)
	{
		if(selectedStatement != null)
			selectedStatement.setSelected(false);

		selectedStatement = panel;
		selectedStatement.setSelected(true);
		btnDelete.setEnabled(true);
		refreshEditors();
	}
	public StatementBundleEditorPanel getSelectedStatement()
	{
		return(selectedStatement);
	}
	public void addDeleteActionListener(ActionListener list)
	{
		btnDelete.addActionListener(list);
		btnMore.addActionListener(list);
	}
	private void clearInfoPanel()
	{
		infoPanel.removeAll();
	}
	private void setupInfoPanel(Collection infoColl)
	{
		HelpInfo helpInfo;
		int type;
		String caption;
		String strInfo;
		clearInfoPanel();
		boolean firstTimeThru = true;
		int nextPosition = lblInfo.getHeight();
		if(infoColl != null)
		{
			Iterator iter = infoColl.iterator();
			while(iter.hasNext())
			{
				if(firstTimeThru)
					infoPanel.add(lblInfo);
				helpInfo = (HelpInfo) iter.next();
				InfoLabel iLabel = new InfoLabel(helpInfo);
				iLabel.setLocation(0, nextPosition);
				nextPosition = nextPosition + iLabel.getHeight();
				infoPanel.add(iLabel);
				firstTimeThru = false;
			}
		}
	}

	public void ensureIsShown(StatementBundleEditorPanel se)
	{
//		Logger.log("ensure is shown");
		if(se != null)
		{
//			if(se.getStatementBundle().getIsListed(se.getBundleIndex()))
//			{
				int setVal;
				int bottomOfPane = scrollPane.getVerticalValue() + scrollPane.getVerticalBlockIncrement(1);
//				Logger.log("bottomOfPane    = " + bottomOfPane);
//				Logger.log("vertical value  = " + scrollPane.getVerticalValue());
//				Logger.log("block increment = " + scrollPane.getVerticalBlockIncrement(1));
//				Logger.log("bottom of edit  = " + se.getBottom());
				if(se.getBottom() > bottomOfPane)
				{
					setVal = scrollPane.getVerticalValue() + se.getBottom() - bottomOfPane;
//					Logger.log("Value set to " + setVal);
					scrollPane.setVerticalValue(setVal);
				}
				if(se.getTop() < scrollPane.getVerticalValue())
				{
					scrollPane.setVerticalValue(se.getTop());
				}
//			}
		}
	}

/*	private Collection checkForInfo(StatementBundle sb)
	{
		Collection helpInfo;
		try
		{
			helpInfo = sb.getInstances("HelpInfo");
			return(helpInfo);
		}
		catch(NullPointerException e){return(null);}
		catch(edu.stanford.smi.protege.util.AssertionFailedException e){return(null);}
	}
*/
	public void enableDelete()
	{
		btnDelete.setEnabled(true);
	}
	public void disableDelete()
	{
		btnDelete.setEnabled(false);
	}
	public void enableMore()
	{
		btnMore.setEnabled(true);
	}
	public void disableMore()
	{
		btnMore.setEnabled(false);
	}
//	public void setStatementBundles(Vector sbs)
//	{
//		statementBundles = sbs;
//	}
	public void clearEditors()
	{
		listPanel.removeAll();
		listPanel.repaint();
	}
}

class InfoSource
{
	public static final int TEXT = 1;
	public static final int URL = 2;
	public int type;
	public String info;
	
	InfoSource(int type, String info)
	{
		this.type = type;
		this.info = info;
	}
}
class InfoLabel extends JLabel implements MouseListener
{
	public HelpInfo infoSource;
	private Color txtColor = null;
	InfoLabel(HelpInfo info)
	{
//	InfoLabel(String text, InfoSource info)
		super(info.getShortDescription(), JLabel.CENTER);
		this.infoSource = info;
		addMouseListener(this);
		setPreferredSize(new Dimension(150,24));
		setSize(150,24);
		this.infoSource = info;
		setForeground(Color.white);
	}

	public void mousePressed(MouseEvent e)
	{
		if(infoSource.getType() == HelpInfo.TEXT_HELP)
		{
			OncMessageBox.showMessageDialog(null,
										  infoSource.getInfoText(),
										  "Information on this statement",
										  JOptionPane.INFORMATION_MESSAGE);
		}
		else if (infoSource.getType() == HelpInfo.URL_HELP)
		{
			BrowserLauncher.launch(infoSource.getInfoText());
		}
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e)
	{
		if(txtColor == null)
			txtColor = getForeground();
		setForeground(TcapColor.pastelBlue);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}
	public void mouseExited(MouseEvent e)
	{
		if(txtColor != null)
			setForeground(txtColor);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}
}
