package com.neosapiens.plugins.reverse.javasource.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.modelsphere.jack.baseDb.db.DbException;
import org.modelsphere.jack.baseDb.db.DbObject;
import org.modelsphere.jack.baseDb.db.DbSemanticalObject;
import org.modelsphere.sms.oo.db.DbOOOperation;

public class SearchFiltering {

    private List<DbObject> objects;

    public SearchFiltering(List<DbObject> objects) {
        this.objects = objects;
    }

    public List<DbObject> search(String value, boolean caseSensitive, boolean searchInName,
            boolean searchInDescription, boolean searchInBody) {
        List<DbObject> matchingObjects = new ArrayList<DbObject>();
        try {
            int casePattern = (caseSensitive == true ? Pattern.DOTALL : Pattern.DOTALL
                    | Pattern.CASE_INSENSITIVE);
            for (DbObject object : objects) {
                boolean matched = false;

                if (searchInName
                        && object.getName() != null
                        && Pattern.compile(Pattern.quote(value), casePattern)
                                .matcher(object.getName()).find()) {
                    matchingObjects.add(object);
                    matched = true;
                }
                if (!matched && searchInDescription && object instanceof DbSemanticalObject) {
                    DbSemanticalObject obj = (DbSemanticalObject) object;
                    if (obj.getDescription() != null
                            && Pattern.compile(Pattern.quote(value), casePattern)
                                    .matcher(obj.getDescription()).find()) {
                        matchingObjects.add(object);
                        matched = true;
                    }
                }
                if (!matched && searchInBody && object instanceof DbOOOperation) {
                    DbOOOperation obj = (DbOOOperation) object;
                    if (obj.getBody() != null
                            && Pattern.compile(Pattern.quote(value), casePattern)
                                    .matcher(obj.getBody()).find()) {
                        matchingObjects.add(object);
                        matched = true;
                    }
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return matchingObjects;
    }
}
