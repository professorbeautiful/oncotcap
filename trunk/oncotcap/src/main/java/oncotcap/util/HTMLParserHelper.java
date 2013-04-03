package oncotcap.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oncotcap.datalayer.persistible.parameter.SingleParameter;
import oncotcap.engine.ValueMap;

public class HTMLParserHelper
{
	public static final Pattern htmlSpecialChar = Pattern.compile("&[.[^;]]*;", Pattern.DOTALL);
	public static final Pattern htmlSpaceChar = Pattern.compile("&nbsp;", Pattern.CASE_INSENSITIVE);
	public static final Pattern htmlHead = Pattern.compile("^\\s*<html>.*<body>\\s*", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
	public static final Pattern htmlFoot = Pattern.compile("^\\s*</body>.*</html>\\s*", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
	public static final Pattern aTag = Pattern.compile("<a\\s*.*?>.*?</a\\s*[.[^>]]*>", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
	public static final Pattern brTag = Pattern.compile("<br.*?>", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
	public static final Pattern pTag = Pattern.compile("(</{0,1}p\\s*.*?>)+", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
	public static final Pattern htmlTag = Pattern.compile("</?[.[^>]]*>", Pattern.DOTALL);
	public static final Pattern whiteSpace = Pattern.compile("\\s+");
	public static final String matchHREF = new String("<\\s*[Aa]\\s*[HhIi][RrDd][Ee]?[Ff]?\\s*=\\s*");    // <a (href=|id=)
	public static final String matchEndTag = new String("\\s*>\\s*?[^<]*\\s*?<\\s*?/\\s*[aA]>"); // >VARNAME</a><x> </x> - took the x off don't know why it was there
	public static final String matchQuote = new String ("\""); // " 
	public static final String matchID = new String("\\s*[HhiI][RrdD][Ee]?[Ff]?\\s*=\\s*");        // ID=|HREF=

	public static void main(String [] args)
	{
		String t = replaceBRTags(" <BR> <BR> <BR> asdfasdf<BR> <BR> ");
	}
	public static String convertHTMLToStdText(String html)
	{
		String rVal = replacePTags(html);
		rVal = replaceBRTags(rVal);
		rVal = removeAllTags(rVal);
		return(rVal);
	}
	public static String removeSpecialChars(String html)
	{
		String rVal = html;
		Matcher mat = htmlSpecialChar.matcher(rVal);
		if(mat != null)
			rVal = mat.replaceAll("");
		return(rVal);
	}
	public static String removeHTMLHead(String html)
	{
		String rVal = html;
		Matcher mat = htmlHead.matcher(html);
		if(mat != null)
			rVal = mat.replaceAll("");
		return(rVal.trim());
	}
	public static String replaceWhiteSpace(String html)
	{
		String rVal = html;
		Matcher mat = whiteSpace.matcher(rVal);
		if(mat != null)
			rVal = mat.replaceAll(" ");
		mat = htmlSpaceChar.matcher(rVal);
		if(mat != null)
			rVal = mat.replaceAll(" ");
		
		return(rVal);
	}
	public static String replacePTags(String html)
	{
		boolean pAtStart = false;
		boolean pAtEnd = false;
		int start = 0;
		int end = 0;
		//check to see if the body starts or ends with a
		//p tag;
		String tVal = html;
		Matcher mat = htmlHead.matcher(tVal);
		if(mat != null) tVal = mat.replaceAll("");
		mat = htmlFoot.matcher(tVal);
		if(mat != null) tVal = mat.replaceAll("");
		mat = pTag.matcher(tVal);
		if(mat.find())
		{
			if(mat.start() == 0)
				pAtStart = true;
		}
		else  //if there are no ptags in this html, don't waste our time, just return
			return(html);
		
		mat = pTag.matcher(tVal);
		boolean found = false;
		while(mat.find())
		{
			found = true;
			end = mat.end();
		}
		if(found && end == tVal.length())
			pAtEnd = true;
			
		String rVal = html;
		mat = pTag.matcher(rVal);
		boolean first = true;
		//replace all p tags that aren't at the beginning or end of the text with two CRs
		while(mat != null && mat.find())
		{
			start = mat.start();
			end = mat.end();
			if(first && pAtStart)
				rVal = rVal.substring(0, start) + rVal.substring(end, rVal.length());
			else if(! mat.find() && pAtEnd)
				rVal = rVal.substring(0, start) + rVal.substring(end, rVal.length());
			else
				rVal = rVal.substring(0, start) + "\n\n" + rVal.substring(end, rVal.length());
			
			mat = pTag.matcher(rVal);
		}
		return(rVal);
	}
	public static String replaceBRTags(String html)
	{
		String rVal = html;
		Matcher mat = brTag.matcher(rVal);
		if(mat != null)
			rVal = mat.replaceAll("\n");
		return(rVal);
	}
	public static String removeAllTags(String html)
	{
		String rVal = html;
		Matcher mat = htmlTag.matcher(rVal);
		rVal = mat.replaceAll("");
		return (rVal);
	}

	public static String interMingleWithWhiteSpace(String lineIn)
	{
		StringBuffer buffIn = new StringBuffer(lineIn);
		StringBuffer buffOut = new StringBuffer("\\s*");
		for (int n = 0; n < buffIn.length(); n++)
			buffOut = buffOut.append(buffIn.charAt(n)).append("\\s*");

		return (buffOut.toString());
	}

	public static String variableValueToHTML(SingleParameter origSingleParam, String variableValue)
	{
		String rVal = " <a href=\"" + origSingleParam.getID() + "\" " + "ID=\"" + origSingleParam.getID() + "\">"
				+ variableValue + "</a>";

		return (rVal);
	}

}
