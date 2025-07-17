package utils; // Ensure this package declaration is correct

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;

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

    /**
     * Displays a success alert dialog.
     * @param title The title of the alert dialog.
     * @param message The content message of the alert dialog.
     */
    public static void showSuccessAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION); // Often uses INFORMATION type
        alert.setTitle(title);
        alert.setHeaderText("Thành công");
        alert.setContentText(message);
        alert.getDialogPane().setStyle("-fx-border-color: green; -fx-border-width: 2px;");
        alert.showAndWait();
    }

    public static boolean showConfirmationAlert(String title, String message) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}
