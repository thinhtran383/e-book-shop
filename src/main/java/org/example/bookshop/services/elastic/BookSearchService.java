package org.example.bookshop.services.elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import org.example.bookshop.domain.elasticsearch.BookDocument;
import org.example.bookshop.repositories.elasticsearch.IBookDocumentRepository;
import org.example.bookshop.responses.PageableResponse;
import org.example.bookshop.responses.book.BookResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookSearchService {
    private final IBookDocumentRepository bookDocumentRepository;
    private final ElasticsearchClient elasticsearchClient;
    private final ModelMapper modelMapper;

    public PageableResponse<BookResponse> filterBooks(String title, int page, int limit) {
        Page<BookDocument> bookDocs = bookDocumentRepository.findByTitleContainingIgnoreCase(title, PageRequest.of(0, 10));

        List<BookResponse> elements = bookDocs.getContent().stream()
                .map(bookDoc -> modelMapper.map(bookDoc, BookResponse.class))
                .toList();


        return PageableResponse.<BookResponse>builder()
                .totalPages(bookDocs.getTotalPages())
                .elements(elements)
                .totalElements(bookDocs.getTotalElements())
                .build();
    }


}
