<?xml version='1.0' encoding='ISO-8859-1' ?>
<!DOCTYPE helpset
  PUBLIC "-//Sun Microsystems Inc.//DTD JavaHelp HelpSet Version 1.0//EN"
         "http://java.sun.com/products/javahelp/helpset_1_0.dtd">

<helpset version="2.0">
<title>ScratchPad Help - Table Of Contents</title>

<maps>
   <homeID>overview</homeID>
	 <mapref location="map.jhm"/>
</maps>



<view>
    <name>TOC</name>
    <label>Table Of Contents</label>
    <type>javax.help.TOCView</type>
    <data>toc.xml</data>
</view>

<view>
    <name>Index</name>
    <label>Index</label>
    <type>javax.help.IndexView</type>
    <data>myindex.xml</data>
</view>

<presentation default="true">
    <name>Main_Window</name>
    <size width="640" height="480" />
	<location x="0" y="0" />
	<title>ScratchPad</title>
	<image>icon</image>
	<toolbar>
	    <helpaction>javax.help.BackAction</helpaction>
	    <helpaction  image="addfav_icon">javax.help.FavoritesAction</helpaction>
	</toolbar>
</presentation>


<impl>
    <helpsetregistry helpbrokerclass="javax.help.DefaultHelpBroker" />
    <viewerregistry viewertype="text/html" viewerclass="com.sun.java.help.impl.CustomKit" />
    <viewerregistry viewertype="application/pdf" viewerclass="Your PDF Editor Kit" />
</impl>


</helpset>