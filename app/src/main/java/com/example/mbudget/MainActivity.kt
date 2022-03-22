package com.example.mbudget

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mbudget.ui.budget.BudgetViewModel
import com.example.mbudget.ui.budget.MonthSelection
import com.example.mbudget.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FIXME: SOFT_INPUT_ADJUST_RESIZE is deprecated in API30. Use WindowInsets.IME instead.
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            MaterialTheme {
                NavigationGraph()
            }
        }
    }

}

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable("home") {
            HomeScreen(
                viewModel = hiltViewModel(),
                navController = navController,
            )
        }

        composable(
            route = "budget/{budgetId}",
            arguments = listOf(navArgument("budgetId") { type = NavType.StringType }),
        ) {
            BudgetScreen(viewModel = hiltViewModel())
        }
    }
}

@Composable
fun HomeScreen(viewModel: HomeViewModel, navController: NavController) {
    com.example.mbudget.ui.home.HomeScreen(
        budgets = viewModel.budgets.observeAsState().value,
        createBudget = viewModel::createBudget,
        openBudget = { budgetId: UUID -> navController.navigate(route = "budget/$budgetId") }
    )
}


@Composable
fun BudgetScreen(viewModel: BudgetViewModel) {
    com.example.mbudget.ui.budget.BudgetScreen(
        budget = viewModel.budget.observeAsState().value,
        saveExpenseAction = viewModel::saveExpense,
        monthSelection = viewModel.selectedYearMonth.observeAsState(YearMonth.now()).value.let {
            MonthSelection(
                selectedMonth = it,
                next = viewModel::nextMonth,
                previous = viewModel::previousMonth,
            )
        }
    )
}