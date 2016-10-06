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

package org.modelsphere.jack.srtool.graphic;

import java.awt.Image;
import java.awt.Rectangle;

import org.modelsphere.jack.actions.ActionInformation;
import org.modelsphere.jack.baseDb.db.*;
import org.modelsphere.jack.baseDb.db.event.DbRefreshListener;
import org.modelsphere.jack.baseDb.db.event.DbUpdateEvent;
import org.modelsphere.jack.graphic.*;

public class DiagramImage extends ImageComponent implements ActionInformation, DbRefreshListener {

    private DbObject imageGo;

    public DiagramImage(Diagram diag, DbObject dbO) throws DbException {
        super(diag);
        imageGo = dbO;
        setAutoFit(false);
        ((DbGraphicalObjectI) imageGo).setGraphicPeer(this);
        populateContents();
        imageGo.addDbRefreshListener(this);

    }

    // BEWARE: overriding methods must call super.delete as last action
    public void delete(boolean all) {
        imageGo.removeDbRefreshListener(this);
        ((DbGraphicalObjectI) imageGo).setGraphicPeer(null);
        super.delete(all);
    }

    private void populateContents() throws DbException {
        setImage((Image) imageGo.get(DbGraphic.fImageGoDiagramImage), (Float) imageGo
                .get(DbGraphic.fImageGoOpacityFactor));
        setRectangle((Rectangle) imageGo.get(DbGraphic.fGraphicalObjectRectangle));
        diagram.setComputePos(this);
    }

    // implementation DbRefreshListener
    public final void refreshAfterDbUpdate(DbUpdateEvent e) throws DbException {
        if (DbGraphic.fImageGoDiagramImage == e.metaField)
            populateContents();
        else if (DbGraphic.fImageGoOpacityFactor == e.metaField)
            populateContents();
    }

    /* implementation ActionInformation */
    public final Db getDb() {
        return imageGo.getDb();
    }

    public final DbObject getSemanticalObject() {
        return null;
    }

    public final DbObject getGraphicalObject() {
        return imageGo;
    }

}
