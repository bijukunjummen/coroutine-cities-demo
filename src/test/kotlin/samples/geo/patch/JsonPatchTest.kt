package samples.geo.patch

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.fge.jsonpatch.JsonPatch
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch
import org.junit.jupiter.api.Test

class JsonPatchTest {
    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    @Test
    fun testBasicPatching() {
        val s = """
        {
          "title": "Goodbye!",
          "author": {
            "givenName": "John",
            "familyName": "Doe"
          },
          "tags": [
            "example",
            "sample"
          ],
          "content": "This will be unchanged"
        }
        """.trimIndent()


        val patch = """
            [
                { "op": "replace", "path": "/title", "value": "Hello!" } 
            ]
        """.trimIndent()

        println(patch(objectMapper.readTree(s), objectMapper.readTree(patch)))
    }

    @Test
    fun testPatchingNested() {
        val s = """
        {
          "title": "Goodbye!",
          "author": {
            "givenName": "John",
            "familyName": "Doe"
          },
          "tags": [
            "example",
            "sample"
          ],
          "content": "This will be unchanged"
        }        
        """.trimIndent()


        val patch = """
            [
                { "op": "replace", "path": "/title", "value": "Hello!"},
                { "op": "remove", "path": "/author/familyName"},
                { "op": "add", "path": "/phoneNumber", "value": "+01-123-456-7890"},
                { "op": "replace", "path": "/tags", "value": ["example"]}
            ]
        """.trimIndent()
        val jsonPatch: JsonPatch = JsonPatch.fromJson(objectMapper.readTree(patch))
        println(jsonPatch.apply(objectMapper.readTree(s)))
    }

    private fun patch(original: JsonNode, patch: JsonNode): JsonNode {
        val jsonPatch: JsonPatch = JsonPatch.fromJson(patch)
        return jsonPatch.apply(original)
    }

    @Test
    fun testBasicMergePatch() {
        val s = """
        {
          "title": "Goodbye!",
          "author": {
            "givenName": "John",
            "familyName": "Doe"
          },
          "tags": [
            "example",
            "sample"
          ],
          "content": "This will be unchanged"
        }
        """.trimIndent()


        val patch = """
        {
          "title": "Hello!"
        }
        """.trimIndent()

        println(JsonMergePatch.fromJson(objectMapper.readTree(patch)).apply(objectMapper.readTree(s)))
    }

    @Test
    fun testMergePatchNested() {
        val s = """
        {
          "title": "Goodbye!",
          "author": {
            "givenName": "John",
            "familyName": "Doe"
          },
          "tags": [
            "example",
            "sample"
          ],
          "content": "This will be unchanged"
        }        
        """.trimIndent()


        val patch = """
        {
          "title": "Hello!",
          "author": {
            "familyName": null
          },
          "phoneNumber": "+01-123-456-7890",
          "tags": ["example"]
        }   
        """.trimIndent()

        val jsonMergePatch: JsonMergePatch = JsonMergePatch.fromJson(objectMapper.readTree(patch))
        val target = jsonMergePatch.apply(objectMapper.readTree(s))

        println(target)
    }

}
