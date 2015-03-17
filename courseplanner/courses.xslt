<?xml version="1.0" encoding="utf-8"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output
		method="html"
		doctype-public="-//W3C//DTD XHTML 1.1//EN"
		doctype-system="http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd"
		/>
		
	<xsl:template match="/">
		<html xml:lang="en">
			<head>
				<title>Elements</title>
				<link rel="stylesheet" href="mass.css" type="text/css" media="all" />
				<meta http-equiv="content-type" content="text/html; charset=utf-8" />
			</head>
			<body>
				<h1>Schedules</h1>
				<xsl:apply-templates select="//schedule" mode="by-schedule">
				</xsl:apply-templates>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="schedule" mode="by-schedule">
		<div style="border: 2px solid #000;clear: both; margin-bottom:2em; padding:1ex; display: inline-block;">
		<div style="display:inline-block; margin-right:1em;">
		MWF:
		<table border="1">
		<xsl:apply-templates select="./mwf/period" mode="by-period">
		</xsl:apply-templates>
		</table>
		</div>
		
		<div style="display:inline-block; margin-left:1em;">
		TR:
		<table border="1">
		<xsl:apply-templates select="./tr/period" mode="by-period">
		</xsl:apply-templates>
		</table>
		</div>
		</div>
		<div></div>
	</xsl:template>
	
	<xsl:template match="period" mode="by-period">
       <xsl:if test="./@block &gt; 7.5*2 and ./@block &lt; 18*2">
		  <xsl:variable name="color" select="./@color"/>
		  <tr bgcolor="{$color}">
		  <td><xsl:value-of select="./@time" /></td> <td><xsl:value-of select="." /></td>
		  </tr>
		  </xsl:if>
	</xsl:template>

</xsl:stylesheet>
