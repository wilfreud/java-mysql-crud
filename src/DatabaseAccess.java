import java.sql.*;

public class DatabaseAccess {


    private Connection connection = null;

    //Prepared statements
    private PreparedStatement insertStatement;
    private PreparedStatement updateStatement;
    private PreparedStatement deleteStatement;
    private PreparedStatement selectAllStatement;
    private PreparedStatement selectOneStatement;

    private PreparedStatement searchStatement;


    public DatabaseAccess(final String username, final String password) {
        try {
            final String dB_name = "javabase";
            final String url = "jdbc:mysql://localhost:3306/" + dB_name;
            this.connection = DriverManager.getConnection(url, username, password);

            if (this.connection == null) throw new SQLException();

//            set statements
            this.insertStatement = connection.prepareStatement("INSERT INTO etudiant (prenom, nom, telephone) VALUES (?, ?, ?)");
            this.updateStatement = connection.prepareStatement("UPDATE etudiant SET prenom = CASE WHEN ? <> '' THEN ? ELSE prenom END, nom = CASE WHEN ? <> '' THEN ? ELSE nom END, telephone = CASE WHEN ? <> '' THEN ? ELSE telephone END WHERE telephone = ?");
            this.selectOneStatement = connection.prepareStatement("SELECT * FROM etudiant WHERE telephone=?");
            this.deleteStatement = connection.prepareStatement("DELETE FROM etudiant WHERE telephone=?");
            this.selectAllStatement = connection.prepareStatement("SELECT * FROM etudiant");
// Could be more performant with a fulltext search index
            this.searchStatement = connection.prepareStatement(
                    "SELECT * " +
                            "FROM etudiant " +
                            "WHERE telephone LIKE ? " +
                            "OR prenom LIKE ? " +
                            "OR nom LIKE ?");

        } catch (SQLException ex) {
            System.err.println("Echec de connexion à la base de données...\n" + ex.getMessage());
            System.exit(-1);
        }
    }


    public void createEtudiant(String prenom, String nom, String telephone) {
        try {
            this.insertStatement.setString(1, prenom);
            this.insertStatement.setString(2, nom);
            this.insertStatement.setString(3, telephone);
            this.insertStatement.executeUpdate();
            System.out.println("Etudiant ajouté!\n");
        } catch (SQLException ex) {
            System.err.println("Impossible d'ajouter cet etudiant...\n" + ex.getMessage());
        }
    }

    public void showEtudiants() {
        try {
            ResultSet result = selectAllStatement.executeQuery();
            this.displayEtudiantResultSet(result);
            result.close();
        } catch (SQLException ex) {
            System.err.println("Errer lors de la recuperation de la liste...\n" + ex.getMessage());
        }

    }

    public boolean findOneEtudiant(String telephone) {
        try {
            this.selectOneStatement.setString(1, telephone);
            ResultSet result = this.selectOneStatement.executeQuery();

            if (!result.next()) {
                return false;
            }

            System.out.println("Informations de l'étudiant");
            this.displayEtudiantResultSet(result);
            result.close();
        } catch (SQLException ex) {
            System.err.println("Erreur lors inattendue lors de la recherche de l'etudiant\n" + ex.getMessage());
        }
        return true;
    }

    public void updateEtudiantInfos(String findPhone, String prenom, String nom, String telephone) {
        try {
            this.updateStatement.setString(1, prenom);
            this.updateStatement.setString(2, prenom);
            this.updateStatement.setString(3, nom);
            this.updateStatement.setString(4, nom);
            this.updateStatement.setString(5, telephone);
            this.updateStatement.setString(6, telephone);
            this.updateStatement.setString(7, findPhone);
            this.updateStatement.executeUpdate();

        } catch (SQLException ex) {
            System.err.println("Erreur lors de la mise à jour de l'etudiant\n" + ex.getMessage());
        }
    }

    public void deleteEtudiant(String telephone) {
        try {
            if (!this.findOneEtudiant(telephone)) {
                System.out.println("Etudiant introuvable");
                return;
            }

            this.deleteStatement.setString(1, telephone);
            int rowsAffected = this.deleteStatement.executeUpdate();
            System.out.println(rowsAffected + " etudiant(s) supprimé(s)");
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la suppression de l'etudiant\n" + ex.getMessage());
        }
    }

    public void searchEtudiant(String criteria) {
        try {
            this.searchStatement.setString(1, "%" + criteria + "%");
            this.searchStatement.setString(2, "%" + criteria + "%");
            this.searchStatement.setString(3, "%" + criteria + "%");
            ResultSet result = this.searchStatement.executeQuery();
            this.displayEtudiantResultSet(result);
            result.close();
        } catch (Exception ex) {
            System.err.println("Erreur lors de la recherche\n" + ex.getMessage());
        }
    }

    //    Terminate connection
    public void terminate() {
        try {
            if (this.connection != null) this.connection.close();
        } catch (SQLException ex) {
            System.err.println("Impossible de fermer la connection à la base de donnéeş...\n" + ex.getMessage());
        }
    }

    private void displayEtudiantResultSet(ResultSet result) throws SQLException {
        if (result == null) return;
        ResultSetMetaData metaData = result.getMetaData();
        int columnCount = metaData.getColumnCount();

        System.out.println("\tListe des etudiants");
//            Print table headers
        for (int i = 1; i <= columnCount; i++) {
            System.out.printf("%-15s", metaData.getColumnName(i));
        }
        System.out.println();

//            print table content
        while (result.next()) {
            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-15s", result.getString(i));
            }
            System.out.println();
        }

        System.out.println();

    }
}
