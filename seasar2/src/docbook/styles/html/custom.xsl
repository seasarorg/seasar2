<?xml version="1.0" encoding="UTF-8"?>

<!--
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version='1.0'>

<!-- Activate Graphics -->
  <xsl:param name="admon.graphics" select="1"/>
  <xsl:param name="admon.graphics.path">images/</xsl:param>
  <xsl:param name="admon.graphics.extension">.gif</xsl:param>

  <xsl:param name="admonition.title.properties">text-align: center</xsl:param>

<!--###################################################
                      Table of Contents
    ################################################### -->

    <xsl:param name="toc.max.depth" select="3"/>
    <xsl:param name="toc.section.depth" select="2"/>

    <xsl:param name="generate.toc">
      book toc,title
      part toc,title
      chapter toc,title
    </xsl:param>

<!--###################################################
                      Header
    ################################################### -->

    <xsl:param name="html.stylesheet">css/stylesheet.css</xsl:param>
    <xsl:param name="html.stylesheet.type">text/css</xsl:param>

<!--###################################################
                      Extensions
    ################################################### -->

    <!-- These extensions are required for table printing and other stuff -->
    <xsl:param name="use.extensions" select="1"/>
    <xsl:param name="tablecolumns.extension" select="0"/>
    <xsl:param name="callout.extensions" select="1"/>
    <xsl:param name="graphicsize.extension" select="0"/>

<!--###################################################
                   Tables
    ################################################### -->

    <xsl:param name="table.borders.with.css" select="1"/>

<!--###################################################
                         Labels
    ################################################### -->

    <!-- Label Chapters and Sections (numbering) -->
    <xsl:param name="chapter.autolabel" select="1"/>
    <xsl:param name="section.autolabel" select="1"/>
    <xsl:param name="section.autolabel.max.depth" select="2"/>

    <xsl:param name="section.label.includes.component.label" select="1"/>
    <xsl:param name="table.footnote.number.format">1</xsl:param>

<!--###################################################
                         Callouts
    ################################################### -->

    <!-- don't use images for callouts -->
    <xsl:param name="callout.graphics" select="0"/>
    <xsl:param name="callout.unicode" select="0"/>

    <!-- Place callout marks at this column in annotated areas -->
    <xsl:param name="callout.defaultcolumn" select="90"/>

<!--###################################################
                          Misc
    ################################################### -->
    <xsl:param name="use.id.as.filename" select="1"/>
    <xsl:param name="chunk.fast" select="1"/>
    <xsl:param name="chunk.section.depth" select="0"/>
    <xsl:param name="chunker.output.encoding">UTF-8</xsl:param>
    <xsl:param name="chunker.output.indent">yes</xsl:param>

    <xsl:param name="saxon.character.representation">native;decimal</xsl:param>


<!-- Remove "Chapter" from the Chapter titles... -->
  <xsl:param name="local.l10n.xml" select="document('')"/>
  <l:i18n xmlns:l="http://docbook.sourceforge.net/xmlns/l10n/1.0">
    <l:l10n language="en">
      <l:context name="title-numbered">
        <l:template name="chapter" text="%n.&#160;%t"/>
        <l:template name="section" text="%n&#160;%t"/>
      </l:context>
    </l:l10n>
  </l:i18n>      
</xsl:stylesheet>
