package org.example.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.Image
import com.seiko.imageloader.rememberImagePainter
import org.example.project.Data.Product
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
@Preview
fun App() {
    MaterialTheme {
        AppContent(viewModel = HomeViewModel())
    }
}


@Composable
fun AppContent(viewModel: HomeViewModel) {

    val products = viewModel.products.collectAsState()
    var query by rememberSaveable { mutableStateOf("") }

    ProductGridScreen(
        products = products.value,
        query = query,
        onQueryChange = { query = it }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductGridScreen(
    products: List<Product>,
    query: String,
    onQueryChange: (String) -> Unit
) {
    val scrollState = rememberLazyGridState()
    val filteredProducts = products.filter {
        it.title.contains(query, ignoreCase = true)
    }

    BoxWithConstraints {
        val platform = getPlatform()
        val maxWidth = this.maxWidth

        val col = when (platform) {
            AppPlatform.IOS, AppPlatform.ANDROID -> 2
            AppPlatform.DESKTOP -> when {
                maxWidth < 500.dp -> 1
                maxWidth < 840.dp -> 3
                else -> 5
            }
        }

        val modifier = if (maxWidth > 840.dp) Modifier.widthIn(max = 1080.dp) else Modifier.fillMaxWidth()

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.fillMaxWidth()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(col),
                state = scrollState,
                contentPadding = PaddingValues(16.dp)
            ) {

                item(span = { GridItemSpan(col) }) {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        query = query,
                        onQueryChange = onQueryChange,
                        onSearch = {},
                        active = false,
                        onActiveChange = {},
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search"
                            )
                        },
                        placeholder = { Text("Search Products") }
                    ) {}
                }

                items(filteredProducts, key = { it.id }) { product ->
                    ProductCard(product)
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(12.dp)
        ) {

            val painter = rememberImagePainter(url = product.image)
            Image(
                painter = painter, // ✅ correct param name
                contentDescription = product.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(140.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .heightIn(min = 40.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$${product.price}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(product.rating.rate.roundToInt()) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "(${product.rating.count})", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

