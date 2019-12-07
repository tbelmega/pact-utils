import au.com.dius.pact.consumer.dsl.*
import kotlin.reflect.KProperty1
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
    example: Any?,
    minNumberOfElements: Int = 1
): DslPart {
    return when (example) {
        null -> PactDslJsonArray
            .arrayMinLike(1, PactDslJsonRootValue.stringType(example))
        is String -> PactDslJsonArray
            .arrayMinLike(1, PactDslJsonRootValue.stringType(example))
        is Number -> PactDslJsonArray
            .arrayMinLike(1, PactDslJsonRootValue.numberType(example))
        is Boolean -> PactDslJsonArray
            .arrayMinLike(1, PactDslJsonRootValue.booleanType(example))
        is Collection<*> -> PactDslJsonArray()
            .array().eachLike(1)
            .likeObject(example.first()!!)
            .closeArray()
        else -> PactDslJsonArray
            .arrayMinLike(minNumberOfElements)
            .likeObject(example)
            .closeObject()
    }
}

/*
 *
 * Class Matcher Convenience Methods
 *
 */
fun PactDslJsonBody.likeObject(example: Any): PactDslJsonBody {
    var builder = this

    example.javaClass.kotlin.memberProperties.forEach {
        builder = buildProperty(it, example, builder)
    }

    return builder
}


fun PactDslJsonBody.likeCollectionOf(name: String, example: Collection<*>): PactDslJsonBody {
    return if (example.isEmpty())
        this.eachLike(name, 0).closeArray().asBody()
    else when (val value = example.first()) {
        null -> this.eachLike(name, PactDslJsonRootValue.stringType(value), 1)
        is String -> this.eachLike(name, PactDslJsonRootValue.stringType(value), 1)
        is Number -> this.eachLike(name, PactDslJsonRootValue.numberType(value), 1)
        is Boolean -> this.eachLike(name, PactDslJsonRootValue.booleanType(value), 1)
        is Collection<*> -> this.eachArrayLike(name, 1)
            .`object`().likeObject(value.first()!!)
            .closeArray().closeArray().asBody()
        else -> this.eachLike(name).likeObject(value).closeArray().asBody()
    }
}

private fun buildProperty(
    it: KProperty1<Any, *>,
    example: Any,
    builder: PactDslJsonBody
): PactDslJsonBody {
    return when (val value = it.get(example)) {
        null -> builder.nullValue(it.name)
        is String -> builder.stringType(it.name, value)
        is Number -> builder.numberType(it.name, value)
        is Boolean -> builder.booleanType(it.name, value)
        is Collection<*> -> builder.likeCollectionOf(it.name, value)
        else -> builder.`object`(it.name, like(value))
    }
}

