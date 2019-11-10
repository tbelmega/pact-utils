import au.com.dius.pact.consumer.dsl.PactDslJsonArray
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PactTestUtilsKtTest {

    @Nested
    inner class ForArrayBodies {

        @Test
        fun `string property is translated to PactDsl`() {
            // given
            val example = SimpleClassWithString(someStringParam = "foo")

            val expectedDslPart = PactDslJsonArray
                .arrayMaxLike(1)
                .stringType("someStringParam", "foo")
                .closeObject()

            // when
            val dslPart = likeCollectionOf(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someStringParam"
            )
        }

        @Test
        fun `number property is translated to PactDsl`() {
            // given
            val example = SimpleClassWithNumber(someNumberParam = 42)

            val expectedDslPart = PactDslJsonArray
                .arrayMaxLike(1)
                .numberType("someNumberParam", 42)
                .closeObject()

            // when
            val dslPart = likeCollectionOf(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someNumberParam"
            )
        }

        @Test
        fun `boolean property is translated to PactDsl`() {
            // given
            val example = SimpleClassWithBoolean(someBooleanParam = true)

            val expectedDslPart = PactDslJsonArray
                .arrayMaxLike(1)
                .booleanType("someBooleanParam", true)
                .closeObject()

            // when
            val dslPart = likeCollectionOf(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someBooleanParam"
            )
        }

        @Test
        fun `different properties are translated to PactDsl`() {
            // given
            val example = ClassWithDifferentProperties(
                someStringParam = "foo",
                someOtherStringParam = "bar",
                someNumberParam = 42,
                someBooleanParam = true
            )

            val expectedDslPart = PactDslJsonArray
                .arrayMaxLike(1)
                .stringType("someStringParam", "foo")
                .stringType("someOtherStringParam", "bar")
                .numberType("someNumberParam", 42)
                .booleanType("someBooleanParam", true)
                .closeObject()

            // when
            val dslPart = likeCollectionOf(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someStringParam",
                "someOtherStringParam",
                "someNumberParam",
                "someBooleanParam"
            )
        }
    }

    @Nested
    inner class ForObjectBodies {

        @Test
        fun `string property is translated to PactDsl`() {
            // given
            val example = SimpleClassWithString(someStringParam = "foo")

            val expectedDslPart = PactDslJsonBody()
                .stringType("someStringParam", "foo")

            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someStringParam"
            )
        }

        @Test
        fun `number property is translated to PactDsl`() {
            // given
            val example = SimpleClassWithNumber(someNumberParam = 42)

            val expectedDslPart = PactDslJsonBody()
                .numberType("someNumberParam", 42)

            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someNumberParam"
            )
        }

        @Test
        fun `boolean property is translated to PactDsl`() {
            // given
            val example = SimpleClassWithBoolean(someBooleanParam = true)

            val expectedDslPart = PactDslJsonBody()
                .booleanType("someBooleanParam", true)

            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someBooleanParam"
            )
        }

        @Test
        fun `different properties are translated to PactDsl`() {
            // given
            val example = ClassWithDifferentProperties(
                someStringParam = "foo",
                someOtherStringParam = "bar",
                someNumberParam = 42,
                someBooleanParam = true
            )

            val expectedDslPart = PactDslJsonBody()
                .stringType("someStringParam", "foo")
                .stringType("someOtherStringParam", "bar")
                .numberType("someNumberParam", 42)
                .booleanType("someBooleanParam", true)

            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someStringParam",
                "someOtherStringParam",
                "someNumberParam",
                "someBooleanParam"
            )
        }
    }
}

data class SimpleClassWithString(
    val someStringParam: String
)

data class SimpleClassWithNumber(
    val someNumberParam: Number
)

data class SimpleClassWithBoolean(
    val someBooleanParam: Boolean
)

data class ClassWithDifferentProperties(
    val someStringParam: String,
    val someOtherStringParam: String,
    val someNumberParam: Number,
    val someBooleanParam: Boolean
)
