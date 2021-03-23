package samples.geo

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.RouterFunction
import samples.geo.web.CityHandler

@Configuration
class AppRouteConfig {
    @Bean
    fun routerFunction(cityHandler: CityHandler): RouterFunction<*> {
        return AppRoutes.routes(cityHandler)
    }
}