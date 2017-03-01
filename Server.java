import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.Files;

public class Server{

	public static void main(String[] args) throws IOException{

		ServerSocket serverSocket = null;
		final int PORT = 7500;
		Socket client;
		ClientHandler handler;

		try{
			serverSocket = new ServerSocket(PORT);
		}
		catch (IOException e){
			System.out.println("Unable to set up port!");
			System.exit(1);
		}

		System.out.println("Server running...");

		do{
			client = serverSocket.accept();
			System.out.println("New client accepted.");
			handler = new ClientHandler(client);
			handler.start();
		} while (true);
	}
}

class ClientHandler extends Thread{

	private Socket client;
	private Scanner input;
	private PrintWriter output;

	public ClientHandler(Socket socket) throws IOException{
		client = socket;
		input = new Scanner(client.getInputStream());
		output = new PrintWriter(client.getOutputStream(), true);
	}

	public void run(){
		String filePath = input.nextLine();
		filePath = (filePath.substring(4));
		//have to substring first as there is a space before requested file path
		filePath = filePath.substring(1, filePath.indexOf(" "));
		//or substring(0,...) and remove trailing / from TEST
		File file = new File ("TEST/" + filePath);
		try{
			input =  new Scanner(new BufferedReader(new FileReader(file)));
			output.print("HTTP/1.0 200 OK\nContent-Type: " + Files.probeContentType(file.toPath())+"\n\n");
			while (input.hasNext())
				output.println(input.nextLine());
		}
		catch (FileNotFoundException e){
			output.println("HTTP/1.0 404 Not Found");
			System.out.println(e);
		}
		catch (IOException e){
			System.out.println(e);
		}

		try{
			System.out.println("Closing down connection...");
			input.close();
			output.close();
			client.close();
		}
		catch(IOException e){
			System.out.println("* Disconnection problem! *");
		}
	}
}
