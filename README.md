# Những thứ cần cài khi dùng eclipse: lombox: https://projectlombok.org/p2

![image](https://github.com/LeDonChung/SetupProjectRMI/assets/105205800/30577df0-f6e9-4e34-850c-fe4b6443fb0a)

# C1: Import project
- B1: Clone Project: git clone https://github.com/LeDonChung/SetupProjectSocket.git và chọn workspace là SetupProjectSocket
- B2: Update và chạy Project Server
- B3: Import một vài dữ liệu trong MariaDB hoặc MSSQL
- B4: Update và chạy Project Client - Nếu lỗi thì export Server
- B5: Sau khi chạy thành công thì có thể đổi tên project lại và xóa các file không cần thiết để code lại như: entities, dao,...
# C2: SetupProjectSocket từ đẩu
# B1: Chuẩn bị CSDL(MSSQL): Xóa TLSv1. Nếu MariaDB thì không cần
 ![image](https://github.com/LeDonChung/SetupProjectRMI/assets/105205800/b6c63379-1485-443e-a7d0-a931cd7612e7)

# B2: Tạo Project Server
-	Tạo với groupid: com.xxx.server
-	Tạo pakege: com.xxx.server / dao, impl / entities
-	Tạo class Server để sau này dùng
-	Import POM, Import META-INF trong src/main/java
-	Update Maven Project
# B3: Kiểm tra Tạo entity
-	 Tạo entity Person: ID, Name
-	Chỉnh sửa <property name="hibernate.hbm2ddl.auto" value="create-drop" />
-	Kết nối MSSQL
-	Hoàn thành kiểm tra
# B4: Chạy Project Server: 
-	Start Server với port: 23861
# B5: Tạo Project Client
-	Export Server: entity, utils
-	Tạo Client.java để call hàm các hàm

  Server
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

  Client
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


# Cách chọn test để chạy
![image](https://github.com/LeDonChung/SetupProjectRMI/assets/105205800/9910de65-503f-462b-9155-001179e9d8e8)
![image](https://github.com/LeDonChung/SetupProjectRMI/assets/105205800/83031b40-54a5-451b-96b8-6d50845805df)

