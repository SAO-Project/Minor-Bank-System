import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.ArrayList;


/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/1/20
 *
 * Contains all bank data.
 */
public class BankCustomerData implements IBankDB{
    private HashMap<String, Customer> nameToCustomer;
    private HashMap<String, Customer> idToCustomer;
    private HashMap<Integer, Customer> accountNumberToCustomer;
    private HashMap<Integer, Account> accountNumberToAccount;
    private HashMap<Customer, ArrayList<String>> transactions;

    /**
     * Creates a object to contain all bank data.
     *
     * @param nameToCustomer "first_name last_name" -> customer
     * @param idToCustomer Customer id to customer object.
     */
    public BankCustomerData(HashMap<String, Customer> nameToCustomer,
                            HashMap<String, Customer> idToCustomer) {
        this.nameToCustomer = nameToCustomer;
        this.idToCustomer = idToCustomer;
        createAccountNumberToCustomerHashMap(this.nameToCustomer);
    }

    @Override
    /**
     * Assumes customer is valid.
     * Will not add.
     */
    public void addCustomer(Customer customer) {
        if (!containsCustomer(Customer.getHashName(customer))) {
            // TODO(Alex): Should we throw here if we try to add a customer
            //  that's already present. Its an edge case...
            return;
        }
        this.nameToCustomer.put(Customer.getHashName(customer), customer);
        this.idToCustomer.put(customer.getId(), customer);
        addCustomerToAccountNumberToCustomerMap(customer);
    }

    @Override
    public Collection<Customer> getCustomers() {
        return idToCustomer.values();
    }

    @Override
    public boolean containsCustomer(String hashName) {
        return this.nameToCustomer.containsKey(hashName);
    }

    @Override
    public Optional<Customer> getCustomer(String fullName) {
        if (nameToCustomer.containsKey(fullName)) {
            return Optional.of(idToCustomer.get(fullName));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Customer> getCustomerId(String id) {
        if (idToCustomer.containsKey(id)) {
            return Optional.of(idToCustomer.get(id));
        }
        return Optional.empty();
    }

    @Override
    public boolean containsChecking(int accountNumber) {
        return getAccount(accountNumber).isPresent();
    }

    @Override
    public boolean containsSavings(int accountNumber) {
        return getAccount(accountNumber).isPresent();
    }

    @Override
    public boolean containsCredit(int accountNumber) {
        return getAccount(accountNumber).isPresent();
    }

    @Override
    public Optional<Account> getAccount(int accountNumber) {
        if (accountNumberToAccount.containsKey(accountNumber)) {
            return Optional.of(accountNumberToAccount.get(accountNumber));
        }
        return Optional.empty();
    }

    @Override
    public void addTransaction(Transaction transaction) {
    }

    @Override
    public Collection<Transaction> getTransactions() {
        return null;
    }

    @Override
    public Collection<Transaction> getTransactions(Customer customer) {
        return null;
    }

    @Override
    public BankStatement getBankStatement(Customer customer) {
        return null;
    }

    /**
     * Creates class attribute accountNumberToCustomer.
     *
     * Adds all customers to upon object initialization.
     * @param idToCustomer Uses data to populate map.
     */
    public void createAccountNumberToCustomerHashMap(
            HashMap<String, Customer> idToCustomer) {
        this.accountNumberToCustomer = new HashMap<>();
        this.accountNumberToAccount = new HashMap<>();

        // Adds account number to map if account active...
        for (Customer customer : idToCustomer.values()) {
            addCustomerToAccountNumberToCustomerMap(customer);
        }
    }

    /**
     * Used to add customer to accountNumberToCustomerMap class attribute.
     *
     * @param customer Customer to add.
     */
    public void addCustomerToAccountNumberToCustomerMap(Customer customer) {
        if (customer.getChecking().getIsActive()) {
            this.accountNumberToCustomer.put(
                    customer.getChecking().getNumber(), customer);
            this.accountNumberToAccount.put(
                    customer.getChecking().getNumber(), customer.getChecking());
        }

        if (customer.getSavings().getIsActive()) {
            this.accountNumberToCustomer.put(
                    customer.getSavings().getNumber(), customer);
            this.accountNumberToAccount.put(
                    customer.getSavings().getNumber(), customer.getSavings());
        }

        if (customer.getCredit().getIsActive()) {
            this.accountNumberToCustomer.put(
                    customer.getCredit().getNumber(), customer);
            this.accountNumberToAccount.put(
                    customer.getCredit().getNumber(), customer.getCredit());
        }
    }
}
