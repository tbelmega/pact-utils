import au.com.dius.pact.consumer.dsl.PactDslJsonArray
import au.com.dius.pact.consumer.dsl.PactDslJsonBody
import au.com.dius.pact.consumer.dsl.PactDslJsonRootValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PactTestUtilsTest {

    @Nested
    inner class ForArrayBodies {

        @Test
        fun `null element is translated to PactDsl`() {
            // given
            val example = null

            val expectedDslPart = PactDslJsonArray
                .arrayMinLike(0, PactDslJsonRootValue.stringType(null))

            // when
            val dslPart = likeCollectionOf(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "null"
            )
        }

        @Test
        fun `string element is translated to PactDsl`() {
            // given
            val example = "foo"

            val expectedDslPart = PactDslJsonArray
                .arrayMinLike(1, PactDslJsonRootValue.stringType("foo"))

            // when
            val dslPart = likeCollectionOf(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "foo"
            )
        }

        @Test
        fun `number element is translated to PactDsl`() {
            // given
            val example = 123

            val expectedDslPart = PactDslJsonArray
                .arrayMinLike(1, PactDslJsonRootValue.numberType(123))

            // when
            val dslPart = likeCollectionOf(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "123"
            )
        }

        @Test
        fun `boolean element is translated to PactDsl`() {
            // given
            val example = true

            val expectedDslPart = PactDslJsonArray
                .arrayMinLike(1, PactDslJsonRootValue.booleanType(true))

            // when
            val dslPart = likeCollectionOf(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "true"
            )
        }

        @Test
        fun `object element is translated to PactDsl`() {
            // given
            val example = ClassWithDifferentProperties(
                someStringParam = "foo",
                someOtherStringParam = "bar",
                someNumberParam = 42,
                someBooleanParam = true
            )

            val expectedDslPart = PactDslJsonArray
                .arrayMinLike(1)
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

        @Test
        fun `array of arrays is translated to PactDsl`() {
            // given
            val example = listOf(SimpleClassWithString(someStringParam = "foo"))

            val expectedDslPart = PactDslJsonArray()
                .array()
                .eachLike(1)
                .stringType("someStringParam", "foo")
                .closeObject()
                .closeArray()


            // when
            val dslPart = likeCollectionOf(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someStringParam",
                "foo"
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
        fun `array property is translated to PactDsl`() {
            // given
            val example = ClassWithNestedArray(
                someStringParam = "foo",
                someNestedArray = listOf("bar")
            )

            val expectedDslPart = PactDslJsonBody()
                .stringType("someStringParam", "foo")
                .minArrayLike("someNestedArray", 0, PactDslJsonRootValue.stringType("bar"), 1)


            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someStringParam",
                "someNestedArray",
                "foo",
                "bar"
            )
        }

        @Test
        fun `null property is translated to PactDsl`() {
            // given
            val example = SimpleClassWithNullableProperty(someNullableParam = null)

            val expectedDslPart = PactDslJsonBody()
                .nullValue("someNullableParam")

            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someNullableParam"
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

    @Nested
    inner class ForObjectsInObjects {

        @Test
        fun `Objects in Objects are translated to PactDsl`() {
            // given
            val example = ClassWithNestedClass(
                someStringParam = "hello",
                someNestedObject = ClassWithDifferentProperties("foo", "bar", 42, false)
            )

            val expectedDslPart = PactDslJsonBody()
                .`object`("someNestedObject")
                .stringType("someStringParam", "foo")
                .stringType("someOtherStringParam", "bar")
                .numberType("someNumberParam", 42)
                .booleanType("someBooleanParam", false)
                .closeObject()
                .asBody()
                .stringType("someStringParam", "hello")

            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someBooleanParam",
                "someOtherStringParam",
                "someNumberParam",
                "someNestedObject"
            )
        }

        @Test
        fun `multiple levels of nesting`() {
            // given
            val example = ClassWithNestedClass(
                someStringParam = "hello",
                someNestedObject = ClassWithNestedClass(
                    someStringParam = "hello2",
                    someNestedObject = ClassWithNestedClass(
                        someStringParam = "hello3",
                        someNestedObject = SimpleClassWithString("hello4")
                    )
                )
            )

            val expectedDslPart = PactDslJsonBody()
                .`object`("someNestedObject")
                .`object`("someNestedObject")
                .`object`("someNestedObject")
                .stringType("someStringParam", "hello4")
                .closeObject()
                .asBody()
                .stringType("someStringParam", "hello3")
                .closeObject()
                .asBody()
                .stringType("someStringParam", "hello2")
                .closeObject()
                .asBody()
                .stringType("someStringParam", "hello")

            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "hello",
                "hello2",
                "hello3",
                "hello4"
            )
        }
    }

    @Nested
    inner class ForArraysInObjects {

        @Test
        fun `empty Arrays in Objects are translated to PactDsl`() {
            // given
            val example = ClassWithNestedArray(
                someStringParam = "hello",
                someNestedArray = emptyList()
            )

            val expectedDslPart = PactDslJsonBody()
                .eachLike("someNestedArray", 0)
                .closeArray()
                .asBody()
                .stringType("someStringParam", "hello")

            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someNestedArray",
                "hello",
                "someStringParam"
            )
        }

        @Test
        fun `Arrays of null in Objects values are translated to PactDsl`() {
            // given
            val example = ClassWithNestedArray(
                someStringParam = "hello",
                someNestedArray = listOf(
                    null
                )
            )

            val expectedDslPart = PactDslJsonBody()
                .eachLike(
                    "someNestedArray",
                    PactDslJsonRootValue.numberType(null),
                    1
                )
                .stringType("someStringParam", "hello")

            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someNestedArray",
                "hello",
                "someStringParam",
                "null"
            )
        }

        @Test
        fun `Arrays of strings in Objects are translated to PactDsl`() {
            // given
            val example = ClassWithNestedArray(
                someStringParam = "hello",
                someNestedArray = listOf(
                    "foo"
                )
            )

            val expectedDslPart = PactDslJsonBody()
                .eachLike(
                    "someNestedArray",
                    PactDslJsonRootValue.stringType("foo"),
                    1
                )
                .stringType("someStringParam", "hello")

            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someNestedArray",
                "hello",
                "someStringParam",
                "foo"
            )
        }

        @Test
        fun `Arrays of numbers in Objects are translated to PactDsl`() {
            // given
            val example = ClassWithNestedArray(
                someStringParam = "hello",
                someNestedArray = setOf(
                    123
                )
            )

            val expectedDslPart = PactDslJsonBody()
                .eachLike(
                    "someNestedArray",
                    PactDslJsonRootValue.numberType(123),
                    1
                )
                .stringType("someStringParam", "hello")

            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someNestedArray",
                "hello",
                "someStringParam",
                "123"
            )
        }

        @Test
        fun `Arrays of booleans in Objects are translated to PactDsl`() {
            // given
            val example = ClassWithNestedArray(
                someStringParam = "hello",
                someNestedArray = listOf(
                    true
                )
            )

            val expectedDslPart = PactDslJsonBody()
                .eachLike(
                    "someNestedArray",
                    PactDslJsonRootValue.booleanType(true),
                    1
                )
                .stringType("someStringParam", "hello")

            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someNestedArray",
                "hello",
                "someStringParam",
                "true"
            )
        }

        @Test
        fun `Arrays of Objects in Objects are translated to PactDsl`() {
            // given
            val example = ClassWithNestedArray(
                someStringParam = "hello",
                someNestedArray = listOf(
                    SimpleClassWithString("foo")
                )
            )

            val expectedDslPart = PactDslJsonBody()
                .eachLike("someNestedArray", 1)
                .stringType("someStringParam", "foo")
                .closeObject()
                .closeArray()
                .asBody()
                .stringType("someStringParam", "hello")

            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someNestedArray",
                "hello",
                "someStringParam",
                "foo"
            )
        }

        @Test
        fun `Array of Arrays in Objects are translated to PactDsl`() {
            // given
            val example = ClassWithNestedArray(
                someStringParam = "hello",
                someNestedArray = listOf(
                    setOf(
                        SimpleClassWithString(someStringParam = "foo")
                    )
                )
            )

            val expectedDslPart = PactDslJsonBody()
                .eachArrayLike("someNestedArray", 1)
                .`object`()
                .stringType("someStringParam", "foo")
                .closeObject()
                .closeArray()
                .closeArray().asBody()
                .stringType("someStringParam", "hello")


            // when
            val dslPart = like(example)

            // then
            assertThat(dslPart.toString()).isEqualTo(
                expectedDslPart.toString()
            )
            assertThat(dslPart.toString()).contains(
                "someStringParam",
                "foo"
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

data class SimpleClassWithNullableProperty(
    val someNullableParam: Any?
)

data class ClassWithDifferentProperties(
    val someStringParam: String,
    val someOtherStringParam: String,
    val someNumberParam: Number,
    val someBooleanParam: Boolean
)

data class ClassWithNestedClass(
    val someNestedObject: Any,
    val someStringParam: String
)

data class ClassWithNestedArray(
    val someNestedArray: Collection<Any?>,
    val someStringParam: String
)
