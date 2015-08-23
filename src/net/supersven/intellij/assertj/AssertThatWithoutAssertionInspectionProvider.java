package net.supersven.intellij.assertj;


import com.intellij.codeInspection.InspectionToolProvider;

public class AssertThatWithoutAssertionInspectionProvider implements InspectionToolProvider {
    public Class[] getInspectionClasses() {
        return new Class[] { AssertThatWithoutAssertionInspection.class};
    }
}