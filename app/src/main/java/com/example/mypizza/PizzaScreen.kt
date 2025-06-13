package com.example.mypizza

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitHorizontalDragOrCancellation
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PizzaScreen(modifier: Modifier) {
    val breadImages = listOf(
        R.drawable.bread_1,
        R.drawable.bread_2,
        R.drawable.bread_3,
        R.drawable.bread_4,
        R.drawable.bread_5
    )
    val onionImages = listOf(
        R.drawable.onion_1,
        R.drawable.onion_2,
        R.drawable.onion_3,
        R.drawable.onion_4,
        R.drawable.onion_5,
        R.drawable.onion_6,
        R.drawable.onion_7,
        R.drawable.onion_8,
    )

    val basilImages = listOf(
        R.drawable.basil_1,
        R.drawable.basil_2,
        R.drawable.basil_3,
        R.drawable.basil_4,
        R.drawable.basil_5,
        R.drawable.basil_6,
        R.drawable.basil_7,
        R.drawable.basil_8,
    )

    val broccoliImages = listOf(
        R.drawable.broccoli_1,
        R.drawable.broccoli_2,
        R.drawable.broccoli_3,
        R.drawable.broccoli_4,
        R.drawable.broccoli_5,
        R.drawable.broccoli_6,
        R.drawable.broccoli_7,
        R.drawable.broccoli_8,
    )

    val mushroomImages = listOf(
        R.drawable.mushroom_1,
        R.drawable.mushroom_2,
        R.drawable.mushroom_3,
        R.drawable.mushroom_4,
        R.drawable.mushroom_5,
        R.drawable.mushroom_6,
        R.drawable.mushroom_7,
        R.drawable.mushroom_8,
    )

    val sausageImages = listOf(
        R.drawable.sausage_1,
        R.drawable.sausage_2,
        R.drawable.sausage_3,
        R.drawable.sausage_4,
        R.drawable.sausage_5,
        R.drawable.sausage_6,
        R.drawable.sausage_7,
        R.drawable.sausage_8,
    )

    val toppingImagesMap = mapOf(
        "onion" to onionImages,
        "basil" to basilImages,
        "broccoli" to broccoliImages,
        "mushroom" to mushroomImages,
        "sausage" to sausageImages
    )

    val selectedBreadIndex = rememberSaveable { mutableIntStateOf(0) }
    val breadSizes = rememberSaveable {
        mutableStateOf(List(breadImages.size) { "M" })
    }
    val toppingsPerBread = rememberSaveable {
        mutableStateOf(List(breadImages.size) { mutableSetOf<String>() })
    }

    val selectedToppings = toppingsPerBread.value[selectedBreadIndex.intValue]

    val gestureModifier = modifier.pointerInput(Unit) {
        forEachGesture {
            awaitPointerEventScope {
                val down = awaitFirstDown()
                val drag = awaitHorizontalDragOrCancellation(down.id)

                drag?.let {
                    val currentIndex = selectedBreadIndex.intValue

                    if (it.positionChange().x > 50 && currentIndex > 0) {
                        // Swipe right
                        selectedBreadIndex.intValue = currentIndex - 1
                    } else if (it.positionChange().x < -50 && currentIndex < breadImages.lastIndex) {
                        // Swipe left
                        selectedBreadIndex.intValue = currentIndex + 1
                    }

                    it.consume()
                }
            }
        }
    }

    val currentIndex = selectedBreadIndex.intValue
    val currentSize = breadSizes.value[currentIndex]

    Column(
        modifier = gestureModifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 12.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(modifier)
//        Plate(modifier, currentSize, breadImages[currentIndex])
        Plate(modifier, currentSize, breadImages[currentIndex], selectedToppings, toppingImagesMap)


        PizzaSizeSelector(
            modifier = Modifier.padding(8.dp),
            selectedSize = currentSize,
            onSizeSelected = { newSize ->
                breadSizes.value = breadSizes.value.toMutableList().also {
                    it[currentIndex] = newSize
                }
            }
        )
        IngredientSection(modifier, selectedToppings)
        Spacer(modifier = Modifier.weight(0.1f))
        AddToCartBtn(modifier)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun IngredientSection(
    modifier: Modifier,
    selectedToppings: MutableSet<String>,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        text = "Customize your pizza",
        fontWeight = FontWeight.Light,
        textAlign = TextAlign.Start
    )

    LazyRow(
        modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        item { AddIngredientButton("basil", R.drawable.basil_8, selectedToppings) }
        item { AddIngredientButton("onion", R.drawable.onion_3, selectedToppings) }
        item { AddIngredientButton("broccoli", R.drawable.broccoli_3, selectedToppings) }
        item { AddIngredientButton("mushroom", R.drawable.mushroom_3, selectedToppings) }
        item { AddIngredientButton("sausage", R.drawable.sausage_3, selectedToppings) }
    }

}


@Composable
fun AddIngredientButton(
    name: String,
    imageId: Int,
    selectedToppings: MutableSet<String>,
) {
    val isSelected = remember { mutableStateOf(name in selectedToppings) }

    Button(
        onClick = {
            val willSelect = !isSelected.value
            isSelected.value = willSelect
            if (willSelect) selectedToppings.add(name)
            else selectedToppings.remove(name)
        },
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected.value) Color(0xFFD0E8FF) else Color.White
        ),
        modifier = Modifier.size(90.dp)
    ) {
        Image(painter = painterResource(id = imageId), contentDescription = null)
    }
}


@Composable
private fun AddToCartBtn(modifier: Modifier) {
    Button(
        onClick = {}, shape = RoundedCornerShape(10.dp), colors = ButtonColors(
            containerColor = Color.Black, contentColor = Color.White,
            disabledContainerColor = Color.Black,
            disabledContentColor = Color.White
        )
    ) {
        Icon(
            painterResource(R.drawable.shopping_cart),
            contentDescription = "shopping cart",
            tint = Color.White, modifier = modifier.size(32.dp)
        )
        Spacer(modifier.padding(4.dp))
        Text("Add to cart")
    }
}

@Composable
fun Plate(
    modifier: Modifier,
    pizzaSize: String,
    bread: Int,
    selectedToppings: Set<String>,
    toppingImagesMap: Map<String, List<Int>>
) {
    val sizeDp = when (pizzaSize.lowercase()) {
        "s" -> 180.dp
        "m" -> 200.dp
        else -> 220.dp
    }
    val basePrice = when (pizzaSize.lowercase()) {
        "s" -> 12
        "m" -> 15
        else -> 17
    }

    val animatedSize = animateDpAsState(
        targetValue = sizeDp,
        label = "animateBreadSize"
    )

    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.plate),
                contentDescription = "plate",
                modifier = Modifier.width(250.dp)
            )
            Image(
                painter = painterResource(bread),
                contentDescription = "bread",
                modifier = Modifier.width(animatedSize.value)
            )
            Box(modifier = Modifier.width(sizeDp)) {
                ToppingOverlay(pizzaSize, selectedToppings, toppingImagesMap)
            }
        }
        Text(
            text = "$$basePrice",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun AddIngredient(img: Int) {
    val isSelected = rememberSaveable { mutableStateOf(false) }

    Button(
        onClick = { isSelected.value = !isSelected.value },
        shape = CircleShape,
        colors = ButtonColors(
            containerColor = if (isSelected.value) Color(0xFFD0E8FF) else Color.White,
            contentColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White
        ),
        modifier = Modifier.size(90.dp)
    ) {
        Image(
            painter = painterResource(img),
            contentDescription = "",
        )
    }
}

@Composable
fun PizzaSizeSelector(modifier: Modifier, selectedSize: String, onSizeSelected: (String) -> Unit) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        listOf("S", "M", "L").forEach { size ->
            val isSelected = size == selectedSize
            val bgColor = animateColorAsState(
                targetValue = if (isSelected) Color.LightGray else Color.White,
                label = "pizzaSizeColor"
            )
            Button(
                onClick = { onSizeSelected(size) },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = bgColor.value,
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .padding(4.dp)
                    .size(62.dp)
            ) {
                Text(
                    text = size, fontSize = 16.sp,
                    color = Color.Black,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )

            }
        }
    }
}

@Composable
fun ToppingOverlay(
    pizzaSize: String,
    selectedToppings: Set<String>,
    toppingImagesMap: Map<String, List<Int>>
) {
    val positions = when (pizzaSize.lowercase()) {
        "s" -> listOf(
            DpOffset(40.dp, 20.dp), DpOffset(60.dp, 60.dp),
            DpOffset(100.dp, 30.dp), DpOffset(130.dp, 70.dp),
            DpOffset(90.dp, 100.dp), DpOffset(30.dp, 90.dp),
            DpOffset(70.dp, 40.dp), DpOffset(120.dp, 110.dp)
        )

        "m" -> listOf(
            DpOffset(50.dp, 30.dp), DpOffset(70.dp, 70.dp),
            DpOffset(110.dp, 40.dp), DpOffset(140.dp, 80.dp),
            DpOffset(100.dp, 120.dp), DpOffset(40.dp, 100.dp),
            DpOffset(80.dp, 50.dp), DpOffset(130.dp, 130.dp)
        )

        else -> listOf(
            DpOffset(60.dp, 40.dp), DpOffset(80.dp, 80.dp),
            DpOffset(120.dp, 50.dp), DpOffset(150.dp, 90.dp),
            DpOffset(110.dp, 140.dp), DpOffset(50.dp, 110.dp),
            DpOffset(90.dp, 60.dp), DpOffset(140.dp, 150.dp)
        )
    }


    var drawn = 0
    for (topping in selectedToppings) {
        val images = toppingImagesMap[topping].orEmpty()
        for (img in images) {
            if (drawn >= positions.size) break
            val offset = positions[drawn]
            Image(
                painter = painterResource(id = img),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
                    .offset(offset.x, offset.y)
            )
            drawn++
        }

    }
}


@Composable
fun Header(modifier: Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(painter = painterResource(R.drawable.arrow_back), contentDescription = "")
        Text(
            text = "Pizza",
            modifier.weight(1f),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Icon(painterResource(R.drawable.fav), contentDescription = "", tint = Color.Black)
    }
}

@Preview
@Composable
fun PreviewPizzaScreen() {
    PizzaScreen(modifier = Modifier)
}