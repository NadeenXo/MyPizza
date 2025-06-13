package com.example.mypizza

import androidx.compose.animation.animateColorAsState
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
    val selectedBreadIndex = rememberSaveable { mutableIntStateOf(0) }
    val breadSizes = rememberSaveable {
        mutableStateOf(List(breadImages.size) { "M" })
    }

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
        Plate(modifier, currentSize, breadImages[currentIndex])
        PizzaSizeSelector(
            modifier = Modifier.padding(8.dp),
            selectedSize = currentSize,
            onSizeSelected = { newSize ->
                breadSizes.value = breadSizes.value.toMutableList().also {
                    it[currentIndex] = newSize
                }
            }
        )
        IngredientSection(modifier)
        Spacer(modifier = Modifier.weight(0.1f))
        AddToCartBtn(modifier)
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun IngredientSection(modifier: Modifier) {
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
        item { AddIngredient(R.drawable.basil_8) }
        item { AddIngredient(R.drawable.basil_8) }
        item { AddIngredient(R.drawable.broccoli_3) }
        item { AddIngredient(R.drawable.plate) }
        item { AddIngredient(R.drawable.bread_1) }
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
        Icon(painterResource(R.drawable.fav), contentDescription = "shopping cart")
        Spacer(modifier.padding(4.dp))
        Text("Add to cart")
    }
}

@Composable
fun Plate(modifier: Modifier, pizzaSize: String, bread: Int) {
    val sizeDp = when (pizzaSize.lowercase()) {
        "s" -> 180.dp
        "m" -> 200.dp
        else -> 220.dp
    }

    val animatedSize = androidx.compose.animation.core.animateDpAsState(
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
        }
        val basePrice = when (pizzaSize) {
            "s" -> 12
            "m" -> 15
            else -> 17
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
//        onClick = onToggle,
        onClick = { isSelected.value = !isSelected.value },
        shape = CircleShape,
        colors = ButtonColors(
            containerColor = if (isSelected.value) Color(0xFFD0E8FF) else Color.White,
            contentColor = Color.White,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White
        ),
        modifier = Modifier.size(80.dp)
    ) {
        Image(
            painter = painterResource(img),
            contentDescription = "",
//            modifier = Modifier.size(32.dp)
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
//            val textColor = if (isSelected) Color.White else Color.LightGray

            Button(
                onClick = { onSizeSelected(size) },
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = bgColor.value,
                    contentColor = Color.Black
                ),
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
//                Text(size)
                Text(
                    text = size,
                    color = if (isSelected) Color.Black else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )

            }
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