/**
 * This is the base composable for all the screens.
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

package be.scri.ui.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import be.scri.ui.common.appcomponents.ActionBar
import be.scri.ui.common.appcomponents.PageTitle

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScribeBaseScreen(
    modifier: Modifier = Modifier,
    pageTitle: String? = null,
    lastPage: String? = null,
    onBackNavigation: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .padding(top = 8.dp),
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            if (lastPage != null) {
                ActionBar(
                    title = lastPage,
                    onClickAction = onBackNavigation,
                    modifier = Modifier,
                )
            }

            if (pageTitle != null) {
                PageTitle(
                    pageTitle = pageTitle,
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp),
                )
            }

            content()
        }
    }
}