/**
 * Handles application-level initialization and sets the default night mode based on user configuration.
 *
 * Copyright (C) 2024 Scribe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package be.scri

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import be.scri.navigation.Screen
import be.scri.ui.common.appcomponents.HintDialog
import be.scri.ui.common.bottombar.ScribeBottomBar
import be.scri.ui.screens.InstallationScreen
import be.scri.ui.screens.LanguageSettingsScreen
import be.scri.ui.screens.PrivacyPolicyScreen
import be.scri.ui.screens.ThirdPartyScreen
import be.scri.ui.screens.WikimediaScreen
import be.scri.ui.screens.about.AboutScreen
import be.scri.ui.screens.settings.SettingsScreen
import be.scri.ui.theme.ScribeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("ComposeModifierMissing")
@Composable
fun ScribeApp(
    pagerState: PagerState,
    navController: NavHostController,
    onDarkModeChange: (Boolean) -> Unit,
    resetHints: () -> Unit,
    @SuppressLint("ComposeUnstableCollections") isHintChanged: Map<Int, Boolean>,
    onDismiss: (Int) -> Unit,
    context: Context,
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    ScribeTheme(
        useDarkTheme = isDarkTheme,
    ) {
        Scaffold(
            bottomBar = {
                ScribeBottomBar(
                    onItemClick = {
                        coroutineScope.launch {
                            if (navBackStackEntry?.destination?.route != "pager") {
                                navController.popBackStack()
                            }
                            pagerState.animateScrollToPage(it)
                        }
                    },
                    pagerState = pagerState,
                    modifier =
                    Modifier,
                )
            },
            modifier = modifier.fillMaxSize(),
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "pager",
            ) {
                composable("pager") {
                    HorizontalPager(
                        state = pagerState,
                        beyondViewportPageCount = 3,
                        modifier = Modifier.padding(innerPadding),
                    ) { page ->
                        when (page) {
                            0 -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                ) {
                                    InstallationScreen(
                                        isDark = isDarkTheme,
                                        context = context,
                                    )
                                    HintDialog(
                                        pagerState = pagerState,
                                        currentPageIndex = 0,
                                        sharedPrefsKey = "hint_shown_main",
                                        hintMessageResId = R.string.app_installation_app_hint,
                                        isHintChanged = isHintChanged[0] == true,
                                        onDismiss = { onDismiss(it) },
                                        modifier =
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                    )
                                }
                                HandleBackPress(pagerState, coroutineScope)
                            }
                            1 -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                ) {
                                    SettingsScreen(
                                        onDarkModeChange = { isDarkMode ->
                                            onDarkModeChange(isDarkMode)
                                        },
                                        onLanguageSettingsClick = { language ->
                                            navController.navigate(
                                                "${Screen.LanguageSettings.route}/$language",
                                            )
                                        },
                                        context = context,
                                    )
                                    HintDialog(
                                        pagerState = pagerState,
                                        currentPageIndex = 1,
                                        sharedPrefsKey = "hint_shown_settings",
                                        hintMessageResId = R.string.app_settings_app_hint,
                                        isHintChanged = isHintChanged[1] == true,
                                        onDismiss = { onDismiss(it) },
                                        modifier = Modifier.padding(8.dp),
                                    )
                                }
                                HandleBackPress(pagerState, coroutineScope)
                            }
                            2 -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                ) {
                                    AboutScreen(
                                        onPrivacyPolicyClick = {
                                            navController.navigate(Screen.PrivacyPolicy.route)
                                        },
                                        onThirdPartyLicensesClick = {
                                            navController.navigate(Screen.ThirdParty.route)
                                        },
                                        onWikiClick = {
                                            navController.navigate(Screen.WikimediaScribe.route)
                                        },
                                        resetHints = { resetHints() },
                                        context = context,
                                    )
                                    HintDialog(
                                        pagerState = pagerState,
                                        currentPageIndex = 2,
                                        sharedPrefsKey = "hint_shown_about",
                                        hintMessageResId = R.string.app_about_app_hint,
                                        isHintChanged = isHintChanged[2] == true,
                                        onDismiss = { onDismiss(it) },
                                        modifier = Modifier.padding(8.dp),
                                    )
                                }
                                HandleBackPress(pagerState, coroutineScope)
                            }
                        }
                    }
                }

                composable(
                    route = "${Screen.LanguageSettings.route}/{languageName}",
                ) {
                    val language = it.arguments?.getString("languageName")
                    if (language != null) {
                        LanguageSettingsScreen(
                            language = language,
                            onBackNavigation = {
                                navController.popBackStack()
                            },
                            modifier = Modifier.padding(innerPadding),
                        )
                    }
                }

                composable(Screen.WikimediaScribe.route) {
                    WikimediaScreen(
                        onBackNavigation = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.padding(innerPadding),
                    )
                }

                composable(Screen.PrivacyPolicy.route) {
                    PrivacyPolicyScreen(
                        onBackNavigation = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.padding(innerPadding),
                    )
                }

                composable(Screen.ThirdParty.route) {
                    ThirdPartyScreen(
                        onBackNavigation = {
                            navController.popBackStack()
                        },
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun HandleBackPress(
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
) {
    BackHandler(enabled = pagerState.currentPage > 0) {
        coroutineScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        }
    }
}
