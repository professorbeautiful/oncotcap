package oncotcap.display.browser;

import java.awt.*;
import java.util.*;
import java.lang.reflect.Array;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.datatransfer.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import oncotcap.display.common.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.datalayer.autogenpersistible.*;
import oncotcap.util.*;

public class OntologyButtonPanel extends JPanel 
		implements ActionListener {

		private static final DataFlavor nodeFlavors [] = { Droppable.ontologyObjectName };

		private LeafCheckBox [] leafCheckBox = 
				(LeafCheckBox [])Array.newInstance(LeafCheckBox.class, 11);

		Border border1;
		TitledBorder titledBorder1;
		Border border2;

		// Create the listener list
		protected EventListenerList listenerList =
				new EventListenerList();

		private ButtonGroup bg = null;
		boolean inAnApplet = true;
		static final int CENTER_START = 4;
    final boolean shouldFill = false;
    final boolean shouldWeightX = false;
		static OntologyButtonPanel buttonPanel = null;
		// Root buttons
		RootCheckBox isRoot= null;
		RootCheckBox knRoot= null;
		RootCheckBox iRoot= null;
		RootCheckBox eRoot= null;
		RootCheckBox sbRoot= null;
		RootCheckBox stRoot= null;
		RootCheckBox cbRoot= null;
//		RootCheckBox opRoot= null;
		RootCheckBox smRoot= null;
		RootCheckBox smgRoot= null;
		RootCheckBox mcRoot= null;
		RootCheckBox kRoot= null;
		
		// Icons

		Image backgroundImage = 
				OncoTcapIcons.getImageIcon("ontologypanelbackground.jpg").getImage();
		ImageIcon infoSourceIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("is.jpg");
 		ImageIcon nuggetIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("kn.jpg");
		ImageIcon interpretationIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("i.jpg");
		ImageIcon encodingIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("e.jpg");
		ImageIcon submodelIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("sm.jpg");
		ImageIcon subModelGroupIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("smg.jpg");
 		ImageIcon modelControllerIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("mc.jpg");
		ImageIcon keywordIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("k.jpg");
		ImageIcon oncProcessIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("op.jpg");
		ImageIcon codeBundleIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("cb.jpg");
		ImageIcon statementBundleIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("sb.jpg");
		ImageIcon statementTemplateIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("st.jpg");
		ImageIcon rootNodeIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("root-selected.jpg");
		ImageIcon rootUnselectedNodeIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("root-unselected.jpg");
		ImageIcon leafNodeSelectedIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("visible-selected.jpg");
		ImageIcon leafNodeUnselectedIcon = 
				oncotcap.util.OncoTcapIcons.getImageIcon("visible-unselected.jpg");
		
    public OntologyButtonPanel() {
				setToolTipText("<html>Select a single root node by clicking to "
											 + "<br> the left of the label icon. Select leaf "
											 + "<br> node(s) by clicking on the arrows to the "
											 + "<br> right of the label icons. Keywords, "
											 + "<br> submodels, submodel groups and statement "
											 + "<br> bundles can be roots and leaves "
											 + "<br> ( not enforced yet). OncProcessClassWriter/Onc Event "
											 + "<br> is a special tree implementation that has not "
											 + "<br>  been translated to the new architecture yet.</html>");
				//setBackground(new Color(212,208,200));
				// Create all the necessary buttons
				// INfo SOurce
        isRoot = new RootCheckBox(OntologyMap.INFO_SOURCE,
																							 this, 
																							 rootUnselectedNodeIcon, 
																							 rootNodeIcon);
				OntologyObjectLabel isButton = new OntologyObjectLabel(infoSourceIcon,isRoot);
				leafCheckBox[OntologyMap.IS] = 
						new LeafCheckBox(OntologyMap.INFO_SOURCE,this, 
														 leafNodeUnselectedIcon, leafNodeSelectedIcon);
				
				//Knowledge Nugget
        knRoot = 
						new RootCheckBox(OntologyMap.NUGGET,this, 
														 rootUnselectedNodeIcon, rootNodeIcon);
        OntologyObjectLabel knButton = new OntologyObjectLabel(nuggetIcon, knRoot);
				leafCheckBox[OntologyMap.KN] = 
						new LeafCheckBox(OntologyMap.NUGGET,this, 
														 leafNodeUnselectedIcon, leafNodeSelectedIcon); 
				
				// Interpretation
        iRoot = 
						new RootCheckBox(OntologyMap.INTERPRETATION,this, 
														 rootUnselectedNodeIcon, rootNodeIcon); 
				leafCheckBox[OntologyMap.I] = 
						new LeafCheckBox(OntologyMap.INTERPRETATION,this, 
														 leafNodeUnselectedIcon, leafNodeSelectedIcon);
        OntologyObjectLabel iButton = new OntologyObjectLabel(interpretationIcon, 
																															iRoot);

				// Encoding
        eRoot =  new RootCheckBox(OntologyMap.ENCODING,
																							 this, 
																							 rootUnselectedNodeIcon, 
																							 rootNodeIcon); 
        OntologyObjectLabel eButton = 
						new OntologyObjectLabel(encodingIcon, eRoot);
				leafCheckBox[OntologyMap.E] =  
						new LeafCheckBox(OntologyMap.ENCODING,
														 this, 
														 leafNodeUnselectedIcon, 
														 leafNodeSelectedIcon);
				
				// Statement Bundle
        sbRoot = 
						new RootCheckBox(OntologyMap.STATEMENT_BUNDLE,
														 this, 
														 rootUnselectedNodeIcon,
														 rootNodeIcon); 
        OntologyObjectLabel sbButton = 
						new OntologyObjectLabel(statementBundleIcon, sbRoot);
				leafCheckBox[OntologyMap.SB] = 
						new LeafCheckBox(OntologyMap.STATEMENT_BUNDLE,this, 
														 leafNodeUnselectedIcon, 
														 leafNodeSelectedIcon);

				// Statement Template
        stRoot = 
						new RootCheckBox(OntologyMap.STATEMENT_TEMPLATE,this, 
														 rootUnselectedNodeIcon, 
														 rootNodeIcon); 
        OntologyObjectLabel stButton = 
						new OntologyObjectLabel(statementTemplateIcon, stRoot);
				leafCheckBox[OntologyMap.ST] = 
						new LeafCheckBox(OntologyMap.STATEMENT_TEMPLATE,this, 
														 leafNodeUnselectedIcon, 
														 leafNodeSelectedIcon);
				
				
        cbRoot = 
						new RootCheckBox(OntologyMap.CODE_BUNDLE,
														 this, 
														 rootUnselectedNodeIcon, 
														 rootNodeIcon); 
        OntologyObjectLabel cbButton = 
						new OntologyObjectLabel(codeBundleIcon, cbRoot);
				leafCheckBox[OntologyMap.CB] = 
						new LeafCheckBox(OntologyMap.CODE_BUNDLE,this, 
														 leafNodeUnselectedIcon, 
														 leafNodeSelectedIcon);
				
				// Onc Process
//        opRoot = 
//						new RootCheckBox(OntologyMap.ONC_PROCESS,
//														 this, 
//														 rootUnselectedNodeIcon, 
//														 rootNodeIcon); 
 //       OntologyObjectLabel opButton = 
//						new OntologyObjectLabel(oncProcessIcon, opRoot);
//				opRoot.setEnabled(false);
//				leafCheckBox[OntologyMap.OP] = 
//						new LeafCheckBox(OntologyMap.ONC_PROCESS,this, 
//														 leafNodeUnselectedIcon, leafNodeSelectedIcon);

				// SubModel
        smRoot = 
						new RootCheckBox(OntologyMap.SUBMODEL,this, 
														 rootUnselectedNodeIcon, rootNodeIcon); 
        OntologyObjectLabel smButton = new OntologyObjectLabel(submodelIcon, smRoot);
				leafCheckBox[OntologyMap.SM] = 
						new LeafCheckBox(OntologyMap.SUBMODEL,this, 
														 leafNodeUnselectedIcon, 
														 leafNodeSelectedIcon);

				// SubModel Groups
        smgRoot = 
						new RootCheckBox(OntologyMap.SUBMODEL_GROUP,
														 this, 
														 rootUnselectedNodeIcon, rootNodeIcon); 
        OntologyObjectLabel smgButton = new OntologyObjectLabel(subModelGroupIcon,
																																smgRoot);
				leafCheckBox[OntologyMap.SMG] = 
						new LeafCheckBox(OntologyMap.SUBMODEL_GROUP,
														 this, 
														 leafNodeUnselectedIcon, leafNodeSelectedIcon);
				        
				// Model Controller
        mcRoot = 
						new RootCheckBox(OntologyMap.MODEL_CONTROLLER,this, 
														 rootUnselectedNodeIcon, rootNodeIcon); 
				OntologyObjectLabel mcButton = 
						new OntologyObjectLabel(modelControllerIcon, mcRoot);
				leafCheckBox[OntologyMap.MC] = 
						new LeafCheckBox(OntologyMap.MODEL_CONTROLLER,this, 
														 leafNodeUnselectedIcon, leafNodeSelectedIcon);


				// Keyword
        kRoot = new RootCheckBox(OntologyMap.KEYWORD, 
																							this, 
																							rootUnselectedNodeIcon, 
																							rootNodeIcon); 
        OntologyObjectLabel kButton = 
						new OntologyObjectLabel(keywordIcon, kRoot);
				leafCheckBox[OntologyMap.K] = 
						new LeafCheckBox(OntologyMap.KEYWORD, this, 
														 leafNodeUnselectedIcon, 
														 leafNodeSelectedIcon);
				
				// Create a button group so only one root can be selected at a time
				bg = new ButtonGroup();
				bg.add(isRoot);
				bg.add(knRoot);
				bg.add(iRoot);
				bg.add(eRoot);
				bg.add(sbRoot);
				bg.add(stRoot);
				bg.add(cbRoot);
//				bg.add(opRoot);
				bg.add(smRoot);
				bg.add(smgRoot);
				bg.add(mcRoot);
				bg.add(kRoot);

				// Set the first selected root node to information source
				ButtonModel model = isRoot.getModel();
				bg.setSelected(model, true);

	
				setLayout(null);

				border1 = BorderFactory.createBevelBorder(BevelBorder.RAISED,
																									Color.white,
																									Color.white,
																									new Color(124, 124, 124),
																									new Color(178, 178, 178));
				titledBorder1 = 
						new TitledBorder
						(BorderFactory.createBevelBorder(BevelBorder.RAISED,
																						 Color.white,
																						 Color.white,
																						 new Color(124, 124, 124),
																						 new Color(178, 178, 178)),"");
				border2 = 
						BorderFactory.createCompoundBorder(titledBorder1,
																							 BorderFactory.createEmptyBorder
																							 (2,0,2,0));
				// Layout at absolut positions
				isButton.setBounds(new Rectangle(133, 10, 115, 18));
				leafCheckBox[OntologyMap.IS].setBounds(new Rectangle(248, 10, 23, 18));
				isRoot.setBounds(new Rectangle(113, 10, 20, 18));
				
				leafCheckBox[OntologyMap.KN].setBounds(new Rectangle(248, 32, 23, 18));
				knButton.setBounds(new Rectangle(133, 32, 115, 18));
				knRoot.setBounds(new Rectangle(113, 32, 20, 18));
				
				leafCheckBox[OntologyMap.I].setBounds(new Rectangle(248, 54, 23, 18));
				iButton.setBounds(new Rectangle(133, 54, 115, 18));
				iRoot.setBounds(new Rectangle(113, 54, 20, 18));
				
				leafCheckBox[OntologyMap.E].setBounds(new Rectangle(320, 79, 23, 18));
				eButton.setBounds(new Rectangle(74, 79, 246, 18));
				eRoot.setBounds(new Rectangle(54, 79, 20, 18));
				
				sbButton.setBounds(new Rectangle(57, 104, 115, 18));
				sbRoot.setBounds(new Rectangle(37, 104, 20, 18));
				leafCheckBox[OntologyMap.SB].setBounds(new Rectangle(172, 104, 23, 18));

				stButton.setBounds(new Rectangle(42, 127, 115, 18));
				leafCheckBox[OntologyMap.ST].setBounds(new Rectangle(157, 127, 23, 18));
				stRoot.setBounds(new Rectangle(22, 127, 20, 18));

				cbButton.setBounds(new Rectangle(28, 151, 115, 18));
				cbRoot.setBounds(new Rectangle(8, 151, 20, 18));
				leafCheckBox[OntologyMap.CB].setBounds(new Rectangle(143, 151, 23, 18));
				
// 				leafCheckBox[OntologyMap.OP].setBounds(new Rectangle(137, 150, 27, 15));
// 				opButton.setBounds(new Rectangle(28, 147, 111, 19));
// 				opRoot.setBounds(new Rectangle(3, 148, 31, 15));
				
				smRoot.setBounds(new Rectangle(204, 104, 20, 18));
				leafCheckBox[OntologyMap.SM].setBounds(new Rectangle(339, 104, 23, 18));
				smButton.setBounds(new Rectangle(224, 104, 115, 18));
				
				smgButton.setBounds(new Rectangle(239, 126, 115, 18));
				leafCheckBox[OntologyMap.SMG].setBounds(new Rectangle(354, 126, 23, 18));
				smgRoot.setBounds(new Rectangle(219, 126, 20, 18));
				
				mcButton.setBounds(new Rectangle(255, 149, 115, 18));
				leafCheckBox[OntologyMap.MC].setBounds(new Rectangle(370, 149, 23, 18));
				mcRoot.setBounds(new Rectangle(235, 149, 20, 18));
				
				
				kButton.setBounds(new Rectangle(133, 179, 115, 18));
				leafCheckBox[OntologyMap.K].setBounds(new Rectangle(247, 179, 23, 18));
				kRoot.setBounds(new Rectangle(113, 179, 20, 18));
				
				setBackground(new Color(235, 232, 227));
				setBorder(border2);
				add(isButton, null);
				add(knButton, null);
				add(iButton, null);
				add(eButton, null);
				add(sbButton, null);
				add(stButton, null);
				add(cbButton, null);
// 				add(opButton, null);
				add(smButton, null);
				add(smgButton, null);
				add(mcButton, null);
				add(kButton, null);
				
				add(leafCheckBox[OntologyMap.IS], null);
				add(leafCheckBox[OntologyMap.KN], null);
				add(leafCheckBox[OntologyMap.I], null);
				add(leafCheckBox[OntologyMap.E], null);
				add(leafCheckBox[OntologyMap.SB], null);
				add(leafCheckBox[OntologyMap.ST], null);
				add(leafCheckBox[OntologyMap.CB], null);
// 				add(leafCheckBox[OntologyMap.OP], null);
				add(leafCheckBox[OntologyMap.SM], null);
				add(leafCheckBox[OntologyMap.SMG], null);
				add(leafCheckBox[OntologyMap.MC], null);
				add(leafCheckBox[OntologyMap.K], null);
				
				add(isRoot, null);
				add(knRoot, null);
				add(iRoot, null);
				add(eRoot, null);
				add(sbRoot, null);
				add(stRoot, null);
				add(cbRoot, null);
// 				add(opRoot, null);
				add(smRoot, null);
				add(smgRoot, null);
				add(mcRoot, null);
				add(kRoot, null);
				setPreferredSize(new Dimension(400,200));
    }
		
		public OntologyButtonPanel getOntologyPanel() {
				return this;
		}

		public ButtonGroup getButtonGroup() {
				return bg;
		}
		
		public  LeafCheckBox [] getLeafCheckBoxArray() {
				return leafCheckBox;
		}
		public  LeafCheckBox  getLeafCheckBox(int i) {
				return leafCheckBox[i];
		}
		public void setRoot(int ontologyMapId) {
				// Set the first selected root node to information source
				// Chould change roots to an array here but for now just put in 
				// yucky case statement
				ButtonModel model = null;
				JCheckBox checkBox = null;
				switch ( ontologyMapId) {
				case OntologyMap.IS:
						checkBox = isRoot;
						break;
				case OntologyMap.KN:
						checkBox = knRoot;
						break;
				case OntologyMap.I:
						checkBox = iRoot;
						break;
				case OntologyMap.E:
						checkBox = eRoot;
						break;
				case OntologyMap.SB:
						checkBox = sbRoot;
						break;
				case OntologyMap.ST:
						checkBox = stRoot;
						break;
				case OntologyMap.CB:
						checkBox = cbRoot;
						break;
//				case OntologyMap.OP:
//						checkBox = opRoot;
//						break;
				case OntologyMap.SM:
						checkBox = smRoot;
						break;
				case OntologyMap.SMG:
						checkBox = smgRoot;
						break;
				case OntologyMap.MC:
						checkBox = mcRoot;
						break;
				case OntologyMap.K:
						checkBox = kRoot;
						break;
				}
				selectCheckBox(checkBox);
		}

		private void selectCheckBox(JCheckBox checkBox) {
				if ( !checkBox.isSelected() )
						checkBox.doClick();
		}
		public void setLeaves(Collection leafIds) {
				Iterator i = leafIds.iterator();
				while ( i.hasNext() ) {
						int id = ((Integer)i.next()).intValue();
						selectCheckBox(leafCheckBox[id]);
				}
		}

		public void setLeavesQuietly(Collection leafIds) {
				Iterator i = leafIds.iterator();
				while ( i.hasNext() ) {
						int id = ((Integer)i.next()).intValue();
						leafCheckBox[id].setSelected(true);
				}
		}

   // Add the event registration and notification code to a class.
    
		// This methods allows classes to register for OntologyPanelEvents
		public void addOntologyPanelListener
				(OntologyPanelListener listener) {
				listenerList.add(OntologyPanelListener.class, listener);
		}
    
		// This methods allows classes to unregister for OntologyPanelEvents
		public void removeOntologyPanelListener
				(OntologyPanelListener listener) {
				listenerList.remove(OntologyPanelListener.class, listener);
		}
    
		// This private class is used to fire OntologyPanelEvents
		private void fireOntologyPanelEvent(OntologyPanelEvent evt) {

				Object[] listeners = listenerList.getListenerList();
				// Each listener occupies two elements 
				// - the first is the listener class
				// and the second is the listener instance
				for (int i=0; i<listeners.length; i+=2) {
						if (listeners[i]==OntologyPanelListener.class) {
								((OntologyPanelListener)listeners[i+1]).changed(evt);
						}
				}
		}

		public void actionPerformed(ActionEvent e){ 
				// System.out.println("what was the action: " + e + " -- " 
				// + e.getSource().getClass() +  " -- " + e.getModifiers() 
				// + " -- " + ( e.getModifiers() & ActionEvent.SHIFT_MASK));
// 				System.out.println("e: " + e);
				// See if there was a modifier 
				//( a button being held down when mouse clicked)
				if ( ((e.getModifiers() & ActionEvent.SHIFT_MASK) 
							== ActionEvent.SHIFT_MASK ) 
						 && ( e.getSource().getClass() == LeafCheckBox.class) ) {
 						//select as leafs all ontology objects between root and this leaf
						setLeavesQuietly(OntologyMap.whatIsBetween(this, 
																								(LeafCheckBox)e.getSource()));

 				}
				fireOntologyPanelEvent(new OntologyPanelEvent(this, e));
		}

	
		
    public static void main(String args[]) {
        buttonPanel = new OntologyButtonPanel();
				JFrame window = new JFrame();
				window.getContentPane().setLayout(new BorderLayout());
				window.getContentPane().add(buttonPanel, BorderLayout.CENTER);
				window.getContentPane().add(new JLabel("HEY"), BorderLayout.NORTH);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("GridBagLayout");
        window.setSize(new Dimension(300,300));
        window.setVisible(true);
    }


		private static final TempImageObserver observer = new TempImageObserver();
		public void paintComponent(Graphics g)
		{
				g.drawImage(backgroundImage,0,0, observer);
				
		}
		static class TempImageObserver implements ImageObserver
		{
				public boolean imageUpdate(Image img, int infoflags, 
																	 int x, int y, int width, int height)
				{
						return(false);
				}
		}
		class  OntologyObjectLabel extends JLabel implements oncotcap.display.common.Droppable {
		
				private RootCheckBox rootBox;

				public OntologyObjectLabel(ImageIcon icon, RootCheckBox rb) {
						super(icon);
						this.rootBox = rb;
						setBackground(new Color(235,232,227));
						setBorder(new EmptyBorder(new Insets(0,0,0,0)));
						// Enable drag on this label
						LabelTransferHandler th = new LabelTransferHandler("text");
						setTransferHandler(th);
						MouseInputListener ml = new MyMouseListener();
						addMouseListener(ml);
						addMouseMotionListener(ml);
				}

				public boolean dropOn(Object dropOnObject){
						//System.out.println("Drop on");
						return false;
				}
				public DataFlavor [] getTransferDataFlavors()
				{
						//System.out.println("getTransferDataFlavors  - oo " + nodeFlavors);
						return(nodeFlavors);
				}
				public boolean isDataFlavorSupported( DataFlavor flavor)
				{
						//System.out.println("isDataFlavorSupported - in oolabel " + flavor);
						if ( flavor.equals(Droppable.ontologyObjectName) )
								return true;
						return false;
				}
				public Object getTransferData(DataFlavor flavor) 
						throws UnsupportedFlavorException, IOException
				{
						if (isDataFlavorSupported(flavor))
								return(new OntologyObjectName(rootBox.getName()));
						else
								throw(new UnsupportedFlavorException(flavor));
				}

				class MyMouseListener implements MouseInputListener {
						public void mouseEntered(MouseEvent e) {
								setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
						}
						public void mouseExited(MouseEvent e)	{
								setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						}
						public void mouseDragged(MouseEvent e)
						{	
								JComponent c = (JComponent)e.getSource();
								TransferHandler handler = c.getTransferHandler();
								handler.exportAsDrag(c, e, 
																		 TransferHandler.COPY);
						}
						public void mouseMoved(MouseEvent e)
						{
						}
						public void mousePressed(MouseEvent e)
						{
						}
						public void mouseClicked(MouseEvent e)
						{
								rootBox.setSelected(true);
								rootBox.fireActionPerformed(new ActionEvent(rootBox, 0, ""));
						}
						public void mouseReleased(MouseEvent e)
						{
						}
				}	
		}	
		class LabelTransferHandler extends TransferHandler {
				public LabelTransferHandler() {
						super("Text");
				}
				public LabelTransferHandler(String s) {
						super(s);
				}
				public void exportAsDrag(JComponent comp, InputEvent e, int action)
				{
						super.exportAsDrag(comp, e, action); 
				}
				public void exportToClipboard(JComponent comp, Clipboard clip, int action)
				{
						super.exportToClipboard(comp, clip, action);
				}
				
				
				public Transferable createTransferable(JComponent c)
				{
						//System.out.println("createtransferable");
						return (Transferable)c;
				}
				protected void exportDone(JComponent c, Transferable t, int action)
				{
				}
				public boolean canImport(JComponent c, DataFlavor[] flavors)
				{
						return false;
				}
				
		}
		
}

