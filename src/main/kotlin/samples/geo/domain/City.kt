package samples.geo.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable

@Table("cities")
data class City(
    @field:Id
    var id: Long? = null,
    val name: String,
    val country: String,
    val pop: Long = 0L
) : Serializable