package samples.geo.web

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import reactor.core.publisher.Mono
import samples.geo.domain.City
import samples.geo.service.CityService

@Service
class CityHandler(private val cityService: CityService) {
    suspend fun getCities(request: ServerRequest): ServerResponse {
        val cities = cityService.getCities()
            .toList()
        return ServerResponse.ok().bodyValueAndAwait(cities)
    }

    suspend fun getCity(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val city = cityService.getCity(id)
        return ServerResponse.ok().bodyValueAndAwait(city)
    }

    suspend fun createCity(request: ServerRequest): ServerResponse {
        val cityToSave = request.awaitBody<City>()
        val savedCity = cityService.saveCity(cityToSave)
        return ServerResponse.status(HttpStatus.CREATED).bodyValueAndAwait(savedCity)
    }

    suspend fun updateCity(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        val cityToUpdate = request.awaitBody<City>()
        val updatedCity = cityService.saveCity(cityToUpdate)
        return ServerResponse.status(HttpStatus.OK).bodyValueAndAwait(updatedCity)
    }

    suspend fun deleteCity(request: ServerRequest): ServerResponse {
        val id = request.pathVariable("id").toLong()
        cityService.deleteCity(id)
        return ServerResponse.ok().buildAndAwait()
    }

    suspend fun getCityIds(request: ServerRequest): ServerResponse {
        val list = cityService.getCityIds()
            .toList()
        return ServerResponse.ok().bodyValueAndAwait(list)
    }
}