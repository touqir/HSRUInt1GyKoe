package domain;

import com.google.common.collect.ComparisonChain;

public class Book implements Comparable<Book> {

    private String title, author, publisher;
    private Shelf shelf;

    public Book(String name) {
        this.title = name;
    }

    public Book() {
    }

    public String getName() {
        return title;
    }

    public void setName(String name) {
        this.title = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String autor) {
        this.author = autor;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    @Override
    public String toString() {
        return title; // + ", " + author + ", " + publisher;
    }

    public void updateFrom(Book updatedBook) {
        title = updatedBook.title;
        author = updatedBook.author;
        publisher = updatedBook.publisher;
        shelf = updatedBook.shelf;
    }

    @Override
    public int compareTo(Book o) {
        // this is ugly as hell but it's the best solution without a unique id on the book
        return ComparisonChain.start().compare(System.identityHashCode(this), System.identityHashCode(o)).result();
    }
}
