package oncotcap.datalayer.persistible;

import java.lang.reflect.*;
import oncotcap.util.*;
import oncotcap.display.common.*;
import oncotcap.display.editor.*;
import oncotcap.display.editor.persistibleeditorpanel.*;

public class TypeHelper
{
	private Class editor;
	private Class storage;
	private Class [] storageAry = { storage };
	
	public TypeHelper(String editor, String storage)
	{
		this.editor = ReflectionHelper.classForName(editor);
		if(this.editor == null)
			System.out.println("Cannot get class for " + editor + " [TypeHelper]");
		setStorageClass(ReflectionHelper.classForName(storage));
	}
	public Class getEditorClass()
	{
		return(editor);
	}
	public Class getStorageClass()
	{
		return(storage);
	}
	public void setStorageClass(Class storage)
	{
		this.storage = storage;
		storageAry[0] = storage;
	}
	public PrimitiveData getStorageInstance()
	{
		try
		{
			PrimitiveData storageInst = (PrimitiveData) storage.newInstance();
			return(storageInst);
		}
		catch(InstantiationException e){System.out.println("Warning: unable to instantiate " + storage);}
		catch(IllegalAccessException e){System.out.println("Warning: Illegal access trying to instantiate " + storage);}

		return(null);
	}
	public EditorPanel getEditorInstance()
	{
		try
		{
			return((EditorPanel) editor.newInstance());
		}
		catch(InstantiationException e){System.out.println("Warning: unable to instantiate " + editor);}
		catch(IllegalAccessException e){System.out.println("Warning: Illegal access trying to instantiate " + editor);}
		
		return(null);
	}
	public EditorPanel getEditorInstanceWithStorage()
	{
		PrimitiveData si = getStorageInstance();
		EditorPanel oe = getEditorInstance(si);
		return(oe);
	}
	public EditorPanel getEditorInstance(Object var)
	{
		Object [] args = { var };
		try
		{
			Constructor construct = editor.getDeclaredConstructor(storageAry);
			return((EditorPanel)construct.newInstance(args));
		}
		catch(NoSuchMethodException e){System.out.println("Warning: unable to instantiate " + editor + " constructor taking only " + storage + " does not exist.");}
		catch(SecurityException e){System.out.println("Warning: unable to instantiate " + editor + ". Security does not permit.");}
		catch(InstantiationException e){System.out.println("Warning: unable to instantiate " + editor);}
		catch(InvocationTargetException e){System.out.println("Warning: unable to instantiate " + editor + ". Invocation problem.\n" + e.getCause()); e.printStackTrace();}
		catch(IllegalArgumentException e){System.out.println("Warning: unable to instantiate " + editor + ". Due to an illegal argument.");}
		catch(IllegalAccessException e){System.out.println("Warning: Illegal access trying to instantiate " + editor);}

		return(null);
	}
}