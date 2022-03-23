package RR;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.util.List;

public class Controller {

    @FXML
    private HBox loginBox;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private HBox messageBox;
    @FXML
    private TextArea msgArea;
    @FXML
    private TextField msgField;
    @FXML
    private ListView<String> clientList;

    public TextArea getMsgArea() {
        return msgArea;
    }

    private final Client client;


    public Controller() {
        client = new Client(this);
        client.openConnection();
    }


    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void btnAuthClick(ActionEvent actionEvent) {
        client.sendMessage("/auth " + loginField.getText() + " " + passwordField.getText());
    }

    public void clickButton(ActionEvent actionEvent) {
        final String message = msgField.getText().trim();
        if (message.isEmpty()) {
            return;
        }
        client.sendMessage(message);
        msgField.clear();
        msgField.requestFocus();
    }

    public void selectClient(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            final String message = msgField.getText();
            final String nick = clientList.getSelectionModel().getSelectedItem();
            msgField.setText("/w " + nick + " " + message);
            msgField.requestFocus();
            msgField.selectEnd();
        }
    }

    public void addMsg(String msg) {
        msgArea.appendText(msg + "\n");
    }

    public boolean setAuth(boolean off) {
        loginBox.setVisible(!off);
        messageBox.setVisible(off);
        return off;
    }

    public void updateClientList(List<String> clients) {
        clientList.getItems().clear();
        clientList.getItems().addAll(clients);
    }


}
