package com.example.diegorochintest.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.diegorochintest.dom.models.Product
import com.example.diegorochintest.ui.utils.formatCurrency

@Composable
fun ProductItem(
    product: Product,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
        ) {
            // Imagen del producto
            AsyncImage(
                model =
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(product.imageUrl)
                        .crossfade(true)
                        .build(),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier =
                    Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(4.dp)),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Precio original
                Text(
                    text = formatCurrency(product.regularPrice),
                    style =
                        MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough,
                            fontSize = 14.sp,
                        ),
                )

                // Precio con descuento
                Text(
                    text = formatCurrency(product.discountPrice),
                    style =
                        MaterialTheme.typography.bodyLarge.copy(
                            color = Color.Red,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        ),
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (product.availableColors.isNotEmpty()) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        product.availableColors.forEach { color ->
                            ColorCircle(colorHex = color.hexCode)
                        }
                    }
                }
            }
        }
    }
}
