package samples.geo.repo

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import samples.geo.domain.City

@DataR2dbcTest
class CityRepoTest {
    @Autowired
    private lateinit var cityRepo: CityRepo

    @Test
    fun testCreateACity() = runBlocking {
        val city = City(name = "city1", country = "USA", pop = 10000L)
        val savedCity = cityRepo.save(city)
        assertThat(savedCity.name).isEqualTo("city1")
        assertThat(savedCity.country).isEqualTo("USA")
        assertThat(savedCity.pop).isEqualTo(10000L)
    }
}