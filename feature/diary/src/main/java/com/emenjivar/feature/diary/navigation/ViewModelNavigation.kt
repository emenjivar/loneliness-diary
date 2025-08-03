package com.emenjivar.feature.diary.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

sealed class NavigationAction {
    class NavigateTo(val route: NavKey) : NavigationAction()
    data object PopBackStack : NavigationAction()
}

/**
 * Contract for viewModels that needs to triggers navigation events.
 *
 * Example:
 * ```kotlin
 * @HiltViewModel
 * class MyViewModel : ViewModel(), ViewModelNavigation by ViewModelNavigationImp() {
 *      private fun onButtonClick() {
 *          navigate(MyRoute)
 *      }
 * }
 * ```
 */
interface ViewModelNavigation {
    /**
     * One-shot navigation event.
     */
    val navigationFlow: SharedFlow<NavigationAction?>

    /**
     * Navigates to the specified route.
     */
    fun navigate(route: NavKey)

    /**
     * Pop the current screen from the stack.
     */
    fun popBackStack()
}

class ViewModelNavigationImp : ViewModelNavigation {
    private val _navigationFlow = MutableSharedFlow<NavigationAction>(
        extraBufferCapacity = 1
    )
    override val navigationFlow: SharedFlow<NavigationAction?>
        get() = _navigationFlow.asSharedFlow()

    override fun navigate(route: NavKey) {
        val action = NavigationAction.NavigateTo(route)
        _navigationFlow.tryEmit(action)
    }

    override fun popBackStack() {
        val action = NavigationAction.PopBackStack
        _navigationFlow.tryEmit(action)
    }
}
