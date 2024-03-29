<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 2.0//EN"
         "http://java.sun.com/products/javahelp/helpset_2_0.dtd">

<helpset version="2.0">
  <!-- title -->
  <title>OncoTCap User's Guide</title>

  <!-- maps -->
  <maps>
     <homeID>top</homeID>
     <mapref location="oncotcap.jhm"/>
  </maps>

  <!-- views -->
  <view>
    <name>TOC</name>
    <label>Table Of Contents</label>
    <type>javax.help.TOCView</type>
    <data>oncotcapTOC.xml</data>
  </view>

  <view>
    <name>Index</name>
    <label>Index</label>
    <type>javax.help.IndexView</type>
    <data>oncotcapIndex.xml</data>
  </view>
  <view>
    <name>Search</name>
    <label>Search</label>
    <type>javax.help.SearchView</type>
    <data engine="com.sun.java.help.search.DefaultSearchEngine">
      JavaHelpSearch
    </data>
  </view>
   <presentation default="true" displayviewimages="true">
     <name>main window</name>
     <size width="900" height="700" />
     <location x="200" y="200" />
     <title>OncoTCAP Help</title>
     <image>tri-arrow</image>
     <toolbar>
		<helpaction>javax.help.BackAction</helpaction>
		<helpaction>javax.help.ForwardAction</helpaction>
		<helpaction>javax.help.SeparatorAction</helpaction>
		<helpaction>javax.help.PrintAction</helpaction>
		<helpaction>javax.help.PrintSetupAction</helpaction>
     </toolbar>
  </presentation>
   
</helpset>

