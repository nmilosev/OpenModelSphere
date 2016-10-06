package com.neosapiens.plugins.reverse.javasource.diagrams;

import java.awt.Point;
import java.util.List;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.graphic.GraphicComponent;
import org.modelsphere.jack.gui.task.Controller;
import org.modelsphere.sms.actions.CreateMissingGraphicsAction;
import org.modelsphere.sms.oo.db.DbOODiagram;
import org.modelsphere.sms.oo.java.db.DbJVClassModel;
import org.modelsphere.sms.oo.java.db.DbJVPackage;
import org.modelsphere.sms.oo.java.graphic.JVDiagramLayout;

public class DiagramCreator {

    private List<DbJVPackage> packages;
    private int startPercent;
    private int endPercent;
    private Controller controller;
    private DbJVClassModel model;

    public DiagramCreator(DbJVClassModel model, List<DbJVPackage> ownedPackages, Controller controller, int startPercent, int endPercent) {
        this.packages = ownedPackages;
        this.controller = controller;
        this.startPercent = startPercent;
        this.endPercent = endPercent;
        this.model = model;
    }

    public void createDiagrams() throws DbException {
        int count = packages.size();
        int span = endPercent-startPercent;
        int i = 1;
        for (DbJVPackage pack : packages) {
        	DbObject composite = (pack == null) ? this.model : pack;
            DbOODiagram diagram = new DbOODiagram(composite);
            CreateMissingGraphicsAction.createOOGraphics(diagram, (Point) null);
            new JVDiagramLayout(diagram, (GraphicComponent[]) null);
            controller.checkPoint(i*span/count+startPercent);
            i++;
        }
    }
}
