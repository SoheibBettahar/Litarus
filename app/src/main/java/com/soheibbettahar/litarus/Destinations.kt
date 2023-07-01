package com.soheibbettahar.litarus

sealed class Destination(val route: String){
    object BooksList : Destination("BooksList")
    object BookDetail : Destination("BooksDetail")
}
