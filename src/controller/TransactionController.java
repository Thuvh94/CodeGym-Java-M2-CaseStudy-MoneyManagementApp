package controller;

import iservice.IncomeTypeManage;
import iservice.OutcomeTypeManage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.IncomeType;
import model.Money;
import model.OutcomeType;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {
   //  all transaction components
    @FXML
    private TableView<Money> allTransactionTable;
    @FXML
    private TableColumn<Money, LocalDate> transactionDateColumn;
    @FXML
    private TableColumn<Money, Long> transactionAmountColumn;
    @FXML
    private TableColumn<Money, String> transactionDetailColumn;
    @FXML
    private Label totalIncomeLabel;
    @FXML
    private Label totalOutcomeLabel;
    @FXML
    private Label realMoneyLabel;

    // edit transaction components
    @FXML
    private Label amountNoticeLabel;
    @FXML
    private Button saveTransactionBtn;
    @FXML
    private TextField amountText;
    @FXML
    private RadioButton incomeRadioBtn;
    @FXML
    private RadioButton outcomeRadioBtn;
    @FXML
    private ChoiceBox transactionGroup;
    @FXML
    private TextField transactionDescription;
    @FXML
    private DatePicker transactionDate;

    public static ObservableList<Money> transactionList = FXCollections.observableArrayList();
    private long totalIncome;
    private long totalOutcome;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    // Validate amount text field
    public void validateAmountTextField() {
        amountText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("^\\d+$") && !newValue.isEmpty()) {
                saveTransactionBtn.setDisable(true);
                amountNoticeLabel.setVisible(true);
            } else {
                saveTransactionBtn.setDisable(false);
                amountNoticeLabel.setVisible(false);
            }
        });
    }

    // When choose income or outcome, the dropdown list of transaction types are changed
    public void chooseIncomeTransaction(ActionEvent actionEvent) {
        // Khi chọn income thì các chi tiêu trong phần loại sẽ khác với outcome
        System.out.println("User choose income transaction");
        transactionGroup.getItems().clear();
        List<IncomeType> incomeTypes = new IncomeTypeManage().findAll();
        for (int i = 0; i < incomeTypes.size(); i++) {
            transactionGroup.getItems().add(incomeTypes.get(i));
        }

    }

    public void chooseOutcomeTransaction(ActionEvent actionEvent) {
        // Khi chọn outcome thì các chi tiêu trong phần loại sẽ khác với income
        System.out.println("User choose outcome transaction");
        transactionGroup.getItems().clear();
        List<OutcomeType> outcomeTypes = new OutcomeTypeManage().findAll();
        for (int i = 0; i < outcomeTypes.size(); i++) {
            transactionGroup.getItems().add(outcomeTypes.get(i));
        }
    }

    // Save button
    public void saveTransaction(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Money inputMoneyObj = getInputMoneyObj();
        transactionList.add(inputMoneyObj);
        setOverViewLabel(inputMoneyObj);
        addTransactionToTable(transactionList);
        System.out.println(transactionList);
        clearInputFields();
    }

    public Money getInputMoneyObj() {
        long inputAmount = Long.parseLong(amountText.getText());
        boolean isIncome = true;
        if (incomeRadioBtn.isSelected() && transactionGroup.getValue() != null)
            isIncome = true;
        else if (outcomeRadioBtn.isSelected() && transactionGroup.getValue() != null) {
            isIncome = false;
        }
        String inputDescription = transactionDescription.getText();
        LocalDate inputDate = transactionDate.getValue();
        Money inputMoneyObj = new Money(inputAmount, isIncome, inputDescription, inputDate);
        return inputMoneyObj;
    }

    public void clearInputFields() {
        amountText.clear();
        transactionDescription.clear();
        transactionDate.getEditor().clear();
        transactionGroup.getItems().clear();
        outcomeRadioBtn.setSelected(false);
        incomeRadioBtn.setSelected(false);
    }

    // Add transactions to table
    public void addTransactionToTable(ObservableList<Money> list){
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        transactionDetailColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        transactionAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        allTransactionTable.setItems(list);
    }

     //Get total income and outcome and display in label
    private void setOverViewLabel(Money moneyObj){
        if(moneyObj.isIncome()){
            setTotalIncomeLabel();
            setRealMoneyLabel();
        }
        else {
            setTotalOutcomeLabel();
            setRealMoneyLabel();
        }
    }
    public long getTotalIncome(){
        totalIncome += getInputMoneyObj().getAmount();
        return totalIncome;
    }

    public long getTotalOutcome(){
        totalOutcome += getInputMoneyObj().getAmount();
        return totalOutcome;
    }
    public long getRealMoney(){
        return (getTotalIncome() - getTotalOutcome());
    }

    private void setTotalIncomeLabel(){
        totalIncomeLabel.setText(String.valueOf(getTotalIncome()));
    }
    private void setTotalOutcomeLabel(){
        totalOutcomeLabel.setText(String.valueOf(getTotalOutcome()));
    }
    private void setRealMoneyLabel(){
        realMoneyLabel.setText(String.valueOf(getTotalIncome()-getTotalOutcome()));
    }
}
