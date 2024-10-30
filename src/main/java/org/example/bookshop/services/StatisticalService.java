package org.example.bookshop.services;

import lombok.RequiredArgsConstructor;
import org.example.bookshop.repositories.ICustomerRepository;
import org.example.bookshop.repositories.IOrderDetailRepository;
import org.example.bookshop.repositories.IOrderRepository;
import org.example.bookshop.responses.book.Top5Response;
import org.example.bookshop.responses.statistical.MonthlyRevenueResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticalService {
    private final IOrderRepository orderRepository;
    private final IOrderDetailRepository orderDetailRepository;
    private final ICustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<Top5Response> getTop5BooksBySales() {
        return orderDetailRepository.findTop5BooksBySales();
    }

    @Transactional(readOnly = true)
    public List<MonthlyRevenueResponse> getRevenueByMonth() {
        List<Object[]> revenueData =  orderRepository.getRevenueByMonth();

        return revenueData.stream().map(data -> {
            Integer month = (Integer) data[0];
            Integer year = (Integer) data[1];
            BigDecimal totalRevenue = (BigDecimal) data[2];

            return new MonthlyRevenueResponse(month, year, totalRevenue);
        }).toList();
    }

    @Transactional(readOnly = true)
    public Long getTotalCustomers() {
        return customerRepository.count();
    }

    @Transactional(readOnly = true)
    public Long getTotalOrders() {
        return orderRepository.count();
    }

    @Transactional(readOnly = true)
    public BigDecimal getTotalRevenue() {
        return orderRepository.getTotalRevenue();
    }


}
