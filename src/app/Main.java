package app;

import entities.*;
import services.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Inicializando o sistema do restaurante...");

        Scanner scanner = new Scanner(System.in);
        Stock stock = new Stock();

        Ingredient tomate = new Ingredient("I01", "Tomate", "unidades");
        Ingredient queijo = new Ingredient("I02", "Queijo Mussarela", "gramas");
        Ingredient massa = new Ingredient("I03", "Massa de Pizza", "unidades");
        Ingredient carne = new Ingredient("I04", "Carne Moída", "gramas");
        stock.add(tomate, 50.0);
        stock.add(queijo, 2000.0);
        stock.add(massa, 30.0);
        stock.add(carne, 5000.0);

        // Criando o cardápio (Menu)
        Dish pizza = new Dish("D01", "Pizza de Queijo", 45.0);
        pizza.addIngredientToRecipe(massa, 1.0);
        pizza.addIngredientToRecipe(queijo, 150.0);
        pizza.addIngredientToRecipe(tomate, 2.0);

        Dish lasanha = new Dish("D02", "Lasanha Bolonhesa", 55.0);
        lasanha.addIngredientToRecipe(carne, 300.0);
        lasanha.addIngredientToRecipe(queijo, 200.0);

        // Criando o Restaurante com seu estoque e cardápio
        Restaurant restaurant = new Restaurant("COMER BEM", stock);
        restaurant.addDishToMenu(pizza);
        restaurant.addDishToMenu(lasanha);

        // Criando uma lista de clientes pré-cadastrados
        List<Client> clients = new ArrayList<>();
        clients.add(new Client("C01", "Jhon Victor"));
        clients.add(new Client("C02", "Clara"));
        clients.add(new Client("C03", "Malu"));

        while (true) {
            System.out.println("\n-===[ BEM-VINDO AO " + restaurant.getName().toUpperCase() + " ]===-");
            System.out.println("Selecione seu perfil:");
            System.out.println("1. Sou um Cliente");
            System.out.println("2. Sou o Gerente");
            System.out.println("0. Sair do Sistema");
            System.out.print("Escolha uma opção: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
                System.out.println("Encerrando o sistema. Até logo!");
                break; // Quebra o loop 'while' e encerra o programa
            }

            switch (choice) {
                case 1:
                    handleUserMenu(scanner, restaurant, clients);
                    break;
                case 2:
                    handleManagerMenu(scanner, restaurant);
                    break;
                default:
                    System.out.println("Opção inválida! Por favor, tente novamente.");
            }
        }
        scanner.close();
    }

    // Menu para o fluxo do Cliente
    public static void handleUserMenu(Scanner scanner, Restaurant restaurant, List<Client> clients) {
        System.out.println("\n-===[ ÁREA DO CLIENTE ]===-");

        System.out.print("Por favor, digite seu nome: ");
        String clientName = scanner.nextLine();

        Client currentClient = null;
        for (Client client : clients) {
            if (client.getName().equalsIgnoreCase(clientName)) {
                currentClient = client; // Encontramos!
                break;
            }
        }

        // Verifique se encontramos o cliente ou se ele é novo.
        if (currentClient == null) {
            String newId = "C" + (clients.size() + 1);
            currentClient = new Client(newId, clientName);
            clients.add(currentClient);
            System.out.println("Bem-vindo(a), " + currentClient.getName() + "! Vimos que você é um novo cliente em nosso sistema.");
        } else {
            // Se encontramos, apenas damos uma mensagem de boas-vindas.
            System.out.println("Bem-vindo(a) de volta, " + currentClient.getName() + "!");
        }

        while (true) {
            System.out.println("\nO que você gostaria de fazer?");
            System.out.println("1. Fazer um pedido");
            System.out.println("2. Avaliar o restaurante");
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) break;

            if (choice == 1) {
                System.out.println("\nNOSSO CARDÁPIO");
                List<Dish> menu = restaurant.getMenu();
                for (int i = 0; i < menu.size(); i++) {
                    System.out.printf("%d. %s - R$%.2f\n", (i+1), menu.get(i).getName(), menu.get(i).getPrice());
                }

                Order order = new Order(currentClient);

                while(true) {
                    System.out.print("Digite o número do prato que deseja adicionar (ou 0 para finalizar o pedido): ");
                    int dishChoice = scanner.nextInt() -1;
                    scanner.nextLine();

                    if (dishChoice == -1) break;

                    if (dishChoice >= 0 && dishChoice < menu.size()) {
                        order.addDish(menu.get(dishChoice));
                        System.out.println("'" + menu.get(dishChoice).getName() + "' adicionado ao pedido.");
                    } else {
                        System.out.println("Prato inválido!");
                    }
                }
                restaurant.placeOrder(order);

            } else if (choice == 2) {
                // ... (A lógica de avaliar continua a mesma) ...
                System.out.print("Dê uma nota de 1 a 5: ");
                int rating = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Deixe um comentário (opcional): ");
                String comment = scanner.nextLine();

                Evaluation eval = new Evaluation(currentClient, rating, comment);
                restaurant.addEvaluation(eval);
                System.out.println("Obrigado pela sua avaliação!");
            }
        }
    }

    // Menu para o fluxo do Gerente
    public static void handleManagerMenu(Scanner scanner, Restaurant restaurant) {
        while (true) {
            System.out.println("\n-===[ PAINEL DO GERENTE ]===-");
            System.out.println("Selecione o relatório que deseja visualizar:");
            System.out.println("1. Gasto médio por pedido (Ticket Médio)");
            System.out.println("2. Prato mais pedido");
            // Adicionar aqui as perguntas para ver os insights!
            System.out.println("0. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) break;

            switch (choice) {
                case 1:
                    System.out.printf("O gasto médio por pedido é de R$%.2f\n", restaurant.getAverageSpendingPerOrder());
                    break;
                case 2:
                    Dish popularDish = restaurant.getMostPopularDish();
                    if (popularDish != null) {
                        System.out.println("O prato mais popular é: " + popularDish.getName());
                    } else {
                        System.out.println("Ainda não há pedidos suficientes para determinar.");
                    }
                    break;
                default:
                    System.out.println("Relatório inválido!");
            }
        }
    }
}