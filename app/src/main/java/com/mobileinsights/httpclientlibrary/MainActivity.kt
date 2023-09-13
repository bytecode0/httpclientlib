package com.mobileinsights.httpclientlibrary

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobileinsights.httpclient.get
import com.mobileinsights.httpclientlibrary.ui.theme.HttpClientLibraryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HttpClientLibraryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GetItems()
                }
            }
        }
    }
}

@Composable
fun GetItems(modifier: Modifier = Modifier) {
    val amiiboList = remember { mutableStateOf<List<AmiiboItem>>(listOf()) }
    Column {
        Button(onClick = {
            get(
                endpoint = "https://www.amiiboapi.com/api/amiibo/",
                onSuccess = {
                    if (it.responseCode == 200) {
                        try {
                            val response: AmiiboResponse = parseJson(it.response)
                            amiiboList.value = response.amiibo
                        } catch (e: Exception) {
                            Log.d("ERROR", "Error at try to parse")
                        }
                    }
                }
            )
        }) {
            Text(
                text = "Get Amiibos",
                modifier = modifier
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(amiiboList.value) { item ->
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                    ) {
                        Column {
                            Text(
                                text = "Amiibo Series: ${item.amiiboSeries}",
                                modifier = modifier
                            )
                            Text(
                                text = "Name: ${item.name}",
                                modifier = modifier
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HttpClientLibraryTheme {
        GetItems()
    }
}