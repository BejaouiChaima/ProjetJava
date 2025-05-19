import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.sql.SQLException;
public class Main {
    private static FilmDao filmDAO = new FilmDao();
    private static SalleDao salleDAO = new SalleDao();
    private static ProjectionDao projectionDAO=new ProjectionDao();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("=== MENU PRINCIPAL ===");
            System.out.println("1 - S'authentifier");
            System.out.println("2 - S'inscrire");
            System.out.println("0 - Quitter");

            System.out.print("Choix : ");
            int choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {
                case 1:
                    // Connexion
                    System.out.print("Email : ");
                    String email = sc.nextLine();

                    System.out.print("Mot de passe : ");
                    String password = sc.nextLine();

                    User user = Authentification.login(email, password);
                    if (user != null) {
                        System.out.println("Bienvenue " + user.getNomComplet());
                        System.out.println("Connecté en tant que : " + user.getRole());

                        // Menu après connexion - dépend du rôle
                        if (user.getRole().equalsIgnoreCase("admin")) {
                            menuAdmin(sc, user);
                        } else {
                            menuClient(sc, user);
                        }
                    } else {
                        System.out.println("Email ou mot de passe incorrect.");
                    }
                    break;

                case 2:
                    // Inscription
                    System.out.print("Nom : ");
                    String nom = sc.nextLine();
                    System.out.print("Prénom : ");
                    String prenom = sc.nextLine();
                    System.out.print("Email : ");
                    String newEmail = sc.nextLine();
                    System.out.print("Mot de passe : ");
                    String newPassword = sc.nextLine();

                    // Le rôle est fixé à "client"
                    boolean success = Authentification.register(nom, prenom, newEmail, newPassword);
                    if (success) {
                        System.out.println("Inscription réussie !");
                        System.out.println("Vous pouvez maintenant vous connecter.");
                    } else {
                        System.out.println("Erreur lors de l'inscription.");
                    }
                    break;

                case 0:
                    running = false;
                    System.out.println("Fermeture du programme...");
                    break;

                default:
                    System.out.println("Choix invalide.");
            }
        }

        sc.close();
    }

    // Menu pour les clients
    private static void menuClient(Scanner sc, User user) {
        boolean connecté = true;

        while (connecté) {
            System.out.println("\n=== MENU CLIENT ===");
            System.out.println("1 - Voir les projections");
            System.out.println("2 - Gérer mes réservations");
            System.out.println("3 - Voir mes billets");
            System.out.println("4 - Se déconnecter");

            System.out.print("Choix : ");
            int action = sc.nextInt();
            sc.nextLine();

            switch (action) {
                case 1:
                    ProjectionDao.consulterSeances();
                    break;
                case 2:
                    gererReservationClient(sc, user);
                    break;
                case 3:
                    BilletDao.consulterBilletsParUser(user.getId());
                    break;
                case 4:
                    connecté = false;
                    System.out.println("Déconnecté avec succès.");
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }

    private static void gererReservationClient(Scanner sc, User user) {
        boolean retour = false;

        while (!retour) {
            System.out.println("\n=== GESTION DE MES RESERVATIONS ===");
            System.out.println("1 - Ajouter une réservation");
            System.out.println("2 - Consulter mes réservations");
            System.out.println("3 - Payer une réservation");
            System.out.println("4 - Annuler une réservation");
            System.out.println("0 - Retour au menu client");

            System.out.print("Choix : ");
            int choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {
                case 1:
                    ajouterReservationUtilisateur();
                    break;
                case 2:
                    consulterMesReservations(user);
                    break;
                case 3:
                    payerReservation(sc, user);
                    break;
                case 4:
                    annulerMaReservation(sc, user);
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide !");
            }
        }
    }
    private static void annulerMaReservation(Scanner sc, User user) {
        consulterMesReservations(user);
        System.out.print("Entrez l'ID de la réservation à annuler : ");
        int id = sc.nextInt();
        sc.nextLine();

        ReservationDao.annulerMaReservation(id); // Appelle directement ta méthode existante
    }

    private static void ajouterReservationUtilisateur() {
        ProjectionDao.consulterSeances();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez l'ID de la séance : ");
        int idSeance = scanner.nextInt();

        boolean reservationValide = false;

        while (!reservationValide) {
            System.out.print("Entrez le nombre de places : ");
            int nbPlaces = scanner.nextInt();

            // On redirige vers une version modifiée de la méthode ajouterReservation
            reservationValide = ReservationDao.ajouterReservation(idSeance, nbPlaces);
        }
    }

    // Menu pour les administrateurs
    private static void menuAdmin(Scanner sc, User user) {
        boolean connecté = true;
        while (connecté) {
            System.out.println("\n=== MENU ADMINISTRATEUR ===");
            System.out.println("1 - Gestion des Films");
            System.out.println("2 - Gestion des Salles");
            System.out.println("3 - Gestion des projections");
            System.out.println("4 - Gestion des réservations");
            System.out.println("5 - Voir toutes les billets");
            System.out.println("6 - Gestion des utilisateurs");
            System.out.println("7 - Se déconnecter");

            System.out.print("Choix : ");
            int action = sc.nextInt();
            sc.nextLine();

            switch (action) {
                case 1:
                    gestionFilms(sc);
                    break;

                case 2:
                    gestionSalles(sc);
                    break;

                case 3:
                    gestionProjections(sc);
                    break;
                case 4:
                    gestionReservations(sc);
                    break;
                case 5:
                    BilletDao.consulterTousBillets();
                    break;
                case 6:
                    gestionUtilisateurs(sc);
                    break;
                case 7:
                    connecté = false;
                    System.out.println("Déconnecté avec succès.");
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
    private static void gestionUtilisateurs(Scanner sc) {
        boolean retour = false;

        while (!retour) {
            System.out.println("\n=== GESTION DES UTILISATEURS ===");
            System.out.println("1 - Afficher tous les utilisateurs");
            System.out.println("2 - Modifier un utilisateur");
            System.out.println("3 - Supprimer un utilisateur");
            System.out.println("0 - Retour au menu administrateur");
            System.out.print("Choix : ");

            int choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {
                case 1:
                    afficherTousLesUtilisateurs();
                    break;
                case 2:
                    modifierUtilisateur(sc);
                    break;
                case 3:
                    supprimerUtilisateur(sc);
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    // Sous-menu pour la gestion des films
    private static void gestionFilms(Scanner sc) {
        boolean retour = false;

        while (!retour) {
            System.out.println("\n=== GESTION DES FILMS ===");
            System.out.println("1 - Ajouter un film");
            System.out.println("2 - Modifier un film");
            System.out.println("3 - Supprimer un film");
            System.out.println("4 - Afficher tous les films");
            System.out.println("5 - Rechercher un film");
            System.out.println("0 - Retour au menu administrateur");
            System.out.print("Choix : ");

            int choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {
                case 1:
                    ajouterFilm(sc);
                    break;
                case 2:
                    modifierFilm(sc);
                    break;
                case 3:
                    supprimerFilm(sc);
                    break;
                case 4:
                    afficherTousFilms();
                    break;
                case 5:
                    rechercherFilm(sc);
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }

    // Sous-menu pour la gestion des salles
    private static void gestionSalles(Scanner sc) {
        boolean retour = false;

        while (!retour) {
            System.out.println("\n=== GESTION DES SALLES ===");
            System.out.println("1 - Ajouter une salle");
            System.out.println("2 - Modifier une salle");
            System.out.println("3 - Supprimer une salle");
            System.out.println("4 - Afficher toutes les salles");
            System.out.println("0 - Retour au menu administrateur");
            System.out.print("Choix : ");

            int choix = sc.nextInt();
            sc.nextLine();

            switch (choix) {
                case 1:
                    ajouterSalle(sc);
                    break;
                case 2:
                    modifierSalle(sc);
                    break;
                case 3:
                    supprimerSalle(sc);
                    break;
                case 4:
                    afficherToutesSalles();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
    private static void gestionReservations(Scanner sc) {
        boolean retour = false;

        while (!retour) {
            System.out.println("\n=== GESTION DES PROJECTIONS ===");
            System.out.println("1 - Consulter toutes les reservations");
            System.out.println("2 - Annuler reservation");
            System.out.println("0 - Retour au menu administrateur");
            System.out.print("Choix : ");

            int choix = sc.nextInt();
            sc.nextLine();  // Consommer la ligne restante

            switch (choix) {
                case 1:
                    consulterToutesReservations();
                    break;
                case 2:
                    ReservationDao.consulterToutesReservations();
                    System.out.print("Entrez l'ID de la réservation à annuler : ");
                    int idReservation = sc.nextInt();
                    sc.nextLine();
                    ReservationDao.annulerReservationParAdmin(idReservation);
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
    private static void gestionProjections(Scanner sc) {
        boolean retour = false;

        while (!retour) {
            System.out.println("\n=== GESTION DES PROJECTIONS ===");
            System.out.println("1 - Ajouter une projection");
            System.out.println("2 - Modifier une projection");
            System.out.println("3 - Supprimer une projection");
            System.out.println("4 - Consulter les projections");
            System.out.println("0 - Retour au menu administrateur");
            System.out.print("Choix : ");

            int choix = sc.nextInt();
            sc.nextLine();  // Consommer la ligne restante

            switch (choix) {
                case 1:
                    ajouterProjection(sc);
                    break;
                case 2:
                    modifierProjection(sc);
                    break;
                case 3:
                    supprimerProjection(sc);
                    break;
                case 4:
                    ProjectionDao.consulterSeances();
                    break;
                case 0:
                    retour = true;
                    break;
                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
    private static void afficherTousLesUtilisateurs() {
        List<User> users = UserDao.getAllUsers();
        System.out.println("\n=== LISTE DES UTILISATEURS ===");

        if (users.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé.");
        } else {
            System.out.printf("%-5s | %-20s | %-25s | %-30s%n",
                    "ID", "Nom et Prénom", "Email", "Rôle");
            System.out.println("-".repeat(90));

            for (User u : users) {
                System.out.printf("%-5d | %-20s | %-25s | %-30s%n",
                        u.getId(), u.getNomComplet(), u.getEmail(), u.getRole());
            }
        }
    }

    private static void modifierUtilisateur(Scanner sc) {
        afficherTousLesUtilisateurs();
        System.out.print("Entrez l'ID de l'utilisateur à modifier : ");
        int id = sc.nextInt();
        sc.nextLine();

        List<User> users = UserDao.getAllUsers();
        User user = users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);

        if (user == null) {
            System.out.println("Utilisateur non trouvé.");
            return;
        }

        System.out.print("Nouveau nom  : ");
        String nom = sc.nextLine().trim();
        if (!nom.isEmpty()) user.setNom(nom);

        System.out.print("Nouveau prénom  : ");
        String prenom = sc.nextLine().trim();
        if (!prenom.isEmpty()) user.setPrenom(prenom);

        System.out.print("Nouvel email : ");
        String email = sc.nextLine().trim();
        if (!email.isEmpty()) user.setEmail(email);

        System.out.print("Nouveau rôle : ");
        String role = sc.nextLine().trim();
        if (!role.isEmpty() && (role.equals("admin") || role.equals("client"))) {
            user.setRole(role);
        }

        if (UserDao.updateUser(user)) {
            System.out.println("Utilisateur modifié avec succès.");
        } else {
            System.out.println("Échec de la modification.");
        }
    }

    private static void supprimerUtilisateur(Scanner sc) {
        afficherTousLesUtilisateurs();
        System.out.print("Entrez l'ID de l'utilisateur à supprimer : ");
        int id = sc.nextInt();
        sc.nextLine();

        if (UserDao.deleteUser(id)) {
            System.out.println("Utilisateur supprimé.");
        } else {
            System.out.println("Erreur lors de la suppression.");
        }
    }

    private static void ajouterProjection(Scanner sc) {
        System.out.println("\n=== AJOUTER UNE PROJECTION ===");

        System.out.print("Horaire de la projection (format HH:MM) : ");
        String horaire = sc.nextLine();

        System.out.print("Titre du film : ");
        String titreFilm = sc.nextLine();

        System.out.print("Numéro de la salle : ");
        int numeroSalle = sc.nextInt();
        sc.nextLine(); // vider le buffer

        // Récupérer l'idSalle et la salle
        int idSalle = SalleDao.getIdSalleParNumero(numeroSalle);
        Salle salle = salleDAO.consulterSalle(idSalle);

        if (salle == null) {
            System.out.println("Erreur : salle introuvable !");
            return;
        }

        int nbPlaces = 0;

        // Boucle pour saisir un nombre de places valide
        while (true) {
            System.out.print("Nombre de places disponibles (max " + salle.getNombrePlaces() + ") : ");
            nbPlaces = sc.nextInt();
            sc.nextLine(); // vider le buffer
            if (nbPlaces <= salle.getNombrePlaces()) {
                break;
            } else {
                System.out.println("Erreur : Ce nombre dépasse la capacité de la salle !");
                System.out.println("Capacité maximale de la salle : " + salle.getNombrePlaces());
                System.out.println("Veuillez saisir un nouveau nombre de places.");
            }
        }

        System.out.print("Prix de la séance : ");
        BigDecimal prix = sc.nextBigDecimal();
        sc.nextLine(); // vider buffer si besoin

        int idFilm = FilmDao.getIdFilmParTitre(titreFilm);

        if (idFilm == -1 || idSalle == -1) {
            System.out.println("Erreur : film ou salle introuvable !");
            return;
        }

        Projection projection = new Projection(0, horaire, nbPlaces, idFilm, idSalle, prix);
        ProjectionDao.creerSeance(projection);
    }

    private static void modifierProjection(Scanner sc) {
        System.out.println("\n=== MODIFIER UNE PROJECTION ===");
        ProjectionDao.consulterSeances();

        System.out.print("\nEntrez l'ID de la séance à modifier : ");
        int id_seance = sc.nextInt();
        sc.nextLine();

        Projection projection = ProjectionDao.consulterProjection(id_seance);

        if (projection != null) {
            System.out.println("Séance trouvée : " + projection.getHoraire());

            // Modifier horaire
            System.out.print("Nouvel horaire (" + projection.getHoraire() + ") : ");
            String horaire = sc.nextLine();
            if (!horaire.isEmpty()) projection.setHoraire(horaire);

            // Modifier titre du film (et récupérer son ID)
            System.out.print("Nouveau titre du film : ");
            String titreFilm = sc.nextLine();
            int idFilm = projection.getId_film();
            if (!titreFilm.isEmpty()) {
                idFilm = FilmDao.getIdFilmParTitre(titreFilm);
                if (idFilm == -1) {
                    System.out.println("Erreur : film introuvable !");
                    return;
                }
                projection.setId_film(idFilm);
            }

            // Modifier numéro de salle (et récupérer son ID)
            System.out.print("Nouveau numéro de salle : ");
            String numeroSalleStr = sc.nextLine();
            int idSalle = projection.getId_salle();
            if (!numeroSalleStr.isEmpty()) {
                int numeroSalle;
                try {
                    numeroSalle = Integer.parseInt(numeroSalleStr);
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : numéro de salle invalide !");
                    return;
                }
                idSalle = SalleDao.getIdSalleParNumero(numeroSalle);
                if (idSalle == -1) {
                    System.out.println("Erreur : salle introuvable !");
                    return;
                }
                projection.setId_salle(idSalle);
            }

            // Récupérer la salle pour vérifier la capacité
            Salle salle = salleDAO.consulterSalle(idSalle);
            if (salle == null) {
                System.out.println("Erreur : salle introuvable !");
                return;
            }

            // Modifier nombre de places avec validation de la capacité
            int nbPlacesActuel = projection.getNbPlaces();
            int nbPlaces = nbPlacesActuel;
            System.out.print("Nouveau nombre de places (" + nbPlacesActuel + ") : ");
            String nbPlacesStr = sc.nextLine();

            if (!nbPlacesStr.isEmpty()) {
                boolean valide = false;
                while (!valide) {
                    try {
                        nbPlaces = Integer.parseInt(nbPlacesStr);
                        if (nbPlaces <= salle.getNombrePlaces()) {
                            valide = true;
                        } else {
                            System.out.println("Erreur : Ce nombre dépasse la capacité de la salle !");
                            System.out.println("Capacité maximale de la salle : " + salle.getNombrePlaces());
                            System.out.print("Veuillez saisir un nouveau nombre de places : ");
                            nbPlacesStr = sc.nextLine();
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Erreur : saisie invalide, veuillez entrer un nombre entier.");
                        System.out.print("Veuillez saisir un nouveau nombre de places : ");
                        nbPlacesStr = sc.nextLine();
                    }
                }
                projection.setNbPlaces(nbPlaces);
            }

            // Modifier prix de la séance
            System.out.print("Nouveau prix (" + projection.getPrix() + ") : ");
            String prixStr = sc.nextLine();
            if (!prixStr.isEmpty()) {
                try {
                    BigDecimal prix = new BigDecimal(prixStr);
                    projection.setPrix(prix);
                } catch (NumberFormatException e) {
                    System.out.println("Erreur : prix invalide. La valeur n'a pas été modifiée.");
                }
            }

            // Mettre à jour la projection en base
            ProjectionDao.modifierSeance(projection);

        } else {
            System.out.println("Projection non trouvée avec l'ID : " + id_seance);
        }
    }




    // Supprimer une projection
    private static void supprimerProjection(Scanner sc) {
        System.out.println("\n=== SUPPRIMER UNE PROJECTION ===");
        ProjectionDao.consulterSeances();
        System.out.print("ID de la projection à supprimer : ");
        int idSeance = sc.nextInt();

        ProjectionDao.supprimerSeance(idSeance);
    }



    private static void ajouterFilm(Scanner sc) {
        System.out.println("\n=== AJOUTER UN FILM ===");

        System.out.print("Date (JJ/MM/AAAA) : ");
        String date = sc.nextLine();

        System.out.print("Catégorie : ");
        String categorie = sc.nextLine();

        System.out.print("Titre : ");
        String titre = sc.nextLine();

        System.out.print("Durée (minutes) : ");
        int duree = sc.nextInt();
        sc.nextLine();

        System.out.print("Réalisateur : ");
        String realisateur = sc.nextLine();

        System.out.print("Description : ");
        String description = sc.nextLine();

        Film nouveauFilm = new Film(date, categorie, titre, duree, realisateur, description);

        if (filmDAO.ajouterFilm(nouveauFilm)) {
            System.out.println("Film ajouté avec succès !");
        } else {
            System.out.println("Erreur lors de l'ajout du film.");
        }
    }

    private static void modifierFilm(Scanner sc) {
        System.out.println("\n=== MODIFIER UN FILM ===");
        afficherTousFilms();

        System.out.print("\nEntrez l'ID du film à modifier : ");
        int idFilm = sc.nextInt();
        sc.nextLine();

        Film film = filmDAO.consulterFilm(idFilm);

        if (film != null) {
            System.out.println("Film trouvé : " + film.getTitre());

            System.out.print("Nouvelle date (" + film.getDate() + ") : ");
            String date = sc.nextLine();
            if (!date.isEmpty()) film.setDate(date);

            System.out.print("Nouvelle catégorie (" + film.getCategorie() + ") : ");
            String categorie = sc.nextLine();
            if (!categorie.isEmpty()) film.setCategorie(categorie);

            System.out.print("Nouveau titre (" + film.getTitre() + ") : ");
            String titre = sc.nextLine();
            if (!titre.isEmpty()) film.setTitre(titre);

            System.out.print("Nouvelle durée (" + film.getDuree() + " minutes) : ");
            String dureeStr = sc.nextLine();
            if (!dureeStr.isEmpty()) film.setDuree(Integer.parseInt(dureeStr));

            System.out.print("Nouveau réalisateur (" + film.getRealisateur() + ") : ");
            String realisateur = sc.nextLine();
            if (!realisateur.isEmpty()) film.setRealisateur(realisateur);

            System.out.print("Nouvelle description (" + film.getDescription() + ") : ");
            String description = sc.nextLine();
            if (!description.isEmpty()) film.setDescription(description);

            if (filmDAO.modifierFilm(film)) {
                System.out.println("Film modifié avec succès !");
            } else {
                System.out.println("Erreur lors de la modification du film.");
            }
        } else {
            System.out.println("Film non trouvé avec l'ID : " + idFilm);
        }
    }

    private static void supprimerFilm(Scanner sc) {
        System.out.println("\n=== SUPPRIMER UN FILM ===");
        afficherTousFilms();

        System.out.print("\nEntrez l'ID du film à supprimer : ");
        int idFilm = sc.nextInt();
        sc.nextLine();

        Film film = filmDAO.consulterFilm(idFilm);

        if (film != null) {
            System.out.println("Film à supprimer : " + film.getTitre());
            System.out.print("Êtes-vous sûr de vouloir supprimer ce film ? (O/N) : ");
            String confirmation = sc.nextLine();

            if (confirmation.equalsIgnoreCase("O")) {
                if (filmDAO.supprimerFilm(idFilm)) {
                    System.out.println("Film supprimé avec succès !");
                } else {
                    System.out.println("Erreur lors de la suppression du film.");
                }
            } else {
                System.out.println("Suppression annulée.");
            }
        } else {
            System.out.println("Film non trouvé avec l'ID : " + idFilm);
        }
    }

    private static void afficherTousFilms() {
        System.out.println("\n=== LISTE DES FILMS ===");
        List<Film> films = filmDAO.getAllFilms();

        if (films.isEmpty()) {
            System.out.println("Aucun film trouvé.");
        } else {
            System.out.printf("%-5s | %-10s | %-15s | %-20s | %-6s | %-20s%n",
                    "ID","Date", "Catégorie", "Titre", "Durée", "Réalisateur");
            System.out.println("-".repeat(105));

            for (Film film : films) {
                System.out.printf("%-5d | %-10s | %-15s | %-20s | %-6d | %-20s%n",
                        film.getIdFilm(), film.getDate(),
                        film.getCategorie(), film.getTitre(), film.getDuree(),
                        film.getRealisateur());
            }
        }
    }

    private static void rechercherFilm(Scanner sc) {
        System.out.println("\n=== RECHERCHER UN FILM ===");
        System.out.print("Entrez un mot-clé pour la recherche : ");
        String motCle = sc.nextLine();

        List<Film> films = filmDAO.rechercherFilmsParTitre(motCle);

        if (films.isEmpty()) {
            System.out.println("Aucun film trouvé pour la recherche : " + motCle);
        } else {
            System.out.println("Résultats pour la recherche : " + motCle);
            System.out.printf("%-5s | %-20s | %-10s | %-15s | %-20s | %-6s | %-20s%n",
                    "ID", "Nom", "Date", "Catégorie", "Titre", "Durée", "Réalisateur");
            System.out.println("-".repeat(105));

            for (Film film : films) {
                System.out.printf("%-5d | %-10s | %-15s | %-20s | %-6d | %-20s%n",
                        film.getIdFilm(), film.getDate(),
                        film.getCategorie(), film.getTitre(), film.getDuree(),
                        film.getRealisateur());
            }
        }
    }

    // Méthodes pour la gestion des salles
    private static void ajouterSalle(Scanner sc) {
        System.out.println("\n=== AJOUTER UNE SALLE ===");

        System.out.print("Numéro de salle : ");
        String numero = sc.nextLine();

        System.out.print("Nombre de places : ");
        int nombrePlaces = sc.nextInt();
        sc.nextLine();

        Salle nouvelleSalle = new Salle(nombrePlaces, numero);

        if (salleDAO.ajouterSalle(nouvelleSalle)) {
            System.out.println("Salle ajoutée avec succès !");
        } else {
            System.out.println("Erreur lors de l'ajout de la salle.");
        }
    }

    private static void modifierSalle(Scanner sc) {
        System.out.println("\n=== MODIFIER UNE SALLE ===");
        afficherToutesSalles();

        System.out.print("\nEntrez l'ID de la salle à modifier : ");
        int idSalle = sc.nextInt();
        sc.nextLine();

        Salle salle = salleDAO.consulterSalle(idSalle);

        if (salle != null) {
            System.out.println("Salle trouvée : " + salle.getNumero());


            System.out.print("Nouveau numéro de salle (" + salle.getNumero() + ") : ");
            String numero = sc.nextLine();
            if (!numero.isEmpty()) salle.setNumero(numero);

            System.out.print("Nouveau nombre de places (" + salle.getNombrePlaces() + ") : ");
            String nombrePlacesStr = sc.nextLine();
            if (!nombrePlacesStr.isEmpty()) salle.setNombrePlaces(Integer.parseInt(nombrePlacesStr));

            if (salleDAO.modifierSalle(salle)) {
                System.out.println("Salle modifiée avec succès !");
            } else {
                System.out.println("Erreur lors de la modification de la salle.");
            }
        } else {
            System.out.println("Salle non trouvée avec l'ID : " + idSalle);
        }
    }

    private static void supprimerSalle(Scanner sc) {
        System.out.println("\n=== SUPPRIMER UNE SALLE ===");
        afficherToutesSalles();

        System.out.print("\nEntrez l'ID de la salle à supprimer : ");
        int idSalle = sc.nextInt();
        sc.nextLine();

        Salle salle = salleDAO.consulterSalle(idSalle);

        if (salle != null) {
            System.out.println("Salle à supprimer : " + salle.getNumero() + " (Places: " + salle.getNombrePlaces() + ")");
            System.out.print("Êtes-vous sûr de vouloir supprimer cette salle ? (O/N) : ");
            String confirmation = sc.nextLine();

            if (confirmation.equalsIgnoreCase("O")) {
                if (salleDAO.supprimerSalle(idSalle)) {
                    System.out.println("Salle supprimée avec succès !");
                } else {
                    System.out.println("Erreur lors de la suppression de la salle.");
                }
            } else {
                System.out.println("Suppression annulée.");
            }
        } else {
            System.out.println("Salle non trouvée avec l'ID : " + idSalle);
        }
    }

    private static void afficherToutesSalles() {
        System.out.println("\n=== LISTE DES SALLES ===");
        List<Salle> salles = salleDAO.getAllSalles();

        if (salles.isEmpty()) {
            System.out.println("Aucune salle trouvée.");
        } else {
            System.out.printf("%-5s | %-15s | %-15s%n",
                    "ID", "Numéro", "Nombre Places");
            System.out.println("-".repeat(40));

            for (Salle salle : salles) {
                System.out.printf("%-5d | %-15s | %-15d%n",
                        salle.getIdSalle(), salle.getNumero(), salle.getNombrePlaces());
            }
        }

    }

    private static void consulterMesReservations(User user) {
        List<Reservation> reservations = ReservationDao.consulterReservationsParUser(user.getId());
    }
    private static void payerReservation(Scanner sc,User user) {
        System.out.print("La liste de mes reservation : ");
        consulterMesReservations(user);
        System.out.print("Entrez l'ID de la réservation à payer : ");
        int idReservation = sc.nextInt();
        sc.nextLine();

        if (ReservationDao.payerReservation(idReservation)) {
            System.out.println("Paiement effectué avec succès !");
        } else {
            System.out.println("Erreur lors du paiement.");
        }
    }
    private static void consulterToutesReservations() {
        List<Reservation> reservations = ReservationDao.consulterToutesReservations();

    }




}