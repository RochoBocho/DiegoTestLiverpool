package com.example.diegorochintest.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.diegorochintest.dom.models.Product
import com.example.diegorochintest.ui.components.ErrorView
import com.example.diegorochintest.ui.components.LoadingWheel
import com.example.diegorochintest.ui.components.ProductItem
import com.example.diegorochintest.ui.components.SearchBar
import com.example.diegorochintest.ui.components.SortOptionDialog
import com.example.diegorochintest.ui.state.SearchEvent
import com.example.diegorochintest.ui.viewmodels.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: SearchViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Observar y reaccionar a los mensajes de error
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.onEvent(SearchEvent.OnClearError)
        }
    }

    // Recolectar los productos paginados
    val productsPagingItems = state.productsFlow.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Diego Productos") },
                navigationIcon = {
                    IconButton(onClick = {
                        (context as? Activity)?.finish()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.onEvent(SearchEvent.OnSortOptionDialogShow) }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Ordenar",
                        )
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        ) {
            SearchBar(
                searchTerm = state.searchTerm,
                onSearchTermChange = { viewModel.onEvent(SearchEvent.OnSearchTermChange(it)) },
                onSearch = { viewModel.onEvent(SearchEvent.OnSearchClick) },
            )

            Box(modifier = Modifier.weight(1f)) {
                ProductList(
                    productItems = productsPagingItems,
                    onRetry = { productsPagingItems.retry() },
                )

                // Mostrar indicador de carga si se está realizando la búsqueda
                if (state.isSearching) {
                    LoadingWheel(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }

    // Diálogo de opciones de ordenamiento
    SortOptionDialog(
        showDialog = state.showSortOptionsDialog,
        availableSortOptions = state.availableSortOptions,
        selectedSortOption = state.selectedSortOption,
        onSortOptionSelected = { sortOption ->
            viewModel.onEvent(SearchEvent.OnSortOptionChange(sortOption))
        },
        onDismiss = { viewModel.onEvent(SearchEvent.OnSortOptionDialogDismiss) },
    )
}

@Composable
fun ProductList(
    productItems: LazyPagingItems<Product>,
    onRetry: () -> Unit,
) {
    when {
        // Carga inicial
        productItems.loadState.refresh is LoadState.Loading -> {
            LoadingWheel(modifier = Modifier.fillMaxSize())
        }

        // Error en la carga inicial
        productItems.loadState.refresh is LoadState.Error -> {
            val error = (productItems.loadState.refresh as LoadState.Error).error
            ErrorView(
                message = "No se pudieron cargar los productos: ${error.localizedMessage}",
                onRetry = onRetry,
                modifier = Modifier.fillMaxSize(),
            )
        }

        // Productos cargados correctamente
        productItems.itemCount > 0 -> {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(
                    count = productItems.itemCount,
                    key = { index ->
                        // Usar el ID del producto como clave
                        productItems[index]?.id ?: index.toString()
                    },
                ) { index ->
                    productItems[index]?.let { product ->
                        ProductItem(product = product)
                    }
                }

                // Mostrar indicador de carga para la paginación
                item {
                    if (productItems.loadState.append is LoadState.Loading) {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            LoadingWheel()
                        }
                    } else if (productItems.loadState.append is LoadState.Error) {
                        val error = (productItems.loadState.append as LoadState.Error).error
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "Error al cargar más productos: ${error.localizedMessage}",
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                        }
                    }
                }
            }
        }

        // No hay resultados
        else -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "No se encontraron productos. Intenta con otra búsqueda.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}
