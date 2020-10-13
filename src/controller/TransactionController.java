package controller;

import iservice.MoneyTypeManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import model.MoneyType;
import model.Money;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class TransactionController implements Initializable {
    //  all transaction components
    @FXML
    private TableView<Money> allTransactionTable;
    @FXML
    private TableColumn<Money, LocalDate> transactionDateColumn;
    @FXML
    private TableColumn<Money, Long> transactionAmountColumn;
    @FXML
    private TableColumn<Money, MoneyType> transactionMoneyTypeColumn;
    @FXML
    private TableColumn<Money, String> transactionDetailColumn;
    @FXML
    private Label totalIncomeLabel;
    @FXML
    private Label totalOutcomeLabel;
    @FXML
    private Label realMoneyLabel;
    @FXML
    private Button resetBtn;

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
    @FXML
    private TextField hiddenUUID;

    private static ObservableList<Money> transactionList;
    private long totalIncome = 0;
    private long totalOutcome = 0;
    private Money selectedTableTransaction;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //...
        //firstNameCol.setSortType(TableColumn.SortType.ASCENDING);
        //...
        //tableView.setItems(data);
        //tableView.getColumns().addAll(firstNameCol, lastNameCol, emailCol, ageCol);
        //tableView.getSortOrder().add(firstNameCol);
        //...
        transactionDateColumn.setSortType(TableColumn.SortType.DESCENDING);
        transactionList = FXCollections.observableArrayList(readFile());
        if (transactionList.size() == 0)
            allTransactionTable.setPlaceholder(new Label("Bạn chưa có giao dịch nào."));
        else{
            addTransactionToTable(transactionList);
            allTransactionTable.refresh();
        }
        amountText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                amountText.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        // disable future date in datepicker
        transactionDate.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.compareTo(LocalDate.now()) > 0);
            }
        });
        getOverviewNum();
        setLabel();
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
        System.out.println("User choose income transaction");
        transactionGroup.getItems().clear();
        List<MoneyType> incomeTypes = new MoneyTypeManagement().findIncomeTypeList();
        for (int i = 0; i < incomeTypes.size(); i++) {
            transactionGroup.getItems().add(incomeTypes.get(i));
        }

    }

    public void chooseOutcomeTransaction(ActionEvent actionEvent) {
        System.out.println("User choose outcome transaction");
        transactionGroup.getItems().clear();
        List<MoneyType> outcomeTypes = new MoneyTypeManagement().findOutcomeTypeList();
        for (int i = 0; i < outcomeTypes.size(); i++) {
            transactionGroup.getItems().add(outcomeTypes.get(i));
        }
    }


    // Save button
    public void saveTransaction(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if(hiddenUUID.getText().trim().isEmpty()){
            Money money = getInputMoneyObj();
            generateUUID(money);
            transactionList.add(money);
        }
        else{
            updateTransaction();
        }
        saveTransactionToTable();

    }
    public void generateUUID(Money money){
        UUID uuid = UUID.randomUUID();
        money.setUuid(uuid);
    }

    public void updateTransaction() {
        Money money = selectedTableTransaction;
        editTransaction(money);
        saveTransactionToTable();
    }
    public void saveTransactionToTable(){
        addTransactionToTable(transactionList);
        allTransactionTable.refresh();
        clearInputFields();
        System.out.println(transactionList);
        getOverviewNum();
        setLabel();
    }

    public Money getInputMoneyObj() {
        Money inputMoneyObj;
        long inputAmount = Long.parseLong(amountText.getText());
        boolean isIncome = true;
        if (incomeRadioBtn.isSelected() && transactionGroup.getValue() != null) {
            isIncome = true;
        } else if (outcomeRadioBtn.isSelected() && transactionGroup.getValue() != null) {
            isIncome = false;
        }
        MoneyType moneyType = (MoneyType) transactionGroup.getValue();
        String inputDescription = transactionDescription.getText();
        LocalDate inputDate = transactionDate.getValue();
        inputMoneyObj = new Money(inputAmount, isIncome, inputDescription, moneyType, inputDate);
        return inputMoneyObj;
    }

    public void clearInputFields() {
        amountText.clear();
        transactionDescription.clear();
        transactionDate.getEditor().clear();
        transactionGroup.getItems().clear();
        outcomeRadioBtn.setSelected(false);
        incomeRadioBtn.setSelected(false);
        hiddenUUID.clear();
    }

    // Add transactions to table
    public void addTransactionToTable(ObservableList<Money> list) {
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        transactionDetailColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        transactionMoneyTypeColumn.setCellValueFactory(new PropertyValueFactory<>("moneyType"));
        transactionAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        allTransactionTable.setItems(list);
    }

    private void getOverviewNum() {
        long income = 0;
        long outcome = 0;
        for (int i = 0; i < transactionList.size(); i++) {
            if (transactionList.get(i).isIncome())
                income += transactionList.get(i).getAmount();
            else
                outcome += transactionList.get(i).getAmount();
        }
        totalIncome = income;
        totalOutcome = outcome;
    }

    private void setLabel() {
        totalIncomeLabel.setText(String.valueOf(totalIncome));
        totalOutcomeLabel.setText(String.valueOf(totalOutcome));
        realMoneyLabel.setText(String.valueOf(totalIncome - totalOutcome));
    }

    // Action when user tap a cell in table
    public void confirmUserAction(Money selectedMoney) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thao tác");
        alert.setHeaderText(null);
        alert.setContentText("Lựa chọn thao tác bạn muốn thực hiện");

        ButtonType buttonEdit = new ButtonType("Chỉnh sửa");
        ButtonType buttonDelete = new ButtonType("Xóa");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonEdit, buttonDelete, buttonCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonEdit) {
            editActionSelected(selectedMoney);
        } else if (result.get() == buttonDelete) {
            confirmDeleteDialog(selectedMoney);
        } else {
            alert.close();
        }
    }
    public void getSelectedItem(MouseEvent click){
        if (click.getClickCount() == 2){
            selectedTableTransaction = allTransactionTable.getSelectionModel().getSelectedItem();
            confirmUserAction(selectedTableTransaction);
        }
    }

    public void confirmDeleteDialog(Money money) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thao tác");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn xóa giao dịch này?");

        ButtonType buttonYes = new ButtonType("Xóa");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonYes, buttonCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonYes) {
            deleteActionSelected(money);
        } else {
            alert.close();
        }
    }


    public void editActionSelected(Money Obj) {
        displayValueForEdit(Obj);
        System.out.println("old" + Obj);
    }

    public void editTransaction(Money Obj) {
        Obj.setAmount(Long.parseLong(amountText.getText()));
        Obj.setMoneyType((MoneyType) transactionGroup.getValue());
        Obj.setDescription(transactionDescription.getText());
        Obj.setDate(transactionDate.getValue());
        boolean isIncome = true;
        if (incomeRadioBtn.isSelected() && transactionGroup.getValue() != null) {
            isIncome = true;
        } else if (outcomeRadioBtn.isSelected() && transactionGroup.getValue() != null) {
            isIncome = false;
        }
        Obj.setIncome(isIncome);
        System.out.println("new obj" + Obj);
    }

    public void deleteActionSelected(Money Obj) {
        System.out.println("Delete");
        transactionList.remove(Obj);
        addTransactionToTable(transactionList);
        getOverviewNum();
        setLabel();
    }

    public void displayValueForEdit(Money Obj) {
        hiddenUUID.setText(Obj.getUuid());
        transactionGroup.setValue(Obj.getMoneyType());
        amountText.setText(String.valueOf(Obj.getAmount()));
        if (Obj.isIncome())
            incomeRadioBtn.setSelected(true);
        else
            outcomeRadioBtn.setSelected(true);
        transactionDescription.setText(Obj.getDescription());
        transactionDate.setValue(Obj.getDate());
    }

     //readFile and writeFile
    public void writeFile() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("transaction.dat"));
            for (Money money : transactionList) {
                objectOutputStream.writeObject(money);
            }
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("bye bye");

    }

    public List<Money> readFile() {
        List<Money> list = new ArrayList<>();
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("transaction.dat"));

            while (true) {
                list.add((Money) objectInputStream.readObject());
            }
        } catch (EOFException e) {
            e.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    // set font color for table
    public void setColorFont(Money Obj){
        transactionAmountColumn.setCellValueFactory(new PropertyValueFactory<Money,Long>("amount"));

        // ** The TableCell class has the method setTextFill(Paint p) that you
        // ** need to override the text color
        //   To obtain the TableCell we need to replace the Default CellFactory
        //   with one that returns a new TableCell instance,
        //   and @Override the updateItem(String item, boolean empty) method.
        //
//        transactionAmountColumn.setCellFactory(new Callback<TableColumn, TableCell>() {
//            public TableCell call(TableColumn param) {
//                return new TableCell<Money, String>() {
//
//                    @Override
//                    public void updateItem(String item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (Obj.isIncome()) {
//                            this.setTextFill(Color.BLUE);
//                        } else{
//                            this.setTextFill(Color.RED);
//                        }
//                            setText(item);
//                        }
//                    };
//                };
//            };
    }

    //reset all
    public void confirmReset(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận thao tác");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn thực hiện thao tác này?\n Mọi giao dịch của bạn sẽ bị xóa và không thể khôi phục.");

        ButtonType buttonYes = new ButtonType("Đồng ý");
        ButtonType buttonCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonYes, buttonCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonYes) {
            transactionList.clear();
            saveTransactionToTable();
        } else {
            alert.close();
        }
    }
}


