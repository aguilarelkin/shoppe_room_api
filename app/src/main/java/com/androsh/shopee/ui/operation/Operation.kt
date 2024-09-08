package com.androsh.shopee.ui.operation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Operation(id: String? = null) {
    DataOperation(id)
}

@Composable
private fun DataOperation(id: String?) {
    var name by rememberSaveable { mutableStateOf("") }
    var cost by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp, vertical = 40.dp)
        ) {
            item {
                LevelText(data = "Nombre")
                FieldName(dataValue = name) {
                    name = it
                }
                LevelText(data = "Precio")
                FieldName(dataValue = cost) {
                    cost = it
                }
                LevelText(data = "Descripcion")
                FieldName(dataValue = description) {
                    description = it
                }
                Button(onClick = {
                    dataOperation(
                        name, cost, description
                    )
                }) {
                    val a = if (id.equals("new")) {
                        "create"
                    } else {
                        "update"
                    }

                    Text(text = a)
                }
            }
        }
    }
}

@Composable
private fun LevelText(data: String) {
    Text(
        text = data,
        modifier = Modifier
            .wrapContentSize()
    )

}

@Composable
private fun FieldName(dataValue: String, dataOnChange: (String) -> Unit) {
    TextField(
        value = dataValue,
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        onValueChange = { dataOnChange(it) },

        )
}

private fun dataOperation(name: String, cost: String, description: String) {
    print(" $name -- $cost  -- $description ")
}