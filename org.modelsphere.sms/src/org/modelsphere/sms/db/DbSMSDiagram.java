/*************************************************************************

Copyright (C) 2009 Grandite

This file is part of Open ModelSphere.

Open ModelSphere is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can redistribute and/or modify this particular file even under the
terms of the GNU Lesser General Public License (LGPL) as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU Lesser General Public License 
(LGPL) along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
or see http://www.gnu.org/licenses/.

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms.db;

import java.awt.*;
import org.modelsphere.jack.baseDb.meta.*;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import org.modelsphere.sms.db.srtypes.*;
import org.modelsphere.sms.international.LocaleMgr;
import org.modelsphere.jack.baseDb.db.srtypes.*;
import javax.swing.Icon;
import java.awt.print.*;

/**
 * <b>Direct subclass(es)/subinterface(s) : </b><A
 * HREF="../../../../org/modelsphere/sms/oo/db/DbOODiagram.html">DbOODiagram</A>, <A
 * HREF="../../../../org/modelsphere/sms/or/db/DbORDiagram.html">DbORDiagram</A>, <A
 * HREF="../../../../org/modelsphere/sms/be/db/DbBEDiagram.html">DbBEDiagram</A>.<br>
 * 
 * <b>Composites : </b>none.<br>
 * <b>Components : </b><A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSUserTextGo.html">DbSMSUserTextGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSStampGo.html">DbSMSStampGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSImageGo.html">DbSMSImageGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSPackageGo.html">DbSMSPackageGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSLineGo.html">DbSMSLineGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSFreeGraphicGo.html">DbSMSFreeGraphicGo</A>, <A
 * HREF="../../../../org/modelsphere/sms/db/DbSMSNoticeGo.html">DbSMSNoticeGo</A>.<br>
 **/
public abstract class DbSMSDiagram extends DbObject {

    //Meta

    public static final MetaField fName = new MetaField(LocaleMgr.db.getString("name"));
    public static final MetaField fPageFormat = new MetaField(LocaleMgr.db.getString("pageFormat"));
    public static final MetaField fPrintScale = new MetaField(LocaleMgr.db.getString("printScale"));
    public static final MetaField fNbPages = new MetaField(LocaleMgr.db.getString("nbPages"));
    public static final MetaField fPageNoPosition = new MetaField(LocaleMgr.db
            .getString("pageNoPosition"));
    public static final MetaField fPageNoFont = new MetaField(LocaleMgr.db.getString("pageNoFont"));
    public static final MetaRelation1 fStyle = new MetaRelation1(LocaleMgr.db.getString("style"), 0);
    public static final MetaRelationN fChildDiagrams = new MetaRelationN(LocaleMgr.db
            .getString("childDiagrams"), 0, MetaRelationN.cardN);
    public static final MetaRelation1 fParentDiagram = new MetaRelation1(LocaleMgr.db
            .getString("parentDiagram"), 0);

    public static final MetaClass metaClass = new MetaClass(LocaleMgr.db.getString("DbSMSDiagram"),
            DbSMSDiagram.class, new MetaField[] { fName, fPageFormat, fPrintScale, fNbPages,
                    fPageNoPosition, fPageNoFont, fStyle, fChildDiagrams, fParentDiagram },
            MetaClass.UML_EXTENSIBILITY_FILTER);

    /**
     * For internal use only.
     **/
    public static void initMeta() {
        try {
            metaClass.setSuperMetaClass(DbObject.metaClass);
            metaClass.setComponentMetaClasses(new MetaClass[] { DbSMSUserTextGo.metaClass,
                    DbSMSStampGo.metaClass, DbSMSImageGo.metaClass, DbSMSPackageGo.metaClass,
                    DbSMSLineGo.metaClass, DbSMSFreeGraphicGo.metaClass, DbSMSNoticeGo.metaClass });
            metaClass.setIcon("dbdiagram.gif");

            fName.setJField(DbSMSDiagram.class.getDeclaredField("m_name"));
            fPageFormat.setJField(DbSMSDiagram.class.getDeclaredField("m_pageFormat"));
            fPageFormat.setVisibleInScreen(false);
            fPrintScale.setJField(DbSMSDiagram.class.getDeclaredField("m_printScale"));
            fPrintScale.setVisibleInScreen(false);
            fNbPages.setJField(DbSMSDiagram.class.getDeclaredField("m_nbPages"));
            fNbPages.setVisibleInScreen(false);
            fPageNoPosition.setJField(DbSMSDiagram.class.getDeclaredField("m_pageNoPosition"));
            fPageNoPosition.setVisibleInScreen(false);
            fPageNoFont.setJField(DbSMSDiagram.class.getDeclaredField("m_pageNoFont"));
            fPageNoFont.setVisibleInScreen(false);
            fStyle.setJField(DbSMSDiagram.class.getDeclaredField("m_style"));
            fStyle.setFlags(MetaField.COPY_REFS);
            fStyle.setVisibleInScreen(false);
            fChildDiagrams.setJField(DbSMSDiagram.class.getDeclaredField("m_childDiagrams"));
            fParentDiagram.setJField(DbSMSDiagram.class.getDeclaredField("m_parentDiagram"));

            fStyle.setOppositeRel(DbSMSStyle.fDiagrams);
            fParentDiagram.setOppositeRel(DbSMSDiagram.fChildDiagrams);

        } catch (Exception e) {
            throw new RuntimeException("Meta init");
        }
    }

    static final long serialVersionUID = 0;

    //Instance variables
    String m_name;
    SrPageFormat m_pageFormat;
    int m_printScale;
    SrDimension m_nbPages;
    PageNoPosition m_pageNoPosition;
    SrFont m_pageNoFont;
    DbSMSStyle m_style;
    DbRelationN m_childDiagrams;
    DbSMSDiagram m_parentDiagram;

    //Constructors

    /**
     * Parameter-less constructor. Required by Java Beans Conventions.
     **/
    public DbSMSDiagram() {
    }

    /**
     * Creates an instance of DbSMSDiagram.
     * 
     * @param composite
     *            the object which will contain the newly-created instance
     **/
    public DbSMSDiagram(DbObject composite) throws DbException {
        super(composite);
        setDefaultInitialValues();
    }

    private void setDefaultInitialValues() throws DbException {
        setPrintScale(new Integer(60));
        setNbPages(new Dimension(1, 1));
        setName(LocaleMgr.misc.getString("diagram"));
        PageFormat format = new PageFormat();

        Paper paper = new Paper();
        double margin = paper.getImageableX() / 4;
        paper.setImageableArea(margin, margin, paper.getWidth() - 2 * margin, paper.getHeight() - 2
                * margin);
        format.setPaper(paper);

        setPageFormat(format);
        setStyle(findStyle());

    }

    /**
     * @return style
     **/
    public abstract DbSMSStyle findStyle() throws DbException;

    //Setters

    /**
     * Sets the "name" property of a DbSMSDiagram's instance.
     * 
     * @param value
     *            the "name" property
     **/
    public final void setName(String value) throws DbException {
        basicSet(fName, value);
    }

    /**
     * Sets the "page format" property of a DbSMSDiagram's instance.
     * 
     * @param value
     *            the "page format" property
     **/
    public final void setPageFormat(PageFormat value) throws DbException {
        basicSet(fPageFormat, value);
    }

    /**
     * Sets the "print scale" property of a DbSMSDiagram's instance.
     * 
     * @param value
     *            the "print scale" property
     **/
    public final void setPrintScale(Integer value) throws DbException {
        basicSet(fPrintScale, value);
    }

    /**
     * Sets the "page count" property of a DbSMSDiagram's instance.
     * 
     * @param value
     *            the "page count" property
     **/
    public final void setNbPages(Dimension value) throws DbException {
        basicSet(fNbPages, value);
    }

    /**
     * Sets the "page number position" property of a DbSMSDiagram's instance.
     * 
     * @param value
     *            the "page number position" property
     **/
    public final void setPageNoPosition(PageNoPosition value) throws DbException {
        basicSet(fPageNoPosition, value);
    }

    /**
     * Sets the "page number font" property of a DbSMSDiagram's instance.
     * 
     * @param value
     *            the "page number font" property
     **/
    public final void setPageNoFont(Font value) throws DbException {
        basicSet(fPageNoFont, value);
    }

    /**
     * Sets the style object associated to a DbSMSDiagram's instance.
     * 
     * @param value
     *            the style object to be associated
     **/
    public final void setStyle(DbSMSStyle value) throws DbException {
        basicSet(fStyle, value);
    }

    /**
     * Sets the parent diagram object associated to a DbSMSDiagram's instance.
     * 
     * @param value
     *            the parent diagram object to be associated
     **/
    public final void setParentDiagram(DbSMSDiagram value) throws DbException {
        basicSet(fParentDiagram, value);
    }

    /**
    
 **/
    public void set(MetaField metaField, Object value) throws DbException {
        if (metaField.getMetaClass() == metaClass) {
            if (metaField == fChildDiagrams)
                ((DbSMSDiagram) value).setParentDiagram(this);
            else
                basicSet(metaField, value);
        } else
            super.set(metaField, value);
    }

    /**
    
 **/
    public void set(MetaRelationN relation, DbObject neighbor, int op) throws DbException {
        super.set(relation, neighbor, op);
    }

    //Getters

    /**
     * Gets the "name" property of a DbSMSDiagram's instance.
     * 
     * @return the "name" property
     **/
    public final String getName() throws DbException {
        return (String) get(fName);
    }

    /**
     * Gets the "page format" of a DbSMSDiagram's instance.
     * 
     * @return the "page format"
     **/
    public final PageFormat getPageFormat() throws DbException {
        return (PageFormat) get(fPageFormat);
    }

    /**
     * Gets the "print scale" property of a DbSMSDiagram's instance.
     * 
     * @return the "print scale" property
     **/
    public final Integer getPrintScale() throws DbException {
        return (Integer) get(fPrintScale);
    }

    /**
     * Gets the "page count" of a DbSMSDiagram's instance.
     * 
     * @return the "page count"
     **/
    public final Dimension getNbPages() throws DbException {
        return (Dimension) get(fNbPages);
    }

    /**
     * Gets the "page number position" of a DbSMSDiagram's instance.
     * 
     * @return the "page number position"
     **/
    public final PageNoPosition getPageNoPosition() throws DbException {
        return (PageNoPosition) get(fPageNoPosition);
    }

    /**
     * Gets the "page number font" of a DbSMSDiagram's instance.
     * 
     * @return the "page number font"
     **/
    public final Font getPageNoFont() throws DbException {
        return (Font) get(fPageNoFont);
    }

    /**
     * Gets the style object associated to a DbSMSDiagram's instance.
     * 
     * @return the style object
     **/
    public final DbSMSStyle getStyle() throws DbException {
        return (DbSMSStyle) get(fStyle);
    }

    /**
     * Gets the list of child diagrams associated to a DbSMSDiagram's instance.
     * 
     * @return the list of child diagrams.
     **/
    public final DbRelationN getChildDiagrams() throws DbException {
        return (DbRelationN) get(fChildDiagrams);
    }

    /**
     * Gets the parent diagram object associated to a DbSMSDiagram's instance.
     * 
     * @return the parent diagram object
     **/
    public final DbSMSDiagram getParentDiagram() throws DbException {
        return (DbSMSDiagram) get(fParentDiagram);
    }

}
