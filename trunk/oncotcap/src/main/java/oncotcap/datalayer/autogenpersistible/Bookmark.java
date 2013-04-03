package oncotcap.datalayer.autogenpersistible;

import javax.swing.ImageIcon;
import javax.swing.*;
import java.lang.reflect.*;

import oncotcap.datalayer.*;
import oncotcap.datalayer.persistible.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.autogeneditorpanel.*;


public class Bookmark extends AutoGenPersistibleWithKeywords 
 {
	private Integer bookmarkLocation;


public Bookmark() {
init();
}


public Bookmark(oncotcap.util.GUID guid) {
	super(guid);
	init();
}
private void init() {
	Method setter = null;
	Method getter = null;
	setter = getSetter("setBookmarkLocation", Integer.class);
	setMethodMap.put("bookmarkLocation", setter);
	getter = getGetter("getBookmarkLocation");
	getMethodMap.put("bookmarkLocation", getter);
}

	public Integer getBookmarkLocation(){
		return bookmarkLocation;
	}
	public void setBookmarkLocation(Integer var ){
		bookmarkLocation = var;
	}

	public String toString() {
		return getGUID().toString();
	}
	public Class getPanelClass()
	{
		return BookmarkPanel.class;
	}
	public String getPrettyName()
	{
		return "Bookmark";
	}
	public ImageIcon getIcon()
	{
		return icon;
	}
}
