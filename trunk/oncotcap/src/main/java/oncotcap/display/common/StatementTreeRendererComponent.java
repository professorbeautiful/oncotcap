package oncotcap.display.common;

import java.awt.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;

import oncotcap.util.HTMLParserHelper;

public class StatementTreeRendererComponent extends JComponent implements TreeRendererComponent
{
	
	public static void main(String [] args)
	{
		StatementTreeRendererComponent rc = new StatementTreeRendererComponent("abc<a stuff=aslf>def</a ><a>123</a>g\nhi\n<a adsfafds >  jkl</a>mnop");
		//StatementTreeRendererComponent rc = new StatementTreeRendererComponent("abcadsfadsf");
		//StatementTreeRendererComponent rc = new StatementTreeRendererComponent("When a patient's primary tumoris resected, a residual tumor burden of\ntype  cell\ncharacteristicsis distributed as a rescaled log beta with\nparameters aParameter\nand bParameter"); 
	}
	public StatementTreeRendererComponent()
	{
//		setSize(totalWidth, myHeight);
//		setPreferredSize(getPreferredSize());
	}
	public StatementTreeRendererComponent(String text)
	{
		setText(text);
		ImageIcon icon = oncotcap.util.OncoTcapIcons.getImageIcon("statementbundle.jpg");
		setIcon(icon);
		JFrame jf = new JFrame();
		jf.setSize(300,300);
		jf.getContentPane().setLayout(new BorderLayout());
		jf.getContentPane().add(this, BorderLayout.CENTER);
		//jf.getContentPane().add(new JLabel(text), BorderLayout.CENTER);
		jf.setVisible(true);		
	}
	private static Hashtable<String, Vector<DisplayString>> allStatements = new Hashtable<String, Vector<DisplayString>>();
	private String text = null;
	private Vector<DisplayString> displayStrings = new Vector<DisplayString>();
	private Icon icon = null;
//	private int totalWidth = 3000;
	private boolean drawBorder = true;
	public void setText(String text)
	{
		setDisplayStrings(text);
		this.text = text;
	}
	private void setDisplayStrings(String text)
	{
		String tString;
		if(text != null && allStatements.containsKey(text))
		{
			displayStrings = allStatements.get(text);
		}
		else if(text == null)
		{
			displayStrings = null;
			return;
		}
		else
		{
			displayStrings = new Vector<DisplayString>();
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
					tString = HTMLParserHelper.removeAllTags(html.substring(sectionStart, tagStart));
					displayStrings.add(new DisplayString(tString, false));
				}
				if(tagStart < tagEnd)
				{
					tString = HTMLParserHelper.removeAllTags(html.substring(tagStart, tagEnd));
					displayStrings.add(new DisplayString(tString, true));
				}
				sectionStart = tagEnd;
			}
			if(sectionStart < html.length())
			{
				tString = HTMLParserHelper.removeAllTags(html.substring(sectionStart, html.length()));
				displayStrings.add(new DisplayString(tString, false));
			}
			allStatements.put(text, displayStrings);
		}
	}

	public void setIcon(Icon icon)
	{
		this.icon = icon;
	}
	private int getIconWidth()
	{
		if(icon == null)
			return(0);
		else
			return(icon.getIconWidth());
	}
	private int getIconHeight()
	{
		if(icon == null)
			return(0);
		else
			return(icon.getIconHeight());
	}

	private static Dimension preferredSize = new Dimension(0, 20);
	public Dimension getPreferredSize()
	{
		FontMetrics metrics = getFontMetrics(getFont());
		int width;
		int spaceWidth = metrics.stringWidth(" ");
		width = spaceWidth;
		if(icon != null)
			width = width + getIconWidth();
		
		for(DisplayString ds : displayStrings)
			width = width + metrics.stringWidth(ds.getText()); // + spaceWidth;
		preferredSize.setSize(width+1, getMyHeight()+1);
		return(preferredSize);
	}
	public Dimension getMinimumSize()
	{
		return(getPreferredSize());
	}
	public Dimension getMaximumSize()
	{
		return(getPreferredSize());
	}
	private int getMyHeight()
	{
		return(Math.max(getFontMetrics(getFont()).getAscent() + 2, getIconHeight()));
	}
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		int xPos = 0;
		Font oldFont = g.getFont();
		Color oldColor = g.getColor();
		Stroke oldStroke = g2.getStroke();
		g.setFont(getFont());
		Dimension size = getPreferredSize();
		g2.setStroke(new BasicStroke((int) size.getHeight()));
		g.setColor(this.getBackground());
		g.drawRect(0,0, (int) size.getWidth(), (int) size.getHeight());
		g2.setStroke(oldStroke);
		
		int yPos = (getMyHeight() + g.getFontMetrics().getAscent()) / 2;
		if(icon != null)
		{
			//xPos = xPos + g.getFontMetrics().stringWidth(" ");
			icon.paintIcon(this, g, xPos, (getMyHeight() - icon.getIconHeight()) / 2);
			xPos = xPos + getIconWidth();
		}
		xPos = xPos + g.getFontMetrics().stringWidth(" ");
		for(DisplayString ds : displayStrings)
		{
			if(ds.getText() != null)
			{
				int width = g.getFontMetrics().stringWidth(ds.getText());

				g.setColor(ds.getColor());
				g.drawString(ds.getText(),xPos, yPos);
				if(ds.isUnderlined())
					g.drawLine(xPos, yPos + 1, xPos + width, yPos + 1);
				xPos = xPos + width;
			}
		}
		xPos = xPos + g.getFontMetrics().stringWidth(" ");
		
		g.setFont(oldFont);
		g.setColor(oldColor);
		

	}
	private class DisplayString
	{
		private Color color = Color.BLACK;
		private boolean underlined = false;
		private String text = "";
		DisplayString(String text, boolean isLink)
		{
			setText(text);
			if(isLink)
			{
				setColor(Color.BLUE);
				setUnderline(true);
			}
			
		}
		void setText(String text)
		{
			this.text = text;
		}
		String getText()
		{
			return(text);
		}
		void setUnderline(boolean underlined)
		{
			this.underlined = underlined;
		}
		boolean isUnderlined()
		{
			return(underlined);
		}
		void setColor(Color color)
		{
			this.color = color;
		}
		Color getColor()
		{
			return(color);
		}
	}
}

