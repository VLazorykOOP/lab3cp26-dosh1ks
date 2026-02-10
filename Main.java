// Розробити програму мовою Java для моделювання роботи кав’ярні:
// Клієнт формує складне замовлення (кава, об’єм, добавки, цукор тощо).
// Система повинна підтримувати оплату через сторонній платіжний сервіс, інтерфейс якого не сумісний з внутрішнім інтерфейсом програми.
 // Користувач може вводити текстові команди, які інтерпретуються системою (наприклад: ADD_SUGAR, ADD_MILK, PRINT_ORDER).
 // Builder
import java.util.Scanner;

class CoffeeOrder {
    private String coffeeType;
    private String size;
    private boolean milk;
    private boolean sugar;

    private CoffeeOrder(Builder builder) {
        this.coffeeType = builder.coffeeType;
        this.size = builder.size;
        this.milk = builder.milk;
        this.sugar = builder.sugar;
    }

    public static class Builder {
        private String coffeeType;
        private String size;
        private boolean milk;
        private boolean sugar;

        public Builder setCoffeeType(String coffeeType) {
            this.coffeeType = coffeeType;
            return this;
        }

        public Builder setSize(String size) {
            this.size = size;
            return this;
        }

        public Builder addMilk() {
            this.milk = true;
            return this;
        }

        public Builder addSugar() {
            this.sugar = true;
            return this;
        }

        public CoffeeOrder build() {
            return new CoffeeOrder(this);
        }
    }

    public void printOrder() {
        System.out.println("\n Замовлення ");
        System.out.println("Кава: " + coffeeType);
        System.out.println("Розмір: " + size);
        System.out.println("Молоко: " + (milk ? "так" : "ні"));
        System.out.println("Цукор: " + (sugar ? "так" : "ні"));
    }
}

//Adapter
class ExternalPaymentService {
    public void makePayment(double amount) {
        System.out.println("Оплата через сторонній сервіс: " + amount + " грн");
    }
}

interface PaymentProcessor {
    void pay(double amount);
}

class PaymentAdapter implements PaymentProcessor {
    private ExternalPaymentService service;

    public PaymentAdapter(ExternalPaymentService service) {
        this.service = service;
    }

    @Override
    public void pay(double amount) {
        service.makePayment(amount);
    }
}

//Interpreter
interface Expression {
    void interpret(CoffeeOrder.Builder builder);
}

class AddMilkExpression implements Expression {
    @Override
    public void interpret(CoffeeOrder.Builder builder) {
        builder.addMilk();
    }
}

class AddSugarExpression implements Expression {
    @Override
    public void interpret(CoffeeOrder.Builder builder) {
        builder.addSugar();
    }
}
public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        CoffeeOrder.Builder builder = new CoffeeOrder.Builder()
                .setCoffeeType("Latte")
                .setSize("Medium");

        System.out.println("Введіть команди:");
        System.out.println("ADD_MILK");
        System.out.println("ADD_SUGAR");
        System.out.println("PRINT_ORDER");

        while (true) {
            System.out.print("Команда: ");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("ADD_MILK")) {
                new AddMilkExpression().interpret(builder);
                System.out.println("Молоко додано");
            } 
            else if (command.equalsIgnoreCase("ADD_SUGAR")) {
                new AddSugarExpression().interpret(builder);
                System.out.println("Цукор додано");
            } 
            else if (command.equalsIgnoreCase("PRINT_ORDER")) {
                CoffeeOrder order = builder.build();
                order.printOrder();

                // Adapter
                ExternalPaymentService externalService = new ExternalPaymentService();
                PaymentProcessor payment = new PaymentAdapter(externalService);
                payment.pay(120.0);
                break;
            } 
            else {
                System.out.println("Невідома команда");
            }
        }

        scanner.close();
    }
}
