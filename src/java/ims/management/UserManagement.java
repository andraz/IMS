/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ims.management;

import ims.entity.Image;
import ims.entity.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;

/**
 *
 * @author andrazhribernik
 */
public class UserManagement {
   
    private EntityManager em;
    /*
     * Create class ImageManagement
     * 
     **/
    public UserManagement(){
        //System.out.println(emf);
        em = Persistence.createEntityManagerFactory("web-jpaPU").createEntityManager();
    }
    
    public User getUserByUsername(String username) throws Exception{
        
        List<User> images = (List<User>) em.createNamedQuery("User.findByUsername").setParameter("username", username).getResultList();
        if(images.size() == 0){
            throw new Exception("User with that particular username does not exist.");
        }
        return images.get(0);
        
    }
    
    public Set<Image> getUserImages(String username) throws Exception{
        User user = getUserByUsername(username);
        return user.getImageSet();
    }
    
}