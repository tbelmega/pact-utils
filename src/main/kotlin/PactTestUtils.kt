import au.com.dius.pact.consumer.dsl.*
import kotlin.reflect.full.memberProperties

/*
 *
 * Request/Response Body Convenience Methods
 *
 */
fun PactDslResponse.bodyWithCollectionOfObjectsLike(
    example: Any,
    minNumberOfElements: Int = 1
): PactDslResponse {
    return this.body(
        likeCollectionOf(example, minNumberOfElements)
    )
}

fun PactDslRequestWithPath.bodyLike(
    example: Any
): PactDslRequestWithPath {
    return this.body(
        like(example)
    )
}

fun like(example: Any): DslPart {
    return PactDslJsonBody().likeObject(example)
}

fun likeCollectionOf(
    example: Any,
    minNumberOfElements: Int = 1
): DslPart {
    return PactDslJsonArray
        .arrayMinLike(minNumberOfElements)
        .likeObject(example)
        .closeObject()
}

/*
 *
 * Class Matcher Convenience Methods
 *
 */
fun PactDslJsonBody.likeObject(example: Any): PactDslJsonBody {
    var builder = this

    example.javaClass.kotlin.memberProperties.forEach {
        when (val value = it.get(example)) {
            is String -> builder = builder.stringType(it.name, value)
            is Number -> builder = builder.numberType(it.name, value)
            is Boolean -> builder = builder.booleanType(it.name, value)
        }
    }

    return builder
}

