<?xml version="1.0"?>
   <xsl:stylesheet 
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
      version="1.0">
   <xsl:output method="html"></xsl:output>
   <xsl:template match="/">
      <html>
         <head>
         <title>XML Tree Control</title>
         <link rel="stylesheet" type="text/css" 
            href="xmlTree.css"/>
         <script type="text/javascript" 
            src="xmlTree.js"></script>
         </head>
         <xsl:apply-templates/>
      </html>
   </xsl:template>
   
   <xsl:template match="tree">
      <body>
      <xsl:apply-templates/>
   </body>
   </xsl:template>
   
   <xsl:template match="branch">
      <span class="trigger">

      <xsl:attribute name="onClick">
	showBranch('<xsl:value-of select="@id"/>');
      </xsl:attribute>
      <img src="closed.gif">
	<xsl:attribute name="id">I<xsl:value-of select="@id"/></xsl:attribute>
      </img>
   
      <xsl:value-of select="branchText"/>
      <br/>
      </span>
   
      <span class="branch">
      <xsl:attribute name="id">
         <xsl:value-of select="@id"/>
      </xsl:attribute>
      <xsl:apply-templates/>
      </span>
   </xsl:template>
   
   <xsl:template match="leaf">
      <img src="doc.gif"/>
      <a>
      <xsl:attribute name="href" >
         <xsl:value-of select="link"/>
      </xsl:attribute>
      <xsl:attribute name="target">classFrame</xsl:attribute>
      <xsl:value-of select="leafText"/>
      </a><br/>
   </xsl:template>
   
   <!-- avoid output of text node 
        with default template -->
   <xsl:template match="branchText"/>
   
   </xsl:stylesheet>
