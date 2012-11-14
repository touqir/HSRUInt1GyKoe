package application.view.mainView;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import net.miginfocom.swing.MigLayout;
import application.controller.LoanDetailController;
import application.core.Repository;
import application.core.Texts;
import application.view.component.JNumberTextField;
import application.view.helper.CustomerComboBoxRenderer;
import application.view.helper.HideTextOnFocusListener;
import application.viewModel.LoanDetailTableModel;

import com.jgoodies.validation.ValidationResult;

import domain.Copy;
import domain.Customer;
import domain.Loan;

public class LoanDetailMainViewBase extends MainViewBase<Loan, LoanDetailController> {

    private static String defaultSearchValue;

    private JPanel panel;
    private JPanel panel_2;
    private JPanel panel_3;
    private JLabel lblSearch;
    private JLabel lblCopyId;
    private JLabel lblCustomer;
    private JLabel lblCondition;
    private JLabel lblCopyTitle;
    private JLabel lblLoanStatus;
    private JLabel lblNumberOfLoans;
    private JLabel lblNumberOfLoansNumber;
    private JTable tblLoans;
    private JButton btnCreateLoan;
    private JTextField txtSearch;
    private JTextField txtCondition;
    private JTextField txtCopyDescription;
    private JNumberTextField txtCopyId;
    private JComboBox<Customer> cbCustomer;

    private HideTextOnFocusListener hideTextOnFocusListener;
    private LoanDetailTableModel loanDetailTableModel;

    public LoanDetailMainViewBase(Loan loan) {
        super(loan);
    }

    @Override
    protected void initModel() {
        super.initModel();
    }

    @Override
    protected void setTexts() {
        // title
        getContainer().setTitle(Texts.get("BookDetailMainView.this.title")); //$NON-NLS-1$

        defaultSearchValue = Texts.get("LoanDetailMainViewBase.defaultSearchValue");
        if (hideTextOnFocusListener != null) {
            hideTextOnFocusListener.updateText(defaultSearchValue);
        }

        // border of panels
        updateLoansPanelTitle();
        panel_2.setBorder(new TitledBorder(null, Texts.get("LoanDetailMainViewBase.newLoan.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBorder(new TitledBorder(null, Texts.get("LoanDetailMainViewBase.customerSelection.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));

        // components
        lblCopyTitle.setText(Texts.get("LoanDetailMainViewBase.newLoan.copyDescriptionLabel"));
        lblNumberOfLoans.setText(Texts.get("LoanDetailMainViewBase.loansOverview.numberOfLoans"));
        lblCopyId.setText(Texts.get("LoanDetailMainViewBase.newLoan.copyId"));
        btnCreateLoan.setText(Texts.get("LoanDetailMainViewBase.newLoan.lendCopyButton"));
        lblCustomer.setText(Texts.get("LoanDetailMainViewBase.customerSelection.customerLabel"));
        lblSearch.setText(Texts.get("LoanDetailMainViewBase.customerSelection.searchLabel"));
        lblCondition.setText(Texts.get("LoanDetailMainViewBase.newLoan.copyCondition"));
    }

    private void updateLoansPanelTitle() {
        String overViewTitle;
        if (cbCustomer.getSelectedItem() instanceof Customer) {
            String customerName = getCurrentCustomerSelection().getFullName();
            overViewTitle = Texts.get("LoanDetailMainViewBase.loansOverview.title") + " " + customerName;
        } else {
            overViewTitle = Texts.get("LoanDetailMainViewBase.loansOverview.titleAlone");
        }
        panel_3.setBorder(new TitledBorder(null, overViewTitle, TitledBorder.LEADING, TitledBorder.TOP, null, null));
    }

    /**
     * Initialize the contents of the frame.
     * 
     * @wbp.parser.entryPoint
     */
    @Override
    protected void initUIElements() {
        super.initUIElements();
        getContainer().setBounds(100, 100, 600, 500);
        getContainer().setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        getContainer().setModalityType(ModalityType.APPLICATION_MODAL);
        Container contentPane = getContainer().getContentPane();

        JPanel mainPanel = new JPanel();
        contentPane.add(mainPanel, BorderLayout.CENTER);
        mainPanel.setLayout(new MigLayout("", "[grow]", "[74.00][top][grow]"));

        createCustomerSelectionSection(mainPanel);

        createNewLoanSection(mainPanel);

        createLoanOverviewSection(mainPanel);

    }

    private void createCustomerSelectionSection(JPanel panel_4) {
        panel = new JPanel();
        panel_4.add(panel, "cell 0 0,growx,aligny top");
        panel.setLayout(new MigLayout("", "[][193.00][grow]", "[grow][grow]"));

        lblSearch = new JLabel();
        panel.add(lblSearch, "cell 0 0,alignx trailing");

        txtSearch = new JTextField();
        panel.add(txtSearch, "cell 1 0,growx");
        txtSearch.setColumns(10);
        txtSearch.setToolTipText(defaultSearchValue);

        lblCustomer = new JLabel();
        panel.add(lblCustomer, "cell 0 1,alignx trailing");

        cbCustomer = new JComboBox<Customer>(Repository.getInstance().getCutomerPMod().getCustomerComboBoxModel());
        cbCustomer.setRenderer(new CustomerComboBoxRenderer());
        panel.add(cbCustomer, "cell 1 1 2 1,growx");
    }

    private void createLoanOverviewSection(JPanel panel_4) {
        panel_3 = new JPanel();
        panel_4.add(panel_3, "cell 0 2,grow");
        panel_3.setLayout(new MigLayout("", "[][grow]", "[grow][grow]"));

        lblNumberOfLoans = new JLabel();
        panel_3.add(lblNumberOfLoans, "cell 0 0");

        lblNumberOfLoansNumber = new JLabel("1");
        panel_3.add(lblNumberOfLoansNumber, "cell 1 0");

        loanDetailTableModel = new LoanDetailTableModel(null);
        tblLoans = new JTable(loanDetailTableModel);
        Object selectedItem = cbCustomer.getSelectedItem();
        if (selectedItem instanceof Customer) {
            loanDetailTableModel.updateLoans((Customer) selectedItem);
        }
        updateLoanTable();
        panel_3.add(new JScrollPane(tblLoans), "cell 0 1 2 1,grow");
    }

    private void createNewLoanSection(JPanel panel_4) {
        panel_2 = new JPanel();
        panel_4.add(panel_2, "cell 0 1,growx,aligny top");
        panel_2.setLayout(new MigLayout("", "[][][][grow][][]", "[][]"));

        lblCopyId = new JLabel();
        panel_2.add(lblCopyId, "cell 0 0,alignx left");

        txtCopyId = new JNumberTextField();
        panel_2.add(txtCopyId, "cell 1 0");
        txtCopyId.setColumns(10);

        lblCopyTitle = new JLabel();
        panel_2.add(lblCopyTitle, "cell 2 0,alignx trailing");

        txtCopyDescription = new JTextField();
        txtCopyDescription.setEditable(false);
        txtCopyDescription.setColumns(5);
        // txtCopyDescription.setEnabled(false);
        panel_2.add(txtCopyDescription, "flowx,cell 3 0,growx");

        lblCondition = new JLabel();
        panel_2.add(lblCondition, "cell 4 0");

        txtCondition = new JTextField();
        txtCondition.setEditable(false);
        txtCondition.setColumns(8);
        panel_2.add(txtCondition, "cell 5 0,growx");

        btnCreateLoan = new JButton();
        panel_2.add(btnCreateLoan, "cell 0 1");

        lblLoanStatus = new JLabel();
        panel_2.add(lblLoanStatus, "cell 1 1 5,alignx left");
    }

    private void updateLoanTable() {
        Customer customer = null;
        if (cbCustomer.getSelectedItem() instanceof Customer) {
            customer = getCurrentCustomerSelection();
        }
        loanDetailTableModel.updateLoans(customer);
    }

    private Customer getCurrentCustomerSelection() {
        return (Customer) cbCustomer.getSelectedItem();
    }

    @Override
    protected LoanDetailController initController() {
        return new LoanDetailController();

    }

    @Override
    protected void initListeners() {
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            private void search() {
                if (txtSearch.getText().equals(defaultSearchValue)) {
                    getController().filterCustomers("");
                } else {
                    getController().filterCustomers(txtSearch.getText());
                }
            }
        });
        hideTextOnFocusListener = new HideTextOnFocusListener(txtSearch, defaultSearchValue);

        Repository.getInstance().getCutomerPMod().getCustomerComboBoxModel().addListDataListener(new ListDataListener() {

            @Override
            public void intervalRemoved(ListDataEvent arg0) {
            }

            @Override
            public void intervalAdded(ListDataEvent arg0) {
            }

            @Override
            public void contentsChanged(ListDataEvent arg0) {
                // call later, cause no mutation allowed in Listener
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        updateLoanTable();
                        setTexts();
                    }
                });
            }
        });

        cbCustomer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                updateLoanTable();
                setTexts();
            }

        });

        btnCreateLoan.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveLoan();
            }

        });

        txtCopyId.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchCopy(txtCopyId.getNumber());
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                searchCopy(txtCopyId.getNumber());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                searchCopy(txtCopyId.getNumber());
            }

            private void searchCopy(Long copyId) {
                if (copyId != null) {
                    updateCopy(getController().searchCopy(copyId));
                } else {
                    updateCopy(null);
                }
            }

        });

    }

    private void updateCopy(Copy searchCopy) {
        if (searchCopy == null) {
            txtCopyDescription.setText("");
            txtCondition.setText("");
        } else {
            String titleName = searchCopy.getTitle().getName();
            txtCopyDescription.setText(titleName);
            // set it as title as well in case the content is too long
            txtCopyDescription.setToolTipText(titleName);
            txtCondition.setText(searchCopy.getCondition().name());
            lblLoanStatus.setText("");
        }
    }

    private void saveLoan() {

        Long copyId = txtCopyId.getNumber();

        if (copyId == null) {
            lblLoanStatus.setText(Texts.get("LoanDetailMainViewBase.newLoan.noInventoryNumber"));
        } else {
            ValidationResult validationResult = getController().validateLoan(copyId, getCurrentCustomerSelection());
            if (validationResult.hasErrors()) {
                lblLoanStatus.setText(validationResult.getMessagesText());
            } else {
                lblLoanStatus.setText(Texts.get("LoanDetailMainViewBase.newLoan.loanSaved"));
                txtCopyId.setText("");
                updateCopy(null);
                Loan loan = getController().saveLoan(copyId, getCurrentCustomerSelection());
                loanDetailTableModel.addLoan(loan);
            }
        }
    }
}
