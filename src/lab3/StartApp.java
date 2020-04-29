package lab3;
import lab3.view.UI;

/**
 * Main class where program starts.
 */
public class StartApp {

    /**
     * Start point of the application
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        UI newUi = new UI();
        newUi.displayUI();
    }
}
