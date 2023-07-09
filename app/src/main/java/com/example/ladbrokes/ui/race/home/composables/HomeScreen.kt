package com.example.ladbrokes.ui.race.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ladbrokes.R
import com.example.ladbrokes.domain.model.race.Race
import com.example.ladbrokes.ui.race.home.HomeViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeScreen(
    onItemClicked: (Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val eventFlow = viewModel.eventFlow
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is HomeViewModel.UIEvent.ShowSnackBar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            HomeTopBar()
        },
        bottomBar = {
            HomeBottomBar(
                onResetPressed = {
                    viewModel.resetFilters()
                },
            )
        },
    ) { innerPadding ->
        HomeContent(
            modifier = Modifier.padding(innerPadding),
            onItemClicked = { onItemClicked(it) },
            isLoading = uiState.isLoading,
            characters = uiState.characters,
            viewModel,
            uiState.filterList
        )
    }
}

@Composable
private fun HomeTopBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.home_title),
                textAlign = TextAlign.Center,
                modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            )
        },
        modifier = Modifier
            .semantics {
                heading()
            }
            .testTag("top bar"),
        backgroundColor = MaterialTheme.colors.surface
    )
}

@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    onItemClicked: (Int) -> Unit,
    isLoading: Boolean = false,
    characters: List<Race> = emptyList(),
    viewModel: HomeViewModel,
    filterList: Array<Boolean>

) {

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colors.surface
    ) {

        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            GroupedCheckbox(
                mItemList = listOf(
                    "Greyhound",
                    "Harness \nracing",
                    "Horse \nracing"
                ),
                viewModel,
                filterList
            )
        }

        LazyColumn(
            contentPadding = PaddingValues(vertical = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 60.dp, end = 8.dp, bottom = 8.dp),

            content = {
                items(characters.size) { index ->
                    RaceItem(
                        modifier = Modifier.fillMaxWidth(),
                        item = characters[index],
                        onItemClicked = { onItemClicked(it) },
                        textStyle = TextStyle(fontSize = 18.sp)
                    )
                }
            }
        )
        if (isLoading) FullScreenLoading()
    }
}

@Composable
private fun HomeBottomBar(
    onResetPressed: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                enabled = true,
                onClick = onResetPressed
            ) {
                Text(text = stringResource(id = R.string.reset_filter))
            }
        }
    }
}

@Composable
fun GroupedCheckbox(mItemList: List<String>, viewModel: HomeViewModel, filterList: Array<Boolean>) {
    mItemList.forEachIndexed { index, item ->
        val isChecked = filterList[index]
        Row(
            modifier = Modifier
                .padding(1.dp)
                .sizeIn(
                    minWidth = 48.dp,
                    minHeight = 48.dp
                )
                .semantics(mergeDescendants = true) { }
                .toggleable(
                    value = isChecked,
                    enabled = true,
                    role = Role.Checkbox,
                    onValueChange = { viewModel.setFilter(index, it) },
                ),
            verticalAlignment = Alignment.CenterVertically,

            ) {

            Checkbox(
                checked = isChecked,
                onCheckedChange = null,
                enabled = true,
                modifier = Modifier.padding(16.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color.Magenta,
                    uncheckedColor = Color.DarkGray,
                    checkmarkColor = Color.Cyan
                )
            )
            Text(text = item)
        }
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}