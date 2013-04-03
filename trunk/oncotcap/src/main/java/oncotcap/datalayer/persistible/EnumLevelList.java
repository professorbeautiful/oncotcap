package oncotcap.datalayer.persistible;

import java.util.*;
import oncotcap.datalayer.AbstractPersistible;
import oncotcap.datalayer.Persistible;
import oncotcap.util.*;
import javax.swing.ImageIcon;
import oncotcap.display.editor.persistibleeditorpanel.*;
import oncotcap.engine.*;

public class EnumLevelList extends AbstractPersistible
	implements Editable
{
		private static String ARROW = " " + 
				(new Character((char) 0x279c)).toString() + " ";
		private static String STAR = (new Character((char) 0x2605)).toString();
		//private static String BULLET = (new Character((char) 0x2022)).toString();
		//private static String BULLET = (new Character((char) 0x22c4)).toString();
		private static String BULLET = " " + 
				(new Character((char) 0x2023)).toString() + " ";
	public String name;
	private SortedList<EnumLevel> levelList = new SortedList<EnumLevel>(new EnumLevelCompare());
	private boolean ordered = false;
	private boolean numericList = false;
	private String minValue;
	private String maxValue;
	private String increment;
	private boolean arithmatic = true;
	private Keyword keyword = null;
	private EditorPanel editorPanel = null;
	private ImageIcon icon =	
			oncotcap.util.OncoTcapIcons.getImageIcon("enumlevellist.jpg");

	public EnumLevelList(oncotcap.util.GUID guid) {
		super(guid);
	}
	public EnumLevelList()
	{
		this(true);
	}
	public EnumLevelList(boolean saveToDataSource)
	{
		if(!saveToDataSource)
			setPersistibleState(Persistible.DO_NOT_SAVE);
	}
	public static void main(String [] args)
	{
		Iterator it = oncotcap.Oncotcap.getDataSource().find(ReflectionHelper.classForName("oncotcap.datalayer.persistible.EnumLevelList")).iterator();
		while(it.hasNext())
		{
			EnumLevelList epl = (EnumLevelList) it.next();
			Iterator it2 = epl.getLevels().iterator();
			while(it2.hasNext())
				System.out.println(it2.next());
		}
	}
	
	
	public Object clone()
	{
		EnumLevelList rList = new EnumLevelList();
		if(name != null)
			rList.name = new String(name);
		
		Iterator it = levelList.iterator();
		while(it.hasNext())
			rList.addLevel(new String(it.next().toString()));
		return(rList);
	}
	public String getName()
	{
		return(name);
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public Boolean isOrderedAsBoolean()
	{
		return(new Boolean(ordered));
	}
	public boolean isOrdered()
	{
		return(ordered);
	}
	public void setOrdered(Boolean ordered)
	{
		if(ordered == null)
			this.ordered = false;
		else
			this.ordered = ordered.booleanValue();
	}
	public void setOrdered(boolean ordered)
	{
		this.ordered = ordered;
	}
	public Boolean isNumericListAsBoolean()
	{
		return(new Boolean(numericList));
	}
	public boolean isNumericList()
	{
		return(numericList);
	}
	public void setNumericList(Boolean numeric)
	{
		if(numeric == null)
			numericList = false;
		else
			numericList = numeric.booleanValue();
	}
	public void setNumericList(boolean numeric)
	{
		numericList = numeric;
	}
	public String getMinAsString()
	{
		return(minValue);
	}
	public String getMin()
	{
		return(minValue);
	}

	public void setMin(String min)
	{
		if(min == null)
			minValue = "0.0";
		else
			minValue = min;
	}
	public String getMaxAsString()
	{
		return(maxValue);
	}
	public String getMax()
	{
		return(maxValue);
	}
	public void setMax(String max)
	{
		if(max == null)
			maxValue = "0.0";
		else
			maxValue = max;
	}

	public String getIncrementAsString()
	{
		return(increment);
	}
	public String getIncrement()
	{
		return(increment);
	}
	public void setIncrement(String inc)
	{
		if(inc == null)
			increment = "1.0";
		else
			increment = inc;
	}

	public Boolean isArithmaticAsBoolean()
	{
		return(new Boolean(arithmatic));
	}
	public boolean isArithmatic()
	{
		return(arithmatic);
	}
	public void setArithmatic(Boolean arith)
	{
		if(arith == null)
			arithmatic = false;
		else
			arithmatic = arith.booleanValue();
	}
	public void setArithmatic(boolean arith)
	{
		arithmatic = arith;
	}
	public Keyword getKeyword()
	{
		return(keyword);
	}
	public void setKeyword(Keyword keyword)
	{
		this.keyword = keyword;
		if ( keyword != null)
				keyword.link(this);
	}
	public ImageIcon getIcon() {
			return icon;
	}
	public boolean showText() {
				return false;
	}
	public SortedList<EnumLevel> getLevels()
	{
		return(levelList);
	}
	public int getNumberOfLevels()
	{
		if (levelList != null && levelList.size() > 0)
			return levelList.size();
		else
			return 0;
	}

	public void setLevels(Collection levels)
	{
		Iterator it = levels.iterator();
		while(it.hasNext())
		{
			Object obj = it.next();
			if(obj instanceof EnumLevel)
				addLevel((EnumLevel) obj);
		}
	}
	public void addLevel(EnumLevel level)
	{
		if(! levelList.contains(level))
			levelList.add(level);
	}
	public EnumLevel addLevel(String level)
	{
		if(level != null)
		{
			EnumLevel levelToAdd = new EnumLevel(level);
			addLevel(levelToAdd);
			return(levelToAdd);
		}
		else
			return(null);
	}
	public Iterator<EnumLevel> getLevelIterator()
	{
		return(levelList.iterator());
	}
	public EnumLevel getLevel(String name)
	{
		EnumLevel el = null;
		Iterator it = levelList.iterator();
		String trimmedName = name.trim();
		while(it.hasNext())
		{
			el = (EnumLevel) it.next();
			if(el.getName().trim().equalsIgnoreCase(trimmedName))
				return(el);
		}
		return(el);
	}
	public boolean contains(String val)
	{
		if(val == null)
			return(false);
		else
			return(levelList.contains(val.trim().toUpperCase()));
	}
	public void removeLevel(String prop)
	{
		if(prop != null)
			levelList.remove(new EnumLevel(prop));
	}
	public void removeLevel(EnumLevel level)
	{
		if(level != null)
			levelList.remove(level);
	}
	class LevelIterator implements Iterator
	{
		Iterator<EnumLevel> iter;
		LevelIterator()
		{
			iter = levelList.iterator();
		}
		public boolean hasNext()
		{
			return(iter.hasNext());
		}
		public  Object next()
		{
			return(iter.next().getName());
		}
		public void remove(){}
				
	}
		// Editable
		public EditorPanel getEditorWithInstance()
		{
				if (editorPanel == null)
						editorPanel = new LevelListEditorPanel(this);
				return(editorPanel);
		}
		public EditorPanel getEditorPanelWithInstance()
		{
				if (editorPanel == null)
						editorPanel = new LevelListEditorPanel(this);
				return(editorPanel);
		}
		public EditorPanel getEditorPanel()
		{
				if (editorPanel == null)
						editorPanel = new LevelListEditorPanel();
				return(editorPanel);
		}
		public String toString() {
				if ( isNumericList() )
						return getNumericLevels();
				else 
						return getStringLevels();
		}
				//return getKeyword() + "--> " + levelList;

					// 	StringBuffer levelListAsString = new StringBuffer();
// 						levelListAsString.append(getMinAsString());
// 						levelListAsString.append(" to ");
// 						levelListAsString.append(getMaxAsString());
// 						//levelListAsString.append("  ");
// 						if ( isArithmatic() )
// 								levelListAsString.append(" by ");
// 						else 
// 								levelListAsString.append(" by x ");
// 						levelListAsString.append(getIncrementAsString());
// 						return levelListAsString.toString();

		private String getNumericLevels() {
				String labelString = getKeyword() + 
						" [ " + 
						getMinAsString()
						+ " to " 
						+ getMaxAsString()
						+ " ] Increment by " 
						+ getIncrementAsString();
				return labelString;

		}
		private String getStringLevels() {
				StringBuilder str = new StringBuilder();
				str.append(getKeyword());
				str.append(ARROW);
				Iterator i = getLevelIterator();
				EnumLevel level = null;
				boolean firstLevel = true;
				while (i != null && i.hasNext() ) {
						if ( firstLevel ) 
								firstLevel = false;
						else
								str.append(BULLET);
						level = (EnumLevel)i.next();
						str.append(level.toString());
				}
				return str.toString();
		}
		public EnumLevelList cloneSubstitute(ValueMapPath path)
		{			
			EnumLevelList ell = new EnumLevelList(false);
			ell.name = path.substituteJavaName(name);
			ell.minValue = path.substitute(minValue);
			ell.maxValue = path.substitute(maxValue);
			ell.increment = path.substitute(increment);
			ell.ordered = ordered;
			ell.numericList = numericList;
			ell.arithmatic = arithmatic;
			Iterator it = levelList.iterator();
			while(it.hasNext())
			{
				ell.addLevel(((EnumLevel) it.next()).cloneSubstitute(path));
			}
			return(ell);
		}
		
		public void union(EnumLevelList uList)
		{ 
			if(uList != null)
			{
				if(uList.isNumericList() || this.isNumericList())
				{
					System.out.println("WARNING: The union of Numeric is not supported!");
					return;
				}
				for(EnumLevel uEntry : uList.getLevels())
				{
//					if(uEntry.toString().equals("MUT-MUT") || uEntry.toString().equals("MUT_MUT"))
//						System.out.println("HERE");
					if(! levelList.contains(uEntry))
						levelList.add(uEntry);
				}
			}
		}
}
