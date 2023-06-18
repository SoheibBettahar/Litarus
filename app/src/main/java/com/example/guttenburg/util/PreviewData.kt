package com.example.guttenburg.util

import com.example.guttenburg.data.repository.model.Book
import com.example.guttenburg.data.repository.model.BookWithExtras

val DEFAULT_BOOK = Book(
    id = 0,
    title = "Moby Dick",
    authors = listOf("Herman Meville"),
    imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
)


val DEFAULT_BOOK_WITH_EXTRAS = BookWithExtras(
    id = 145,
    title = "Middlemarch",
    description = "This is an analysis of the life of an English provincial town during the" +
            " time of social unrest prior to the first Reform Bill of 1832. It is told through" +
            " the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters" +
            " who illuminate the condition of English life in the mid 19th century. This is an " +
            "analysis of the life of an English provincial town during the time of social unrest" +
            " prior to the first Reform Bill of 1832. It is told through the lives of Dorothea " +
            "<Brooke and Dr Tertius Lydgate and includes a host of characters who illuminate the " +
            "condition of English life in the mid 19th century. This is an analysis of the life " +
            "of an English provincial town during the time of social unrest prior to the first " +
            "Reform Bill of 1832. It is told through the lives of Dorothea Brooke and Dr Tertius" +
            " Lydgate and includes a host of characters who illuminate the condition of English " +
            "life in the mid 19th century. This is an analysis of the life of an English provincial" +
            " town during the time of social unrest prior to the first Reform Bill of 1832. It is " +
            "told through the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host" +
            " of characters who illuminate the condition of English life in the mid 19th century." +
            " This is an analysis of the life of an English provincial town during the time of " +
            "social unrest prior to the first Reform Bill of 1832. It is told through the lives of" +
            " Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters who " +
            "illuminate the condition of English life in the mid 19th century. This is an analysis" +
            " of the life of an English provincial town during the time of social unrest prior to" +
            " the first Reform Bill of 1832. It is told through the lives of Dorothea Brooke and " +
            "Dr Tertius Lydgate and includes a host of characters who illuminate the condition of" +
            " English life in the mid 19th century. This is an analysis of the life of an English" +
            " provincial town during the time of social unrest prior to the first Reform Bill of " +
            "1832. It is told through the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters who illuminate the condition of English life in the mid 19th century. This is an analysis of the life of an English provincial town during the time of social unrest prior to the first Reform Bill of 1832. It is told through the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters who illuminate the condition of English life in the mid 19th century. This is an analysis of the life of an English provincial town during the time of social unrest prior to the first Reform Bill of 1832. It is told through the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters who illuminate the condition of English life in the mid 19th century. This is an analysis of the life of an English provincial town during the time of social unrest prior to the first Reform Bill of 1832. It is told through the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters who illuminate the condition of English life in the mid 19th century. This is an analysis of the life of an English provincial town during the time of social unrest prior to the first Reform Bill of 1832. It is told through the lives of Dorothea Brooke and Dr Tertius Lydgate and includes a host of characters who illuminate the condition of English life in the mid 19th century.",
    pageCount = 740,
    downloadUrl = "https://www.gutenberg.org/ebooks/145.txt.utf-8",
    fileExtension = ".epub",
    imageUrl = "https://www.gutenberg.org/cache/epub/145/pg145.cover.medium.jpg",
    downloadCount = 130471,
    languages = "English",
    authors = "Eliot, George",
    downloadId = null,
    fileUri = null
)


val DEFAULT_BOOK_LIST = listOf(
    Book(
        id = 0, title = "Moby Dick", authors = listOf("Herman Meville"), imageUrl = ""
    ), Book(
        id = 0,
        title = "Authority",
        authors = listOf("Jeff Vandermer"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 1,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 2,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 3,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 4,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 4,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 5,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 6,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 7,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 8,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    ), Book(
        id = 9,
        title = "The Adventures Of A dying Man",
        authors = listOf("William Shakespeare"),
        imageUrl = "https://www.gutenberg.org/cache/epub/2641/pg2641.cover.medium.jpg"
    )
)


val DEFAULT_CATEGORIES = mapOf(
    "All" to "",
    "Adventure" to "Adventure",
    "Biography" to "Biography",
    "Children" to "Children",
    "Detective" to "Detective",
    "Drama" to "Drama",
    "Fantasy" to "Fantasy",
    "Fiction" to "Fiction",
    "History" to "History",
    "Horror" to "Horror",
    "Humor" to "Humor",
    "Literature" to "Literature",
    "Mystery" to "Mystery",
    "Romance" to "Romance",
    "Science" to "Science",
    "Thriller" to "Thriller",
    "Poetry" to "Poetry",
)