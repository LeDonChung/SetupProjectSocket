package com.setup.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.setup.server.dao.PersonDAO;
import com.setup.server.dao.impl.PersonDAOImpl;
import com.setup.server.entities.Gender;
import com.setup.server.entities.Person;
import com.setup.server.utils.AppUtils;
import com.setup.server.utils.ResponseDTO;
import com.setup.server.utils.RequestDTO;

public class Server {
	public static void main(String[] args) {
		try (var server = new ServerSocket(AppUtils.PORT)) {
			System.out.println("Server is running on port " + AppUtils.PORT);
			while (true) {
				var client = server.accept();
				System.out.println("Client connected: " + client.getInetAddress().getHostAddress());

				Server serverTemp = new Server();

				new Thread(serverTemp.new HandlerClient(client)).start();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class HandlerClient implements Runnable {
		private Socket client;
		private PersonDAO personDAO;

		public HandlerClient(Socket client) {
			this.client = client;
			this.personDAO = new PersonDAOImpl();
		}

		@Override
		public void run() {
			try (var ois = new ObjectInputStream(client.getInputStream());
					var oos = new ObjectOutputStream(client.getOutputStream())) {
				while (true) {
					RequestDTO request = (RequestDTO) ois.readObject();
					System.out.println("Request received: " + request.getRequest());

					switch (request.getRequest()) {
					case "Person.findAll": {
						List<Person> persons = personDAO.findAll();
						ResponseDTO response = new ResponseDTO("success", persons);

						oos.writeObject(response);
						oos.flush();
						break;
					}
					case "Person.findById": {
						int id = (int) request.getData();
						Person person = personDAO.findById(id);
						
						String res = person != null ? "success" : "error";
						ResponseDTO response = new ResponseDTO(res, person);
						oos.writeObject(response);
						oos.flush();
						break;
					}
					
					case "Person.findByGender": {
						Gender gender = (Gender) request.getData();
						List<Person> persons = personDAO.findByGender(gender); 
						
						ResponseDTO response = new ResponseDTO("success", persons);
						oos.writeObject(response);
						oos.flush();
						break;
					}
					case "Person.findByGenderAndManager": {
						Person person = (Person) request.getData();
						List<Person> persons = personDAO.findByGenderAndManager(person.getGender(), person.getManager().getId());
						
						ResponseDTO response = new ResponseDTO("success", persons);
						oos.writeObject(response);
						oos.flush();
						break;
					}
					default: {
						ResponseDTO response = new ResponseDTO("error", "Invalid request");
						oos.writeObject(response);
						oos.flush();
						break;
					}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
