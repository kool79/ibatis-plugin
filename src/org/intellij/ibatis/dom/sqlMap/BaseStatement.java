package org.intellij.ibatis.dom.sqlMap;

import com.intellij.javaee.model.xml.CommonDomModelElement;
import com.intellij.psi.PsiClass;
import com.intellij.util.xml.*;
import org.intellij.ibatis.dom.converters.IbatisClassConverter;
import org.intellij.ibatis.dom.converters.ParameterMapConverter;
import org.jetbrains.annotations.NotNull;

/**
 * base statement in SQL Map file
 */
public interface BaseStatement extends CommonDomModelElement {
    /**
     * get the id for statement
     *
     * @return statement id
     */
    @Attribute("id")
    public GenericAttributeValue<String> getId();

    /**
     * get the parameter class for
     *
     * @return get parameter class
     */
    @Attribute("parameterClass")
    @Convert(IbatisClassConverter.class)
    public GenericAttributeValue<PsiClass> getParameterClass();

    /**
     * get parameter Map for class
     *
     * @return parameter map class
     */
    @Attribute("parameterMap")
    @Convert(ParameterMapConverter.class)
    public GenericAttributeValue<ParameterMap> getParameterMap();

    /**
     * get the SQL code for statement
     *
     * @return SQL sentence
     */
    @NotNull public String getSQL();

}