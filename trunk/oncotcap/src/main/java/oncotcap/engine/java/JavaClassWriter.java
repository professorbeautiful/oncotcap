package oncotcap.engine.java;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import oncotcap.datalayer.persistible.EnumDefinition;
import oncotcap.display.editor.EditorFrame;
import oncotcap.display.editor.persistibleeditorpanel.Editable;
import oncotcap.engine.*;
import oncotcap.util.Logger;

public class JavaClassWriter
{
	public static void writeClass(ProcessDefinition def, PackageDir packageDir)
	{
		FileWriter fw = null;
		try
		{
			fw = getFile(def, packageDir);
			if(fw != null)
			{
				writeProcess(def, fw, packageDir.packageName);
				fw.close();
			}
		}
		catch(IOException e){Logger.log("ERROR: Can't write to file " + packageDir.packagePath + "." + def.getName() + ".");}
		//catch(CodeProviderParseException e){Logger.log("ERROR: Can't parse all sections of " + def.getName() + "\n" + e.getCodeProvider());
		//	if(e.getCodeProvider() instanceof Editable)
		//	{
		//		EditorFrame.showEditor((Editable) e.getCodeProvider());
		//	}
		//	Logger.log("\n!!!\n" + e.getCode() + "\n!!!\n");
		//}

	}
	private static void writeProcess(ProcessDefinition def, FileWriter fw, String packageName) throws IOException
	{
		System.out.println("__JavaClassWriter.writeProcess: " + def.getName());

		fw.write("package " + packageName + ";\n\n");
		fw.write("// Written " + 
				java.text.DateFormat.getDateInstance().format(new Date()) + "\n");
		fw.write("import java.util.*;\n");
		fw.write("import java.lang.reflect.Array;\n");
		fw.write("import oncotcap.*;\n");
		fw.write("import oncotcap.datalayer.persistible.*;\n");
		fw.write("import oncotcap.sim.*;\n");
		fw.write("import oncotcap.sim.random.*;\n");
		fw.write("import oncotcap.sim.schedule.*;\n");
		fw.write("import oncotcap.process.treatment.*;\n");
		fw.write("import oncotcap.util.*;\n");
		fw.write("import oncotcap.process.*;\n");
		fw.write("import oncotcap.process.adverseevent.*;\n");
		fw.write("import oncotcap.process.cells.*;\n");
		fw.write("import oncotcap.annotation.*;\n");
		
		//class definition
		//if abstract super doesn't exist, use OncProcess
		
		fw.write("public class " + def.getName() + " extends " + def.getType() + "\n{\n");



	//	idVars = getIdVars();
		
		writeMain(def, fw);
		
		writeConstructors(def, fw);

		writeDeclarations(def, fw);

		checkForStandardMethods(def, fw);
		
		if(! def.isCollection())
			writeIdVarMethods(def, fw);
		
		writeMethods(def, fw);

		writeEvents(def, fw);

		if(! def.isCollection())
			writeNewCollectionMethod(def, fw);

		//closing curly for the class definition
		fw.write("\n\n}");

	}
	private static void writeMain(ProcessDefinition def, FileWriter fw) throws IOException
	{
		if(! def.isCollection())
		{
			fw.write("\n\n\tpublic static void main(String [] args)\n\t{\n");
			fw.write("\t\tTopOncParent topP = new TopOncParent();\n");
			fw.write("\t\t" + def.getName() + " o = new " + def.getName() + "(topP);\n");
			fw.write("\t\toncotcap.Oncotcap.handleCommandLine(args);\n");
			fw.write("\t\ttopP.getMasterScheduler().execute();\n");
			fw.write("\t}");
		}
	}
	private static FileWriter getFile(ProcessDefinition def, PackageDir pd) throws IOException
	{
		String fp;
		File outputFile;
		fp = new String(pd.packagePath);
		fp = fp.concat(def.getName());
		fp = fp.concat(".java");

		if ( (outputFile = oncotcap.util.FileHelper.openFileForWrite(fp, true)) == null)
		{
			Logger.log("ERROR: Can't write to file " + fp + ".");
			return(null);
		}
		else
			return(new FileWriter(outputFile));
	}
	private static void writeConstructors(ProcessDefinition def, FileWriter fw) throws IOException
	{
/*		if(hasCollection())
			fw.write("\n\tprivate static OncCollection collection = new " + getName() + "Collection(null);\n");
		else
			fw.write("\n\tprivate static OncCollection collection = null;\n");

		fw.write("\n\tpublic OncCollection getCollection(){ return(collection); }\n");
*/
		if(! def.isCollection())
		{
			fw.write("\n\tpublic " + def.getName() + "(OncParent parent, OncEnum [] levels)\n\t{\n");
			fw.write("\t\tsuper(parent);\n");
			fw.write("\t\tif(levels != null)\n\t\t{\n");
//			fw.write("\t\t\tidEnums = levels;\n");
			fw.write("\t\t\tfor(int n = 0; n < levels.length; n++)\n");
			fw.write("\t\t\t\tchangeProp(levels[n]);\n");
			fw.write("\t\t}\n");
//			fw.write("\t\tlocalInit();\n");
			fw.write("\t}\n");

			fw.write("\n\n\tpublic " + def.getName() + "(OncParent parent)\n\t{\n");
			fw.write("\t\tsuper(parent);\n");
//			fw.write("\t\tlocalInit();\n");
			fw.write("\t}\n");
		}
		else //is a collection
		{
			fw.write("\n\tpublic " + def.getName() + "(OncEnum [] levels, MasterScheduler sched, EventManager evMgr)\n\t{\n");
			fw.write("\t\tsuper(sched, evMgr);\n");
//			fw.write("\t\tif(levels != null)\n\t\t{\n");				
//			fw.write("\t\t\tfor(int n = 0; n < levels.length; n++)\n");
//			fw.write("\t\t\t\tchangeProp(levels[n]);\n");
//			fw.write("\t\t}\n");
			fw.write("\t}\n");

			fw.write("\n\n\tpublic " + def.getName() + "(MasterScheduler sched, EventManager evMgr)\n\t{\n");
			fw.write("\t\tsuper(sched, evMgr);\n");
			fw.write("\t}\n");

			fw.write("\n\n\tpublic OncCollection getCollection()\n");
			fw.write("\t{\n\t\treturn(this);\n\t}\n");
		}
	}
	private static void writeDeclarations(ProcessDefinition def, FileWriter fw) throws IOException
	{
		fw.write("//BEGIN_DECLARATION_SECTION\n");
		
		
		writeDeclarationSection(def.getDeclarationSection(), fw);

			//loop through all events and register them with the event
		//handler
		int evN = 0;
		for(ClassSection event : def.getEvents())
		{
			fw.write("\t\t String event" + (evN++) + " = getEventManager().registerEvent(this, " + "\"" + event.getName() + "\");\n");
		}
		fw.write("\n//END_DECLARATION_SECTION\n");
	}
	private static void writeDeclarationSection(ClassSection sect, FileWriter fw) throws IOException
	{
		for(InstructionAndValues iav : sect.getInstructionsAndValues())
		{
			writeInstruction(iav, fw);
		}
	}
	private static void writeInstruction(InstructionAndValues iav, FileWriter fw) throws IOException
	{
		String code = JavaCodeProvider.getCode(iav);
		fw.write(code);
	}
	private static void writeMethods(ProcessDefinition def, FileWriter fw) throws IOException
	{
		for(ClassSection sect : def.getMethods())
			writeMethodSection(sect, fw);
	}
	private static void writeMethodSection(ClassSection sect, FileWriter fw) throws IOException
	{
		String name = sect.getName();
		System.out.println("______JavaClassWriter.writeMethodSection: " + name);
		fw.write("\tpublic void " + name + "()\n\t{\n");
		if(name.equals("init"))
			fw.write("\t\tsuper.init();\n");
		else if(name.equals("update"))
			fw.write("\t\tsuper.update();\n");
		else if(name.equals("collection_update"))
			fw.write("\t\tsuper.collection_update();\n");
		for(InstructionAndValues iav : sect.getInstructionsAndValues())
		{
			writeInstruction(iav, fw);
		}
		fw.write("\t}\n");
	}
	
	//if update and init sections aren't contained in this process def write blank ones
	//also write default collection update method for collections
	private static void checkForStandardMethods(ProcessDefinition def, FileWriter fw) throws IOException
	{
		if(def.isCollection())
		{
			fw.write("\n\tpublic void collection_update(Object proc, EventParameters params)\n\t{\n");
			fw.write("\tcollection_update();\n\t}\n");
		}
		fw.write("\n\tpublic void update(Object proc, EventParameters params)\n\t{\n");
		fw.write("\tupdate();\n\t}\n");
		
		if(! containsSectionNamed("init", def.getMethods()))
		{
			//blank init section
			fw.write("\n\npublic void init()\n{\n");
			fw.write("\n\tsuper.init(); //this line added by precompiler\n");
			if(def.isCollection())
				fw.write("\n\tcollection_init(); //this line added by precompiler\n");
 			fw.write("}\n");
		}

		if(! containsSectionNamed("update", def.getMethods()))
			fw.write("\n\n\tpublic void update()\n\t{\n\t\tsuper.update();\n\t}\n");
	}
	private static boolean containsSectionNamed(String name, Collection<ClassSection> sections)
	{
		for(ClassSection sect : sections)
		{
			if(sect.getName().equals(name))
				return(true);
		}
		return(false);
	}
		
	private static void writeEvents(ProcessDefinition def, FileWriter fw) throws IOException
	{
		for(ClassSection sect : def.getEvents())
			writeEventSection(sect, fw);
	}
	private static void writeEventSection(ClassSection sect, FileWriter fw) throws IOException
	{
		String name = sect.getName();
		fw.write("\tpublic void " + name + "(Object caller, EventParameters eventParameters)\n\t{\n");
		fw.write("\t\ttry\n\t\t{\n");
		for(InstructionAndValues iav : sect.getInstructionsAndValues())
		{
			writeInstruction(iav, fw);
		}
		fw.write("\n\t\t}\n");
		fw.write("\t\tcatch(Exception e)\n\t\t{\n");
		fw.write("\t\t\tif(e instanceof ArgumentNotFoundException)\n\t\t\t{\n");
		fw.write("\t\t\t\tSystem.out.println(e.toString());\n");
		fw.write("\t\t\t}\n");
		fw.write("\t\t\telse if(e instanceof ConversionException)\n\t\t\t{\n");
		fw.write("\t\t\t\tSystem.out.println(e.toString());\n");
		fw.write("\t\t\t}\n");
		fw.write("\t\t\telse\n\t\t\t{\n");
		fw.write("\t\t\t\tSystem.out.println(\"ERROR: Exception in event " + name + "\\n\" + e.getCause().toString());\n");
		fw.write("\t\t\t}\n");
		fw.write("\t\t}\n");
		fw.write("\t}\n");

	}
	private static void writeNewCollectionMethod(ProcessDefinition def, FileWriter fw) throws IOException
	{
		fw.write("\tpublic OncCollection newCollectionInstance()\n\t{\n");
		fw.write("\t\treturn(new " + def.getName() + "Collection(getMasterScheduler(), getEventManager()));\n\t}\n");
	}
	private static void writeIdVarMethods(ProcessDefinition def, FileWriter fw) throws IOException
	{
		EnumDefinition id;
		boolean firstTime;
		String name = def.getName();
			//clone method w/o args
		fw.write("\n\npublic Object clone()\n{\n");
		fw.write("\treturn(clone(this.getParent()));\n");
		fw.write("}\n");


		fw.write("\n\n\tpublic Object clone(OncParent par)\n{\n");
		fw.write("\t\t" + name + " rv = new " + name + "(par);\n");
		fw.write("\t\tOncEnum [] enums = getIdEnums();");
		fw.write("\t\tfor(int n = 0; n < enums.length; n++)\n\t\t{\n");
		fw.write("\t\t\trv.changeProp(getIdEnums());\n\t\t}\n");
		fw.write("\t\treturn(rv);\n");
		fw.write("}\t\n");

		fw.write("\n\n\tpublic Object clone(OncEnum newLevel)\n\t{\n");
		fw.write("\n\t\tOncEnum [] oldIdEnums = getIdEnums();\n");
		fw.write("\t\tOncEnum [] newLevels = (OncEnum []) Array.newInstance(OncEnum.class, oldIdEnums.length);\n");
		fw.write("\t\tfor(int n = 0; n < newLevels.length; n++)\n\t\t{\n");
		fw.write("\t\t\tif(oldIdEnums[n].getClass().equals(newLevel.getClass()))\n\t\t\t{\n");
		fw.write("\t\t\t\tnewLevels[n] = newLevel;\n\t\t\t}\n");
		fw.write("\t\t\telse\n");
		fw.write("\t\t\t\tnewLevels[n] = oldIdEnums[n];\n\t\t}\n");
		fw.write("\t\treturn(new " + name + "(this.getParent(), newLevels));\n\t}\n");
		
		fw.write("\n\n\tpublic Object clone(OncEnum [] newLevels)\n\t{\n");
		fw.write("\t\tboolean found;\n");
		fw.write("\t\tOncEnum [] oldIdEnums = getIdEnums();\n");
		fw.write("\t\tOncEnum [] cloneLevels = (OncEnum []) Array.newInstance(OncEnum.class, oldIdEnums.length);\n");
		fw.write("\t\tfor(int n = 0; n < cloneLevels.length; n++)\n\t\t{\n");
		fw.write("\t\t\tfound = false;\n");
		fw.write("\t\t\tfor(int i = 0; i < newLevels.length; i++)\n\t\t\t{\n");
		fw.write("\t\t\t\tif(oldIdEnums[n].getClass().equals(newLevels[i].getClass()))\n\t\t\t\t{\n");
		fw.write("\t\t\t\t\tcloneLevels[n] = newLevels[i];\n");
		fw.write("\t\t\t\t\tfound = true;\n\t\t\t\t}\n\t\t\t}\n");
		fw.write("\t\t\tif(!found)\n");
		fw.write("\t\t\t\tcloneLevels[n] = oldIdEnums[n];\n\t\t}\n");
		fw.write("\t\treturn(new " + name + "(this.getParent(), cloneLevels));\n\t}\n");
	}
}
