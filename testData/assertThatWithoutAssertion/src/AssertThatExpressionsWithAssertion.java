import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AssertThatExpressionsWithoutAssertion {

    @Test
    public void correctAssert_isEqualTo() {
        assertThat("foo").isEqualTo("foo");
    }

    @Test
    public void correctAssert__describeAs_isEqualTo() {
        assertThat("foo").describedAs("foo is foo").isEqualTo("foo");
    }
}
