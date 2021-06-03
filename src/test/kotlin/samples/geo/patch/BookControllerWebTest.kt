package samples.geo.patch

import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@WebFluxTest(
    controllers = [BookController::class]
)
class BookControllerWebTest {

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun testJsonPatch() {
        val patch = """
            [
                { "op": "replace", "path": "/title", "value": "Hello!"},
                { "op": "remove", "path": "/author/familyName"},
                { "op": "add", "path": "/phoneNumber", "value": "+01-123-456-7890"},
                { "op": "replace", "path": "/tags", "value": ["example1", "example2"]}
            ]
        """.trimIndent()
        webTestClient.patch()
            .uri("/books/1")
            .header("Content-Type", "application/json-patch+json")
            .body(BodyInserters.fromValue(patch))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.title").isEqualTo("Hello!")
            .jsonPath("$.author.familyName").doesNotExist()
            .jsonPath("$.phoneNumber").isEqualTo("+01-123-456-7890")
            .jsonPath("$.tags").value(Matchers.contains("example1", "example2"))
    }

    @Test
    fun testJsonMergePatch() {
        val patch = """
            {
              "title": "Hello!",
              "author": {
                "familyName": null
              },
              "phoneNumber": "+01-123-456-7890",
              "tags": ["example1", "example2"]
            }
        """.trimIndent()
        webTestClient.patch()
            .uri("/books/1")
            .header("Content-Type", "application/merge-patch+json")
            .body(BodyInserters.fromValue(patch))
            .exchange()
            .expectStatus()
            .isOk
            .expectBody()
            .jsonPath("$.title").isEqualTo("Hello!")
            .jsonPath("$.author.familyName").doesNotExist()
            .jsonPath("$.phoneNumber").isEqualTo("+01-123-456-7890")
            .jsonPath("$.tags").value(Matchers.contains("example1", "example2"))
    }
}
