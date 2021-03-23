package samples.geo.repo

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import samples.geo.domain.City

interface CityRepo : CoroutineCrudRepository<City, Long>