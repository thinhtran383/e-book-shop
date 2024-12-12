package org.example.bookshop.services.elastic;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.domain.elasticsearch.BookDocument;
import org.example.bookshop.repositories.database.IBookRepository;
import org.example.bookshop.repositories.elasticsearch.IBookDocumentRepository;
import org.example.bookshop.responses.book.BookResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticSearchSync {
    private final IBookRepository bookRepository;
    private final IBookDocumentRepository bookDocumentRepository;
    private final ModelMapper modelMapper;

    @Scheduled(fixedRate = 60000)
    public void syncBookData(){
        Page<BookResponse> bookResponsePage = bookRepository.findAllBook(PageRequest.of(0, 1000));

        bookDocumentRepository.saveAll(bookResponsePage.getContent().stream()
                .map(bookResponse -> modelMapper.map(bookResponse, BookDocument.class))
                .collect(Collectors.toList()));
    }


}
