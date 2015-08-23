import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertThatExpressionsWithoutAssertion {

    @Test
    public void invalidAssert() {
        assertThat("foo");
    }

    @Test
    public void invalidAssert() {
        assertThat("foo").describedAs("foo not compared");
    }
}
