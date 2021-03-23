package samples.geo.kotlin

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import samples.geo.domain.City
import kotlin.coroutines.CoroutineContext

@SpringBootTest(webEnvironment = RANDOM_PORT)
class CitiesCoroutineTest {
    @Autowired
    private lateinit var restTemplate: RestTemplate

    @LocalServerPort
    private var localServerPort: Int = 0

    @Test
    fun testGetCitiesBlocking() {
        runBlocking(Dispatchers.IO) {
            val cities = getCities()
            cities.forEach { city -> LOGGER.info("Retreived $city") }
        }
    }

    private suspend fun CoroutineScope.getCities(): List<City> {
        val cityIds: List<String> = getCityIds()
        return cityIds
            .map { cityId -> async { getCityForId(cityId) } }
            .map { deferred -> deferred.await() }
    }

    private suspend fun CoroutineScope.getCityIds(): List<String> {
        val cityIdsEntity: ResponseEntity<List<String>> = restTemplate
            .exchange("http://localhost:$localServerPort/cityids",
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<List<String>>() {})
        return cityIdsEntity.body!!
    }

    private fun getCityForId(id: String): City {
        LOGGER.info("Getting city for $id")
        return restTemplate.getForObject("http://localhost:$localServerPort/cities/$id", City::class.java)!!
    }

    @TestConfiguration
    class SpringConfig {
        @Bean
        fun restTemplate(): RestTemplate {
            return RestTemplate()
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(CitiesCoroutineTest::class.java)
    }
}