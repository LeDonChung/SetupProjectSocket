package com.setup.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.setup.server.entities.Gender;
import com.setup.server.entities.Person;
import com.setup.server.utils.RequestDTO;
import com.setup.server.utils.ResponseDTO;

public class Client {
	private static final int PORT = 23861;
	private static final String HOST = "localhost";

	public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

		try (var socket = new Socket(HOST, PORT);
				var scanner = new Scanner(System.in);
				var oos = new ObjectOutputStream(socket.getOutputStream());
				var ois = new ObjectInputStream(socket.getInputStream())) {
			int choice = 0;
			System.out.println("Connected to server");
			RequestDTO request = null;

			while (true) {
				System.out.println("MENU");
				System.out.println("1. Find all person");
				System.out.println("2. Find person by Gender");
				System.out.println("3. Find person by id");
				System.out.println("4. Find by Gender and Manager");
				System.out.println("Choose an option: ");
				choice = scanner.nextInt();

				switch (choice) {
				case 1: {
					// send request to server
					request = new RequestDTO();
					request.setRequest("Person.findAll");
					oos.writeObject(request);
					oos.flush();

					// receive response from server
					ResponseDTO response = (ResponseDTO) ois.readObject();
					System.out.println("Server response: " + response.getData());

					System.out.println("List of person: ");
					List<Person> persons = (List<Person>) response.getData();
					persons.forEach(System.out::println);
					break;
				}

				case 2: {
					// send request to server
					request = new RequestDTO();
					request.setRequest("Person.findByGender");
					request.setData(Gender.FEMALE);
					oos.writeObject(request);
					oos.flush();

					// receive response from server
					ResponseDTO response = (ResponseDTO) ois.readObject();
					System.out.println("Server response: " + response.getData());

					System.out.println("List of person: ");
					List<Person> persons = (List<Person>) response.getData();
					persons.forEach(System.out::println);
					break;
				}
				case 3: {
					scanner.nextLine(); // consume newline
					System.out.println("Enter person id: ");
					int id = scanner.nextInt();
					
					// send request to server
					request = new RequestDTO();
					request.setRequest("Person.findById");
					request.setData(id);
					oos.writeObject(request);
					oos.flush();

					// receive response from server
					ResponseDTO response = (ResponseDTO) ois.readObject();
					System.out.println("Server response: " + response.getData());

					Person person = (Person) response.getData();
					System.out.println("Person: " + person);
					break;
				}
				case 4: {
					scanner.nextLine(); // consume newline
					
					
					// C1: Truyền param dưới dạng Map
//					Map<String, Object> data = new HashMap<>();
//					data.put("gender", Gender.FEMALE);
//					data.put("manager", 1);
//					request.setData(data);
					
					// C2: Truyền param dưới dạng Object
					Person person = new Person();
					person.setGender(Gender.FEMALE);
					
					Person manager = new Person();
					manager.setId(1);
					person.setManager(manager);
					
					// send request to server
					request = new RequestDTO();
					request.setRequest("Person.findByGenderAndManager");
					request.setData(person);
					oos.writeObject(request);
					oos.flush();

					// receive response from server
					ResponseDTO response = (ResponseDTO) ois.readObject();
					System.out.println("Server response: " + response.getData());

					System.out.println("List of person: ");
					List<Person> persons = (List<Person>) response.getData();
					persons.forEach(System.out::println);
					break;
				}

				}
			}
		}
	}
}
