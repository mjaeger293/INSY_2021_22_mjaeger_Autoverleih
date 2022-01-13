package it.htl.steyr.autoverleih;

import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedPublisher;
import it.htl.steyr.autoverleih.interfaces.IDialogConfirmedSubscriber;
import it.htl.steyr.autoverleih.model.Customer;
import it.htl.steyr.autoverleih.model.repositories.CustomerRepository;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class NewCustomerController implements IDialogConfirmedPublisher {

    CustomerController customerController;

    public TextField firstnameTextField;
    public TextField lastnameTextField;
    public TextField emailTextField;
    public TextField streetTextField;
    public TextField zipTextField;
    public TextField cityTextField;
    public TextField ibanTextField;

    @Autowired
    CustomerRepository customerRepository;


    public void saveClicked(ActionEvent actionEvent) {
        String firstname = firstnameTextField.getText();
        String lastname = lastnameTextField.getText();
        String email = emailTextField.getText();
        String street = streetTextField.getText();
        String zipString = zipTextField.getText();
        String city = cityTextField.getText();
        String iban = ibanTextField.getText();

        if (!firstname.equals("") && !lastname.equals("") &&
                emailFormatValid(email) && !street.equals("") && zipCodeValid(zipString) &&
                !city.equals("") && ibanValid(iban))
        {
            Customer newCustomer = new Customer(lastname, firstname, email, street,
                    Integer.parseInt(zipString), city, iban);

            customerRepository.save(newCustomer);

            // Update Table View
            customerController.windowConfirmed();

            closeWindow(actionEvent);
        }
    }

    public void cancelClicked(ActionEvent actionEvent) {
        closeWindow(actionEvent);
    }

    private void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void addSubscriber(IDialogConfirmedSubscriber sub) {
        customerController = (CustomerController) sub;
    }


    /**
     * Check if email is valid
     *
     * @param email
     * @return
     */
    private boolean emailFormatValid(String email) {
        // Dieses Pattern entspricht RFC 5322, welches u.a. das Format einer validen Email-Adresse angibt
        Pattern pattern = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

        return pattern.matcher(email).matches();
    }

    /**
     * Check if zip code is valid
     *
     * @param zipString
     * @return
     */
    private static boolean zipCodeValid(String zipString) {
        // Die maximale Anzahl von Ziffern in einer Postleitzahl (weltweit) ist 10. Das Minimum ist 3.
        Pattern pattern = Pattern.compile("\\d{3,10}");

        return pattern.matcher(zipString).matches();
    }

    /**
     * Check if iban is valid
     *
     * @param iban
     * @return
     */
    private static boolean ibanValid(String iban) {
        // Remove whitespaces
        iban.replace(" ", "");

        Pattern pattern = Pattern.compile("AT\\d{18}");

        return pattern.matcher(iban).matches();
    }
}
