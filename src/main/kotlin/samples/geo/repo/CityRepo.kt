package samples.geo.repo

import org.springframework.data.r2dbc.repository.R2dbcRepository
import samples.geo.domain.City

interface CityRepo : R2dbcRepository<City, Long>