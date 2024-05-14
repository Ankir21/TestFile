package com.example.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Scanner;

public class MenuProcessor {


    private static final String MENU_FILE_PATH = "src/main/resources/menu.json";



    private static ClientData clientData;
    private static JsonNode menuData;
    private static JsonNode currentMenu;
    private static JsonNode navigationData;
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            loadMenuData();
            clientData = new ClientData("ИмяКлиента1", 200, 1335885824);
            clientData.setInternetTrafficFormatted(formatInternetTraffic(clientData.getInternetTraffic()));
            clientData.setTimeOfDay(getTimeOfDay());

            currentMenu = menuData.get("menu").get("Главное меню");

            Scanner scanner = new Scanner(System.in);
            while (true) {
                displayCurrentMenu();
                String input = scanner.nextLine();

                if (input.equals(navigationData.get("main_menu").asText())) {
                    currentMenu = menuData.get("menu").get("Главное меню");
                } else if (input.equals(navigationData.get("back").asText())) {
                    currentMenu = menuData.get("menu").get("Главное меню"); // Adjust to previous menu if needed
                } else if (input.equals(navigationData.get("repeat").asText())) {
                    continue;
                } else if (currentMenu.has("options") && currentMenu.get("options").has(input)) {
                    currentMenu = menuData.get("menu").get(currentMenu.get("options").get(input).asText());
                } else {
                    System.out.println("Неверная команда, попробуйте еще раз.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void loadMenuData() throws IOException {
        menuData = mapper.readTree(new File(MENU_FILE_PATH));
        navigationData = menuData.get("navigation");
    }

    private static void displayCurrentMenu() {
        String message = getMessageForCurrentMenu();
        System.out.println(message);
    }

    private static String getMessageForCurrentMenu() {
        JsonNode messageNode = currentMenu.get("message");
        if (messageNode.isArray()) {
            Iterator<JsonNode> elements = messageNode.elements();
            while (elements.hasNext()) {
                JsonNode element = elements.next();
                if (element.get("condition").asText().equals("else") || evalCondition(element.get("condition").asText())) {
                    return formatMessage(element.get("text").asText());
                }
            }
        } else {
            return formatMessage(messageNode.asText());
        }
        return "";
    }

    private static boolean evalCondition(String condition) {
        if (condition.contains("balance")) {
            int threshold = menuData.get("conditions").get("balance_threshold").asInt();
            if (condition.contains("<")) {
                return clientData.getBalance() < threshold;
            } else if (condition.contains(">")) {
                return clientData.getBalance() > threshold;
            } else if (condition.contains("=")) {
                return clientData.getBalance() == threshold;
            }
        }
        return false;
    }

    private static String formatMessage(String message) {
        return message.replace("{name}", clientData.getName())
                .replace("{balance}", String.valueOf(clientData.getBalance()))
                .replace("{internet_traffic}", clientData.getInternetTrafficFormatted())
                .replace("{time_of_day}", clientData.getTimeOfDay());
    }

    private static String formatInternetTraffic(long bytes) {
        long gb = bytes / (1024 * 1024 * 1024);
        long mb = (bytes % (1024 * 1024 * 1024)) / (1024 * 1024);
        return gb + " ГБ " + mb + " МБ";
    }

    private static String getTimeOfDay() {
        int hour = LocalTime.now().getHour();
        if (hour >= 5 && hour < 12) {
            return "утро";
        } else if (hour >= 12 && hour < 17) {
            return "день";
        } else if (hour >= 17 && hour < 21) {
            return "вечер";
        } else {
            return "ночь";
        }
    }
}
