package iustudy.webdev.ausleihproject.service;

import iustudy.webdev.ausleihproject.data.BookingRepository;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    final BookingRepository bookingRepository;
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
}
