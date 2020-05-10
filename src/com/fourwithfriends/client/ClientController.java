package com.fourwithfriends.client;

public class ClientController {

   private ClientControllerDelegate controller;

   public static void main(String[] args) {
       new ClientController().run();
   }

   private ClientController() {
       IClientModel model = new ClientModel();
       IClientView view = new ClientView();
       controller = new ClientControllerDelegate();
       controller.setModel(model);
       controller.setView(view);
       view.setModel(model);
       view.setController(controller);
   }

   private void run() {
       controller.run();
   }
}
