package samples.geo

import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.coRouter
import samples.geo.web.CityHandler

object AppRoutes {
    fun routes(cityHandler: CityHandler): RouterFunction<*> = coRouter {
        accept(MediaType.APPLICATION_JSON).nest {
            GET("/cities", cityHandler::getCities)
            GET("/cities/{id}", cityHandler::getCity)
            POST("/cities", cityHandler::createCity)
            PUT("/cities/{id}", cityHandler::updateCity)
            DELETE("/cities/{id}", cityHandler::deleteCity)
            GET("/cityids", cityHandler::getCityIds)
        }
    }
}