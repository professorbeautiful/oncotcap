package oncotcap.datalayer;

import java.awt.Color;
import oncotcap.util.GUID;
import oncotcap.display.editor.EditorFrame;

import javax.swing.ImageIcon;

public interface Persistible
{
		public static final int UNSET = 0;
		public static final int CLEAN = 1;
		public static final int DIRTY = 2;
		public static final int DELETED = -1;
		public static final int DO_NOT_SAVE = -2;

	public String getClassName(); 
	public Object getCurrent();
	public String getPrettyName();
	public GUID getGUID();
	public void setGUID(GUID guid);
	public void setGUID(String guid);
	public void setGUID(GUID guid, OncoTCapDataSource ds);
	public void update();
	public boolean equals(Object obj);
	public int hashCode();
	public Object toDataSourceFormat();
	public String toDisplayString();
	public EditorFrame getCurrentEditor();
	public void setCurrentEditor(EditorFrame editor);
	public void addSaveListener(SaveListener listener);
	public void removeSaveListener(SaveListener listener);
	public void fireSaveEvent();
	public void fireDeleteEvent();
	public boolean link(Persistible relatedPersistible);
	public boolean connectedTo(Persistible persistibleObject);
	public void delete();
	public void setPersistibleState(int state);
	public int getPersistibleState();
	public boolean showText();
	public ImageIcon getIcon();
	public Class getSetSource();
	public void setSetSource(Class cls);
	public Color getForeground();
	public void setForeground(Color color);
		public void setCreationTime(String creationTime);
		public String getCreationTime();
		public void setModifier(String modifier);
		public String getModifier();
		public void setModificationTime(String modificationTime);
		public String getModificationTime();
		public Integer getVersionNumber();
		public void setVersionNumber(Integer vn);
}
