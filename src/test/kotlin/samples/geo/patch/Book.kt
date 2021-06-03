package samples.geo.patch

data class Book(
    val title: String,
    val author: Author,
    val tags: List<String>,
    val content: String,
    val phoneNumber: String? = null
)

data class Author(
    val givenName: String,
    val familyName: String? = null
)
