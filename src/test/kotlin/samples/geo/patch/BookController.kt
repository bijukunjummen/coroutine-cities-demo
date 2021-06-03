package samples.geo.patch

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.github.fge.jsonpatch.JsonPatch
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/books")
class BookController(
    private val objectMapper: ObjectMapper
) {
    @PatchMapping(path = ["/{id}"], consumes = ["application/json-patch+json"])
    fun jsonPatchBook(
        @PathVariable id: String,
        @RequestBody patch: JsonNode
    ): Mono<ResponseEntity<Book>> {
        return Mono.fromSupplier {
            val jsonPatch: JsonPatch = JsonPatch.fromJson(patch)
            val original: JsonNode = objectMapper.valueToTree(BOOKS.get(id))
            val patched: JsonNode = jsonPatch.apply(original)
            val patchedBook: Book =
                objectMapper.treeToValue(patched) ?: throw RuntimeException("Could not convert json back to book")
            BOOKS[id] = patchedBook
            ResponseEntity.ok(patchedBook)
        }
    }

    @PatchMapping(path = ["/{id}"], consumes = ["application/merge-patch+json"])
    fun jsonMergePatchBook(
        @PathVariable id: String,
        @RequestBody patch: JsonNode
    ): Mono<ResponseEntity<Book>> {
        return Mono.fromSupplier {
            val original: JsonNode = objectMapper.valueToTree(BOOKS.get(id))
            val patched: JsonNode = JsonMergePatch.fromJson(patch).apply(original)
            val patchedBook: Book =
                objectMapper.treeToValue(patched) ?: throw RuntimeException("Could not convert json back to book")
            BOOKS[id] = patchedBook
            ResponseEntity.ok(patchedBook)
        }
    }

    companion object {
        val BOOKS = mutableMapOf(
            "1" to Book(
                title = "Goodbye!",
                author = Author(givenName = "John", familyName = "Doe"),
                tags = listOf("sample", "example"),
                content = "This will be unchanged"
            )
        )
    }
}
