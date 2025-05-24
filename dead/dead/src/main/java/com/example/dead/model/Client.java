package com.example.dead.model;//package com.example.dead.back.model;
//
//import com.example.dead.back.controller.DataBaseController;
//import com.example.dead.back.controller.FileManager;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//@Getter
//@Setter
//public class Client implements Runnable {
//    public static Set<Client> clientSet = new HashSet<>();
//    public static int counter = 1;
//    private int id;
//    private Socket socket;
//    private String name;
//    private ObjectOutputStream outputStream;
//    private ObjectInputStream inputStream;
//    private Client pvClient;
//
//    public Client(Socket socket) throws IOException {
//        this.socket = socket;
//        this.id = counter++;
//        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
//        this.inputStream = new ObjectInputStream(socket.getInputStream());
//        clientSet.add(this);
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (!socket.isClosed()) {
//                try {
//                    Object inpObject = inputStream.readObject();
//                    System.out.println("Received: " + inpObject);
//
//                    List<Object> write = ServerManager.manage(inpObject);
//                    outputStream.writeObject(write);
//                    outputStream.flush();
//                    System.out.println("Sent" + write);
//
//                } catch (IOException | ClassNotFoundException e) {
//                    System.err.println("Connection lost with client #" + id);
//                    break;
//                }
//            }
//        } finally {
//            closeConnection();
//        }
//    }
//
//    private void closeConnection() {
//        try {
//            clientSet.remove(this);
//            if (inputStream != null) inputStream.close();
//            if (outputStream != null) outputStream.close();
//            if (socket != null && !socket.isClosed()) socket.close();
//            System.out.println("Client #" + id + " disconnected.");
//        } catch (IOException e) {
//            System.err.println("Error while closing client #" + id + ": " + e.getMessage());
//        }
//    }
//
//    public static void shutdownServer(){
//        System.out.println("Shutting down the server...");
//
//        for (Client client : Client.clientSet) {
//            client.closeConnection();
//        }
//        Client.clientSet.clear();
//        System.out.println("All clients have been disconnected.");
//
//        FileManager.getInstance().writeUsersToFile(DataBaseController.getInstance().getUsers());
//        System.out.println("Database information has been saved to the file.");
//
//        System.out.println("Server is shutting down...");
//
//        System.exit(0);
//    }
//}
