package net.supersven.intellij.assertj;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ex.LocalInspectionToolWrapper;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.testFramework.UsefulTestCase;
import com.intellij.testFramework.builders.JavaModuleFixtureBuilder;
import com.intellij.testFramework.fixtures.*;
import com.intellij.util.PathUtil;
import org.assertj.core.api.Assertions;

public class AssertThatWithoutAssertionInspectionTest extends UsefulTestCase {

    private JavaCodeInsightTestFixture myFixture;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        final IdeaTestFixtureFactory fixtureFactory = IdeaTestFixtureFactory.getFixtureFactory();
        final TestFixtureBuilder<IdeaProjectTestFixture> testFixtureBuilder = fixtureFactory.createFixtureBuilder(getName());
        myFixture = JavaTestFixtureFactory.getFixtureFactory().createCodeInsightFixture(testFixtureBuilder.getFixture());
        myFixture.setTestDataPath(getTestDataPath());

        final JavaModuleFixtureBuilder builder = testFixtureBuilder.addModule(JavaModuleFixtureBuilder.class);
        builder.addLibrary("assertj", PathUtil.getJarPathForClass(Assertions.class));
        builder.addContentRoot(myFixture.getTempDirPath()).addSourceRoot("");
        builder.setMockJdkLevel(JavaModuleFixtureBuilder.MockJdkLevel.jdk15);
        myFixture.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        try {
            myFixture.tearDown();
        } finally {
            myFixture = null;

            super.tearDown();
        }
    }

    protected String getTestDataPath() {
        return "testData/assertThatWithoutAssertion/";
    }

    private void doTest() throws Exception {
        myFixture.testInspection("", new LocalInspectionToolWrapper(new AssertThatWithoutAssertionInspection()));
    }

    public void test() throws Exception {
        doTest();
    }
}