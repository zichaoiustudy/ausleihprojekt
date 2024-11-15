package iustudy.webdev.ausleihproject.service;

import iustudy.webdev.ausleihproject.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MainService {

    final DeviceRepository deviceRepository;
    final BookingRepository bookingRepository;

    @Autowired
    public MainService(DeviceRepository deviceRepository, BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
        this.deviceRepository = deviceRepository;
    }

    // device methods
    // find & search
    public List<Device> findAllDevices() { return deviceRepository.findAll(); }

    public Device findDevice(long id) { return deviceRepository.findById(id).orElse(null); }

    public List<Device> searchDevices(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank())
            return findAllDevices();
        else return deviceRepository.search(searchTerm);
    }

    // save

    public void saveDevice(Device device) {
        if (device == null) {
            System.err.println("Device is null!");
            return;
        }
        device.setStatus(DeviceStatus.AVAILABLE);
        deviceRepository.save(device);
    }

    public void updateDevice(Device device) {
        if (device == null) {
            System.err.println("Device is null!");
            return;
        }

        Booking lastBooking = findLastBooking(device);
        if (lastBooking != null && lastBooking.getReturnDate() == null) {
            if (lastBooking.getBorrowDate().plusDays(device.getMaxDays()).isAfter(LocalDate.now())
                    || device.getMaxDays() == 0) {
                device.setStatus(DeviceStatus.RENTED);
            } else {
                device.setStatus(DeviceStatus.MISSING);
            }
        } else {
            device.setStatus(DeviceStatus.AVAILABLE);
        }
        deviceRepository.save(device);
    }

    // delete
    public void deleteDevice(Device device) { deviceRepository.delete(device); }

    // booking methods
    // find
    public List<Booking> findAllBookings() { return bookingRepository.findAll(); }

    public List<Booking> findBookingsByDevice(Device device) { return bookingRepository.findByDevice(device.getId()); }

    public Booking findLastBooking(Device device) {
        if (findBookingsByDevice(device).isEmpty()) {
            return null;
        }
        return findBookingsByDevice(device).get(findBookingsByDevice(device).size() - 1);
    }

    public List<Booking> searchBookings(String searchTerm) {
        if (searchTerm == null || searchTerm.isBlank())
            return findAllBookings();
        else return bookingRepository.search(searchTerm);
    }

    //save
    public void saveBooking(Booking booking) {
        if (booking == null) {
            System.err.println("Booking is null!");
            return;
        }

        bookingRepository.save(booking);
        updateDevice(booking.getDevice());
    }

    // delete
    public void deleteBooking(Booking booking) { bookingRepository.delete(booking); }
}
