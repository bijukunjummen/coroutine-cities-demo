package samples.geo.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Service
import samples.geo.domain.City
import samples.geo.repo.CityRepo

@Service
class CityService(
    private val cityRepo: CityRepo
) {
    fun getCities(): Flow<City> {
        return cityRepo.findAll().asFlow()
    }

    suspend fun getCity(id: Long): City {
        return cityRepo.findById(id).awaitSingle()
    }

    suspend fun saveCity(city: City): City {
        return cityRepo.save(city).awaitSingle()
    }

    suspend fun upateCity(city: City): City {
        return cityRepo.save(city).awaitSingle()
    }

    suspend fun deleteCity(id: Long) {
        cityRepo.deleteById(id).awaitSingle()
    }

    fun getCityIds(): Flow<Long> {
        return getCities().map { city -> city.id ?: 0L }
    }
}