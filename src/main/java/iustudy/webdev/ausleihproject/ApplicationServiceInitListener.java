package iustudy.webdev.ausleihproject;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import iustudy.webdev.ausleihproject.data.Booking;
import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.data.DeviceStatus;
import iustudy.webdev.ausleihproject.service.BookingService;
import iustudy.webdev.ausleihproject.service.DeviceService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ApplicationServiceInitListener implements VaadinServiceInitListener {

	private final DeviceService deviceService;
	private final BookingService bookingService;

	public ApplicationServiceInitListener(DeviceService deviceService, BookingService bookingService) {
		this.deviceService = deviceService;
		this.bookingService = bookingService;
	}

	@Override
	public void serviceInit(ServiceInitEvent serviceInitEvent) {

		initData();

	}

	private void initData() {

		String[][] devices = {
				{"Laptop", "Lenovo ThinkPad X1 2-in-1 Gen 9 (14' Intel)", "AVAILABLE"},
				{"Laptop", "Lenovo Legion 7i Gen 9 (16' Intel)", "AVAILABLE"},
				{"Phone", "iPhone 13 Pro Max 256GB graphite", "AVAILABLE"},
				{"Tablet", "iPad Pro 12.9-inch (5th generation)", "AVAILABLE"},
				{"Laptop", "Dell XPS 13 (9310)", "AVAILABLE"},
				{"Phone", "Samsung Galaxy S21 Ultra", "AVAILABLE"},
				{"Laptop", "HP Spectre x360 14", "AVAILABLE"},
				{"Tablet", "Samsung Galaxy Tab S7+", "AVAILABLE"},
				{"Phone", "Google Pixel 6 Pro", "AVAILABLE"},
				{"Laptop", "Apple MacBook Pro 16-inch (2021)", "RENTED"},
				{"Phone", "OnePlus 9 Pro", "MISSING"},
				{"Tablet", "Microsoft Surface Pro 7", "AVAILABLE"},
				{"Laptop", "Asus ROG Zephyrus G14", "AVAILABLE"},
				{"Phone", "Sony Xperia 1 III", "AVAILABLE"},
				{"Tablet", "Amazon Fire HD 10", "AVAILABLE"},
				{"Laptop", "Acer Swift 3", "AVAILABLE"},
				{"Phone", "Xiaomi Mi 11 Ultra", "AVAILABLE"},
				{"Tablet", "Lenovo Tab P11 Pro", "AVAILABLE"},
				{"Laptop", "Razer Blade 15", "AVAILABLE"},
				{"Phone", "Oppo Find X3 Pro", "AVAILABLE"}
		};

		String[][] bookings = {
				{"0", "John Doe", "2022-12-25", "2022-12-31", "7"},
				{"0", "John Doe", "2023-04-25", "2023-05-01", "7"},
				{"2", "Sam Smith", "2023-04-25", "2023-05-30", "90"},
				{"1", "Lora Johansen", "2023-05-14", "2023-05-30", "90"}
		};

		Device[] deviceObjects = new Device[devices.length];

		for (int i = 0; i < devices.length; i++) {
			DeviceStatus status = DeviceStatus.valueOf(devices[i][2]);
			deviceObjects[i] = new Device(devices[i][0], devices[i][1], status);
			deviceService.saveDevice(deviceObjects[i]);
		}

		for (String[] bookingData : bookings) {
			Device device = deviceObjects[Integer.parseInt(bookingData[0])];
			LocalDate borrowDate = LocalDate.parse(bookingData[2]);
			LocalDate returnDate = LocalDate.parse(bookingData[3]);
			int maxDays = Integer.parseInt(bookingData[4]);
			Booking booking = new Booking(device, bookingData[1], borrowDate, returnDate, maxDays);
			bookingService.saveBooking(booking);
		}

		System.out.println("____________Initialise database data_____________");
	}

}
