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
				{"Phone", "iPhone 13 Pro Max 256GB graphite", "AVAILABLE"}
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
