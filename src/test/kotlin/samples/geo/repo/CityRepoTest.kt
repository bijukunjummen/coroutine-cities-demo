package samples.geo.repo

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import reactor.test.StepVerifier
import samples.geo.domain.City

@DataR2dbcTest
class CityRepoTest {
    @Autowired
    private lateinit var cityRepo: CityRepo

    @Test
    fun testCreateACity() {
        val city = City(name = "city1", country = "USA", pop = 10000L)
        StepVerifier.create(cityRepo.save(city))
            .assertNext { city ->
                assertThat(city.name).isEqualTo("city1")
                assertThat(city.country).isEqualTo("USA")
                assertThat(city.pop).isEqualTo(10000L)
            }
            .verifyComplete()
    }
}