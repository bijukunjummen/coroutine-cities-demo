package samples.geo.web

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.invocation.InvocationOnMock
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import samples.geo.AppRoutes.routes
import samples.geo.domain.City
import samples.geo.service.CityService

class CityHandlerTests {
    private lateinit var webTestClient: WebTestClient
    private val cityService = mock<CityService>()

    @BeforeEach
    fun setUp() {
        whenever(cityService.getCities())
            .thenReturn(
                flow {
                    emit(City(1L, "test1", "country1", 1L))
                    emit(City(2L, "test2", "country2", 2L))
                }
            )
        whenever(cityService.getCityIds())
            .thenReturn(
                flow {
                    emit(1L)
                    emit(2L)
                }
            )
        val cityHandler = CityHandler(cityService)
        webTestClient = WebTestClient.bindToRouterFunction(routes(cityHandler)).build()
    }

    @Test
    fun allCities() {
        webTestClient.get()
            .uri("/cities")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(City::class.java)
            .hasSize(2)
            .contains(
                City(1L, "test1", "country1", 1L),
                City(2L, "test2", "country2", 2L)
            )
    }

    @Test
    fun allCityIds() {
        webTestClient.get()
            .uri("/cityids")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(Long::class.java)
            .contains(1L, 2L)
    }

    @Test
    fun saveCity() = runBlocking {
        whenever(cityService.saveCity(any()))
            .thenAnswer { invocation: InvocationOnMock -> invocation.getArgument(0) }
        webTestClient.post()
            .uri("/cities")
            .body(BodyInserters.fromValue(City(name = "test1", country = "country1", pop = 1L)))
            .exchange()
            .expectStatus().isCreated
            .expectBody()
    }

    @Test
    fun getACity() = runBlocking {
        whenever(cityService.getCity(1L)).thenReturn(City(1L, "test1", "country1", 1L))
        whenever(cityService.getCity(2L)).thenReturn(City(2L, "test2", "country2", 2L))
        webTestClient.get()
            .uri("/cities/1")
            .exchange()
            .expectBody()
            .json("{\"id\": 1, \"name\": \"test1\",\"country\":\"country1\",\"pop\": 1}")
    }
}