package com.yrgo.dataaccess;

import com.yrgo.domain.Call;
import com.yrgo.domain.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class CustomerDaoJDBCTemplateImpl implements CustomerDao {

    private JdbcTemplate template;

    public CustomerDaoJDBCTemplateImpl(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void create(Customer customer) {
        template.update("insert into CUSTOMER (CUSTOMER_ID, COMPANY_NAME, EMAIL, TELEPHONE, NOTES) VALUES (?,?,?,?,?)",
                customer.getCustomerId(),
                customer.getCompanyName(),
                customer.getEmail(),
                customer.getTelephone(),
                customer.getNotes());
    }

    @Override
    public Customer getById(String customerId) throws RecordNotFoundException {
        return template.queryForObject("select * from CUSTOMER where CUSTOMER_ID = ?", Customer.class, customerId);
    }

    @Override
    public List<Customer> getByName(String name) {
        return template.query("select * from CUSTOMER where CUSTOMER_NAME = ?", new CustomerRowMapper(), name);
    }

    @Override
    public void update(Customer customerToUpdate) throws RecordNotFoundException {
        template.update("update CUSTOMER SET COMPANY_NAME=?, EMAIL=?, TELEPHONE=?, NOTES=? \n" +
                "                WHERE CUSTOMER_ID = ?", customerToUpdate.getCompanyName(),
                customerToUpdate.getEmail(),
                customerToUpdate.getTelephone(),
                customerToUpdate.getNotes(),
                customerToUpdate.getCustomerId());
    }

    @Override
    public void delete(Customer oldCustomer) throws RecordNotFoundException {
        template.update("delete from CUSTOMER where CUSTOMER_ID = ?", oldCustomer.getCustomerId());
    }

    @Override
    public List<Customer> getAllCustomers() {

        return template.query("select * from CUSTOMER", new CustomerRowMapper());
    }

    @Override
    public Customer getFullCustomerDetail(String customerId) throws RecordNotFoundException {
        Customer customer = getById(customerId);
        List<Call> callList = template.query("select * from TBL_CALL where CUSTOMER_ID = ?", new CallRowMapper(), customerId);
        customer.setCalls(callList);
        return customer;
    }

    @Override
    public void addCall(Call newCall, String customerId) throws RecordNotFoundException {

        template.update("INSERT INTO TBL_CALL(NOTES, TIME_AND_DATE, CUSTOMER_ID) VALUES (?, ?, ?)", newCall.getNotes(), newCall.getTimeAndDate(), customerId);

    }

    private class CustomerRowMapper implements RowMapper<Customer> {

        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            String id = rs.getString(1);
            String companyName = rs.getString(2);
            String email = rs.getString(3);
            String telephone = rs.getString(4);
            String notes = rs.getString(5);
            return new Customer(id, companyName, email, telephone, notes);
        }
    }

    private class CallRowMapper implements RowMapper<Call> {

        @Override
        public Call mapRow(ResultSet rs, int rowNum) throws SQLException {
            String notes = rs.getString(1);
            Date timeAndDate = rs.getDate(2);
            return new Call(notes, timeAndDate);
        }
    }

    public void createTable() {

        String CREATE_TABLE_CUSTOMER = "CREATE TABLE CUSTOMER( CUSTOMER_ID VARCHAR(20) PRIMARY KEY ,COMPANY_NAME VARCHAR(50), EMAIL VARCHAR(50), TELEPHONE VARCHAR(20), NOTES VARCHAR(255)) ";

        String CREATE_TABLE_CALL = "CREATE TABLE TBL_CALL( NOTES VARCHAR(255), TIME_AND_DATE DATE, CUSTOMER_ID VARCHAR(20) REFERENCES CUSTOMER(CUSTOMER_ID)) ";


        this.template.execute(CREATE_TABLE_CUSTOMER);
        this.template.execute(CREATE_TABLE_CALL);

    }
}
