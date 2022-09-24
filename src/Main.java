import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        MainManu menu = new MainManu();

        menu.start();


//        boolean menu = true;
//        String opcjaMenu;
//
//        System.out.println("Witaj w MojaPoczta. Zaloguj się lub utwóz użytkownika.");
//        System.out.println("1. Zaloguj");
//        System.out.println("2. Zarejestruj użytkownika");
//        System.out.println("3. Zakończ");
//
//        Scanner skaner = new Scanner(System.in);
//
//        while (menu) {
//           opcjaMenu = skaner.nextLine();
//
//           if (opcjaMenu.equals("1")) {
//               System.out.println("zaloguj");
//           }
//            else if (opcjaMenu.equals("2")) {
//               System.out.println("Zarejestruj");
//           }
//            else if (opcjaMenu.equals("3")) {
//               menu = false;
//               break;
//           }
//            else {
//               System.out.println("Wybierz element menu");
//           }
//
//        }


//        Logowanie logow = new Logowanie();
//
//        boolean zalogowano = logow.logowanie("damix", "qwerty");
//        if (zalogowano)
//            System.out.println("Zalogowano");
//        else
//            System.out.println("Błąd logowania");




    }
}


class Logowanie {

    private String login, password;
    private boolean zalogowano;
    boolean userExist;
    private String actualUser;
    File file = new File("log.txt");

    public boolean logowanie(String login, String password) {

        this.login = login;
        this.password = password;

        return sprawdzPlik();

    }

    public boolean sprawdzPlik() {

        Scanner skanerPliku = null;
        try {
            skanerPliku = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        while (skanerPliku.hasNextLine()) {
            String tmpLogin = skanerPliku.nextLine();
            if (login.equals(tmpLogin)) {
                String tmpPass = skanerPliku.nextLine();
                if(password.equals(tmpPass)) {
                    skanerPliku.close();
                    System.out.println("Zalogowano");
                    setActualUser(login);
                    makeUserPanel(actualUser);
                    return true;

                }
            }


        }
        skanerPliku.close();
        System.out.println("Błąd logowania");
        return false;



    }

    public boolean sprawdzPlik(boolean onlyLogin, String login) {

        this.login = login;

        Scanner skanerPliku = null;
        try {
            skanerPliku = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        while (skanerPliku.hasNextLine()) {
            String separator = "//Dodano nowego użytkownika ";
            String tmpString = skanerPliku.nextLine();
            if (tmpString.equals(separator)) {
                String tmpLogin = skanerPliku.nextLine();
                if (login.equals(tmpLogin)) {
                    userExist = true;
                    return false;
                }
            }


        }
    return true;
    }
    public void setActualUser(String actualUser) {

        this.actualUser = actualUser;

    }

    public String getActualUser() {
        return actualUser;
    }

    public void makeUserPanel(String actualUser) {

        System.out.println("Panel użytkownika: ");
        System.out.println("1. Napisz wiadomość");
        System.out.println("2. Wyświetl wiadomośći odebrane");
        System.out.println("3. Wyświetl wiadomośći wysłane");
        System.out.println("4. Wyświetl wiadomośći SPAM");

        Scanner panelSkaner = new Scanner(System.in);
        String panelMenu = panelSkaner.nextLine();


        switch (panelMenu) {
            case "1" :
                sendMail();       /////// metoda na wysylanie wiadomosci
                break;
            case "2" :
                openReceived();
                break;
            case "3" :
                openSend();
                break;
            case "4" :
                openSpam();
                break;
        }



    }
    String tytyl = null;
    public void sendMail() {

        System.out.println("Podaj adresata wiadomości");
        Scanner adresat = new Scanner(System.in);
        String adresatWiadomosci = adresat.nextLine();
        if (czyIstnieje(adresatWiadomosci)) {
            System.out.println("Podaj tytuł wiadomości: ");
            adresat = new Scanner(System.in);
            tytyl = adresat.nextLine();
            System.out.println("Wpisz poniżej treść wiadomości: ");
            Scanner trescWiadomosci = new Scanner(System.in);
            String trescMaila = trescWiadomosci.nextLine();

            File receivedMail = new File("users/" + adresatWiadomosci + "/received/" + tytyl + ".txt");
            File sendMail = new File("users/" + getActualUser() + "/send/" + tytyl + ".txt");

            try {
//                PrintWriter pw = new PrintWriter(receivedMail);
//                PrintWriter pw2 = new PrintWriter(sendMail);

                PrintWriter[] pwTest = new PrintWriter[2];
                pwTest[0] = new PrintWriter(receivedMail);
                pwTest[1] = new PrintWriter(sendMail);

                for(int i = 0; i <= 1; i++) {
                    pwTest[i].println("From:" + getActualUser() + ",to:" + adresatWiadomosci + ",title:" + tytyl);
                    pwTest[i].println("**********************************************************");
                    pwTest[i].println(trescMaila);
                    pwTest[i].close();

                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

        }
        else {
            System.out.println("Nie znaleziono takiego użytkowniak w bazie danych. Wpisz poprawną nazwę.");
            sendMail();
        }



    }

    public boolean czyIstnieje(String user) {

        File logFile = new File("log.txt");
        Scanner logFileScanner = null;
        try {
             logFileScanner = new Scanner(logFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String separator = "//Dodano nowego użytkownika ";
        while (logFileScanner.hasNextLine()) {
            String nextLine = logFileScanner.nextLine();
            if(nextLine.equals(separator)) {
                String userInLogFile = logFileScanner.nextLine();
                if (user.equals(userInLogFile))
                    return true;
            }
        }


        return false;
    }

    public void openMail(String folder, String actualMail) {

        File mail = new File("users/" + getActualUser() + "/" + folder + "/" + actualMail);
        Scanner fileScanner = null;
        try {
            fileScanner = new Scanner(mail);

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        while (fileScanner.hasNextLine()) {
            String nextLine = fileScanner.nextLine();
            if (nextLine.contains("From:"))
                fileScanner.nextLine();
            System.out.println(nextLine);


        }

        fileScanner.close();

        if (folder.equals("received")) {
            System.out.println("Przeniesc do SPAMU?");

        Scanner doSpamu = new Scanner(System.in);
        String takNie = doSpamu.nextLine().toLowerCase();

            if (takNie.equals("t") || takNie.equals("tak")) {
                try {
                    Files.move(Paths.get("users/" + getActualUser() + "/received/" + actualMail), Paths.get("users/" + getActualUser() + "/SPAM/" + actualMail), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public void openReceived() {

        File received = new File("users/" + getActualUser()  + "/received");

        String[] pliki = received.list();
////        File[] pliki1 = received.listFiles();
//
//        //Typ danych //zmienna // nazwa tablicy
//        for (String i : pliki) {
//            System.out.println(i);
//        }

        for (int i = 0; i < pliki.length; i++) {
            System.out.println((i+1) + ". " + pliki[i]);
        }

        Scanner skan = new Scanner(System.in);
        int wybor = skan.nextInt();


        if (wybor > 0 && wybor <= pliki.length) {

            String actualMail = pliki[wybor-1];
            openMail("received", actualMail);
        }
            else {
            System.out.println("Wybierz prawidłową wiadomość:");
            openReceived();
        }


//        try {
//            Scanner receivedSkaner = new Scanner(received);
//            String linia = receivedSkaner.nextLine();
//            while (receivedSkaner.hasNextLine()) {
//                System.out.println(linia);
//            }
//
//            receivedSkaner.close();
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }


    }


    public void openSend() {

        File send = new File("users/" + getActualUser() + "/send");

        String[] wiadomosci = send.list();

        for (int i = 0; i < wiadomosci.length; i++) {
            System.out.println(i+1 + ". " + wiadomosci[i]);
        }


        Scanner skan = new Scanner(System.in);
        int wybor = skan.nextInt();


        if (wybor > 0 && wybor <= wiadomosci.length) {

            String actualMail = wiadomosci[wybor-1];
            openMail("send", actualMail);
        }
        else {
            System.out.println("Wybierz prawidłową wiadomość:");
            openSend();
        }


    }

    public void openSpam() {

        File spam = new File("users/" + getActualUser() + "/SPAM");

        String[] wiadomosciSpam = spam.list();

        for(int i = 0; i < wiadomosciSpam.length; i++) {
            System.out.println(i+1 +". "+wiadomosciSpam[i]);
        }

        Scanner skaner = new Scanner(System.in);
        int wybor = skaner.nextInt();

        if (wybor > 0 && wybor <= wiadomosciSpam.length) {
            String actualMail = wiadomosciSpam[wybor-1];
            openMail("Spam", actualMail);
        }
        else {
            System.out.println("Wybierz prawidłową wiadomość:");
            openSpam();
        }


    }



}


class Rejestracja extends Logowanie{

    private String login, password;
    private boolean zalogowano;


    public void zarejestruj(String login, String password) {


        FileWriter fw = null;

        try {

            fw = new FileWriter(file, true);
            fw.write("//Dodano nowego użytkownika \n");
            fw.write(login + "\n");
            fw.write(password + "\n");

            fw.close();

            stworzKatalogi(login);

        } catch (IOException e) {
            System.err.print("Something went wrong");
        }


    }

    public void stworzKatalogi(String login) {

        int i = 1;
        String[] nazwyKatalogow = new String[4];
        nazwyKatalogow[0] = login;
        nazwyKatalogow[1] = "received";
        nazwyKatalogow[2] = "send";
        nazwyKatalogow[3] = "SPAM";


        File file = new File("users/" + login);
        file.mkdirs();

        while (i <= nazwyKatalogow.length-1) {
            File files = new File("users/" + login + "/" +  nazwyKatalogow[i]);
            files.mkdir();
            i++;
        }

//        File katalogi = new File("users/" + login);
//        katalogi.mkdir();
//
//        katalogi = new File("users/" + "received");
//        katalogi.mkdir();
//
//        katalogi = new File("users/" + "send");
//        katalogi.mkdir();
//
//        katalogi = new File("users/" + "SPAM");
//        katalogi.mkdir();
//

    }

}



class MainManu {

    String menuOption;
    String login, pass;
    Logowanie logging = new Logowanie();
    Rejestracja regis = new Rejestracja();

    public MainManu() {
        System.out.println("Witaj w MojaPoczta. Zaloguj się lub utwóz użytkownika.");
        System.out.println("1. Zaloguj");
        System.out.println("2. Zarejestruj użytkownika");
        System.out.println("3. Zakończ");
    }

    public void start() {

        Scanner skaner = new Scanner(System.in);
        menuOption = skaner.nextLine();
        boolean loginIsOk = false;

        if (menuOption.equals("1") || menuOption.equalsIgnoreCase("zaloguj")) {
            System.out.println("Podaj nazwę użytkownika: ");
            login = skaner.nextLine();
            System.out.println("Podaj hasło: ");
            pass = skaner.nextLine();
            logging.logowanie(login, pass);
        }
        else if (menuOption.equals("2") || menuOption.equals("2.") || menuOption.equalsIgnoreCase("Zarejestruj użytkownika")) {
            while (loginIsOk == false) {
                System.out.println("Podaj nazwę użytkownika: [min 3 max 10 znaków]");
                login = skaner.nextLine();
                if (login.length() >= 3 && login.length() <= 10 && logging.sprawdzPlik(true, login)) {
                    System.out.println("Login zatwierdzony");
                    loginIsOk = true;
                }
                else {
                    System.out.println("Login nie spełnia wymagań lub istnieje");
                }

            }
            System.out.println("Podaj hasło: [min 6 znaków, min 1 znak specjalny]");
            pass = skaner.nextLine();
            regis.zarejestruj(login, pass);
        }

    }


}