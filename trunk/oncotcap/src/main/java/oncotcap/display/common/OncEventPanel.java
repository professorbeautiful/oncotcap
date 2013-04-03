package oncotcap.display.common;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import oncotcap.util.*;

public class OncEventPanel extends JPanel
{
	JTable table;
	TcapScrollPane scrollPane;
	OncEventTableModel data = new OncEventTableModel();
	TableColumn cols[] = { new TableColumn(), new TableColumn(), new TableColumn() };
	private int width = 300;
	private int height = 100;
	
	public static void main(String [] args)
	{
		JFrame jf = new JFrame();
		Container cPane = jf.getContentPane();
		cPane.setLayout(null);
		jf.setSize(500,500);
		final OncEventPanel ep = new OncEventPanel();
		ep.setSize(300,100);
		ep.setLocation(10,10);
		ep.addRow(new OncEvent(1.0, "ONE", "Description one"));
		ep.addRow(new OncEvent(2.0,"TWO","DESCRIPTION TWO"));
		ep.addRow(new OncEvent(3.0,"THREE","DESCRIPTION THREE"));
		ep.addRow(new OncEvent(4.0,"Four","DESCRIPTION Four"));
		ep.addRow(new OncEvent(5.0,"FIVE","DESCRIPTION FIVE"));
		ep.addRow(new OncEvent(6.0,"SIX","DESCRIPTION SIX"));

		cPane.add(ep);

		JButton but = new JButton("Stuff");
		but.setSize(100,30);
		but.setLocation(350,400);
		but.addActionListener(new ActionListener()
				{	public void actionPerformed(ActionEvent e)
					{
						Logger.log("Button pushed");
						ep.clear();
						//ep.addRow(new OncEvent(1.0, "ONE", "Description one"));
						//ep.addRow(new OncEvent(2.0,"TWO","DESCRIPTION TWO"));
						//ep.addRow(new OncEvent(4.0,"Four","DESCRIPTION Four"));
						//ep.addRow(new OncEvent(5.0,"FIVE","DESCRIPTION FIVE"));
						//ep.addRow(new OncEvent(6.0,"SIX","DESCRIPTION SIX"));
						//ep.addRow(new OncEvent(7.0,"LALA","SLFJ:SLDJFSLDFFLSDF"));
						//ep.addRow(new OncEvent(8.8,"SDFSDF","WOSDIFJOSDIFJSPDOFIJ"));
						//ep.setWidth(400);
					}
				}
		);
		cPane.add(but);
		
		
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		jf.setVisible(true);
	}
	public OncEventPanel()
	{
		int n;
		
		table = new JTable(data);
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		//table.sizeColumnsToFit(true);

		for (n=0; n<3; n++)
			cols[n] = table.getColumnModel().getColumn(n);

		cols[0].setMinWidth(70);
		cols[0].setMaxWidth(70);
		cols[0].setPreferredWidth(70);
		cols[0].setHeaderValue("Time");
		cols[1].setMinWidth(170);
		cols[1].setMaxWidth(170);
		cols[1].setPreferredWidth(170);
//		cols[1].setWidth(200);
		cols[1].setHeaderValue("Event");
		cols[2].setHeaderValue("Description");
		scrollPane = new TcapScrollPane(table);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setLocation(0,0);
		add(scrollPane);
	}

	public void addRow(OncEvent e)
	{
		data.add(e);
		//table.setEditingRow(table.getRowCount()-1);
		scrollPane.scrollToBottom();
		setSize();
	}

	public void clear()
	{
		data.clear();
		setSize();
	}
	public void revalidateAndRepaint()
	{
		table.revalidate();
		table.repaint();
		scrollPane.revalidate();
		scrollPane.repaint();
	}

	public void setWidth(int width)
	{
		this.width = width;
		setSize();
	}
	public void setHeight(int height)
	{
		this.height = height;
		setSize();
	}

	public void setSize(int width, int height)
	{
		this.width = width;
		this.height = height;
		setSize();
	}

	private void setSize()
	{
		table.setSize(width,table.getRowHeight() * data.getRowCount());
		table.setPreferredSize(new Dimension(width, table.getRowHeight() * data.getRowCount()));
		scrollPane.setSize(width,height);
		scrollPane.setPreferredSize(new Dimension(width,height));
		super.setSize(width,height+5);
		setPreferredSize(new Dimension(width,height));
		//DOESNT WORK:  table.getSelectionModel().setSelectionInterval(data.getRowCount()-1,data.getRowCount()-1);
		/*Selects but doesn't scroll:
		 */
		/*DOESNT WORK:
		 *table.setRowSelectionAllowed(true);
		if (data.getRowCount()>0)
			table.addRowSelectionInterval(data.getRowCount()-1,data.getRowCount()-1);
		table.clearSelection();
		*/
		//Logger.log("Setting to last row");
		revalidateAndRepaint();
	}

	public void setTimeOnTrial(double time)
	{
		data.setTimeOnTrial(time);
	}

	public double getTimeOnTrial()
	{
		return(data.getTimeOnTrial());
	}
}