package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.dto.BookDto;
import org.example.bookshop.entities.Book;
import org.example.bookshop.entities.Category;
import org.example.bookshop.repositories.IBookRepository;
import org.example.bookshop.responses.book.BookResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookService {
    private final IBookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        return bookRepository.findAllBook(pageable);
    }

    @Transactional
    public BookResponse createNewBooK(BookDto bookDto) {
        Book newBook = modelMapper.map(bookDto, Book.class);

        newBook.setCategoryID(new Category(bookDto.getCategoryID(), bookDto.getCategoryName()));

        return modelMapper.map(bookRepository.save(newBook), BookResponse.class);
    }

}
