package utils; // Ensure this package declaration is correct

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtils {
    /**
     * Displays an error alert dialog.
     * @param title The title of the alert dialog.
     * @param message The content message of the alert dialog.
     */
    public static void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a warning alert dialog.
     * @param title The title of the alert dialog.
     * @param message The content message of the alert dialog.
     */
    public static void showWarningAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays an information alert dialog.
     * @param title The title of the alert dialog.
     * @param message The content message of the alert dialog.
     */
    public static void showInformationAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
