package com.yuriikonovalov.helia.presentation.search

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SheetState
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.designsystem.components.Checkbox
import com.yuriikonovalov.helia.designsystem.components.Chip
import com.yuriikonovalov.helia.designsystem.components.Divider
import com.yuriikonovalov.helia.designsystem.components.HotelDisplayMode
import com.yuriikonovalov.helia.designsystem.components.HotelSummaries
import com.yuriikonovalov.helia.designsystem.components.NavbarAction
import com.yuriikonovalov.helia.designsystem.components.PrimaryButton
import com.yuriikonovalov.helia.designsystem.components.SearchBar
import com.yuriikonovalov.helia.designsystem.components.SecondaryButton
import com.yuriikonovalov.helia.designsystem.theme.HeliaTheme
import com.yuriikonovalov.helia.domain.entities.HotelSummary
import com.yuriikonovalov.helia.domain.entities.SearchQuery
import com.yuriikonovalov.helia.domain.valueobjects.HotelCategory
import com.yuriikonovalov.helia.domain.valueobjects.HotelFacility
import com.yuriikonovalov.helia.domain.valueobjects.HotelSearchFilter
import com.yuriikonovalov.helia.presentation.home.stringResId
import com.yuriikonovalov.helia.presentation.utils.asStringRes
import com.yuriikonovalov.helia.presentation.utils.getIconResWithCurrentMode
import com.yuriikonovalov.helia.presentation.utils.getTintWithCurrentMode
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    onHotelClick: (hotelId: String) -> Unit,
    viewModel: SearchScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        state = state,
        onSearchValueChanged = { viewModel.handleIntent(SearchIntent.UpdateSearchValue(it)) },
        onSearchClick = { viewModel.handleIntent(SearchIntent.Search) },
        onHotelCategoryClick = { viewModel.handleIntent(SearchIntent.ChangeHotelCategory(it)) },
        onHotelClick = onHotelClick,
        onBookmarkHotelClick = { id, isBookmarked ->
            viewModel.handleIntent(SearchIntent.BookmarkHotel(id, isBookmarked))
        },
        onUpdateDisplayMode = { viewModel.handleIntent(SearchIntent.ChangeDisplayMode(it)) },
        onDeleteQuery = { viewModel.handleIntent(SearchIntent.DeleteRecentSearchQuery(it)) },
        onUpdateSearchFocus = { viewModel.handleIntent(SearchIntent.UpdateSearchBarFocus(it)) },
        onCountryClick = { viewModel.handleIntent(SearchIntent.ClickCountry(it)) },
        onSortClick = { viewModel.handleIntent(SearchIntent.ClickSort(it)) },
        onPriceRangeChanged = { viewModel.handleIntent(SearchIntent.ChangePriceRange(it)) },
        onFacilityClick = { viewModel.handleIntent(SearchIntent.ClickFacility(it)) },
        onResetClick = { viewModel.handleIntent(SearchIntent.ResetFilter) },
        onResetUnappliedFilter = { viewModel.handleIntent(SearchIntent.ResetUnappliedFilter) },
        onApplyClick = { viewModel.handleIntent(SearchIntent.ApplyFilter) },
    )

    val localFocusManager = LocalFocusManager.current
    BackHandler(state.isSearchBarFocused) {
        viewModel.handleIntent(SearchIntent.UpdateSearchBarFocus(false))
        localFocusManager.clearFocus()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchScreenContent(
    state: SearchUiState,
    onSearchValueChanged: (String) -> Unit,
    onSearchClick: () -> Unit,
    onHotelCategoryClick: (category: HotelCategory?) -> Unit,
    onHotelClick: (hotelId: String) -> Unit,
    onBookmarkHotelClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    onUpdateDisplayMode: (displayMode: HotelDisplayMode) -> Unit,
    onDeleteQuery: (id: String) -> Unit,
    onUpdateSearchFocus: (focused: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onCountryClick: (String) -> Unit,
    onSortClick: (HotelSearchFilter.HotelSearchSort) -> Unit,
    onFacilityClick: (HotelFacility) -> Unit,
    onResetClick: () -> Unit,
    onResetUnappliedFilter: () -> Unit,
    onApplyClick: () -> Unit,
    onPriceRangeChanged: (HotelSearchFilter.PriceRange) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isFilterBottomSheetOpen by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(focused) {
        snapshotFlow { focused }
            .collect {
                onUpdateSearchFocus(focused)
            }
    }

    Column(modifier = modifier) {
        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 24.dp),
            value = state.searchValue,
            onValueChanged = onSearchValueChanged,
            onSearchKeyboardAction = {
                focusManager.clearFocus()
                onSearchClick()
            },
            onFilterClick = {
                coroutineScope.launch {
                    isFilterBottomSheetOpen = true
                    bottomSheetState.show()
                }
            },
            interactionSource = interactionSource
        )
        HotelCategories(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            selectedCategory = state.hotelCategory,
            onHotelCategoryClick = onHotelCategoryClick
        )

        SearchContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .weight(1f),
            isSearchBarFocused = state.isSearchBarFocused,
            hotelDisplayMode = state.displayMode,
            hotelCategory = state.hotelCategory,
            numberOfHotels = state.numberOfHotels,
            isSearching = state.isSearching,
            hotels = state.hotels,
            onHotelClick = onHotelClick,
            onBookmarkHotelClick = onBookmarkHotelClick,
            onUpdateDisplayMode = onUpdateDisplayMode,
            searchQueries = state.searchQueries,
            onDeleteQuery = onDeleteQuery,
            onQueryClick = { text ->
                onSearchValueChanged(text)
                onSearchClick()
                focusManager.clearFocus()
            }
        )
    }


    FilterBottomSheet(
        isOpen = isFilterBottomSheetOpen,
        filter = state.filter,
        countries = state.countries,
        onCountryClick = onCountryClick,
        onSortClick = onSortClick,
        onPriceRangeChanged = onPriceRangeChanged,
        onFacilityClick = onFacilityClick,
        onDismissRequest = {
            coroutineScope
                .launch { bottomSheetState.hide() }
                .invokeOnCompletion {
                    isFilterBottomSheetOpen = false
                    onResetUnappliedFilter()
                }
        },
        bottomSheetState = bottomSheetState,
        onResetClick = {
            coroutineScope
                .launch { bottomSheetState.hide() }
                .invokeOnCompletion {
                    isFilterBottomSheetOpen = false
                    onResetClick()
                }
        },
        onApplyClick = {
            coroutineScope
                .launch { bottomSheetState.hide() }
                .invokeOnCompletion {
                    isFilterBottomSheetOpen = false
                    onApplyClick()
                }
        },
    )
}


@Composable
private fun HotelCategories(
    selectedCategory: HotelCategory?,
    onHotelCategoryClick: (category: HotelCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = HotelCategory.values()
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Chip(
                toggled = selectedCategory == null,
                onClick = { onHotelCategoryClick(null) },
                text = stringResource(R.string.search_screen_category_all_hotels)
            )
        }
        items(items = categories, key = { it.ordinal }) { category ->
            Chip(
                toggled = category == selectedCategory,
                onClick = { onHotelCategoryClick(category) },
                text = stringResource(category.stringResId)
            )
        }
    }
}


@Composable
private fun SearchContent(
    isSearchBarFocused: Boolean,
    hotelDisplayMode: HotelDisplayMode,
    hotelCategory: HotelCategory?,
    numberOfHotels: Int,
    isSearching: Boolean,
    hotels: List<HotelSummary>,
    onHotelClick: (hotelId: String) -> Unit,
    onBookmarkHotelClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    onUpdateDisplayMode: (displayMode: HotelDisplayMode) -> Unit,
    searchQueries: List<SearchQuery>,
    onDeleteQuery: (id: String) -> Unit,
    onQueryClick: (text: String) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        modifier = modifier,
        targetState = isSearchBarFocused,
        label = "SearchContent"
    ) { focused ->
        if (focused) {
            RecentSearchQueries(
                modifier = Modifier.fillMaxWidth(),
                searchQueries = searchQueries,
                onDeleteQuery = onDeleteQuery,
                onQueryClick = onQueryClick
            )
        } else {
            Hotels(
                modifier = Modifier.fillMaxWidth(),
                hotelDisplayMode = hotelDisplayMode,
                hotelCategory = hotelCategory,
                numberOfHotels = numberOfHotels,
                isSearching = isSearching,
                hotels = hotels,
                onHotelClick = onHotelClick,
                onBookmarkHotelClick = onBookmarkHotelClick,
                onUpdateDisplayMode = onUpdateDisplayMode
            )
        }
    }
}

@Composable
private fun RecentSearchQueries(
    searchQueries: List<SearchQuery>,
    onDeleteQuery: (id: String) -> Unit,
    onQueryClick: (text: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val queryColor =
        if (HeliaTheme.theme.isDark) HeliaTheme.colors.greyscale400 else HeliaTheme.colors.greyscale600

    Column(modifier = modifier) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            text = stringResource(R.string.search_screen_recent),
            style = HeliaTheme.typography.heading6,
            color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = searchQueries, key = { it.id }) { query ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 12.dp)
                            .clickable { onQueryClick(query.text) },
                        text = query.text,
                        style = HeliaTheme.typography.bodyXLargeMedium,
                        color = queryColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    IconButton(onClick = { onDeleteQuery(query.id) }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_square_border),
                            contentDescription = stringResource(R.string.search_screen_cd_delete_query),
                            tint = queryColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Hotels(
    hotelDisplayMode: HotelDisplayMode,
    hotelCategory: HotelCategory?,
    numberOfHotels: Int,
    isSearching: Boolean,
    hotels: List<HotelSummary>,
    onHotelClick: (hotelId: String) -> Unit,
    onBookmarkHotelClick: (hotelId: String, isBookmarked: Boolean) -> Unit,
    onUpdateDisplayMode: (displayMode: HotelDisplayMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        HotelsHeader(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            numberOfHotels = numberOfHotels,
            hotelCategory = hotelCategory,
            hotelDisplayMode = hotelDisplayMode,
            onListClick = { onUpdateDisplayMode(HotelDisplayMode.LIST) },
            onGridClick = { onUpdateDisplayMode(HotelDisplayMode.GRID) }
        )
        HotelSummaries(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            isLoading = isSearching,
            hotelDisplayMode = hotelDisplayMode,
            hotels = hotels,
            onHotelClick = onHotelClick,
            onBookmarkHotelClick = onBookmarkHotelClick
        )
    }
}

@Composable
private fun HotelsHeader(
    numberOfHotels: Int,
    hotelCategory: HotelCategory?,
    hotelDisplayMode: HotelDisplayMode,
    onListClick: () -> Unit,
    onGridClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val text = if (hotelCategory == null) {
        stringResource(R.string.search_screen_all, numberOfHotels)
    } else {
        stringResource(hotelCategory.stringResId) + " ($numberOfHotels)"
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = HeliaTheme.typography.heading6,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (HeliaTheme.theme.isDark) HeliaTheme.colors.white else HeliaTheme.colors.greyscale900
        )
        Row {
            NavbarAction(
                iconResId = HotelDisplayMode.LIST.getIconResWithCurrentMode(hotelDisplayMode),
                onClick = onListClick,
                contentDescription = stringResource(R.string.recently_booked_screen_cd_list),
                tint = HotelDisplayMode.LIST.getTintWithCurrentMode(hotelDisplayMode)
            )
            NavbarAction(
                iconResId = HotelDisplayMode.GRID.getIconResWithCurrentMode(hotelDisplayMode),
                onClick = onGridClick,
                contentDescription = stringResource(R.string.recently_booked_screen_cd_grid),
                tint = HotelDisplayMode.GRID.getTintWithCurrentMode(hotelDisplayMode)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet(
    isOpen: Boolean,
    filter: HotelSearchFilter,
    countries: List<String>,
    onCountryClick: (String) -> Unit,
    onSortClick: (HotelSearchFilter.HotelSearchSort) -> Unit,
    onFacilityClick: (HotelFacility) -> Unit,
    onDismissRequest: () -> Unit,
    bottomSheetState: SheetState,
    onResetClick: () -> Unit,
    onApplyClick: () -> Unit,
    onPriceRangeChanged: (HotelSearchFilter.PriceRange) -> Unit
) {
    if (isOpen)
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxWidth(),
            onDismissRequest = onDismissRequest,
            sheetState = bottomSheetState,
            windowInsets = WindowInsets(0),
            containerColor = HeliaTheme.backgroundColor
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                BottomSheetHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                )
                CountryFilter(
                    modifier = Modifier.fillMaxWidth(),
                    selectedCountries = filter.countries,
                    countries = countries,
                    onCountryClick = onCountryClick
                )
                Sort(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    selectedSort = filter.sort,
                    onSortClick = onSortClick
                )
                PriceFilter(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp),
                    priceRange = filter.priceRange,
                    onPriceRangeChanged = onPriceRangeChanged
                )
                FacilityFilter(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    selectedFacilities = filter.facilities,
                    onFacilityClick = onFacilityClick
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                )
                FilterButtons(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .navigationBarsPadding(),
                    onResetClick = onResetClick,
                    onApplyClick = onApplyClick
                )
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(bottom = 16.dp)
                )
            }
        }
}

@Composable
private fun BottomSheetHeader(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = stringResource(R.string.search_screen_bottom_sheet_header),
        style = HeliaTheme.typography.heading4,
        color = HeliaTheme.primaryTextColor,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun FilterLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = text,
        color = HeliaTheme.primaryTextColor,
        style = HeliaTheme.typography.heading6
    )
}

@Composable
private fun CountryFilter(
    selectedCountries: List<String>,
    countries: List<String>,
    onCountryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        FilterLabel(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(R.string.search_screen_filter_country)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = countries, key = { it }) { country ->
                Chip(
                    toggled = country in selectedCountries,
                    text = country,
                    onClick = { onCountryClick(country) },
                )
            }
        }
    }
}

@Composable
private fun Sort(
    selectedSort: HotelSearchFilter.HotelSearchSort?,
    onSortClick: (HotelSearchFilter.HotelSearchSort) -> Unit,
    modifier: Modifier = Modifier
) {
    val sortOptions = remember { HotelSearchFilter.HotelSearchSort.values() }

    Column(modifier = modifier) {
        FilterLabel(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(R.string.search_screen_filter_sort),
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = sortOptions, key = { it.ordinal }) { sort ->
                Chip(
                    toggled = sort == selectedSort,
                    text = stringResource(id = sort.asStringRes()),
                    onClick = { onSortClick(sort) },
                )
            }
        }
    }
}

@Composable
private fun FacilityFilter(
    selectedFacilities: List<HotelFacility>,
    onFacilityClick: (HotelFacility) -> Unit,
    modifier: Modifier = Modifier
) {
    val facilities = remember { HotelFacility.values() }

    Column(modifier = modifier) {
        FilterLabel(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = stringResource(R.string.search_screen_filter_facility),
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = facilities, key = { it.ordinal }) { facility ->
                Checkbox(
                    checked = facility in selectedFacilities,
                    text = stringResource(id = facility.asStringRes()),
                    onCheckedChange = { onFacilityClick(facility) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PriceFilter(
    priceRange: HotelSearchFilter.PriceRange,
    onPriceRangeChanged: (HotelSearchFilter.PriceRange) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = SliderDefaults.colors(
        activeTrackColor = HeliaTheme.colors.primary500,
        inactiveTrackColor = if (HeliaTheme.theme.isDark) HeliaTheme.colors.dark3 else HeliaTheme.colors.greyscale200
    )
    val valueRange = remember {
        HotelSearchFilter.PriceRange.DEFAULT_FROM.toFloat()..HotelSearchFilter.PriceRange.DEFAULT_TO.toFloat()
    }

    Column(modifier = modifier) {
        FilterLabel(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.search_screen_filter_price_range)
        )
        RangeSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            valueRange = valueRange,
            value = priceRange.from.toFloat()..priceRange.to.toFloat(),
            onValueChange = { range ->
                onPriceRangeChanged(
                    HotelSearchFilter.PriceRange(
                        from = range.start.toDouble(),
                        to = range.endInclusive.toDouble()
                    )
                )
            },
            startThumb = { PriceSliderThumb(text = "$${priceRange.from.toInt()}") },
            endThumb = { PriceSliderThumb(text = "$${priceRange.to.toInt()}") },
            colors = colors
        )
    }
}

@Composable
private fun PriceSliderThumb(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(HeliaTheme.colors.white)
                .border(4.dp, Brush.linearGradient(HeliaTheme.colors.gradientGreen), CircleShape)
        )
        Box(
            modifier = Modifier
                .offset(y = -(24.dp))
                .clip(RoundedCornerShape(4.dp))
                .background(Brush.linearGradient(HeliaTheme.colors.gradientGreen)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(vertical = 2.dp, horizontal = 4.dp),
                text = text,
                style = HeliaTheme.typography.bodyMediumSemiBold,
                color = HeliaTheme.colors.white
            )
        }
    }
}


@Composable
private fun FilterButtons(
    onResetClick: () -> Unit,
    onApplyClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SecondaryButton(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.search_screen_bottom_sheet_reset_button),
            onClick = onResetClick
        )
        PrimaryButton(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.search_screen_bottom_sheet_apply_button),
            onClick = onApplyClick
        )
    }
}