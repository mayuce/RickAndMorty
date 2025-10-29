package com.aliyuce.rickandmorty.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aliyuce.rickandmorty.R
import com.aliyuce.rickandmorty.ui.theme.RickAndMortyTheme

@Composable
fun ErrorComp(
    modifier: Modifier = Modifier,
    error: String?,
    onRetry: (() -> Unit)?,
) {
    val message = error ?: stringResource(id = R.string.default_error)

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "⚠️",
            fontSize = 48.sp,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(72.dp),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (onRetry != null) {
            Button(onClick = onRetry) {
                Text(text = stringResource(id = R.string.retry))
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
)
@Composable
private fun ErrorLightPreview() {
    RickAndMortyTheme {
        ErrorComp(
            error = "An error occurred while fetching data.",
            onRetry = {},
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
)
@Composable
private fun ErrorDarkPreview() {
    RickAndMortyTheme {
        ErrorComp(
            error = "An error occurred while fetching data.",
            onRetry = {},
        )
    }
}
