package iustudy.webdev.ausleihproject.service;

import iustudy.webdev.ausleihproject.data.Booking;
import iustudy.webdev.ausleihproject.data.BookingRepository;
import iustudy.webdev.ausleihproject.data.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // find
    public List<Booking> findAllBookings() { return bookingRepository.findAll(); }

    public Booking findBooking(long id) { return bookingRepository.findById(id).orElse(null); }

    public List<Booking> findBookingsByDevice(Device device) { return bookingRepository.findByDevice(device.getId()); }

    public List<Booking> findBookingsByUser(String name) { return bookingRepository.findByUser(name); }

    //save
    public void saveBooking(Booking booking) {
        if (booking == null) {
            System.err.println("Booking is null!");
            return;
        }
        bookingRepository.save(booking);
    }

    // delete
    public void deleteBooking(long id) { bookingRepository.deleteById(id); }
}
