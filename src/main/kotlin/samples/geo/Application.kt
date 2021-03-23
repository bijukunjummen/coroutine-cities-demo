package samples.geo

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.io.ClassPathResource
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import samples.geo.domain.City
import samples.geo.repo.CityRepo

@SpringBootApplication
class Application {
    @Bean
    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
        val initializer = ConnectionFactoryInitializer()
        initializer.setConnectionFactory(connectionFactory)
        initializer.setDatabasePopulator(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
        return initializer
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@Service
internal class DataPopulator(private val cityRepo: CityRepo) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        Flux
            .merge(
                cityRepo.save(City(name = "Portland", country = "USA", pop = 1600000L)),
                cityRepo.save(City(name = "Seattle", country = "USA", pop = 3200000L)),
                cityRepo.save(City(name = "SFO", country = "USA", pop = 6400000L)),
                cityRepo.save(City(name = "LA", country = "USA", pop = 12800000L)),
                cityRepo.save(City(name = "Denver", country = "USA", pop = 3000000L)),
                cityRepo.save(City(name = "Chicago", country = "USA", pop = 25600000L)),
                cityRepo.save(City(name = "NY", country = "USA", pop = 25600000L))
            )
            .collectList()
            .thenMany(cityRepo.findAll())
            .subscribe { city -> println(city) }
    }
}