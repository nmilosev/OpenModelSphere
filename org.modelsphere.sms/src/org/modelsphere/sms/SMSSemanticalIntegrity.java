/*************************************************************************

Copyright (C) 2008 Grandite

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

You can reach Grandite at: 

20-1220 Lebourgneuf Blvd.
Quebec, QC
Canada  G2K 2G4

or

open-modelsphere@grandite.com

 **********************************************************************/
package org.modelsphere.sms;

import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.baseDb.db.event.DbUpdateListener;
import org.modelsphere.jack.baseDb.meta.MetaField;
import org.modelsphere.sms.db.*;

/**
 * @author gpelletier, neosapiens inc
 * 
 */
public final class SMSSemanticalIntegrity implements DbUpdateListener {

    public SMSSemanticalIntegrity() {
        MetaField.addDbUpdateListener(this, 0, new MetaField[] { DbSMSLink.fSourceObjects,
                DbSMSLink.fTargetObjects });
    }

    // Supports DbUpdateListener
    public void dbUpdated(DbUpdateEvent event) throws DbException {
        if (event.dbo instanceof DbSMSLink
                && event.op == Db.REMOVE_FROM_RELN
                && (event.metaField == DbSMSLink.fSourceObjects || event.metaField == DbSMSLink.fTargetObjects)) {
            // make sure that a graphical link between a notice and another
            // object is still valid.
            // it is considered valid if the graphical objects attached by the
            // graphical link are still
            // both in sources or targets for the link (using the semantical
            // objects associated with the graphical objects).
            DbSMSLink link = (DbSMSLink) event.dbo;
            if (event.neighbor instanceof DbSMSNotice) {
                // The link will not have been identified as a notice link since
                // both the notice SO and GO have
                // been deleted (delete SO then cascade on the GO). In that
                // case, the neighbor will be the notice SO
                // which has been removed from fSourceObjects or fTargetObjects.
                validateLink(link);
            } else if (isNoticeLink(link)) {
                if (event.neighbor != null) {
                    // The opposite to the notice on the link has been removed.
                    // Validate if altered then remove.
                    validateLink(link);
                } else {
                    // We need to validate the link using the graphical objects
                    // associated with the link GO.
                    // The semantical link has been broken (the notice SO has
                    // been removed)
                    // and it can't be used to validate if a link was used to
                    // attach a notice.
                    // In that case we search on the link GOs front and back
                    // ends for
                    validateLinkUsingGO(link);
                }
            }
        }

    }

    private void validateLink(DbSMSLink link) throws DbException {
        if ((link.getNbNeighbors(DbSMSLink.fSourceObjects) == 0 && link
                .getNbNeighbors(DbSMSLink.fTargetObjects) == 1)
                || (link.getNbNeighbors(DbSMSLink.fSourceObjects) == 1 && link
                        .getNbNeighbors(DbSMSLink.fTargetObjects) == 0)) {
            // link has not been updated (1 source 1 target and one of them is
            // the notice,
            // remove the link.
            link.remove();
        } else {
            // remove graphical links and preserve the semantical link since it
            // has been modified
            // by the user.
            DbEnumeration gos = link.getLinkGos().elements();
            while (gos.hasMoreElements()) {
                DbSMSLinkGo go = (DbSMSLinkGo) gos.nextElement();
                go.remove();
            }
            gos.close();
        }
    }

    private void validateLinkUsingGO(DbSMSLink link) throws DbException {
        // make sure the SO for the notice GO is in sources or in targets,
        // if not remove the link GO
        DbSMSNoticeGo noticeGO = null;
        DbEnumeration gos = link.getLinkGos().elements();
        DbSMSLinkGo go = null;
        DbSMSGraphicalObject oppositeGO = null;
        while (gos.hasMoreElements()) {
            go = (DbSMSLinkGo) gos.nextElement();
            DbSMSGraphicalObject front = go.getFrontEndGo();
            DbSMSGraphicalObject back = go.getBackEndGo();
            if (front instanceof DbSMSNoticeGo)
                noticeGO = (DbSMSNoticeGo) front;
            else if (back instanceof DbSMSNoticeGo)
                noticeGO = (DbSMSNoticeGo) back;
            if (noticeGO != null) {
                oppositeGO = noticeGO == front ? back : front;
                break;
            }
        }
        gos.close();

        if (noticeGO != null && oppositeGO != null) {
            DbSMSNotice notice = noticeGO.getNotice();
            DbObject opposite = oppositeGO.getSO();
            if (notice != null && opposite != null) {
                boolean soFound = false;
                boolean oppositeSOFound = false;
                DbEnumeration sources = link.getSourceObjects().elements();
                while (sources.hasMoreElements()) {
                    DbObject source = (DbObject) sources.nextElement();
                    if (source == notice) {
                        soFound = true;
                        break;
                    }
                    if (source == opposite) {
                        oppositeSOFound = true;
                        break;
                    }
                }
                sources.close();
                DbEnumeration targets = link.getTargetObjects().elements();
                while (targets.hasMoreElements()) {
                    DbObject target = (DbObject) targets.nextElement();
                    if (target == notice) {
                        soFound = true;
                        break;
                    }
                    if (target == opposite) {
                        oppositeSOFound = true;
                        break;
                    }
                }
                targets.close();
                if (!oppositeSOFound || !soFound)
                    go.remove();
            }
        }

    }

    private boolean isNoticeLink(DbSMSLink link) throws DbException {
        boolean notice = false;
        DbEnumeration sources = link.getSourceObjects().elements();
        while (sources.hasMoreElements()) {
            DbObject source = (DbObject) sources.nextElement();
            if (source instanceof DbSMSNotice) {
                notice = true;
                break;
            }
        }
        sources.close();
        if (!notice) {
            DbEnumeration targets = link.getTargetObjects().elements();
            while (targets.hasMoreElements()) {
                DbObject target = (DbObject) targets.nextElement();
                if (target instanceof DbSMSNotice) {
                    notice = true;
                    break;
                }
            }
            targets.close();
        }
        if (!notice) {
            // validate with gos
            DbEnumeration gos = link.getLinkGos().elements();
            while (gos.hasMoreElements()) {
                DbSMSLinkGo go = (DbSMSLinkGo) gos.nextElement();
                DbSMSGraphicalObject front = go.getFrontEndGo();
                DbSMSGraphicalObject back = go.getBackEndGo();
                if (front instanceof DbSMSNoticeGo || back instanceof DbSMSNoticeGo) {
                    notice = true;
                    break;
                }
            }
            gos.close();
        }
        return notice;
    }

}