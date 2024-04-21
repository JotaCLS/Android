package com.example.watchlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.watchlist.ui.theme.WatchListTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.lifecycle.ViewModel
import androidx.activity.viewModels
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration



class MainActivity : ComponentActivity() {
    private val viewModel: WatchListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchListTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WatchListScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun WatchListScreen(viewModel: WatchListViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        AddMovie(viewModel::addMovie)
        WatchList(
            watchList = viewModel.watchList.value,
            onToggleWatched = { movie -> viewModel.toogleWatched(movie) }
        )
    }
}

@Composable
fun WatchList(watchList: List<Movie>, onToggleWatched: (Movie) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (watchList.isEmpty()) {
            Text("Your watchlist is empty")
        } else {
            watchList.forEach { movie ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { onToggleWatched(movie) }) {

                        val iconAlpha = if (movie.watched) 1f else 0.3f
                        Icon(
                            Icons.Filled.Visibility,
                            contentDescription = "Toggle watched status",
                            tint = Color.Transparent.copy(alpha = iconAlpha)
                        )
                    }
                    Text(
                        text = movie.title,
                        modifier = Modifier.weight(1f),
                        textDecoration = if (movie.watched) TextDecoration.LineThrough else null
                    )
                    if (movie.watched) {
                        Text("Watched", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun AddMovie(onAdd: (Movie) -> Unit) {
    val (text, setText) = remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = setText,
            label = { Text("Add a movie") },
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = {
            onAdd(Movie(text))
            setText("")
        }) {
            Icon(Icons.Filled.Add, contentDescription = "Add movie")
        }
    }
}

data class Movie(val title: String, val watched: Boolean = false)

class WatchListViewModel : ViewModel() {
    val watchList = mutableStateOf<List<Movie>>(listOf(
        Movie("The Shawshank Redemption", watched = true),
        Movie("Fight Club"),
        Movie("The Dark Knight"),
        Movie("Scarface", watched = true),
        Movie("American Psycho"),
        Movie("Dead Poets Society", watched = true),

    ))

    fun addMovie(movie: Movie) {
        val updatedList = watchList.value.toMutableList()
        updatedList.add(movie)
        watchList.value = updatedList
    }

    fun toogleWatched(movie: Movie) {
        val updatedList = watchList.value.toMutableList()
        val index = updatedList.indexOf(movie)
        updatedList[index] = movie.copy(watched = !movie.watched)
        watchList.value = updatedList
    }
}



