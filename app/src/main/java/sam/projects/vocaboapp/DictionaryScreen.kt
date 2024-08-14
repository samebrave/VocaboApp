package sam.projects.vocaboapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun DictionaryScreen(viewModel: DictionaryViewModel = viewModel()) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.selamlar),
                contentDescription = null,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        item {
            OutlinedTextField(
                value = viewModel.word,
                onValueChange = { viewModel.word = it },
                label = { Text("Enter a word") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Button(onClick = { viewModel.searchWord() }) {
                Text("Search")
            }
        }

        item {
            if (viewModel.isLoading) {
                CircularProgressIndicator()
            } else if (viewModel.error.isNotEmpty()) {
                Text(viewModel.error, color = MaterialTheme.colorScheme.error)
            } else {
                Text(viewModel.definition)
            }
        }

        items(viewModel.previousSearches) { previousSearch ->
            Text(text = previousSearch, style = MaterialTheme.typography.bodyMedium)
        }
    }
}