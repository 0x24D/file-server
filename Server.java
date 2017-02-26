import java.io.*;
import java.net.*;
import java.util.*;

public class Server
{
	public static void main(String[] args) throws IOException
	{
		ServerSocket serverSocket = null;
		final int PORT = 7500;
		Socket client;
		ClientHandler handler;

		try
		{
			serverSocket = new ServerSocket(PORT);
		}
		catch (IOException ioEx)
		{
			System.out.println("Unable to set up port!");
			System.exit(1);
		}

		System.out.println("Server running...");

		do
		{
			client = serverSocket.accept();
			System.out.println("New client accepted.");
			handler = new ClientHandler(client);
			handler.start();
		} while (true);
	}
}

class ClientHandler extends Thread
{
	private Socket client;
	private Scanner input;
	private PrintWriter output;

	public ClientHandler(Socket socket) throws IOException
	{
		client = socket;
		input = new Scanner(client.getInputStream());
		output = new PrintWriter(client.getOutputStream(), true);
	}

	public void run()
	{
		String filePath = input.nextLine();
		filePath = (filePath.substring(4));
		//have to substring first as there is a space before requested file path
		filePath = filePath.substring(1, filePath.indexOf(" "));
		//or substring(0,...) and remove trailing / from file path
		System.out.println("Requested file: " + filePath);
		output.println("/" + filePath);



		try
		{
			System.out.println("Closing down connection...");
			client.close();
			input.close();
			output.close();
		}
		catch(IOException ioEx)
		{
			System.out.println("* Disconnection problem! *");
		}
	}
}
