package org.intellij.ibatis.provider;

import com.intellij.psi.*;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.util.InheritanceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * sqlclient method call filter
 */
public class SqlClientElementFilter implements ElementFilter {
    public static List<String> operationMethods = new ArrayList<String>();
    public static List<String> operationClass = new ArrayList<String>();

    static {
        operationMethods.add("insert");
        operationMethods.add("update");
        operationMethods.add("delete");
        operationMethods.add("queryF");
        operationMethods.add("queryW");
        operationClass.add("org.springframework.orm.ibatis.SqlMapClientOperations");
        operationClass.add("com.ibatis.sqlmap.client.SqlMapExecutor");
    }

    public boolean isAcceptable(Object o, PsiElement psiElement) {
        PsiLiteralExpression literalExpression = (PsiLiteralExpression) psiElement;
        PsiElement parent = literalExpression.getParent().getParent();
        if (parent != null && parent instanceof PsiMethodCallExpression) {
            final PsiMethodCallExpression callExpression = (PsiMethodCallExpression) parent;
            final PsiMethod psiMethod = callExpression.resolveMethod();
            if (psiMethod != null) {
                //method validation
                String methodName = psiMethod.getName();
                if (methodName.length() > 6) {
                    methodName = methodName.substring(0, 6);
                }
                if (!operationMethods.contains(methodName)) {
                    return false;
                }
                //first parameter validate
                PsiElement previousElement = literalExpression.getPrevSibling();
                if (previousElement != null) {
                    String text1 = previousElement.getText();
                    String text2 = "";
                    if (!text1.equals("(") && previousElement.getPrevSibling() != null) {
                        text2 = previousElement.getPrevSibling().getText();
                    }
                    if (!(text1.equals("(") || (text1.concat(text2).trim().equals("(")))) {
                        return false;
                    }
                }

                //class validate
                final PsiClass psiClass = psiMethod.getContainingClass();
                for (String operationClazz : operationClass) {
                    final PsiClass expectedClass = PsiManager.getInstance(psiClass.getProject()).findClass(operationClazz, psiClass.getResolveScope());
                    if (InheritanceUtil.isInheritorOrSelf(psiClass, expectedClass, true)) return true;
                }
            }
        }
        return false;
    }

    public boolean isClassAcceptable(Class aClass) {
        return aClass == PsiLiteralExpression.class;
    }
}