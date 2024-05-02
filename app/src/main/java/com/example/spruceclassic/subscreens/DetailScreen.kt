package com.example.spruceclassic.subscreens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.spruceclassic.R


@Composable
fun DetailScreen(a: String?, b: String?, c: String?, img: String?) {

    Log.e("TAG ", "scrne : detail data: $a $b ")
    pojoDetail(v = a, date = b, vr = c, img = img)
}


@Composable
fun pojoDetail(v: String?, date: String?, vr: String?, img: String?) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(12.dp, 2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (v != null) {
            Text(
                text = v,
                fontSize = 25.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 5.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp, bottom = 5.dp)
        ) {
            // Image and text at start
            Icon(
//                            imageVector = Icons.Filled.Person,
                painterResource(id = R.drawable.ic_calendar),
                tint = Color.Gray,
                modifier = Modifier
                    .size(25.dp)
                    .padding(9.dp, 0.dp, 0.dp, 0.dp),
                contentDescription = "New Album"
            )

            if (date != null) {
                Text(
                    text = date,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(3.dp, 4.dp)
                )
            }
        }

        Image(
            painter = rememberAsyncImagePainter("https://spruce.hestawork.com/wp-content/uploads/2019/06/$img"),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.FillBounds
        )

        if (vr != null) {
            Text(text = vr,
                modifier = Modifier.padding(top = 10.dp))
        }
    }
}

