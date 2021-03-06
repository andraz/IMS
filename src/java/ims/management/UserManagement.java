/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ims.management;

import ims.entity.Image;
import ims.entity.Role;
import ims.entity.User;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *This class provides methods to accessing users data into database.
 * @author andrazhribernik
 */
public class UserManagement {
   
    /*
     * Create class ImageManagement
     * 
     **/
    private static EntityManager em;
    public UserManagement(){
        //System.out.println(emf);
        em = Persistence.createEntityManagerFactory("web-jpaPU").createEntityManager();
    }
    
    /**
     * This method return User who has the same username as is given in parameter username.
     * @param username This parameter defines search query for particular user.
     * @return This method return User, which has the same username as parameter username.
     * @throws Exception This exception is thrown when user with that username
     * does not exists.
     */
    public User getUserByUsername(String username) throws Exception{
        em.getEntityManagerFactory().getCache().evictAll();
        List<User> images = (List<User>) em.createNamedQuery("User.findByUsername").setParameter("username", username).getResultList();
        if(images.size() == 0){
            throw new Exception("User with that particular username does not exist.");
        }
        return images.get(0);
        
    }
    /**
     * This method return Set of images that specified user has already bought. 
     * User is defined by username.
     * @param username This parameter defines user.
     * @return Set of objects Image, which belong to specified user.
     * @throws Exception This exception is thrown if user does not exist.
     */
    public Set<Image> getUserImages(String username) throws Exception{
        User user = getUserByUsername(username);
        return user.getImageSet();
    }
    
    /**
     * This method return Set of images that specified user has already bought. 
     * @param user This parameter defines user.
     * @return Set of objects Image, which belong to specified user.
     * @throws Exception This exception is thrown if user does not exist.
     */
    public Set<Image> getUserImages(User user) throws Exception{
        return user.getImageSet();
    }
    
    /**
     * This method return Set of images that specified user uploaded on portal. 
     * @param user This parameter defines user.
     * @return Set of objects Image, which belong to specified user.
     * @throws Exception This exception is thrown if user does not exist.
     */
    public Set<Image> getUserUploadedImages(User user) throws Exception{
        return user.getImageSet1();
    }
    
    /**
     * This method return User with specified id.
     * @param id This parameter defines user.
     * @return User which has the same id as specified in parameter.
     */
    public User getUserById(int id){
        return em.find(User.class, id);
    }
    
    /**
     * Add new user into a database.
     * @param username new user username
     * @param password new user password
     */
    public void addUser(String username, String password){
        em.getEntityManagerFactory().getCache().evictAll();
        Role userRole = (Role) em.createNamedQuery("Role.findByName").setParameter("name", "user").getResultList().get(0);
        em.getTransaction().begin();
        try{
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setRoleidRole(userRole);
            em.persist(newUser);
            em.getTransaction().commit();
        }
        catch(Exception e){
            em.getTransaction().rollback();
        }
        
    }
    /**
     * Add new user into a database.
     * @param username new user username
     * @param password new user password
     * @param role new user role defined with integer. 1 for user, 3 for contributor
     */
    public void addUser(String username, String password, Integer role){
        em.getEntityManagerFactory().getCache().evictAll();
        Role userRole = (Role) em.createNamedQuery("Role.findByIdRole").setParameter("idRole", role).getResultList().get(0);
        em.getTransaction().begin();
        try{
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setRoleidRole(userRole);
            em.persist(newUser);
            em.getTransaction().commit();
        }
        catch(Exception e){
            em.getTransaction().rollback();
        }
        
    }
    
    /**
     * Update specified user into database
     * @param u specified user with updated properties
     */
    public void editUser(User u){
        em.getEntityManagerFactory().getCache().evictAll();
        em.getTransaction().begin();
        try{
            em.persist(u);
            em.getTransaction().commit();
        }
        catch(Exception e){
            em.getTransaction().rollback();
        }
    }
    
    /**
     * Return user role with role id
     * @param id role id
     * @return user role
     */
    public Role getRoleById(Integer id){
        Role userRole = (Role) em.createNamedQuery("Role.findByIdRole").setParameter("idRole", id).getResultList().get(0);
        return userRole;
    }
    
    /**
     * Return list of all users except users who are administrators.
     * @return List of users
     */
    public List<User> getAllUsersExceptAdmin(){
        em.getEntityManagerFactory().getCache().evictAll();
        List<User> users = (List<User>) em.createNamedQuery("User.findAllExceptAdmin").getResultList();
        return users;
    }
    
}
