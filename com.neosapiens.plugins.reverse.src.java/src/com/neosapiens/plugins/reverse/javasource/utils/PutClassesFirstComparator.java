package com.neosapiens.plugins.reverse.javasource.utils;

import java.util.Comparator;

public class PutClassesFirstComparator implements Comparator<SemanticDeclaration> {

    @Override
    public int compare(SemanticDeclaration o1, SemanticDeclaration o2) {
        boolean o1isClass = o1 instanceof DeclarationOfClassOrInterface;
        boolean o2isClass = o2 instanceof DeclarationOfClassOrInterface;

        if (o1isClass && o2isClass)
            return 0;
        else if (o1isClass && !o2isClass)
            return -1;
        else
            return 1;
    }
}
