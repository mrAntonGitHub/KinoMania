package i.o.mob.dev.kinomania.di.modules

import dagger.Module
import dagger.Provides
import i.o.mob.dev.kinomania.repository.Repository
import i.o.mob.dev.kinomania.repository.RepositoryDelegate

@Module
class RepositoryModule {

    @Provides
    fun provideRepository(repository: Repository) : RepositoryDelegate{
        return repository
    }

}