package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

public class CustomerDaoJPAImpl implements CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(Customer customer) {
        entityManager.persist(customer);
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        Query query = entityManager.createQuery("select customer from Custumer as customer where customer.customerId = :customerId");
        query.setParameter("customerId", customerId);
        return (Customer) query.getSingleResult();
    }

    @Override
    public List<Customer> getByName(String name) {
        Query query = entityManager.createQuery("select customer from Custumer as customer where customer.name = :name");
        query.setParameter("name", name);
        return (List<Customer>) query.getResultList();
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        entityManager.merge(customerToUpdate);
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        entityManager.remove(oldCustomer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        Query query = entityManager.createQuery("select customer from Custumer as customer ");
        return (List<Customer>) query.getResultList();
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        Query query = entityManager.createQuery("SELECT c FROM Customer c LEFT JOIN FETCH c.calls as calls WHERE c.customerId = :customerId");
        query.setParameter("customerId", customerId);
        return (Customer) query.getSingleResult();
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {
        entityManager.find(Customer.class, customerId).addCall(newCall);
    }
}
