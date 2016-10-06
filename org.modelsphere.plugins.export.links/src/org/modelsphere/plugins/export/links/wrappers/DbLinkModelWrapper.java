/*************************************************************************

Copyright (C) 2009 neosapiens inc./MSSS

This file is part of Link Report plug-in.

Link Report plug-in is free software; you can redistribute it and/or modify
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

Link Report plug-in has been developed by neosapiens inc. for
the purposes of the Ministère de la Santé et des Services Sociaux
du Québec (MSSS).
 
You can reach neosapiens inc. at: 
  http://www.neosapiens.com

 **********************************************************************/
package org.modelsphere.plugins.export.links.wrappers;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.modelsphere.jack.baseDb.db.DbEnumeration;
import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbRelationN;
import org.modelsphere.plugins.export.links.international.LocaleMgr;
import org.modelsphere.sms.db.DbSMSLink;
import org.modelsphere.sms.db.DbSMSLinkModel;

public class DbLinkModelWrapper {
    private static final String NO_MODEL = LocaleMgr.misc.getString("NoModel");
    private static final String X_MODELS = LocaleMgr.misc.getString("0Models");

    private DbProjectWrapper m_parent;
    private DbSMSLinkModel m_linkModel;

    public DbLinkModelWrapper(DbProjectWrapper parent, DbSMSLinkModel linkModel) {
        m_parent = parent;
        m_linkModel = linkModel;
    }

    public DbProjectWrapper getParent() {
        return m_parent;
    }

    public String getSource() throws DbException {
        List<String> models = new ArrayList<String>();

        List<DbLinkWrapper> links = getLinks();
        for (DbLinkWrapper link : links) {
            List<DbColumnWrapper> sources = link.getSourceColumns();
            for (DbColumnWrapper column : sources) {
                String modelName = column.getModelName();
                if (!models.contains(modelName)) {
                    models.add(modelName);
                }
            }
        } //end for

        String source = toText(models);
        return source;
    }

    public String getDestination() throws DbException {
        List<String> models = new ArrayList<String>();

        List<DbLinkWrapper> links = getLinks();
        for (DbLinkWrapper link : links) {
            List<DbColumnWrapper> sources = link.getDestinationColumns();
            for (DbColumnWrapper column : sources) {
                String modelName = column.getModelName();
                if (!models.contains(modelName)) {
                    models.add(modelName);
                }
            }
        } //end for

        String destination = toText(models);
        return destination;
    }

    private String toText(List<String> models) {
        String text;
        if (models.size() == 0) {
            text = NO_MODEL;
        } else if (models.size() == 1) {
            text = models.get(0);
        } else {
            text = MessageFormat.format(X_MODELS, models.size());
        } //end if

        return text;
    }

    public String getName() throws DbException {
        String name = m_linkModel.getName();
        return name;
    }

    private List<DbLinkWrapper> m_links = null;

    public List<DbLinkWrapper> getLinks() throws DbException {
        if (m_links == null) {
            m_links = new ArrayList<DbLinkWrapper>();

            //for each project component
            DbRelationN relN = m_linkModel.getComponents();
            DbEnumeration enu = relN.elements();
            while (enu.hasMoreElements()) {
                DbObject o = enu.nextElement();
                if (o instanceof DbSMSLink) {
                    DbSMSLink l = (DbSMSLink) o;
                    DbLinkWrapper link = new DbLinkWrapper(this, l);
                    m_links.add(link);
                } //end if
            } //end while
            enu.close();
        }

        return m_links;
    }

}
