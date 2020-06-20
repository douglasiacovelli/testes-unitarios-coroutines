package com.iacovelli.coroutines

interface Repository {
    suspend fun fetchCatFact(): CatFact
}

class RepositoryImpl(private val service: APIService) : Repository {

    override suspend fun fetchCatFact(): CatFact {
        return service.fetchCatFact()
    }
}