package com.yrgo.dataaccess;

import com.yrgo.domain.Action;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class ActionDaoJPAImpl implements ActionDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(Action newAction) {
        entityManager.persist(newAction);
    }

    @Override
    public List<Action> getIncompleteActions(String userId) {

        Query query = entityManager.createQuery("select action from Action as action where action.owningUser = :owningUser and action.complete = false");
        query.setParameter("owningUser", userId);
        return (List<Action>) query.getResultList();
    }

    @Override
    public void update(Action actionToUpdate) throws RecordNotFoundException {
        entityManager.merge(actionToUpdate);
    }

    @Override
    public void delete(Action oldAction) throws RecordNotFoundException {
        entityManager.remove(oldAction);
    }
}
