package oncotcap.util;

import java.io.StringWriter;
import java.util.*;
import java.util.regex.*;

public class StringHelper
{
	//used by getUniqueVariableName
	private static int varCount = 0;
	public final static String emptyString = "";
	
	
	//la la la
	public static void main(String [] args)
	{
		System.out.println(replaceNewLineChars("blech\nblock"));
		//System.out.println(replaceHTMLSpacesAndCRs("<html> blech argh 123<body abc>mor estuf\n fa b<a >12345</a></body asd asfd> asdjasdjfadsf </moretags>"));
		//System.out.println("*" + javaNameKeepBQuotes("Cancer_cell") + "*");
		//System.out.println("*" + javaNameKeepBQuotes("ble/ch`some stuff`blec h") + "*");
		//System.out.println("blech");
		//System.out.println(javaName("c@a.se"));
		//System.out.println(javaName("b-arf*"));
		//System.out.println(javaName("barf"));
		//System.out.println("*" + javaName("@nomangle()") + "*");
		//System.out.println(javaName("@nomangle(a)"));
//		System.out.println(isJavaKeyword("if"));
//		System.out.println(isJavaKeyword("abstract"));
//		System.out.println(isJavaKeyword("blech"));
//		System.out.println(htmlToText("<html>   <head>        </head>   <body>     <p>       The good news: SB is nicely legible. The badnews: you can't fill in        numbers or treatment schedules in SB's with upstream parameters.     </p>     <p>       The regimen calls for&#160; <a id=\"686cd3a6000000cc000000f745becb26\" href=\"686cd3a6000000cc000000f745becb26\"> Drug A</a> to be given with schedule&#160; <a id=\"888e66b80000013c000000f748b7ba05\" href=\"888e66b80000013c000000f748b7ba05\"> Day List A</a> <a id=\"888e66b80000013a000000f748b7b9fb\" href=\"888e66b80000013a000000f748b7b9fb\"> Duration A</a> <a id=\"888e66b80000013b000000f748b7b9fb\" href=\"888e66b80000013b000000f748b7b9fb\"> Courses A</a>     </p>     <p>       [&lt;== can't delete this newline] and <a id=\"14c0db70000000d0000000f745bf22a8\" href=\"14c0db70000000d0000000f745bf22a8\"> Drug B</a> &#160;to be given with schedule&#160;&#160; <a id=\"888e66b800000140000000f748b8615e\" href=\"888e66b800000140000000f748b8615e\"> Day List B</a> <a id=\"888e66b80000013e000000f748b8615e\" href=\"888e66b80000013e000000f748b8615e\"> Duration B</a> <a id=\"888e66b80000013f000000f748b8615e\" href=\"888e66b80000013f000000f748b8615e\"> Courses B</a>      </p>   </body> </html> "));
/*		System.out.println(isNumeric("adsf"));
		System.out.println(isNumeric(""));
		System.out.println(isNumeric(null));
		System.out.println(isNumeric("44"));
		System.out.println(isNumeric("1.2"));
*/
//		System.out.println(padToLength("12", '0', 10));
//		System.out.println("1234567890");
//		Logger.setConsoleLogging(true);
//		Logger.log("A" + noQuotes(" ") + "A");
		//Logger.log("*" + getHTMLBody("asdfasd<body>blech</body>") + "*");
		//Logger.log("*" + removeWhiteSpace("\t a b  c") + "*");
		//Logger.log("$$"+chopWord("::::::",':')+"$$");
		/*substitute_test("abcdefg", "abc", "XYZ");
		substitute_test("abcdefg", "bcd", "XYZ");
		substitute_test("abcdefg", "efg", "XYZ");
		substitute_test("abcdefg", "000", "XYZ");
		substitute_test("abcdefg", "b", "bbb");
		substitute_test("abcdefgabcdefg", "b", "bbb"); */
	}

	public static void substitute_test(String s, String in, String out){
		Logger.log(" Substituting " + out + " for " + in + " in " + s);
		Logger.log(" Result: " + StringHelper.substitute(s, in, out) + "\n");
	}
	public static String substituteInPlace(String s, String in, String out){
		int i;
		int len = in.length();
		int upper = s.length();
			// Caution! this is subject to infinite loop!
		while ( upper >= 0 && (i = s.substring(0,upper).lastIndexOf(in)) >= 0){
				//Logger.log("Replacing at i=" + i + "  upper = " + upper );
			s = (i>0 ? s.substring(0,i) : "")
				  .concat(out).concat(
									  (i+len <= s.length() ?  s.substring(i+len,s.length()) : "")
									 );
			upper = i-1;
		}
		return(s);
	}


	/**
	 **  checks to see if the string inputed can be converted to a
	 **  numeric value.
	 **
	 **  @returns true if the value is numeric, false if it it isn't
	 **/
	public static boolean isNumeric(String number)
	{
		double tVal;
		
		if(number == null || number.trim().equals(""))
			return(false);

		try{ tVal = Double.parseDouble(number); }
		catch(NumberFormatException e){ return(false); }

		return(true);
	}
	
	/**
	 **  checks to see if the string inputed can be converted to an
	 **  integer value.
	 **
	 **  @returns true if the value is integer, false if it it isn't
	 **/
	public static boolean isInteger(String number)
	{
		int tVal;
		if(number == null || number.trim().equals(""))
			return(false);

		try{tVal = Integer.parseInt(number);}
		catch(NumberFormatException e){return(false);}

		return(true);
	}
	/**
	 ** substitute Substitutes the string <code>out</code> for all
	 ** occurrences of <code>in</code> in the target string
	 ** <code>s</code>.
	 **/
	public static String substitute(String s, String in, String out)
	{
		String s2 = new String(s);
		return(substituteInPlace(s2, in, out));
	}

		public static String substituteRegEx(Pattern pattern,
																				 String originalString,
																				 String replacementString) {
				StringBuffer newStringBuffer = new StringBuffer();
				int idx = 0;
				Matcher matcher = pattern.matcher(originalString);
				while(matcher.find()) {
						newStringBuffer.append(originalString.substring(idx, matcher.start()));
						// System.out.println("MATCHED " 
// 															 + originalString.substring(matcher.start(), 
// 																													matcher.end()) 
// 															 + "END");
						newStringBuffer.append(replacementString);
						idx = matcher.end();
				}
				newStringBuffer.append(originalString.substring(idx, originalString.length()));
				return newStringBuffer.toString();
		}

	public static String removeWhiteSpace(String body)
	{
		int n;
		int i = 0;
		int len = body.length();
		char [] data = new char[len];
		char [] rdata = new char[len];
		body.getChars(0, body.length(), data, 0);
		for (n=0; n<len; n++)
		{
			if(data[n] != ' ' && data[n] != '\t' && data[n] != '\n')
				rdata[i++] = data[n];
		}
		return(new String(rdata, 0, i));
	}
	public static String removeEOL(String body)
	{
		int n;
		int i = 0;
		int len = body.length();
		char [] data = new char[len];
		char [] rdata = new char[len];
		body.getChars(0, body.length(), data, 0);
		for (n=0; n<len; n++)
		{
			if(data[n] != ' ' && data[n] != '\r' && data[n] != '\n' && data[n] != '\f')
				rdata[i++] = data[n];
		}
		return(new String(rdata, 0, i));
		
	}
	public static String getLine(String body)
	{
		if (body == null)
			return(null);

		char[] cBody = body.toCharArray();
		int lineLen = cBody.length;
		int n=0;
		
		while(n<lineLen && cBody[n] != '\n') n++;

		
		return(new String(cBody,0,n));
	}

	public static String chopLine(String body)
	{
		if (body == null)
			return(null);

		char[] cBody = body.toCharArray();
		int lineLen = cBody.length;
		int n=0;

		while(n<lineLen && cBody[n] != '\n') n++;
		while(n<lineLen && cBody[n] == '\n') n++;
		
		return(new String(cBody, n, lineLen - n));
	}
	public static String getWord(String line)
	{
		if (line == null)
			return(null);

		if (line.length() == 0)
			return(new String());
					
		int i, b;
		char rs;
		char[] rets = line.toCharArray();

		boolean done = false;
		int lineLen = rets.length;

		b=0;
		while(b<lineLen && ((rs = rets[b++]) == ' ' || rs == '\t'));

		b--;

		i = b;
		while (i < lineLen && (rs = rets[i++]) != ' ' && rs != '\t');
		if (i < lineLen || (i==lineLen && (rets[i-1] == ' ' || rets[i-1] == '\t'))) i = i - 2;
		else i--;

		return(new String(rets, b, i-b+1));

	}
	public static String getWord(String line, char delimiter)
	{
		if (line == null)
			return(null);

		if (line.length() == 0)
			return(new String());
		
		int i, b;
		char[] rets = line.toCharArray();
		boolean done = false;
		int lineLen = rets.length;

		b=0;
		while(b<lineLen && rets[b++] == delimiter);

		b--;

		i = b;
		while (i < lineLen && rets[i++] != delimiter);
		if (i < lineLen || (i==lineLen && rets[i-1] == delimiter)) i = i - 2;
		else i--;

		return(new String(rets, b, i-b+1));
	}

	public static String chopWord(String line)
	{
		if (line == null)
			return(null);

		if (line.length()==0)
			return(new String());
		
		int i, b, c;
		char rs;
		char[] rets = line.toCharArray();

		boolean done = false;
		int lineLen = rets.length;

		b=0;
		while(b<lineLen && ((rs = rets[b++]) == ' ' || rs == '\t'));

		b--;

		i = b;
		while (i < lineLen && (rs = rets[i++]) != ' ' && rs != '\t');
		if (i < lineLen || (i==lineLen && (rets[i-1] == ' ' || rets[i-1] == '\t'))) i = i - 2;
		else i--;

		c = ++i;
		if(! (c==lineLen))
		{
			while (c< lineLen && ((rs = rets[c++]) == ' ' || rs == '\t'));
			if (rets[c-1] != ' ' || rets[c-1] != '\t')c--;
		}

		return(new String(rets, c, lineLen-c));
	}

	public static String chopWord(String line, char delimiter)
	{
		if (line == null)
			return(null);

		if (line.length() == 0)
			return(new String());
		
		int i, b, c;
		char[] rets = line.toCharArray();

		boolean done = false;
		int lineLen = rets.length;

		b=0;
		while(b<lineLen && rets[b++] == delimiter);

		b--;

		i = b;
		while (i < lineLen && rets[i++] != delimiter);
		if (i < lineLen || (i==lineLen && rets[i-1] == delimiter)) i = i - 2;
		else i--;

		c = ++i;
		if(! (c==lineLen))
		{
			while (c< lineLen && rets[c++] == delimiter);
			if (rets[c-1] != delimiter) c--;
		}
		return(new String(rets, c, lineLen-c).trim());
	}
	public static String getHTMLBody(String htmlSource)
	{
		String lower = new String(htmlSource.toLowerCase());
		int first = lower.indexOf("<body>");
		int last = lower.indexOf("</body>");
		Logger.log("FIRST: " + first + " LAST: " + last);
		if(first >= 0 && last >= 6) 
			return(htmlSource.substring(first+6, last));
		else
			return(htmlSource);
	}

	private static Pattern htmlHead = Pattern.compile("^\\s*<html>.*<body>\\s*", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
	private static Pattern htmlHeadTag = 
			Pattern.compile("<head>", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
	private static Pattern htmlTag = Pattern.compile("</?[.[^>]]*>", Pattern.DOTALL);
	private static Pattern pHtmlTag = Pattern.compile("<p.*?>", Pattern.DOTALL);
	private static Pattern brHtmlTag = Pattern.compile("<br>", Pattern.DOTALL);
	private static Pattern pEndHtmlTag = Pattern.compile("</p>", Pattern.DOTALL);
	private static Pattern htmlSpecialChar = Pattern.compile("&[.[^;]]*;", Pattern.DOTALL);
	private static Pattern htmlBodyTag = Pattern.compile("<body[.[^>]]*>", Pattern.CASE_INSENSITIVE + Pattern.DOTALL);
	private static Pattern htmlBodyEndTag = Pattern.compile("</body[.[^>]]*>", Pattern.CASE_INSENSITIVE + Pattern.DOTALL);

	public static Pattern quotedVar = Pattern.compile("`.*?`", Pattern.DOTALL);
	
	public static String htmlToText(String htmlSource)
	{
			if (htmlSource == null)
					return htmlSource;
		Matcher mat = htmlHead.matcher(htmlSource);
		if ( mat == null )
			return htmlSource;
		String rVal = mat.replaceAll("");

		mat = htmlTag.matcher(rVal);
		rVal = mat.replaceAll("");
		mat = htmlSpecialChar.matcher(rVal);
		rVal = mat.replaceAll("");

		return(rVal.trim());
	}

	public static String htmlNoParagraphs(String htmlSource)
	{
		Matcher mat = pHtmlTag.matcher(htmlSource);
		String rVal = mat.replaceAll("");
		mat = pEndHtmlTag.matcher(rVal);
		rVal = mat.replaceAll("");
		mat = brHtmlTag.matcher(rVal);
		rVal = mat.replaceAll("");
		return(rVal.trim());
	}

	public static String addHtmlStyle(String htmlSource) {
			// Plop in an empty style tag after the header
			if (htmlSource == null)
					return htmlSource;
			Matcher mat = htmlHeadTag.matcher(htmlSource);
			if ( mat == null )
					return htmlSource;
			String rVal = mat.replaceAll("<head><style TYPE=\"text/css\"> BODY { color: black; font-type:TimesNewRoman;} </style>");
			return(rVal.trim());
	}

	public static String noQuotes(String strIn)
	{
		String rVal = new String(strIn.trim());
		if(rVal.length() >= 1 && rVal.substring(0, 1).equals("\""))
			rVal = rVal.substring(1);

		if(rVal.length() >= 1 && rVal.substring(rVal.length()-1, rVal.length()).equals("\""))
			rVal = rVal.substring(0,rVal.length() - 1);
		
		return(rVal);
	}
	public static String removeQuotesOS(String strIn)
	{
		if(! (Util.getOS() == Util.WINDOWSOS))
			return(noQuotes(strIn));
		else
			return(strIn);
	}
	public static String addQuotesOS(String strIn)
	{
		if( Util.getOS() == Util.WINDOWSOS)
			return("\"" + strIn + "\"");
		else
			return(strIn);
	}
	public static String padToLength(String stringToPad, char characterToPadWith, int length)
	{
		if(stringToPad.length() >= length)
			return(stringToPad);
		else
		{
			StringBuffer strTack = new StringBuffer(stringToPad).reverse();
			for(int n = stringToPad.length() + 1; n <= length; n++)
			{
				strTack = strTack.append(characterToPadWith);
			}
			return(strTack.reverse().toString());
		}
	}
	/**
	 **	returns the just the name of a class (without the package) given a full class name including
	 **	the package.
	 **/
	public static String className(String classAndPackage)
	{
		int dotPos = classAndPackage.lastIndexOf(".");
		if(dotPos >= 0)
			return(classAndPackage.substring(dotPos+1));
		else
			return(classAndPackage);
	}
	public static boolean blank(String chk)
	{
		if(chk == null)
			return(true);
		else if(chk.trim() == "")
			return(true);
		else
			return(false);
	}

		public static String capitalize(String words) {
        StringBuffer buffer = new StringBuffer();
        boolean isNewWord = true;
        int length = words.length();
        for (int i = 0; i < length; ++i) {
            char c = words.charAt(i);
            if (Character.isWhitespace(c)) {
                isNewWord = true;
            } else if (isNewWord) {
                c = Character.toUpperCase(c);
                isNewWord = false;
            }
            buffer.append(c);
        }
        return buffer.toString();
    }

		/*public static String capitalize(String s) {
				if ( s == null ) 
						return s;
				// Get first letter
				String upperCaseFirstLetter = String.valueOf(Character.toUpperCase(s.charAt(0)));
				String restOfWord = s.substring(1,s.length());
				return upperCaseFirstLetter.concat(restOfWord);
		}
		*/

		public static String ifNull(String originalString) {
		if (originalString == null
			|| "null".equals(originalString)
			|| (originalString != null && originalString.trim().length() == 0)) {
			return null;
		}
		return originalString;
	}

	
/*		public static boolean isValidJavaName(String s) {
				if ( s.indexOf(" ") > -1 ) return false;
				if ( s.indexOf("?") > -1 ) return false;
				if ( s.indexOf("+") > -1 ) return false;
				if ( s.indexOf("+") > -1 ) return false;
				if ( s.indexOf("#") > -1 ) return false;
				if ( s.indexOf("@") > -1 ) return false;
				if ( s.indexOf("!") > -1 ) return false;
				if ( s.indexOf("^") > -1 ) return false;
				if ( s.indexOf("&") > -1 ) return false;
				if ( s.indexOf("*") > -1 ) return false;
				if ( s.indexOf(")") > -1 ) return false;
				if ( s.indexOf("(") > -1 ) return false;
				if ( s.indexOf("$") > -1 ) return false;
				if ( s.indexOf("`") > -1 ) return false;
				if ( s.indexOf("~") > -1 ) return false;
				if ( s.indexOf("<") > -1 ) return false;
				if ( s.indexOf("?") > -1 ) return false;
				if ( s.indexOf(">") > -1 ) return false;
				if ( s.indexOf(".") > -1 ) return false;
				if ( s.indexOf(",") > -1 ) return false;
				if ( s.indexOf("/") > -1 ) return false;
				if ( s.indexOf("{") > -1 ) return false;
				if ( s.indexOf("{") > -1 ) return false;
				if ( s.indexOf("}") > -1 ) return false;
				if ( s.indexOf("[") > -1 ) return false;
				if ( s.indexOf("]") > -1 ) return false;
				if ( s.indexOf(";") > -1 ) return false;
				if ( s.indexOf(":") > -1 ) return false;
				if ( s.indexOf("'") > -1 ) return false;
				if ( s.indexOf("\"") > -1 ) return false;
				return true;
		}*/

	//allowed characters in a java identifier per java language spec for
	//java 1.4
	private static Pattern javaChars = Pattern.compile("[A-Za-z0-9_$]+");

	//include a dot (.) when comparing characters for valid java
	//identifiers. This isn't correct, but is needed when mangling names
	//after the "precompile" stage when several dotted names can be
	//mangled at once.
	private static Pattern notJavaChars = Pattern.compile("[^A-Za-z0-9_$.]");
	
	public static boolean isValidJavaName(String s)
	{
		return(javaChars.matcher(s).matches()  && ! isJavaKeyword(s));
	}

	//java keywords as of 1.4, included null and boolean literals too-
	//even though they're not considered keywords, we'll treat them as
	//such...
	private static Pattern javaKeywords = Pattern.compile("(abstract)|(boolean)|(break)|(byte)|(case)|(catch)|(char)|(class)|(const)|(continue)|(default)|(do)|(double)|(else)|(extends)|(false)|(final)|(finally)|(float)|(for)|(goto)|(if)|(implements)|(import)|(instanceof)|(int)|(interface)|(long)|(native)|(new)|(null)|(package)|(private)|(protected)|(public)|(return)|(short)|(static)|(strictfp)|(super)|(switch)|(synchronized)|(this)|(throw)|(throws)|(transient)|(true)|(try)|(void)|(volatile)|(while)");
	
	public static boolean isJavaKeyword(String s)
	{
		return(javaKeywords.matcher(s).matches());
	}

	/**
	 **	javaName  returns a string that can be used an identifier for
	 **	a java name (for a class name, variable name, etc ...)
	 **/
	public static String javaName(String name)
	{
		if(name == null)
			return(null);
		String nameOut;
		String trimmedName = name.trim();
		if(trimmedName.startsWith("`") && trimmedName.endsWith("`"))
			return(trimmedName);

		String upCase = trimmedName.toUpperCase();
		
		if(upCase.startsWith("@NOMANGLE(") && trimmedName.endsWith(")"))
			return(trimmedName.substring(10,trimmedName.length() - 1));
		
		if(isJavaKeyword(name))
			nameOut = "$".concat(name);
		else
			nameOut = notJavaChars.matcher(name).replaceAll("_");
		return(nameOut);
	}
	public static String javaNameKeepBQuotes(String name)
	{
		BQuoteReplacer bqr = new BQuoteReplacer();
		String newName = bqr.replaceQuotes(name);
//		DissectString ds = new DissectString(name);
//		String newName = ds.processStringForBackquotes(name);
		newName = javaName(newName);
//		return(ds.fixBackquotes(newName));
		newName = bqr.replaceID(newName);
		return(newName);
		
	}
	public static String variableName(String name)
	{
		return(javaName("_"+name));
	}
	public static String variableNameKeepBackQuotes(String name)
	{
		return(StringHelper.javaNameKeepBQuotes("_"+name));
	}
	public static String getUniqueVariableName()
	{
		return(("var" + (varCount++)));
	}
	public static void printColl(Collection coll)
	{
		Iterator it = coll.iterator();
		while(it.hasNext())
			System.out.println(it.next().toString());
	}

		
	public static class BQuoteReplacer extends Hashtable<GUID, String>
	{
		public String replaceQuotes(String code)
		{
			if(code == null)
				return(null);
			
			Matcher match = quotedVar.matcher(code);
			String codeout = "";
			String value;
			int idx = 0;
			while(match.find())
			{
					//System.out.println(match.group());
				GUID id = new GUID();
				codeout = codeout + code.substring(idx, match.start()) + "_" + id.getStrId() + "_";
				put(id, code.substring(match.start(), match.end()));
				idx = match.end();
			}
			codeout = codeout + code.substring(idx, code.length());
			return(codeout);
		}
		public static Vector retrieveQuotes(String code) {
			if(code == null)
				return(null);

			Matcher match = quotedVar.matcher(code);
			Vector quotedStrings = new Vector();
			String value;
			int idx = 0;
			while(match.find()) {
				quotedStrings.addElement(code.substring(match.start(),
					match.end()));
				idx = match.end();
			}
			return(quotedStrings);
		}

		public String replaceID(String ident)
		{
			// this substitution uses a plan hashtable where key is vme.getName(),
			// and value is vme.getValue() to substitute for backquoted 
			// strings
			StringWriter resolvedString = new StringWriter();
			String backQuotedString = null;
			// Use this value map guid = originalstring
			// backquoted strings 
			// Take the identifier string and loop thru the hashtable and
			// replace all hashtable keys in the string
			String guidString = null;
			String originalString = null;
			for (Enumeration e= keys(); 
					 e.hasMoreElements(); ) {
					GUID id = (GUID) e.nextElement();
					guidString = "_" + id.getStrId() + "_";	
					originalString = get(id);
					ident = ident.replaceAll(guidString, originalString);
			}	
			return ident;	
/*			if(code == null)
				return(null);
			Enumeration ids = keys();
			String rCode = code;
			String codeout = "";			
			while(ids.hasMoreElements())
			{
				GUID id = (GUID) ids.nextElement();
				String paramName = get(id);
				Pattern idPattern = Pattern.compile("_" + id.getStrId() + "_");
				Matcher match = idPattern.matcher(rCode);
				String value;
				int idx = 0;
				while(match.find())
				{
					codeout = codeout + rCode.substring(idx, match.start()) + paramName;
					idx = match.end();
				}
				codeout = codeout + rCode.substring(idx, rCode.length());
			}
			return(codeout);	
		 */
		}
	}
	public static boolean isBackquoteRef(String strTest)
	{
		if(strTest.trim().startsWith("`") && strTest.trim().endsWith("`"))
			return(true);
		else
			return(false);
	}
	/**
	 * Removes leading and trailing quote from a backquoted reference string.
	 * If the string isn't a valid backquoted reference it is returned unchanged.
	 * @param ref
	 * @return
	 */
	public static String removeQuotesFromReference(String ref)
	{
		if(isBackquoteRef(ref))
		{
			String rVal = ref.trim();
			rVal = rVal.substring(1,rVal.length()-1);
			return(rVal);
		}
		else
			return(ref);
	}

	private static Pattern space = Pattern.compile(" +");
	private static Pattern crs = Pattern.compile("[\n\r\f]+");
	public static String replaceHTMLSpacesAndCRs(String in)
	{
		String out = "";
		String startTxt = "";
		String endTxt = "";
		Matcher bodyStart = htmlBodyTag.matcher(in);
		Matcher bodyEnd = htmlBodyEndTag.matcher(in);
		boolean startFound = bodyStart.find();
		String repTarget;
		if(startFound && bodyEnd.find())
		{
			startTxt = in.substring(0, bodyStart.end());
			repTarget = in.substring(bodyStart.end(), bodyEnd.start());
			endTxt = in.substring(bodyEnd.start());
		}
		else if(startFound)
		{
			startTxt = in.substring(0, bodyStart.end());
			repTarget = in.substring(bodyStart.end());
		}
		else
			repTarget = in;

		Matcher tags = htmlTag.matcher(repTarget);
		int endOfLastTag = 0;
		while(tags.find())
		{
			out = out + substituteSpacesAndCRs(repTarget.substring(endOfLastTag, tags.start())) + repTarget.substring(tags.start(), tags.end());
			endOfLastTag = tags.end();
		}
		out = substituteRegEx(singlePTag, out, "<br><br>");
		out = substituteRegEx(singlePEndTag, out, "");
		out = out + substituteSpacesAndCRs(repTarget.substring(endOfLastTag));
		Matcher twoSpace = twoSpaces.matcher(out);
		while(twoSpace.find())
		{
			out = substituteRegEx(twoSpaces, out, "&#160;");
			twoSpace = twoSpaces.matcher(out);
		}
		twoSpace = spaceAndReturns.matcher(out);
		while(twoSpace.find())
		{
			out = substituteRegEx(spaceAndReturns, out, "<br>");
			twoSpace = spaceAndReturns.matcher(out);
		}
		return(startTxt + out + endTxt);
		
		
	}
	private static Pattern singlePTag = Pattern.compile("<p>", Pattern.CASE_INSENSITIVE);
	private static Pattern singlePEndTag = Pattern.compile("</p>", Pattern.CASE_INSENSITIVE);
	private static Pattern twoSpaces = Pattern.compile("&#160;&#160;");
	private static Pattern spaceAndReturns = Pattern.compile("&#160;<br>");
	
	private static String substituteSpacesAndCRs(String in)
	{
		String out = substituteRegEx(space, in, "&#160;");
		out = substituteRegEx(crs, out, "");
		return(out);
	}
	public static boolean notEmpty(String s){
		if(s == null || s.trim().equals(""))
			return false;
		else
			return true;
	}
	
	public static String convertTime(double time)
	{
		int sign;
		double inTime;
		
		inTime = time;
		if (inTime >= 0)
			sign = 1;
		else
			sign = -1;
		inTime = Math.abs(inTime);
		String outString = new String();
		int intTemp = new Double(inTime).intValue();
	    outString = outString.concat(new Integer(sign * intTemp).toString()).concat("m ");
		if (new Double((inTime - intTemp) * 28).intValue() > 0) 
			outString = outString.concat(" ").concat(new Integer(new Double((inTime - intTemp) * 28).intValue()).toString()).concat("d");
			
		return(outString);
	}

	/**
	 * Replace new line (eol/cr) characters with a \n
	 * 
	 * @param lines
	 * @return
	 */
    public static String replaceNewLineChars(String lines) 
    {
        if(lines == null || lines.length() == 0)
            return(lines);
        String out = "";
        StringBuffer buff = new StringBuffer(lines);
        for(int n = 0 ; n < lines.length () ; n++)
        {
            if(lines.charAt(n) == '\n')
                out = out + "\\n";
            else
                out = out + lines.charAt(n);
        }
        return(out); 
    }
    
    public static String quadBackslashes(String lines) 
    {
        if(lines == null || lines.length() == 0)
            return(lines);
        String out = "";
        StringBuffer buff = new StringBuffer(lines);
        for(int n = 0 ; n < lines.length () ; n++)
        {
            if(lines.charAt(n) == '\\')
                out = out + "\\\\";
            else
                out = out + lines.charAt(n);
        }
        return(out); 
    }
}
