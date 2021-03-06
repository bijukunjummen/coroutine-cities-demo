package samples.geo

import kotlinx.coroutines.runBlocking
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service
import samples.geo.domain.City
import samples.geo.repo.CityRepo

@SpringBootApplication
class Application {
}

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@Service
internal class DataPopulator(private val cityRepo: CityRepo) : ApplicationRunner {
    override fun run(args: ApplicationArguments) {
        runBlocking {
            cityRepo.save(City(name = "Portland", country = "USA", pop = 1600000L))
            cityRepo.save(City(name = "Seattle", country = "USA", pop = 3200000L))
            cityRepo.save(City(name = "SFO", country = "USA", pop = 6400000L))
            cityRepo.save(City(name = "LA", country = "USA", pop = 12800000L))
            cityRepo.save(City(name = "Denver", country = "USA", pop = 3000000L))
            cityRepo.save(City(name = "Chicago", country = "USA", pop = 25600000L))
            cityRepo.save(City(name = "NY", country = "USA", pop = 25600000L))
        }
    }
}
