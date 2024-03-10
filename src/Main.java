import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        final String username = System.getenv("mysql_username");
        final String password = System.getenv("mysql_password");

        DatabaseAccess manager = new DatabaseAccess(username, password);

        // Variables
        String firstName, lastName, phone;

        System.out.println("Bienvenue !");
        while (true) {
            showMenu();
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 0:
                    System.out.println("Au revoir!");
                    manager.terminate();
                    System.exit(0);
                    break;
                case 1:
                    System.out.print("Prenom: ");
                    firstName = sc.nextLine();

                    System.out.print("Nom: ");
                    lastName = sc.nextLine();

                    System.out.print("Telephone: ");
                    phone = sc.nextLine();

                    manager.createEtudiant(firstName, lastName, phone);
                    break;
                case 2:
                    manager.showEtudiants();
                    break;
                case 3:
                    System.out.println("Entrez le numero de telephone de l'etudiant");
                    String etuPhone = sc.nextLine();

//                    Check for exsitence
                    if (!manager.findOneEtudiant(etuPhone)) {
                        System.out.println("Etudiant introuvable");
                        break;
                    }

                    System.out.println("Laissez les champs vides pour ne rien changer");
                    System.out.print("Nouveau prenom: ");
                    firstName = sc.nextLine();

                    System.out.print("Nouveau nom: ");
                    lastName = sc.nextLine();

                    System.out.print("Nouveau numero de telephone: ");
                    phone = sc.nextLine();

                    manager.updateEtudiantInfos(etuPhone, firstName, lastName, phone);
                    break;
                case 4:
                    System.out.println("Indiquez le numero de l'etudiant à supprimer");
                    phone = sc.nextLine();
                    manager.deleteEtudiant(phone);
                    break;
                case 5:
                    System.out.println("Qui ?");
                    phone = sc.nextLine();
                    manager.searchEtudiant(phone);
                    break;
                default:
                    System.out.println("Option invalide!");
                    break;

            }

        }

    }

    public static void showMenu() {
        System.out.println("Veuillez sélectionner une option :");
        System.out.println("1. Ajouter un étudiant");
        System.out.println("2. Afficher tous les étudiants");
        System.out.println("3. Mettre à jour un étudiant");
        System.out.println("4. Supprimer un étudiant");
        System.out.println("5. Rechercher un étudiant");
        System.out.println("0. Quitter");
    }

}
