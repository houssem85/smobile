package fr.strada.smobile.di.app

import fr.strada.smobile.data.repositories.*

class FakeAppModule : AppModule() {

    override fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository {
        return FakeUserRepository()
    }

    override fun provideMesActivitiesRepository(mesActivitesRepository: MesActivitesRepositoryImpl): MesActivitesRepository {
        return FakeMesActivitiesRepository()
    }

    override fun provideInfractionsRepository(infractionRepositoryImpl: InfractionRepositoryImpl): InfractionRepository {
        return FakeInfractionRepository()
    }
}