package iustudy.webdev.ausleihproject;

import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import iustudy.webdev.ausleihproject.data.Booking;
import iustudy.webdev.ausleihproject.data.Device;
import iustudy.webdev.ausleihproject.service.MainService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ApplicationServiceInitListener implements VaadinServiceInitListener {

	private final MainService service;

	public ApplicationServiceInitListener(MainService service) {
		this.service = service;
	}

	@Override
	public void serviceInit(ServiceInitEvent serviceInitEvent) {

		initData();

	}

	private void initData() {

		String[][] devices = {
				{"Laptop", "Lenovo ThinkPad X1 2-in-1 Gen 9 (14' Intel)", "120"},
				{"Laptop", "Lenovo Legion 7i Gen 9 (16' Intel)", "120"},
				{"Phone", "iPhone 13 Pro Max 256GB graphite", "30"},
				{"Tablet", "iPad Pro 12.9-inch (5th generation)", "30"}
		};

		String[][] bookings = {
				{"0", "John Doe", "2022-12-25", "2022-12-31"},
				{"0", "John Doe", "2023-04-25", ""},
				{"2", "Sam Smith", "2023-04-25", "2023-05-30"},
				{"1", "Lora Johansen", "2023-05-14", "2023-05-30"},
				{"1", "Lora Johansen", "2024-02-14", "2024-03-14"},
				{"1", "Conor McGregor", "2024-11-07", ""}
		};

		Device[] deviceObjects = new Device[devices.length];

		for (int i = 0; i < devices.length; i++) {
			int maxDays = Integer.parseInt(devices[i][2]);
			deviceObjects[i] = new Device(devices[i][0], devices[i][1], maxDays);
			service.saveDevice(deviceObjects[i]);
		}

		for (String[] bookingData : bookings) {
			Device device = deviceObjects[Integer.parseInt(bookingData[0])];
			LocalDate borrowDate = LocalDate.parse(bookingData[2]);
			LocalDate returnDate = !bookingData[3].isEmpty() ? LocalDate.parse(bookingData[3]) : null;
			Booking booking = new Booking(device, bookingData[1], borrowDate, returnDate);
			service.saveBooking(booking);
		}

		System.out.println("____________Initialise database data_____________");
	}

}
