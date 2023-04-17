@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.weatherapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.weatherapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarUi(
    onValueChanged: (String) -> Unit = {},
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(Size.spaceXS)
            .fillMaxWidth()
            .height(Size.space7XL),
    ) {
        val image: Painter = painterResource(id = R.drawable.search_icon)
        val text = rememberSaveable { mutableStateOf("") }
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            value = text.value,
            onValueChange = {
                text.value = it
                onValueChanged(it)
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }),
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .clickable {
                    onClick()
                },
            textStyle = LocalTextStyle.current.copy(fontSize = FontSize.medium),
            leadingIcon = {
                Image(
                    modifier = Modifier.padding(Size.space2XS),
                    painter = image,
                    contentDescription = null
                )
            },
            enabled = enabled,
            placeholder = {
                Text(
                    fontSize = FontSize.medium,
                    text = stringResource(R.string.search_bar_hint),
                )
            }
        )
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}