package net.supersven.intellij.assertj;


import com.intellij.codeHighlighting.HighlightDisplayLevel;
import com.intellij.codeInsight.daemon.GroupNames;
import com.intellij.codeInspection.BaseJavaBatchLocalInspectionTool;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.*;
import com.siyeh.ig.BaseInspectionVisitor;
import org.jetbrains.annotations.NotNull;

public class AssertThatWithoutAssertionInspection extends BaseJavaBatchLocalInspectionTool {

    @Override
    @NotNull
    public String getDisplayName() {
        return "AssertJ assertThat() without assertion";
    }

    @NotNull
    public String getGroupDisplayName() {
        return GroupNames.BUGS_GROUP_NAME;
    }

    @Override
    @NotNull
    public String getID() {
        return "AssertThatWithoutAssertion";
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @NotNull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return super.getDefaultLevel();
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(ProblemsHolder holder, boolean isOnTheFly) {
        return new AssertThatWithoutAssertionInspectionVisitor(holder);
    }

    private static class AssertThatWithoutAssertionInspectionVisitor extends BaseInspectionVisitor {
        private ProblemsHolder problems;

        public AssertThatWithoutAssertionInspectionVisitor(ProblemsHolder problems) {
            this.problems = problems;
        }

        @Override
        public void visitMethodCallExpression(@NotNull PsiMethodCallExpression expression) {
            super.visitMethodCallExpression(expression);

            if (!isAssertJAssertThat(expression)){
                return;
            }

            PsiMethodCallExpression parentPsiMethodCallExpression = findParentMethodExpression(expression);

            if (parentPsiMethodCallExpression == null) {
                return;
            }

            String text = parentPsiMethodCallExpression.getText();
            boolean hasAssertion = text.matches("assertThat.*isEqualTo.*");
            if (!hasAssertion) {
                problems.registerProblem(expression, "AssertJ assertThat() without assertion #loc", ProblemHighlightType.ERROR);
            }
        }

        private boolean isAssertJAssertThat(@NotNull PsiMethodCallExpression expression) {
            PsiReferenceExpression methodExpression = expression.getMethodExpression();
            String methodName = methodExpression.getReferenceName();
            if (methodName == null || !methodName.equals("assertThat")) {
                return false;
            }
            PsiMethod method = expression.resolveMethod();
            if (method == null) {
                return false;
            }
            PsiClass containingClass = method.getContainingClass();

            if (containingClass == null || !containingClass.getQualifiedName().equals(org.assertj.core.api.Assertions.class.getCanonicalName())) {
                return false;
            }

            return true;
        }

        private PsiMethodCallExpression findParentMethodExpression(PsiMethodCallExpression expression) {
            PsiElement expressionStatement = findContainingPsiExpressionStatement(expression);

            return (PsiMethodCallExpression) expressionStatement.getFirstChild();
        }

        private PsiElement findContainingPsiExpressionStatement(PsiMethodCallExpression expression) {
            PsiElement topLevelExpression = expression;

            while (!(topLevelExpression instanceof PsiExpressionStatement)) {
                topLevelExpression = topLevelExpression.getParent();
            }
            return topLevelExpression;
        }
    }
}