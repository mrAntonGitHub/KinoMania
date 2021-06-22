package i.o.mob.dev.kinomania.di.component

import dagger.Component
import i.o.mob.dev.kinomania.di.modules.AppModule
import i.o.mob.dev.kinomania.di.modules.RepositoryModule
import i.o.mob.dev.kinomania.di.modules.SearchApiImpl
import i.o.mob.dev.kinomania.ui.categories.CategoryViewModel
import i.o.mob.dev.kinomania.ui.filmsGrid.FilmsGridViewModel
import i.o.mob.dev.kinomania.ui.home.HomeViewModel
import i.o.mob.dev.kinomania.ui.movie.MovieViewModel
import i.o.mob.dev.kinomania.ui.review.ReviewViewModel
import i.o.mob.dev.kinomania.ui.search.SearchViewModel
import i.o.mob.dev.kinomania.ui.parameteredSearch.ParameteredFilmsViewModel
import i.o.mob.dev.kinomania.ui.parameteredSearch.SetParametersViewModel
import i.o.mob.dev.kinomania.ui.staff.StaffViewModel
import i.o.mob.dev.kinomania.utils.Utils
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,  SearchApiImpl::class, RepositoryModule::class])
interface AppComponent {
    fun inject(homeViewModel: HomeViewModel)
    fun inject(utils: Utils)
    fun inject(movieViewModel: MovieViewModel)
    fun inject(searchViewModel: SearchViewModel)
    fun inject(setParametersViewModel: SetParametersViewModel)
    fun inject(categoryViewModel : CategoryViewModel)
    fun inject(staffViewModel: StaffViewModel)
    fun inject(parameteredFilmsViewModel: ParameteredFilmsViewModel)
    fun inject(filmsGridViewModel: FilmsGridViewModel)
    fun inject(reviewViewModel: ReviewViewModel)
}