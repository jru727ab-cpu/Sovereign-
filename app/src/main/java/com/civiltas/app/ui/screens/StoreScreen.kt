package com.civiltas.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.civiltas.app.ui.theme.*

@Composable
fun StoreScreen() {
    Column(modifier = Modifier.fillMaxSize().background(DeepNavy)) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
            Text(
                "ORDER ARCHIVES", fontSize = 10.sp, color = Gold, letterSpacing = 3.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text("Store", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(4.dp))
            Text("Premium knowledge & benefits", fontSize = 13.sp, color = TextMuted)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item { SectionHeader("Secrets Packs") }
            item {
                StoreItemCard(
                    title = "Gnosis Bundle I",
                    description = "Unlock 3 Lore secrets: First Signal, Compass Cipher, and Archivist's Last Entry",
                    price = "\$2.99",
                    badge = "POPULAR"
                )
            }
            item {
                StoreItemCard(
                    title = "Operative Dossier",
                    description = "Unlock 2 Survival Intel secrets: Corridor Map Fragment and Convergence Protocol",
                    price = "\$1.99"
                )
            }
            item {
                StoreItemCard(
                    title = "Complete Secrets Library",
                    description = "Unlock all 12 secrets in the Order of the Compass Secrets Library",
                    price = "\$6.99",
                    badge = "BEST VALUE"
                )
            }
            item { SectionHeader("Subscriptions") }
            item {
                StoreItemCard(
                    title = "VIP: Compass Bearer",
                    description = "Monthly subscription: 2x ore production, exclusive secrets, ad-free, season pass included",
                    price = "\$4.99/mo",
                    badge = "VIP"
                )
            }
            item { SectionHeader("One-Time Purchases") }
            item {
                StoreItemCard(
                    title = "Remove Ads",
                    description = "Permanently remove all advertisements from CIVILTAS",
                    price = "\$1.99"
                )
            }
            item {
                StoreItemCard(
                    title = "Catastrophe Cycle Season Pass",
                    description = "Full access to seasonal content, exclusive rewards, and catastrophe event participation",
                    price = "\$9.99"
                )
            }
            item {
                Spacer(Modifier.height(8.dp))
                Text(
                    "⚠ Store is coming soon. All purchases are non-functional stubs in this MVP.",
                    fontSize = 11.sp, color = TextMuted,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        title, fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
        color = TextMuted, letterSpacing = 1.sp,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
fun StoreItemCard(title: String, description: String, price: String, badge: String? = null) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                if (badge != null) {
                    Surface(
                        color = Gold.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            badge, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 9.sp, color = Gold, fontWeight = FontWeight.Bold, letterSpacing = 1.sp
                        )
                    }
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(description, fontSize = 13.sp, color = TextMuted, lineHeight = 18.sp)
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(price, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Gold)
                Button(
                    onClick = {},
                    enabled = false,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = SlateLight,
                        disabledContentColor = TextMuted
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Coming Soon", fontSize = 12.sp)
                }
            }
        }
    }
}
