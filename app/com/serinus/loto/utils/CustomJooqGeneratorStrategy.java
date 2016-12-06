package com.serinus.loto.utils;

import org.jooq.util.DefaultGeneratorStrategy;
import org.jooq.util.Definition;

public class CustomJooqGeneratorStrategy extends DefaultGeneratorStrategy {


    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        String packageName = super.getJavaPackageName(definition, mode);

        // Override the default package for the POJO's
        if (packageName.endsWith(Constants$.MODULE$.JOOQ_POJOS_DEFAULT_PACKAGE_SUFFIX())) {
            packageName = Constants$.MODULE$.JOOQ_POJOS_NEW_PACKAGE();
        }

        return packageName;
    }


}
